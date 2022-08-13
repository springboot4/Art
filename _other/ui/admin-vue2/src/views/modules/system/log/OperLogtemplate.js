export const tableObj = {
  columns: [
    {
      title: '操作模块',
      field: 'title',
      width: '100px'
    },
    {
      title: '业务类型',
      field: 'businessType',
      width: '100px',
      type: 'dict',
      options: [
        {
          label: '其他',
          value: '0'
        },
        {
          label: '新增',
          value: '1'
        },
        {
          label: '修改',
          value: '2'
        },
        {
          label: '删除',
          value: '3'
        },
        {
          label: '登录',
          value: '4'
        },
        {
          label: '导出',
          value: '5'
        },
        {
          label: '导入',
          value: '6'
        },
        {
          label: '强退',
          value: '7'
        },
        {
          label: '生成代码',
          value: '8'
        },
        {
          label: '清空数据',
          value: '9'
        }
      ]
    },
    {
      title: '方法名称',
      field: 'method',
      width: '100px'
    },
    {
      title: '请求方式',
      field: 'requestMethod',
      width: '100px'
    },
    {
      title: '操作人员',
      field: 'operName',
      width: '100px'
    },
    {
      title: '请求URL',
      field: 'operUrl',
      width: '100px'
    },
    {
      title: '主机地址',
      field: 'operIp',
      width: '100px'
    },
    {
      title: '请求参数',
      field: 'operParam',
      width: '100px'
    },
    {
      title: '操作状态',
      field: 'status',
      type: 'dict',
      options: [
        {
          label: '正常',
          value: '0'
        },
        {
          label: '异常',
          value: '1'
        }
      ],
      width: '100px'
    },
    {
      title: '错误消息',
      field: 'errorMsg',
      width: '100px'
    },
    {
      title: '执行时间',
      field: 'time',
      width: '100px'
    },
    {
      title: '创建时间',
      field: 'createTime',
      width: '100px'
    },
    {
      title: '操作',
      width: '120px',
      field: 'action',
      fixed: 'right',
      slot: true
    }
  ]
}
