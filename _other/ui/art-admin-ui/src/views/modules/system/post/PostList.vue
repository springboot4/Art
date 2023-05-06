<template>
  <div>
    <div class="m-3 p-3 bg-white">
      <!--搜索表单-->
      <vxe-form>
        <vxe-form-item title="岗位名称">
          <vxe-input placeholder="请输入" v-model="model.queryParam.postName" />
        </vxe-form-item>
        <vxe-form-item title="岗位编码">
          <vxe-input placeholder="请输入" v-model="model.queryParam.postCode" />
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
        <vxe-column field="postName" title="岗位名称" />
        <vxe-column field="postCode" title="岗位编码" />
        <vxe-column field="description" title="岗位描述" />
        <vxe-column field="createTime" title="创建时间" />
        <vxe-column fixed="right" width="200" title="操作">
          <template #default="{ row }">
            <span>
              <a href="javascript:" @click="show(row)">查看</a>
            </span>
            <a-divider type="vertical" />
            <span>
              <a href="javascript:" @click="edit(row)">编辑</a>
            </span>
            <a-divider type="vertical" />
            <a-popconfirm title="是否删除" @confirm="remove(row)" okText="是" cancelText="否">
              <a href="javascript:" style="color: red">删除</a>
            </a-popconfirm>
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
      <PostEdit ref="postEdit" @ok="queryPage" />
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { onMounted, ref, defineComponent } from 'vue'
  import { VxeTableInstance, VxeToolbarInstance } from 'vxe-table'
  import useTablePage from '/@/hooks/art/useTablePage'
  import { page, del } from '/@/api/system/post'
  import PostEdit from './PostEdit.vue'
  import { FormOperationType } from '/@/enums/formOperationType'
  import { useMessage } from '/@/hooks/web/useMessage'

  const { handleTableChange, pageQueryResHandel, resetQuery, pagination, model, loading } =
    useTablePage(queryPage)
  const { createMessage } = useMessage()
  const xTable = ref<VxeTableInstance>()
  const xToolbar = ref<VxeToolbarInstance>()
  const postEdit = ref()

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
    postEdit.value?.init(null, FormOperationType.ADD)
  }

  /**
   * 查看
   */
  function show(item) {
    postEdit.value?.init(item.postId, FormOperationType.SHOW)
  }

  /**
   * 编辑
   */
  function edit(item) {
    postEdit.value?.init(item.postId, FormOperationType.EDIT)
  }

  /**
   * 删除
   */
  function remove(item) {
    del(item.postId).then((_) => {
      createMessage.success('删除成功')
      queryPage()
    })
  }

  defineComponent({
    name: 'PostList',
  })
</script>

<style lang="less" scoped></style>
