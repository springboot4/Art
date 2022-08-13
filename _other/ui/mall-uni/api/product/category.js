import request  from '../../utils/request'

/**
 * 分类列表
 */
export function list (parentId) {
  return request({
    url: '/product/app/category/list',
    params: { parentId },
    method: 'GET'
  })
}

