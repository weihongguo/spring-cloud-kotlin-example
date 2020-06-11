package com.example.base.mq

import com.alibaba.fastjson.JSON
import com.example.base.BaseRepository
import com.example.base.BaseService
import com.example.base.BaseServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageBuilder
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.rabbit.connection.CorrelationData
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

data class CustomMessage (
        var queue: String,
        var content: String,
        var authorization: String? = null
) {
    fun uuid(): String = "${queue}?content=$content&time=${System.currentTimeMillis()}"

    override fun toString(): String {
        return "CustomMessage [queue=$queue content=$content authorization=$authorization]"
    }
}

data class EntityMessage (
        var queue: String,
        var entityType: String = "",
        var entityId: Long = 0,
        var operate: String = "",
        var authorization: String? = null
) {

    fun uuid(): String = "${queue}?entityType=${entityType}&entityId=${entityId}&operate=${operate}&time=${System.currentTimeMillis()}"

    override fun toString(): String {
        return "EntityMessage [queue=$queue entityType=$entityType entityId=$entityId operate=$operate authorization=$authorization]"
    }
}

enum class EntityMessageOperateEnum (var value: String, var label: String) {
    CREATE("create", "创建"),
    UPDATE("update", "更新"),
    DELETE("delete", "删除")
}

@Service
final class MqService(private var rabbitTemplate: RabbitTemplate): RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    private val log = LoggerFactory.getLogger(javaClass)

    init {
        rabbitTemplate.setConfirmCallback(this)
        rabbitTemplate.setReturnCallback(this)
    }

    @Autowired
    lateinit var mqLogService: MqLogService

    fun send(queue: String, content: String, authorization: String? = null) {
        val customMessage = CustomMessage(
                queue = queue,
                content = content,
                authorization = authorization
        )
        this.send(customMessage)
    }

    fun send(customMessage: CustomMessage) {
        val json = JSON.toJSONString(customMessage)
        val uuid = customMessage.uuid()
        val message = MessageBuilder.withBody(json.toByteArray())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setCorrelationId(uuid)
                .build()
        val correlationData = CorrelationData(uuid)
        rabbitTemplate.convertAndSend(customMessage.queue, message, correlationData)
    }

    fun send(entityMessage: EntityMessage) {
        val json = JSON.toJSONString(entityMessage)
        val uuid = entityMessage.uuid()
        val message = MessageBuilder.withBody(json.toByteArray())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setCorrelationId(uuid)
                .build()
        val correlationData = CorrelationData(uuid)
        rabbitTemplate.convertAndSend(entityMessage.queue, message, correlationData)
    }

    override fun confirm(correlationData: CorrelationData?, ack: Boolean, cause: String?) {
        if (!ack) {
            val correlationId = correlationData?.id ?: "unknown"
            val reason = cause ?: "unknown"
            mqLogService.confirmFail(correlationId, reason)
        }
        log.info("### message queue confirm $correlationData ack=$ack cause=$cause ###")
    }

    override fun returnedMessage(message: Message, replyCode: Int, replyText: String, exchange: String, routingKey: String) {
        val reason = "returnedMessage [replyCode=$replyCode replyText=$replyText exchange=$exchange routingKey=$routingKey]"
        mqLogService.returnMessageFail(message.messageProperties.correlationId, String(message.body), reason)
    }
}

interface MqLogService : BaseService<MqLog> {
    fun confirmFail(correlationId: String, reason: String)
    fun returnMessageFail(correlationId: String, message: String, reason: String)
    fun processFail(queue: String, message: String, reason: String)
}

@Service
class MqLogServiceImpl : BaseServiceImpl<MqLog>(), MqLogService {

    @Autowired
    lateinit var mqLogRepository: MqLogRepository

    override fun getRepository(): BaseRepository<MqLog> {
        return mqLogRepository
    }

    override fun confirmFail(correlationId: String, reason: String) {
        val mqLog = MqLog(
                correlationId = correlationId,
                operate = MqLogOperateEnum.CONFIRM.value,
                result = MqLogResultEnum.FAIL.value,
                reason = reason
        )
        this.save(mqLog)
    }

    override fun returnMessageFail(correlationId: String, message: String, reason: String) {
        val mqLog = MqLog(
                correlationId = correlationId,
                message = message,
                operate = MqLogOperateEnum.RETURNED_MESSAGE.value,
                result = MqLogResultEnum.FAIL.value,
                reason = reason
        )
        this.save(mqLog)
    }

    override fun processFail(queue: String, message: String, reason: String) {
        val mqLog = MqLog(
                queue = queue,
                message = message,
                operate = MqLogOperateEnum.PROCESS.value,
                result = MqLogResultEnum.FAIL.value,
                reason = reason
        )
        this.save(mqLog)
    }
}

@Repository
interface MqLogRepository : BaseRepository<MqLog>