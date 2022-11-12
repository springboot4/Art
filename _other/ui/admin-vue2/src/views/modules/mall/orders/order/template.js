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

export const statusArr = [
  {
    label: '待付款',
    value: '101'
  },
  {
    label: '用户取消',
    value: '102'
  },
  {
    label: '系统取消',
    value: '103'
  },
  {
    label: '已付款',
    value: '201'
  },
  {
    label: '申请退款',
    value: '202'
  },
  {
    label: '已退款',
    value: '203'
  },
  {
    label: '待发货',
    value: '301'
  },
  {
    label: '已发货',
    value: '401'
  },
  {
    label: '用户收货',
    value: '501'
  },
  {
    label: '系统收货',
    value: '502'
  },
  {
    label: '已完成',
    value: '901'
  }
]

export const tableObj = {
  columns: [
    {
      title: '订单号',
      field: 'orderSn'
    },
    {
      title: '会员id',
      field: 'memberId'
    },
    {
      title: '订单来源',
      field: 'sourceType',
      type: 'dict',
      options: [
        {
          label: 'pc端',
          value: '0'
        },
        {
          label: '移动端-购物车',
          value: '1'
        },
        {
          label: '移动端-直接购买',
          value: '2'
        }
      ]
    },
    {
      title: '订单状态',
      field: 'status',
      type: 'dict',
      options: statusArr
    },
    {
      title: '支付金额',
      field: 'payAmount',
      slot: true
    },
    {
      title: '支付方式',
      field: 'payType',
      type: 'dict',
      options: [
        {
          label: '微信jsapi',
          value: '1'
        },
        {
          label: '支付宝',
          value: '2'
        },
        {
          label: '余额',
          value: '3'
        },
        {
          label: '微信',
          value: '4'
        }
      ]
    },
    {
      title: '备注',
      field: 'remark'
    },
    {
      title: '创建时间',
      field: 'createTime'
    }
  ],
  itemColumns: [
    {
      title: '商品名称',
      field: 'skuName'
    },
    {
      title: '图片',
      field: 'picUrl',
      slot: true
    },
    {
      title: '价格',
      field: 'totalAmount',
      slot: true
    },
    {
      title: '数量',
      field: 'count'
    }
  ]
}
