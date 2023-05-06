import { defHttp } from '/@/utils/http/axios'
import { PageResult } from '/#/axios'
import { OperLogDTO } from './types'

/**
 * 分页
 */
export function page(params) {
  return defHttp.get<PageResult<OperLogDTO>>({
    url: '/system/operLog/page',
    params,
  })
}

/**
 * 获取单条
 */
export function get(id) {
  return defHttp.get<OperLogDTO>({
    url: '/system/operLog/findById',
    params: { id },
  })
}

/**
 * 修改
 */
export function update(data: OperLogDTO) {
  return defHttp.post<OperLogDTO>({
    url: '/system/operLog/update',
    data,
  })
}

/**
 * 保存
 */
export function add(data: OperLogDTO) {
  return defHttp.post<OperLogDTO>({
    url: '/system/operLog/add',
    data,
  })
}

/**
 * 删除
 */
export function del(id) {
  return defHttp.delete<OperLogDTO>({
    url: `/system/operLog/delete?id=${id}`,
  })
}
