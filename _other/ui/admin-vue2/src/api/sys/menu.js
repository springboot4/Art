import { axios } from '@/utils/request'

export function getAllMenuTree () {
  return axios({
    url: '/system/menu/getAllMenuTree',
    method: 'GET'
  })
}

export function getObj (id) {
  return axios({
    url: '/system/menu/getMenuById/' + id,
    method: 'GET'
  })
}

export function addObj (obj) {
  return axios({
    url: '/system/menu/save',
    method: 'POST',
    data: obj
  })
}

export function putObj (obj) {
  return axios({
    url: '/system/menu/update',
    method: 'POST',
    data: obj
  })
}

export function delObj (id) {
  return axios({
    url: '/system/menu/delete/' + id,
    method: 'DELETE'
  })
}
