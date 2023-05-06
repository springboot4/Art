<template>
  <BasicModal
    v-bind="$attrs"
    :title="title"
    :loading="confirmLoading"
    :visible="visible"
    :mask-closable="showable"
    @cancel="handleCancel"
  >
    <a-form ref="formRef" :model="formData" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-form-item label="主键" :hidden="true">
        <a-input v-model:value="formData.id" :disabled="showable" />
      </a-form-item>
      <a-form-item label="任务名称" name="jobName">
        <a-input
          v-model:value="formData.jobName"
          :disabled="showable"
          placeholder="请输入任务名称"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="日志信息" name="jobMessage">
        <a-textarea
          v-model:value="formData.jobMessage"
          :disabled="showable"
          placeholder="请输入日志信息"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="执行状态" name="status">
        <span v-if="formData.status === '0'"> 正常 </span>
        <span v-else> 失败 </span>
      </a-form-item>
      <a-form-item label="异常信息" name="exceptionInfo">
        <a-textarea
          v-model:value="formData.exceptionInfo"
          :disabled="showable"
          placeholder="请输入异常信息"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="创建时间" name="createTime">
        <a-input
          v-model:value="formData.createTime"
          :disabled="showable"
          placeholder="请输入创建时间"
          allow-clear
        />
      </a-form-item>
    </a-form>
    <template #footer>
      <a-space>
        <a-button @click="handleCancel">取消</a-button>
      </a-space>
    </template>
  </BasicModal>
</template>

<script lang="ts" setup>
  import { BasicModal } from '/@/components/Modal'
  import { nextTick, Ref, ref } from 'vue'
  import useFormEdit from '/@/hooks/art/useFormEdit'
  import { FormOperationType } from '/@/enums/formOperationType'
  import { FormInstance } from 'ant-design-vue'
  import { get } from '/@/api/system/job/log'
  import { JobLogDTO } from '/@/api/system/job/log/types'

  const {
    initFormEditType,
    handleCancel,
    labelCol,
    wrapperCol,
    title,
    confirmLoading,
    visible,
    showable,
  } = useFormEdit()

  const formRef = ref<FormInstance>()

  let formData: Ref<JobLogDTO> = ref({
    id: '',
    jobName: '',
    jobMessage: '',
    status: '',
    exceptionInfo: '',
    createTime: '',
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
   * 重置表单字段
   */
  function resetForm() {
    formData = ref({
      id: '',
      jobName: '',
      jobMessage: '',
      status: '',
      exceptionInfo: '',
      createTime: '',
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
