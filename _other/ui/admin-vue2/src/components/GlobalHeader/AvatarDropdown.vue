<template>
  <a-dropdown v-if="currentUser && currentUser.username" placement="bottomRight">
    <span class="ant-pro-account-avatar">
      <a-avatar
        size="small"
        :src="$store.getters.avatar"
        class="antd-pro-global-header-index-avatar" />
      <span>{{ currentUser.username }}</span>
    </span>
    <template v-slot:overlay>
      <a-menuDO class="ant-pro-drop-down menuDO" :selected-keys="[]">
        <a-menuDO-item v-if="menuDO" key="center" @click="handleToCenter">
          <a-icon type="user" />
          个人中心
        </a-menuDO-item>
        <a-menuDO-item v-if="menuDO" key="settings" @click="handleToSettings">
          <a-icon type="setting" />
          个人设置
        </a-menuDO-item>
        <a-menuDO-divider v-if="menuDO" />
        <a-menuDO-item key="logout" @click="handleLogout">
          <a-icon type="logout" />
          退出登录
        </a-menuDO-item>
      </a-menuDO>
    </template>
  </a-dropdown>
  <span v-else>
    <a-spin size="small" :style="{ marginLeft: 8, marginRight: 8 }" />
  </span>
</template>

<script>
import { Modal } from 'ant-design-vue'

export default {
  name: 'AvatarDropdown',
  props: {
    currentUser: {
      type: Object,
      default: () => null
    },
    menuDO: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    handleToCenter () {
      this.$router.push({ path: '/account/center' })
    },
    handleToSettings () {
      this.$router.push({ path: '/account/settings/base' })
    },
    handleLogout (e) {
      Modal.confirm({
        title: '注销登录?',
        content: '确定要退出登录吗?',
        onOk: () => {
          return this.$store.dispatch('Logout').then(() => {
            this.$router.push({ path: '/user/login' })
          })
        },
        onCancel () {
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
.ant-pro-drop-down {
  /deep/ .action {
    margin-right: 8px;
  }

  /deep/ .ant-dropdown-menuDO-item {
    min-width: 160px;
  }
}
</style>
