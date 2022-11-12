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
 * 分页
 */
export function page (params) {
  return axios({
    url: '/promotion/admin/couponActivity/page',
    method: 'GET',
    params: params
  })
}

/**
 * 添加优惠券活动
 */
export function add (obj) {
  return axios({
    url: '/promotion/admin/couponActivity/save',
    method: 'POST',
    data: obj
  })
}

/**
 * 获取单条
 */
export function get (id) {
  return axios({
    url: '/promotion/admin/couponActivity/findById',
    method: 'GET',
    params: { id }
  })
}

/**
 * 关闭优惠券
 */
export function close (id) {
  return axios({
    url: `/promotion/admin/couponActivity/close/${id}`,
    method: 'PUT'
  })
}

/**
 * 获取全部
 */
export function findAll () {
  return axios({
    url: '/couponActivity/findAll',
    method: 'GET'
  })
}

/**
 * 更新
 */
export function update (obj) {
  return axios({
    url: '/couponActivity/update',
    method: 'POST',
    data: obj
  })
}

/**
 * 删除
 */
export function del (id) {
  return axios({
    url: '/couponActivity/delete',
    params: { id },
    method: 'DELETE'
  })
}
