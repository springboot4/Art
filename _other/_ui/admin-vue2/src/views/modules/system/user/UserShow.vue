<template>
  <a-drawer
    :title="title"
    :width="650"
    :mask-closable="showable"
    @close="handleCancel"
    :visible="visible"
    :confirmLoading="confirmLoading">
    <a-spin :spinning="confirmLoading" style="margin-bottom: 3rem">
      <a-form-model
        ref="form"
        :labelCol="labelCol"
        :wrapperCol="wrapperCol"
        :model="form">
        <a-form-model-item label="用户账号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="username">
          <span>{{ form.username }}</span>
        </a-form-model-item>
        <a-form-model-item label="部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="username">
          <span>{{ form.deptName }}</span>
        </a-form-model-item>
        <a-form-model-item label="邮箱" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="username">
          <span>{{ form.email }}</span>
        </a-form-model-item>
        <a-form-model-item label="手机号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="username">
          <span>{{ form.mobile }}</span>
        </a-form-model-item>
        <a-form-model-item label="注册时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="username">
          <span>{{ form.createTime }}</span>
        </a-form-model-item>
        <a-form-model-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="username">
          <span>{{ form.description }}</span>
        </a-form-model-item>
        <a-form-model-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="username">
          <span v-if="form.sex==='0'">男 <a-icon type="man" /></span>
          <span v-if="form.sex==='1'">女 <a-icon type="woman" /></span>
        </a-form-model-item>
        <a-form-model-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="username">
          <a-tag v-if="form.status==='0'" color="#f50">锁定</a-tag>
          <a-tag v-if="form.status==='1'" color="pink">正常</a-tag>
        </a-form-model-item>
        <a-form-model-item label="角色列表">
          <a-tag color="green" v-for="o in roles" :key="o">{{ o }}</a-tag>
        </a-form-model-item>
        <a-form-model-item label="岗位列表">
          <a-tag color="cyan" v-for="o in posts" :key="o">{{ o }}</a-tag>
        </a-form-model-item>
        <a-form-model-item label="部门列表">
          <a-tag color="blue" v-for="o in deptList" :key="o">{{ o }}</a-tag>
        </a-form-model-item>
      </a-form-model>
    </a-spin>
  </a-drawer>
</template>

<script>
import { FormMixin } from '@/mixins/FormMixin'

export default {
  name: 'UserShow',
  mixins: [FormMixin],
  data () {
    return {
      userStatusCode: 'UserStatusCode',
      form: {},
      roles: [],
      posts: [],
      dataScopes: [],
      deptList: []
    }
  },
  methods: {
    edit (row) {
      this.form = row

      if (row.roleName) {
        this.roles = row.roleName && row.roleName.split(',')
      }
      if (row.postName) {
        this.posts = row.postName && row.postName.split(',')
      }
      if (row.deptName) {
        this.deptList = row.deptName && row.deptName.split(',')
      }
      this.confirmLoading = false
    }
  }
}
</script>

<style scoped>

</style>
