<template>
  <a-date-picker
    :defaultValue="moment(text, format)"
    :placeholder="placeholder"
    :readonly="readonly"
    :disabled="disabled"
    :format="format"
    @click.native="handleClick"
    @change="handleChange"
  />
</template>

<script>
import props from '../core/props'
import event from '../core/event'
import create from '../core/create'
import moment from 'moment'

export default create({
  name: 'date',
  mixins: [props(), event()],
  data () {
    return {
      name: 'date'
    }
  },
  props: {
    valueFormat: {},
    format: {}
  },
  methods: {
    moment
  }
})
</script>

<style scoped>

</style>
