<template>
  <a-card :bordered="false">
    <div style="width: 100%" >
      <vue-json-editor
        v-model="json"
        :show-btns="false"
      />
      <br>
      <a-button @click="update()">确定</a-button>
    </div>
  </a-card>

</template>

<script>
import vueJsonEditor from 'vue-json-editor'
import { findAll, edit } from '@/api/sys/route'
export default {
  name: 'RouteIndex',
  components: {
    vueJsonEditor
  },
  data () {
    return {
      json: undefined
    }
  },
  created () {
    this.findAll()
  },
  methods: {
    update () {
      edit(this.json).then(() => {
        this.$message.success('更新路由成功！')
        this.findAll()
      })
    },
    findAll () {
      findAll().then(res => {
        const result = res.data
        console.log('res:', res)
        for (let i = 0; i < result.length; i++) {
          const route = result[i]
          if (route.predicates) {
            const predicates = route.predicates
            route.predicates = JSON.parse(predicates)
          }
          if (route.filters) {
            const filters = route.filters
            route.filters = JSON.parse(filters)
          }
        }
        this.json = result
      })
    }
  }
}

</script>
