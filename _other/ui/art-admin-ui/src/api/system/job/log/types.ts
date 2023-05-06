export type JobLogDTO = {
  /**
   * 主键
   */
  id?: string
  /**
   * 任务名称
   */
  jobName?: string
  /**
   * 日志信息
   */
  jobMessage?: string
  /**
   * 执行状态（0正常 1失败）
   */
  status?: string
  /**
   * 异常信息
   */
  exceptionInfo?: string
  /**
   * 创建时间
   */
  createTime?: string
}
