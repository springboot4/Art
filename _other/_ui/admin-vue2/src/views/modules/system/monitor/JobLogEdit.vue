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
        label="任务日志ID"
        prop="jobLogId"
      >
        {{ form.jobLogId }}
      </a-form-model-item>
      <a-form-model-item
        label="任务名称"
        prop="jobName"
      >
        {{ form.jobName }}
      </a-form-model-item>
      <a-form-model-item
        label="任务组名"
        prop="jobGroup"
      >
        {{ form.jobGroup }}
      </a-form-model-item>
      <a-form-model-item
        label="调用目标字符串"
        prop="invokeTarget"
      >
        {{ form.invokeTarget }}
      </a-form-model-item>
      <a-form-model-item
        label="日志信息"
        prop="jobMessage"
      >
        {{ form.jobMessage }}
      </a-form-model-item>
      <a-form-model-item
        label="执行状态"
        prop="status"
      >
        <a-tag v-if="form.status==='0'" color="#2db7f5">正常</a-tag>
        <a-tag v-if="form.status==='1'" color="#f50">失败</a-tag>
      </a-form-model-item>
      <a-form-model-item
        label="异常信息"
        prop="exceptionInfo"
      >
        {{ form.exceptionInfo }}
      </a-form-model-item>
      <a-form-model-item
        label="创建时间"
        prop="createTime"
      >
        {{ form.createTime }}
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
import { add, get, update } from '@/api/sysMonitor/jobLog'

export default {
  name: 'JobLogEdit',
  mixins: [FormMixin],
  data () {
    return {
      form: {
        id: null,
        jobLogId: null,
        jobName: null,
        jobGroup: null,
        invokeTarget: null,
        jobMessage: null,
        status: null,
        exceptionInfo: null,
        createTime: null
      },
      rules: {}
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
