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

export function getDeptTree () {
  return axios({
    url: '/system/dept/getDeptTree',
    method: 'GET'
  })
}

export function deleteById (id) {
  return axios({
    url: '/system/dept/deleteById/' + id,
    method: 'DELETE'
  })
}

export function addDept (data) {
  return axios({
    url: '/system/dept/addDept',
    method: 'POST',
    data: data
  })
}

export function getDeptById (id) {
  return axios({
    url: '/system/dept/getDeptById/' + id,
    method: 'GET'
  })
}

export function updateDept (data) {
  return axios({
    url: '/system/dept/updateDept',
    method: 'PUT',
    data: data
  })
}
