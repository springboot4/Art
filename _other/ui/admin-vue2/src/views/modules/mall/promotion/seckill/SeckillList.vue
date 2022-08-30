<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
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
      <template v-slot:hours="{row}">
        <a-tag v-for="(item,index) in unixHours(row.hours) " :key="index">{{ item }}&nbsp;</a-tag>
      </template>
      <template v-slot:promotionStatus="{row}">
        <a-tag color="#f50" v-if="!row.startTime||row.startTime===undefined||!row.endTime||row.endTime===undefined">已关闭</a-tag>
        <a-tag color="#87d068" v-else-if="new Date()>=new Date(row.startTime)&&new Date()<=new Date(row.endTime)">已开始</a-tag>
        <a-tag color="#108ee9" v-else-if="new Date()<new Date(row.startTime)">未开始</a-tag>
        <a-tag color="orange" v-else-if="new Date()>new Date(row.endTime)">已结束</a-tag>
      </template>
      <template v-slot:action="{row}">
        <span v-if="new Date()<new Date(row.startTime)">
          <a href="javascript:;" @click="seckillManage(row)">管理</a>
          <a-divider type="vertical" />
        </span>
        <span v-if="new Date()<new Date(row.startTime)">
          <a href="javascript:;" @click="edit(row)">编辑</a>
          <a-divider type="vertical" />
        </span>
        <a href="javascript:;" @click="close(row)" style="color: red">关闭</a>
      </template>
    </f-table>
    <seckill-edit
      ref="seckillEdit"
      @ok="queryPage"
    />
  </a-card >
</template>

<script>
import { tableObj } from './template'
import { page, closeSeckil } from '@/api/mall/promotion/seckill'
import { TableMixin } from '@/mixins/TableMixin'
import SeckillEdit from '@/views/modules/mall/promotion/seckill/SeckillEdit'

export default {
  name: 'SeckillList',
  components: {
    SeckillEdit
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
    handleOk () {
      this.queryPage()
    },
    seckillManage (row) {
      this.$router.push({
        path: '/promotion/seckill/manage',
        query: {
          seckillId: row.id
        }
      })
    },
    edit (record) {
      this.$refs.seckillEdit.init(record.id, 'edit')
    },
    close (record) {
      closeSeckil(record.id).then(_ => {
        this.$message.info('关闭秒杀活动成功')
        this.queryPage()
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

<style scoped>

</style>
