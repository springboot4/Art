<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item label="字典编码">
              <a-input v-model="queryParam.type" placeholder="字典编码" />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <a-form-item label="字典类型">
              <a-select v-model="queryParam.systemFlag" @change="queryPage">
                <a-select-option
                  v-for="item in dictTypes"
                  :key="item.id"
                  :label="item.label"
                  :value="item.value">
                  {{ item.label }}
                </a-select-option>
              </a-select>
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
      <template v-slot:systemFlag="{row}">
        <a-tag v-if="row.systemFlag===dictTypes[0].value" color="green">{{ dictTypes[0].label }}</a-tag>
        <a-tag v-if="row.systemFlag===dictTypes[1].value" color="cyan">{{ dictTypes[1].label }}</a-tag>
      </template>
      <template v-slot:action="{row}">
        <a href="javascript:" @click="show(row)">查看</a>
        <a-divider type="vertical" />
        <a href="javascript:" @click="edit(row)">编辑</a>
        <a-divider type="vertical" />
        <a href="javascript:" @click="itemList(row)">字典项</a>
        <a-divider type="vertical" />
        <a-popconfirm title="是否删除权限" @confirm="remove(row)" okText="是" cancelText="否">
          <a-icon slot="icon" type="question-circle-o" style="color: red" />
          <a href="javascript:;" style="color: red">删除</a>
        </a-popconfirm>
      </template>
    </f-table>
    <Dict-edit
      ref="dictEdit"
      @ok="handleOk" />
    <dict-item-list
      ref="dictItemList"
    />
  </a-card>
</template>

<script>
import { tableObj } from './template'
import { del, getDictItemsByType, page } from '@/api/sys/dict'
import DictEdit from './DictEdit'
import DictItemList from './DictItemList'
import { TableMixin } from '@/mixins/TableMixin'

export default {
  name: 'DictList',
  components: {
    DictEdit,
    DictItemList
  },
  mixins: [TableMixin],
  data () {
    return {
      tableObj,
      queryParam: {
        type: '',
        systemFlag: ''
      },
      dictTypes: [],
      loadData: (parameter) => {
        return page(Object.assign(parameter, this.queryParam)).then(res => {
          return res.data
        })
      }
    }
  },
  created () {
    getDictItemsByType('dict_type').then(res => {
      this.dictTypes = res.data
    })
  },
  methods: {
    restQuery () {
      this.queryParam = {}
      this.queryPage()
    },
    handleOk () {
      this.queryPage()
    },
    add () {
      this.$refs.dictEdit.init('', 'add')
    },
    edit (record) {
      this.$refs.dictEdit.init(record.id, 'edit')
    },
    show (record) {
      this.$refs.dictEdit.init(record.id, 'show')
    },
    itemList (record) {
      this.$refs.dictItemList.list(record)
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

<style scoped>

</style>
