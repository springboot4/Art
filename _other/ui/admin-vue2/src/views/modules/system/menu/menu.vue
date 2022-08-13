<template>
  <a-card :bordered="false">
    <f-table
      :columns="tableObj.columns"
      :data="loadData"
      :showPagination="false"
      :expandAllTree="false"
      :seq="false"
      ref="table"
    >
      <template v-slot:buttons>
        <a-button @click="addData" icon="plus" type="primary" v-action="'sys:menu:save'">新建</a-button>
      </template>
      <span slot="type" slot-scope="{ row,text }">
        <span v-show="String(text) === '0'">菜单</span>
        <span v-show="String(text) === '1'">按钮</span>
      </span>
      <span slot="action" slot-scope="{ row }">
        <a href="javascript:;" @click="show(row)">查看</a>
        <a-divider type="vertical" />
        <a href="javascript:;" @click="edit(row)">编辑</a>
        <a-divider type="vertical" />
        <a-popconfirm title="是否删除权限" @confirm="deleteItem(row)" okText="是" cancelText="否">
          <a-icon slot="icon" type="question-circle-o" style="color: red" />
          <a href="javascript:;" style="color: red">删除</a>
        </a-popconfirm>
      </span>
    </f-table>
    <permission-add-or-update
      ref="modalForm"
      @ok="handleOk" />
  </a-card>
</template>

<script>
import { delObj, getAllMenuTree } from '@/api/sys/menu'
import permissionAddOrUpdate from './permissionAddOrUpdate'
import { tableObj } from './template'
import { TableMixin } from '@/mixins/TableMixin'

export default {
  name: 'Menu',
  mixins: [TableMixin],
  components: {
    permissionAddOrUpdate
  },
  data () {
    return {
      tableObj,
      loading: false,
      tableData: [],
      loadData: (parameter) => {
        return getAllMenuTree(
          Object.assign(parameter, this.queryParam)
        ).then(res => {
          console.log('res', res)
          return res.data
        }).catch(e => {})
      }
    }
  },
  methods: {
    deleteItem (record) {
      delObj(record.id).then(_ => {
        this.$message.info('删除成功')
        this.queryPage()
      }).catch(err => {
        this.$message.error(err.msg)
      })
    },
    edit (row) {
      this.$refs.modalForm.init(row, 'edit')
    },
   show (row) {
     this.$refs.modalForm.init(row, 'show')
   },
    handleOk () {
      this.queryPage()
    }
  }
}
</script>

<style scoped>

</style>
