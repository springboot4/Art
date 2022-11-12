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
