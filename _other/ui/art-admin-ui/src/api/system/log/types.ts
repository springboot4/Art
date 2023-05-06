export type OperLogDTO = {
  /**
   * 主键
   */
  id?: string | null
  /**
   * 标题
   */
  title?: string
  /**
   * 业务类型
   */
  businessType?: number | null | string
  /**
   * 方法名称
   */
  method?: string
  /**
   * 请求方式
   */
  requestMethod?: string
  /**
   * 操作人员
   */
  operName?: string
  /**
   * 请求url
   */
  operUrl?: string
  /**
   * 主机地址
   */
  operIp?: string
  /**
   * 请求参数
   */
  operParam?: string
  /**
   * 操作状态（0正常 1异常）
   */
  status?: number | null
  /**
   * 异常信息
   */
  errorMsg?: string
  /**
   * 执行时间
   */
  time?: string
  /**
   * 创建人
   */
  createBy?: string
  /**
   * 创建时间
   */
  createTime?: string
  /**
   * 更新人
   */
  updateBy?: string
  /**
   * 更新时间
   */
  updateTime?: string
}
