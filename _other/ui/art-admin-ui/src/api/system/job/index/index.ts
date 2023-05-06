import { defHttp } from '/@/utils/http/axios'
import { PageResult } from '/#/axios'
import { JobDTO } from './types'

/**
 * 执行一次
 */
export function runJob(data) {
  return defHttp.put({
    url: '/schedule/job/run',
    data,
  })
}

/**
 * 修改job状态
 */
export function changeJobStatus(data) {
  return defHttp.put({
    url: '/schedule/job/changeStatus',
    data,
  })
}

/**
 * 分页
 */
export function page(params) {
  return defHttp.get<PageResult<JobDTO>>({
    url: '/schedule/job/page',
    params,
  })
}

/**
 * 获取单条
 */
export function get(id) {
  return defHttp.get<JobDTO>({
    url: '/schedule/job/findById',
    params: { id },
  })
}

/**
 * 修改
 */
export function update(data: JobDTO) {
  return defHttp.post<JobDTO>({
    url: '/schedule/job/update',
    data,
  })
}

/**
 * 保存
 */
export function add(data: JobDTO) {
  return defHttp.post<JobDTO>({
    url: '/schedule/job/add',
    data,
  })
}

/**
 * 删除
 */
export function del(id) {
  return defHttp.delete<JobDTO>({
    url: `/schedule/job/delete?id=${id}`,
  })
}
