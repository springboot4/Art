<template>
  <div>
    <vxe-toolbar
      size="small"
      import
      export
      print
      custom
      v-show="toolbarShow"
      :refresh="{query: loadData}"
    >
      <template v-slot:buttons>
        <slot name="buttons"></slot>
      </template>
    </vxe-toolbar>

    <vxe-table
      ref="xTable"
      border="full"
      show-header-overflow
      show-overflow
      :column-config="{resizable: true,isCurrent: true}"
      :row-config="{isHover: true,isCurrent: true}"
      :print-config="{}"
      :export-config="{}"
      :data="localDataSource"
      :loading="localLoading"
      @sort-change="sortChangeEvent"
      @cell-dblclick="cellClickEvent"
      height="500px"
      :tree-config="{children: children,expandAll: expandAllTree}"
      :keyboard-config="{isArrow: true}"
      :row-style="rowStyle"
      :cell-class-name="cellClassName"
      v-on="$listeners"
      @checkbox-change="selectChangeEvent"
      @checkbox-all="selectAllEvent"
    >

      <!--复选框-->
      <vxe-column type="checkbox" width="60" v-if="checkbox"></vxe-column>

      <!--序号列-->
      <vxe-column type="seq" width="60" v-if="seq" :title="seqTitle"></vxe-column>

      <!--展开行-->
      <vxe-column type="expand" width="60" v-if="expandConfig&&expandConfig.expand">
        <template v-slot:content="{ row, rowIndex }">
          <template>
            <vxe-table border :data="row[expandConfig.expandField]" size="small">

              <template v-for="(column,index) in expandConfig.columns">
                <vxe-column
                  :key="index"
                  :field="column.field"
                  :title="column.title"
                  :width="column.width"
                  :sortable="column.sortable || false"
                  :tree-node="column.treeNode || false"
                  :align="column.align||'left'"
                  :fixed="column.fixed"
                >

                  <!--该列是否开启插槽-->
                  <template slot-scope="scope">

                    <!--不开启插槽，也没有type，直接显示内容-->
                    <span v-if="column.slot !== true && !column.type">
                      {{ scope.row[column.field] }}
                    </span>

                    <!--开启了插槽，那么根据字典名生成一个插槽-->
                    <slot
                      v-else-if="column.slot"
                      :row="scope.row"
                      :text="scope.row[column.field]"
                      :name="column.field"
                    >
                    </slot>

                    <!--否则根据列的type显示内容-->
                    <template v-else>

                      <!--input类型，直接显示内容-->
                      <span v-if="['input'].includes(column.type)">
                        {{ scope.row[column.field] }}
                      </span>

                      <!--如果是url类型，那么生成一个超链接-->
                      <span v-if="['url'].includes(column.type)">
                        <a :href="scope.row[column.field]" :target="column.target || '_blank'">{{ scope.row[column.prop] }}</a>
                      </span>

                      <!--如果是img类型，那么生成img标签-->
                      <span v-if="['url'].includes(column.type)">
                        <img :src="scope.row[column.field]" alt="FxzMall" />
                      </span>

                      <!--如果是字典类型，那么过滤出字典内容-->
                      <span v-if="['dict'].includes(column.type)">
                        <span>{{ scope.row[column.field] | dictFilter(column.options) }}</span>
                      </span>

                    </template>

                  </template>
                </vxe-column>
              </template>
            </vxe-table>
          </template>
        </template>
      </vxe-column>

      <template #empty>
        <span style="color: red;">
          <img src="https://n.sinaimg.cn/sinacn17/w120h120/20180314/89fc-fyscsmv5911424.gif">
          <p>不用再看了，没有更多数据了！</p>
        </span>
      </template>

      <template v-for="(column,index) in columns">
        <vxe-column
          :key="index"
          :field="column.field"
          :title="column.title"
          :width="column.width"
          :sortable="column.sortable || false"
          :tree-node="column.treeNode || false"
          :align="column.align||'left'"
          :fixed="column.fixed"
        >

          <!--该列是否开启插槽-->
          <template slot-scope="scope">

            <!--不开启插槽，也没有type，直接显示内容-->
            <span v-if="column.slot !== true && !column.type">
              {{ scope.row[column.field] }}
            </span>

            <!--开启了插槽，那么根据字典名生成一个插槽-->
            <slot
              v-else-if="column.slot"
              :row="scope.row"
              :text="scope.row[column.field]"
              :name="column.field"
            >
            </slot>

            <!--否则根据列的type显示内容-->
            <template v-else>

              <!--input类型，直接显示内容-->
              <span v-if="['input'].includes(column.type)">
                {{ scope.row[column.field] }}
              </span>

              <!--如果是url类型，那么生成一个超链接-->
              <span v-if="['url'].includes(column.type)">
                <a :href="scope.row[column.field]" :target="column.target || '_blank'">{{ scope.row[column.prop] }}</a>
              </span>

              <!--如果是img类型，那么生成img标签-->
              <span v-if="['url'].includes(column.type)">
                <img :src="scope.row[column.field]" alt="FxzMall" />
              </span>

              <!--如果是字典类型，那么过滤出字典内容-->
              <span v-if="['dict'].includes(column.type)">
                <span>{{ scope.row[column.field] | dictFilter(column.options) }}</span>
              </span>

            </template>

          </template>
        </vxe-column>

      </template>
    </vxe-table>

    <!--如果显示分页-->
    <vxe-pager
      v-if="showPagination"
      border
      size="medium"
      :loading="localLoading"
      :current-page="localPagination.currentPage"
      :page-size="localPagination.pageSize"
      :total="localPagination.total"
      :layouts="['PrevPage', 'JumpNumber', 'NextPage', 'FullJump', 'Sizes', 'Total']"
      @page-change="handlePageChange">
    </vxe-pager>
  </div>

</template>

<script>
import Sortable from 'sortablejs'

export default {
  name: 'FTable',
  data () {
    return {
      localDataSource: [], // 表格数据（与 loadData 行为一致，更新数据是不会重置状态）
      localLoading: false, // 表格是否显示加载中
      localPagination: {
        currentPage: 1, // 当前页
        pageSize: 50, // 每页记录数
        total: 0 // 总条数
      }
    }
  },
  props: {
    expandConfig: {
      type: Object,
      default: () => null
    },
    children: {
      type: String,
      default: 'children'
    },
    columns: { // 表格的列数据
      type: Array,
      default: function () {
        return []
      }
    },
    data: { // 加载表格数据的方法
      type: Function,
      required: true
    },
    cellClickEvent: { // 行点击事件
      type: Function,
      default: function () {
        return () => {
        }
      }
    },
    expandAllTree: { // 是否展开全部树节点
      type: Boolean,
      default: true
    },
    seq: { // 是否开启序号列
      type: Boolean,
      default: true
    },
    seqTitle: { // 序号列title
      type: String,
      default: '序号'
    },
    checkbox: { // 是否开启复选框
      type: Boolean,
      default: false
    },
    toolbarShow: { // 是否显示工具栏
      type: Boolean,
      default: true
    },
    pageNum: { // 当前页
      type: Number,
      default: 1
    },
    pageSize: { // 每页记录数
      type: Number,
      default: 50
    },
    showPagination: { // 是否显示分页
      type: Boolean,
      default: true
    },
    selectChangeEvent: { // 只对 type=checkbox 有效，当手动勾选并且值发生改变时触发的事件
      type: Function,
      default: function () {
        return () => {
        }
      }
    },
    selectAllEvent: { // 只对 type=checkbox 有效，当手动勾选全选时触发的事件
      type: Function,
      default: function () {
        return () => {
        }
      }
    },
    rowStyle: { // 给行附加样式，也可以是函数
      type: Function,
      default: function () {
        return () => {
        }
      }
    },
    cellClassName: { // 给单元格附加 className
      type: Function,
      default: function () {
        return () => {
        }
      }
    }
  },
  methods: {
    // 当排序条件发生变化时会触发该事件
    sortChangeEvent ({ column, property, order }) {
      console.info(column, property, order, 'column, property, orders')
      let orders = order
      if (order) {
        orders = order === 'desc' ? 'descend' : 'ascend'
      }
      this.loadData(this.localPagination, {}, {
        field: property,
        order: orders
      })
    },
    // 分页发生改变时会触发该事件
    handlePageChange ({ currentPage, pageSize }) {
      this.loadData({ currentPage, pageSize })
    },
    loadData (pagination, filters, sorter) {
      this.localLoading = true // 表格加载
      let parameter = {} // 构造查询参数

      if (this.showPagination) { // 如果显示分页
        // 赋值分页查询数据
        parameter = Object.assign(parameter, {
          current: (pagination && pagination.currentPage) ||
            this.localPagination.currentPage || this.pageNum,
          size: (pagination && pagination.pageSize) ||
            this.localPagination.pageSize || this.pageSize
        })
      }

      parameter = Object.assign(parameter,
        (sorter && sorter.field && {
          sortField: sorter.field
        }) || {},
        (sorter && sorter.order && {
          sortOrder: sorter.order
        }) || {}, {
          ...filters
        }
      )

      const result = this.data(parameter) // 传入构造好的参数 调用加载数据的方法

      if ((typeof result === 'object' || typeof result === 'function') && typeof result.then === 'function') { // 处理返回值
        result.then(r => {
          if (this.showPagination) {
            // 处理分页数据
            this.localPagination = Object.assign({}, this.localPagination, {
              currentPage: Number(r.current), // 返回结果中的当前分页数
              total: Number(r.total), // 返回结果中的总记录数
              pageSize: (pagination && pagination.pageSize) || this.localPagination.pageSize // 每页记录数
            })
            // 为防止删除数据后导致页面当前页面数据长度为 0 ,自动翻页到上一页 此处要注意后端要返回正确的当前页数值 否则可能死循环
            if (r.records.length === 0 && this.localPagination.currentPage !== 1) {
              this.localPagination.currentPage--
              this.loadData()
              return
            }
            this.localDataSource = r.records // 返回结果中的数组数据
          } else {
            this.localDataSource = r
          }
        }).catch(err => {
          this.$message.error(err.msg)
        }).finally(_ => {
          this.localLoading = false
        })
      }
    },
    // 列拖拽
    columnDrop () {
      this.$nextTick(() => {
        const xTable = this.$refs.xTable
        this.sortable = Sortable.create(xTable.$el.querySelector('.body--wrapper>.vxe-table--header .vxe-header--row'), {
          handle: '.vxe-header--column:not(.col--fixed)',
          onEnd: ({ item, newIndex, oldIndex }) => {
            const { fullColumn, tableColumn } = xTable.getTableColumn()
            const targetThElem = item
            const wrapperElem = targetThElem.parentNode
            const newColumn = fullColumn[newIndex]
            if (newColumn.fixed) {
              // 错误的移动
              if (newIndex > oldIndex) {
                wrapperElem.insertBefore(targetThElem, wrapperElem.children[oldIndex])
              } else {
                wrapperElem.insertBefore(wrapperElem.children[oldIndex], targetThElem)
              }
              return this.$XModal.message({ message: '固定列不允许拖动！', status: 'error' })
            }
            // 转换真实索引
            const oldColumnIndex = xTable.getColumnIndex(tableColumn[oldIndex])
            const newColumnIndex = xTable.getColumnIndex(tableColumn[newIndex])
            // 移动到目标列
            const currRow = fullColumn.splice(oldColumnIndex, 1)[0]
            fullColumn.splice(newColumnIndex, 0, currRow)
            xTable.loadColumn(fullColumn)
          }
        })
      })
    },
    // 行拖拽
    rowDrop () {
      this.$nextTick(() => {
        const xTable = this.$refs.xTable
        this.sortable = Sortable.create(xTable.$el.querySelector('.body--wrapper>.vxe-table--body tbody'), {
          handle: '.drag-btn',
          onEnd: ({ newIndex, oldIndex }) => {
            const currRow = this.localDataSource.splice(oldIndex, 1)[0]
            this.localDataSource.splice(newIndex, 0, currRow)
            this.$emit('changeData', this.localDataSource)
          }
        })
      })
    },
    refresh (bool = false) {
      this.loadData()
    }
  },
  beforeDestroy () {
    if (this.sortable) {
      this.sortable.destroy()
    }
  },
  created () {
    this.loadData()
    this.columnDrop()
    this.rowDrop()
  }
}
</script>

<style scoped>

</style>
