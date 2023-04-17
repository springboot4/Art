import { axios } from '@/utils/request'

export function bindInfo () {
  return axios({
    url: '/system/third/bindInfo',
    method: 'GET'
  })
}

export function unBind (type) {
  return axios({
    url: '/system/third/unBind',
    method: 'PUT',
    params: { type }
  })
}
