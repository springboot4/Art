/**
 * @description: Login interface parameters
 */
export interface LoginParams {
  username?: string
  password?: string
  grant_type?: string
  scope?: string
  captcha?: string
  mobile?: string
}

export interface SysUser {
  userId: string
  username: string
  avatar: string
  deptId: string
  deptName: string
  description: string
  email: string
  mobile: string
  postId: string
  postName: string
  roleId: string
  roleName: string
  sex: number
}

/**
 * @description: 执行/OAuth2/token方法的返回值
 */
export interface LoginResultModel {
  /**
   * accessToken 必须
   */
  access_token: string
  /**
   * refreshToken 必须
   */
  refresh_token: string
  /**
   * additionalParameters 可选
   */
  additional_parameters?: { [key: string]: any }
}

/**
 * @description: Get user information return value
 */
export interface GetUserInfoModel {
  // 用户信息
  sysUser: SysUser
  // 用户拥有的权限标识
  permissions: string[]
}
