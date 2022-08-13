export const tableObj = {
  columns: [
    {
      title: '用户名',
      field: 'username'
    },
    {
      title: '部门',
      field: 'deptName'
    },
    {
      title: '邮箱',
      field: 'email'
    },
    {
      title: '手机号',
      field: 'mobile'
    },
    {
      title: '状态',
      field: 'status',
      type: 'dict',
      options: [
        {
          label: '锁定',
          value: '0'
        },
        {
          label: '正常',
          value: '1'
        }
      ]
    },
    {
      title: '注册时间',
      field: 'createTime'
    },
    {
      title: '性别',
      field: 'sex',
      type: 'dict',
      options: [
        {
          label: '男',
          value: '0'
        },
        {
          label: '女',
          value: '1'
        }
      ]
    },
    {
      title: '角色',
      field: 'roleName'
    },
    {
      title: '岗位',
      field: 'postName'
    },
    {
      title: '操作',
      width: '200px',
      field: 'action',
      slot: true
    }
  ]
}
