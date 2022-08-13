<template>
  <a-select
    v-model="text"
    :loading="loading"
    :placeholder="placeholder"
    :readonly="readonly"
    :disabled="disabled"
    @click.native="handleClick"
    @change="handleChange"
  >
    <a-select-option
      v-for="(item,index) in netDic"
      :key="index"
      :disabled="item[disabledKey]"
      :value="item[valueKey]"
    >{{ getLabelText(item) }}
    </a-select-option>
  </a-select>
</template>

<script>
import props from '../core/props'
import event from '../core/event'
import create from '../core/create'
export default create({
  name: 'select',
  mixins: [props(), event()],
  watch: {
    dic: {
      handler (val) {
        this.netDic = val
      },
      immediate: true
    }
  },
  data () {
    return {
      netDic: [],
      loading: false
    }
  },
  created () { },
  mounted () {
    console.log('select 运行请求', this.dic, this.dicUrl)
    if (this.dic.length === 0 && this.dicUrl) {
      this.loading = true
      this.axios.get(this.dicUrl).then(res => {
        this.netDic = res.data
      }).catch(err => {
        this.$message.error(err.msg)
      }).finally(_ => {
        this.loading = false
      })
    }
  },
  methods: {}
})
</script>

<style scoped>

</style>
