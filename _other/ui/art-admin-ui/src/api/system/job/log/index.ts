import { defHttp } from '/@/utils/http/axios'
import { PageResult } from '/#/axios'
import { JobLogDTO } from './types'

/**
 * 分页
 */
export function page(params) {
  return defHttp.get<PageResult<JobLogDTO>>({
    url: '/schedule/jobLog/page',
    params,
  })
}

/**
 * 获取单条
 */
export function get(id) {
  return defHttp.get<JobLogDTO>({
    url: '/schedule/jobLog/findById',
    params: { id },
  })
}

/**
 * 修改
 */
export function update(data: JobLogDTO) {
  return defHttp.post<JobLogDTO>({
    url: '/schedule/jobLog/update',
    data,
  })
}

/**
 * 保存
 */
export function add(data: JobLogDTO) {
  return defHttp.post<JobLogDTO>({
    url: '/schedule/jobLog/add',
    data,
  })
}

/**
 * 删除
 */
export function del(id) {
  return defHttp.delete<JobLogDTO>({
    url: `/schedule/jobLog/delete?id=${id}`,
  })
}
