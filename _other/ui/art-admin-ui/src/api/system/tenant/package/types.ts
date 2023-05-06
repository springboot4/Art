export type TenantPackageDTO = {
  /**
   * 套餐id
   */
  id?: string
  /**
   * 套餐名
   */
  name?: string
  /**
   * 套餐状态（0正常 1停用）
   */
  status?: number
  /**
   * 备注
   */
  remark?: string
  /**
   * 关联的菜单编号
   */
  menuIds?: string
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
   * 0-正常，1-删除
   */
  delFlag?: boolean
}
