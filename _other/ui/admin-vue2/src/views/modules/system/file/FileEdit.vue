<template>
  <a-modal
    :title="title"
    :width="modalWidth"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :maskClosable="false"
    @cancel="handleOk"
  >
    <a-upload
      name="file"
      :multiple="false"
      action="/api/system/file/add"
      :headers="headers"
      :fileList="fileList"
      @change="handleChange"
    >
      <a-button>
        <a-icon type="upload" />
        上传文件
      </a-button>
    </a-upload>
    <template v-slot:footer>
      <a-button v-if="!showable" key="forward" :loading="confirmLoading" type="primary" @click="handleOk">确定
      </a-button>
    </template>
  </a-modal>
</template>

<script>
import { FormMixin } from '@/mixins/FormMixin'
import { get } from '@/api/sys/file'
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'

export default {
  name: 'FileEdit',
  mixins: [FormMixin],
  data () {
    return {
      form: {
        id: null,
        fileName: null,
        bucketName: null,
        original: null,
        type: null,
        fileSize: null,
        delFlag: null,
        createTime: null,
        updateTime: null,
        createBy: null,
        updateBy: null
      },
      fileList: [],
      headers: {
        Authorization: 'Bearer ' + Vue.ls.get(ACCESS_TOKEN)
      }
    }
  },
  methods: {
    handleChange (info) {
      this.fileList = info.fileList
      if (info.file.status !== 'uploading') {
        console.log(info.file, info.fileList)
      }
      if (info.file.status === 'done') {
        this.$message.success(`${info.file.name} 上传成功!`)
      } else if (info.file.status === 'error') {
        this.$message.error(`${info.file.name}  上传失败!`)
      }
    },
    edit (id, type) {
      if (['edit', 'show'].includes(type)) {
        this.confirmLoading = true
        get(id).then(res => {
          this.form = res.data
          this.confirmLoading = false
        })
      } else {
        this.confirmLoading = false
      }
    },
    handleOk () {
      this.fileList = []
      this.handleCancel()
      this.$emit('ojbk')
    }
  }
}
</script>

<style scoped>

</style>
