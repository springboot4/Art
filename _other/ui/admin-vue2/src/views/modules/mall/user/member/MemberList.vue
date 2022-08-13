<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="12">
            <a-form-item >
              <a-input v-model="queryParam.nickName" placeholder="会员名称" />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <a-button type="primary" @click="queryPage">查询</a-button>
            <a-button style="margin-left: 8px" @click="restQuery">重置</a-button>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <f-table
      :expandConfig="expandConfig"
      :columns="tableObj.columns"
      :data="loadData"
      ref="table">
      <template v-slot:avatarUrl="{row}">
        <img :src="row.avatarUrl" width="40"/>
      </template>
      <template v-slot:balance="{row}">
        {{ row.balance | moneyFormatter }}
      </template>
    </f-table>
  </a-card>
</template>

<script>
import { tableObj } from './template'
import { page } from '@/api/mall/user/member'
import { TableMixin } from '@/mixins/TableMixin'

export default {
  name: 'MemberList',
  mixins: [TableMixin],
  data () {
    return {
      expandConfig: {
        expand: true,
        expandField: 'addressList',
        columns: tableObj.addressColumns
      },
      tableObj,
      queryParam: {},
      loadData: (parameter) => {
        return page(Object.assign(parameter, this.queryParam)).then(res => {
          return res.data
        })
      }
    }
  },
  methods: {
    handleOk () {
      this.queryPage()
    }
  }
}
</script>

<style scoped>

</style>
