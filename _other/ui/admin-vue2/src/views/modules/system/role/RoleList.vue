<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item label="角色名称">
              <a-input v-model="queryParam.roleName" placeholder="角色名称" />
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
      :cellClickEvent="show"
      :columns="tableObj.columns"
      :data="loadData"
      ref="table">
      <template v-slot:buttons>
        <a-button type="primary" icon="plus" @click="add">新建</a-button>
      </template>
      <template v-slot:avatar="{row}">
        <a-avatar size="large" :src="row.avatar" v-if="row.avatar" />
        <a-avatar size="large" icon="user" v-else />
      </template>
      <template v-slot:action="{row}">
        <a href="javascript:" @click="show(row)">查看</a>
        <a-divider type="vertical" />
        <a href="javascript:" @click="edit(row)">编辑</a>
        <a-divider type="vertical" />
        <a-popconfirm title="是否删除权限" @confirm="deleteItem(row)" okText="是" cancelText="否">
          <a-icon slot="icon" type="question-circle-o" style="color: red" />
          <a href="javascript:;" style="color: red">删除</a>
        </a-popconfirm>
      </template>
    </f-table>

    <role-add-or-update
      ref="modalForm"
      @ok="handleOk"/>

  </a-card>
</template>

<script>

import { TableMixin } from '@/mixins/TableMixin'
import { tableObj } from './template'
import { PageRole, deleteRoleById } from '@/api/sys/role'
import RoleAddOrUpdate from './RoleAddOrUpdate'

export default {
  name: 'RoleList',
  components: {
    RoleAddOrUpdate
  },
  mixins: [TableMixin],
  data () {
    return {
      tableObj,
      queryParam: {
        username: '',
        mobile: ''
      },
      loadData: (parameter) => {
        console.log('param:', this.queryParam)
        return PageRole(
          Object.assign(parameter, this.queryParam)
        ).then(res => {
          return res.data
        })
      }
    }
  },
  methods: {
    deleteItem (record) {
      deleteRoleById(record.roleId).then(_ => {
        this.$message.info('删除成功')
        this.queryPage()
      }).catch(err => {
        this.$message.error(err.msg)
      })
    },
    handleOk () {
      this.queryPage()
    },
    resetQuery () {
      this.queryParam = {}
      this.queryPage()
    },
    show (record) {
      this.$refs.modalForm.init(record.roleId || record.row.roleId, 'show')
    },
    edit (record) {
      this.$refs.modalForm.init(record.roleId, 'edit')
    },
    add () {
      this.$refs.modalForm.init('', 'add')
    }
  },
  created () {

  }
}
</script>
