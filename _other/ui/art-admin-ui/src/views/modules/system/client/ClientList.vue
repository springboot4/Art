<template>
  <div>
    <div class="m-3 p-3 bg-white">
      <!--搜索表单-->
      <vxe-form>
        <vxe-form-item title="客户端">
          <vxe-input placeholder="请输入" v-model="model.queryParam.clientId" />
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
        <vxe-column field="clientId" title="客户端ID" width="100" />
        <vxe-column field="clientSecret" title="客户端密钥" width="200" />
        <vxe-column field="scope" title="域" width="200" />
        <vxe-column field="authorizedGrantTypes" title="认证类型" width="300" />
        <vxe-column field="webServerRedirectUri" title="重定向地址" width="300" />
        <vxe-column field="accessTokenValidity" title="token 有效期" width="200" />
        <vxe-column field="refreshTokenValidity" title="刷新令牌有效期" width="200" />
        <vxe-column field="additionalInformation" title="令牌扩展字段JSON" width="200" />
        <vxe-column field="autoapprove" title="是否自动放行" width="100" />
        <vxe-column field="createTime" title="创建时间" width="200" />
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
      <ClientEdit ref="clientEdit" @ok="queryPage" />
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { defineComponent, onMounted, ref } from 'vue'
  import { VxeTableInstance, VxeToolbarInstance } from 'vxe-table'
  import useTablePage from '/@/hooks/art/useTablePage'
  import { page, del } from '/@/api/system/client'
  import ClientEdit from './ClientEdit.vue'
  import { FormOperationType } from '/@/enums/formOperationType'
  import { useMessage } from '/@/hooks/web/useMessage'

  const { handleTableChange, pageQueryResHandel, resetQuery, pagination, model, loading } =
    useTablePage(queryPage)
  const { createMessage } = useMessage()
  const xTable = ref<VxeTableInstance>()
  const xToolbar = ref<VxeToolbarInstance>()
  const clientEdit = ref()

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
    clientEdit.value?.init(null, FormOperationType.ADD)
  }

  /**
   * 查看
   */
  function show(item) {
    clientEdit.value?.init(item.clientId, FormOperationType.SHOW)
  }

  /**
   * 编辑
   */
  function edit(item) {
    clientEdit.value?.init(item.clientId, FormOperationType.EDIT)
  }

  /**
   * 删除
   */
  function remove(item) {
    del(item.clientId).then((_) => {
      createMessage.success('删除成功')
      queryPage()
    })
  }

  defineComponent({
    name: 'ClientList',
  })
</script>

<style lang="less" scoped></style>
