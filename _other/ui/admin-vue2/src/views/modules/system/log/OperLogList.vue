<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item label="操作模块">
              <a-input v-model="queryParam.title" placeholder="请输入操作模块" />
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
        <a-divider type="vertical" v-if="row.businessType!==4" />
        <a-popconfirm title="是否删除权限" @confirm="remove(row)" okText="是" cancelText="否" v-if="row.businessType!==4">
          <a-icon slot="icon" type="question-circle-o" style="color: red" />
          <a href="javascript:;" style="color: red">删除</a>
        </a-popconfirm>
      </template>
    </f-table>
    <OperLog-edit
      ref="operLogEdit"
      @ok="handleOk" />
  </a-card>
</template>

<script>
import { tableObj } from './OperLogtemplate'
import { del, page } from '@/api/sys/operLog'
import OperLogEdit from './OperLogEdit'
import { TableMixin } from '@/mixins/TableMixin'

export default {
  name: 'OperLogList',
  components: {
    OperLogEdit
  },
  mixins: [TableMixin],
  data () {
    return {
      tableObj,
      queryParam: {},
      loadData: (parameter) => {
        if (this.$route.path.includes('login')) { this.queryParam.businessType = 4 } else this.queryParam.businessType = ''
        return page(Object.assign(parameter, this.queryParam)).then(res => {
          return res.data
        })
      }
    }
  },
  watch: {
    $route (to, from) {
      this.queryPage()
    }
  },
  comments: {
  },
  methods: {
    handleOk () {
      this.queryPage()
    },
    edit (record) {
      this.$refs.operLogEdit.init(record.id, 'edit')
    },
    show (record) {
      this.$refs.operLogEdit.init(record.id, 'show')
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
