<template>
  <BasicModal
    v-bind="$attrs"
    :title="modalTile"
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
      <a-form-item label="套餐名" name="name">
        <a-input
          v-model:value="formData.name"
          :disabled="showable"
          placeholder="请输入套餐名"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="菜单权限" name="menuIds">
        <a-tree
          :disabled="showable"
          v-if="treeData.length"
          checkStrictly
          checkable
          :tree-data="treeData"
          v-model:checkedKeys="tempMenu"
        >
          <template #title="{ title }">
            {{ title }}
          </template>
        </a-tree>
      </a-form-item>
      <a-form-item label="套餐状态" name="status">
        <a-radio-group v-model:value="formData.status" button-style="solid" :disabled="showable">
          <a-radio-button :value="Number(0)">正常</a-radio-button>
          <a-radio-button :value="Number(1)">停用</a-radio-button>
        </a-radio-group>
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
  import { get, update, add } from '/@/api/system/tenant/package'
  import { TenantPackageDTO } from '/@/api/system/tenant/package/types'
  import { page as getMenuTree } from '/@/api/system/menu'

  const {
    initFormEditType,
    handleCancel,
    labelCol,
    wrapperCol,
    title: modalTile,
    confirmLoading,
    visible,
    showable,
    formOperationType,
  } = useFormEdit()

  const emits = defineEmits(['ok'])

  const rules = reactive({
    name: [{ required: true, message: '请输入套餐名', trigger: ['blur', 'change'] }],
    status: [{ required: true, message: '请输入套餐状态', trigger: ['blur', 'change'] }],
    menuIds: [{ required: true, message: '请输入关联的菜单编号', trigger: ['blur', 'change'] }],
  })

  const formRef = ref<FormInstance>()

  let formData: Ref<TenantPackageDTO> = ref({
    id: '',
    name: '',
    status: 1,
    remark: '',
    menuIds: '',
  })

  let tempMenu = ref<{ checked: string[]; halfChecked: string[] | number[] }>({
    checked: [],
    halfChecked: [],
  })
  let treeData: Ref<any[]> = ref([])
  getMenuTree().then((res) => {
    if (res && res.length > 0) {
      treeData.value = res.map((item) => {
        return buildTree(item)
      })
    }
  })
  function buildTree(res) {
    if (res) {
      const v = {
        title: res.title,
        key: res.id,
      }
      if (res.children && res.children.length > 0) {
        v.children = res.children.map((item) => {
          return buildTree(item)
        })
      }
      return v
    }
    return res
  }

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
        tempMenu.value.checked = (res.menuIds || '').split(',')
      })
    }
    confirmLoading.value = false
  }

  /**
   * 保存新增或者编辑
   */
  function handleOk() {
    formData.value.menuIds = tempMenu.value.checked?.join(',')

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
    tempMenu = ref<{ checked: string[]; halfChecked: string[] | number[] }>({
      checked: [],
      halfChecked: [],
    })
    formData = ref({
      id: '',
      name: '',
      status: 1,
      remark: '',
      menuIds: '',
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
