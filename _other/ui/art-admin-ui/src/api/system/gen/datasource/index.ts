import { defHttp } from '/@/utils/http/axios'
import { PageResult } from '/#/axios'
import { GenDatasourceConfDTO } from './types'

/**
 * 分页
 */
export function listDs() {
  return defHttp.get<GenDatasourceConfDTO[]>({
    url: '/generate/datasourceConf/listDs',
  })
}

/**
 * 分页
 */
export function page(params) {
  return defHttp.get<PageResult<GenDatasourceConfDTO>>({
    url: '/generate/datasourceConf/page',
    params,
  })
}

/**
 * 获取单条
 */
export function get(id) {
  return defHttp.get<GenDatasourceConfDTO>({
    url: '/generate/datasourceConf/findById',
    params: { id },
  })
}

/**
 * 修改
 */
export function update(data: GenDatasourceConfDTO) {
  return defHttp.post<GenDatasourceConfDTO>({
    url: '/generate/datasourceConf/update',
    data,
  })
}

/**
 * 保存
 */
export function add(data: GenDatasourceConfDTO) {
  return defHttp.post<GenDatasourceConfDTO>({
    url: '/generate/datasourceConf/addDs',
    data,
  })
}

/**
 * 删除
 */
export function del(id) {
  return defHttp.delete<GenDatasourceConfDTO>({
    url: `/generate/datasourceConf/delete?id=${id}`,
  })
}
