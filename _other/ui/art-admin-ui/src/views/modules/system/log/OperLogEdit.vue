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
      <a-form-item label="主键" :hidden="true">
        <a-input v-model:value="formData.id" :disabled="showable" />
      </a-form-item>
      <a-form-item label="标题" name="title">
        <a-input
          v-model:value="formData.title"
          :disabled="showable"
          placeholder="请输入标题"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="业务类型" name="businessType">
        <a-input
          v-model:value="formData.businessType"
          :disabled="showable"
          placeholder="请输入业务类型"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="方法名称" name="method">
        <a-input
          v-model:value="formData.method"
          :disabled="showable"
          placeholder="请输入方法名称"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="请求方式" name="requestMethod">
        <a-input
          v-model:value="formData.requestMethod"
          :disabled="showable"
          placeholder="请输入请求方式"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="操作人员" name="operName">
        <a-input
          v-model:value="formData.operName"
          :disabled="showable"
          placeholder="请输入操作人员"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="请求url" name="operUrl">
        <a-input
          v-model:value="formData.operUrl"
          :disabled="showable"
          placeholder="请输入请求url"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="主机地址" name="operIp">
        <a-input
          v-model:value="formData.operIp"
          :disabled="showable"
          placeholder="请输入主机地址"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="请求参数" name="operParam">
        <a-input
          v-model:value="formData.operParam"
          :disabled="showable"
          placeholder="请输入请求参数"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="操作状态（0正常 1异常）" name="status">
        <a-input
          v-model:value="formData.status"
          :disabled="showable"
          placeholder="请输入操作状态（0正常 1异常）"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="异常信息" name="errorMsg">
        <a-input
          v-model:value="formData.errorMsg"
          :disabled="showable"
          placeholder="请输入异常信息"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="执行时间" name="time">
        <a-input
          v-model:value="formData.time"
          :disabled="showable"
          placeholder="请输入执行时间"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="创建人" name="createBy">
        <a-input
          v-model:value="formData.createBy"
          :disabled="showable"
          placeholder="请输入创建人"
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
      <a-form-item label="更新人" name="updateBy">
        <a-input
          v-model:value="formData.updateBy"
          :disabled="showable"
          placeholder="请输入更新人"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="更新时间" name="updateTime">
        <a-input
          v-model:value="formData.updateTime"
          :disabled="showable"
          placeholder="请输入更新时间"
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
  import { get, update, add } from '/@/api/system/log'
  import { OperLogDTO } from '/@/api/system/log/types'

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

  const rules = reactive({})

  const formRef = ref<FormInstance>()

  let formData: Ref<OperLogDTO> = ref({
    id: null,
    title: '',
    businessType: null,
    method: '',
    requestMethod: '',
    operName: '',
    operUrl: '',
    operIp: '',
    operParam: '',
    status: null,
    errorMsg: '',
    time: '',
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
      id: null,
      title: '',
      businessType: null,
      method: '',
      requestMethod: '',
      operName: '',
      operUrl: '',
      operIp: '',
      operParam: '',
      status: null,
      errorMsg: '',
      time: '',
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
