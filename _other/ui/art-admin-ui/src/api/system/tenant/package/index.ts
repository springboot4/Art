import { defHttp } from '/@/utils/http/axios'
import { PageResult } from '/#/axios'
import { TenantPackageDTO } from './types'

/**
 * 查询所有的租户套餐
 */
export function findAllPackages() {
  return defHttp.get<TenantPackageDTO[]>({
    url: '/system/tenant/package/findAll',
  })
}

/**
 * 分页
 */
export function page(params) {
  return defHttp.get<PageResult<TenantPackageDTO>>({
    url: '/system/tenant/package/page',
    params,
  })
}

/**
 * 获取单条
 */
export function get(id) {
  return defHttp.get<TenantPackageDTO>({
    url: '/system/tenant/package/findById',
    params: { id },
  })
}

/**
 * 修改
 */
export function update(data: TenantPackageDTO) {
  return defHttp.post<TenantPackageDTO>({
    url: '/system/tenant/package/update',
    data,
  })
}

/**
 * 保存
 */
export function add(data: TenantPackageDTO) {
  return defHttp.post<TenantPackageDTO>({
    url: '/system/tenant/package/add',
    data,
  })
}

/**
 * 删除
 */
export function del(id) {
  return defHttp.delete<TenantPackageDTO>({
    url: `/system/tenant/package/delete?id=${id}`,
  })
}
