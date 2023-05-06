import type { AppRouteRecordRaw, Menu } from '/@/router/types'

import { defineStore } from 'pinia'
import { store } from '/@/store'
import { useI18n } from '/@/hooks/web/useI18n'
import { useUserStore } from './user'
import { flatMultiLevelRoutes, transformObjToRoute } from '/@/router/helper/routeHelper'
import { transformRouteToMenu } from '/@/router/helper/menuHelper'
import { DASHBOARD, PAGE_NOT_FOUND_ROUTE } from '/@/router/routes/basic'

import { filter } from '/@/utils/helper/treeHelper'

import { getMenuList } from '/@/api/base/menu'

import { useMessage } from '/@/hooks/web/useMessage'
import { PageEnum } from '/@/enums/pageEnum'
import { getMenuListResultModel } from '/@/api/base/model/menuModel'

interface PermissionState {
  // Permission code list
  permCodeList: string[] | number[]
  // Whether the route has been dynamically added
  isDynamicAddedRoute: boolean
  // To trigger a menu update
  lastBuildMenuTime: number
  // Backstage menu list
  backMenuList: Menu[]
  frontMenuList: Menu[]
}

export const usePermissionStore = defineStore({
  id: 'app-permission',
  state: (): PermissionState => ({
    permCodeList: [],
    // Whether the route has been dynamically added
    isDynamicAddedRoute: false,
    // To trigger a menu update
    lastBuildMenuTime: 0,
    // Backstage menu list
    backMenuList: [],
    // menu List
    frontMenuList: [],
  }),
  getters: {
    getPermCodeList(): string[] | number[] {
      return this.permCodeList
    },
    getBackMenuList(): Menu[] {
      return this.backMenuList
    },
    getFrontMenuList(): Menu[] {
      return this.frontMenuList
    },
    getLastBuildMenuTime(): number {
      return this.lastBuildMenuTime
    },
    getIsDynamicAddedRoute(): boolean {
      return this.isDynamicAddedRoute
    },
  },
  actions: {
    setPermCodeList(codeList: string[]) {
      this.permCodeList = codeList
    },

    setBackMenuList(list: Menu[]) {
      this.backMenuList = list
      list?.length > 0 && this.setLastBuildMenuTime()
    },

    setFrontMenuList(list: Menu[]) {
      this.frontMenuList = list
    },

    setLastBuildMenuTime() {
      this.lastBuildMenuTime = new Date().getTime()
    },

    setDynamicAddedRoute(added: boolean) {
      this.isDynamicAddedRoute = added
    },
    resetState(): void {
      this.isDynamicAddedRoute = false
      this.permCodeList = []
      this.backMenuList = []
      this.lastBuildMenuTime = 0
    },
    async buildRoutesAction(): Promise<AppRouteRecordRaw[]> {
      const { t } = useI18n()
      const userStore = useUserStore()

      // 路由信息
      let routes: AppRouteRecordRaw[] = []
      const routeRemoveIgnoreFilter = (route: AppRouteRecordRaw) => {
        const { meta } = route
        const { ignoreRoute } = meta || {}
        return !ignoreRoute
      }

      /**
       * @description 根据设置的首页path，修正routes中的affix标记（固定首页）
       * */
      const patchHomeAffix = (routes: AppRouteRecordRaw[]) => {
        if (!routes || routes.length === 0) return
        let homePath: string = userStore.getUserInfo.homePath || PageEnum.BASE_HOME

        function patcher(routes: AppRouteRecordRaw[], parentPath = '') {
          if (parentPath) parentPath = parentPath + '/'
          routes.forEach((route: AppRouteRecordRaw) => {
            const { path, children, redirect } = route
            const currentPath = path.startsWith('/') ? path : parentPath + path
            if (currentPath === homePath) {
              if (redirect) {
                homePath = route.redirect! as string
              } else {
                route.meta = Object.assign({}, route.meta, { affix: true })
                throw new Error('end')
              }
            }
            children && children.length > 0 && patcher(children, currentPath)
          })
        }

        try {
          patcher(routes)
        } catch (e) {
          // 已处理完毕跳出循环
        }
        return
      }

      const { createMessage } = useMessage()

      createMessage.loading({
        content: t('sys.app.menuLoading'),
        duration: 1,
      })

      // 路由信息
      let routeList: AppRouteRecordRaw[] = []
      try {
        const menuItems: getMenuListResultModel = await getMenuList()
        routeList = menuItems['routes'] as AppRouteRecordRaw[]
      } catch (error) {
        console.error(error)
      }

      // 动态引入组件
      routeList = transformObjToRoute(routeList)

      //  路由转换到菜单结构
      const backMenuList = transformRouteToMenu(routeList)

      // 保存菜单信息
      this.setBackMenuList(backMenuList)

      // remove meta.ignoreRoute item
      routeList = filter(routeList, routeRemoveIgnoreFilter)
      routeList = routeList.filter(routeRemoveIgnoreFilter)

      routeList = flatMultiLevelRoutes(routeList)
      routes = [PAGE_NOT_FOUND_ROUTE, ...routeList]

      routes.push(DASHBOARD)
      patchHomeAffix(routes)
      return routes
    },
  },
})

// Need to be used outside the setup
export function usePermissionStoreWithOut() {
  return usePermissionStore(store)
}
