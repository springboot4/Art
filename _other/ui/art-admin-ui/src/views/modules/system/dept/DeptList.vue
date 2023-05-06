<template>
  <div>
    <div class="m-3 p-3 bg-white">
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
        :tree-config="{
          children: 'children',
          expandAll: true,
        }"
        :data="tableData"
        :loading="loading"
        align="center"
      >
        <vxe-column field="deptName" title="部门名称" :tree-node="true" />
        <vxe-column field="orderNum" title="排序" />
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

      <!--编辑表单-->
      <DeptEdit ref="deptEdit" @ok="queryPage" />
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { onMounted, ref, defineComponent, Ref } from 'vue'
  import { VxeTableInstance, VxeToolbarInstance } from 'vxe-table'
  import useTablePage from '/@/hooks/art/useTablePage'
  import { page, del } from '/@/api/system/dept'
  import DeptEdit from './DeptEdit.vue'
  import { FormOperationType } from '/@/enums/formOperationType'
  import { useMessage } from '/@/hooks/web/useMessage'
  import { DeptDTO } from '/@/api/system/dept/types'

  const { loading } = useTablePage(queryPage)
  const { createMessage } = useMessage()
  const xTable = ref<VxeTableInstance>()
  const xToolbar = ref<VxeToolbarInstance>()
  const deptEdit = ref()

  let tableData: Ref<DeptDTO[]> = ref([])

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
    page().then((res) => {
      tableData.value = [res] as DeptDTO[]
      loading.value = false
    })
  }

  /**
   * 添加
   */
  function add() {
    deptEdit.value?.init(null, FormOperationType.ADD)
  }

  /**
   * 查看
   */
  function show(item) {
    deptEdit.value?.init(item.deptId, FormOperationType.SHOW)
  }

  /**
   * 编辑
   */
  function edit(item) {
    deptEdit.value?.init(item.deptId, FormOperationType.EDIT)
  }

  /**
   * 删除
   */
  function remove(item) {
    del(item.deptId).then((_) => {
      createMessage.success('删除成功')
      queryPage()
    })
  }

  defineComponent({
    name: 'DeptList',
  })
</script>

<style lang="less" scoped></style>
