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
      title: '商品属性',
      field: 'name'
    },
    {
      title: '属性值',
      field: 'value'
    },
    {
      title: '操作',
      width: '80px',
      field: 'action',
      slot: true
    }
  ],
  specColumns: [
    {
      title: '商品名称',
      field: 'name'
    },
    {
      title: '商品价格',
      field: 'price'
    },
    {
      title: '库存编号',
      field: 'skuSn'
    },
    {
      title: '库存数量',
      field: 'stockNum'
    },
    {
      title: '操作',
      width: '150px',
      field: 'action',
      slot: true
    }
  ]
}
