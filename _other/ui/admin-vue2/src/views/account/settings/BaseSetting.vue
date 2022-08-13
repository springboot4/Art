<template>
  <div class="account-settings-info-view">
    <a-row :gutter="16">
      <a-col :md="24" :lg="16">
        <a-form-model layout="vertical" ref="form" :model="form" :rules="rules">
          <a-form-model-item
            label="电话"
            prop="mobile"
          >
            <a-input placeholder="电话" v-model="form.mobile" />
          </a-form-model-item>

          <a-form-model-item
            label="个人简介"
            prop="description"
          >
            <a-textarea placeholder="个人简介" v-model="form.description" />
          </a-form-model-item>

          <a-form-model-item
            label="电子邮件"
            :required="false"
            prop="email"
          >
            <a-input placeholder="电子邮件" v-model="form.email" />
          </a-form-model-item>

          <a-form-model-item
            label="登录密码"
            :required="false"
            prop="password"
          >
            <a-input placeholder="密码" v-model="form.password" />
          </a-form-model-item>

          <a-form-model-item
            label="性别"
            :required="false"
            prop="ssex"
          >
            <a-radio-group v-model="form.sex">
              <a-radio :value="item.value" v-for="item in dictTypes" :key="item.id">{{ item.label }}</a-radio>
            </a-radio-group>
          </a-form-model-item>

          <a-form-model-item>
            <a-button :loading="confirmLoading" type="primary" @click="handleOk">提交</a-button>
          </a-form-model-item>
        </a-form-model>

      </a-col>
      <a-col :md="24" :lg="8" :style="{ minHeight: '180px' }">
        <div class="ant-upload-preview" @click="$refs.modal.edit(1)">
          <a-icon type="cloud-upload-o" class="upload-icon" />
          <div class="mask">
            <a-icon type="plus" />
          </div>
          <img :src="option.img" id="myImgTouxiang" />
        </div>
      </a-col>

    </a-row>

    <avatar-modal ref="modal" @ok="setavatar" />

  </div>
</template>

<script>
import AvatarModal from './AvatarModal'
import { handleImg } from '@/utils/util'
import { updateById } from '@/api/sys/user'
import { validateEmailRule, validateMobileRule } from '@/utils/validate'
import { getDictItemsByType } from '@/api/sys/dict'

export default {
  components: {
    AvatarModal
  },
  data () {
    return {
      dictTypes: [],
      confirmLoading: false,
      rules: {
        mobile: [
          { validator: validateMobileRule, trigger: 'blur' }
        ],
        email: [
          {
            validator: validateEmailRule, trigger: 'blur'
          }
        ]
      },
      form: {
        sex: this.$store.getters.userInfo.sysUser.sex,
        avatar: this.$store.getters.url, // 数据库中的地址
        mobile: this.$store.getters.userInfo.sysUser.mobile,
        description: this.$store.getters.userInfo.sysUser.description,
        email: this.$store.getters.userInfo.sysUser.email,
        password: undefined,
        userId: this.$store.getters.userInfo.sysUser.userId
      },
      // cropper
      preview: {},
      option: {
        img: this.$store.getters.avatar, // 显示的头像
        info: true,
        size: 1,
        outputType: 'jpeg',
        canScale: false,
        autoCrop: true,
        // 只有自动截图开启 宽度高度才生效
        autoCropWidth: 180,
        autoCropHeight: 180,
        fixedBox: true,
        // 开启宽度和高度比例
        fixed: true,
        fixedNumber: [1, 1]
      }
    }
  },
  computed: {
    userInfo () {
      return this.$store.getters.userInfo
    }
  },
  created () {
    getDictItemsByType('sex_type').then(res => {
      this.dictTypes = res.data
    })
  },
  methods: {
    handleOk () {
      this.$refs.form.validate(async valid => {
        if (valid) {
          if (this.form.mobile && this.form.mobile.indexOf('*') > 0) {
            this.form.mobile = undefined
          }
          if (this.form.email && this.form.email.indexOf('*') > 0) {
            this.form.email = undefined
          }
          this.confirmLoading = true
          await updateById(this.form).then(res => {
            this.$message.success('更新成功')
          })
          setTimeout(() => {
            this.confirmLoading = false
            this.$store.dispatch('Logout').then(() => {
              this.$router.push({ path: '/user/login' })
            })
          }, 200)
        } else {
          return false
        }
      })
    },
    setavatar (url) {
      this.form.avatar = url
      handleImg(url, 'myImgTouxiang')
    }
  }
}
</script>

<style lang="less" scoped>

.avatar-upload-wrapper {
  height: 200px;
  width: 100%;
}

.ant-upload-preview {
  position: relative;
  margin: 0 auto;
  width: 100%;
  max-width: 180px;
  border-radius: 50%;
  box-shadow: 0 0 4px #ccc;

  .upload-icon {
    position: absolute;
    top: 0;
    right: 10px;
    font-size: 1.4rem;
    padding: 0.5rem;
    background: rgba(222, 221, 221, 0.7);
    border-radius: 50%;
    border: 1px solid rgba(0, 0, 0, 0.2);
  }

  .mask {
    opacity: 0;
    position: absolute;
    background: rgba(0, 0, 0, 0.4);
    cursor: pointer;
    transition: opacity 0.4s;

    &:hover {
      opacity: 1;
    }

    i {
      font-size: 2rem;
      position: absolute;
      top: 50%;
      left: 50%;
      margin-left: -1rem;
      margin-top: -1rem;
      color: #d6d6d6;
    }
  }

  img, .mask {
    width: 100%;
    max-width: 180px;
    height: 100%;
    border-radius: 50%;
    overflow: hidden;
  }
}
</style>
