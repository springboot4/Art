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
        :data="menuTree"
        :tree-config="{
          children: 'children',
          accordion: true,
        }"
        :loading="loading"
        align="center"
      >
        <vxe-column field="title" title="菜单名称" :tree-node="true" width="200" />
        <vxe-column field="perms" title="权限标识" width="200" />
        <vxe-column field="component" title="路由组件component" width="200" />
        <vxe-column field="path" title="对应路由path" width="200" />
        <vxe-column field="icon" title="图标" width="200">
          <template #default="{ row }">
            <div v-if="row.icon">
              <icon :icon="row.icon" />
            </div>
          </template>
        </vxe-column>
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
      <MenuEdit ref="menuEdit" @ok="queryPage" />
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { defineComponent, onMounted, ref } from 'vue'
  import { VxeTableInstance, VxeToolbarInstance } from 'vxe-table'
  import { del, page } from '/@/api/system/menu'
  import MenuEdit from './MenuEdit.vue'
  import { FormOperationType } from '/@/enums/formOperationType'
  import { useMessage } from '/@/hooks/web/useMessage'
  import Icon from '/@/components/Icon'

  const { createMessage } = useMessage()
  const xTable = ref<VxeTableInstance>()
  const xToolbar = ref<VxeToolbarInstance>()
  const menuEdit = ref()

  const menuTree = ref([])

  let loading = ref(false)

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
      menuTree.value = res
      loading.value = false
    })
  }

  /**
   * 添加
   */
  function add() {
    menuEdit.value?.init(null, FormOperationType.ADD)
  }

  /**
   * 查看
   */
  function show(item) {
    menuEdit.value?.init(item.id, FormOperationType.SHOW)
  }

  /**
   * 编辑
   */
  function edit(item) {
    menuEdit.value?.init(item.id, FormOperationType.EDIT)
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

  defineComponent({
    name: 'Menu',
  })
</script>

<style lang="less" scoped></style>
