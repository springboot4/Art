export const tableObj = {
  columns: [
    {
      title: '昵称',
      field: 'nickName'
    },
    {
      title: '手机号码',
      field: 'mobile'
    },
    {
      title: '头像',
      field: 'avatarUrl',
      slot: true
    },
    {
      title: '性别',
      field: 'gender',
      type: 'dict',
      options: [
        {
          label: '女',
          value: '0'
        },
        {
          label: '男',
          value: '1'
        }
      ]
    },
    {
      title: '出生日期',
      field: 'birthday'
    },
    {
      title: '状态',
      field: 'status',
      type: 'dict',
      options: [
        {
          label: '正常',
          value: '1'
        },
        {
          label: '异常',
          value: '0'
        }
      ]
    },
    {
      title: '注册时间',
      field: 'createTime'
    },
    {
      title: '余额',
      field: 'balance',
      slot: true
    }
  ],
  addressColumns: [
    {
      title: '收货人',
      field: 'consigneeName'
    },
    {
      title: '联系方式',
      field: 'consigneeMobile'
    },
    {
      title: '收货地址',
      field: 'detailAddress'
    },
    {
      title: '是否默认',
      field: 'defaulted',
      type: 'dict',
      options: [
        {
          label: '是',
          value: '1'
        },
        {
          label: '否',
          value: '0'
        }
      ]
    }
  ]
}
