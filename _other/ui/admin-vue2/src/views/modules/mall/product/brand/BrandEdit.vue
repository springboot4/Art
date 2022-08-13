<template>
  <a-modal
    :title="title"
    :width="modalWidth"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :maskClosable="false"
    @cancel="handleCancel"
  >
    <a-form-model
      ref="form"
      :model="form"
      :rules="rules"
      :label-col="labelCol"
      :wrapper-col="wrapperCol"
    >
      <a-form-model-item label="主键" prop="id" hidden="true">
        <a-input v-model="form.id" :disabled="true" />
      </a-form-model-item>
      <a-form-model-item
        label="品牌名称"
        prop="name"
      >
        <a-input v-model="form.name" :disabled="showable" />
      </a-form-model-item>
      <a-form-model-item
        label="排序"
        prop="sort"
      >
        <a-input v-model="form.sort" :disabled="showable" />
      </a-form-model-item>
      <a-form-model-item
        label="LOGO图片"
        prop="logoUrl"
      >
        <a-upload
          name="file"
          action="/api/system/file/add"
          :headers="headers"
          list-type="picture-card"
          class="avatar-uploader"
          :customRequest="uploadFunc"
          :showUploadList="false">
          <img v-if="options.img" id="logoImg" :src="options.img" alt="logo" width="150px"/>
          <div v-else>
            <a-icon type="plus" />
            <div class="ant-upload-text">
              上传图片
            </div>
          </div>
        </a-upload>
      </a-form-model-item>
    </a-form-model>

    <template v-slot:footer>
      <a-button key="cancel" @click="handleCancel">取消</a-button>
      <a-button v-if="!showable" key="forward" :loading="confirmLoading" type="primary" @click="handleOk">保存</a-button>
    </template>
  </a-modal>
</template>

<script>
import { FormMixin } from '@/mixins/FormMixin'
import { add, get, update } from '@/api/mall/product/brand'
import { add as addFile } from '@/api/sys/file'
import { handleImg } from '@/utils/util'

export default {
  name: 'BrandEdit',
  mixins: [FormMixin],
  data () {
    return {
      options: {
        img: undefined
      },
      form: {
        id: null,
        name: null,
        logoUrl: null,
        sort: null,
        gmtCreate: null,
        gmtModified: null
      },
      rules: {}
    }
  },
  methods: {
    uploadFunc (file) {
      const formData = new FormData()
      formData.append('file', file.file)
      addFile(formData).then(res => {
        console.log('res:', res)
        this.form.logoUrl = res.data.data.url
        this.options.img = res.data.data.url
        handleImg(res.data.data.url, 'logoImg')
        file.status = 'done'
        this.$message.success('上传成功')
      }).catch(_ => {
        this.$message.error('上传失败')
      })
    },
    edit (id, type) {
      this.resetForm()
      this.options.img = undefined
      if (['edit', 'show'].includes(type)) {
        this.confirmLoading = true
        get(id).then(res => {
          this.form = res.data
          this.options.img = this.form.logoUrl
          handleImg(this.form.logoUrl, 'logoImg')
          this.confirmLoading = false
        })
      } else {
        this.confirmLoading = false
      }
    },
    handleOk () {
      this.$refs.form.validate(async valid => {
        if (valid) {
          this.confirmLoading = true
          if (this.type === 'add') {
            await add(this.form)
          } else if (this.type === 'edit') {
            await update(this.form)
          }
          setTimeout(() => {
            this.confirmLoading = false
            this.$emit('ok')
            this.visible = false
          }, 200)
        } else {
          return false
        }
      })
    },
    resetForm () {
      this.$nextTick(() => {
        this.$refs.form.resetFields()
      })
    }
  }
}
</script>

<style scoped>

</style>
