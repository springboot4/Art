import { axios } from '@/utils/request'

/**
 * 获取全部
 */
export function findAll () {
  return axios({
    url: '/system/routeConf/findAll',
    method: 'GET'
  })
}

/**
 * 更新
 */
export function edit (data) {
  return axios({
    url: '/system/routeConf/update',
    method: 'PUT',
    data: data
  })
}
