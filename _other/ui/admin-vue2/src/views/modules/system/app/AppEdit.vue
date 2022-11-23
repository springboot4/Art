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
        label="应用名称"
        prop="name"
      >
        <a-input v-model="form.name" :disabled="showable" />
      </a-form-model-item>
      <a-form-model-item
        label="应用编码"
        prop="code"
      >
        <a-input v-model="form.code" :disabled="showable" />
      </a-form-model-item>
      <a-form-item
        label="图标"
        prop="icon"
      >
        <a-input v-model="form.icon" :disabled="showable">
          <template v-slot:addonAfter>
            <a-icon
              type="setting"
              @click="selectIcons" />
          </template>
        </a-input>
      </a-form-item>
      <a-form-model-item
        label="排序"
        prop="sort"
      >
        <a-input-number v-model="form.sort" :disabled="showable" style="width: 100%"/>
      </a-form-model-item>
    </a-form-model>

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

    <template v-slot:footer>
      <a-button key="cancel" @click="handleCancel">取消</a-button>
      <a-button v-if="!showable" key="forward" :loading="confirmLoading" type="primary" @click="handleOk">保存
      </a-button>
    </template>
  </a-modal>
</template>

<script>
import { FormMixin } from '@/mixins/FormMixin'
import { get, add, update } from '@/api/sys/app'
import IconSelector from '@/components/IconSelector'

export default {
  name: 'SysAppEdit',
  mixins: [FormMixin],
  components: { IconSelector },
  data () {
    return {
      form: {
        id: null,
        name: null,
        code: null,
        icon: null,
        sort: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null
      },
      rules: {},
      visibleIcon: false
    }
  },
  methods: {
    // 选择图标
    handleIconChange (icon) {
      this.visibleIcon = false
      const iconData = { icon }
      this.form.icon = iconData.icon
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
    edit (id, type) {
      this.form = {}
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
    }
  }
}
</script>
