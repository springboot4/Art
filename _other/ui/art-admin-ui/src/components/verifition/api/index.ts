import { defHttp } from '/@/utils/http/axios'

//获取验证图片  以及token
export function reqGet(data) {
  return defHttp.get({
    url: '/code',
    data,
  })
}

//滑动或者点选验证
export function reqCheck(data) {
  return defHttp.post({
    url: '/code/check',
    data,
  })
}
