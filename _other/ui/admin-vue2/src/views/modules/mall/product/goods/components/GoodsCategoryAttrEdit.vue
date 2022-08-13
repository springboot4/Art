<template>
  <a-modal
    :title="'添加'"
    :width="modalWidth"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :maskClosable="false"
    @cancel="handleCancel"
  >
    <a-alert message="选择商品分类相关属性" type="info" close-text="关闭" />
    <a-form-model
      ref="form"
      :model="form"
      :rules="rules"
      :label-col="labelCol"
      :wrapper-col="wrapperCol"
    >
      <a-form-model-item label="属性名称" prop="attr">
        <a-select v-model="form.attr">
          <a-select-option
            v-for="item in attrList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
            {{ item.name }}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item label="属性值" prop="attrVal" v-if="form.attr">
        <a-input v-model="form.attrVal" />
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
import { listAttributes } from '@/api/mall/product/attribute'

export default {
  name: 'GoodsCategoryAttr',
  mixins: [FormMixin],
  data () {
    return {
      form: {
        attr: undefined,
        attrVal: undefined
      },
      rules: {
        attr: [{ required: true, message: '不能为空', trigger: 'blur' }],
        attrVal: [{ required: true, message: '不能为空', trigger: 'blur' }]
      },
      attrList: [],
      tmp: []
    }
  },
  props: {
    categoryId: {
      type: String,
      default: undefined
    },
    attrType: {
      type: Number,
      default: undefined
    },
    attrValList: {
      type: Array,
      default: () => []
    }
  },
  watch: {
    attrType: {
      handler () {
        this.listAttributes()
      },
      immediate: true
    },
    categoryId: {
      handler () {
        console.log('categoryId', this.categoryId)
        this.listAttributes()
      },
      immediate: true
    },
    attrValList: {
      handler () {
        this.tmp = this.attrValList
      },
      immediate: true
    }
  },
  methods: {
    listAttributes () {
      listAttributes(this.categoryId, this.attrType).then(res => {
        this.attrList = res.data
      })
    },
    edit () {
      this.resetForm()
    },
    handleOk () {
      this.$refs.form.validate(async valid => {
        if (valid) {
          const data = {
            attributeId: this.form.attr,
            value: this.form.attrVal,
            name: this.attrList.filter(item => item.id === this.form.attr)[0].name,
            type: this.attrType
          }
          this.tmp = this.tmp.filter(item => item.attributeId !== data.attributeId)

          this.tmp.push(data)

          this.$emit('ok', this.tmp)
          this.visible = false
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
