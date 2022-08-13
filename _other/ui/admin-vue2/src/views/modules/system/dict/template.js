export const tableObj = {
  columns: [
    {
      title: '字典编码',
      field: 'type'
    },
    {
      title: '描述',
      field: 'description'
    },
    {
      title: '字典类型',
      field: 'systemFlag',
      align: 'center',
      slot: true
    },
    {
      title: '备注信息',
      field: 'remark'
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
