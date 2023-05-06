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
      <a-form-item label="岗位ID" name="postId" :hidden="true">
        <a-input v-model:value="formData.postId" :disabled="showable" />
      </a-form-item>
      <a-form-item label="岗位编码" name="postCode">
        <a-input
          v-model:value="formData.postCode"
          :disabled="showable"
          placeholder="请输入岗位编码"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="岗位名称" name="postName">
        <a-input
          v-model:value="formData.postName"
          :disabled="showable"
          placeholder="请输入岗位名称"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="岗位排序" name="postSort">
        <a-input-number
          v-model:value="formData.postSort"
          :disabled="showable"
          placeholder="请输入岗位排序"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="描述" name="description">
        <a-textarea
          v-model:value="formData.description"
          :disabled="showable"
          placeholder="请输入描述"
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
  import { get, update, add } from '/@/api/system/post'
  import { PostDTO } from '/@/api/system/post/types'

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
    postCode: [{ required: true, message: '请输入岗位编码', trigger: ['blur', 'change'] }],
    postName: [{ required: true, message: '请输入岗位名称', trigger: ['blur', 'change'] }],
    postSort: [{ required: true, message: '请输入岗位排序', trigger: ['blur', 'change'] }],
  })

  const formRef = ref<FormInstance>()

  let formData: Ref<PostDTO> = ref({
    postId: '',
    postCode: '',
    postName: '',
    postSort: 0,
    description: '',
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
      postId: '',
      postCode: '',
      postName: '',
      postSort: 0,
      description: '',
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
