import { defHttp } from '/@/utils/http/axios'
import { PageResult } from '/#/axios'
import { PostDTO } from './types'

/**
 * 查询所有岗位
 */
export function getAllPost() {
  return defHttp.get({
    url: '/system/post/findAll',
  })
}

/**
 * 分页
 */
export function page(params) {
  return defHttp.get<PageResult<PostDTO>>({
    url: '/system/post/page',
    params,
  })
}

/**
 * 获取单条
 */
export function get(id) {
  return defHttp.get<PostDTO>({
    url: '/system/post/findById',
    params: { id },
  })
}

/**
 * 修改
 */
export function update(data: PostDTO) {
  return defHttp.post<PostDTO>({
    url: '/system/post/update',
    data,
  })
}

/**
 * 保存
 */
export function add(data: PostDTO) {
  return defHttp.post<PostDTO>({
    url: '/system/post/add',
    data,
  })
}

/**
 * 删除
 */
export function del(id) {
  return defHttp.delete<PostDTO>({
    url: `/system/post/delete?id=${id}`,
  })
}
