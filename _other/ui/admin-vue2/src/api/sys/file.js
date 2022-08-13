import { axios } from '@/utils/request'

/**
 * 分页
 */
export function page (params) {
  return axios({
    url: '/system/file/page',
    method: 'GET',
    params: params
  })
}

/**
 * 获取单条
 */
export function get (id) {
  return axios({
    url: '/system/file/findById',
    method: 'GET',
    params: { id }
  })
}

/**
 * 获取全部
 */
export function findAll (id) {
  return axios({
    url: '/system/file/findAll',
    method: 'GET'
  })
}

/**
 * 添加
 */
export function add (obj) {
  return axios({
    url: '/system/file/add',
    method: 'POST',
    data: obj
  })
}

/**
 * 更新
 */
export function update (obj) {
  return axios({
    url: '/system/file/update',
    method: 'POST',
    data: obj
  })
}

/**
 * 删除
 */
export function del (id) {
  return axios({
    url: '/system/file/delete',
    params: { id },
    method: 'DELETE'
  })
}
