/**
 * 用户三方平台绑定信息
 */
export interface UserThirdBindInfo {
  gitee: BindInfo
}

/**
 * 绑定信息
 */
interface BindInfo {
  bind?: boolean
  username?: string
  avatar?: string
}
