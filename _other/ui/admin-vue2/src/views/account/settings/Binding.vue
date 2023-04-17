<template>
  <div>
    <a-spin :spinning="loading">
      <a-row :span="24">
        <a-col :span="14">
          <a-list>
            <a-list-item>
              <a-list-item-meta title="码云(gitee)">
                <template #description>
                  <span v-show="bindInfo.gitee.bind">
                    <a-avatar :src="bindInfo.gitee.avatar" />
                    {{ bindInfo.gitee.username }}&nbsp;&nbsp;
                  </span>
                  <span v-show="!bindInfo.gitee.bind">未绑定</span>
                </template>
              </a-list-item-meta>
              <template #actions>
                <a v-if="!bindInfo.gitee.bind" @click="bindThird(giteeUrl)">绑定</a>
                <a v-else @click="unBindThird('gitee')" style="color: red">解绑</a>
              </template>
            </a-list-item>
          </a-list>
        </a-col>
      </a-row>
    </a-spin>
  </div>
</template>

<script>
import { configuration } from '@/api/auth'
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import { bindInfo, unBind } from '@/api/sys/third'

export default {
  data () {
    return {
      giteeUrl: '',
      loading: false,
      // 绑定信息
      bindInfo: {
        gitee: {}
      }
    }
  },
  methods: {
    init () {
      this.loading = true
      this.getConfiguration()
      this.getBindInfo()
      this.loading = false
    },
    async getBindInfo () {
      await bindInfo().then(res => {
        this.bindInfo = res.data
      })
    },
    async getConfiguration () {
      await configuration().then(res => {
        this.giteeUrl = `http://art-gateway:9999/auth/gitee/authorize/${res.data.giteeAppid}?binding=true&access_token=${Vue.ls.get(ACCESS_TOKEN)}`
        this.loading = false
      })
    },
    /**
     * 绑定账号
     */
    bindThird (url) {
      location.href = url
    },
    /**
     * 解绑
     */
    unBindThird (type) {
      unBind(type).then(res => {
        this.init()
      })
    }
  },
  mounted () {
    this.init()
  }
}
</script>
