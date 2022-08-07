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
        <a-form-model-item label="roleId" prop="roleId" :hidden="true">
          <a-input :disabled="showable" placeholder="roleId" v-model="form.roleId" />
        </a-form-model-item>
        <a-form-model-item label="角色名称" prop="roleName">
          <a-input :disabled="showable" placeholder="请输入角色名称" v-model="form.roleName" />
        </a-form-model-item>
        <a-form-model-item label="角色描述" prop="remark">
          <a-input :disabled="showable" placeholder="请输入描述" v-model="form.remark" />
        </a-form-model-item>
        <a-form-model-item label="菜单权限" prop="permission">
          <a-tree
            :disabled="showable"
            checkStrictly
            checkable
            :tree-data="treeData"
            @check="onCheck"
            v-model="tempMenuId"
          >
          </a-tree>
        </a-form-model-item>
        <a-form-model-item label="数据权限" prop="dataScope">
          <a-select v-model="form.dataScope" :disabled="showable">
            <a-select-option
              v-for="item in dictTypes"
              :key="item.id"
              :label="item.label"
              :value="Number(item.value)">
              {{ item.label }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item v-if="form.dataScope===2" label="指定部门数据权限" prop="dataScopeDeptIds">
          <a-tree
            :disabled="showable"
            checkStrictly
            checkable
            :tree-data="deptTree"
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
import { getAllMenuTree } from '@/api/sys/menu'
import { addRole, getRoleById, editRole } from '@/api/sys/role'
import { getDictItemsByType } from '@/api/sys/dict'
import { getDeptTree } from '@/api/sys/dept'

export default {
  name: 'RoleAddOrUpdate',
  mixins: [FormMixin],
  data () {
    return {
      confirmDirty: false,
      dictTypes: [],
      roleList: [],
      deptList: [],
      treeData: [],
      deptTree: [],
      tempMenuId: undefined,
      tempDeptId: undefined,
      form: {
        roleName: '',
        remark: '',
        menuId: ''
      },
      rules: {
        roleName: [{ required: true, message: '请输入角色名' }],
        remark: [{ required: true, message: '请输入角色描述' }],
        dataScope: [{ required: true, message: '请选择数据权限' }]
      }
    }
  },
  created () {
    getAllMenuTree().then(res => {
      if (res.data && res.data.length > 0) {
        this.treeData = res.data.map(item => {
          return this.buildTree(item)
        })
      }
    })
    getDeptTree().then(res => {
      if (res.data) {
        this.deptTree.push(this.buildDeptTree(res.data))
      }
    })
    getDictItemsByType('data_permission_type').then(res => {
      this.dictTypes = res.data
    })
  },
  methods: {
    onCheck () {
    },
    buildDeptTree (res) {
      if (res) {
        const v = {
          title: res.deptName,
          key: res.deptId
        }
        if (res.children && res.children.length > 0) {
          v.children = res.children.map((item) => {
            return this.buildDeptTree(item)
          })
        }
        return v
      }
    },
    buildTree (res) {
      if (res) {
        const v = {
          title: res.title,
          key: res.id
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
    edit (id, type) {
      this.resetForm()
      this.tempDeptId = undefined
      this.tempMenuId = undefined
      this.confirmLoading = false
      this.confirmDirty = false
      this.visible = true
      if (['edit', 'show'].includes(type)) {
        this.confirmLoading = true
        getRoleById(id).then(res => {
          this.form = res.data
          if (this.form.menuId) {
            this.tempMenuId = {
              checked: this.form.menuId.split(',')
            }
          }
          this.confirmLoading = false
        })
      }
    },
    handleOk () {
      this.$refs.form.validate(async valid => {
        if (valid) {
          if (this.tempMenuId && this.tempMenuId.checked) {
            this.form.menuId = this.tempMenuId.checked.join()
          }
          if (this.tempDeptId && this.tempDeptId.checked) {
            this.form.dataScopeDeptIds = this.tempDeptId.checked.join()
          }
          console.log('form:', this.form)
          this.confirmLoading = true
          if (this.type === 'add') {
            await addRole(this.form)
          } else if (this.type === 'edit') {
            await editRole(this.form)
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

<style lang='less'>

</style>
