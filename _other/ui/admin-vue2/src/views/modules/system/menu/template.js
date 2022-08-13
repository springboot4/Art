export const tableObj = {
  hidden: true,
  columns: [
    {
      title: '菜单名称',
      field: 'title',
      type: 'input',
      placeholder: '',
      search: false,
      treeNode: true,
      width: 180
    },
    {
      title: '权限代码',
      field: 'perms',
      hidden: false
    },
    {
      title: '菜单类型',
      field: 'type',
      slot: true
    },
    {
      title: 'URL',
      field: 'path'
    },
    {
      title: '组件路径',
      field: 'component'
    },
    {
      title: '组件名称',
      field: 'name'
    },
    {
      title: '是否缓存',
      field: 'keepAlive',
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
    },
    {
      title: '是否隐藏页面',
      field: 'hidden',
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
    },
    {
      title: '操作',
      width: '200px',
      field: 'action',
      slot: true
    }
  ]
}
