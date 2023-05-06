import { UploadApiResult } from './model/uploadModel'
import { defHttp } from '/@/utils/http/axios'
import { UploadFileParams } from '/#/axios'
import { getAppEnvConfig } from '/@/utils/env'
import { getTenant } from '/@/utils/auth'
import { useUserStoreWithOut } from '/@/store/modules/user'

const useUserStore = useUserStoreWithOut()

const { VITE_GLOB_API_URL } = getAppEnvConfig()

/**
 * 文件上传
 */
export function uploadApi(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: ProgressEvent) => void,
) {
  return defHttp.uploadFile<UploadApiResult>(
    {
      url: VITE_GLOB_API_URL + '/system/file/add',
      onUploadProgress,
      headers: {
        Authorization: 'Bearer ' + useUserStore.getToken,
        'TENANT-ID': getTenant(),
      },
    },
    params,
  )
}
