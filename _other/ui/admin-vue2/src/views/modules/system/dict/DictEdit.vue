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
        <a-input v-model="form.id" v-if="!showable" />
        <span v-else>{{ form.id }}</span>
      </a-form-model-item>
      <a-form-model-item
        label="字典编码"
        prop="type"
      >
        <a-input v-model="form.type" v-if="!showable" />
        <span v-else>{{ form.type }}</span>
      </a-form-model-item>
      <a-form-model-item
        label="字典描述"
        prop="description"
      >
        <a-input v-model="form.description" v-if="!showable" />
        <span v-else>{{ form.description }}</span>
      </a-form-model-item>
      <a-form-model-item
        label="字典备注"
        prop="remark"
      >
        <a-input v-model="form.remark" v-if="!showable" />
        <span v-else>{{ form.remark }}</span>
      </a-form-model-item>
      <a-form-model-item
        label="字典类型"
        prop="systemFlag"
      >
        <a-select v-model="form.systemFlag" v-if="!showable">
          <a-select-option
            v-for="item in dictTypes"
            :key="item.id"
            :label="item.label"
            :value="item.value">
            {{ item.label }}
          </a-select-option>
        </a-select>
        <span v-else>
          {{ getLabel() }}
        </span>
      </a-form-model-item>
    </a-form-model>

    <template v-slot:footer>
      <a-button key="cancel" @click="handleCancel">取消</a-button>
      <a-button v-if="!showable" key="forward" :loading="confirmLoading" type="primary" @click="handleOk">保存</a-button>
    </template>
  </a-modal>
</template>

<script>
import { FormMixin } from '@/mixins/FormMixin'
import { add, get, getDictItemsByType, update } from '@/api/sys/dict'

export default {
  name: 'DictEdit',
  mixins: [FormMixin],
  data () {
    return {
      form: {
        id: null,
        type: null,
        description: null,
        remark: null,
        systemFlag: null,
        delFlag: null,
        createTime: null,
        createBy: null,
        updateBy: null,
        updateTime: null
      },
      rules: {},
      dictTypes: []
    }
  },
  created () {
    getDictItemsByType('dict_type').then(res => {
      this.dictTypes = res.data
    })
  },
  methods: {
    getLabel () {
      if (this.dictTypes && this.dictTypes.length > 0) {
        const res = this.dictTypes.filter(item => item.value === this.form.systemFlag)
        return res && res[0] && res[0].label
      }
      return ''
    },
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
          this.confirmLoading = true
          if (this.type === 'add') {
            await add(this.form)
          } else if (this.type === 'edit') {
            await update(this.form).catch(e => { this.$message.error(e.msg) })
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

<style scoped>

</style>
