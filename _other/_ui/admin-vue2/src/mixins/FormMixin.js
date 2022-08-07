import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'

export const FormMixin = {
  data () {
    return {
      title: '新增',
      labelCol: {
        sm: { span: 7 }
      },
      wrapperCol: {
        sm: { span: 13 }
      },
      modalWidth: 640,
      confirmLoading: false,
      visible: false,
      editable: false,
      addable: false,
      showable: false,
      type: 'add',
      headers: {
        Authorization: 'Bearer ' + Vue.ls.get(ACCESS_TOKEN)
      }
    }
  },
  methods: {
    init (id, type, ...vars) { // 初始化表单
      this.visible = true
      this.type = type
      if (type && type === 'add') {
        this.addable = true
        this.title = '新增'
      }
      if (type === 'edit') {
        this.editable = true
        this.title = '修改'
      }
      if (type === 'show') {
        this.showable = true
        this.title = '查看'
      }
      this.edit(id, type, ...vars)
    },
    edit () {},
    handleCancel () {
      this.visible = false
      setTimeout(() => {
        this.addable = false
        this.showable = false
        this.editable = false
      }, 200)
    }
  }
}
