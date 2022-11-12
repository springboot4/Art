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
      title: '活动名称',
      field: 'promotionName',
      width: '150px'
    },
    {
      title: '活动开始时间',
      field: 'startTime',
      width: '150px'
    },
    {
      title: '活动结束时间',
      field: 'endTime',
      width: '150px'
    },
    {
      title: '活动范围',
      field: 'activityScope',
      width: '150px',
      slot: true
    },
    {
      title: '优惠券活动类型',
      field: 'couponActivityType',
      width: '150px',
      slot: true
    },
    {
      title: '状态',
      field: 'statue',
      width: '150px',
      slot: true
    },
    {
      title: '操作',
      width: '200px',
      field: 'action',
      slot: true
    }
  ]
}
