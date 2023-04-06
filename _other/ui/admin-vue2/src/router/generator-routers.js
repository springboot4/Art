/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// eslint-disable-next-line
import * as loginService from '@/api/login'
// eslint-disable-next-line
import { BasicLayout, BlankLayout, PageView, RouteView } from '@/layouts'

// 前端路由表
const constantRouterComponents = {
  // 基础页面 layout 必须引入
  BasicLayout: BasicLayout,
  BlankLayout: BlankLayout,
  RouteView: RouteView,
  PageView: PageView,
  '403': () => import(/* webpackChunkName: "error" */ '@/views/exception/403'),
  '404': () => import(/* webpackChunkName: "error" */ '@/views/exception/404'),
  '500': () => import(/* webpackChunkName: "error" */ '@/views/exception/500'),

  'analysis': () => import('@/views/dashboard/Analysis'),
  'workplace': () => import('@/views/dashboard/Workplace'),
  'monitor': () => import('@/views/dashboard/Monitor')
}

/* const constRouter = [
  {
    path: '/welcome',
    name: 'welcome',
    component: 'index/welcome',
    title: '首页',
    hidden: '1'
  }
] */

// 前端未找到页面路由（固定不用改）
const notFoundRouter = {
  path: '*',
  redirect: '/404',
  hidden: '1'
}

// 根级菜单
const rootRouter = {
  name: 'index',
  path: '/',
  component: 'BasicLayout',
  redirect: '/welcome',
  title: '首页',
  children: [],
  application: '1'
}

/**
 * 动态生成菜单
 * @param token
 * @returns {Promise<Router>}
 */
export const generatorDynamicRouter = (token) => {
  return new Promise((resolve, reject) => {
    loginService.getCurrentUserNav(token).then(res => {
      const apps = res.data.apps
      rootRouter.children = res.data.routes

      const menuNav = []
      menuNav.push(rootRouter)
      const routers = generator(menuNav)
      routers.push(notFoundRouter)

      resolve({ routers, apps })
    }).catch(err => {
      reject(err)
    })
  })
}

/**
 * 格式化树形结构数据 生成 vue-router 层级路由表
 *
 * @param routerMap
 * @returns {*}
 */
export const generator = (routerMap) => {
  return routerMap.map(item => {
    const currentRouter = {
      // 如果路由设置了 path，则作为默认 path，否则 路由地址 动态拼接生成如 /dashboard/workplace
      path: item.path,
      // 路由名称，建议唯一
      name: item.name,
      application: item.application,
      component: (constantRouterComponents[item.component]) || (() => import(`@/views/modules/${item.component}`)),

      // meta: 页面标题, 菜单图标, 页面权限(供指令权限用，可去掉)
      meta: {
        icon: item.icon,
        title: item.title,
        permission: item.perms
      }
    }

    // 是否设置了隐藏菜单
    if (item.hidden && String(item.hidden) === '1' || !item.title) {
      currentRouter.hidden = true
    }

    // 为了防止出现后端返回结果不规范，处理有可能出现拼接出两个 反斜杠
    if (currentRouter.path && !currentRouter.path.startsWith('http')) {
      currentRouter.path = currentRouter.path.replace('//', '/')
    }
    // 重定向
    item.redirect && (currentRouter.redirect = item.redirect)
    // 是否有子菜单，并递归处理
    if (item.children && item.children.length > 0) {
      // Recursion
      currentRouter.children = generator(item.children, currentRouter)
    }
    return currentRouter
  })
}
