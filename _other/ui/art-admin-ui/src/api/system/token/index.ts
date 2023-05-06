import { defHttp } from '/@/utils/http/axios'
import { PageResult } from '/#/axios'

/**
 * 分页
 */
export const page = (params) => {
  return defHttp.get<PageResult>({
    url: '/system/token/page',
    params,
  })
}

/**
 * 删除
 */
export const del = (id) => {
  return defHttp.delete({
    url: `/system/token/${id}`,
  })
}
