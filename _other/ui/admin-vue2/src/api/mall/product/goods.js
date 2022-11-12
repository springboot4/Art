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
 * 批量保存
 */
export function addGoods (obj) {
  return axios({
    url: '/product/goods/add',
    method: 'POST',
    data: obj
  })
}

/**
 * 分页
 */
export function page (params) {
  return axios({
    url: '/search/goods/page',
    method: 'GET',
    params: params
  })
}

/**
 * 删除
 */
export function del (id) {
  return axios({
    url: '/product/goods/delete',
    params: { id },
    method: 'DELETE'
  })
}

/**
 * 获取sku列表
 */
export function listSku () {
  return axios({
    url: '/product/goods/listSku',
    method: 'GET'
  })
}
