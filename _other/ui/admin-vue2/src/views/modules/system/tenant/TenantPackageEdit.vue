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
        label="套餐名"
        prop="name"
      >
        <a-input v-model="form.name" :disabled="showable" />
      </a-form-model-item>
      <a-form-model-item
        label="关联菜单"
        prop="menuIds"
      >
        <a-tree
          :disabled="showable"
          checkStrictly
          checkable
          :tree-data="treeData"
          v-model="tempMenuId"
        >
        </a-tree>
      </a-form-model-item>
      <a-form-model-item
        label="备注"
        prop="remark"
      >
        <a-input v-model="form.remark" :disabled="showable" />
      </a-form-model-item>
      <a-form-model-item
        label="套餐状态"
        prop="status"
      >
        <a-radio-group
          :disabled="showable"
          :options="statusEnum"
          v-model="form.status" />
      </a-form-model-item>
      <a-form-model-item prop="createBy" hidden="true"/>
      <a-form-model-item prop="createTime" hidden="true"/>
      <a-form-model-item prop="updateBy" hidden="true"/>
      <a-form-model-item prop="updateTime" hidden="true"/>
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
import { add, get, update } from '@/api/sys/tenantPackage'
import { getAllMenuTree } from '@/api/sys/menu'

export default {
  name: 'SystemPackageEdit',
  mixins: [FormMixin],
  data () {
    return {
      treeData: [],
      tempMenuId: undefined,
      form: {
        id: null,
        name: null,
        status: null,
        remark: null,
        menuIds: null,
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
    getAllMenuTree().then(res => {
      if (res.data && res.data.length > 0) {
        this.treeData = res.data.map(item => {
          return this.buildTree(item)
        })
      }
    })
  },
  methods: {
    edit (id, type) {
      this.resetForm()
      if (['edit', 'show'].includes(type)) {
        this.confirmLoading = true
        get(id).then(res => {
          this.form = res.data
          this.tempMenuId = {
            checked: this.form.menuIds.split(',')
          }
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
          if (this.tempMenuId && this.tempMenuId.checked) {
            this.form.menuIds = this.tempMenuId.checked.join()
          }
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
    }
  }
}
</script>
