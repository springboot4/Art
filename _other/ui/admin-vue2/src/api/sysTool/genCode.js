import { axios } from '@/utils/request'

export function PageDataTable (param) {
  return axios({
    url: '/generate/gen/table/page',
    method: 'GET',
    params: param
  })
}

export function editRole (data) {
  return axios({
    url: '/generate/role/editRole',
    method: 'PUT',
    data: data
  })
}

export function codeGenPreview (queryInfo) {
  return axios({
    url: '/generate/gen/code/codeGenPreview',
    method: 'GET',
    params: { tableName: queryInfo.tableName, dsName: queryInfo.dsName }
  })
}

/**
 * 下载
 */
export function genCodeZip (queryInfo) {
  return axios({
    url: '/generate/gen/code/genCodeZip',
    method: 'GET',
    responseType: 'blob',
    params: { tableName: queryInfo.tableName, dsName: queryInfo.dsName }
  })
}
