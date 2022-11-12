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
