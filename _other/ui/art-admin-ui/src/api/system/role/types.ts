export type RoleDTO = {
  /**
   * 角色ID
   */
  roleId?: string
  /**
   * 角色名称
   */
  roleName?: string
  /**
   * 角色描述
   */
  remark?: string
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
   * 数据权限范围
   */
  dataScope?: number | null
  /**
   * 数据范围(指定部门数组)
   */
  dataScopeDeptIds?: string
  /**
   * 角色code
   */
  code?: string
  /**
   * 租户id
   */
  tenantId?: string
  menuId?: string
}
