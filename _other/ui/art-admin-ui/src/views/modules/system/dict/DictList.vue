<template>
  <div>
    <div class="m-3 p-3 bg-white">
      <!--搜索表单-->
      <vxe-form>
        <vxe-form-item title="字典类型">
          <vxe-input placeholder="请输入" v-model="model.queryParam.type" />
        </vxe-form-item>
        <vxe-form-item title="字典类型">
          <vxe-select v-model="model.queryParam.systemFlag" placeholder="请选择">
            <vxe-option
              v-for="item in dictItems"
              :key="item.id"
              :value="item.value"
              :label="item.label"
            />
          </vxe-select>
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
        <vxe-column field="type" title="字典编码" width="200" />
        <vxe-column field="description" title="描述" />
        <vxe-column field="remark" title="备注" />
        <vxe-column field="systemFlag" title="字典类型">
          <template #default="{ row }">
            <a-tag color="green">
              {{ dictItems.find((d) => d.value === row.systemFlag)?.label }}</a-tag
            >
          </template>
        </vxe-column>
        <vxe-column field="createTime" title="创建时间" width="200" />
        <vxe-column field="createBy" title="创建人" />
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
            <span>
              <a href="javascript:" @click="showItem(row)">字典项</a>
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
      <DictEdit ref="dictEdit" @ok="queryPage" />
      <DictItemList ref="dictItemList" @ok="queryPage" />
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { defineComponent, onMounted, ref, Ref } from 'vue'
  import { VxeTableInstance, VxeToolbarInstance } from 'vxe-table'
  import useTablePage from '/@/hooks/art/useTablePage'
  import { del, page } from '/@/api/system/dict'
  import DictEdit from './DictEdit.vue'
  import DictItemList from './DictItemList.vue'
  import { FormOperationType } from '/@/enums/formOperationType'
  import { useMessage } from '/@/hooks/web/useMessage'
  import useDict from '/@/hooks/art/useDict'
  import { DictItemDto } from '/@/api/system/dict/types'

  const { dictItemsByType } = useDict()
  const { handleTableChange, pageQueryResHandel, resetQuery, pagination, model, loading } =
    useTablePage(queryPage)
  const { createMessage } = useMessage()
  const xTable = ref<VxeTableInstance>()
  const xToolbar = ref<VxeToolbarInstance>()
  const dictEdit = ref()
  const dictItemList = ref()

  const dictItems: Ref<DictItemDto[]> = ref([])
  dictItemsByType('dict_type', dictItems.value)

  onMounted(async () => {
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
   * 查看字典项
   */
  function showItem(item) {
    dictItemList.value?.init(item)
  }

  /**
   * 添加
   */
  function add() {
    dictEdit.value?.init(null, FormOperationType.ADD)
  }

  /**
   * 查看
   */
  function show(item) {
    dictEdit.value?.init(item.id, FormOperationType.SHOW)
  }

  /**
   * 编辑
   */
  function edit(item) {
    dictEdit.value?.init(item.id, FormOperationType.EDIT)
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
    name: 'DictList',
  })
</script>

<style lang="less" scoped></style>
