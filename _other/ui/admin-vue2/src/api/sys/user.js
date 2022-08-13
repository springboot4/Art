import { axios } from '@/utils/request'

export function fetchList (params) {
  return axios({
    url: '/system/user',
    method: 'GET',
    params: params
  })
}

export function getById (id) {
  return axios({
    url: '/system/user/getUserById/' + id,
    method: 'GET'
  })
}

export function updateById (data) {
  return axios({
    url: '/system/user',
    method: 'PUT',
    data: data
  })
}

export function addUser (data) {
  return axios({
    url: '/system/user',
    method: 'POST',
    data: data
  })
}
