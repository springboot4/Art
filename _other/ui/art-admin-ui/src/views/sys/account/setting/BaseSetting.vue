<template>
  <CollapseContainer title="基本设置" :canExpan="false">
    <a-row :gutter="24">
      <a-col :span="14">
        <a-form
          ref="formRef"
          :model="formData"
          :rules="rules"
          :label-col="labelCol"
          :wrapper-col="wrapperCol"
        >
          <a-form-item label="电话" name="mobile">
            <a-input v-model:value="formData.mobile" placeholder="请输入电话" allow-clear />
          </a-form-item>
          <a-form-item label="电子邮件" name="email">
            <a-input v-model:value="formData.email" placeholder="请输入电子邮件" allow-clear />
          </a-form-item>
          <a-form-item label="登录密码" name="password">
            <a-input v-model:value="formData.password" placeholder="请输入登录密码" allow-clear />
          </a-form-item>
          <a-form-item label="个人简介" name="description">
            <a-textarea
              v-model:value="formData.description"
              placeholder="请输入个人简介"
              allow-clear
            />
          </a-form-item>
          <a-form-item label="性别" name="sex">
            <a-select v-model:value="formData.sex">
              <a-select-option v-for="item in sexList" :value="item.value" :key="item.value">
                {{ item.label }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-form>
      </a-col>
      <a-col :span="10">
        <div class="change-avatar">
          <div class="mb-2">头像</div>
          <CropperAvatar
            :uploadApi="uploadApi"
            @change="updateAvatar"
            :value="avatar"
            btnText="更换头像"
            :btnProps="{ preIcon: 'ant-design:cloud-upload-outlined' }"
            width="150"
          />
        </div>
      </a-col>
    </a-row>
    <a-button type="primary" @click="handleOk"> 更新基本信息 </a-button>
  </CollapseContainer>
</template>
<script lang="ts" setup>
  import { CropperAvatar } from '/@/components/Cropper'
  import { useUserStore } from '/@/store/modules/user'
  import { computed, reactive, ref } from 'vue'
  import { CollapseContainer } from '/@/components/Container'
  import useFormEdit from '/@/hooks/art/useFormEdit'
  import useDict from '/@/hooks/art/useDict'
  import { UserInfo } from '/#/store'
  import { updateInfoById } from '/@/api/system/user'
  import { useMessage } from '/@/hooks/web/useMessage'
  import headerImg from '/@/assets/images/header.jpg'
  import { uploadApi } from '/@/api/base/upload'
  import { cloneDeep } from 'lodash-es'

  let sexList = ref([])
  useDict().dictItemsByType('sex_type', sexList.value)

  let userStore = useUserStore()

  let formRef = ref()
  const formData = ref<UserInfo>(cloneDeep(userStore.getUserInfo))
  const rules = reactive({})
  const { labelCol, wrapperCol } = useFormEdit()

  const avatar = computed(() => {
    const { avatar } = userStore.getUserInfo
    return avatar || headerImg
  })

  function updateAvatar({ source, data }) {
    const userinfo = userStore.getUserInfo
    userinfo.avatar = source
    userStore.setUserInfo(userinfo)
    formData.value.avatar = data
  }

  function handleOk() {
    if (formData.value.mobile && formData.value.mobile.indexOf('*') > 0) {
      formData.value.mobile = undefined
    }
    if (formData.value.email && formData.value.email.indexOf('*') > 0) {
      formData.value.email = undefined
    }

    formRef.value?.validate().then(async () => {
      updateInfoById(formData.value).then((_) => {
        useMessage().createMessage.success('修改成功！')
        userStore.logout(true)
      })
    })
  }
</script>

<style lang="less" scoped>
  .change-avatar {
    img {
      display: block;
      margin-bottom: 15px;
      border-radius: 50%;
    }
  }
</style>
