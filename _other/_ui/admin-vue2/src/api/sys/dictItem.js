import { axios } from '@/utils/request'

/**
 * 分页
 */
export function itemPage (params) {
  return axios({
    url: '/system/dictItem/page',
    method: 'GET',
    params: params
  })
}

/**
 * 获取单条
 */
export function itemGet (id) {
  return axios({
    url: '/system/dictItem/findById',
    method: 'GET',
    params: { id }
  })
}

/**
 * 获取全部
 */
export function itemFindAll (id) {
  return axios({
    url: '/system/dictItem/findAll',
    method: 'GET'
  })
}

/**
 * 添加
 */
export function itemAdd (obj) {
  return axios({
    url: '/system/dictItem/add',
    method: 'POST',
    data: obj
  })
}

/**
 * 更新
 */
export function itemUpdate (obj) {
  return axios({
    url: '/system/dictItem/update',
    method: 'POST',
    data: obj
  })
}

/**
 * 删除
 */
export function itemDel (id) {
  return axios({
    url: '/system/dictItem/delete',
    params: { id },
    method: 'DELETE'
  })
}

/**
 * 判断字典项的编码是否已经使用
 */
export function itemExistsByCode (queryParam) {
  return axios({
    url: '/system/dictItem/itemExistsByCode',
    method: 'GET',
    params: queryParam
  })
}
