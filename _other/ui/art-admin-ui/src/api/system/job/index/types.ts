export type JobDTO = {
  /**
   * 任务ID
   */
  jobId?: string
  /**
   * 任务名称
   */
  jobName?: string
  /**
   * 任务组名
   */
  jobGroup?: string
  /**
   * 方法执行参数
   */
  parameters?: string
  /**
   * cron执行表达式
   */
  cronExpression?: string
  /**
   * 计划执行错误策略（1立即执行 2执行一次 3放弃执行）
   */
  misfirePolicy?: string
  /**
   * 状态（0正常 1暂停）
   */
  status?: string
  /**
   * 创建者
   */
  createBy?: string
  /**
   * 创建时间
   */
  createTime?: string
  /**
   * 更新者
   */
  updateBy?: string
  /**
   * 更新时间
   */
  updateTime?: string
  /**
   * 备注信息
   */
  remark?: string
}
