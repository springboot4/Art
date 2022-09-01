import { axios } from '@/utils/request'

/**
 * 添加秒杀活动商品信息
 */
export function addSeckillApply (seckillId, obj) {
  return axios({
    url: `/promotion/admin/seckillApply/${seckillId}`,
    method: 'POST',
    data: obj
  })
}
