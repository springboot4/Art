<template>
  <BasicDrawer
    forceRender
    showFooter
    v-bind="$attrs"
    title="用户详情"
    width="60%"
    :visible="visible"
    @close="visible = false"
  >
    <a-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      :label-col="labelCol"
      :wrapper-col="wrapperCol"
    >
      <a-form-item label="主键" :hidden="true" name="userId">
        <a-input v-model:value="formData.userId" :disabled="true" />
      </a-form-item>
      <a-form-item label="用户名" name="username">
        <a-input
          v-model:value="formData.username"
          :disabled="formOperationType !== FormOperationType.ADD"
          placeholder="请输入用户名"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="邮箱" name="email">
        <a-input
          v-model:value="formData.email"
          :disabled="showable"
          placeholder="请输入邮箱"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="手机号" name="mobile">
        <a-input
          v-model:value="formData.mobile"
          :disabled="showable"
          placeholder="请输入联系电话"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="性别" name="sex">
        <a-select v-model:value="formData.sex" :disabled="showable">
          <a-select-option v-for="item in sexList" :value="item.value" :key="item.value">
            {{ item.label }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="描述" name="description">
        <a-input
          v-model:value="formData.description"
          :disabled="showable"
          placeholder="请输入描述"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="角色" name="roleId">
        <a-select
          :disabled="showable"
          v-model:value="tempRole"
          mode="multiple"
          style="width: 100%"
          placeholder="请选择角色"
          :options="roleList"
          :field-names="{ label: 'roleName', value: 'roleId' }"
        />
      </a-form-item>
      <a-form-item label="岗位" name="postId">
        <a-select
          :disabled="showable"
          v-model:value="tempPost"
          mode="multiple"
          style="width: 100%"
          placeholder="请选择岗位"
          :options="postList"
          :field-names="{ label: 'postName', value: 'postId' }"
        />
      </a-form-item>
      <a-form-item label="部门" name="deptId">
        <a-tree
          :disabled="showable"
          v-if="deptTree.length"
          checkStrictly
          checkable
          defaultExpandAll
          :tree-data="deptTree"
          v-model:checkedKeys="tempDept"
        >
          <template #title="{ title }">
            {{ title }}
          </template>
        </a-tree>
      </a-form-item>
    </a-form>
    <template #footer>
      <a-space>
        <a-button @click="handleCancel">取消</a-button>
        <a-button v-if="!showable" :loading="confirmLoading" type="primary" @click="handleOk"
          >保存</a-button
        >
      </a-space>
    </template>
  </BasicDrawer>
</template>

<script lang="ts" setup>
  import { nextTick, reactive, Ref, ref, watch } from 'vue'
  import useFormEdit from '/@/hooks/art/useFormEdit'
  import { FormOperationType } from '/@/enums/formOperationType'
  import { FormInstance } from 'ant-design-vue'
  import { get, add, update } from '/@/api/system/user'
  import { UserDTO } from '/@/api/system/user/types'
  import BasicDrawer from '/@/components/Drawer/src/BasicDrawer.vue'
  import useDict from '/@/hooks/art/useDict'
  import { getAllRole } from '/@/api/system/role'
  import { getAllPost } from '/@/api/system/post'
  import { getDeptTree } from '/@/api/system/dept'

  const {
    initFormEditType,
    handleCancel,
    labelCol,
    wrapperCol,
    confirmLoading,
    visible,
    showable,
    formOperationType,
  } = useFormEdit()

  const emits = defineEmits(['ok'])

  const rules = reactive({
    username: [{ required: true, message: '请输入用户名', trigger: ['blur', 'change'] }],
    deptId: [{ required: true, message: '请输入部门ID', trigger: ['blur', 'change'] }],
    email: [
      { required: true, message: '请输入邮箱', trigger: ['blur', 'change'] },
      {
        type: 'email',
        message: '请输入正确的邮箱格式',
        trigger: ['blur', 'change'],
      },
    ],
    mobile: [
      { required: true, message: '请输入联系电话', trigger: ['blur', 'change'] },
      {
        pattern: /^1[3456789]\d{9}$/,
        message: '请输入正确的手机号码',
        trigger: ['blur', 'change'],
      },
    ],
    sex: [{ required: true, message: '请输入性别', trigger: ['blur', 'change'] }],
  })

  const formRef = ref<FormInstance>()

  let formData: Ref<UserDTO> = ref({
    userId: '',
    username: '',
    password: '',
    deptId: '',
    email: '',
    mobile: '',
    status: '',
    createTime: '',
    updateTime: '',
    lastLoginTime: '',
    sex: '',
    avatar: '',
    description: '',
    createBy: '',
    updateBy: '',
    tenantId: '',
    roleId: '',
  })

  let sexList = ref([])
  useDict().dictItemsByType('sex_type', sexList.value)

  let tempRole: Ref<string[]> = ref([])
  let roleList = ref([])
  getAllRole().then((res) => {
    roleList = res
  })

  let postList = ref([])
  getAllPost().then((res) => {
    postList = res
  })
  let tempPost: Ref<string[]> = ref([])

  let deptTree: Ref<any[]> = ref([])
  getDeptTree().then((res) => {
    deptTree.value.push(buildTree(res))
  })
  let tempDept = ref<{ checked: string[]; halfChecked: string[] | number[] }>({
    checked: [],
    halfChecked: [],
  })
  watch(tempDept, () => {
    if (tempDept.value && tempDept.value.checked?.length > 1) {
      tempDept.value.checked.shift()
    }
  })

  function buildTree(res) {
    if (res) {
      // 当前节点
      const v: { children: any[]; title: string; key: string } = {
        title: res.deptName,
        key: res.deptId,
        children: [],
      }

      //  处理子节点
      if (res.children && res.children.length > 0) {
        v.children = res.children.map((item) => {
          return buildTree(item)
        })
      }
      return v
    }
    return res
  }

  /**
   * 表单初始化
   */
  function init(id, operationType: FormOperationType) {
    initFormEditType(operationType)
    resetForm()
    getInfo(id, operationType)
  }

  /**
   * 获取表单信息
   */
  async function getInfo(id, editType: FormOperationType) {
    if ([FormOperationType.EDIT, FormOperationType.SHOW].includes(editType)) {
      confirmLoading.value = true
      await get(id).then((res) => {
        tempRole.value = res.roleId?.split(',') || []
        tempPost.value = res.postId?.split(',') || []

        if (tempDept.value && tempDept.value.checked && tempDept.value.checked.length) {
          tempDept.value.checked.splice(0)
        }
        tempDept.value.checked.push(res.deptId as string)

        formData.value = res
      })
    }
    confirmLoading.value = false
  }

  /**
   * 保存新增或者编辑
   */
  function handleOk() {
    formData.value.roleId = tempRole.value?.join(',')
    formData.value.postId = tempPost.value?.join(',')
    formData.value.deptId = tempDept.value?.checked[0]

    formRef.value?.validate().then(async () => {
      confirmLoading.value = true
      if (formOperationType.value === FormOperationType.ADD) {
        await add(formData.value)
      } else if (formOperationType.value === FormOperationType.EDIT) {
        await update(formData.value)
      }
      confirmLoading.value = false
      handleCancel()
      emits('ok')
    })
  }

  /**
   * 重置表单字段
   */
  function resetForm() {
    formData = ref({
      userId: '',
      username: '',
      password: '',
      deptId: '',
      email: '',
      mobile: '',
      status: '',
      createTime: '',
      updateTime: '',
      lastLoginTime: '',
      sex: '',
      avatar: '',
      description: '',
      createBy: '',
      updateBy: '',
      tenantId: '',
      roleId: '',
    })
    nextTick(() => {
      formRef.value?.resetFields()
      tempDept.value?.checked?.splice(0)
      tempRole.value?.splice(0)
      tempPost.value?.splice(0)
    })
  }

  /**
   * 暴露子组件init方法
   */
  defineExpose({
    init,
  })
</script>

<style lang="less" scoped></style>
