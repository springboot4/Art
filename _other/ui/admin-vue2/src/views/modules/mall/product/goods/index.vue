<template>
  <div>
    <a-card>
      <f-table
        :expandConfig="expandConfig"
        :columns="tableObj.columns"
        :data="loadData"
        ref="table">
        <template v-slot:picUrl="{row}">
          <img :src="row.picUrl" width="40" />
        </template>
        <template v-slot:originPrice="{row}">
          {{ row.originPrice | moneyFormatter() }}
        </template>
        <template v-slot:price="{row}">
          {{ row.price | moneyFormatter() }}
        </template>
        <template v-slot:detail="{row}">
          <a href="javascript:" @click="spuInfo(row.detail)">详情</a>
        </template>
        <template v-slot:action="{row}">
          <a-popconfirm title="是否删除权限" @confirm="deleteItem(row)" okText="是" cancelText="否">
            <a href="javascript:;" style="color: red">删除</a>
          </a-popconfirm>
        </template>
      </f-table>
    </a-card>
    <a-modal v-model="visible" title="商品详情" @ok="spuInfo" :footer="null">
      <div v-html="goodDetail"></div>
    </a-modal>
  </div>
</template>

<script>
import { tableObj } from '@/views/modules/mall/product/goods/template'
import { del, page } from '@/api/mall/product/goods'
import { TableMixin } from '@/mixins/TableMixin'
export default {
  name: 'GoodsIndex',
  mixins: [TableMixin],
  data () {
    return {
      expandConfig: {
        expand: true,
        expandField: 'skuList',
        columns: tableObj.skuColumns
      },
      tableObj,
      tableData: [],
      tableLoading: false,
      current: 1,
      pageSize: 20,
      defaultCurrent: 1,
      total: 0,
      visible: false,
      goodDetail: undefined,
      loadData: (parameter) => {
        const params = {
          current: this.current,
          pageSize: this.pageSize
        }
        return page(Object.assign(parameter, params)).then(res => {
          return res.data
        })
      }
    }
  },
  methods: {
    deleteItem (row) {
      del(row.id).then(_ => {
        this.$message.success('删除成功')
        this.restQuery()
      })
    },
    spuInfo (info) {
      if (this.visible) {
        this.visible = false
        this.goodDetail = undefined
      } else {
        this.visible = true
        this.goodDetail = info
      }
    }
  }
}
</script>

<style scoped>

</style>
