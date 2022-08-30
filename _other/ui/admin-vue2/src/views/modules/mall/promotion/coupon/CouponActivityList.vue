<template>
  <a-card :bordered="false">
    <f-table
      :columns="tableObj.columns"
      :data="loadData"
      ref="table">
      <template v-slot:buttons>
        <a-button type="primary" icon="plus" @click="add">新建券活动</a-button>
      </template>
      <template v-slot:activityScope="{row}">
        <span v-if="row.activityScope==='all'">全部会员</span>
        <span v-else>指定会员</span>
      </template>
      <template v-slot:couponActivityType="{row}">
        <span v-if="row.activityScope==='registered'">新人赠券</span>
        <span v-else>精确发券</span>
      </template>
      <template v-slot:statue="{row}">
        <a-tag color="#f50" v-if="getStatue(row)==='已关闭'">已关闭</a-tag>
        <a-tag color="#87d068" v-if="getStatue(row)==='已开始'">已开始</a-tag>
        <a-tag color="#108ee9" v-if="getStatue(row)==='新建'">未开始</a-tag>
        <a-tag color="orange" v-if="getStatue(row)==='已结束'">已结束</a-tag>
      </template>
      <template v-slot:action="{row}">
        <span v-if="getStatue(row)==='已开始'">
          <a href="javascript:;" @click="close(row)" style="color: red">关闭</a>
          <a-divider type="vertical" />
        </span>
        <span>
          <a href="javascript:;" @click="show(row)">查看</a>
        </span>
      </template>
    </f-table>
    <CouponActivity-edit
      ref="couponActivityEdit"
      @ok="handleOk" />
  </a-card>
</template>

<script>
import { tableObj } from './CouponActivitytemplate'
import { close, page } from '@/api/mall/promotion/couponActivity'
import CouponActivityEdit from './CouponActivityEdit'
import { TableMixin } from '@/mixins/TableMixin'

export default {
  name: 'CouponActivityList',
  components: {
    CouponActivityEdit
  },
  mixins: [TableMixin],
  data () {
    return {
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
    getStatue (row) {
      if (!row.startTime && !row.endTime) return '已关闭'

      const now = new Date()

      if (now < new Date(row.startTime)) {
        // 如果当前时间早于开始时间 那么是新建状态
        return '新建'
      } else if (new Date(row.endTime) < now) {
        // 如果结束时间早于当前时间 那么是结束状态
        return '已结束'
      } else if (now < new Date(row.endTime)) {
        // 如果当前时间早于结束时间 那么是开始状态
        return '已开始'
      }

      return '已关闭'
    },
    handleOk () {
      this.queryPage()
    },
    close (record) {
      close(record.id).then(_ => {
        this.$message.info('关闭成功')
        this.queryPage()
      })
    },
    add () {
      this.$refs.couponActivityEdit.init('', 'add')
    },
    show (record) {
      console.log('record:', record)
      this.$refs.couponActivityEdit.init(record.id, 'show')
    }
  }
}
</script>

<style scoped>

</style>
