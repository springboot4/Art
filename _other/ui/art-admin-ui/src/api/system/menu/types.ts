export type MenuDTO = {
  /**
   * 菜单/按钮ID
   */
  id?: string
  /**
   * 上级菜单ID
   */
  parentId?: string
  /**
   * title
   */
  title?: string
  /**
   * 菜单/按钮名称
   */
  name?: string
  /**
   * 权限标识(多个用逗号分隔，如：user:list,user:create)
   */
  perms?: string
  /**
   * 类型 0菜单 1按钮
   */
  type?: string
  /**
   * 对应路由组件component
   */
  component?: string
  /**
   * 对应路由path
   */
  path?: string
  /**
   * 重定向
   */
  redirect?: string
  /**
   * 图标
   */
  icon?: string
  /**
   * 是否缓存 0:否 1:是
   */
  keepAlive?: number | null
  /**
   * 排序
   */
  orderNum?: number | null
  /**
   * 创建时间
   */
  createTime?: string
  /**
   * 修改时间
   */
  updateTime?: string
  /**
   * 创建人
   */
  createBy?: string
  /**
   * 更新人
   */
  updateBy?: string
  /**
   * 是否隐藏(1 隐藏 0 不隐藏)
   */
  hidden?: string
  /**
   * 应用id
   */
  application?: string
}
