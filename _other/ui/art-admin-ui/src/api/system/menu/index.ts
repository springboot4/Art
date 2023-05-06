import { defHttp } from '/@/utils/http/axios'
import { MenuDTO } from './types'

/**
 * 分页
 */
export function page() {
  return defHttp.get({
    url: '/system/menu/getUserMenuTree',
  })
}

/**
 * 获取属性下拉框
 */
export function getTreeSelect() {
  return defHttp.get({
    url: '/system/menu/getTreeSelect',
  })
}

/**
 * 获取单条
 */
export function get(id) {
  return defHttp.get<MenuDTO>({
    url: `/system/menu/getMenuById/${id}`,
  })
}

/**
 * 修改
 */
export function update(data: MenuDTO) {
  return defHttp.post<MenuDTO>({
    url: '/system/menu/update',
    data,
  })
}

/**
 * 保存
 */
export function add(data: MenuDTO) {
  return defHttp.post<MenuDTO>({
    url: '/system/menu/save',
    data,
  })
}

/**
 * 删除
 */
export function del(id) {
  return defHttp.delete<MenuDTO>({
    url: `/system/menu/delete/${id}`,
  })
}
