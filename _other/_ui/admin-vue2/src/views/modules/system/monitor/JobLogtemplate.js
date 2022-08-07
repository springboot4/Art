export const tableObj = {
  columns: [
    {
      title: '任务日志ID',
      field: 'jobLogId'
    },
    {
      title: '任务名称',
      field: 'jobName'
    },
    {
      title: '任务组名',
      field: 'jobGroup'
    },
    {
      title: '调用目标字符串',
      field: 'invokeTarget'
    },
    {
      title: '日志信息',
      field: 'jobMessage'
    },
    {
      title: '执行状态',
      field: 'status',
      type: 'dict',
      options: [
        {
          label: '正常',
          value: '0'
        },
        {
          label: '失败',
          value: '1'
        }
      ]
    },
    {
      title: '异常信息',
      field: 'exceptionInfo'
    },
    {
      title: '创建时间',
      field: 'createTime',
      width: '200px'
    },
    {
      title: '操作',
      width: '100px',
      field: 'action',
      slot: true
    }
  ]
}
