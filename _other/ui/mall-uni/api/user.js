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

/**
 * 用户相关api
 */
import request from "../utils/request";

// #ifdef MP
export function login(parameter) {
    return request({
        url: '/auth/oauth/token',
        method: 'post',
        headers: {
            // wechat:123456
            'Authorization': 'Basic d2VjaGF0OjEyMzQ1Ng=='
        },
        params: {grant_type:'wechat', code: parameter.code, encryptedData: parameter.encryptedData,iv:parameter.iv,}
    })
}
// #endif

// #ifndef MP
export function login(parameter) {
    return request({
        url: '/auth/oauth/token',
        method: 'post',
        headers: {
            // mall-app:123456
            'Authorization': 'Basic bWFsbC1hcHA6MTIzNDU2'
        },
        params: {mobile: parameter.mobile, code: parameter.code, grant_type: 'sms_code'}
    })
}
// #endif


export function getUserInfo () {
    return request({
        url: '/user/member/auth/loadUserByUserId',
        method: 'get',
        headers: {
            'auth': true
        }
    })
}

export function logout() {
    return request({
        url: '/auth/myLogout',
        method: 'delete',
        headers: {
            'auth': true // 需要认证，通过
        }
    })
}

