import { reactive, toRefs } from 'vue'
import { FormOperationType } from '/@/enums/formOperationType'

export default function () {
  const model = reactive({
    labelCol: {
      sm: { span: 7 },
    },
    wrapperCol: {
      sm: { span: 13 },
    },
    title: '新增',
    modalWidth: 640,
    confirmLoading: false,
    visible: false,
    editable: false,
    addable: false,
    showable: false,
    formOperationType: FormOperationType.ADD,
  })

  const {
    labelCol,
    wrapperCol,
    title,
    modalWidth,
    confirmLoading,
    visible,
    editable,
    addable,
    showable,
    formOperationType,
  } = toRefs(model)

  /**
   * 初始化表单状态
   */
  function initFormEditType(operationType: FormOperationType) {
    formOperationType.value = operationType
    visible.value = true
    showable.value = false
    if (formOperationType.value === FormOperationType.ADD) {
      addable.value = true
      title.value = '新增'
    }
    if (formOperationType.value === FormOperationType.EDIT) {
      editable.value = true
      title.value = '修改'
    }
    if (formOperationType.value === FormOperationType.SHOW) {
      showable.value = true
      title.value = '查看'
    }
  }

  /**
   * 关闭
   */
  function handleCancel() {
    visible.value = false
    addable.value = false
    editable.value = false
    showable.value = false
  }

  return {
    model,
    labelCol,
    wrapperCol,
    title,
    modalWidth,
    confirmLoading,
    visible,
    editable,
    addable,
    showable,
    formOperationType,
    initFormEditType,
    handleCancel,
  }
}
