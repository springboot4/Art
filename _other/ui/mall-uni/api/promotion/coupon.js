import request from '../../utils/request'

/**
 * 查询秒杀活动
 */
export function receiveCoupon(id) {
    return request({
        url: '/promotion/app/coupon/receiveCoupon/'+id,
        method: 'GET',
        headers: {
            'auth': true
        }
    })
}
