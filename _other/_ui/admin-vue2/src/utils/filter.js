import Vue from 'vue'
import moment from 'moment'
import 'moment/locale/zh-cn'

moment.locale('zh-cn')

Vue.filter('NumberFormat', function (value) {
  if (!value) {
    return '0'
  }
  const intPartFormat = value.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,') // 将整数部分逢三一断
  return intPartFormat
})

Vue.filter('dayjs', function (dataStr, pattern = 'YYYY-MM-DD HH:mm:ss') {
  return moment(dataStr).format(pattern)
})

Vue.filter('moment', function (dataStr, pattern = 'YYYY-MM-DD HH:mm:ss') {
  return moment(dataStr).format(pattern)
})

Vue.filter('dictFilter', function (data, list = []) {
  try {
    return list.filter(item => Number(item.value) === Number(data))[0].label
  } catch (e) {
    return data
  }
})

Vue.filter('moneyFormatter', function (num) {
  return '¥' + (isNaN(num) ? 0.00 : parseFloat((num / 100).toFixed(2)))
})
