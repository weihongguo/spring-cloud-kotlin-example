package com.example.database.service

import com.alibaba.fastjson.JSON
import com.example.database.entity.MqFailLog
import com.example.database.entity.MqFailLogOperateEnum
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageBuilder
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.rabbit.connection.CorrelationData
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.net.URLEncoder

/**
 * @Author：GuoGuo
 * @Date 2020/6/9 14:39
 **/

data class CustomMessage (
    var queue: String,
    var content: String,
    var authorization: String? = null
) {
    fun uuid(): String = "${queue}?content=${URLEncoder.encode(content, "utf-8")}&time=${System.currentTimeMillis()}"

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

    fun uuid(): String = "${queue}?entityType=${entityType}&entityId=${entityId}&operate=${operate}"

    override fun toString(): String {
        return "EntityMessage [queue=$queue entityType=$entityType entityId=$entityId operate=$operate authorization= $authorization]"
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
    lateinit var mqFailLogService: MqFailLogService

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
            val correlationId = correlationData?.id
            val reason = cause ?: "undefined"
            val mqFailLog = MqFailLog(
                correlationId = correlationId,
                operate = MqFailLogOperateEnum.CONFIRM.value,
                reason = reason
            )
            mqFailLogService.save(mqFailLog)
        }
        log.info("### confirm $ack $cause ${correlationData?.id} ###")
    }

    override fun returnedMessage(message: Message, replyCode: Int, replyText: String, exchange: String, routingKey: String) {
        val reason = "$replyCode#:#$replyText#:#$exchange#:#$routingKey"
        val mqFailLog = MqFailLog(
            message = String(message.body),
            operate = MqFailLogOperateEnum.RETURNED_MESSAGE.value,
            reason = reason
        )
        mqFailLogService.save(mqFailLog)
    }
}

interface MqFailLogService : BaseService<MqFailLog>

@Service
class MqFailLogServiceImpl : BaseServiceImpl<MqFailLog>(), MqFailLogService {

    @Autowired
    lateinit var mqFailLogRepository: MqFailLogRepository

    override fun getRepository(): BaseRepository<MqFailLog> {
        return mqFailLogRepository
    }
}

@Repository
interface MqFailLogRepository : BaseRepository<MqFailLog>