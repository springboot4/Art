export type DictItemDTO = {
  /**
   * 主键
   */
  id?: string | null
  /**
   * 字典ID
   */
  dictId?: string
  /**
   * 值
   */
  value?: string
  /**
   * 标签
   */
  label?: string
  /**
   * 字典类型
   */
  type?: string
  /**
   * 描述
   */
  description?: string
  /**
   * 排序（升序）
   */
  sortOrder?: number | null
  /**
   * 备注
   */
  remark?: string
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
   * 修改人
   */
  updateBy?: string
  /**
   * 更新时间
   */
  updateTime?: string
}
