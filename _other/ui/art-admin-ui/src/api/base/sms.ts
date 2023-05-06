import { defHttp } from '/@/utils/http/axios'
import { SmsModel } from '/@/api/base/model/smsModel'

enum Api {
  sendSms = '/system/sms/sendSms',
}

/**
 * 发送短信
 */

export const sendSmsApi = (params: SmsModel) => {
  return defHttp.get({ url: Api.sendSms, params })
}
