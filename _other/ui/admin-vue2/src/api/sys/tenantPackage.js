import { axios } from '@/utils/request'

/**
 * 分页
 */
export function page (params) {
  return axios({
    url: '/system/tenant/package//page',
    method: 'GET',
    params: params
  })
}

/**
 * 获取单条
 */
export function get (id) {
  return axios({
    url: '/system/tenant/package//findById',
    method: 'GET',
    params: { id }
  })
}

/**
 * 获取全部
 */
export function findAll () {
  return axios({
    url: '/system/tenant/package//findAll',
    method: 'GET'
  })
}

/**
 * 添加
 */
export function add (obj) {
  return axios({
    url: '/system/tenant/package//add',
    method: 'POST',
    data: obj
  })
}

/**
 * 更新
 */
export function update (obj) {
  return axios({
    url: '/system/tenant/package//update',
    method: 'POST',
    data: obj
  })
}

/**
 * 删除
 */
export function del (id) {
  return axios({
    url: '/system/tenant/package//delete',
    params: { id },
    method: 'DELETE'
  })
}
