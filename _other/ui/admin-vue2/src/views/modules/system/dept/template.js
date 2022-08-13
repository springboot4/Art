export const tableObj = {
  columns: [
    {
      title: '部门名称',
      field: 'deptName',
      type: 'input',
      placeholder: '',
      search: false,
      treeNode: true
    },
    {
      title: '排序',
      field: 'orderNum'
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
