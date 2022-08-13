import { axios } from '@/utils/request'

/**
 * 分页
 */
export function page (params) {
  return axios({
    url: '/user/member/page',
    method: 'GET',
    params: params
  })
}
