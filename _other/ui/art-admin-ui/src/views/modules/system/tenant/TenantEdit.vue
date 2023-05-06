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
      <a-form-item label="租户名" name="name">
        <a-input
          v-model:value="formData.name"
          :disabled="showable"
          placeholder="请输入租户名"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="租户套餐" name="packageId">
        <a-select v-model:value="formData.packageId" :disabled="showable">
          <a-select-option v-for="item in packages" :key="item.id" :value="item.id">{{
            item.name
          }}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="租户管理员姓名" name="tenantAdminName">
        <a-input
          v-model:value="formData.tenantAdminName"
          :disabled="showable"
          placeholder="请输入当前租户管理员姓名"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="租户管理员手机号" name="tenantAdminMobile">
        <a-input
          v-model:value="formData.tenantAdminMobile"
          :disabled="showable"
          placeholder="请输入当前租户管理员手机号"
          allow-clear
        />
      </a-form-item>
      <a-form-item
        label="初始账号"
        name="username"
        v-if="FormOperationType.ADD === formOperationType"
      >
        <a-input
          v-model:value="formData.username"
          :disabled="showable"
          placeholder="请输入初始账号"
          allow-clear
        />
      </a-form-item>
      <a-form-item
        label="初始密码"
        name="password"
        v-if="FormOperationType.ADD === formOperationType"
      >
        <a-input
          v-model:value="formData.password"
          :disabled="showable"
          placeholder="请输入初始密码"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="过期时间" name="expireTime">
        <a-date-picker
          show-time
          placeholder="过期时间"
          format="YYYY-MM-DD HH:mm:ss"
          v-model:value="tempExpireTime"
          style="width: 100%"
          @change="onChange"
          :disabled="showable"
        />
      </a-form-item>
      <a-form-item label="账号数量" name="accountCount">
        <a-input-number
          v-model:value="formData.accountCount"
          :min="0"
          :disabled="showable"
          placeholder="请输入账号数量"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="租户状态" name="status">
        <a-radio-group v-model:value="formData.status" button-style="solid" :disabled="showable">
          <a-radio-button :value="Number(0)">正常</a-radio-button>
          <a-radio-button :value="Number(1)">停用</a-radio-button>
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
  import { get, update, add } from '/@/api/system/tenant/index'
  import { TenantDTO } from '/@/api/system/tenant/index/types'
  import dayjs, { Dayjs } from 'dayjs'
  import { findAllPackages } from '/@/api/system/tenant/package'
  import { TenantPackageDTO } from '/@/api/system/tenant/package/types'

  let packages: Ref<TenantPackageDTO[]> = ref([])
  findAllPackages().then((res) => {
    packages.value = res
  })

  let tempExpireTime = ref<Dayjs>()
  const onChange = () => {
    formData.value.expireTime = tempExpireTime.value?.format('YYYY-MM-DD HH:mm:ss')
  }

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
    name: [{ required: true, message: '请输入租户名', trigger: ['blur', 'change'] }],
    tenantAdminName: [
      { required: true, message: '请输入当前租户管理员姓名', trigger: ['blur', 'change'] },
    ],
    tenantAdminMobile: [
      { required: true, message: '请输入当前租户管理员手机号', trigger: ['blur', 'change'] },
    ],
    status: [{ required: true, message: '请输入租户状态', trigger: ['blur', 'change'] }],
    packageId: [{ required: true, message: '请输入租户套餐', trigger: ['blur', 'change'] }],
    expireTime: [{ required: true, message: '请输入过期时间', trigger: ['blur', 'change'] }],
    accountCount: [{ required: true, message: '请输入账号数量', trigger: ['blur', 'change'] }],
  })

  const formRef = ref<FormInstance>()

  let formData: Ref<TenantDTO> = ref({
    id: '',
    name: '',
    tenantAdminId: '',
    tenantAdminName: '',
    tenantAdminMobile: '',
    status: 0,
    packageId: '',
    expireTime: '',
    accountCount: 0,
    createBy: '',
    createTime: '',
    updateBy: '',
    updateTime: '',
    password: '',
    username: '',
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
        tempExpireTime.value = dayjs(formData.value.expireTime)
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
      tenantAdminId: '',
      tenantAdminName: '',
      tenantAdminMobile: '',
      status: 0,
      packageId: '',
      expireTime: '',
      accountCount: 0,
      createBy: '',
      createTime: '',
      updateBy: '',
      updateTime: '',
      password: '',
      username: '',
    })
    tempExpireTime = ref<Dayjs>()
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
