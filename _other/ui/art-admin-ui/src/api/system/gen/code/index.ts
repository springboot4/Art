import { defHttp } from '/@/utils/http/axios'
import { PageResult } from '/#/axios'
import { CodeGenPreviewDTO, DatabaseTableDO } from '/@/api/system/gen/code/types'

/**
 * 分页
 */
export function page(params) {
  return defHttp.get<PageResult<DatabaseTableDO>>({
    url: '/generate/gen/table/page',
    params,
  })
}

/**
 * 预览
 */
export function codeGenPreview(params) {
  return defHttp.get<CodeGenPreviewDTO[]>({
    url: '/generate/gen/code/codeGenPreview',
    params: params,
  })
}

/**
 * 下载代码
 */
export function genCodeZip(params) {
  return defHttp.get({
    url: '/generate/gen/code/genCodeZip',
    params: params,
    responseType: 'blob',
  })
}
