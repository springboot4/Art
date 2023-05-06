import { defHttp } from '/@/utils/http/axios'
import { PageResult } from '/#/axios'
import { TenantDTO } from './types'

/**
 * 获取单条
 */
export function getTenantIdByName(name) {
  return defHttp.get<string>({
    url: '/system/tenant/findIdByName',
    params: { name },
  })
}

/**
 * 分页
 */
export function page(params) {
  return defHttp.get<PageResult<TenantDTO>>({
    url: '/system/tenant/page',
    params,
  })
}

/**
 * 获取单条
 */
export function get(id) {
  return defHttp.get<TenantDTO>({
    url: '/system/tenant/findById',
    params: { id },
  })
}

/**
 * 修改
 */
export function update(data: TenantDTO) {
  return defHttp.post<TenantDTO>({
    url: '/system/tenant/update',
    data,
  })
}

/**
 * 保存
 */
export function add(data: TenantDTO) {
  return defHttp.post<TenantDTO>({
    url: '/system/tenant/add',
    data,
  })
}

/**
 * 删除
 */
export function del(id) {
  return defHttp.delete<TenantDTO>({
    url: `/system/tenant/delete?id=${id}`,
  })
}
