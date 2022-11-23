<template>
  <a-modal
    :title="'添加'"
    :width="modalWidth"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :maskClosable="false"
    @cancel="handleCancel"
  >
    <a-alert message="选择商品分类相关规格" type="info" close-text="关闭" />
    <a-form-model
      ref="form"
      :model="form"
      :rules="rules"
      :label-col="labelCol"
      :wrapper-col="wrapperCol"
    >
      <a-form-model-item label="商品编码" prop="skuSn">
        <a-input v-model="form.skuSn" />
      </a-form-model-item>
      <a-form-model-item label="商品名称" prop="name">
        <a-input v-model="form.name" />
      </a-form-model-item>
      <a-form-model-item label="价格" prop="price">
        <a-input-number v-model="form.price" step="0.01" style="width: 100%" />
      </a-form-model-item>
      <a-form-model-item label="库存" prop="stockNum">
        <a-input-number v-model="form.stockNum" style="width: 100%" />
      </a-form-model-item>
      <a-form-model-item
        label="商品图片"
        prop="picUrl"
      >
        <a-upload
          name="file"
          action="/api/system/file/add"
          :headers="headers"
          list-type="picture-card"
          class="avatar-uploader"
          :customRequest="uploadFunc"
          :showUploadList="false">
          <img v-if="form.picUrl" id="logoImg" :src="getImg(form.picUrl)" alt="img" width="150px"/>
          <div v-else>
            <a-icon type="plus" />
            <div class="ant-upload-text">
              上传图片
            </div>
          </div>
        </a-upload>
      </a-form-model-item>
      <a-form-model-item label="商品规格" prop="specIdList">
        <a-select v-model="form.specIdList" mode="multiple" @change="handleChange">
          <a-select-option
            v-for="item in attrList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
            {{ item.name }}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item
        v-if="form.specIdList&&form.specIdList.length>0"
        v-for="(item, index) in attrItemList"
        :key="index"
        :label="'规格' +index+':'+ (item.name) "
        :prop="'specValList.' + index + '.value'">
        <a-input v-model="form.specValList[index].value" />
      </a-form-model-item>
    </a-form-model>

    <template v-slot:footer>
      <a-button key="cancel" @click="handleCancel">取消</a-button>
      <a-button :loading="confirmLoading" type="primary" @click="handleOk">保存</a-button>
    </template>
  </a-modal>
</template>

<script>
import { FormMixin } from '@/mixins/FormMixin'
import { listAttributes } from '@/api/mall/product/attribute'
import { add as addFile } from '@/api/sys/file'

export default {
  name: 'GoodsCategorySpec',
  mixins: [FormMixin],
  data () {
    return {
      form: {
        skuSn: undefined,
        name: undefined,
        price: undefined,
        stockNum: undefined,
        picUrl: undefined,
        specIdList: [],
        specValList: []
      },
      rules: {
        skuSn: [{ required: true, message: '不能为空', trigger: 'blur' }],
        name: [{ required: true, message: '不能为空', trigger: 'blur' }],
        price: [{ required: true, message: '不能为空', trigger: 'blur' }],
        stockNum: [{ required: true, message: '不能为空', trigger: 'blur' }],
        specIdList: [{ required: true, message: '不能为空', trigger: 'blur' }]
      },
      attrList: [], // 分类相关的所有属性
      attrItemList: [], // 选中的属性
      tmp: [] // 返回给父组件的最终数据
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
        this.listAttributes()
      },
      immediate: true
    },
    attrValList: {
      handler () {
        this.tmp = JSON.parse(JSON.stringify(this.attrValList))
      },
      immediate: true
    }
  },
  methods: {
    handleChange (v) {
      // 为了保证顺序，这里用了双重循环。。。
      const arrrrr = []
      v.map(item => {
        this.attrList.forEach(attr => {
          if (item === attr.id) {
            arrrrr.push(attr)
          }
        })
      })
      this.attrItemList = arrrrr
      if (this.attrItemList && this.attrItemList.length > 0) {
        this.form.specValList = []
        this.attrItemList.forEach(item => {
          const t = {
            attributeId: item.id,
            name: item.name,
            value: '',
            type: this.attrType
          }
          this.form.specValList.push(t)
        })
      }
    },
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
          this.tmp.push(this.form)

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
    },
    uploadFunc (file) {
      const formData = new FormData()
      formData.append('file', file.file)
      addFile(formData).then(res => {
        this.form.picUrl = res.data.data.url
        file.status = 'done'
        this.$message.success('上传成功')
      }).catch(_ => {
        this.$message.error('上传失败')
      })
    },
    getImg (icon) {
      return 'http://127.0.0.1:8301' + icon
    }
  }
}
</script>

<style scoped>

</style>
