<template>
  <a-drawer
    :title="title"
    :width="650"
    :mask-closable="true"
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
        <a-form-model-item label="用户名" prop="username">
          <a-input placeholder="请输入用户账号" v-model="form.username" disabled />
        </a-form-model-item>
        <a-form-model-item label="手机号" prop="mobile">
          <a-input placeholder="请输入用户手机号" v-model="form.mobile" />
        </a-form-model-item>
        <a-form-model-item label="邮箱" prop="email">
          <a-input placeholder="请输入用户邮箱" v-model="form.email" />
        </a-form-model-item>
        <a-form-model-item label="描述" prop="email">
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
            v-model="form.roleId"
          >
            <a-select-option v-for="item in roleList" :key="item.roleId" :label="item.roleName" :value="item.roleId">
              {{ item.roleName }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="岗位" prop="postId">
          <a-select
            mode="multiple"
            style="width: 200px"
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
            defaultExpandAll
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
import { getById, updateById } from '@/api/sys/user'
import { validateEmail, validateMobile } from '@/utils/validate'
import { getDeptTree } from '@/api/sys/dept'
import { getAllRole } from '@/api/sys/role'
import { findAll } from '@/api/sys/post'

export default {
  name: 'UserEdit',
  mixins: [FormMixin],
  data () {
    return {
      postList: [],
      roleList: [],
      deptList: [],
      treeData: [],
      tempDeptId: { halfChecked: [], checked: [] },
      form: {
        roleId: '',
        postId: '',
        id: '',
        name: '',
        username: '',
        phone: '',
        email: '',
        avatar: ''
      },
      rules: {
        username: [{ required: true, message: '请输入用户名' }, { validator: this.validateUsername, trigger: 'blur' }],
        mobile: [{ required: true, message: '请输入手机号' }, { validator: this.validatePhone, trigger: 'blur' }],
        email: [{ required: true, message: '请输入邮箱' }, { validator: this.validateEmail, trigger: 'blur' }],
        description: [{ required: true, message: '请输入描述' }]
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
    onCheck () {
      if (this.tempDeptId && this.tempDeptId.checked.length > 1) {
        this.tempDeptId.checked.shift()
      }
    },
    async edit (id) {
      this.resetForm()
      this.confirmLoading = true
      await getById(id).then(res => {
        this.form = res.data
        this.tempDeptId = { halfChecked: [], checked: [] }
        if (this.form.deptId || this.form.deptId === 0) {
          this.tempDeptId.checked.push(this.form.deptId)
        }
        if (this.form.roleId) {
          this.form.roleId = this.form.roleId.split(',')
        } else {
          this.form.roleId = undefined
        }
        if (this.form.postId) {
          this.form.postId = this.form.postId.split(',')
        } else {
          this.form.postId = undefined
        }
        delete this.form.password
        this.confirmLoading = false
      })
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
    handleOk () {
      this.$refs.form.validate(async valid => {
        if (valid) {
          this.confirmLoading = true
          if (this.form.postId) {
            this.form.postId = this.form.postId.join()
          }
          if (this.form.roleId) {
            this.form.roleId = this.form.roleId.join()
          }
          if (this.tempDeptId && this.tempDeptId.checked) {
            this.form.deptId = this.tempDeptId.checked[0]
          }
          this.form.avatar = ''
          await updateById(this.form)
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
