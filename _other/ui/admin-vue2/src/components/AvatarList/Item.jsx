/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import PropTypes from 'ant-design-vue/es/_util/vue-types'
import { Tooltip, Avatar } from 'ant-design-vue'
import { getSlotOptions } from 'ant-design-vue/lib/_util/props-util'
import { warning } from 'ant-design-vue/lib/vc-util/warning'

export const AvatarListItemProps = {
  tips: PropTypes.string.def(null),
  src: PropTypes.string.def('')
}

const Item = {
  __ANT_AVATAR_CHILDREN: true,
  name: 'AvatarListItem',
  props: AvatarListItemProps,
  created () {
    warning(getSlotOptions(this.$parent).__ANT_AVATAR_LIST, 'AvatarListItem must be a subcomponent of AvatarList')
  },
  render () {
    const AvatarDom = <Avatar size={this.$parent.size} src={this.src} />
    return this.tips && <Tooltip title={this.tips}>{AvatarDom}</Tooltip> || <AvatarDom />
  }
}

export default Item
