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
      :label-col="labelCol"
      :wrapper-col="wrapperCol"
      :rules="rules"
    >
      <a-form-model-item label="主键" prop="id" hidden="true">
        <a-input v-model="form.id" :disabled="showable" />
      </a-form-model-item>
      <a-form-model-item
        label="数据源名称"
        prop="name"
      >
        <a-input v-model="form.name" :disabled="showable" />
      </a-form-model-item>
      <a-form-model-item
        label="jdbcUrl"
        prop="url"
      >
        <a-textarea v-model="form.url" :disabled="showable" />
      </a-form-model-item>
      <a-form-model-item
        label="用户名"
        prop="username"
      >
        <a-input v-model="form.username" :disabled="showable" />
      </a-form-model-item>
      <a-form-model-item
        label="密码"
        prop="password"
      >
        <a-input v-model="form.password" :disabled="showable" />
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
import { add, get, update } from '@/api/sysTool/datasourceConf'

/**
 *
 * @param {校验数据源名} rule
 * @param {*} value
 * @param {*} callback
 */
const validateDsName = (rule, value, callback) => {
  const re = /(?=.*[a-z])(?=.*_)/
  if (value && (!(re).test(value))) {
    callback(new Error('数据源名称不合法, 组名_数据源名形式'))
  } else {
    callback()
  }
}

export default {
  name: 'DatasourceConfEdit',
  mixins: [FormMixin],
  data () {
    return {
      form: {
        id: null,
        name: null,
        url: null,
        username: null,
        password: null,
        delFlag: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null
      },
      rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' },
          { max: 32, message: '长度在 32 个字符', trigger: 'blur' },
          { validator: validateDsName, trigger: 'blur' }
        ],
        url: [
          { required: true, message: '请输入jdbcUrl', trigger: 'blur' }
        ],
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { max: 64, message: '长度在 64 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { max: 64, message: '长度在 64 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    edit (id, type) {
      this.$nextTick(() => {
        this.$refs['form'].resetFields()
      })
      if (['edit', 'show'].includes(type)) {
        this.confirmLoading = true
        get(id).then(res => {
          this.form = res.data
          this.confirmLoading = false
          if (type === 'edit') this.form.password = ''
        })
      }
    },
    handleOk () {
      this.$refs.form.validate(async valid => {
        if (valid) {
          this.confirmLoading = true
          if (this.type === 'add') {
            await add(this.form)
          } else if (this.type === 'edit') {
            await update(this.form).catch(() => {
              this.$message.error('数据源信息错误，连接失败!')
            })
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
