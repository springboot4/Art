<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item label="用户名">
              <a-input v-model="queryParam.username" placeholder="用户名" />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <a-button type="primary" @click="queryPage">查询</a-button>
            <a-button style="margin-left: 8px" @click="resetQuery">重置</a-button>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <f-table
      :columns="tableObj.columns"
      :data="loadData"
      ref="table">
      <template v-slot:action="{row}">
        <a href="javascript:" @click="deleteItem(row)">强退</a>
      </template>
    </f-table>

  </a-card>
</template>

<script>

import { TableMixin } from '@/mixins/TableMixin'
import { tableObj } from './template'
import { removeToken, pageToken } from '@/api/sys/token'

export default {
  name: 'TokenList',
  mixins: [TableMixin],
  data () {
    return {
      tableObj,
      queryParam: {
        username: ''
      },
      loadData: (parameter) => {
        console.log('param:', this.queryParam)
        return pageToken(
          Object.assign(parameter, this.queryParam)
        ).then(res => {
          return res.data
        })
      }
    }
  },
  methods: {
    deleteItem (record) {
      removeToken(record.access_token).then(_ => {
        this.$message.info('删除成功')
        this.queryPage()
      }).catch(err => {
        this.$message.error(err.msg)
      })
    },
    resetQuery () {
      this.queryParam = {}
      this.queryPage()
    }
  }
}
</script>
