<template>
  <ScrollContainer>
    <div ref="wrapperRef" :class="prefixCls">
      <Tabs tab-position="left" :tabBarStyle="tabBarStyle">
        <template v-for="item in settingList" :key="item.key">
          <TabPane :tab="item.name">
            <component :is="item.component" />
          </TabPane>
        </template>
      </Tabs>
    </div>
  </ScrollContainer>
</template>
<script lang="ts">
  import ScrollContainer from '/@/components/Container/src/ScrollContainer.vue'
  import { Tabs } from 'ant-design-vue'
  import { defineComponent } from 'vue'
  import BaseSetting from '/@/views/sys/account/setting/BaseSetting.vue'
  import { settingList } from '/@/views/sys/account/setting/data'
  import AccountBind from '/@/views/sys/account/setting/AccountBind.vue'
  export default defineComponent({
    components: {
      ScrollContainer,
      Tabs,
      TabPane: Tabs.TabPane,
      BaseSetting,
      AccountBind,
    },
    setup() {
      return {
        prefixCls: 'account-setting',
        settingList,
        tabBarStyle: {
          width: '220px',
        },
      }
    },
  })
</script>

<style lang="less">
  .account-setting {
    margin: 12px;
    background-color: @component-background;

    .base-title {
      padding-left: 0;
    }

    .ant-tabs-tab-active {
      background-color: @item-active-bg;
    }
  }
</style>
