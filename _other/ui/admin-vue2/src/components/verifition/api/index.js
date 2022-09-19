import { axios } from '@/utils/request'

// 获取验证图片  以及token
export function reqGet (data) {
  return axios({
    url: '/code',
    method: 'get',
    data
  })
}

// 滑动或者点选验证
export function reqCheck (data) {
  return axios({
    url: '/code/check',
    method: 'post',
    params: data
  })
}
