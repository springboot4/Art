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
        <a-button type="primary" icon="plus" @click="add">新建</a-button>
      </template>
      <template v-slot:status="{row}">
        <a-switch :checked="checkSwitch(row)" @change="onChange(row)" />
      </template>
      <template v-slot:action="{row}">
        <a href="javascript:;" @click="show(row)">查看</a>
        <a-divider type="vertical" />
        <a href="javascript:;" @click="edit(row)">编辑</a>
        <a-divider type="vertical" />
        <a-dropdown>
          <a class="ant-dropdown-link" @click="e => e.preventDefault()">
            更多
            <a-icon type="down" />
          </a>
          <a-menu slot="overlay">
            <a-menu-item>
              <a-popconfirm :title="`确定执行一次${row.jobName}任务?`" @confirm="handleRun(row)" okText="是" cancelText="否">
                <a href="javascript:;">执行一次</a>
              </a-popconfirm>
            </a-menu-item>
            <a-menu-item>
              <a @click="handleJobLog(row)">执行日志</a>
            </a-menu-item>
            <a-menu-item>
              <a-popconfirm title="是否删除权限" @confirm="remove(row)" okText="是" cancelText="否">
                <a href="javascript:;" style="color: red">删除</a>
              </a-popconfirm>
            </a-menu-item>
          </a-menu>
        </a-dropdown>
      </template>
    </f-table>
    <Job-edit
      ref="jobEdit"
      @ok="handleOk" />
  </a-card>
</template>

<script>
import { tableObj } from './template'
import { changeStatus, del, page, runJob } from '@/api/sysMonitor/job'
import JobEdit from './JobEdit'
import { TableMixin } from '@/mixins/TableMixin'

export default {
  name: 'JobList',
  components: {
    JobEdit
  },
  mixins: [TableMixin],
  data () {
    return {
      tableObj,
      queryParam: {
        jobName: ''
      },
      loadData: (parameter) => {
        return page(Object.assign(parameter, this.queryParam)).then(res => {
          console.log('res', res)
          return res.data
        })
      }
    }
  },
  methods: {
    handleJobLog (row) {
      this.$router.push({
        path: '/sysMonitor/jobLog',
        query: {
          jobName: row.jobName,
          jobGroup: row.jobGroup
        }
      })
    },
    handleRun (row) {
      runJob(row).then(res => {
        this.$message.success('执行成功')
      })
    },
    checkSwitch (row) {
      return row.status === '0'
    },
    onChange (checked) {
      if (checked.status === '0') {
        checked.status = '1'
      } else {
        checked.status = '0'
      }
      changeStatus(checked).then(res => {
        this.queryPage()
      })
    },
    handleOk () {
      this.queryPage()
    },
    add () {
      this.$refs.jobEdit.init('', 'add')
    },
    edit (record) {
      this.$refs.jobEdit.init(record.jobId, 'edit')
    },
    show (record) {
      this.$refs.jobEdit.init(record.jobId, 'show')
    },
    remove (record) {
      del(record.jobId).then(_ => {
        this.$message.info('删除成功')
        this.queryPage()
      })
    }
  }
}
</script>

<style scoped>

</style>
