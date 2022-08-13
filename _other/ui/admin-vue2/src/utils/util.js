import { axios } from '@/utils/request'
import { validatenull } from '@/utils/validate'

export function treeDataTranslate (data, value = 'value', title = 'title', children = 'children') {
  const temp = []
  if (data.constructor !== Array) {
    const arr = []
    arr.push(data)
    data = arr
  }
  for (let i = 0; i < data.length; i++) {
    const p = {
      key: data[i][value],
      title: data[i][title],
      value: String(data[i][value])
    }
    if (data[i][children] && data[i][children].length > 0) {
      p.children = treeDataTranslate(data[i][children], value, title, children)
    }
    temp.push(p)
  }

  return temp
}

export function timeFix () {
  const time = new Date()
  const hour = time.getHours()
  return hour < 9 ? '早上好' : hour <= 11 ? '上午好' : hour <= 13 ? '中午好' : hour < 20 ? '下午好' : '晚上好'
}

export function welcome () {
  const arr = ['休息一会儿吧', '准备吃什么呢?', '要不要打一把 DOTA', '我猜你可能累了']
  const index = Math.floor(Math.random() * arr.length)
  return arr[index]
}

/**
 * 触发 window.resize
 */
export function triggerWindowResizeEvent () {
  const event = document.createEvent('HTMLEvents')
  event.initEvent('resize', true, true)
  event.eventType = 'message'
  window.dispatchEvent(event)
}

export function handleScrollHeader (callback) {
  let timer = 0

  let beforeScrollTop = window.pageYOffset
  callback = callback || function () {
  }
  window.addEventListener(
    'scroll',
    event => {
      clearTimeout(timer)
      timer = setTimeout(() => {
        let direction = 'up'
        const afterScrollTop = window.pageYOffset
        const delta = afterScrollTop - beforeScrollTop
        if (delta === 0) {
          return false
        }
        direction = delta > 0 ? 'down' : 'up'
        callback(direction)
        beforeScrollTop = afterScrollTop
      }, 50)
    },
    false
  )
}

export function isIE () {
  const bw = window.navigator.userAgent
  const compare = (s) => bw.indexOf(s) >= 0
  const ie11 = (() => 'ActiveXObject' in window)()
  return compare('MSIE') || ie11
}

/**
 * Remove loading animate
 * @param id parent element id or class
 * @param timeout
 */
export function removeLoadingAnimate (id = '', timeout = 1500) {
  if (id === '') {
    return
  }
  setTimeout(() => {
    document.body.removeChild(document.getElementById(id))
  }, timeout)
}

/**
 *
 * @param url 目标下载接口
 * @param query 查询参数
 * @param fileName 文件名称
 * @returns {*}
 */
export function downBlobFile (url, query, fileName) {
  return axios({
    url: url,
    method: 'get',
    responseType: 'blob',
    params: query
  }).then(response => {
    // 处理返回的文件流
    const blob = response
    if (blob && blob.size === 0) {
      this.$message.error('内容为空，无法下载')
      return
    }
    const link = document.createElement('a')
    link.href = window.URL.createObjectURL(blob)
    link.download = fileName
    document.body.appendChild(link)
    link.click()
    window.setTimeout(function () {
      window.URL.revokeObjectURL(blob)
      document.body.removeChild(link)
    }, 0)
  })
}

/**
 *  <img> <a> src 处理
 * @returns {PromiseLike<T | never> | Promise<T | never>}
 */
export function handleImg (url, id) {
  return validatenull(url)
    ? null
    : axios({
      url: url,
      method: 'get',
      responseType: 'blob'
    }).then(response => {
      // 处理返回的文件流
      const blob = response
      const src = URL.createObjectURL(blob)

      if (validatenull(id)) {
        return src
      }
      const img = document.getElementById(id)
      img.src = src
      window.setTimeout(function () {
        window.URL.revokeObjectURL(blob)
      }, 0)
    })
}
