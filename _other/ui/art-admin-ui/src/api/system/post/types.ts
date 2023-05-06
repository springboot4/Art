export type PostDTO = {
  /**
   * 岗位ID
   */
  postId?: string
  /**
   * 岗位编码
   */
  postCode?: string
  /**
   * 岗位名称
   */
  postName?: string
  /**
   * 岗位排序
   */
  postSort?: number
  /**
   * 是否删除  -1：已删除  0：正常
   */
  delFlag?: string
  /**
   * 描述
   */
  description?: string
  /**
   * 创建时间
   */
  createTime?: string
  /**
   * 创建人
   */
  createBy?: string
  /**
   * 更新时间
   */
  updateTime?: string
  /**
   * 更新人
   */
  updateBy?: string
}
