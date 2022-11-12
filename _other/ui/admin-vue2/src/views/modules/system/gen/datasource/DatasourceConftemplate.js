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

export const tableObj = {
  columns: [
    {
      title: '数据源名称',
      field: 'name'
    },
    {
      title: 'url',
      field: 'url'
    },
    {
      title: '用户名',
      field: 'username'
    },
    {
      title: '密码',
      field: 'password'
    },
    {
      title: '操作',
      width: '200px',
      field: 'action',
      slot: true
    }
  ]
}
