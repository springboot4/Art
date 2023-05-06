<template>
  <div>
    <div class="m-3 p-3 bg-white">
      <!--搜索表单-->
      <vxe-form>
        <vxe-form-item title="操作模块">
          <vxe-input placeholder="请输入" v-model="model.queryParam.title" />
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
        <vxe-column field="title" title="操作模块" width="200" />
        <vxe-column field="businessType" title="业务类型" width="200">
          <template #default="{ row }">
            {{ OperOptions.find((option) => option.value === row.businessType.toString())?.label }}
          </template>
        </vxe-column>
        <vxe-column
          field="method"
          title="方法名称"
          width="200"
          v-if="!(OptionsEnum.LOGIN.toString() === model.queryParam?.businessType)"
        />
        <vxe-column field="operName" title="操作人员" width="200" />
        <vxe-column field="operIp" title="主机地址" width="200" />
        <vxe-column
          field="operUrl"
          title="请求url"
          width="200"
          v-if="!(OptionsEnum.LOGIN.toString() === model.queryParam.businessType)"
        />
        <vxe-column
          field="requestMethod"
          title="请求方式"
          width="200"
          v-if="!(OptionsEnum.LOGIN.toString() === model.queryParam.businessType)"
        />
        <vxe-column
          field="operParam"
          title="请求参数"
          width="200"
          v-if="!(OptionsEnum.LOGIN.toString() === model.queryParam.businessType)"
        />
        <vxe-column field="status" title="操作状态" width="200">
          <template #default="{ row }">
            {{ statusDict.find((option) => option.value === row.status?.toString())?.label }}
          </template>
        </vxe-column>
        <vxe-column field="errorMsg" title="异常信息" width="200" />
        <vxe-column field="time" title="执行时间" width="200" />
        <vxe-column field="createTime" title="创建时间" width="200" />
        <vxe-column fixed="right" width="200" title="操作">
          <template #default="{ row }">
            <span>
              <a href="javascript:" @click="show(row)">查看</a>
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
      <OperLogEdit ref="operLogEdit" @ok="queryPage" />
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { defineComponent, onMounted, ref, watch } from 'vue'
  import { useRouter } from 'vue-router'
  import { VxeTableInstance, VxeToolbarInstance } from 'vxe-table'
  import useTablePage from '/@/hooks/art/useTablePage'
  import { del, page } from '/@/api/system/log'
  import OperLogEdit from './OperLogEdit.vue'
  import { FormOperationType } from '/@/enums/formOperationType'
  import { useMessage } from '/@/hooks/web/useMessage'
  import { OperOptions, OptionsEnum } from '/@/enums/operEnum'
  import { OperLogDTO } from '/@/api/system/log/types'

  const statusDict = [
    { label: '正常', value: '0' },
    { label: '异常', value: '1' },
  ]
  const { handleTableChange, pageQueryResHandel, resetQuery, pagination, model, loading } =
    useTablePage(queryPage)
  const { createMessage } = useMessage()
  const xTable = ref<VxeTableInstance>()
  const xToolbar = ref<VxeToolbarInstance>()
  const operLogEdit = ref()
  const router = useRouter()

  watch(
    () => router.currentRoute.value.path,
    () => {
      queryPage()
    },
    { immediate: true }, // 初始化时立即执行回调函数
  )

  onMounted(() => {
    vxeBind()
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
    if (router.currentRoute.value.path.includes('login')) {
      ;(model.queryParam as OperLogDTO).businessType = OptionsEnum.LOGIN.toString()
    } else {
      ;(model.queryParam as OperLogDTO).businessType = OptionsEnum.ADD.toString()
    }
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
    operLogEdit.value?.init(item.id, FormOperationType.SHOW)
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
    name: 'OperLogList',
  })
</script>

<style lang="less" scoped></style>
