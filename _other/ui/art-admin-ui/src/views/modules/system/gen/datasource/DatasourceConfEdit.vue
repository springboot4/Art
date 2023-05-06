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
      <a-form-item label="主键" :hidden="true" name="id">
        <a-input v-model:value="formData.id" :disabled="showable" />
      </a-form-item>
      <a-form-item label="数据源名称" name="name">
        <a-input
          v-model:value="formData.name"
          :disabled="showable"
          placeholder="请输入数据源名称"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="jdbc-url" name="url">
        <a-input
          v-model:value="formData.url"
          :disabled="showable"
          placeholder="请输入jdbc-url"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="用户名" name="username">
        <a-input
          v-model:value="formData.username"
          :disabled="showable"
          placeholder="请输入用户名"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="密码" name="password">
        <a-input
          v-model:value="formData.password"
          :disabled="showable"
          placeholder="请输入密码"
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
  import { get, update, add } from '/@/api/system/gen/datasource'
  import { GenDatasourceConfDTO } from '/@/api/system/gen/datasource/types'

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
    name: [{ required: true, message: '请输入数据源名称', trigger: ['blur', 'change'] }],
    url: [{ required: true, message: '请输入jdbc-url', trigger: ['blur', 'change'] }],
    username: [{ required: true, message: '请输入用户名', trigger: ['blur', 'change'] }],
    password: [{ required: true, message: '请输入密码', trigger: ['blur', 'change'] }],
  })

  const formRef = ref<FormInstance>()

  let formData: Ref<GenDatasourceConfDTO> = ref({
    id: '',
    name: '',
    url: '',
    username: '',
    password: '',
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
        formData.value.password = ''
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
      id: '',
      name: '',
      url: '',
      username: '',
      password: '',
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
