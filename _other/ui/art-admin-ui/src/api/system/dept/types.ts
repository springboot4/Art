export type DeptDTO = {
  /**
   * 部门ID
   */
  deptId?: string
  /**
   * 上级部门ID
   */
  parentId?: string
  /**
   * 部门名称
   */
  deptName?: string
  /**
   * 排序
   */
  orderNum?: number
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
}
