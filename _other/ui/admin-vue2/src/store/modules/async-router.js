/**
 * 向后端请求用户的菜单，动态生成路由
 */
import { constantRouterMap } from '@/config/router.config'
import { generatorDynamicRouter } from '@/router/generator-routers'

const permission = {
  state: {
    routers: constantRouterMap,
    addRouters: [],
    apps: [] // 应用信息
  },
  mutations: {
    SET_ROUTERS: (state, res) => {
      state.addRouters = res.routers
      state.routers = constantRouterMap.concat(res.routers)
      state.apps = res.apps
    }
  },
  actions: {
    GenerateRoutes ({ commit }) {
      return new Promise(resolve => {
        generatorDynamicRouter().then(res => {
          commit('SET_ROUTERS', res)
          resolve()
        })
      })
    }
  }
}

export default permission
