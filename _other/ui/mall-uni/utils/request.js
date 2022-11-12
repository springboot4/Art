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

import axios from 'axios'
import store from '@/store'
axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'

// create an axios instance
const service = axios.create({
	//baseURL: "https://www.youlai.tech/prod-api", //  微信线上接口地址
	//baseURL: "http://www.youlai.tech:81/prod-api", //  H5/Android/iOS 线上地址
	baseURL: "http://localhost:8301", // 本地开发环境地址
	withCredentials: true, // send cookies when cross-domain requests
	timeout: 10000 // request timeout
})

// request interceptor
service.interceptors.request.use(
	config => {
		// do something before request is sent
		if (config.headers.auth === true) { // 判断请求是否需要认证
			const token = uni.getStorageSync('token')
			if (token) {
				config.headers['Authorization'] = token;
			}
		}
		config.headers['TENANT-ID'] = '0'
		return config
	},
	error => {
		// do something with request error
		console.log(error) // for debug
		return Promise.reject(error)
	}
)

service.defaults.adapter = function(config) {
	return new Promise((resolve, reject) => {
		var settle = require('axios/lib/core/settle');
		var buildURL = require('axios/lib/helpers/buildURL');
		uni.request({
			method: config.method.toUpperCase(),
			url: config.baseURL + buildURL(config.url, config.params, config.paramsSerializer),
			header: config.headers,
			data: config.data,
			dataType: config.dataType,
			responseType: config.responseType,
			sslVerify: config.sslVerify,
			complete: function complete(response) {
				response = {
					data: response.data,
					status: response.statusCode,
					errMsg: response.errMsg,
					header: response.header,
					config: config
				};

				settle(resolve, reject, response);
			}
		})
	})
}


// response interceptor
service.interceptors.response.use(({
		config,
		data
	}) => {
		return data
	},
	error => {
		const {msg} = error.response.data
		const status = error.response.status

		if (status == '401') { // token过期
			uni.showToast({
				title: '会话已过期，请重新登录',
				success() {
					uni.navigateTo({
						url: `/pages/public/login`,
					});
				}
			})
		} else {
			uni.showToast({
				title: msg,
				icon: 'none'
			});
			return Promise.reject(new Error(msg || 'Errors'))
		}
	}
)

export default service
