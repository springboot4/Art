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
