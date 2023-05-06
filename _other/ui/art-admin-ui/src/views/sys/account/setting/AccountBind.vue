<template>
  <CollapseContainer title="账号绑定" :canExpan="false">
    <a-list>
      <a-list-item>
        <a-list-item-meta>
          <template #avatar>
            <Icon
              class="avatar"
              icon="simple-icons:gitee"
              color="red"
              v-if="!bindInfo.gitee.bind"
            />
            <a-avatar :src="bindInfo.gitee.avatar" v-else />
          </template>
          <template #title>
            Gitee
            <a-button
              type="link"
              size="small"
              class="extra"
              v-if="!bindInfo.gitee.bind"
              @click="bindThird(giteeUrl)"
            >
              绑定
            </a-button>
            <a-button
              type="link"
              size="small"
              class="extra"
              v-else
              @click="unBindThird('gitee')"
              style="color: red"
            >
              解绑
            </a-button>
          </template>
          <template #description>
            <div v-if="!bindInfo.gitee.bind">当前账号未绑定Gitee</div>
            <div v-else>
              {{ bindInfo.gitee.username }}
            </div>
          </template>
        </a-list-item-meta>
      </a-list-item>
    </a-list>
  </CollapseContainer>
</template>
<script lang="ts" setup>
  import { CollapseContainer } from '/@/components/Container/index'
  import Icon from '../../../../components/Icon/Icon.vue'
  import { onMounted, Ref, ref } from 'vue'
  import { useUserStoreWithOut } from '/@/store/modules/user'
  import { findBindInfo, unBind } from '/@/api/system/third'
  import { UserThirdBindInfo } from '/@/api/system/third/types'
  import { findConfiguration } from '/@/api/system/auth'

  const useUserStore = useUserStoreWithOut()

  let giteeUrl = ref('')
  let bindInfo: Ref<UserThirdBindInfo> = ref<UserThirdBindInfo>({ gitee: {} })

  onMounted(() => {
    init()
  })

  function init() {
    getConfiguration()
    getBindInfo()
  }

  async function getBindInfo() {
    await findBindInfo().then((res) => {
      bindInfo.value = res
    })
  }

  async function getConfiguration() {
    await findConfiguration().then((res) => {
      giteeUrl.value = `http://art-gateway:9999/auth/gitee/authorize/${res.giteeAppid}?binding=true&access_token=${useUserStore.getToken}`
    })
  }

  function bindThird(url) {
    location.href = url
  }

  function unBindThird(type) {
    unBind(type).then((_) => {
      init()
    })
  }
</script>
<style lang="less" scoped>
  .avatar {
    font-size: 40px !important;
  }

  .extra {
    margin-top: 10px;
    margin-right: 30px;
    float: right;
    cursor: pointer;
  }
</style>
