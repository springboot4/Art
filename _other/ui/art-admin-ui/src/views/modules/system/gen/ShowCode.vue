<template>
  <BasicModal
    width="80%"
    v-bind="$attrs"
    title="代码预览"
    :loading="confirmLoading"
    :visible="visible"
    :mask-closable="showable"
    @cancel="handleCancel"
    :footer="null"
  >
    <a-tabs>
      <a-tab-pane v-for="(item, index) in codeInfo" :key="index" :tab="item.name">
        <a id="btn" style="float: right" @click="copy(item.content)">复制</a>
        <pre>{{ item.content }}</pre>
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>

<script lang="ts" setup>
  import { BasicModal } from '/@/components/Modal'
  import useFormEdit from '/@/hooks/art/useFormEdit'
  import { FormOperationType } from '/@/enums/formOperationType'
  import { ref } from 'vue'
  import { codeGenPreview } from '/@/api/system/gen/code'
  import useClipboard from 'vue-clipboard3'
  import { useMessage } from '/@/hooks/web/useMessage'

  const { initFormEditType, handleCancel, confirmLoading, visible, showable } = useFormEdit()

  const { createMessage } = useMessage()

  let codeInfo = ref()

  const { toClipboard } = useClipboard()
  async function copy(text) {
    try {
      await toClipboard(text)
      createMessage.success('复制成功！')
    } catch (e) {
      createMessage.error('复制失败！')
    }
  }

  /**
   * 表单初始化
   */
  function init(tableName, dsName, operationType: FormOperationType) {
    initFormEditType(operationType)
    let queryInfo = {
      tableName: tableName,
      dsName: dsName || '',
    }
    codeGenPreview(queryInfo).then((res) => {
      codeInfo.value = res
    })
  }

  /**
   * 暴露子组件init方法
   */
  defineExpose({
    init,
  })
</script>

<style lang="less" scoped></style>
