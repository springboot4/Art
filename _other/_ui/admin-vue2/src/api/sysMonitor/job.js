import { axios } from '@/utils/request'

/**
 * 分页
 */
export function page (params) {
  return axios({
    url: '/schedule/job/page',
    method: 'GET',
    params: params
  })
}

/**
 * 获取单条
 */
export function get (id) {
  return axios({
    url: '/schedule/job/findById',
    method: 'GET',
    params: { id }
  })
}

/**
 * 添加
 */
export function add (obj) {
  return axios({
    url: '/schedule/job/add',
    method: 'POST',
    data: obj
  })
}

/**
 * 更新
 */
export function update (obj) {
  return axios({
    url: '/schedule/job/update',
    method: 'POST',
    data: obj
  })
}

/**
 * 删除
 */
export function del (id) {
  return axios({
    url: '/schedule/job/delete',
    params: { id },
    method: 'DELETE'
  })
}

/**
 * 状态改变
 */
export function changeStatus (obj) {
  return axios({
    url: '/schedule/job/changeStatus',
    method: 'PUT',
    data: obj
  })
}

// 定时任务立即执行一次
export function runJob (data) {
  return axios({
    url: '/schedule/job/run',
    method: 'put',
    data: data
  })
}
