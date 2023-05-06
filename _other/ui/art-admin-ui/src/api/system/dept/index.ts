import { defHttp } from '/@/utils/http/axios'
import { DeptDTO } from './types'

/**
 * 查询部门树
 */
export function getDeptTree() {
  return defHttp.get({
    url: '/system/dept/getDeptTree',
  })
}

/**
 * 分页
 */
export function page() {
  return defHttp.get<DeptDTO[]>({
    url: '/system/dept/getDeptTree',
  })
}

/**
 * 获取单条
 */
export function get(id) {
  return defHttp.get<DeptDTO>({
    url: `/system/dept/getDeptById/${id}`,
  })
}

/**
 * 修改
 */
export function update(data: DeptDTO) {
  return defHttp.put<DeptDTO>({
    url: '/system/dept/updateDept',
    data,
  })
}

/**
 * 保存
 */
export function add(data: DeptDTO) {
  return defHttp.post<DeptDTO>({
    url: '/system/dept/addDept',
    data,
  })
}

/**
 * 删除
 */
export function del(id) {
  return defHttp.delete<DeptDTO>({
    url: `/system/dept/deleteById/${id}`,
  })
}
