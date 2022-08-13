import { axios } from '@/utils/request'

/**
 * 根据分类id获取属性
 */
export function listAttributes (categoryId, type) {
  return axios({
    url: '/product/attribute/listAttributes',
    method: 'GET',
    params: { categoryId, type }
  })
}

/**
 * 批量保存
 */
export function saveAttributeBatch (obj) {
  return axios({
    url: '/product/attribute/saveAttributeBatch',
    method: 'POST',
    data: obj
  })
}

/**
 * 分页
 */
export function page (params) {
  return axios({
    url: '/product/attribute/page',
    method: 'GET',
    params: params
  })
}

/**
 * 获取单条
 */
export function get (id) {
  return axios({
    url: '/product/attribute/findById',
    method: 'GET',
    params: { id }
  })
}

/**
 * 获取全部
 */
export function findAll (id) {
  return axios({
    url: '/product/attribute/findAll',
    method: 'GET'
  })
}

/**
 * 添加
 */
export function add (obj) {
  return axios({
    url: '/product/attribute/add',
    method: 'POST',
    data: obj
  })
}

/**
 * 更新
 */
export function update (obj) {
  return axios({
    url: '/product/attribute/update',
    method: 'POST',
    data: obj
  })
}

/**
 * 删除
 */
export function del (id) {
  return axios({
    url: '/product/attribute/delete',
    params: { id },
    method: 'DELETE'
  })
}
