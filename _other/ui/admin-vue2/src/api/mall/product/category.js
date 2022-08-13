import { axios } from '@/utils/request'

/**
 * 分页
 */
export function page (params) {
  return axios({
    url: '/product/category/page',
    method: 'GET',
    params: params
  })
}

/**
 * 获取单条
 */
export function get (id) {
  return axios({
    url: '/product/category/findById',
    method: 'GET',
    params: { id }
  })
}

/**
 * 分类列表
 */
export function list () {
  return axios({
    url: '/product/category/list',
    method: 'GET'
  })
}

/**
 * 获取全部
 */
export function findAll (id) {
  return axios({
    url: '/product/category/findAll',
    method: 'GET'
  })
}

/**
 * 添加
 */
export function add (obj) {
  return axios({
    url: '/product/category/add',
    method: 'POST',
    data: obj
  })
}

/**
 * 更新
 */
export function update (obj) {
  return axios({
    url: '/product/category/update',
    method: 'POST',
    data: obj
  })
}

/**
 * 删除
 */
export function del (id) {
  return axios({
    url: '/product/category/delete',
    params: { id },
    method: 'DELETE'
  })
}
