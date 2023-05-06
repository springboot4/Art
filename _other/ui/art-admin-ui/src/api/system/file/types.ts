export type FileDTO = {
  /**
   *
   */
  id?: string
  /**
   *
   */
  fileName?: string
  /**
   *
   */
  bucketName?: string
  /**
   *
   */
  original?: string
  /**
   *
   */
  type?: string
  /**
   * 文件大小
   */
  fileSize?: string
  /**
   * 0-正常，1-删除
   */
  delFlag?: string
  /**
   * 创建时间
   */
  createTime?: string
  /**
   * 修改时间
   */
  updateTime?: string
  /**
   * 创建者
   */
  createBy?: string
  /**
   * 更新人
   */
  updateBy?: string
}

export type FileDownloadDTO = {
  /**
   * 桶名称
   */
  bucket?: string
  /**
   *文件名
   */
  fileName?: string
}
