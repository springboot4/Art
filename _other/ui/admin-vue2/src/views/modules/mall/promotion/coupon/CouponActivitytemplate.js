export const tableObj = {
  columns: [
    {
      title: '活动名称',
      field: 'promotionName',
      width: '150px'
    },
    {
      title: '活动开始时间',
      field: 'startTime',
      width: '150px'
    },
    {
      title: '活动结束时间',
      field: 'endTime',
      width: '150px'
    },
    {
      title: '活动范围',
      field: 'activityScope',
      width: '150px',
      slot: true
    },
    {
      title: '优惠券活动类型',
      field: 'couponActivityType',
      width: '150px',
      slot: true
    },
    {
      title: '状态',
      field: 'statue',
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
