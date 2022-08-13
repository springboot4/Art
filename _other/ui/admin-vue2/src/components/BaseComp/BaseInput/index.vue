<template>
  <a-input
    v-model="text"
    :placeholder="placeholder"
    :readonly="readonly"
    :disabled="disabled"
    @click.native="handleClick"
    @change="handleChange"
  />
</template>

<script>
import props from '../core/props'
import event from '../core/event'
import create from '../core/create'

export default create({
  name: 'input',
  mixins: [props(), event()],
  data () {
    return {}
  },
  methods: {}
})
</script>

<style scoped>

</style>
