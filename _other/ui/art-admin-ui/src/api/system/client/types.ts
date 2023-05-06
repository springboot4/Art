export type ClientDetailsDTO = {
  /**
   * 客户端ID
   */
  clientId?: string | null
  /**
   * 资源列表
   */
  resourceIds?: string
  /**
   * 客户端密钥
   */
  clientSecret?: string
  /**
   * 域
   */
  scope?: string
  /**
   * 认证类型
   */
  authorizedGrantTypes?: string
  /**
   * 重定向地址
   */
  webServerRedirectUri?: string
  /**
   * 角色列表
   */
  authorities?: string
  /**
   * token 有效期
   */
  accessTokenValidity?: number | null
  /**
   * 刷新令牌有效期
   */
  refreshTokenValidity?: number | null
  /**
   * 令牌扩展字段JSON
   */
  additionalInformation?: string
  /**
   * 是否自动放行
   */
  autoapprove?: string
  /**
   * 创建时间
   */
  createTime?: string
  /**
   * 更新时间
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
