import { axios } from '@/utils/request'

/**
 * cha
 */
export function listDs () {
  return axios({
    url: '/generate/datasourceConf/listDs',
    method: 'GET'
  })
}

/**
 * 分页
 */
export function page (params) {
  return axios({
    url: '/generate/datasourceConf/page',
    method: 'GET',
    params: params
  })
}

/**
 * 获取单条
 */
export function get (id) {
  return axios({
    url: '/generate/datasourceConf/findById',
    method: 'GET',
    params: { id }
  })
}

/**
 * 添加
 */
export function add (obj) {
  return axios({
    url: '/generate/datasourceConf/addDs',
    method: 'POST',
    data: obj
  })
}

/**
 * 更新
 */
export function update (obj) {
  return axios({
    url: '/generate/datasourceConf/update',
    method: 'POST',
    data: obj
  })
}

/**
 * 删除
 */
export function del (id) {
  return axios({
    url: '/generate/datasourceConf/delete',
    params: { id },
    method: 'DELETE'
  })
}
