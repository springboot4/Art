import { axios } from '@/utils/request'

/**
 * 分页
 */
export function page (params) {
  return axios({
    url: '/schedule/jobLog/page',
    method: 'GET',
    params: params
  })
}

/**
 * 获取单条
 */
export function get (id) {
  return axios({
    url: '/schedule/jobLog/findById',
    method: 'GET',
    params: { id }
  })
}

/**
 * 获取全部
 */
export function findAll (id) {
  return axios({
    url: '/schedule/jobLog/findAll',
    method: 'GET'
  })
}

/**
 * 添加
 */
export function add (obj) {
  return axios({
    url: '/schedule/jobLog/add',
    method: 'POST',
    data: obj
  })
}

/**
 * 更新
 */
export function update (obj) {
  return axios({
    url: '/schedule/jobLog/update',
    method: 'POST',
    data: obj
  })
}

/**
 * 删除
 */
export function del (id) {
  return axios({
    url: '/schedule/jobLog/delete',
    params: { id },
    method: 'DELETE'
  })
}
