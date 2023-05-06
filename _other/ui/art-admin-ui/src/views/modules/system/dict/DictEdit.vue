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
      <a-form-item label="字典编码" name="type">
        <a-input
          v-model:value="formData.type"
          :disabled="showable"
          placeholder="请输入字典编码"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="字典描述" name="description">
        <a-input
          v-model:value="formData.description"
          :disabled="showable"
          placeholder="请输入描述"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="字典类型" name="systemFlag">
        <a-select v-model:value="formData.systemFlag" placeholder="字典类型" :disabled="showable">
          <a-select-option v-for="item in dictItems" :key="item.id" :value="item.value">
            {{ item.label }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea
          v-model:value="formData.remark"
          :disabled="showable"
          placeholder="请输入备注"
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
  import { get, update, add } from '/@/api/system/dict'
  import { DictDTO, DictItemDto } from '/@/api/system/dict/types'
  import useDict from '/@/hooks/art/useDict'

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
  const { dictItemsByType } = useDict()

  const emits = defineEmits(['ok'])

  const rules = reactive({
    type: [{ required: true, message: '请输入字典编码', trigger: ['blur', 'change'] }],
    description: [{ required: true, message: '请输入字典描述', trigger: ['blur', 'change'] }],
    systemFlag: [{ required: true, message: '请选择字典类型', trigger: ['blur', 'change'] }],
  })

  const formRef = ref<FormInstance>()

  let formData: Ref<DictDTO> = ref({
    id: '',
    type: '',
    description: '',
    remark: '',
    systemFlag: '',
    delFlag: '',
    createTime: '',
    createBy: '',
    updateBy: '',
    updateTime: '',
  })

  const dictItems: Ref<DictItemDto[]> = ref([])
  dictItemsByType('dict_type', dictItems.value)

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
      id: '',
      type: '',
      description: '',
      remark: '',
      systemFlag: '',
      delFlag: '',
      createTime: '',
      createBy: '',
      updateBy: '',
      updateTime: '',
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
