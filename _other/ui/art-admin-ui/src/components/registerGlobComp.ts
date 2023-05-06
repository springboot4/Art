import type { App } from 'vue'
import { Button } from './Button'
import {
  Input,
  Layout,
  Space,
  Form,
  Card,
  Row,
  Col,
  Popconfirm,
  Divider,
  InputNumber,
  Alert,
  Upload,
  Select,
  Tag,
  TreeSelect,
  Radio,
  Tree,
  Tabs,
  Switch,
  Dropdown,
  Menu,
  DatePicker,
  List,
  Avatar,
} from 'ant-design-vue'
import VXETable from 'vxe-table'
import 'vxe-table/lib/style.css'
export function registerGlobComp(app: App) {
  app
    .use(Input)
    .use(Button)
    .use(Layout)
    .use(Space)
    .use(Form)
    .use(Card)
    .use(Row)
    .use(Col)
    .use(Popconfirm)
    .use(Divider)
    .use(InputNumber)
    .use(Alert)
    .use(Upload)
    .use(Select)
    .use(Tag)
    .use(TreeSelect)
    .use(Radio)
    .use(Tree)
    .use(Tabs)
    .use(Switch)
    .use(Dropdown)
    .use(Menu)
    .use(DatePicker)
    .use(List)
    .use(Avatar)
    .use(VXETable)
}
