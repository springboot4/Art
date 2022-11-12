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
      title: '主键',
      field: 'id'
    },
    {
      title: '商品分类名称',
      field: 'name'
    },
    {
      title: '父级ID',
      field: 'parentId'
    },
    {
      title: '层级',
      field: 'level'
    },
    {
      title: '图标地址',
      field: 'iconUrl'
    },
    {
      title: '排序',
      field: 'sort'
    },
    {
      title: '显示状态:( 0:隐藏 1:显示)',
      field: 'visible'
    },
    {
      title: '创建时间',
      field: 'createTime'
    },
    {
      title: '更新时间',
      field: 'updateTime'
    },
    {
      title: '创建人',
      field: 'createBy'
    },
    {
      title: '更新人',
      field: 'updateBy'
    },
    {
      title: '操作',
      width: '200px',
      field: 'action',
      slot: true
    }
  ]
}
