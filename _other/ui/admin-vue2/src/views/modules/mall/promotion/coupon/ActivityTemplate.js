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
      title: '优惠券名称',
      field: 'couponName',
      width: '120px'
    },
    {
      title: '消费门槛',
      field: 'consumeThreshold',
      width: '100px'
    },
    {
      title: '使用范围',
      field: 'scopeType',
      width: '100px',
      slot: true
    },
    {
      title: '获取方式',
      field: 'getType',
      width: '100px',
      slot: true
    },
    {
      title: '折扣',
      field: 'couponDiscount',
      width: '100px'
    },
    {
      title: '面额',
      field: 'price',
      width: '100px'
    },
    {
      title: '活动开始时间',
      field: 'startTime',
      width: '180px'
    },
    {
      title: '活动结束时间',
      field: 'endTime',
      width: '180px'
    }
  ]
}
