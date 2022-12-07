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
        <a-input v-model="form.jobId" :disabled="showable" />
      </a-form-model-item>
      <a-form-model-item label="任务名称" prop="jobName">
        <a-input v-model="form.jobName" v-if="!showable" />
        <span v-else>{{form.jobName}}</span>
      </a-form-model-item>
      <a-form-model-item label="任务分组" prop="jobGroup">
        <a-input v-model="form.jobGroup" v-if="!showable"/>
        <span v-else>{{form.jobGroup}}</span>
      </a-form-model-item>
      <a-form-model-item label="执行参数" prop="parameters">
        <a-input v-model="form.parameters" v-if="!showable"/>
        <span v-else>{{form.parameters}}</span>
      </a-form-model-item>
      <a-form-model-item label="corn表达式" prop="cronExpression">
        <a-input v-model="form.cronExpression" v-if="!showable"/>
        <span v-else>{{form.cronExpression}}</span>
      </a-form-model-item>
      <a-form-model-item label="执行策略" prop="misfirePolicy">
        <a-radio-group default-value="0" button-style="solid" v-model="form.misfirePolicy" v-if="!showable">
          <a-radio-button value="1">立即执行</a-radio-button>
          <a-radio-button value="2">执行一次</a-radio-button>
          <a-radio-button value="3">放弃执行</a-radio-button>
        </a-radio-group>
        <span v-else>
          <a-tag v-if="form.misfirePolicy==='1'" color="#87d068">立即执行</a-tag>
          <a-tag v-if="form.misfirePolicy==='2'" color="#87d068">执行一次</a-tag>
          <a-tag v-if="form.misfirePolicy==='3'" color="#87d068">放弃执行</a-tag>
        </span>
      </a-form-model-item>
      <a-form-model-item label="状态" prop="status">
        <a-radio-group default-value="1" v-model="form.status" v-if="!showable">
          <a-radio value="0">正常</a-radio>
          <a-radio value="1">暂停</a-radio>
        </a-radio-group>
        <span v-else>
          <a-tag v-if="form.status==='0'" color="#87d068">正常</a-tag>
          <a-tag v-if="form.status==='1'" color="#f50">暂停</a-tag>
        </span>
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
import { get, add, update } from '@/api/sysMonitor/job'

export default {
  name: 'JobEdit',
  mixins: [FormMixin],
  data () {
    return {
      form: {
        jobId: '',
        jobName: '',
        jobGroup: '',
        parameters: '',
        cronExpression: '',
        misfirePolicy: '',
        status: ''
      },
      rules: {
        jobName: [
          { required: true, message: '请输入任务名称', trigger: 'blur' }
        ],
        jobGroup: [
          { required: true, message: '请选择任务分组', trigger: 'blur' }
        ],
        parameters: [
          { required: true, message: '请输入调用方法', trigger: 'blur' }
        ],
        cronExpression: [
          { required: true, message: '请输入corn表达式', trigger: 'blur' }
        ],
        misfirePolicy: [
          { required: true, message: '请选择执行策略', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择任务状态', trigger: 'blur' }
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
          console.log('form:', this.form)
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
