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
      <a-form-model-item label="主键" prop="id" hidden="true">
        <a-input v-model="form.id" :disabled="showable" />
      </a-form-model-item>
      <a-form-model-item
        label="租户名"
        prop="name"
      >
        <a-input v-model="form.name" :disabled="showable" />
      </a-form-model-item>
      <a-form-model-item
        label="租户套餐"
        prop="packageId"
      >
        <a-select v-model="form.packageId" :disabled="showable">
          <a-select-option :value="row.id" v-for="row in packages" :key="row.id">{{ row.name }}</a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item
        label="管理员姓名"
        prop="tenantAdminName"
      >
        <a-input v-model="form.tenantAdminName" :disabled="showable" />
      </a-form-model-item>
      <a-form-model-item
        label="管理员手机号"
        prop="tenantAdminMobile"
      >
        <a-input v-model="form.tenantAdminMobile" :disabled="showable" />
      </a-form-model-item>
      <a-form-model-item
        label="过期时间"
        prop="expireTime"
      >
        <a-date-picker
          :disabled="showable"
          v-model="form.expireTime"
          format="YYYY-MM-DD HH:mm:ss"
          :show-time="{ defaultValue: moment('00:00:00', 'HH:mm:ss') }"
          style="width: 100%"
        />
      </a-form-model-item>
      <a-form-model-item
        label="初始账号"
        prop="username"
        v-if="type==='add'"
      >
        <a-input v-model="form.username" :disabled="showable" />
      </a-form-model-item>
      <a-form-model-item
        label="账号密码"
        prop="password"
        v-if="type==='add'"
      >
        <a-input v-model="form.password" :disabled="showable" />
      </a-form-model-item>
      <a-form-model-item
        label="账号数量"
        prop="accountCount"
      >
        <a-input-number v-model="form.accountCount" :disabled="showable" style="width: 100%" />
      </a-form-model-item>
      <a-form-model-item
        label="租户状态"
        prop="status"
      >
        <a-radio-group
          :disabled="showable"
          :options="statusEnum"
          v-model="form.status" />
      </a-form-model-item>
    </a-form-model>

    <template v-slot:footer>
      <a-button key="cancel" @click="handleCancel">取消</a-button>
      <a-button v-if="!showable" key="forward" :loading="confirmLoading" type="primary" @click="handleOk">保存
      </a-button>
    </template>
  </a-modal>
</template>

<script>
import { FormMixin } from '@/mixins/FormMixin'
import { add, get, update } from '@/api/sys/tenant'
import moment from 'moment'
import { findAll } from '@/api/sys/tenantPackage'

export default {
  name: 'SysTenantEdit',
  mixins: [FormMixin],
  data () {
    return {
      packages: [],
      form: {
        id: null,
        name: null,
        tenantAdminId: null,
        tenantAdminName: null,
        tenantAdminMobile: null,
        status: null,
        packageId: null,
        expireTime: null,
        accountCount: null,
        username: null,
        password: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null
      },
      rules: {},
      statusEnum: [
        {
          label: '开启',
          value: 0
        },
        {
          label: '关闭',
          value: 1
        }
      ]
    }
  },
  created () {
    findAll().then(res => {
      this.packages = res.data
    })
  },
  methods: {
    moment,
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
          this.form.expireTime = moment(this.form.expireTime).format('YYYY-MM-DD HH:mm:ss')
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
