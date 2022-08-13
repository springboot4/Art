<template>
  <a-drawer
    :visible="visible"
    title="字典列表"
    :maskClosable="true"
    :width="1000"
    placement="right"
    :closable="true"
    @close="handleCancel"
  >
    <f-table
      :columns="tableObj.columns"
      :data="loadData"
      ref="itemTable">
      <template v-slot:buttons>
        <a-button type="primary" icon="plus" @click="add">新建</a-button>
      </template>
      <template v-slot:action="{row}">
        <a href="javascript:" @click="show(row)">查看</a>
        <a-divider type="vertical" />
        <a href="javascript:" @click="edit(row)">编辑</a>
        <a-divider type="vertical" />
        <a-popconfirm title="是否删除权限" @confirm="remove(row)" okText="是" cancelText="否">
          <a-icon slot="icon" type="question-circle-o" style="color: red" />
          <a href="javascript:;" style="color: red">删除</a>
        </a-popconfirm>
      </template>
    </f-table>
    <dict-item-edit
      ref="dictItemEdit"
      @ok="init"
    />
  </a-drawer>
</template>

<script>
import { itemDel, itemPage } from '@/api/sys/dictItem'
import DictItemEdit from './DictItemEdit'
import { TableMixin } from '@/mixins/TableMixin'
import { tableObj } from './ItemTemplate'

export default {
  name: 'DictItemList',
  components: {
    DictItemEdit
  },
  mixins: [TableMixin],
  data () {
    return {
      tableObj,
      visible: false,
      dict: '',
      loadData: (parameter) => {
        return itemPage(Object.assign(parameter, this.queryParam)).then(res => {
          return res.data
        })
      }
    }
  },
  methods: {
    // 展示列表
    list (dict) {
      this.dict = dict
      this.queryParam.dictId = dict.id
      this.init()
    },
    init () {
      this.visible = true
      this.$nextTick(() => {
        this.$refs.itemTable.refresh(false)
      })
    },
    add () {
      this.$refs.dictItemEdit.init(this.dict, 'add')
    },
    edit (record) {
      this.$refs.dictItemEdit.init(record, 'edit')
    },
    show (record) {
      this.$refs.dictItemEdit.init(record, 'show')
    },
    remove (record) {
      itemDel(record.id).then(() => {
        this.$message.info('删除成功')
        this.init()
      }).catch(e => { this.$message.error(e.msg) })
    },
    handleCancel () {
      this.visible = false
    }
  }
}
</script>

<style scoped>

</style>
