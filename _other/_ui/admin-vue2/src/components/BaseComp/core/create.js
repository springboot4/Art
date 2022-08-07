import { KEY_COMPONENT_NAME } from '../global/variable'
export default function (sfc) {
  sfc.name = KEY_COMPONENT_NAME + sfc.name
  return sfc
}
