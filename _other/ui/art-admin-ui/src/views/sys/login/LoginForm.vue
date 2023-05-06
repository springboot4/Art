<template>
  <LoginFormTitle v-show="getShow" class="enter-x" />
  <Form
    class="p-4 enter-x"
    :model="formData"
    :rules="getFormRules"
    ref="formRef"
    v-show="getShow"
    @keypress.enter="handleLogin"
  >
    <FormItem name="tenant" class="enter-x">
      <Input
        size="large"
        v-model:value="formData.tenant"
        :placeholder="t('sys.login.tenant')"
        class="fix-auto-fill"
      />
    </FormItem>
    <FormItem name="account" class="enter-x">
      <Input
        size="large"
        v-model:value="formData.account"
        :placeholder="t('sys.login.userName')"
        class="fix-auto-fill"
      />
    </FormItem>
    <FormItem name="password" class="enter-x">
      <InputPassword
        size="large"
        visibilityToggle
        v-model:value="formData.password"
        :placeholder="t('sys.login.password')"
      />
    </FormItem>

    <!--    <ARow class="enter-x">-->
    <!--      <ACol :span="12">-->
    <!--        <FormItem>-->
    <!--          &lt;!&ndash; No logic, you need to deal with it yourself 记住我&ndash;&gt;-->
    <!--          &lt;!&ndash;          <Checkbox v-model:checked="rememberMe" size="small">&ndash;&gt;-->
    <!--          &lt;!&ndash;            {{ t('sys.login.rememberMe') }}&ndash;&gt;-->
    <!--          &lt;!&ndash;          </Checkbox>&ndash;&gt;-->
    <!--        </FormItem>-->
    <!--      </ACol>-->
    <!--      <ACol :span="12">-->
    <!--        <FormItem :style="{ 'text-align': 'right' }">-->
    <!--          &lt;!&ndash; No logic, you need to deal with it yourself 忘记密码&ndash;&gt;-->
    <!--          &lt;!&ndash;          <Button type="link" size="small" @click="setLoginState(LoginStateEnum.RESET_PASSWORD)">&ndash;&gt;-->
    <!--          &lt;!&ndash;            {{ t('sys.login.forgetPassword') }}&ndash;&gt;-->
    <!--          &lt;!&ndash;          </Button>&ndash;&gt;-->
    <!--        </FormItem>-->
    <!--      </ACol>-->
    <!--    </ARow>-->

    <FormItem class="enter-x">
      <Button type="primary" size="large" block @click="verifyLogin" :loading="loading">
        {{ t('sys.login.loginButton') }}
      </Button>
      <!-- <Button size="large" class="mt-4 enter-x" block @click="handleRegister">
        {{ t('sys.login.registerButton') }}
      </Button> -->
    </FormItem>
    <ARow class="enter-x">
      <ACol :md="8" :xs="24">
        <Button block @click="setLoginState(LoginStateEnum.MOBILE)">
          {{ t('sys.login.mobileSignInFormTitle') }}
        </Button>
      </ACol>
      <!--      注册-->
      <!--      <ACol :md="6" :xs="24">-->
      <!--        <Button block @click="setLoginState(LoginStateEnum.REGISTER)">-->
      <!--          {{ t('sys.login.registerButton') }}-->
      <!--        </Button>-->
      <!--      </ACol>-->
    </ARow>
    <a-divider class="enter-x">{{ t('sys.login.otherSignIn') }}</a-divider>
    <div class="flex justify-evenly enter-x" :class="`${prefixCls}-sign-in-way`">
      <a :href="giteeUrl">
        <icon-font type="icon-gitee1" />
      </a>
    </div>
  </Form>

  <Verify
    @success="verifySuccess"
    :mode="'pop'"
    :captchaType="'blockPuzzle'"
    :imgSize="{ width: '330px', height: '155px' }"
    ref="verify"
  />
</template>
<script lang="ts" setup>
  import { computed, onMounted, reactive, ref, unref } from 'vue'
  import { Button, Col, Form, Input, Row } from 'ant-design-vue'
  import LoginFormTitle from './LoginFormTitle.vue'
  import Verify from '/@/components/verifition/Verify.vue'

  import { useI18n } from '/@/hooks/web/useI18n'
  import { useMessage } from '/@/hooks/web/useMessage'

  import { useUserStore } from '/@/store/modules/user'
  import { LoginStateEnum, useFormRules, useFormValid, useLoginState } from './useLogin'
  import { useDesign } from '/@/hooks/web/useDesign'
  import { createFromIconfontCN } from '@ant-design/icons-vue'
  import { findConfiguration } from '/@/api/system/auth'

  const ACol = Col
  const ARow = Row
  const FormItem = Form.Item
  const InputPassword = Input.Password
  const { t } = useI18n()
  const { notification, createErrorModal } = useMessage()
  const { prefixCls } = useDesign('login')
  const userStore = useUserStore()

  const { setLoginState, getLoginState } = useLoginState()
  const { getFormRules } = useFormRules()

  const verify = ref()

  const formRef = ref()
  const loading = ref(false)

  const formData = reactive({
    tenant: null,
    account: 'fxz',
    password: '123456',
  })

  const { validForm } = useFormValid(formRef)

  //onKeyStroke('Enter', handleLogin);

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.LOGIN)

  const IconFont = createFromIconfontCN({
    scriptUrl: '//at.alicdn.com/t/c/font_4052295_gmlf56196z.js',
  })

  let giteeUrl = ref('')

  onMounted(() => {
    getConfiguration()
  })

  async function getConfiguration() {
    await findConfiguration().then((res) => {
      giteeUrl.value = `http://art-gateway:9999/auth/gitee/authorize/${res.giteeAppid}?binding=true`
    })
  }

  function verifyLogin() {
    verify.value.show()
  }

  function verifySuccess() {
    handleLogin()
  }

  async function handleLogin() {
    const data = await validForm()
    if (!data) return
    try {
      loading.value = true
      const userInfo = await userStore.login({
        password: data.password,
        username: data.account,
        grant_type: 'password',
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
