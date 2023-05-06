import { defHttp } from '/@/utils/http/axios'
import { LoginParams } from './model/userModel'

import { ErrorMessageMode } from '/#/axios'

enum Api {
  Login = '/auth/oauth2/token',
  Logout = '/auth/token/myLogout',
  GetUserInfo = '/system/user/info',
}

/**
 * @description: 登录获取token接口
 */
export function loginApi(loginData: LoginParams, mode: ErrorMessageMode = 'modal') {
  return defHttp.post<any>(
    {
      url: Api.Login,
      data: loginData,
      params: loginData,
      headers: {
        // 客户端:密钥base64编码 fxz:123456
        Authorization: 'Basic Znh6OjEyMzQ1Ng==',
      },
    },
    {
      errorMessageMode: mode,
    },
  )
}

/**
 * 获取用户信息
 */
export function getUserInfo() {
  return defHttp.get<any>({ url: Api.GetUserInfo }, { errorMessageMode: 'none' })
}

/**
 * 退出登录
 */
export function doLogout() {
  return defHttp.delete({ url: Api.Logout })
}
