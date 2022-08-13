<template>
  <a-modal
    :title="title"
    :width="640"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :maskClosable="false"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item
          label="deptId"
          :hidden="true"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-input
            v-decorator="['deptId']"
            :read-only="editable || showable"
          />
        </a-form-item>
        <a-form-item
          label="上级部门"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <biz-select-tree
            :keykey="'deptId'"
            :val="'deptName'"
            :disabled="editable|| showable"
            :dicUrl="'/system/dept/getDeptTree'"
            v-decorator="['parentId',{rules: [{required: true, message: '请选择上级部门!'}]}]"
          >
          </biz-select-tree>
        </a-form-item>
        <a-form-item
          label="部门名称"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-input
            :disabled="showable"
            v-decorator="['deptName', {rules: [{required: true, message: '请输入部门名称!'}]}]" />
        </a-form-item>
        <a-form-item
          label="排序"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-input-number
            :disabled="showable"
            v-decorator="['orderNum', {rules: [{required: true, message: '排序!'}]}]"></a-input-number>
        </a-form-item>
      </a-form>
    </a-spin>

    <template slot="footer">
      <div v-show="!showable">
        <a-button key="cancel" @click="handleCancel">取消</a-button>
        <a-button key="forward" :loading="confirmLoading" type="primary" @click="handleOk">保存</a-button>
      </div>
    </template>
  </a-modal>
</template>

<script>
import { addDept, getDeptById, updateDept } from '@/api/sys/dept'
import pick from 'lodash.pick'
import { FormMixin } from '@/mixins/FormMixin'

const isOrNoList = [
  {
    label: '是',
    value: '1'
  },
  {
    label: '否',
    value: '0'
  }
]

export default {
  name: 'DeptAddOrUpdate',
  mixins: [FormMixin],
  data () {
    return {
      isOrNoList,
      visible: false,
      confirmLoading: false,
      form: this.$form.createForm(this),
      treeData: [],
      menuType: '0',
      typeDict: [
        {
          label: '菜单',
          value: '0'
        },
        {
          label: '按钮',
          value: '1'
        }
      ]
    }
  },
  methods: {
    handleChangeMenu (e) {
      this.menuType = e
    },
    edit (id, type, permsType) {
      this.visible = true
      const { form: { setFieldsValue, resetFields } } = this
      if (['edit', 'show'].includes(type)) {
        this.confirmLoading = true
        getDeptById(id).then(res => {
          const record = res.data
          record.parentId = String(record.parentId)
          this.confirmLoading = false
          this.$nextTick(() => {
            setFieldsValue(pick(record, ['deptId', 'parentId', 'deptName', 'orderNum']))
          })
        })
      } else {
        resetFields()
      }
    },
    handleOk () {
      this.form.validateFields(async (err, values) => {
        if (!err) {
          this.confirmLoading = true
          try {
            if (this.type === 'add') {
              await addDept(values)
            } else if (this.type === 'edit') {
              await updateDept(values)
            }
            setTimeout(() => {
              this.confirmLoading = false
              this.$emit('ok')
              this.visible = false
            }, 1500)
          } catch (err) {
            this.$message.error(err.msg)
            this.confirmLoading = false
          }
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
