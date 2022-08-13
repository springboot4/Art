import request from '../../utils/request'

/**
 * 分类列表
 */
export function listSpuWithPage(params) {
    return request({
        url: '/search/app/es/goods/page',
        params,
        method: 'GET',
        headers: {
            'auth': false
        }
    })
}

/**
 * 获取商品详情
 *
 * @param {Object} spuId
 */
export function getSpuDetail(spuId) {
    return request({
        url: '/search/app/es/goods/' + spuId,
        method: 'get',
        headers: {
            'auth': false
        }
    })
}
