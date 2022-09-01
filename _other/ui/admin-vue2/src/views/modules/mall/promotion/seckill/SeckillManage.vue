<template>
  <a-card :bordered="false">
    <a-table :columns="columns" :data-source="tableData" :pagination="false" bordered>
      <span slot="hours" slot-scope="text">
        <a-tag v-for="(item,index) in text " :key="index">{{ item }}&nbsp;</a-tag>
      </span>
    </a-table>
    <br /><br />
    <a-button type="primary" icon="plus" @click="add">选择商品</a-button>
    <br /><br />
    <a-tabs @change="callback" type="card" :activeKey="activeKey">
      <a-tab-pane :key="index" :tab="item" v-for="(item,index) in hours">
        <a-table :columns="skuColumns" :data-source="selectSkuList" :pagination="false" bordered>
          <span slot="originalPrice" slot-scope="text">
            {{ text / 100 }}
          </span>
          <span slot="price" slot-scope="text">
            {{ text / 100 }}
          </span>
          <span slot="action" slot-scope="text, record">
            <a-popconfirm title="是否删除?" @confirm="deleteItem(record)" okText="是" cancelText="否">
              <a-icon slot="icon" type="question-circle-o" style="color: red" />
              <a href="javascript:;" style="color: red">删除</a>
            </a-popconfirm>
          </span>
        </a-table>
      </a-tab-pane>
    </a-tabs>
    <seckill-manage-edit ref="seckillManageEdit" @ok="queryData" />
  </a-card>
</template>

<script>
import { FormMixin } from '@/mixins/FormMixin'
import { deleteApply, get } from '@/api/mall/promotion/seckill'
import SeckillManageEdit from '@/views/modules/mall/promotion/seckill/SeckillManageEdit'
const columns = [
  {
    title: '活动名称',
    dataIndex: 'promotionName',
    key: 'promotionName',
    width: 180
  },
  {
    title: '活动开始时间',
    dataIndex: 'startTime',
    key: 'startTime',
    width: 180
  },
  {
    title: '报名截至时间',
    dataIndex: 'applyEndTime',
    key: 'applyEndTime',
    width: '180px'
  },
  {
    title: '时间场次',
    dataIndex: 'hours',
    key: 'hours',
    width: '200px',
    scopedSlots: { customRender: 'hours' },
    ellipsis: true
  }
]
const skuColumns = [
  {
    title: '商品名称',
    dataIndex: 'goodsName',
    key: 'goodsName',
    width: 180
  },
  {
    title: '原始价格',
    dataIndex: 'originalPrice',
    key: 'originalPrice',
    width: 180,
    scopedSlots: { customRender: 'originalPrice' }
  },
  {
    title: '促销价格',
    dataIndex: 'price',
    key: 'price',
    width: '180',
    scopedSlots: { customRender: 'price' }
  },
  {
    title: '库存',
    dataIndex: 'quantity',
    key: 'quantity',
    width: '100'
  },
  {
    title: '操作',
    key: 'action',
    scopedSlots: { customRender: 'action' }
  }
]
export default {
  name: 'SeckillManage',
  mixins: [FormMixin],
  components: { SeckillManageEdit },
  data () {
    return {
      columns,
      skuColumns,
      tableData: [],
      queryParam: {},
      hours: [],
      skuList: [],
      selectSkuList: [],
      activeKey: 0
    }
  },
  watch: {
    $route () {
      this.queryParam.seckillId = this.$route.query.seckillId
      this.queryData()
    }
  },
  created () {
    this.queryData()
  },
  methods: {
    add () {
      this.$refs.seckillManageEdit.init(this.$route.query.seckillId || this.queryParam.seckillId, 'add', this.hours[this.activeKey].split(':')[0])
    },
    deleteItem (row) {
      deleteApply(row.seckillId, row.id).then(_ => {
        this.$message.success('删除成功')
        this.queryData()
      })
    },
    callback (key) {
      this.activeKey = key
      const tempHour = this.hours[this.activeKey]
      this.selectSkuList = this.skuList.filter(item => (item.timeLine + ':00') === tempHour)
      console.log(this.selectSkuList)
    },
    queryData () {
      const seckillId = this.$route.query.seckillId || this.queryParam.seckillId
      if (!seckillId && !this.queryParam.seckillId) return
      get(seckillId).then(res => {
        console.log('res.data', res.data)
        const arr = []
        arr.push(res.data)
        this.tableData = arr
        this.hours = this.unixHours(this.tableData[0].hours)
        this.tableData[0].hours = this.hours
        this.skuList = this.tableData[0].seckillApplyList
        this.callback(0)
        this.confirmLoading = false
        console.log('this.tableData:', this.tableData)
      })
    },
    unixHours (item) {
      // 处理小时场次
      const hourArr = item.split(',')
      for (let i = 0; i < hourArr.length; i++) {
        hourArr[i] += ':00'
      }
      return hourArr
    }
  }
}
</script>
