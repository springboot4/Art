// with polyfills
import 'core-js/stable'
import 'regenerator-runtime/runtime'
import Vue from 'vue'

import App from './App.vue'
import router from './router'
import store from './store/'
import i18n from './locales'
import { VueAxios } from './utils/request'
import ProLayout, { PageHeaderWrapper } from '@ant-design-vue/pro-layout'
import themePluginConfig from '../config/themePluginConfig'

import bootstrap from './core/bootstrap'

import './core/lazy_use'
import './permission' // permission control
import './utils/filter' // global filter
import './global.less'

import Storage from 'vue-ls'
import 'xe-utils'
import VXETable from 'vxe-table'
import 'vxe-table/lib/index.css'

import KFormDesign from 'k-form-design'
import 'k-form-design/lib/k-form-design.css'

Vue.use(KFormDesign)
Vue.config.productionTip = false // 这里有多个样式，自己可以根据需要切换
Vue.use(VueAxios)
Vue.use(VXETable)

Vue.component('pro-layout', ProLayout)
Vue.component('page-header-wrapper', PageHeaderWrapper)
window.umi_plugin_ant_themeVar = themePluginConfig.theme

Vue.use(Storage, {
  namespace: 'Fxz_', // key prefix
  name: 'ls', // name variable Vue.[ls] or this.[$ls],
  storage: 'local' // storage name session, local, memory
})

new Vue({
  router,
  store,
  i18n,
  created: bootstrap,
  render: h => h(App)
}).$mount('#app')
