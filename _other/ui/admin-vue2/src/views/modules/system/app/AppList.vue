<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item label="应用名">
              <a-input v-model="queryParam.name" placeholder="应用名" />
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
      <template v-slot:action="{row}">
        <a href="javascript:;" @click="show(row)">查看</a>
        <a-divider type="vertical" />
        <a href="javascript:;" @click="edit(row)">编辑</a>
        <a-divider type="vertical" />
        <a-popconfirm title="是否删除权限" @confirm="remove(row)" okText="是" cancelText="否">
          <a-icon slot="icon" type="question-circle-o" style="color: red" />
          <a href="javascript:;" style="color: red">删除</a>
        </a-popconfirm>
      </template>
    </f-table>
    <sys-app-edit
      ref="sysAppEdit"
      @ok="handleOk"/>
  </a-card>
</template>

<script>
import { tableObj } from './template'
import { page, del } from '@/api/sys/app'
import SysAppEdit from './AppEdit'
import { TableMixin } from '@/mixins/TableMixin'

export default {
  name: 'SysAppList',
  components: {
    SysAppEdit
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
    add () {
      this.$refs.sysAppEdit.init('', 'add')
    },
    edit (record) {
      this.$refs.sysAppEdit.init(record.id, 'edit')
    },
    show (record) {
      this.$refs.sysAppEdit.init(record.id, 'show')
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
