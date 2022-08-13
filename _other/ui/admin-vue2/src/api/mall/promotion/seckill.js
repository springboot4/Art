import { axios } from '@/utils/request'

/**
 * 关闭秒杀活动
 */
export function closeSeckil (id) {
  return axios({
    url: '/promotion/admin/seckill/close/' + id,
    method: 'PUT'
  })
}

/**
 * 分页
 */
export function page (params) {
  return axios({
    url: '/promotion/admin/seckill/page',
    method: 'GET',
    params: params
  })
}

/**
 * 获取单条
 */
export function get (id) {
  return axios({
    url: '/promotion/admin/seckill/' + id,
    method: 'GET'
  })
}

/**
 * 删除秒杀请求
 */
export function deleteApply (seckillId, applyId) {
  return axios({
    url: '/promotion/admin/seckillApply/' + seckillId + '/' + applyId,
    method: 'DELETE'
  })
}

/**
 * 更新
 */
export function update (obj) {
  return axios({
    url: '/promotion/admin/seckill/update',
    method: 'PUT',
    data: obj
  })
}

/**
 * 获取全部
 */
export function findAll (id) {
  return axios({
    url: '/seckill/findAll',
    method: 'GET'
  })
}

/**
 * 添加
 */
export function add (obj) {
  return axios({
    url: '/seckill/add',
    method: 'POST',
    data: obj
  })
}
