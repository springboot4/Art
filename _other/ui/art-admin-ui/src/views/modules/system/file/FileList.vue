<template>
  <div>
    <div class="m-3 p-3 bg-white">
      <!--搜索表单-->
      <vxe-form>
        <vxe-form-item title="文件名">
          <vxe-input placeholder="请输入文件名" v-model="model.queryParam.fileName" />
        </vxe-form-item>
        <vxe-form-item title="原始文件名">
          <vxe-input placeholder="请输入原始文件名" v-model="model.queryParam.original" />
        </vxe-form-item>
        <vxe-form-item title="桶名称">
          <vxe-input placeholder="请输入桶名称" v-model="model.queryParam.bucketName" />
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
          <a-upload
            name="file"
            :action="uploadAction"
            :headers="uploadHeader"
            @change="handleChange"
            :showUploadList="false"
            :multiple="false"
          >
            <a-button type="primary" pre-icon="ic:round-cloud-upload">上传 </a-button>
          </a-upload>
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
        <vxe-column field="fileName" title="文件名" width="150" />
        <vxe-column field="original" title="原始文件名称" width="150" />
        <vxe-column field="bucketName" title="桶名称" width="150" />
        <vxe-column field="type" title="文件类型" width="150" />
        <vxe-column field="fileSize" title="文件大小" width="150" />
        <vxe-column field="createTime" title="创建时间" width="150" />
        <vxe-column field="createBy" title="创建者" width="150" />
        <vxe-column fixed="right" width="200" title="操作">
          <template #default="{ row }">
            <span>
              <a href="javascript:" @click="downloadFile(row)">下载</a>
            </span>
            <a-divider type="vertical" />
            <span>
              <a href="javascript:" @click="preSign(row)">预览</a>
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
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { defineComponent, onMounted, ref } from 'vue'
  import { VxeTableInstance, VxeToolbarInstance } from 'vxe-table'
  import useTablePage from '/@/hooks/art/useTablePage'
  import { del, page, preSignUploadUrl } from '/@/api/system/file'
  import { useMessage } from '/@/hooks/web/useMessage'
  import { FileDownloadDTO, FileDTO } from '/@/api/system/file/types'
  import { downloadByOnlineUrl } from '/@/utils/file/download'
  import type { UploadChangeParam } from 'ant-design-vue'
  import { useUpload } from '/@/hooks/art/useUpload'

  const { handleTableChange, pageQueryResHandel, resetQuery, pagination, model, loading } =
    useTablePage(queryPage)
  const { createMessage } = useMessage()
  const xTable = ref<VxeTableInstance>()
  const xToolbar = ref<VxeToolbarInstance>()

  const { uploadHeader, uploadAction } = useUpload('/system/file/add')
  const apiUrl = import.meta.env.VITE_GLOB_API_URL

  /**
   * 文件上传状态改变
   */
  const handleChange = (info: UploadChangeParam) => {
    if (info.file.status === 'done') {
      createMessage.success(`${info.file.name} 上传成功！`)
    } else if (info.file.status === 'error') {
      createMessage.error(`${info.file.name} 上传失败`)
    }
    queryPage()
  }

  /**
   * 预览文件
   */
  function preSign(fileDto: FileDTO) {
    const preSignDto: FileDownloadDTO = {
      bucket: fileDto.bucketName,
      fileName: fileDto.fileName,
    }
    preSignUploadUrl(preSignDto).then((res) => {
      window.open(res)
    })
  }

  /**
   * 下载文件
   */
  function downloadFile(fileDto: FileDTO) {
    downloadByOnlineUrl(
      `${apiUrl}/system/file/download/${fileDto.bucketName}/${fileDto.fileName}`,
      fileDto.original as string,
    )
  }

  /**
   * 删除文件
   */
  function remove(item) {
    del(item.id).then((_) => {
      createMessage.success('删除成功')
      queryPage()
    })
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

  defineComponent({
    name: 'FileList',
  })
</script>

<style lang="less" scoped></style>
