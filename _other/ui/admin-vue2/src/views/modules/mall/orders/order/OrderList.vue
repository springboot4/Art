<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="12">
            <a-form-item >
              <a-input v-model="queryParam.orderSn" placeholder="订单号" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12" >
            <a-form-item>
              <a-range-picker @change="onChange" v-model="time2"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8" >
            <a-form-item>
              <a-select v-model="queryParam.status" placeholder="订单状态">
                <a-select-option v-for="(item,index) in statusArr" :key="index" :value="item.value">
                  {{ item.label }}
                </a-select-option>
              </a-select></a-form-item>
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
      <template v-slot:payAmount="{row}">
        {{ row.payAmount | moneyFormatter }}
      </template>
      <template v-slot:totalAmount="{row}">
        {{ row.totalAmount | moneyFormatter }}
      </template>

      <template v-slot:picUrl="{row}">
        <img :src="row.picUrl" width="40"/>
      </template>

    </f-table>
  </a-card>
</template>

<script>
import { tableObj, statusArr } from './template'
import { page } from '@/api/mall/order/order'
import { TableMixin } from '@/mixins/TableMixin'
import { FormMixin } from '@/mixins/FormMixin'

export default {
  name: 'OrderList',
  mixins: [TableMixin, FormMixin],
  data () {
    return {
      expandConfig: {
        expand: true,
        expandField: 'orderItems',
        columns: tableObj.itemColumns
      },
      time2: undefined,
      tableObj,
      statusArr,
      queryParam: {},
      loadData: (parameter) => {
        return page(Object.assign(parameter, this.queryParam)).then(res => {
          return res.data
        })
      }
    }
  },
  methods: {
    onChange (date, dateString) {
      this.queryParam.beginDate = dateString[0]
      this.queryParam.endDate = dateString[0]
    },
    handleOk () {
      this.queryPage()
    },
    restQuery () {
      this.time2 = undefined
      this.queryParam = {}
      this.queryPage()
    }
  }
}
</script>

<style scoped>

</style>
