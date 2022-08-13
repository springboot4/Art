<template>
  <a-card :bordered="false">
    <f-table
      :columns="tableObj.columns"
      :data="loadData"
      :showPagination="false"
      :seq="false"
      ref="table"
    >
      <template v-slot:buttons>
        <a-button @click="addData" icon="plus" type="primary" v-action="'sys:menu:save'">新建</a-button>
      </template>
      <template v-slot:action="{row}">
        <a href="javascript:;" @click="show(row)">查看</a>
        <a-divider type="vertical" />
        <a href="javascript:;" @click="edit(row)">编辑</a>
        <a-divider type="vertical" />
        <a-popconfirm title="是否删除权限" @confirm="deleteItem(row)" okText="是" cancelText="否">
          <a-icon slot="icon" type="question-circle-o" style="color: red" />
          <a href="javascript:;" style="color: red">删除</a>
        </a-popconfirm>
      </template>
    </f-table>

    <dept-add-or-update
      ref="modalForm"
      @ok="handleOk" />

  </a-card>

</template>

<script>
import DeptAddOrUpdate from '@/views/modules/system/dept/DeptAddOrUpdate'
import { TableMixin } from '@/mixins/TableMixin'
import { tableObj } from '@/views/modules/system/dept/template'
import { deleteById, getDeptTree } from '@/api/sys/dept'

export default {
  name: 'Menu',
  mixins: [TableMixin],
  components: {
    DeptAddOrUpdate
  },
  data () {
    return {
      tableObj,
      loadData: (parameter) => {
        return getDeptTree(
          Object.assign(parameter, this.queryParam)
        ).then(res => {
          const temp = []
          temp.push(res.data)
          return temp
        })
      }
    }
  },
  methods: {
    show (row) {
      this.$refs.modalForm.init(row.deptId, 'show')
    },
    edit (row) {
      this.$refs.modalForm.init(row.deptId, 'edit')
    },
    deleteItem (row) {
      deleteById(row.deptId).then(res => {
        this.$message.success('删除成功')
        this.queryPage()
      }).catch(err => {
        this.$message.error(err.response.data.message)
      })
    },
    handleOk () {
      this.queryPage()
    }
  }
}

</script>
