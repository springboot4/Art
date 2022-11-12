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

export function getAllRole () {
  return axios({
    url: '/system/role/getAllRole',
    method: 'GET'
  })
}

export function PageRole (param) {
  return axios({
    url: '/system/role/page',
    method: 'GET',
    params: param
  })
}

export function addRole (data) {
  return axios({
    url: '/system/role/addRole',
    method: 'POST',
    data: data
  })
}

export function editRole (data) {
  return axios({
    url: '/system/role/editRole',
    method: 'PUT',
    data: data
  })
}

export function getRoleById (id) {
  return axios({
    url: '/system/role/getRoleById/' + id,
    method: 'GET'
  })
}

export function deleteRoleById (id) {
  return axios({
    url: '/system/role/deleteRoleById/' + id,
    method: 'DELETE'
  })
}
