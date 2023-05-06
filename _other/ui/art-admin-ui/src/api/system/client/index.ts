import { defHttp } from '/@/utils/http/axios'
import { PageResult } from '/#/axios'
import { ClientDetailsDTO } from './types'

/**
 * 分页
 */
export function page(params) {
  return defHttp.get<PageResult<ClientDetailsDTO>>({
    url: '/system/client/page',
    params,
  })
}

/**
 * 获取单条
 */
export function get(id) {
  return defHttp.get<ClientDetailsDTO>({
    url: '/system/client/findById',
    params: { id },
  })
}

/**
 * 修改
 */
export function update(data: ClientDetailsDTO) {
  return defHttp.post<ClientDetailsDTO>({
    url: '/system/client/update',
    data,
  })
}

/**
 * 保存
 */
export function add(data: ClientDetailsDTO) {
  return defHttp.post<ClientDetailsDTO>({
    url: '/system/client/add',
    data,
  })
}

/**
 * 删除
 */
export function del(id) {
  return defHttp.delete<ClientDetailsDTO>({
    url: `/system/client/delete?id=${id}`,
  })
}
