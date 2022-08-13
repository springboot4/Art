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
      <a-form-model-item label="主键" prop="id" hidden="true" >
        <a-input v-model="form.id" :disabled="showable"/>
      </a-form-model-item>
      <a-form-model-item
        label="活动名称"
        prop="promotionName"
      >
        <a-input v-model="form.promotionName" :disabled="showable"/>
      </a-form-model-item>
      <a-form-model-item
        label="活动开始时间"
        prop="startTime"
      >
        <a-input v-model="form.startTime" :disabled="true"/>
      </a-form-model-item>
      <a-form-model-item
        label="报名截至时间"
        prop="applyEndTime"
      >
        <a-input v-model="form.applyEndTime" :disabled="true"/>
      </a-form-model-item>
      <a-form-model-item
        label="开启几点场"
        prop="hours"
      >
        <a-select
          mode="multiple"
          style="width: 200px"
          v-model="form.hours"
          :disabled="showable"
        >
          <a-select-option v-for="i of 24" :key="(i-1)" >
            {{ i-1 }}点场
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item
        label="申请规则"
        prop="seckillRule"
      >
        <a-input v-model="form.seckillRule" :disabled="showable"/>
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
import { get, add, update } from '@/api/mall/promotion/seckill'
export default {
  name: 'SeckillEdit',
  mixins: [FormMixin],
  data () {
    return {
      form: {
        id: null,
        promotionName: null,
        startTime: null,
        endTime: null,
        applyEndTime: null,
        hours: null,
        seckillRule: null,
        goodsNum: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        deleteFlag: null
      },
      rules: {},
      hoursList: [

      ]
    }
  },
  methods: {
    edit (id, type) {
      this.resetForm()
      if (['edit', 'show'].includes(type)) {
        this.confirmLoading = true
        get(id).then(res => {
          this.form = res.data
          const num = []
          const str = this.form.hours.split(',')
          str.forEach(item => {
            num.push(Number(item))
          })
          this.form.hours = num
          this.confirmLoading = false
        })
      } else {
        this.confirmLoading = false
      }
    },
    handleOk () {
      this.$refs.form.validate(async valid => {
        if (valid) {
          this.form.hours = this.form.hours.join(',')
          this.confirmLoading = true
          if (this.type === 'add') {
            await add(this.form)
          } else if (this.type === 'edit') {
            console.log('form', this.form)
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
    }
  }
}
</script>

<style scoped>

</style>
