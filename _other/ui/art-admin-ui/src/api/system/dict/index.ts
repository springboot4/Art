import { defHttp } from '/@/utils/http/axios'
import { PageResult } from '/#/axios'
import { DictDTO, DictItemDto } from '/@/api/system/dict/types'

/**
 * 根据字典类型获取字典下的所有字典项
 */
export function getDictItemsByType(type) {
  return defHttp.get<DictItemDto[]>({
    url: 'system/dict/getDictItemsByType',
    params: {
      type,
    },
  })
}

/**
 * 分页
 */
export function page(params) {
  return defHttp.get<PageResult<DictDTO>>({
    url: '/system/dict/page',
    params,
  })
}

/**
 * 获取单条
 */
export function get(id) {
  return defHttp.get<DictDTO>({
    url: '/system/dict/findById',
    params: { id },
  })
}

/**
 * 修改
 */
export function update(data: DictDTO) {
  return defHttp.post<DictDTO>({
    url: '/system/dict/update',
    data,
  })
}

/**
 * 保存
 */
export function add(data: DictDTO) {
  return defHttp.post<DictDTO>({
    url: '/system/dict/add',
    data,
  })
}

/**
 * 删除
 */
export function del(id) {
  return defHttp.delete<DictDTO>({
    url: `/system/dict/delete?id=${id}`,
  })
}
