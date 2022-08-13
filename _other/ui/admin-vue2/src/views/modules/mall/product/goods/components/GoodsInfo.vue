<template xmlns="">
  <div>
    <a-form-model
      style="left:20px;margin-top: 30px"
      ref="form"
      :rules="rules"
      :model="goodsInfo"
      :label-col="labelCol"
      :wrapper-col="wrapperCol">
      <a-form-model-item label="商品分类" prop="categoryId">
        <a-cascader
          v-model="goodsInfo.categoryId"
          :options="CateGoryList"
          change-on-select
          :fieldNames="{ label: 'name', value: 'id', children: 'children' }"
          placeholder="请选择商品分类" />
      </a-form-model-item>
      <a-form-model-item label="品牌名称" prop="brandId">
        <a-select v-model="goodsInfo.brandId">
          <a-select-option :key="item.id" v-for="item in brandList ">{{ item.name }}</a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item label="商品名称" prop="name">
        <a-input v-model="goodsInfo.name" />
      </a-form-model-item>
      <a-form-model-item label="原价" prop="originPrice">
        <a-input-number v-model="goodsInfo.originPrice" step="0.01" style="width: 100%;" />
      </a-form-model-item>
      <a-form-model-item label="现价" prop="price">
        <a-input-number v-model="goodsInfo.price" step="0.01" style="width: 100%;" />
      </a-form-model-item>
      <a-form-model-item label="商品简介" prop="description">
        <a-textarea v-model="goodsInfo.description" />
      </a-form-model-item>
      <a-form-model-item label="商品相册">
        <a-row
          :gutter="[16,16]">
          <a-col
            v-for="(item,index) in pictures"
            :key="index"
            :span="8">
            <a-card size="small">
              <a-upload
                name="file"
                action="/api/system/file/add"
                :headers="headers"
                list-type="picture-card"
                class="avatar-uploader"
                :show-upload-list="false"
                style="width: 130px;height: 128px;position:relative;"
                :customRequest="(...defaultArgs)=>uploadFunc(defaultArgs,index)"
                :showUploadList="false">
                <img
                  v-if="item.url"
                  id="iconUrl"
                  :src="getImg(item.url)"
                  alt="logo"
                  style="width: 100px;height: 100px;"
                />
                <a-icon type="plus" v-if="!item.url" />
                <a-icon
                  type="close"
                  v-if="item.url"
                  style="position:absolute;margin-top: -8px;right: 12px;color: #f5222d"
                  @click.stop="removeImg(index)" />
              </a-upload>
              <div v-if="item.url">
                <span v-if="item.main === true" style="color: #ff4d51;">商品主图</span>
                <span v-else @click="changeMainPicture(index)" style="color: #00A0E9">设为主图</span>
              </div>
              <div v-else>
                <span>请上传</span>
              </div>
            </a-card>
          </a-col>
        </a-row>
      </a-form-model-item>
      <a-form-model-item label="商品详情" prop="detail">
        <quill-editor @change="editChange" />
      </a-form-model-item>
    </a-form-model>
    <div style="margin-bottom: 10px;">
      <a-button
        type="primary"
        @click="handleNext"
        style="margin-right: 10px">下一步，设置商品属性
      </a-button>
    </div>
  </div>
</template>

<script>
import QuillEditor from '@/components/Editor/QuillEditor'
import { FormMixin } from '@/mixins/FormMixin'
import { list } from '@/api/mall/product/category'
import { add as addFile } from '@/api/sys/file'
import { findAll } from '@/api/mall/product/brand'

export default {
  name: 'GoodInfo',
  mixins: [FormMixin],
  components: { QuillEditor },
  data () {
    return {
      labelCol: {
        sm: { span: 2 }
      },
      wrapperCol: {
        sm: { span: 10 }
      },
      brandList: [],
      CateGoryList: [],
      pictures: [
        { url: undefined, main: true }, // main = true 代表主图，可切换
        { url: undefined, main: false },
        { url: undefined, main: false },
        { url: undefined, main: false },
        { url: undefined, main: false }
      ],
      rules: {
        categoryId: [{ required: true, message: '请选择商品分类', trigger: 'blur' }],
        name: [{ required: true, message: '请填写商品名称', trigger: 'blur' }],
        originPrice: [{ required: true, message: '请填写原价', trigger: 'blur' }],
        price: [{ required: true, message: '请填写现价', trigger: 'blur' }],
        brandId: [{ required: true, message: '请选择商品品牌', trigger: 'blur' }]
      }
    }
  },
  props: {
    goodsInfo: {
      type: Object,
      default: () => {
      }
    }
  },
  created () {
    this.init()
  },
  methods: {
    handleNext () {
      this.$refs.form.validate(async valid => {
        if (valid) {
          const mainPicUrl = this.pictures.filter((item) => item.main === true && item.url).map((item) => item.url)
          if (mainPicUrl && mainPicUrl.length > 0) {
            this.goodsInfo.picUrl = mainPicUrl[0]
          }

          const subPicUrl = this.pictures.filter((item) => item.main === false && item.url).map((item) => item.url)
          if (subPicUrl && subPicUrl.length > 0) {
            this.goodsInfo.subPicUrls = subPicUrl
          }

          this.$emit('next')
        } else {
          return false
        }
      })
    },
    changeMainPicture (changeIndex) {
      this.$message.success('设为主图')
      const currMainPicture = { ...this.pictures[0] }
      const nextMainPicture = { ...this.pictures[changeIndex] }

      this.pictures[0].url = nextMainPicture.url
      this.pictures[changeIndex].url = currMainPicture.url
    },
    editChange (html) {
      this.goodsInfo.detail = html
    },
    removeImg (index) {
      this.$message.success('删除图片成功')
      this.$set(this.pictures, index, { url: undefined })
    },
    uploadFunc (file, index) {
      file = file[0]
      const formData = new FormData()
      formData.append('file', file.file)
      addFile(formData).then(res => {
        this.pictures[index].url = res.data.data.url
        file.status = 'done'
        this.$message.success('上传成功')
      }).catch(_ => {
        this.$message.error('上传失败')
      })
    },
    init () {
      list().then(res => {
        this.CateGoryList = res.data
      })
      findAll().then(res => {
        this.brandList = res.data
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
