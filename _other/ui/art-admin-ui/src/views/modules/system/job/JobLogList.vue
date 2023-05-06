<template>
  <div>
    <div class="m-3 p-3 bg-white">
      <!--搜索表单-->
      <vxe-form>
        <vxe-form-item title="任务名称">
          <vxe-input placeholder="请输入任务名称" v-model="model.queryParam.jobName" />
        </vxe-form-item>
        <vxe-form-item title="任务分组">
          <vxe-input placeholder="请输入任务分组" v-model="model.queryParam.jobGroup" />
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
        <vxe-column field="jobName" title="任务名称" />
        <vxe-column field="jobMessage" title="日志信息" />
        <vxe-column field="status" title="执行状态">
          <template #default="{ row }">
            <span v-if="row.status === '0'"> 正常 </span>
            <span v-else> 失败 </span>
          </template>
        </vxe-column>
        <vxe-column field="exceptionInfo" title="异常信息" />
        <vxe-column field="createTime" title="创建时间" />
        <vxe-column fixed="right" width="200" title="操作">
          <template #default="{ row }">
            <span>
              <a href="javascript:" @click="show(row)">查看</a>
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
      <JobLogEdit ref="jobLogEdit" @ok="queryPage" />
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { onMounted, ref, defineComponent, watch } from 'vue'
  import { VxeTableInstance, VxeToolbarInstance } from 'vxe-table'
  import useTablePage from '/@/hooks/art/useTablePage'
  import { page } from '/@/api/system/job/log'
  import JobLogEdit from './JobLogEdit.vue'
  import { FormOperationType } from '/@/enums/formOperationType'
  import { router } from '/@/router'

  const { handleTableChange, pageQueryResHandel, resetQuery, pagination, model, loading } =
    useTablePage(queryPage)
  const xTable = ref<VxeTableInstance>()
  const xToolbar = ref<VxeToolbarInstance>()
  const jobLogEdit = ref()

  watch(
    () => router.currentRoute.value.query,
    (newValue) => {
      const queryParam = newValue as { jobName?: string; jobGroup?: string }
      if (queryParam) {
        ;(model.queryParam as { jobName?: string; jobGroup?: string }).jobName = queryParam.jobName
        ;(model.queryParam as { jobName?: string; jobGroup?: string }).jobGroup =
          queryParam.jobGroup
        queryPage()
      }
    },
    { immediate: true },
  )

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
   * 查看
   */
  function show(item) {
    jobLogEdit.value?.init(item.id, FormOperationType.SHOW)
  }

  defineComponent({
    name: 'JobLogList',
  })
</script>

<style lang="less" scoped></style>
