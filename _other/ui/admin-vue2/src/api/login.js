import request from '@/utils/request'

const userApi = {
  Login: '/auth/oauth/token',
  Logout: '/auth/myLogout',
  ForgePassword: '/auth/forge-password',
  Register: '/auth/register',
  twoStepCode: '/auth/2step-code',
  SendSms: '/system/sms/sendSms',
  SendSmsErr: '/account/sms_err',
  // get my info
  // UserInfo: '/auth/user/info',
  UserInfo: '/system/user/info',
  UserMenu: '/system/menu/nav',
  getCaptcha: 'captcha'
}

/**
 * login func
 * parameter: {
 *     username: '',
 *     password: '',
 *     remember_me: true,
 *     captcha: '12345'
 * }
 * @param parameter
 * @returns {*}
 */
export function login (parameter) {
  return request({
    url: userApi.Login,
    method: 'post',
    data: parameter,
    headers: {
      // 'Authorization': 'Basic c3dhZ2dlcjoxMjM0NTY=' // swagger:123456 避免验证验证码 http://localhost:8301/auth/captcha?key=77777439生成验证码
      // fxz:123456
      'Authorization': 'Basic Znh6OjEyMzQ1Ng=='
    },
    params: parameter
  })
}

export function getCaptcha () {
  return request({
    url: userApi.getCaptcha,
    method: 'GET'
  })
}

export function getSmsCaptcha (parameter) {
  return request({
    url: userApi.SendSms,
    method: 'get',
    params: parameter
  })
}

export function getInfo () {
  return request({
    url: userApi.UserInfo,
    params: { client_id: 'fxz' },
    method: 'get'
  })
}

export function getCurrentUserNav () {
  return request({
    url: userApi.UserMenu,
    method: 'get'
  })
}

export function logout () {
  return request({
    url: userApi.Logout,
    method: 'DELETE'
  })
}

/**
 * get user 2step code open?
 * @param parameter {*}
 */
export function get2step (parameter) {
  return request({
    url: userApi.twoStepCode,
    method: 'post',
    data: parameter
  })
}
