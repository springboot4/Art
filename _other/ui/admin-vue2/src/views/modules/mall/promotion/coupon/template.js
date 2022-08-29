export const tableObj = {
  columns: [
    {
      title: '优惠券名称',
      field: 'couponName',
      width: '120px'
    },
    {
      title: '消费门槛',
      field: 'consumeThreshold',
      width: '100px'
    },
    {
      title: '使用范围',
      field: 'scopeType',
      width: '100px',
      slot: true
    },
    {
      title: '获取方式',
      field: 'getType',
      width: '100px',
      slot: true
    },
    {
      title: '折扣',
      field: 'couponDiscount',
      width: '100px'
    },
    {
      title: '面额',
      field: 'price',
      width: '100px'
    },
    {
      title: '发行数量',
      field: 'publishNum',
      width: '100px'
    },
    {
      title: '已被领取的数量',
      field: 'receivedNum',
      width: '100px'
    },
    {
      title: '活动开始时间',
      field: 'startTime',
      width: '180px'
    },
    {
      title: '活动结束时间',
      field: 'endTime',
      width: '180px'
    },
    {
      title: '状态',
      field: 'statue',
      width: '100px',
      slot: true
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
