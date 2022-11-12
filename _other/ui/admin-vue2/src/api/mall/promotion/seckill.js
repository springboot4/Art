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
