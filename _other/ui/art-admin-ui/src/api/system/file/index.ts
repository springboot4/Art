import { defHttp } from '/@/utils/http/axios'
import { PageResult } from '/#/axios'
import { FileDownloadDTO, FileDTO } from './types'

/**
 * 分页
 */
export function page(params) {
  return defHttp.get<PageResult<FileDTO>>({
    url: '/system/file/page',
    params,
  })
}

/**
 * 预览文件url
 */
export function preSignUploadUrl(data: FileDownloadDTO) {
  return defHttp.post({
    url: '/system/file/preSignUploadUrl',
    data,
  })
}

/**
 * 修改
 */
export function update(data: FileDTO) {
  return defHttp.post<FileDTO>({
    url: '/system/file/update',
    data,
  })
}

/**
 * 保存
 */
export function add(data: FileDTO) {
  return defHttp.post<FileDTO>({
    url: '/system/file/add',
    data,
  })
}

/**
 * 删除
 */
export function del(id) {
  return defHttp.delete<FileDTO>({
    url: `/system/file/delete?id=${id}`,
  })
}
