<template>
  <a-modal
    :title="title"
    :width="1280"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :maskClosable="false"
    @cancel="handleCancel"
  >
    <vxe-table
      v-if="!showable"
      border
      resizable
      :data="skuData"
      @checkbox-change="selectChangeEvent"
      :edit-config="{trigger: 'dblclick', mode: 'cell'}">
      <vxe-table-column type="checkbox" width="60"></vxe-table-column>
      <vxe-table-column field="skuSn" title="商品编码"></vxe-table-column>
      <vxe-table-column field="name" title="商品名称"></vxe-table-column>
      <vxe-table-column
        field="price"
        title="价格"
        :edit-render="{name: '$input', props: {type: 'number', disabled: showable}}"
      ></vxe-table-column>
      <vxe-table-column
        field="quantity"
        title="促销数量"
        :edit-render="{name: '$input', props: {type: 'number', disabled: showable}}"></vxe-table-column>
    </vxe-table>
    <template v-slot:footer>
      <a-button key="cancel" @click="handleCancel">取消</a-button>
      <a-button v-if="!showable" key="forward" :loading="confirmLoading" type="primary" @click="handleOk">保存
      </a-button>
    </template>
  </a-modal>
</template>

<script>
import { tableObj } from '@/views/modules/mall/promotion/coupon/ActivityTemplate'
import { FormMixin } from '@/mixins/FormMixin'
import { listSku } from '@/api/mall/product/goods'
import { addSeckillApply } from '@/api/mall/promotion/seckillApply'

export default {
  name: 'SeckillManageEdit',
  mixins: [FormMixin],
  data () {
    return {
      tableObj,
      selectData: [],
      skuData: [],
      timeLine: undefined,
      applyVOs: [],
      seckillId: undefined
    }
  },
  created () {
    this.getSkuList()
  },
  methods: {
    getSkuList () {
      this.skuData = []
      listSku().then(res => {
        this.skuData = res.data
        this.skuData.forEach(sku => {
          sku.originalPrice = sku.price
          sku.price = sku.price / 100
          sku.skuId = sku.id
          sku.goodsName = sku.name
          sku.timeLine = this.timeLine
        })
      })
    },
    selectChangeEvent ({ checked, records }) {
      if (checked) {
        this.selectData = records
        console.log(records)
      }
    },
    edit (id, type, val) {
      this.form = {}
      this.seckillId = id
      this.timeLine = val
      this.skuData = []
      this.selectData = []
      this.getSkuList()
      this.confirmLoading = false
    },
    handleOk () {
      this.selectData.forEach(item => {
        item.price = item.price * 100
        item.id = undefined
      })

      this.confirmLoading = true

      this.applyVOs = this.selectData

      console.log('保存:', this.applyVOs)
      addSeckillApply(this.seckillId, this.applyVOs)
        .then(_ => {
          setTimeout(() => {
            this.confirmLoading = false
            this.$emit('ok')
            this.visible = false
          }, 200)
        })
        .catch(error => {
          this.$message.error(error.msg)
        })
    }
  }
}
</script>
