export const tableObj = {
  columns: [
    {
      title: '客户端ID',
      field: 'clientId',
      width: '100px'
    },
    {
      title: '资源列表',
      field: 'resourceIds',
      width: '100px'
    },
    {
      title: '客户端密钥',
      field: 'clientSecret',
      width: '100px'
    },
    {
      title: '域',
      field: 'scope',
      width: '100px'
    },
    {
      title: '认证类型',
      field: 'authorizedGrantTypes',
      width: '200px'
    },
    {
      title: '重定向地址',
      field: 'webServerRedirectUri',
      width: '200px'
    },
    {
      title: '角色列表',
      field: 'authorities',
      width: '100px'
    },
    {
      title: 'token 有效期',
      field: 'accessTokenValidity',
      width: '100px'
    },
    {
      title: '刷新令牌有效期',
      field: 'refreshTokenValidity',
      width: '100px'
    },
    {
      title: '令牌扩展字段JSON',
      field: 'additionalInformation',
      width: '100px'
    },
    {
      title: '是否自动放行',
      field: 'autoapprove',
      width: '100px'
    },
    {
      title: '更新时间',
      field: 'updateTime',
      width: '100px'
    },
    {
      title: '更新人',
      field: 'updateBy',
      width: '100px'
    },
    {
      title: '操作',
      width: '200px',
      field: 'action',
      slot: true,
      fixed: 'right'
    }
  ]
}
