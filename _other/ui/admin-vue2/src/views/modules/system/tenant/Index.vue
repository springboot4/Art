<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item label="租户名称">
              <a-input v-model="queryParam.name" placeholder="租户名称" />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <a-button type="primary" @click="queryPage">查询</a-button>
            <a-button style="margin-left: 8px" @click="resetQuery">重置</a-button>
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
        <span v-if="row.status===0">正常</span>
        <span v-else>关闭</span>
      </template>
      <template v-slot:type="{row}">
        <span v-if="row.id==0">系统套餐</span>
        <span v-else>普通套餐</span>
      </template>
      <template v-slot:action="{row}">
        <a href="javascript:" @click="show(row)">查看</a>
        <a-divider type="vertical" />
        <a href="javascript:" @click="edit(row)">编辑</a>
        <a-divider type="vertical" />
        <a-popconfirm title="是否删除" @confirm="remove(row)" okText="是" cancelText="否">
          <a-icon slot="icon" type="question-circle-o" style="color: red" />
          <a href="javascript:;" style="color: red">删除</a>
        </a-popconfirm>
      </template>
    </f-table>
    <SysTenant-edit
      ref="sysTenantEdit"
      @ok="handleOk"/>
  </a-card>
</template>

<script>
import { tableObj } from './Template'
import { page, del } from '@/api/sys/tenant'
import SysTenantEdit from './TenantEdit'
import { TableMixin } from '@/mixins/TableMixin'

export default {
  name: 'SysTenantList',
  components: {
    SysTenantEdit
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
    resetQuery () {
      this.queryParam = {}
      this.queryPage()
    },
    handleOk () {
      this.queryPage()
    },
    add () {
      this.$refs.sysTenantEdit.init('', 'add')
    },
    edit (record) {
      this.$refs.sysTenantEdit.init(record.id, 'edit')
    },
    show (record) {
      this.$refs.sysTenantEdit.init(record.id, 'show')
    },
    remove (record) {
      del(record.id).then(_ => {
        this.$message.info('删除成功')
        this.queryPage()
      }).catch(e => {
        this.$message.error(e.msg)
      })
    }
  }
}
</script>
