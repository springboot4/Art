<template>
  <div>
    <div class="m-3 p-3 bg-white">
      <vxe-form>
        <vxe-form-item title="数据源">
          <vxe-select v-model="model.queryParam.dsName" placeholder="请选择">
            <vxe-option
              v-for="item in dsList"
              :key="item.id"
              :value="item.name"
              :label="item.name"
            />
          </vxe-select>
        </vxe-form-item>
        <vxe-form-item title="表名称">
          <vxe-input placeholder="请输入表名称" v-model="model.queryParam.tableName" />
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
      <vxe-toolbar ref="xToolbar" custom print export :refresh="{ query: queryPage }" />

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
        <vxe-column field="tableName" title="表名" />
        <vxe-column field="engine" title="引擎类型" />
        <vxe-column field="tableComment" title="表描述" />
        <vxe-column field="createTime" title="创建时间" />
        <vxe-column fixed="right" width="200" title="操作">
          <template #default="{ row }">
            <span>
              <a href="javascript:" @click="show(row)">预览</a>
            </span>
            <a-divider type="vertical" />
            <span>
              <a href="javascript:" @click="genCode(row)">生成</a>
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
      <ShowCode ref="showCode" @ok="queryPage" />
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { defineComponent, onMounted, Ref, ref } from 'vue'
  import { VxeTableInstance, VxeToolbarInstance } from 'vxe-table'
  import useTablePage from '/@/hooks/art/useTablePage'
  import { page, genCodeZip } from '/@/api/system/gen/code'
  import { FormOperationType } from '/@/enums/formOperationType'
  import { listDs } from '/@/api/system/gen/datasource'
  import { GenDatasourceConfDTO } from '/@/api/system/gen/datasource/types'
  import ShowCode from '/@/views/modules/system/gen/ShowCode.vue'
  import { downloadByData } from '/@/utils/file/download'

  const { handleTableChange, pageQueryResHandel, resetQuery, pagination, model, loading } =
    useTablePage(queryPage)
  const xTable = ref<VxeTableInstance>()
  const xToolbar = ref<VxeToolbarInstance>()
  const showCode = ref()

  let dsList: Ref<GenDatasourceConfDTO[]> = ref([])
  listDs().then((res) => {
    dsList.value = res
  })

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
   * 预览代码
   */
  function show(item) {
    showCode.value?.init(
      item.tableName,
      (model.queryParam as { dsName?: string })?.dsName,
      FormOperationType.SHOW,
    )
  }

  /**
   * 下载代码
   */
  function genCode(item) {
    let params = {
      tableName: item.tableName,
      dsName: (model.queryParam as { dsName?: string })?.dsName || '',
    }
    genCodeZip(params).then((res) => {
      downloadByData(res, item.tableName + '.zip')
    })
  }

  defineComponent({
    name: 'CodeGenIndex',
  })
</script>

<style lang="less" scoped></style>
