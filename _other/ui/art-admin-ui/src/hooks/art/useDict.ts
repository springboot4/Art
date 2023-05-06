import { getDictItemsByType } from '/@/api/system/dict'
import { DictItemDto } from '/@/api/system/dict/types'

export default function () {
  /**
   * 根据字典类型获取字典项
   * @param dictType 字典类型
   * @param dictItems dictItems
   */
  function dictItemsByType(dictType: string, dictItems: DictItemDto[]) {
    getDictItemsByType(dictType).then((res) => {
      dictItems.push(...res)
    })
  }

  return {
    dictItemsByType,
  }
}
