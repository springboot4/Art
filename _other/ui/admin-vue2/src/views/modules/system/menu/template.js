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
  hidden: true,
  columns: [
    {
      title: '菜单名称',
      field: 'title',
      type: 'input',
      placeholder: '',
      search: false,
      treeNode: true,
      width: 180
    },
    {
      title: '权限代码',
      field: 'perms',
      hidden: false
    },
    {
      title: '菜单类型',
      field: 'type',
      slot: true
    },
    {
      title: 'URL',
      field: 'path'
    },
    {
      title: '组件路径',
      field: 'component'
    },
    {
      title: '组件名称',
      field: 'name'
    },
    {
      title: '是否缓存',
      field: 'keepAlive',
      type: 'dict',
      options: [
        {
          label: '是',
          value: '1'
        },
        {
          label: '否',
          value: '0'
        }
      ]
    },
    {
      title: '是否隐藏页面',
      field: 'hidden',
      type: 'dict',
      options: [
        {
          label: '是',
          value: '1'
        },
        {
          label: '否',
          value: '0'
        }
      ]
    },
    {
      title: '操作',
      width: '200px',
      field: 'action',
      slot: true
    }
  ]
}
