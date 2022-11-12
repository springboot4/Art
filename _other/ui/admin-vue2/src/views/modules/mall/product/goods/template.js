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
      title: '商品名称',
      field: 'name'
    },
    {
      title: '商品图片',
      field: 'picUrl',
      slot: true,
      width: '100px'
    },
    {
      title: '商品类目',
      field: 'categoryName'
    },
    {
      title: '商品品牌',
      field: 'brandName'
    },
    {
      title: '零售价',
      field: 'originPrice'
    },
    {
      title: '促销价',
      field: 'price',
      slot: true,
      width: '120px'
    },
    {
      title: '销量',
      field: 'sales',
      width: '80px'
    },
    {
      title: '单位',
      field: 'unit',
      width: '80px'
    },
    {
      title: '描述',
      field: 'description',
      width: '80px'
    },
    {
      title: '商品详情',
      field: 'detail',
      slot: true,
      width: '100px'
    },
    {
      title: '操作',
      field: 'action',
      slot: true,
      width: '80px'
    }
  ],
  skuColumns: [
    {
      title: '库存编码',
      field: 'skuSn'
    },
    {
      title: '商品规格',
      field: 'name'
    },
    {
      title: '图片',
      field: 'picUrl',
      slot: true
    },
    {
      title: '现价',
      field: 'price',
      slot: true
    },
    {
      title: '库存',
      field: 'stockNum'
    }
  ]
}
