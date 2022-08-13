import { axios } from '@/utils/request'

/**
 * 分页
 */
export function page (params) {
  return axios({
    url: '/order/orders/page',
    method: 'GET',
    params: params
  })
}
