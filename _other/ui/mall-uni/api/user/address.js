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

import request from "../../utils/request";

export function list() {
	return request({
		url: '/user/app/address/findAll',
		method: 'get',
		headers: {
			'auth': true // 需要认证
		}
	})
}


export function add(data) {
	return request({
		url: '/user/app/address/add',
		method: 'post',
		data: data,
		headers: {
			'auth': true // 需要认证
		}
	})
}


export function update(data) {
	return request({
		url: '/user/app/address/update',
		method: 'post',
		data: data,
		headers: {
			'auth': true // 需要认证
		}
	})
}
