import type { AppRouteRecordRaw } from '/@/router/types'
import { EXCEPTION_COMPONENT, LAYOUT, PAGE_NOT_FOUND_NAME, REDIRECT_NAME } from '/@/router/constant'

// 404 on a page
export const PAGE_NOT_FOUND_ROUTE: AppRouteRecordRaw = {
  path: '/:path(.*)*',
  name: PAGE_NOT_FOUND_NAME,
  component: LAYOUT,
  meta: {
    title: 'ErrorPage',
    hideBreadcrumb: true,
    hideMenu: true,
  },
  title: '',
  children: [
    {
      path: '/:path(.*)*',
      title: '',
      name: PAGE_NOT_FOUND_NAME,
      component: EXCEPTION_COMPONENT,
      meta: {
        title: 'ErrorPage',
        hideBreadcrumb: true,
        hideMenu: true,
      },
    },
  ],
}

export const REDIRECT_ROUTE: AppRouteRecordRaw = {
  title: '',
  path: '/redirect',
  component: LAYOUT,
  name: 'RedirectTo',
  meta: {
    title: REDIRECT_NAME,
    hideBreadcrumb: true,
    hideMenu: true,
  },
  children: [
    {
      title: '',
      path: '/redirect/:path(.*)',
      name: REDIRECT_NAME,
      component: () => import('/@/views/sys/redirect/index.vue'),
      meta: {
        title: REDIRECT_NAME,
        hideBreadcrumb: true,
      },
    },
  ],
}

/**
 * 登陆后的首页
 */
export const DASHBOARD: AppRouteRecordRaw = {
  title: 'dashboard',
  path: '/dashboard',
  name: 'Dashboard',
  component: LAYOUT,
  redirect: '/dashboard/analysis',
  meta: {
    orderNo: 10,
    icon: 'ion:grid-outline',
    title: 'Dashboard',
  },
  children: [
    {
      title: '分析页',
      path: 'analysis',
      name: 'Analysis',
      component: () => import('/@/views/dashboard/analysis/index.vue'),
      meta: {
        title: '分析页',
      },
    },
    {
      title: '工作台',
      path: 'workbench',
      name: 'Workbench',
      component: () => import('/@/views/dashboard/workbench/index.vue'),
      meta: {
        title: '工作台',
      },
    },
  ],
}
