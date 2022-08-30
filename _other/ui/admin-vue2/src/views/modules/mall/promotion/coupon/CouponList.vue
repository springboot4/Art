<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="12">
            <a-form-item >
              <a-input v-model="queryParam.couponName" placeholder="优惠券名称" />
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
      :columns="tableObj.columns"
      :data="loadData"
      ref="table">
      <template v-slot:buttons>
        <a-button type="primary" icon="plus" @click="add">新建优惠券</a-button>
      </template>
      <template v-slot:scopeType="{row}">
        <span v-if="row.scopeType==='all'">全品类</span>
        <span v-else>指定商品</span>
      </template>
      <template v-slot:getType="{row}">
        <span v-if="row.getType==='free'">直接领取</span>
        <span v-else>活动发放</span>
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
        <span v-if="getStatue(row)==='已关闭'">
          <a href="javascript:;" @click="remove(row)" style="color: red">删除</a>
          <a-divider type="vertical" />
        </span>
        <span v-if="getStatue(row)==='新建'">
          <a href="javascript:;" @click="edit(row)" >编辑</a>
          <a-divider type="vertical" />
        </span>
        <span>
          <a href="javascript:;" @click="show(row)" >查看</a>
        </span>
      </template>
    </f-table>
    <Coupon-edit
      ref="couponEdit"
      @ok="handleOk" />
  </a-card>
</template>

<script>
import { tableObj } from './template'
import { page, del, close } from '@/api/mall/promotion/coupon'
import CouponEdit from './CouponEdit'
import { TableMixin } from '@/mixins/TableMixin'

export default {
  name: 'CouponList',
  components: {
    CouponEdit
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
      const register = row.rangeDayType != null &&
        row.rangeDayType === 'dynamictime' &&
        (row.effectiveDays != null && row.effectiveDays > 0 && row.effectiveDays <= 365)
      if (register) {
        return '已开始'
      }
      if (row.endTime == null) {
        return row.startTime != null ? '已开始' : '已关闭'
      }

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
    add () {
      this.$refs.couponEdit.init('', 'add')
    },
    edit (record) {
      this.$refs.couponEdit.init(record.id, 'edit')
    },
    show (record) {
      this.$refs.couponEdit.init(record.id, 'show')
    },
    close (record) {
      close(record.id).then(_ => {
        this.$message.info('关闭成功')
        this.queryPage()
      })
    },
    remove (record) {
      del(record.id).then(_ => {
        this.$message.info('删除成功')
        this.queryPage()
      })
    }
  }
}
</script>

<style scoped>

</style>
