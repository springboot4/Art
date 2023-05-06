export enum OptionsEnum {
  OTHER = '0',
  ADD = '1',
  MODIFY = '2',
  DELETE = '3',
  LOGIN = '4',
  EXPORT = '5',
  IMPORT = '6',
  FORCE_OUT = '7',
  GENERATE_CODE = '8',
  CLEAR_DATA = '9',
}

export const OperOptions = [
  { label: '其他', value: OptionsEnum.OTHER },
  { label: '新增', value: OptionsEnum.ADD },
  { label: '修改', value: OptionsEnum.MODIFY },
  { label: '删除', value: OptionsEnum.DELETE },
  { label: '登录', value: OptionsEnum.LOGIN },
  { label: '导出', value: OptionsEnum.EXPORT },
  { label: '导入', value: OptionsEnum.IMPORT },
  { label: '强退', value: OptionsEnum.FORCE_OUT },
  { label: '生成代码', value: OptionsEnum.GENERATE_CODE },
  { label: '清空数据', value: OptionsEnum.CLEAR_DATA },
]
