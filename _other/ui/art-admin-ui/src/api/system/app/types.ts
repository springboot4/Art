export type AppDTO = {
  /**
   * 主键
   */
  id?: number | string | null

  /**
   * 应用名称
   */
  name: string

  /**
   * 应用编码
   */
  code: string

  /**
   * 图标
   */
  icon: string

  /**
   * 排序
   */
  sort: number
}
