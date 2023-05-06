import { defHttp } from '/@/utils/http/axios'
import { PageResult } from '/#/axios'
import { UserDTO } from './types'
import { UserInfo } from '/#/store'

/**
 * 分页
 */
export function page(params) {
  return defHttp.get<PageResult<UserDTO>>({
    url: '/system/user',
    params,
  })
}

/**
 * 获取单条
 */
export function get(id) {
  return defHttp.get<UserDTO>({
    url: `/system/user/getUserById/${id}`,
  })
}

/**
 * 修改
 */
export function update(data: UserDTO) {
  return defHttp.put<UserDTO>({
    url: '/system/user',
    data,
  })
}

/**
 * 更新个人信息
 */
export function updateInfoById(data: UserInfo) {
  return defHttp.put({
    url: '/system/user/info',
    data,
  })
}

/**
 * 保存
 */
export function add(data: UserDTO) {
  return defHttp.post<UserDTO>({
    url: '/system/user',
    data,
  })
}

/**
 * 删除
 */
export function del(id) {
  return defHttp.delete<UserDTO>({
    url: `/system/user/delete?id=${id}`,
  })
}
