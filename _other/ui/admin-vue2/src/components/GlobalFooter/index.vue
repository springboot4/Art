<template>
  <global-footer class="footer custom-render">
    <template v-slot:links>
      <a href="https://fxz.life" target="_blank">document</a>
      <a href="https://gitee.com/fxzcloud" target="_blank">Github</a>
    </template>
    <template v-slot:copyright>
      fxzmall 2022
    </template>
  </global-footer>
</template>

<script>
import { GlobalFooter } from '@ant-design-vue/pro-layout'

export default {
  name: 'ProGlobalFooter',
  components: {
    GlobalFooter
  }
}
</script>
