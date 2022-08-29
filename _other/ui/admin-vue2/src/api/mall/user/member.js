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

/**
 * 列出所有会员信息
 */
export function listMembers (params) {
  return axios({
    url: '/user/member/listMembers',
    method: 'GET',
    params: params
  })
}
