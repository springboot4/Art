import { axios } from '@/utils/request'

/**
 * 添加优惠券
 */
export function add (obj) {
  return axios({
    url: '/promotion/admin/coupon/save',
    method: 'POST',
    data: obj
  })
}

/**
 * 分页查询优惠券
 */
export function page (params) {
  return axios({
    url: '/promotion/admin/coupon/page',
    method: 'GET',
    params: params
  })
}

/**
 * 关闭优惠券
 */
export function close (id) {
  return axios({
    url: `/promotion/admin/coupon/close/${id}`,
    method: 'PUT'
  })
}

/**
 * 删除优惠券
 */
export function del (id) {
  return axios({
    url: '/promotion/admin/coupon/remove',
    params: { id },
    method: 'DELETE'
  })
}

/**
 * 查看优惠券详情
 */
export function get (id) {
  return axios({
    url: `/promotion/admin/coupon/${id}/info`,
    method: 'GET'
  })
}

/**
 * 获取全部
 */
export function findAll () {
  return axios({
    url: '/promotion/admin/coupon/findAll',
    method: 'GET'
  })
}

/**
 * 更新
 */
export function update (obj) {
  return axios({
    url: '/promotion/admin/coupon/update',
    method: 'POST',
    data: obj
  })
}
