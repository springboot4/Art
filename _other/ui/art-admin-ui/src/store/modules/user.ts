import type { UserInfo } from '/#/store'
import type { ErrorMessageMode } from '/#/axios'
import { defineStore } from 'pinia'
import { store } from '/@/store'
import { PageEnum } from '/@/enums/pageEnum'
import {
  REFRESH_TOKEN_KEY,
  ROLES_KEY,
  TENANT_KEY,
  TOKEN_KEY,
  USER_INFO_KEY,
} from '/@/enums/cacheEnum'
import { getAuthCache, setAuthCache } from '/@/utils/auth'
import { LoginParams } from '/@/api/base/model/userModel'
import { doLogout, getUserInfo, loginApi } from '/@/api/base/user'
import { useI18n } from '/@/hooks/web/useI18n'
import { useMessage } from '/@/hooks/web/useMessage'
import { router } from '/@/router'
import { usePermissionStore } from '/@/store/modules/permission'
import { RouteRecordRaw } from 'vue-router'
import { PAGE_NOT_FOUND_ROUTE } from '/@/router/routes/basic'
import { isArray } from '/@/utils/is'
import { h } from 'vue'
import { urlToBase64 } from '/@/utils/file/base64Conver'
import { getAppEnvConfig } from '/@/utils/env'
const { VITE_GLOB_API_URL } = getAppEnvConfig()

interface UserState {
  userInfo: Nullable<UserInfo>
  token?: string
  refreshToken?: string
  roleList: string[]
  sessionTimeout?: boolean
  lastUpdateTime: number
  tenantId?: string
}

export const useUserStore = defineStore({
  id: 'app-user',
  state: (): UserState => ({
    // user info
    userInfo: null,
    // token
    token: undefined,
    // roleList
    roleList: [],
    // Whether the login expired
    sessionTimeout: false,
    // Last fetch time
    lastUpdateTime: 0,
    tenantId: '',
  }),
  getters: {
    getTenant(): string {
      return this.tenantId || getAuthCache<string>(TENANT_KEY) || ''
    },
    getUserInfo(): UserInfo {
      return this.userInfo || getAuthCache<UserInfo>(USER_INFO_KEY) || {}
    },
    getToken(): string {
      return this.token || getAuthCache<string>(TOKEN_KEY)
    },
    getRoleList(): string[] {
      return this.roleList.length > 0 ? this.roleList : getAuthCache<string[]>(ROLES_KEY)
    },
    getSessionTimeout(): boolean {
      return !!this.sessionTimeout
    },
    getLastUpdateTime(): number {
      return this.lastUpdateTime
    },
  },
  actions: {
    setTenant(info: string | undefined) {
      this.tenantId = info ? info : ''
      setAuthCache(TENANT_KEY, info)
    },
    setToken(info: string | undefined) {
      this.token = info ? info : ''
      setAuthCache(TOKEN_KEY, info)
    },
    setRefreshToken(info: string | undefined) {
      this.refreshToken = info ? info : ''
      setAuthCache(REFRESH_TOKEN_KEY, info)
    },
    setRoleList(roleList: string[]) {
      this.roleList = roleList
      setAuthCache(ROLES_KEY, roleList)
    },
    setUserInfo(info: UserInfo | null) {
      this.userInfo = info
      this.lastUpdateTime = new Date().getTime()
      setAuthCache(USER_INFO_KEY, info)
    },
    setSessionTimeout(flag: boolean) {
      this.sessionTimeout = flag
    },
    resetState() {
      this.userInfo = null
      this.token = ''
      this.roleList = []
      this.sessionTimeout = false
    },
    /**
     * @description: login
     */
    async login(
      params: LoginParams & {
        goHome?: boolean
        mode?: ErrorMessageMode
      },
    ): Promise<UserInfo | null> {
      try {
        const { goHome = true, mode, ...loginParams } = params

        // 获取token
        const data = await loginApi(loginParams, mode)

        const { access_token, refresh_token } = data

        // 保存token信息
        this.setToken(access_token)
        this.setRefreshToken(refresh_token)
        return this.afterLoginAction(goHome)
      } catch (error) {
        return Promise.reject(error)
      }
    },
    async afterLoginAction(goHome?: boolean): Promise<UserInfo | null> {
      if (!this.getToken) return null
      // 获取用户信息
      const userInfo = await this.getUserInfoAction()
      const sessionTimeout = this.sessionTimeout
      if (sessionTimeout) {
        this.setSessionTimeout(false)
      } else {
        const permissionStore = usePermissionStore()
        if (!permissionStore.isDynamicAddedRoute) {
          const routes = await permissionStore.buildRoutesAction()
          routes.forEach((route) => {
            router.addRoute(route as unknown as RouteRecordRaw)
          })
          router.addRoute(PAGE_NOT_FOUND_ROUTE as unknown as RouteRecordRaw)
          permissionStore.setDynamicAddedRoute(true)
        }
        goHome && (await router.replace(userInfo?.homePath || PageEnum.BASE_HOME))
      }

      return userInfo
    },
    async getUserInfoAction(): Promise<UserInfo | null> {
      if (!this.getToken) return null
      const userInfo = await getUserInfo()
      // 保存权限信息
      const { permissions = [] } = userInfo
      if (isArray(permissions)) {
        this.setRoleList(permissions)
      } else {
        userInfo.permissions = []
        this.setRoleList([])
      }

      //保存用户信息
      const sysUser = userInfo.sysUser

      const user: UserInfo = {
        userId: sysUser.userId,
        username: sysUser.username,
        realName: sysUser.username,
        description: sysUser.description,
        roles: permissions,
        sex: sysUser.sex,
        email: sysUser.email,
        mobile: sysUser.mobile,
      }

      urlToBase64(`${VITE_GLOB_API_URL}/${sysUser.avatar}`).then((base64) => {
        user.avatar = base64 || ''
      })

      this.setUserInfo(user)
      return user
    },
    /**
     * @description: logout
     */
    async logout(goLogin = false) {
      if (this.getToken) {
        try {
          await doLogout()
        } catch {
          console.log('注销Token失败')
        }
      }
      this.setToken(undefined)
      this.setSessionTimeout(false)
      this.setUserInfo(null)
      goLogin && router.push(PageEnum.BASE_LOGIN)
    },

    /**
     * @description: Confirm before logging out
     */
    confirmLoginOut() {
      const { createConfirm } = useMessage()
      const { t } = useI18n()
      createConfirm({
        iconType: 'warning',
        title: () => h('span', t('sys.app.logoutTip')),
        content: () => h('span', t('sys.app.logoutMessage')),
        onOk: async () => {
          await this.logout(true)
        },
      })
    },
  },
})

// Need to be used outside the setup
export function useUserStoreWithOut() {
  return useUserStore(store)
}
