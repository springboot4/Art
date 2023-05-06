import { defHttp } from '/@/utils/http/axios'
import { PageResult } from '/#/axios'
import { DictItemDTO } from './itemTypes'

/**
 * 分页
 */
export function page(params) {
  return defHttp.get<PageResult<DictItemDTO>>({
    url: '/system/dictItem/page',
    params,
  })
}

/**
 * 获取单条
 */
export function get(id) {
  return defHttp.get<DictItemDTO>({
    url: '/system/dictItem/findById',
    params: { id },
  })
}

/**
 * 修改
 */
export function update(data: DictItemDTO) {
  return defHttp.post<DictItemDTO>({
    url: '/system/dictItem/update',
    data,
  })
}

/**
 * 保存
 */
export function add(data: DictItemDTO) {
  return defHttp.post<DictItemDTO>({
    url: '/system/dictItem/add',
    data,
  })
}

/**
 * 删除
 */
export function del(id) {
  return defHttp.delete<DictItemDTO>({
    url: `/system/dictItem/delete?id=${id}`,
  })
}
