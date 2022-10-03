import { axios } from '@/utils/request'

/**
 * 获取当前用户角色下的所有菜单信息
 */
export function getUserMenuTree () {
  return axios({
    url: '/system/menu/getUserMenuTree',
    method: 'GET'
  })
}

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
