import { useUserStoreWithOut } from '/@/store/modules/user'
import { computed } from 'vue'
import { getAppEnvConfig } from '/@/utils/env'
import { getTenant } from '/@/utils/auth'
const useUserStore = useUserStoreWithOut()
const { VITE_GLOB_API_URL } = getAppEnvConfig()

export function useUpload(uploadUrl: string) {
  /**
   * 文件上传请求头
   */
  const uploadHeader = computed(() => {
    return {
      Authorization: 'Bearer ' + useUserStore.getToken,
      'TENANT-ID': getTenant(),
    }
  })

  /**
   * 上传地址
   */
  const uploadAction = computed(() => {
    return VITE_GLOB_API_URL + uploadUrl
  })

  return {
    uploadHeader,
    uploadAction,
  }
}
