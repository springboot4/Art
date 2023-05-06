<template>
  <BasicModal
    v-bind="$attrs"
    :title="title"
    :loading="confirmLoading"
    :visible="visible"
    :mask-closable="showable"
    @cancel="handleCancel"
  >
    <a-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      :label-col="labelCol"
      :wrapper-col="wrapperCol"
    >
      <a-form-item label="任务ID" name="jobId" :hidden="true">
        <a-input v-model:value="formData.jobId" :disabled="showable" />
      </a-form-item>
      <a-form-item label="任务名称" name="jobName">
        <a-input
          v-model:value="formData.jobName"
          :disabled="showable"
          placeholder="请输入任务名称"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="任务组名" name="jobGroup">
        <a-input
          v-model:value="formData.jobGroup"
          :disabled="showable"
          placeholder="请输入任务组名"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="方法执行参数" name="parameters">
        <a-input
          v-model:value="formData.parameters"
          :disabled="showable"
          placeholder="请输入方法执行参数"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="cron执行表达式" name="cronExpression">
        <a-input
          v-model:value="formData.cronExpression"
          :disabled="showable"
          placeholder="请输入cron执行表达式"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="执行策略" name="misfirePolicy">
        <a-radio-group
          v-model:value="formData.misfirePolicy"
          button-style="solid"
          :disabled="showable"
        >
          <a-radio-button value="1">立即执行</a-radio-button>
          <a-radio-button value="2">执行一次</a-radio-button>
          <a-radio-button value="3">放弃执行</a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="状态" name="status">
        <a-radio-group v-model:value="formData.status" button-style="solid" :disabled="showable">
          <a-radio-button value="0">正常</a-radio-button>
          <a-radio-button value="1">暂停</a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="备注信息" name="remark">
        <a-textarea
          v-model:value="formData.remark"
          :disabled="showable"
          placeholder="请输入备注信息"
          allow-clear
        />
      </a-form-item>
    </a-form>
    <template #footer>
      <a-space>
        <a-button @click="handleCancel">取消</a-button>
        <a-button v-if="!showable" :loading="confirmLoading" type="primary" @click="handleOk"
          >保存</a-button
        >
      </a-space>
    </template>
  </BasicModal>
</template>

<script lang="ts" setup>
  import { BasicModal } from '/@/components/Modal'
  import { nextTick, reactive, Ref, ref } from 'vue'
  import useFormEdit from '/@/hooks/art/useFormEdit'
  import { FormOperationType } from '/@/enums/formOperationType'
  import { FormInstance } from 'ant-design-vue'
  import { get, update, add } from '/@/api/system/job/index'
  import { JobDTO } from '/@/api/system/job/index/types'

  const {
    initFormEditType,
    handleCancel,
    labelCol,
    wrapperCol,
    title,
    confirmLoading,
    visible,
    showable,
    formOperationType,
  } = useFormEdit()

  const emits = defineEmits(['ok'])

  const rules = reactive({
    jobName: [{ required: true, message: '请输入任务名称', trigger: ['blur', 'change'] }],
    jobGroup: [{ required: true, message: '请输入任务组名', trigger: ['blur', 'change'] }],
    cronExpression: [
      { required: true, message: '请输入cron执行表达式', trigger: ['blur', 'change'] },
    ],
    misfirePolicy: [
      {
        required: true,
        message: '请输入计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
        trigger: ['blur', 'change'],
      },
    ],
    status: [{ required: true, message: '请输入状态（0正常 1暂停）', trigger: ['blur', 'change'] }],
  })

  const formRef = ref<FormInstance>()

  let formData: Ref<JobDTO> = ref({
    jobId: '',
    jobName: '',
    jobGroup: '',
    parameters: '',
    cronExpression: '',
    misfirePolicy: '',
    status: '',
    remark: '',
  })

  /**
   * 表单初始化
   */
  function init(id, operationType: FormOperationType) {
    initFormEditType(operationType)
    resetForm()
    getInfo(id, operationType)
  }

  /**
   * 获取表单信息
   */
  async function getInfo(id, editType: FormOperationType) {
    if ([FormOperationType.EDIT, FormOperationType.SHOW].includes(editType)) {
      confirmLoading.value = true
      await get(id).then((res) => {
        formData.value = res
      })
    }
    confirmLoading.value = false
  }

  /**
   * 保存新增或者编辑
   */
  function handleOk() {
    formRef.value?.validate().then(async () => {
      confirmLoading.value = true
      if (formOperationType.value === FormOperationType.ADD) {
        await add(formData.value)
      } else if (formOperationType.value === FormOperationType.EDIT) {
        await update(formData.value)
      }
      confirmLoading.value = false
      handleCancel()
      emits('ok')
    })
  }

  /**
   * 重置表单字段
   */
  function resetForm() {
    formData = ref({
      jobId: '',
      jobName: '',
      jobGroup: '',
      parameters: '',
      cronExpression: '',
      misfirePolicy: '',
      status: '',
      remark: '',
    })
    nextTick(() => formRef.value?.resetFields())
  }

  /**
   * 暴露子组件init方法
   */
  defineExpose({
    init,
  })
</script>

<style lang="less" scoped></style>
