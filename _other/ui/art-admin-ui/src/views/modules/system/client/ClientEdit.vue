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
      <a-form-item label="客户端ID" name="clientId">
        <a-input
          v-model:value="formData.clientId"
          :disabled="showable"
          placeholder="请输入客户端ID"
        />
      </a-form-item>
      <a-form-item label="客户端密钥" name="clientSecret">
        <a-input
          v-model:value="formData.clientSecret"
          :disabled="showable"
          placeholder="请输入客户端密钥"
        />
      </a-form-item>
      <a-form-item label="资源列表" name="resourceIds">
        <a-input
          v-model:value="formData.resourceIds"
          :disabled="showable"
          placeholder="请输入资源列表"
        />
      </a-form-item>
      <a-form-item label="域" name="scope">
        <a-input v-model:value="formData.scope" :disabled="showable" placeholder="请输入域" />
      </a-form-item>
      <a-form-item label="认证类型" name="authorizedGrantTypes">
        <a-input
          v-model:value="formData.authorizedGrantTypes"
          :disabled="showable"
          placeholder="请输入认证类型"
        />
      </a-form-item>
      <a-form-item label="重定向地址" name="webServerRedirectUri">
        <a-input
          v-model:value="formData.webServerRedirectUri"
          :disabled="showable"
          placeholder="请输入重定向地址"
        />
      </a-form-item>
      <a-form-item label="角色列表" name="authorities">
        <a-input
          v-model:value="formData.authorities"
          :disabled="showable"
          placeholder="请输入角色列表"
        />
      </a-form-item>
      <a-form-item label="token 有效期" name="accessTokenValidity">
        <a-input
          v-model:value="formData.accessTokenValidity"
          :disabled="showable"
          placeholder="请输入token 有效期"
        />
      </a-form-item>
      <a-form-item label="刷新令牌有效期" name="refreshTokenValidity">
        <a-input
          v-model:value="formData.refreshTokenValidity"
          :disabled="showable"
          placeholder="请输入刷新令牌有效期"
        />
      </a-form-item>
      <a-form-item label="令牌扩展字段JSON" name="additionalInformation">
        <a-input
          v-model:value="formData.additionalInformation"
          :disabled="showable"
          placeholder="请输入令牌扩展字段JSON"
        />
      </a-form-item>
      <a-form-item label="是否自动放行" name="autoapprove">
        <a-input
          v-model:value="formData.autoapprove"
          :disabled="showable"
          placeholder="请输入是否自动放行"
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
  import { get, update, add } from '/@/api/system/client'
  import { ClientDetailsDTO } from '/@/api/system/client/types'

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
    clientId: [{ required: true, message: '请输入客户端id', trigger: ['blur', 'change'] }],
    clientSecret: [{ required: true, message: '请输入客户端密钥', trigger: ['blur', 'change'] }],
  })

  const formRef = ref<FormInstance>()

  let formData: Ref<ClientDetailsDTO> = ref({
    clientId: '',
    clientSecret: '',
    scope: '',
    authorizedGrantTypes: '',
    webServerRedirectUri: '',
    accessTokenValidity: null,
    refreshTokenValidity: null,
    additionalInformation: '',
    autoapprove: '',
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
      clientId: '',
      clientSecret: '',
      scope: '',
      authorizedGrantTypes: '',
      webServerRedirectUri: '',
      accessTokenValidity: null,
      refreshTokenValidity: null,
      additionalInformation: '',
      autoapprove: '',
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
