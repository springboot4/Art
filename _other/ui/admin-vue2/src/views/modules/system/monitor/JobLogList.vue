<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item label="任务名称">
              <a-input v-model="queryParam.jobName" placeholder="请输入任务名称" />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <a-form-item label="任务组名">
              <a-input v-model="queryParam.jobGroup" placeholder="请输入任务组名" />
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
      <template v-slot:action="{row}">
        <a href="javascript:;" @click="show(row)">查看</a>
      </template>
    </f-table>
    <JobLog-edit
      ref="jobLogEdit"
      @ok="handleOk" />
  </a-card>
</template>

<script>
import { tableObj } from './JobLogtemplate'
import { del, page } from '@/api/sysMonitor/jobLog'
import JobLogEdit from './JobLogEdit'
import { TableMixin } from '@/mixins/TableMixin'

export default {
  name: 'JobLogList',
  components: {
    JobLogEdit
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
  watch: {
    $route () {
      this.queryParam.jobName = this.$route.query.jobName
      this.queryParam.jobGroup = this.$route.query.jobGroup
      this.queryPage()
    }
  },
  methods: {
    handleOk () {
      this.queryPage()
    },
    add () {
      this.$refs.jobLogEdit.init('', 'add')
    },
    edit (record) {
      this.$refs.jobLogEdit.init(record.id, 'edit')
    },
    show (record) {
      this.$refs.jobLogEdit.init(record.jobLogId, 'show')
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
