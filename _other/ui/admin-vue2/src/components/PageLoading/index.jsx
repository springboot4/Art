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

import { Spin } from 'ant-design-vue'

export const PageLoading = {
  name: 'PageLoading',
  props: {
    tip: {
      type: String,
      default: 'Loading..'
    },
    size: {
      type: String,
      default: 'large'
    }
  },
  render () {
    const style = {
      textAlign: 'center',
      background: 'rgba(0,0,0,0.6)',
      position: 'fixed',
      top: 0,
      bottom: 0,
      left: 0,
      right: 0,
      zIndex: 1100
    }
    const spinStyle = {
      position: 'absolute',
      left: '50%',
      top: '40%',
      transform: 'translate(-50%, -50%)'
    }
    return (<div style={style}>
      <Spin size={this.size} style={spinStyle} tip={this.tip} />
    </div>)
  }
}

const version = '0.0.1'
const loading = {}

loading.newInstance = (Vue, options) => {
  let loadingElement = document.querySelector('body>div[type=loading]')
  if (!loadingElement) {
    loadingElement = document.createElement('div')
    loadingElement.setAttribute('type', 'loading')
    loadingElement.setAttribute('class', 'ant-loading-wrapper')
    document.body.appendChild(loadingElement)
  }

  const cdProps = Object.assign({ visible: false, size: 'large', tip: 'Loading...' }, options)

  const instance = new Vue({
    data () {
      return {
        ...cdProps
      }
    },
    render () {
      const { tip } = this
      const props = {}
      this.tip && (props.tip = tip)
      if (this.visible) {
        return <PageLoading { ...{ props } } />
      }
      return null
    }
  }).$mount(loadingElement)

  function update (config) {
    const { visible, size, tip } = { ...cdProps, ...config }
    instance.$set(instance, 'visible', visible)
    if (tip) {
      instance.$set(instance, 'tip', tip)
    }
    if (size) {
      instance.$set(instance, 'size', size)
    }
  }

  return {
    instance,
    update
  }
}

const api = {
  show: function (options) {
    this.instance.update({ ...options, visible: true })
  },
  hide: function () {
    this.instance.update({ visible: false })
  }
}

const install = function (Vue, options) {
  if (Vue.prototype.$loading) {
    return
  }
  api.instance = loading.newInstance(Vue, options)
  Vue.prototype.$loading = api
}

export default {
  version,
  install
}
