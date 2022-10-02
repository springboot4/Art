export const tableObj = {
  columns: [
    {
      title: '租户名',
      field: 'name'
    },
    {
      title: '管理员姓名',
      field: 'tenantAdminName'
    },
    {
      title: '管理员手机号',
      field: 'tenantAdminMobile'
    },
    {
      title: '状态',
      field: 'status',
      slot: true
    },
    {
      title: '过期时间',
      field: 'expireTime'
    },
    {
      title: '账号数量',
      field: 'accountCount'
    },
    {
      title: '操作',
      width: '200px',
      field: 'action',
      slot: true
    }
  ]
}
