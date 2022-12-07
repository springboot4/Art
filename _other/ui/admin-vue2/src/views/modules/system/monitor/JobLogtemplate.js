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
      title: '任务名称',
      field: 'jobName'
    },
    {
      title: '日志信息',
      field: 'jobMessage'
    },
    {
      title: '执行状态',
      field: 'status',
      type: 'dict',
      options: [
        {
          label: '正常',
          value: '0'
        },
        {
          label: '失败',
          value: '1'
        }
      ]
    },
    {
      title: '异常信息',
      field: 'exceptionInfo'
    },
    {
      title: '创建时间',
      field: 'createTime',
      width: '200px'
    },
    {
      title: '操作',
      width: '100px',
      field: 'action',
      slot: true
    }
  ]
}
