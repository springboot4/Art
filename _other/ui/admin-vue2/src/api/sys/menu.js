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
 * 获取当前用户角色下的所有菜单信息
 */
export function getUserMenuTree () {
  return axios({
    url: '/system/menu/getUserMenuTree',
    method: 'GET'
  })
}

export function getAllMenuTree () {
  return axios({
    url: '/system/menu/getAllMenuTree',
    method: 'GET'
  })
}

export function getObj (id) {
  return axios({
    url: '/system/menu/getMenuById/' + id,
    method: 'GET'
  })
}

export function addObj (obj) {
  return axios({
    url: '/system/menu/save',
    method: 'POST',
    data: obj
  })
}

export function putObj (obj) {
  return axios({
    url: '/system/menu/update',
    method: 'POST',
    data: obj
  })
}

export function delObj (id) {
  return axios({
    url: '/system/menu/delete/' + id,
    method: 'DELETE'
  })
}
