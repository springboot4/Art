export const tableObj = {
  columns: [
    {
      title: '文件名',
      field: 'fileName'
    },
    {
      title: '空间名',
      field: 'bucketName'
    },
    {
      title: '原文件名',
      field: 'original'
    },
    {
      title: '文件类型',
      field: 'type'
    },
    {
      title: '文件大小',
      field: 'fileSize'
    },
    {
      title: '创建时间',
      field: 'createTime'
    },
    {
      title: '操作',
      width: '200px',
      field: 'action',
      slot: true
    }
  ]
}
