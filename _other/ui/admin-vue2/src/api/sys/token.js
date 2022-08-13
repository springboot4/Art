import { axios } from '@/utils/request'

export function pageToken (params) {
  return axios({
    url: '/system/token/page',
    method: 'get',
    params
  })
}

export function removeToken (token) {
  return axios({
    url: '/system/token/' + token,
    method: 'DELETE'
  })
}
