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
      title: '用户名',
      field: 'username'
    },
    {
      title: '部门',
      field: 'deptName'
    },
    {
      title: '邮箱',
      field: 'email'
    },
    {
      title: '手机号',
      field: 'mobile'
    },
    {
      title: '状态',
      field: 'status',
      type: 'dict',
      options: [
        {
          label: '锁定',
          value: '0'
        },
        {
          label: '正常',
          value: '1'
        }
      ]
    },
    {
      title: '注册时间',
      field: 'createTime'
    },
    {
      title: '性别',
      field: 'sex',
      type: 'dict',
      options: [
        {
          label: '男',
          value: '0'
        },
        {
          label: '女',
          value: '1'
        }
      ]
    },
    {
      title: '角色',
      field: 'roleName'
    },
    {
      title: '岗位',
      field: 'postName'
    },
    {
      title: '操作',
      width: '200px',
      field: 'action',
      slot: true
    }
  ]
}
