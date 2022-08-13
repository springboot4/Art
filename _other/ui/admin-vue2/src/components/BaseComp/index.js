import components from './ui'

const install = function (Vue, opts = {}) {
  components.map(component => {
    Vue.component(component.name, component)
  })
}

if (typeof window !== 'undefined' && window.Vue) {
  install(window.Vue)
}

const BizCom = {
  version: '0.0.1',
  install
}

export default BizCom
