export const tableObj = {
  columns: [
    {
      title: '商品属性',
      field: 'name'
    },
    {
      title: '属性值',
      field: 'value'
    },
    {
      title: '操作',
      width: '80px',
      field: 'action',
      slot: true
    }
  ],
  specColumns: [
    {
      title: '商品名称',
      field: 'name'
    },
    {
      title: '商品价格',
      field: 'price'
    },
    {
      title: '库存编号',
      field: 'skuSn'
    },
    {
      title: '库存数量',
      field: 'stockNum'
    },
    {
      title: '操作',
      width: '150px',
      field: 'action',
      slot: true
    }
  ]
}
