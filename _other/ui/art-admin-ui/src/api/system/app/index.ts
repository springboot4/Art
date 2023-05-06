import { defHttp } from '/@/utils/http/axios'
import { PageResult } from '/#/axios'
import { AppDTO } from './types'

/**
 * 分页
 */
export const page = (params) => {
  return defHttp.get<PageResult<AppDTO>>({
    url: '/system/app/page',
    params,
  })
}

/**
 * 获取单条
 */
export const get = (id) => {
  return defHttp.get<AppDTO>({
    url: '/system/app/findById',
    params: { id },
  })
}

/**
 * 修改
 */
export const update = (data: AppDTO) => {
  return defHttp.post<AppDTO>({
    url: '/system/app/update',
    data,
  })
}

/**
 * 保存
 */
export const add = (data: AppDTO) => {
  return defHttp.post<AppDTO>({
    url: '/system/app/add',
    data,
  })
}

/**
 * 删除
 */
export const del = (id) => {
  return defHttp.delete<AppDTO>({
    url: `/system/app/delete?id=${id}`,
  })
}

/**
 * 查询所有应用信息
 */
export const findAllApp = () => {
  return defHttp.get<AppDTO[]>({
    url: '/system/app/findAll',
  })
}
