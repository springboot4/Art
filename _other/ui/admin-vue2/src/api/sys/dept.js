import { axios } from '@/utils/request'

export function getDeptTree () {
  return axios({
    url: '/system/dept/getDeptTree',
    method: 'GET'
  })
}

export function deleteById (id) {
  return axios({
    url: '/system/dept/deleteById/' + id,
    method: 'DELETE'
  })
}

export function addDept (data) {
  return axios({
    url: '/system/dept/addDept',
    method: 'POST',
    data: data
  })
}

export function getDeptById (id) {
  return axios({
    url: '/system/dept/getDeptById/' + id,
    method: 'GET'
  })
}

export function updateDept (data) {
  return axios({
    url: '/system/dept/updateDept',
    method: 'PUT',
    data: data
  })
}
