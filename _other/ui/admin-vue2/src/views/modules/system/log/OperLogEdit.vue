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
        label="模块标题"
        prop="title"
      >
        {{ form.title }}
      </a-form-model-item>
      <a-form-model-item
        label="业务类型"
        prop="businessType"
      >
        <a-tag v-if="form.businessType===0" color="pink">其他</a-tag>
        <a-tag v-if="form.businessType===1" color="purple">新增</a-tag>
        <a-tag v-if="form.businessType===2" color="orange">修改</a-tag>
        <a-tag v-if="form.businessType===3" color="#f50">删除</a-tag>
        <a-tag v-if="form.businessType===4" color="green">登录</a-tag>
        <a-tag v-if="form.businessType===5" color="cyan">导出</a-tag>
        <a-tag v-if="form.businessType===6" color="blue">导入</a-tag>
        <a-tag v-if="form.businessType===7" color="#2db7f5">强退</a-tag>
        <a-tag v-if="form.businessType===8" color="#87d068">生成代码</a-tag>
        <a-tag v-if="form.businessType===9" color="#108ee9">清空数据</a-tag>
      </a-form-model-item>
      <a-form-model-item
        label="方法名称"
        prop="method"
        v-if="form.businessType!==4"
      >
        {{ form.method }}
      </a-form-model-item>
      <a-form-model-item
        label="请求方式"
        prop="requestMethod"
      >
        {{ form.requestMethod }}
      </a-form-model-item>
      <a-form-model-item
        label="操作人员"
        prop="operName"
      >
        {{ form.operName }}
      </a-form-model-item>
      <a-form-model-item
        label="请求URL"
        prop="operUrl"
        v-if="form.businessType!==4"
      >
        {{ form.operUrl }}
      </a-form-model-item>
      <a-form-model-item
        label="主机地址"
        prop="operIp"
      >
        {{ form.operIp }}
      </a-form-model-item>
      <a-form-model-item
        label="请求参数"
        prop="operParam"
      >
        {{ form.operParam }}
      </a-form-model-item>
      <a-form-model-item
        label="操作状态"
        prop="status"
      >
        <a-tag v-if="form.status=='0'" color="#2db7f5">正常</a-tag>
        <a-tag v-if="form.status=='1'" color="#f50">异常</a-tag>
      </a-form-model-item>
      <a-form-model-item
        label="错误消息"
        prop="errorMsg"
        v-if="form.businessType!==4"
      >
        {{ form.errorMsg }}
      </a-form-model-item>
      <a-form-model-item
        label="执行时间"
        prop="time"
        v-if="form.businessType!==4"
      >
        {{ form.time }}
      </a-form-model-item>
      <a-form-model-item
        label="创建时间"
        prop="createTime"
      >
        {{ form.createTime }}
      </a-form-model-item>
      <a-form-model-item
        label="更新时间"
        prop="updateTime"
        hidden="true"
      >
      </a-form-model-item>
      <a-form-model-item
        label="创建人"
        prop="createBy"
        hidden="true"
      >
        <a-input v-model="form.createBy" :disabled="showable" />
      </a-form-model-item>
      <a-form-model-item
        label="更新人"
        prop="updateBy"
        hidden="true"
      >
        <a-input v-model="form.updateBy" :disabled="showable" />
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
import { add, get, update } from '@/api/sys/operLog'

export default {
  name: 'OperLogEdit',
  mixins: [FormMixin],
  data () {
    return {
      form: {
        id: null,
        title: null,
        businessType: null,
        method: null,
        requestMethod: null,
        operName: null,
        operUrl: null,
        operIp: null,
        operParam: null,
        status: null,
        errorMsg: null,
        time: null,
        createTime: null,
        updateTime: null,
        createBy: null,
        updateBy: null
      },
      rules: {}
    }
  },
  methods: {
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
