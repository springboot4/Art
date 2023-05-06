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
      <a-form-item label="上级菜单" name="parentId">
        <a-tree-select
          v-model:value="formData.parentId"
          :disabled="showable"
          show-search
          style="width: 100%"
          :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
          placeholder="请选择上级菜单"
          allow-clear
          :tree-data="menuTree"
          :field-names="{
            children: 'children',
            label: 'title',
            value: 'id',
          }"
        />
      </a-form-item>
      <a-form-item label="名称" name="title">
        <a-input
          v-model:value="formData.title"
          :disabled="showable"
          placeholder="请输入菜单名称"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="权限标识" name="perms">
        <a-input
          v-model:value="formData.perms"
          :disabled="showable"
          placeholder="请输入权限标识"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="类型" name="type">
        <a-radio-group v-model:value="formData.type" button-style="solid" :disabled="showable">
          <a-radio-button value="0">菜单</a-radio-button>
          <a-radio-button value="1">按钮</a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="组件名称" name="name">
        <a-input
          v-model:value="formData.name"
          :disabled="showable"
          placeholder="请输入组件名称"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="对应组件" name="component" v-if="formData.type === '0'">
        <a-input
          v-model:value="formData.component"
          :disabled="showable"
          placeholder="请输入对应路由组件"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="对应路由" name="path" v-if="formData.type === '0'">
        <a-input
          v-model:value="formData.path"
          :disabled="showable"
          placeholder="请输入对应路由path"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="重定向地址" name="redirect" v-if="formData.type === '0'">
        <a-input
          v-model:value="formData.redirect"
          :disabled="showable"
          placeholder="请输入重定向"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="图标" name="icon" v-if="formData.type === '0'">
        <IconPicker v-model:value="formData.icon" :disabled="showable" />
      </a-form-item>
      <a-form-item label="排序" name="orderNum">
        <a-input-number
          v-model:value="formData.orderNum"
          :disabled="showable"
          placeholder="请输入排序"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="所属应用" name="application" v-if="formData.type === '0'">
        <a-select v-model:value="formData.application" :disabled="showable">
          <a-select-option v-for="item in appList" :key="item.id" :value="item.id">{{
            item.name
          }}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="是否缓存" name="keepAlive" v-if="formData.type === '0'">
        <a-radio-group v-model:value="formData.keepAlive" button-style="solid" :disabled="showable">
          <a-radio-button value="1">不缓存</a-radio-button>
          <a-radio-button value="0">缓存</a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="是否隐藏" name="hidden" v-if="formData.type === '0'">
        <a-radio-group v-model:value="formData.hidden" button-style="solid" :disabled="showable">
          <a-radio-button value="0">不隐藏</a-radio-button>
          <a-radio-button value="1">隐藏</a-radio-button>
        </a-radio-group>
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
  import { get, update, add, getTreeSelect } from '/@/api/system/menu'
  import { MenuDTO } from '/@/api/system/menu/types'
  import IconPicker from '/@/components/Icon/src/IconPicker.vue'
  import { findAllApp } from '/@/api/system/app'
  import { AppDTO } from '/@/api/system/app/types'

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
    parentId: [{ required: true, message: '请输入上级菜单', trigger: ['blur', 'change'] }],
    title: [{ required: true, message: '请输入菜单/按钮名称', trigger: ['blur', 'change'] }],
    name: [{ required: true, message: '请输入组件名称', trigger: ['blur', 'change'] }],
    perms: [
      {
        required: true,
        message: '请输入权限标识(多个用逗号分隔，如：user:list,user:create)',
        trigger: ['blur', 'change'],
      },
    ],
    type: [{ required: true, message: '请输入类型 0菜单 1按钮', trigger: ['blur', 'change'] }],
    orderNum: [{ required: true, message: '请输入排序', trigger: ['blur', 'change'] }],
  })

  const formRef = ref<FormInstance>()

  let formData: Ref<MenuDTO> = ref({
    id: '',
    parentId: '',
    title: '',
    name: '',
    perms: '',
    type: '',
    component: '',
    path: '',
    redirect: '',
    icon: '',
    keepAlive: null,
    orderNum: null,
    createTime: '',
    updateTime: '',
    createBy: '',
    updateBy: '',
    hidden: '',
    application: '',
  })

  let menuTree = ref([])
  getTreeSelect().then((res) => {
    menuTree.value = res
  })

  let appList: Ref<AppDTO[]> = ref([])
  findAllApp().then((res) => (appList.value = res))

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
      parentId: '',
      title: '',
      name: '',
      perms: '',
      type: '',
      component: '',
      path: '',
      redirect: '',
      icon: '',
      keepAlive: null,
      orderNum: null,
      createTime: '',
      updateTime: '',
      createBy: '',
      updateBy: '',
      hidden: '',
      application: '',
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
