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
import { ACCESS_TOKEN } from '@/store/mutation-types'
export const TableMixin = {
  data () {
    return {
      // 分页参数
      pages: {
        size: 10,
        current: 1
      },
      alert: {
        clear: true
      },
      selectedRowKeys: [],
      selectedRows: [],
      queryParam: {},
      // file-upload
      headers: {
        Authorization: 'Bearer ' + Vue.ls.get(ACCESS_TOKEN)
      }
    }
  },
  methods: {
    handlerquery (e) {
      this.queryParam = { ...e }
      this.queryPage()
    },
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },
    handleSubmit (e) {
      e.preventDefault()
      this.queryPage()
    },
    restQuery () {
      this.queryParam = {}
      this.queryPage()
    },
    queryPage (bool = false) {
      this.$refs.table.refresh(bool)
    },
    showData (record) {
      this.$refs.modalForm.init(record.id, 'show')
    },
    editData (record) {
      this.$refs.modalForm.init(record.id, 'edit')
    },
    addData () {
      this.$refs.modalForm.init('', 'add')
    }
  }
}
