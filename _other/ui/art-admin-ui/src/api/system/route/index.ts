import { defHttp } from '/@/utils/http/axios'
import { RouteConfDTO } from './types'

/**
 * 分页
 */
export function findAll() {
  return defHttp.get<RouteConfDTO[]>({
    url: '/system/routeConf/findAll',
  })
}

/**
 * 修改
 */
export function update(data) {
  return defHttp.put({
    url: '/system/routeConf/update',
    data,
  })
}
