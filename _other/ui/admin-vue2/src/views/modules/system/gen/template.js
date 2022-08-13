export const tableObj = {
  columns: [
    {
      title: '表名',
      field: 'tableName'
    },
    {
      title: '引擎类型',
      field: 'engine'
    },
    {
      title: '表描述',
      field: 'tableComment'
    },
    {
      title: '创建时间',
      field: 'createTime'
    },
    {
      title: '操作',
      width: '200px',
      field: 'action',
      slot: true
    }
  ]
}
