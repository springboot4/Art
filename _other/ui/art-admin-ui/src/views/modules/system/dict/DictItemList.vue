<template>
  <BasicDrawer
    forceRender
    showFooter
    v-bind="$attrs"
    title="字典项列表"
    width="60%"
    :visible="visible"
    @close="visible = false"
  >
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
        :data="pagination.records"
        :loading="loading"
        align="center"
      >
        <vxe-column type="seq" width="60" title="序号" />
        <vxe-column field="label" title="字典标签" />
        <vxe-column field="value" title="字典值" />
        <vxe-column field="type" title="字典类型" />
        <vxe-column field="description" title="描述" />
        <vxe-column field="remark" title="备注" />
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
      <DictItemEdit ref="dictItemEdit" @ok="queryPage" />
    </div>
  </BasicDrawer>
</template>

<script lang="ts" setup>
  import { nextTick, ref } from 'vue'
  import { VxeTableInstance, VxeToolbarInstance } from 'vxe-table'
  import useTablePage from '/@/hooks/art/useTablePage'
  import { del, page } from '/@/api/system/dict/itemIndex'
  import DictItemEdit from './DictItemEdit.vue'
  import { FormOperationType } from '/@/enums/formOperationType'
  import { useMessage } from '/@/hooks/web/useMessage'
  import BasicDrawer from '/@/components/Drawer/src/BasicDrawer.vue'
  import { DictItemDTO } from '/@/api/system/dict/itemTypes'

  const { handleTableChange, pageQueryResHandel, pagination, model, loading } =
    useTablePage(queryPage)
  const { createMessage } = useMessage()
  const xTable = ref<VxeTableInstance>()
  const xToolbar = ref<VxeToolbarInstance>()
  const dictItemEdit = ref()
  let visible = ref(false)

  let dictFather = ref()

  nextTick(() => {
    vxeBind()
  })

  function init(dict) {
    dictFather = dict
    ;(model.queryParam as DictItemDTO).dictId = dict.id
    visible.value = true
    queryPage()
  }

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
    dictItemEdit.value?.init(null, FormOperationType.ADD, dictFather)
  }

  /**
   * 查看
   */
  function show(item) {
    dictItemEdit.value?.init(item.id, FormOperationType.SHOW, null)
  }

  /**
   * 编辑
   */
  function edit(item) {
    dictItemEdit.value?.init(item.id, FormOperationType.EDIT, null)
  }

  /**
   * 删除
   */
  function remove(item) {
    del(item.id).then((_) => {
      createMessage.success('删除成功')
      queryPage()
    })
  }

  defineExpose({
    init,
  })
</script>

<style lang="less" scoped></style>
