<template>
  <a-drawer
    :title="title"
    :width="650"
    :mask-closable="showable"
    @close="handleCancel"
    :visible="visible"
    :confirmLoading="confirmLoading">
    <a-spin :spinning="confirmLoading" style="margin-bottom: 3rem">
      <a-form-model
        ref="form"
        :model="form"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        :rules="rules">
        <a-form-model-item label="用户账号" prop="username">
          <a-input placeholder="请输入用户账号" v-model="form.username" />
        </a-form-model-item>
        <a-form-model-item label="手机号" prop="mobile">
          <a-input placeholder="请输入用户手机号" v-model="form.mobile" />
        </a-form-model-item>
        <a-form-model-item label="邮箱" prop="email">
          <a-input placeholder="请输入用户邮箱" v-model="form.email" />
        </a-form-model-item>
        <a-form-model-item label="描述" prop="description">
          <a-input placeholder="请输入描述" v-model="form.description" />
        </a-form-model-item>
        <a-form-model-item label="性别" prop="sex">
          <a-select v-model="form.sex">
            <a-select-option value="0">男</a-select-option>
            <a-select-option value="1">女</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="角色" prop="roleId">
          <a-select
            mode="multiple"
            style="width: 200px"
            @change="handleChange"
            v-model="form.roleId"
          >
            <a-select-option v-for="item in roleList" :key="item.roleId">
              {{ item.roleName }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="岗位" prop="postId">
          <a-select
            mode="multiple"
            style="width: 200px"
            @change="handleChange"
            v-model="form.postId"
          >
            <a-select-option v-for="item in postList" :key="item.postId">
              {{ item.postName }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="部门" prop="deptId">
          <a-tree
            checkStrictly
            checkable
            :tree-data="treeData"
            @check="onCheck"
            v-model="tempDeptId"
          >
          </a-tree>
        </a-form-model-item>
      </a-form-model>
    </a-spin>
    <div class="drawer-button">
      <a-button @click="handleCancel" style="margin-right: .8rem">取消</a-button>
      <a-button @click="handleOk" type="primary" :loading="confirmLoading">提交</a-button>
    </div>
  </a-drawer>
</template>

<script>
import { FormMixin } from '@/mixins/FormMixin'
import { getAllRole } from '@/api/sys/role'
import { addUser } from '@/api/sys/user'
import { getDeptTree } from '@/api/sys/dept'
import { validateEmail, validateMobile } from '@/utils/validate'
import { findAll } from '@/api/sys/post'

export default {
  name: 'UserAdd',
  mixins: [FormMixin],
  data () {
    return {
      confirmDirty: false,
      postList: [],
      roleList: [],
      deptList: [],
      treeData: [],
      tempDeptId: undefined,
      form: {
        username: '',
        mobile: '',
        email: '',
        sex: '',
        description: '',
        roleId: undefined,
        deptId: undefined,
        postId: undefined
      },
      rules: {
        username: [{ required: true, message: '请输入用户名' }, { validator: this.validateUsername, trigger: 'blur' }],
        mobile: [{ required: true, message: '请输入手机号' }, { validator: this.validatePhone, trigger: 'blur' }],
        email: [{ required: true, message: '请输入邮箱' }, { validator: this.validateEmail, trigger: 'blur' }],
        sex: [{ required: true, message: '性别' }]
      }
    }
  },
  created () {
    findAll().then(res => {
      this.postList = res.data
    })
    getAllRole().then(res => {
      this.roleList = res.data
    })
    getDeptTree().then(res => {
      this.treeData.push(this.buildTree(res.data))
    })
  },
  methods: {
    validateEmail (rule, value, callback) {
      if (!value) {
        callback()
      } else {
        if (validateEmail(value)) {
          callback()
        } else {
          // eslint-disable-next-line standard/no-callback-literal
          callback('请输入正确格式的邮箱!')
        }
      }
    },
    validateUsername (rule, value, callback) {
      if (!value) {
        callback()
      } else {
        if (value.length > 10) {
          // eslint-disable-next-line standard/no-callback-literal
          callback('用户名过长!')
        } else if (value.length < 3) {
          // eslint-disable-next-line standard/no-callback-literal
          callback('用户名过短!')
        } else {
          callback()
        }
      }
    },
    validatePhone (rule, value, callback) {
      if (!value) {
        callback()
      } else {
        const { msg, result } = validateMobile(value)
        if (result) {
          callback()
        } else {
          callback(msg)
        }
      }
    },
    onCheck () {
      if (this.tempDeptId && this.tempDeptId.checked.length > 1) {
        this.tempDeptId.checked.shift()
      }
    },
    buildTree (res) {
      if (res) {
        const v = {
          title: res.deptName,
          key: res.deptId
        }
        if (res.children && res.children.length > 0) {
          v.children = res.children.map((item) => {
            return this.buildTree(item)
          })
        }
        return v
      }
      return res
    },
    handleChange (v) {
    },
    edit () {
      this.tempDeptId = undefined
      this.confirmLoading = false
      this.confirmDirty = false
      this.resetForm()
    },
    handleOk () {
      this.$refs.form.validate(async valid => {
        if (valid) {
          if (this.form.postId) {
            this.form.postId = this.form.postId.join()
          }
          if (this.form.roleId) {
            this.form.roleId = this.form.roleId.join()
          }
          if (this.tempDeptId && this.tempDeptId.checked) {
            this.form.deptId = this.tempDeptId.checked[0]
          }
          this.confirmLoading = true
          await addUser(this.form)
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

<style lang="less">

</style>
