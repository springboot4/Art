export const tableObj = {
  columns: [
    {
      title: '活动名称',
      field: 'promotionName',
      width: '180px',
      fixed: 'left'
    },
    {
      title: '活动开始时间',
      field: 'startTime',
      width: '180px'
    },
    {
      title: '报名截至时间',
      field: 'applyEndTime',
      width: '180px'
    },
    {
      title: '时间场次',
      field: 'hours',
      width: '200px',
      slot: true
    },
    {
      title: '活动状态',
      field: 'promotionStatus',
      width: '150px',
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
