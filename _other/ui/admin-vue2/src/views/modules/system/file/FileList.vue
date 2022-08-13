<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item label="文件名">
              <a-input v-model="queryParam.fileName" placeholder="文件名" />
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
        <a href="javascript:;" @click="downFile(row)">下载</a>
        <a-divider type="vertical" />
        <a-popconfirm title="是否删除权限" @confirm="remove(row)" okText="是" cancelText="否">
          <a-icon slot="icon" type="question-circle-o" style="color: red" />
          <a href="javascript:;" style="color: red">删除</a>
        </a-popconfirm>
      </template>
    </f-table>
    <File-edit
      @ojbk="restQuery"
      ref="fileEdit"
      @ok="handleOk" />
  </a-card>
</template>

<script>
import { tableObj } from './Filetemplate'
import { del, page } from '@/api/sys/file'
import FileEdit from './FileEdit'
import { TableMixin } from '@/mixins/TableMixin'
import TestWork from '@/views/dashboard/TestWork'
import { downBlobFile } from '@/utils/util'

export default {
  name: 'FileList',
  components: {
    TestWork,
    FileEdit
  },
  mixins: [TableMixin],
  data () {
    return {
      tableObj,
      queryParam: { fileName: '' },
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
    downFile (row) {
      downBlobFile(
        '/system/file/' + row.bucketName + '/' + row.fileName,
        this.queryParam,
        row.fileName
      )
    },
    add () {
      this.$refs.fileEdit.init('', 'add')
    },
    edit (record) {
      this.$refs.fileEdit.init(record.id, 'edit')
    },
    show (record) {
      this.$refs.fileEdit.init(record.id, 'show')
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
