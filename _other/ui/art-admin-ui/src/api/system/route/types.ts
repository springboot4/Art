export type RouteConfDTO = {
  /**
   * 主键
   */
  id?: string
  /**
   *
   */
  name?: string
  /**
   *
   */
  routeId?: string
  /**
   * 断言
   */
  predicates?: string
  /**
   * 过滤器
   */
  filters?: string
  /**
   *
   */
  uri?: string
  /**
   * 排序
   */
  sortOrder?: number
  /**
   * 路由元信息
   */
  metadata?: string
  /**
   * 创建人
   */
  createBy?: string
  /**
   * 修改人
   */
  updateBy?: string
  /**
   * 创建时间
   */
  createTime?: string
  /**
   * 修改时间
   */
  updateTime?: string
  /**
   *
   */
  delFlag?: string
}
