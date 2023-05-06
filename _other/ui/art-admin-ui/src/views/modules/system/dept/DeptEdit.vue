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
      <a-form-item label="部门ID" name="deptId" :hidden="true">
        <a-input
          v-model:value="formData.deptId"
          :disabled="showable"
          placeholder="请输入部门ID"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="上级部门" name="parentId">
        <a-tree-select
          v-model:value="formData.parentId"
          :disabled="showable"
          style="width: 100%"
          :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
          placeholder="请选择上级部门"
          allow-clear
          tree-default-expand-all
          :tree-data="deptTree"
          :field-names="{
            children: 'children',
            label: 'deptName',
            value: 'deptId',
          }"
        />
      </a-form-item>
      <a-form-item label="部门名称" name="deptName">
        <a-input
          v-model:value="formData.deptName"
          :disabled="showable"
          placeholder="请输入部门名称"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="排序" name="orderNum">
        <a-input-number
          v-model:value="formData.orderNum"
          :disabled="showable"
          placeholder="请输入排序"
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
  import { get, update, add, getDeptTree } from '/@/api/system/dept'
  import { DeptDTO } from '/@/api/system/dept/types'

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
    parentId: [{ required: true, message: '请输入上级部门ID', trigger: ['blur', 'change'] }],
    deptName: [{ required: true, message: '请输入部门名称', trigger: ['blur', 'change'] }],
    orderNum: [{ required: true, message: '请输入排序', trigger: ['blur', 'change'] }],
  })

  const formRef = ref<FormInstance>()

  let formData: Ref<DeptDTO> = ref({
    deptId: '',
    parentId: '',
    deptName: '',
    orderNum: 0,
  })

  let deptTree: Ref<DeptDTO[]> = ref([])
  getDeptTree().then((res) => {
    deptTree.value.push(res)
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
      deptId: '',
      parentId: '',
      deptName: '',
      orderNum: 0,
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
