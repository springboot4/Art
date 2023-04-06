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

import Vue from 'vue'
import axios from 'axios'
import store from '@/store'
import notification from 'ant-design-vue/es/notification'
import { VueAxios } from './axios'
import { ACCESS_TOKEN, TENANT_ID } from '@/store/mutation-types'

// 创建 axios 实例
const request = axios.create({
  // API 请求的默认前缀
  baseURL: process.env.VUE_APP_API_BASE_URL,
  // baseURL: 'http://127.0.0.1:9999',
  timeout: 60000 // 请求超时时间
})

// 异常拦截处理器
const errorHandler = (error) => {
  if (error.response) {
    const data = error.response.data
    if (error.response.status === 403) {
      notification.error({
        message: '禁止访问',
        description: data.message
      })
    } else if (error.response.status === 401 && !(data.result && data.result.isLogin)) {
      notification.error({
        message: '未经授权',
        description: '授权验证失败'
      })
      if (Vue.ls.get(ACCESS_TOKEN)) {
        store.dispatch('Logout').then(() => {
          setTimeout(() => {
            window.location.reload()
          }, 1500)
        })
      }
    } else {
      notification.error({
        message: '操作失败！',
        description: data.msg
      })
    }
  }
  return Promise.reject(error.response.data)
}

// request interceptor
request.interceptors.request.use(config => {
  const token = Vue.ls.get(ACCESS_TOKEN)
  // 如果 token 存在让每个请求携带自定义 token
  if (token) {
    config.headers['Authorization'] = 'Bearer ' + token
  }

  const tenantId = Vue.ls.get(TENANT_ID)
  if (tenantId) {
    // 设置租户信息
    config.headers['TENANT-ID'] = tenantId
  }

  return config
}, errorHandler)

// response interceptor
request.interceptors.response.use((response) => {
  if (response.data.code && response.data.code === 'B0001') {
    return Promise.reject(response.data)
  }
  return response.data
}, errorHandler)

const installer = {
  vm: {},
  install (Vue) {
    Vue.use(VueAxios, request)
  }
}

export default request

export {
  installer as VueAxios,
  request as axios
}
