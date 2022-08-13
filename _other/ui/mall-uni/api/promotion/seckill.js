import request from '../../utils/request'

/**
 * 查询秒杀活动
 */
export function seckillList() {
    return request({
        url: '/promotion/admin/seckillApply/list',
        method: 'GET',
        headers: {
            'auth': false
        }
    })
}
