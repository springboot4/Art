export const tableObj = {
  columns: [
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
      title: 'cron执行表达式',
      field: 'cronExpression'
    },
    {
      title: '状态',
      field: 'status',
      slot: true
    },
    {
      title: '操作',
      width: '200px',
      field: 'action',
      slot: true
    }
  ]
}
