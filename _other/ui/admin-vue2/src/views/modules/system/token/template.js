export const tableObj = {
  columns: [
    {
      title: '用户名',
      field: 'username'
    },
    {
      title: '客户端',
      field: 'client_id'
    },
    {
      title: '令牌',
      field: 'access_token'
    },
    {
      title: '令牌类型',
      field: 'token_type'
    },
    {
      title: '过期时间',
      field: 'expires_in'
    },
    {
      title: '操作',
      width: '200px',
      field: 'action',
      slot: true
    }
  ]
}
