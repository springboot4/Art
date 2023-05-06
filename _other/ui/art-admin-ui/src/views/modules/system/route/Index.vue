<template>
  <div>
    <div class="m-3 p-3 bg-white">
      <div style="width: 100%">
        <a-alert message="路由配置是非常专业的事情，不建议非工程师操作。" type="warning" />
        <br />
        <Vue3JsonEditor v-model="json" :show-btns="false" @json-change="onJsonChange" />
        <br />
        <a-button @click="saveOrUpdate()" type="primary" ghost>确定</a-button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { defineComponent, onMounted, ref } from 'vue'
  import { useMessage } from '/@/hooks/web/useMessage'
  import { findAll, update } from '/@/api/system/route'
  import { Vue3JsonEditor } from 'vue3-json-editor'

  const { createMessage } = useMessage()
  let json = ref({})

  onMounted(() => {
    loadData()
  })

  function onJsonChange(value) {
    json.value = value
  }

  function saveOrUpdate() {
    update(json.value).then(() => {
      createMessage.success('更新路由成功!')
      findAll()
    })
  }

  function loadData() {
    findAll().then((res) => {
      const result = res
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
      json.value = result
    })
  }

  defineComponent({
    name: 'RouteList',
  })
</script>
