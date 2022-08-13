export const tableObj = {
  columns: [
    {
      title: '主键',
      field: 'id'
    },
    {
      title: '商品分类名称',
      field: 'name'
    },
    {
      title: '父级ID',
      field: 'parentId'
    },
    {
      title: '层级',
      field: 'level'
    },
    {
      title: '图标地址',
      field: 'iconUrl'
    },
    {
      title: '排序',
      field: 'sort'
    },
    {
      title: '显示状态:( 0:隐藏 1:显示)',
      field: 'visible'
    },
    {
      title: '创建时间',
      field: 'createTime'
    },
    {
      title: '更新时间',
      field: 'updateTime'
    },
    {
      title: '创建人',
      field: 'createBy'
    },
    {
      title: '更新人',
      field: 'updateBy'
    },
    {
      title: '操作',
      width: '200px',
      field: 'action',
      slot: true
    }
  ]
}
