import { TablePageModel } from '/#/web'
import { reactive, ref } from 'vue'
import { PageResult } from '/#/axios'

export default function <T>(queryPageCallback: CallableFunction) {
  // 数据内容
  const model = reactive({
    pages: {
      // 每页数量
      size: 10,
      // 当前页
      current: 1,
    },

    // 查询参数
    queryParam: {},

    // 分页参数
    pagination: {},
  } as TablePageModel<T>)

  // 加载状态
  const loading = ref(false)

  // 不可重新赋值 会失去绑定
  const { pages, pagination } = model

  /**
   * 普通查询
   */
  function query() {
    resetPage()
    queryPageCallback()
  }

  /**
   * 表格翻页或变动
   */
  function handleTableChange({ currentPage, pageSize }) {
    pages.current = currentPage
    pages.size = pageSize
    queryPageCallback()
  }

  /**
   * 重置当前页数
   */
  function resetPage() {
    pages.current = 1
  }

  /**
   * 分页查询返回结果处理
   */
  function pageQueryResHandel(res: PageResult) {
    pagination.current = Number(res.current)
    pagination.size = Number(res.size)
    pagination.total = Number(res.total)
    pagination.records = res.records
    loading.value = false
  }

  /**
   * 重置查询
   */
  function resetQuery() {
    resetQueryParams()
    queryPageCallback()
  }

  /**
   * 重置查询参数
   */
  function resetQueryParams() {
    model.queryParam = {}
  }

  return {
    model,
    loading,
    pages,
    pagination,
    query,
    resetPage,
    pageQueryResHandel,
    handleTableChange,
    resetQuery,
    resetQueryParams,
  }
}
