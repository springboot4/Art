<template>
  <a-modal
    :title="title"
    :width="modalWidth"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :maskClosable="false"
    @cancel="handleCancel"
  >
    <a-form-model
      ref="form"
      :model="form"
      :rules="rules"
      :label-col="labelCol"
      :wrapper-col="wrapperCol"
    >
      <a-form-model-item
        label="客户端ID"
        prop="clientId"
      >
        <a-input v-model="form.clientId" :disabled="showable"/>
      </a-form-model-item>
      <a-form-model-item
        label="资源列表"
        prop="resourceIds"
      >
        <a-input v-model="form.resourceIds" :disabled="showable"/>
      </a-form-model-item>
      <a-form-model-item
        label="客户端密钥"
        prop="clientSecret"
      >
        <a-input v-model="form.clientSecret" :disabled="showable"/>
      </a-form-model-item>
      <a-form-model-item
        label="域"
        prop="scope"
      >
        <a-input v-model="form.scope" :disabled="showable"/>
      </a-form-model-item>
      <a-form-model-item
        label="认证类型"
        prop="authorizedGrantTypes"
      >
        <a-input v-model="form.authorizedGrantTypes" :disabled="showable"/>
      </a-form-model-item>
      <a-form-model-item
        label="重定向地址"
        prop="webServerRedirectUri"
      >
        <a-input v-model="form.webServerRedirectUri" :disabled="showable"/>
      </a-form-model-item>
      <a-form-model-item
        label="角色列表"
        prop="authorities"
      >
        <a-input v-model="form.authorities" :disabled="showable"/>
      </a-form-model-item>
      <a-form-model-item
        label="token 有效期"
        prop="accessTokenValidity"
      >
        <a-input v-model="form.accessTokenValidity" :disabled="showable"/>
      </a-form-model-item>
      <a-form-model-item
        label="刷新令牌有效期"
        prop="refreshTokenValidity"
      >
        <a-input v-model="form.refreshTokenValidity" :disabled="showable"/>
      </a-form-model-item>
      <a-form-model-item
        label="令牌扩展字段JSON"
        prop="additionalInformation"
      >
        <a-input v-model="form.additionalInformation" :disabled="showable"/>
      </a-form-model-item>
      <a-form-model-item
        label="是否自动放行"
        prop="autoapprove"
      >
        <a-input v-model="form.autoapprove" :disabled="showable"/>
      </a-form-model-item>
    </a-form-model>

    <template v-slot:footer>
      <a-button key="cancel" @click="handleCancel">取消</a-button>
      <a-button v-if="!showable" key="forward" :loading="confirmLoading" type="primary" @click="handleOk">保存</a-button>
    </template>
  </a-modal>
</template>

<script>
import { FormMixin } from '@/mixins/FormMixin'
import { get, add, update } from '@/api/sys/client'
export default {
  name: 'ClientEdit',
  mixins: [FormMixin],
  data () {
    return {
      form: {
        id: null,
        clientId: null,
        resourceIds: null,
        clientSecret: null,
        scope: null,
        authorizedGrantTypes: null,
        webServerRedirectUri: null,
        authorities: null,
        accessTokenValidity: null,
        refreshTokenValidity: null,
        additionalInformation: null,
        autoapprove: null,
        createTime: null,
        updateTime: null,
        createBy: null,
        updateBy: null
      },
      rules: {

      }
    }
  },
  methods: {
    edit (id, type) {
      this.resetForm()
      if (['edit', 'show'].includes(type)) {
        this.confirmLoading = true
        get(id).then(res => {
          this.form = res.data
          this.confirmLoading = false
        })
      } else {
        this.confirmLoading = false
      }
    },
    handleOk () {
      this.$refs.form.validate(async valid => {
        if (valid) {
          this.confirmLoading = true
          if (this.type === 'add') {
            await add(this.form)
          } else if (this.type === 'edit') {
            await update(this.form)
          }
          setTimeout(() => {
            this.confirmLoading = false
            this.$emit('ok')
            this.visible = false
          }, 200)
        } else {
          return false
        }
      })
    },
    resetForm () {
      this.$nextTick(() => {
        this.$refs.form.resetFields()
      })
    }
  }
}
</script>

<style scoped>

</style>
