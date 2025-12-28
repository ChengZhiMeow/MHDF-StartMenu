package cn.chengzhimeow.mhdfstartmenu.thread

import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit

abstract class CancellableRunnable : Runnable {
    internal lateinit var future: ScheduledFuture<*>

    fun cancel() {
        future.cancel(false)
    }
}

abstract class Thread protected constructor(
    private val id: String,
    size: Int = 1
) {
    private val thread = ScheduledThreadPoolExecutor(size, object : ThreadFactory {
        private var id = 0

        override fun newThread(r: Runnable): java.lang.Thread {
            id++
            if (size == 1) return Thread(r, this@Thread.id)
            return Thread(r, this@Thread.id + " Pool-" + id)
        }
    })

    /**
     * 关闭线程池
     */
    fun kill() {
        this.thread.shutdown()
    }

    /**
     * 执行任务
     *
     * @param task 任务实例
     */
    fun execute(task: Runnable) {
        this.thread.submit(task)
    }

    /**
     * 执行任务
     *
     * @param task 任务实例
     */
    fun execute(task: CancellableRunnable) {
        val future = this.thread.submit(task)
        task.future = future as ScheduledFuture<*>
    }

    /**
     * 延迟执行任务
     *
     * @param task  任务实例
     * @param delay 延迟时间(单位: 毫秒)
     */
    fun schedule(
        task: Runnable,
        delay: Long,
    ) {
        this.thread.schedule(task, delay, TimeUnit.MILLISECONDS)
    }

    /**
     * 延迟执行任务
     *
     * @param task  任务实例
     * @param delay 延迟时间(单位: 毫秒)
     */
    fun schedule(
        task: CancellableRunnable,
        delay: Long,
    ) {
        val future = this.thread.schedule(task, delay, TimeUnit.MILLISECONDS)
        task.future = future
    }

    /**
     * 定时执行任务
     *
     * @param task   任务实例
     * @param delay  延迟时间(单位: 毫秒)
     * @param period 间隔时间(单位: 毫秒)
     */
    fun schedule(
        task: Runnable,
        delay: Long,
        period: Long,
    ) {
        this.thread.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS)
    }

    /**
     * 定时执行任务
     *
     * @param task   任务实例
     * @param delay  延迟时间(单位: 毫秒)
     * @param period 间隔时间(单位: 毫秒)
     */
    fun schedule(
        task: CancellableRunnable,
        delay: Long,
        period: Long,
    ) {
        val future = this.thread.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS)
        task.future = future
    }
}
