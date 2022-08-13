// eslint-disable-next-line no-unused-vars
import moment from 'moment'

export default function () {
  return {
    methods: {
      getLabelText (item) {
        if (typeof this.typeformat === 'function') {
          return this.typeformat(item, this.labelKey, this.valueKey)
        }
        return item[this.labelKey]
      },
      handleClick () {
        console.log('this.click', this.click())
        const result =
          this.isString && this.multiple ? this.text.join(',') : this.text
        if (typeof this.click === 'function') {
          this.click({ value: result, column: this.column })
        }
      },
      handleChange (value) {
        let result = value
        this.text = result
        if (['date', 'time'].includes(this.type)) {
          const format = this.format.replace('dd', 'DD').replace('yyyy', 'YYYY')
          console.log(format, 'format')
          result = value.format(format)
        } else if (['radio', 'input'].includes(this.type)) {
          result = value.target.value
        }
        if ((this.isString || this.isNumber) && (this.multiple || ['checkbox', 'cascader', 'img', 'array'].includes(this.type))) {
          result = value.join(',')
        }
        if (typeof this.change === 'function') {
          this.change({ value: result, column: this.column })
        }
        this.$emit('input', result)
        this.$emit('change', result)
      }
    }
  }
}
