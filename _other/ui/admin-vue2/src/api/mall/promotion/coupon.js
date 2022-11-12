/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { axios } from '@/utils/request'

/**
 * 列出优惠券
 */
export function listCoupon (params) {
  return axios({
    url: '/promotion/admin/coupon/listCoupon',
    method: 'GET',
    params: params
  })
}

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
