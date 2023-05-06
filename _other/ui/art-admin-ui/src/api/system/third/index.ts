import { UserThirdBindInfo } from './types'
import { defHttp } from '/@/utils/http/axios'

/**
 * 绑定信息获取
 */
export function findBindInfo() {
  return defHttp.get<UserThirdBindInfo>({
    url: '/system/third/bindInfo',
  })
}

/**
 * 解除绑定
 */
export function unBind(type) {
  return defHttp.put({
    url: '/system/third/unBind',
    params: { type },
  })
}
