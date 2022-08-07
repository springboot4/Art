<template>
  <a-modal
    :title="'代码预览'"
    width="80%"
    top="5vh"
    :visible="visible"
    :maskClosable="false"
    @cancel="handleCancel"
    :footer="null"
  >
    <a-tabs >
      <a-tab-pane v-for="(item, index) in tableData" :key="index" :tab="item.name">
        <a id="btn" style="float:right" @click="copyLink(item.content)">复制</a>
        <pre><code v-html="item.content"></code></pre>
      </a-tab-pane>
    </a-tabs>
  </a-modal>

</template>
<script>
import Clipboard from 'clipboard'
import 'highlight.js/styles/github-gist.css'
import { FormMixin } from '@/mixins/FormMixin'
import { codeGenPreview } from '@/api/sysTool/genCode'

export default {
  name: 'ShowCode',
  mixins: [FormMixin],
  data () {
    return {
      tableData: [],
      defaultActiveKey: null
    }
  },
  methods: {
    edit (queryInfo) {
      codeGenPreview(queryInfo).then(res => {
        this.tableData = res.data
      })
    },
    copyLink (value) {
      const clipboard = new Clipboard('#btn', { text: () => value })
      // 复制成功执行的回调
      clipboard.on('success', () => {
        this.$message.success('复制成功')
        clipboard.destroy()
      })
    }
  }
}
</script>
