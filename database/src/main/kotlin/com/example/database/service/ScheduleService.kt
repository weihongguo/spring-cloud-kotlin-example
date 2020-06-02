package com.example.database.service

import com.example.database.BaseRepository
import com.example.database.BaseServiceImpl
import com.example.database.entity.ScheduleJob
import com.example.database.entity.ScheduleLog
import com.example.database.entity.ScheduleResultEnum
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @Author：GuoGuo
 * @Date 2020/6/2 14:29
 **/

interface ScheduleJobService {
    fun canRun(module: String, name: String, lockDuration: Long, lockBy: String): ScheduleJob?
}

@Service
class ScheduleJobServiceImpl : BaseServiceImpl<ScheduleJob>(), ScheduleJobService {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var scheduleJobRepository: ScheduleJobRepository

    override fun getRepository(): BaseRepository<ScheduleJob> {
        return scheduleJobRepository
    }

    override fun canRun(module: String, name: String, lockDuration: Long, lockBy: String): ScheduleJob? {
        val scheduleJob = scheduleJobRepository.findByModuleAndName(module, name)
        scheduleJob?.let {
            val now = Date()
            if (it.lockUntil == null) {
                it.lockTime = now
                it.lockUntil = Date(now.time + lockDuration)
                it.lockBy = lockBy
                if (scheduleJobRepository.lockByModuleAndName(it) != 1) {
                    return null
                }
            } else {
                if (scheduleJob.lockUntil!!.after(now)) {
                    return null
                }
                val oldLockTime = Date(it.lockTime!!.time)
                val oldLockUntil = Date(it.lockUntil!!.time)
                val oldLockBy = it.lockBy!!
                it.lockTime = now
                it.lockUntil = Date(now.time + lockDuration)
                it.lockBy = lockBy
                if (scheduleJobRepository.lockByModuleAndName(it, oldLockTime, oldLockUntil, oldLockBy) != 1) {
                    return null
                }
            }
            return scheduleJob
        }
        log.error("Can not find schedule jog ${module}:${name}")
        return null
    }
}

@Repository
interface ScheduleJobRepository : BaseRepository<ScheduleJob> {
    fun findByModuleAndName(mobile: String, name: String): ScheduleJob?

    @Modifying
    @Transactional(rollbackFor = [Exception::class])
    @Query(
        "update ScheduleJob" +
            " set updateTime = :#{#scheduleJob.lockTime}" +
            ",lockTime = :#{#scheduleJob.lockTime}" +
            ",lockUntil = :#{#scheduleJob.lockUntil}" +
            ",lockBy = :#{#scheduleJob.lockBy}" +
            " where module = :#{#scheduleJob.module}" +
            " and name = :#{#scheduleJob.name}" +
            " and lockTime is null" +
            " and lockUntil is null" +
            " and lockBy is null"
    )
    fun lockByModuleAndName(scheduleJob: ScheduleJob): Int

    @Modifying
    @Transactional(rollbackFor = [Exception::class])
    @Query(
        "update ScheduleJob" +
                " set updateTime = :#{#scheduleJob.lockTime}" +
                ",lockTime = :#{#scheduleJob.lockTime}" +
                ",lockUntil = :#{#scheduleJob.lockUntil}" +
                ",lockBy = :#{#scheduleJob.lockBy}" +
                " where module = :#{#scheduleJob.module}" +
                " and name = :#{#scheduleJob.name}" +
                " and lockTime = :oldLockTime" +
                " and lockUntil = :oldLockUntil" +
                " and lockBy = :oldLockBy"
    )
    fun lockByModuleAndName(scheduleJob: ScheduleJob, oldLockTime: Date, oldLockUntil: Date, oldLockBy: String): Int
}

interface ScheduleLogService {
    fun record(
        scheduleJob: ScheduleJob,
        startTime: Date,
        endTime: Date,
        result: ScheduleResultEnum,
        resultInfo: String?
    ): ScheduleLog
}

@Service
class ScheduleLogServiceImpl : BaseServiceImpl<ScheduleLog>(), ScheduleLogService {

    @Autowired
    lateinit var scheduleLogRepository: ScheduleLogRepository

    override fun getRepository(): BaseRepository<ScheduleLog> {
        return scheduleLogRepository
    }

    override fun record(
        scheduleJob: ScheduleJob,
        startTime: Date,
        endTime: Date,
        result: ScheduleResultEnum,
        resultInfo: String?
    ): ScheduleLog {
        val scheduleLog = ScheduleLog(
            scheduleJobId = scheduleJob.id!!,
            processBy = scheduleJob.lockBy!!,
            startTime = startTime,
            endTime = endTime,
            result = result.value,
            resultInfo = resultInfo
        )
        setForCreate(scheduleLog)
        return scheduleLogRepository.save(scheduleLog)
    }
}

@Repository
interface ScheduleLogRepository : BaseRepository<ScheduleLog>

