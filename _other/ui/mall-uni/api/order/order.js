import request from '../../utils/request'

// 订单确认
export function confirm(skuId) {
    return request({
        url: '/order/app/orders/confirm',
        method: 'post',
        params: {skuId},
        headers: {
            'auth': true // 需要认证
        }
    })
}

// 订单提交
export function submit(data) {
    return request({
        url: '/order/app/orders/submit',
        method: 'post',
        data: data,
        headers: {
            'auth': true // 需要认证
        }
    })
}

// 订单支付
export function pay(orderId, payType) {
    return request({
        url: '/order/app/orders/' + orderId + '/pay',
        method: 'post',
        params: {payType: payType},
        headers: {
            'auth': true // 需要认证
        }
    })
}

// 取消订单
export function cancelOrder(orderId) {
    return request({
        url: '/order/app/orders/cancel',
        method: 'put',
        params: {orderId},
        headers: {
            'auth': true // 需要认证
        }
    })
}

// 删除订单
export function deleteOrder(orderId) {
    return request({
        url: '/order/app/orders/' + orderId,
        method: 'delete',
        headers: {
            'auth': true // 需要认证
        }
    })
}

// 订单列表
export function listOrdersWithPage(params) {
    return request({
        url: '/order/app/orders',
        method: 'get',
        params: params,
        headers: {
            'auth': true // 需要认证
        }
    })
}
