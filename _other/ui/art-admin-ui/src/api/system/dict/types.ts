export type DictDTO = {
  /**
   *  主键
   */
  id?: string
  /**
   * 类型
   */
  type?: string
  /**
   * 描述
   */
  description?: string
  /**
   * 备注
   */
  remark?: string
  /**
   * 是否是系统内置
   */
  systemFlag?: string
  /**
   * 删除标记
   */
  delFlag?: string
  /**
   * 创建时间
   */
  createTime?: string
  /**
   * 创建人
   */
  createBy?: string
  /**
   * 更新人
   */
  updateBy?: string
  /**
   * 更新时间
   */
  updateTime?: string
}

export type DictItemDto = {
  dictId: string
  value: string
  label: string
  type: string
  description: string
  sortOrder: number
  remark: string
}
