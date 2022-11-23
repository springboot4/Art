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
      <a-form-model-item label="岗位ID" prop="postId" hidden="true">
        <a-input v-model="form.postId" v-if="!showable" />
      </a-form-model-item>
      <a-form-model-item label="岗位编码" prop="postCode">
        <a-input v-model="form.postCode" v-if="!showable" />
        <span v-else>{{ form.postCode }}</span>
      </a-form-model-item>
      <a-form-model-item label="岗位名称" prop="postName">
        <a-input v-model="form.postName" v-if="!showable" />
        <span v-else>{{ form.postName }}</span>
      </a-form-model-item>
      <a-form-model-item label="描述" prop="description">
        <a-textarea v-model="form.description" v-if="!showable" />
        <span v-else>{{ form.description }}</span>
      </a-form-model-item>
      <a-form-model-item label="岗位排序" prop="postSort">
        <a-input-number v-model="form.postSort" v-if="!showable" />
        <span v-else>{{ form.postSort }}</span>
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
import { get, add, update } from '@/api/sys/post'

export default {
  name: 'PostEdit',
  mixins: [FormMixin],
  data () {
    return {
      form: {
        id: null,
        postId: null,
        postCode: null,
        postName: null,
        postSort: null,
        delFlag: null,
        description: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null
      },
      rules: {
        postCode: [
          { required: true, message: '请输入岗位编码', trigger: 'blur' }
        ],
        postName: [
          { required: true, message: '请输入岗位名称', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入描述', trigger: 'blur' }
        ],
        postSort: [
          { required: true, message: '请输入岗位排序', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    edit (id, type) {
      this.resetForm()
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
