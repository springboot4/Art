import { defHttp } from '/@/utils/http/axios'

/**
 * 查询配置信息
 */
export const findConfiguration = () => {
  return defHttp.get({
    url: '/auth/configuration',
  })
}
