<template>
  <div>
    <div class="m-3 p-3 bg-white">
      <!--搜索表单-->
      <vxe-form>
        <vxe-form-item title="用户名">
          <vxe-input placeholder="请输入" v-model="model.queryParam.username" />
        </vxe-form-item>
        <vxe-form-item title="手机号">
          <vxe-input placeholder="请输入" v-model="model.queryParam.mobile" />
        </vxe-form-item>
        <vxe-form-item>
          <a-space>
            <a-button type="primary" pre-icon="ant-design:search-outlined" @click="queryPage">
              查询
            </a-button>
            <a-button type="primary" ghost pre-icon="iconamoon:restart-bold" @click="resetQuery"
              >重置</a-button
            >
          </a-space>
        </vxe-form-item>
      </vxe-form>

      <!--表格工具栏-->
      <vxe-toolbar ref="xToolbar" custom print export :refresh="{ query: queryPage }">
        <template #buttons>
          <a-button type="primary" pre-icon="lucide:folder-plus" @click="add">新建</a-button>
        </template>
      </vxe-toolbar>

      <!--表格-->
      <vxe-table
        ref="xTable"
        border
        show-header-overflow
        show-overflow
        :column-config="{ resizable: true }"
        :export-config="{}"
        :print-config="{}"
        :data="pagination.records"
        :loading="loading"
        align="center"
      >
        <vxe-column type="seq" width="60" title="序号" />
        <vxe-column field="username" title="用户名" width="100" />
        <vxe-column field="email" title="邮箱" width="150" />
        <vxe-column field="mobile" title="联系电话" width="150" />
        <vxe-column field="sex" title="性别" width="60">
          <template #default="{ row }">
            {{ sexList.find((s) => s.value === row.sex).label }}
          </template>
        </vxe-column>
        <vxe-column field="roleName" title="角色" width="100" />
        <vxe-column field="deptName" title="部门" width="100" />
        <vxe-column field="postName" title="岗位" width="100" />
        <vxe-column field="createTime" title="注册时间" width="200" />
        <vxe-column fixed="right" width="200" title="操作">
          <template #default="{ row }">
            <span>
              <a href="javascript:" @click="show(row)">查看</a>
            </span>
            <a-divider type="vertical" />
            <span>
              <a href="javascript:" @click="edit(row)">编辑</a>
            </span>
          </template>
        </vxe-column>
      </vxe-table>

      <!--表格分页-->
      <vxe-pager
        size="medium"
        :loading="loading"
        :current-page="pagination.current"
        :page-size="pagination.size"
        :total="pagination.total"
        @page-change="handleTableChange"
      />

      <!--编辑表单-->
      <UserEdit ref="userEdit" @ok="queryPage" />
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { defineComponent, onMounted, ref } from 'vue'
  import { VxeTableInstance, VxeToolbarInstance } from 'vxe-table'
  import useTablePage from '/@/hooks/art/useTablePage'
  import { page } from '/@/api/system/user'
  import UserEdit from './UserEdit.vue'
  import { FormOperationType } from '/@/enums/formOperationType'
  import useDict from '/@/hooks/art/useDict'

  const { handleTableChange, pageQueryResHandel, resetQuery, pagination, model, loading } =
    useTablePage(queryPage)
  const xTable = ref<VxeTableInstance>()
  const xToolbar = ref<VxeToolbarInstance>()
  const userEdit = ref()

  let sexList = ref([])
  useDict().dictItemsByType('sex_type', sexList.value)

  onMounted(() => {
    vxeBind()
    queryPage()
  })

  /**
   * vxe表格、工具栏绑定
   */
  function vxeBind() {
    const $table = xTable.value
    const $toolbar = xToolbar.value
    $table?.connect($toolbar as VxeToolbarInstance)
  }

  /**
   * 分页查询
   */
  function queryPage() {
    loading.value = true
    page({
      ...model.queryParam,
      ...model.pages,
    }).then((res) => {
      pageQueryResHandel(res)
    })
  }

  /**
   * 添加
   */
  function add() {
    userEdit.value?.init(null, FormOperationType.ADD)
  }

  /**
   * 查看
   */
  function show(item) {
    userEdit.value?.init(item.userId, FormOperationType.SHOW)
  }

  /**
   * 编辑
   */
  function edit(item) {
    userEdit.value?.init(item.userId, FormOperationType.EDIT)
  }

  defineComponent({
    name: 'UserList',
  })
</script>

<style lang="less" scoped></style>
