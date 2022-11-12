/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
