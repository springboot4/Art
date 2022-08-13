import request from '../../utils/request'

/**
 * 发送短信验证码
 */
export function sendSmsCode(phoneNumber) {
    return request({
        url: '/system/sms/sendSms',
        params: {phoneNumber},
        method: 'GET'
    })
}

