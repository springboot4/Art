import { axios } from '@/utils/request'

export function getAllRole () {
  return axios({
    url: '/system/role/getAllRole',
    method: 'GET'
  })
}

export function PageRole (param) {
  return axios({
    url: '/system/role/page',
    method: 'GET',
    params: param
  })
}

export function addRole (data) {
  return axios({
    url: '/system/role/addRole',
    method: 'POST',
    data: data
  })
}

export function editRole (data) {
  return axios({
    url: '/system/role/editRole',
    method: 'PUT',
    data: data
  })
}

export function getRoleById (id) {
  return axios({
    url: '/system/role/getRoleById/' + id,
    method: 'GET'
  })
}

export function deleteRoleById (id) {
  return axios({
    url: '/system/role/deleteRoleById/' + id,
    method: 'DELETE'
  })
}
