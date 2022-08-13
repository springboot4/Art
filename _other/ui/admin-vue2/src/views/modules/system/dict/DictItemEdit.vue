<template>
  <a-modal
    :title="title"
    :width="modalWidth"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :maskClosable="false"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
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
          label="字典类型"
          prop="type"
        >
          <a-input v-if="!showable" :disabled="true" v-model="form.type" />
          <span v-if="showable">{{ form.type }}</span>
        </a-form-model-item>
        <a-form-model-item
          label="字典项值"
          prop="value"
        >
          <a-input v-if="!showable" v-model="form.value" />
          <span v-if="showable">{{ form.value }}</span>
        </a-form-model-item>
        <a-form-model-item
          label="字典项名称"
          prop="label"
        >
          <a-input v-if="!showable" v-model="form.label" />
          <span v-if="showable">{{ form.label }}</span>
        </a-form-model-item>
        <a-form-model-item
          v-show="wrapperCol"
          prop="sortOrder"
          label="排序">
          <a-input-number
            v-if="!showable"
            v-model="form.sortOrder"
            style="width: 200px"
          />
          <span v-if="showable">{{ form.sortOrder }}</span>
        </a-form-model-item>
        <a-form-model-item
          label="描述"
          prop="description"
        >
          <a-input
            v-if="!showable"
            v-model="form.description"
          />
          <span v-if="showable">{{ form.description }}</span>
        </a-form-model-item>
        <a-form-model-item
          label="创建时间"
          prop="createTime"
          hidden="true"
        >
          <a-input v-model="form.createTime" />
        </a-form-model-item>
        <a-form-model-item
          label="创建人"
          prop="createBy"
          hidden="true"
        >
          <a-input v-model="form.createBy" />
        </a-form-model-item>
      </a-form-model>
    </a-spin>
    <template v-slot:footer>
      <a-button key="cancel" @click="handleCancel">取消</a-button>
      <a-button v-if="!showable" key="forward" :loading="confirmLoading" type="primary" @click="handleOk">保存</a-button>
    </template>
  </a-modal>
</template>

<script>
import { FormMixin } from '@/mixins/FormMixin'
import { itemAdd, itemGet, itemUpdate, itemExistsByCode } from '@/api/sys/dictItem'

export default {
  name: 'DictItemEdit',
  mixins: [FormMixin],
  data () {
    return {
      form: {
        id: '',
        value: '',
        label: '',
        type: '',
        sortOrder: 0,
        description: '',
        dictType: '',
        dictId: ''
      },
      rules: {
        value: [
          { required: true, message: '请输入字典项值' },
          { validator: this.validateCode, trigger: 'blur' }
        ],
        label: [
          { required: true, message: '请输入字典项名称' }
        ],
        sortOrder: [
          { required: true, message: '请输入字典项排序' }
        ],
        description: [
          { required: true, message: '请输入字典项描述' }
        ]
      }
    }
  },
  methods: {
    edit (record, type) {
      this.resetForm()
      this.$nextTick(() => {
        this.form.type = record.type
        this.form.dictId = record.id
        this.form.dictType = record.type
      })
      if (['edit', 'show'].includes(type)) {
        this.confirmLoading = true
        itemGet(record.id).then(res => {
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
            await itemAdd(this.form)
          } else if (this.type === 'edit') {
            await itemUpdate(this.form).catch(e => { this.$message.error(e.msg) })
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
    // 验证字典项值是否被使用
    async validateCode (rule, v, callback) {
      // eslint-disable-next-line no-redeclare
      const { id, value, dictId } = this.form
      let res
      const queryParam = { id, value, dictId }
      if (this.type === 'edit') {
        res = await itemExistsByCode(queryParam)
      } else {
        res = await itemExistsByCode(queryParam)
      }
      console.log('res:', res)
      if (!res.data) {
        // eslint-disable-next-line no-undef
        callback()
      } else {
        // eslint-disable-next-line standard/no-callback-literal
        callback('该编码已存在!')
      }
    }
  }
}
</script>

<style scoped>

</style>
