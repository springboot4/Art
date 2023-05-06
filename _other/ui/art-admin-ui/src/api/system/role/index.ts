import { defHttp } from '/@/utils/http/axios'
import { RoleDTO } from '/@/api/system/role/types'
import { PageResult } from '/#/axios'

/**
 * 查询所有角色
 */
export function getAllRole() {
  return defHttp.get({
    url: '/system/role/getAllRole',
  })
}

/**
 * 分页
 */
export function page(params) {
  return defHttp.get<PageResult<RoleDTO>>({
    url: '/system/role/page',
    params,
  })
}

/**
 * 获取单条
 */
export function get(id) {
  return defHttp.get<RoleDTO>({
    url: `/system/role/getRoleById/${id}`,
  })
}

/**
 * 修改
 */
export function update(data: RoleDTO) {
  return defHttp.put<RoleDTO>({
    url: '/system/role/editRole',
    data,
  })
}

/**
 * 保存
 */
export function add(data: RoleDTO) {
  return defHttp.post<RoleDTO>({
    url: '/system/role/addRole',
    data,
  })
}

/**
 * 删除
 */
export function del(id) {
  return defHttp.delete<RoleDTO>({
    url: `/system/role/deleteRoleById/${id}`,
  })
}
