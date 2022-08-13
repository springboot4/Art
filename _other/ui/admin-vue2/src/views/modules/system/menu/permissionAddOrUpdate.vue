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
          label="id"
          :hidden="true"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-input
            v-decorator="['id']"
            :readonly="editable || showable"
          />
        </a-form-item>
        <a-form-item
          label="上级菜单"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <biz-select-tree
            :disabled="showable"
            :dicUrl="'/system/menu/getTreeSelect'"
            v-decorator="['parentId',{rules: [{required: true, message: '请选择上级菜单!'}]}]"
          >
          </biz-select-tree>
        </a-form-item>
        <a-form-item
          label="菜单类型"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-select
            :disabled="showable"
            @change="handleChangeMenu"
            v-decorator="['type', { rules: [{ required: true, message: '请选择菜单类型!' }] }]"
          >
            <a-select-option :key="dict.value" v-for="dict in typeDict">
              {{ dict.label }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item
          label="名称"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-input
            :disabled="showable"
            v-decorator="['title', {rules: [{required: true, message: '请输入菜单名称!'}]}]" />
        </a-form-item>
        <a-form-item
          label="URL"
          v-if="menuType !== '1'"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-input
            :disabled="showable"
            v-decorator="['path', {rules: [{required: true, message: '请输入访问路径!'}]}]" />
        </a-form-item>
        <a-form-item
          label="组件地址"
          :labelCol="labelCol"
          v-if="menuType !== '1'"
          :wrapperCol="wrapperCol"
        >
          <a-input
            :disabled="showable"
            v-decorator="['component', {rules: [{required: true, message: '请输入组件地址!'}]}]" />
        </a-form-item>
        <a-form-item
          label="组件名称"
          :labelCol="labelCol"
          v-if="menuType !== '2'"
          :wrapperCol="wrapperCol"
        >
          <a-input
            :disabled="showable"
            v-decorator="['name', {rules: [{required: true, message: '请输入组件名称!'}]}]" />
        </a-form-item>
        <a-form-item
          label="图标"
          :labelCol="labelCol"
          v-if="menuType !== '2'"
          :wrapperCol="wrapperCol"
        >
          <a-input
            :disabled="showable"
            v-decorator="['icon', {rules: [{required: true, message: '请选择图标!'}]}]" >
            <template v-slot:addonAfter>
              <a-icon
                type="setting"
                @click="selectIcons" />
            </template>
          </a-input>
        </a-form-item>
        <a-form-item
          label="重定向"
          :labelCol="labelCol"
          v-if="menuType !== '1'"
          :wrapperCol="wrapperCol"
        >
          <a-input
            :disabled="showable"
            v-decorator="['redirect', {rules: [{required: false, message: '请输入默认跳转地址redirect!'}]}]" />
        </a-form-item>
        <a-form-item
          label="是否缓存"
          :labelCol="labelCol"
          v-if="menuType !== '1'"
          :wrapperCol="wrapperCol"
        >
          <a-radio-group
            :disabled="showable"
            :options="isOrNoList"
            v-decorator="['keepAlive', {rules: [{required: true, message: '请选择页面是否缓存!'}]}]" />
        </a-form-item>
        <a-form-item
          label="是否隐藏"
          v-if="menuType !== '1'"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-radio-group
            :disabled="showable"
            :options="isOrNoList"
            v-decorator="['hidden', {rules: [{required: true, message: '请选择页面是否隐藏!'}]}]" />
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
        <a-form-item
          label="权限代码"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-input
            :disabled="showable"
            v-decorator="['perms', {rules: [{required: true, message: '请输入权限代码!'}]}]" />
        </a-form-item>
      </a-form>
    </a-spin>

    <a-modal
      :width="850"
      :visible="visibleIcon"
      @cancel="handleCancelIcon"
      footer=""
      :mask="false"
      :closable="false"
      :destroyOnClose="true"
    >
      <icon-selector v-model="form.icon" @change="handleIconChange"/>
    </a-modal>

    <template slot="footer">
      <div v-show="!showable">
        <a-button key="cancel" @click="handleCancel">取消</a-button>
        <a-button key="forward" :loading="confirmLoading" type="primary" @click="handleOk">保存</a-button>
      </div>
    </template>
  </a-modal>
</template>

<script>
import pick from 'lodash.pick'
import { addObj, putObj, getObj } from '@/api/sys/menu'
import { FormMixin } from '@/mixins/FormMixin'
import IconSelector from '@/components/IconSelector'

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
  name: 'PermissionAddOrUpdate',
  mixins: [FormMixin],
  components: { IconSelector },
  data () {
    return {
      isOrNoList,
      visible: false,
      visibleIcon: false,
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
    // 选择图标
    handleIconChange (icon) {
      this.visibleIcon = false
      console.log(icon)
      const iconData = { icon }
      const { form: { setFieldsValue } } = this
      this.$nextTick(() => {
        setFieldsValue(pick(iconData, ['icon']))
      })
    },
    // 选择图标弹出
    selectIcons () {
      if (!this.showable) {
        this.visibleIcon = true
      }
    },
    // 取消选择图标
    handleCancelIcon () {
      this.visibleIcon = false
    },
    handleChangeMenu (e) {
      this.menuType = e
    },
    edit (tmpRecord, type, permsType) {
      this.visible = true
      const { form: { setFieldsValue, resetFields } } = this
      resetFields()
      if (['edit', 'show'].includes(type)) {
        this.confirmLoading = true
        getObj(tmpRecord.id).then(res => {
          const record = res.data
          record.parentId = String(record.parentId)
          this.confirmLoading = false
          console.log('record', record)
          this.$nextTick(() => {
            setFieldsValue(pick(record, ['id', 'title', 'type', 'perms', 'parentId', 'path', 'component', 'name', 'icon', 'keepAlive', 'hidden', 'redirect', 'orderNum']))
          })
        })
      }
    },
    handleOk () {
      this.form.validateFields(async (err, values) => {
        if (!err) {
          console.log(values)
          this.confirmLoading = true
          try {
            if (this.type === 'add') {
              await addObj(values)
            } else if (this.type === 'edit') {
              await putObj(values)
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
