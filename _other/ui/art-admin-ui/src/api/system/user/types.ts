export type UserDTO = {
  /**
   * 用户ID
   */
  userId?: string
  /**
   * 用户名
   */
  username?: string
  /**
   * 密码
   */
  password?: string
  /**
   * 部门ID
   */
  deptId?: string
  /**
   * 邮箱
   */
  email?: string
  /**
   * 联系电话
   */
  mobile?: string
  /**
   * 状态 0锁定 1有效
   */
  status?: string
  /**
   * 创建时间
   */
  createTime?: string
  /**
   * 修改时间
   */
  updateTime?: string
  /**
   * 最近访问时间
   */
  lastLoginTime?: string
  /**
   * 性别 0男 1女 2保密
   */
  ssex?: string
  /**
   * 头像
   */
  avatar?: string
  /**
   * 描述
   */
  description?: string
  /**
   * 创建人
   */
  createBy?: string
  /**
   * 更新人
   */
  updateBy?: string
  /**
   * 租户id
   */
  tenantId?: string
  /**
   * 角色id
   */
  roleId?: string
  /**
   * 岗位id
   */
  postId?: string
}
