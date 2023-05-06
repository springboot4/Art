<template>
  <template v-if="getShow">
    <LoginFormTitle class="enter-x" />
    <Form class="p-4 enter-x" :model="formData" :rules="getFormRules" ref="formRef">
      <FormItem name="tenant" class="enter-x">
        <Input
          size="large"
          v-model:value="formData.tenant"
          :placeholder="t('sys.login.tenant')"
          class="fix-auto-fill"
        />
      </FormItem>
      <FormItem name="mobile" class="enter-x">
        <Input
          size="large"
          v-model:value="formData.mobile"
          :placeholder="t('sys.login.mobile')"
          class="fix-auto-fill"
        />
      </FormItem>
      <FormItem name="sms" class="enter-x">
        <CountdownInput
          size="large"
          class="fix-auto-fill"
          v-model:value="formData.sms"
          :sendCodeApi="doSendSms"
          :placeholder="t('sys.login.smsCode')"
        />
      </FormItem>

      <FormItem class="enter-x">
        <Button type="primary" size="large" block @click="handleLogin" :loading="loading">
          {{ t('sys.login.loginButton') }}
        </Button>
        <Button size="large" block class="mt-4" @click="handleBackLogin">
          {{ t('sys.login.backSignIn') }}
        </Button>
      </FormItem>
    </Form>
    <Verify
      @success="verifySuccess"
      :mode="'pop'"
      :captchaType="'blockPuzzle'"
      :imgSize="{ width: '330px', height: '155px' }"
      ref="verify"
    />
  </template>
</template>
<script lang="ts" setup>
  import { reactive, ref, computed, unref } from 'vue'
  import { Form, Input, Button } from 'ant-design-vue'
  import { CountdownInput } from '/@/components/CountDown'
  import LoginFormTitle from './LoginFormTitle.vue'
  import { useI18n } from '/@/hooks/web/useI18n'
  import { useLoginState, useFormRules, useFormValid, LoginStateEnum } from './useLogin'
  import { sendSmsApi } from '/@/api/base/sms'
  import { SmsModel } from '/@/api/base/model/smsModel'
  import { useUserStore } from '/@/store/modules/user'
  import { useMessage } from '/@/hooks/web/useMessage'
  import { useDesign } from '/@/hooks/web/useDesign'
  import Verify from '/@/components/verifition/Verify.vue'

  let verify = ref()

  const { prefixCls } = useDesign('login')

  const { notification, createErrorModal } = useMessage()

  const userStore = useUserStore()

  const FormItem = Form.Item
  const { t } = useI18n()
  const { handleBackLogin, getLoginState } = useLoginState()
  const { getFormRules } = useFormRules()

  const formRef = ref()
  const loading = ref(false)
  const formData = reactive({
    tenant: '',
    mobile: '',
    sms: '',
  })
  const { validForm } = useFormValid(formRef)
  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.MOBILE)

  function doSendSms() {
    verify.value.show()
  }

  function verifySuccess() {
    const params: SmsModel = {
      phoneNumber: formData.mobile,
    }
    return sendSmsApi(params)
  }

  async function handleLogin() {
    const data = await validForm()
    if (!data) return
    try {
      loading.value = true
      const userInfo = await userStore.login({
        captcha: data.sms,
        mobile: data.mobile,
        grant_type: 'sms_code',
        scope: 'server',
        mode: 'none', //不要默认的错误提示
      })
      if (userInfo) {
        notification.success({
          message: t('sys.login.loginSuccessTitle'),
          description: `${t('sys.login.loginSuccessDesc')}: ${userInfo.realName}`,
          duration: 3,
        })
      }
    } catch (error) {
      createErrorModal({
        title: t('sys.api.errorTip'),
        content: (error as unknown as Error).message || t('sys.api.networkExceptionMsg'),
        getContainer: () => document.body.querySelector(`.${prefixCls}`) || document.body,
      })
    } finally {
      loading.value = false
    }
  }
</script>
