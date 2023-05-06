<template>
  <div>
    <div class="m-3 p-3 bg-white">
      <!--搜索表单-->
      <vxe-form>
        <vxe-form-item title="任务名称">
          <vxe-input placeholder="请输入任务名称" v-model="model.queryParam.jobName" />
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
        <vxe-column field="jobName" title="任务名称" width="100" />
        <vxe-column field="jobGroup" title="任务组名" width="100" />
        <vxe-column field="parameters" title="方法执行参数" width="200" />
        <vxe-column field="cronExpression" title="cron执行表达式" width="200" />
        <vxe-column field="status" title="状态" width="100">
          <template #default="{ row }">
            <a-switch :checked="checkSwitch(row)" @change="onChange(row)" />
          </template>
        </vxe-column>
        <vxe-column field="createTime" title="创建时间" width="200" />
        <vxe-column field="remark" title="备注信息" width="200" />
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
            <a-dropdown>
              <a class="ant-dropdown-link" @click.prevent>
                更多
                <DownOutlined />
              </a>
              <template #overlay>
                <a-menu>
                  <a-menu-item>
                    <a-popconfirm
                      :title="`确定执行一次${row.jobName}任务?`"
                      @confirm="handleRun(row)"
                      okText="是"
                      cancelText="否"
                    >
                      <a href="javascript:;">执行一次</a>
                    </a-popconfirm>
                  </a-menu-item>
                  <a-menu-item>
                    <a @click="handleJobLog(row)">执行日志</a>
                  </a-menu-item>
                  <a-menu-item>
                    <a-popconfirm
                      title="是否删除"
                      @confirm="remove(row)"
                      okText="是"
                      cancelText="否"
                    >
                      <a href="javascript:" style="color: red">删除</a>
                    </a-popconfirm>
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
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
      <JobEdit ref="jobEdit" @ok="queryPage" />
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { onMounted, ref, defineComponent } from 'vue'
  import { VxeTableInstance, VxeToolbarInstance } from 'vxe-table'
  import useTablePage from '/@/hooks/art/useTablePage'
  import { page, del, changeJobStatus, runJob } from '/@/api/system/job/index'
  import JobEdit from './JobEdit.vue'
  import { FormOperationType } from '/@/enums/formOperationType'
  import { useMessage } from '/@/hooks/web/useMessage'
  import { DownOutlined } from '@ant-design/icons-vue'
  import { router } from '/@/router'

  const { handleTableChange, pageQueryResHandel, resetQuery, pagination, model, loading } =
    useTablePage(queryPage)
  const { createMessage } = useMessage()
  const xTable = ref<VxeTableInstance>()
  const xToolbar = ref<VxeToolbarInstance>()
  const jobEdit = ref()

  onMounted(() => {
    vxeBind()
    queryPage()
  })

  function handleJobLog(row) {
    router.push({
      path: '/job/log',
      query: {
        jobName: row.jobName,
        jobGroup: row.jobGroup,
      },
    })
  }

  function handleRun(row) {
    runJob(row).then(() => {
      createMessage.success('执行成功')
    })
  }

  function checkSwitch(row) {
    return row.status === '0'
  }

  function onChange(checked) {
    if (checked.status === '0') {
      checked.status = '1'
    } else {
      checked.status = '0'
    }
    changeJobStatus(checked).then((_) => {
      queryPage()
    })
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
    jobEdit.value?.init(null, FormOperationType.ADD)
  }

  /**
   * 查看
   */
  function show(item) {
    jobEdit.value?.init(item.jobId, FormOperationType.SHOW)
  }

  /**
   * 编辑
   */
  function edit(item) {
    jobEdit.value?.init(item.jobId, FormOperationType.EDIT)
  }

  /**
   * 删除
   */
  function remove(item) {
    del(item.jobId).then((_) => {
      createMessage.success('删除成功')
      queryPage()
    })
  }

  defineComponent({
    name: 'JobList',
  })
</script>

<style lang="less" scoped></style>
