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

import Vue from 'vue'
import { ACCESS_TOKEN, TENANT_ID } from '@/store/mutation-types'

export const FormMixin = {
  data () {
    return {
      title: '新增',
      labelCol: {
        sm: { span: 7 }
      },
      wrapperCol: {
        sm: { span: 13 }
      },
      modalWidth: 640,
      confirmLoading: false,
      visible: false,
      editable: false,
      addable: false,
      showable: false,
      type: 'add',
      headers: {
        Authorization: 'Bearer ' + Vue.ls.get(ACCESS_TOKEN),
        'TENANT-ID': Vue.ls.get(TENANT_ID)
      }
    }
  },
  methods: {
    init (id, type, ...vars) { // 初始化表单
      this.visible = true
      this.type = type
      if (type && type === 'add') {
        this.addable = true
        this.title = '新增'
      }
      if (type === 'edit') {
        this.editable = true
        this.title = '修改'
      }
      if (type === 'show') {
        this.showable = true
        this.title = '查看'
      }
      this.edit(id, type, ...vars)
    },
    edit () {},
    handleCancel () {
      this.visible = false
      setTimeout(() => {
        this.addable = false
        this.showable = false
        this.editable = false
      }, 200)
    }
  }
}
