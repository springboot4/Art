export type TenantDTO = {
  /**
   * 租户id
   */
  id?: string
  /**
   * 租户名
   */
  name?: string
  /**
   * 当前租户管理员id
   */
  tenantAdminId?: string
  /**
   * 当前租户管理员姓名
   */
  tenantAdminName?: string
  /**
   * 当前租户管理员手机号
   */
  tenantAdminMobile?: string
  /**
   * 租户状态（0正常 1停用）
   */
  status?: number
  /**
   * 租户套餐id
   */
  packageId?: string
  /**
   * 过期时间
   */
  expireTime?: string
  /**
   * 账号数量
   */
  accountCount?: number
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
  password?: string
  username?: string
}
