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
        <a-input v-model="form.id" :disabled="showable" />
      </a-form-model-item>
      <a-form-model-item
        label="上级分类"
        v-if="type==='add'"
      >
        <span>{{ pNode.name }}</span>
      </a-form-model-item>
      <a-form-model-item
        label="商品分类名称"
        prop="name"
      >
        <a-input v-model="form.name" :disabled="showable" />
      </a-form-model-item>
      <a-form-model-item
        label="分类图标"
        prop="iconUrl"
      >
        <a-upload
          name="file"
          action="/api/system/file/add"
          :headers="headers"
          list-type="picture-card"
          class="avatar-uploader"
          :customRequest="uploadFunc"
          :showUploadList="false">
          <img v-if="options.img" id="iconUrl" :src="options.img" alt="logo" width="150px"/>
          <div v-else>
            <a-icon type="plus" />
            <div class="ant-upload-text">上传图片</div>
          </div>
        </a-upload>
      </a-form-model-item>
      <a-form-model-item
        label="显示状态"
        prop="visible"
      >
        <a-radio-group v-model="form.visible">
          <a-radio :value="0">隐藏</a-radio>
          <a-radio :value="1">显示</a-radio>
        </a-radio-group>
      </a-form-model-item>
      <a-form-model-item
        label="排序"
        prop="sort"
      >
        <a-input-number v-model="form.sort" :disabled="showable" style="width: 100%"/>
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
import { add, get, update } from '@/api/mall/product/category'
import { add as addFile } from '@/api/sys/file'
import { handleImg } from '@/utils/util'

export default {
  name: 'CategoryEdit',
  mixins: [FormMixin],
  data () {
    return {
      form: {
        id: null,
        name: null,
        parentId: null,
        level: null,
        iconUrl: null,
        sort: null,
        visible: null,
        createTime: null,
        updateTime: null,
        createBy: null,
        updateBy: null
      },
      rules: {},
      options: {
        img: undefined
      }
    }
  },
  props: {
    pNode: {
      type: Object,
      default: () => null
    }
  },
  methods: {
    uploadFunc (file) {
      const formData = new FormData()
      formData.append('file', file.file)
      addFile(formData).then(res => {
        this.form.iconUrl = res.data.data.url
        this.options.img = res.data.data.url
        handleImg(res.data.data.url, 'iconUrl')
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
            this.form.level = this.pNode.level + 1
            this.form.parentId = this.pNode.id
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
