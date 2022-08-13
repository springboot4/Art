<template>
  <a-card>
    <a-steps :current="active" style="margin-top: 10px">
      <a-step title="填写商品信息" />
      <a-step title="设置商品属性" />
      <a-step title="设置商品库存" />
    </a-steps>

    <goods-info :goods-info="goodsInfo" v-if="active===0" @next="next" />
    <good-attr-val :goods-info="goodsInfo" v-if="active===1" @next="next" @prev="prev" />
    <goods-spec-val :goods-info="goodsInfo" v-if="active===2" @next="next" @prev="prev" />
  </a-card>
</template>

<script>
import GoodsInfo from './components/GoodsInfo'
import GoodAttrVal from '@/views/modules/mall/product/goods/components/GoodsAttrVal'
import GoodsSpecVal from '@/views/modules/mall/product/goods/components/GoodsSpecVal'

export default {
  name: 'Info',
  components: { GoodsInfo, GoodAttrVal, GoodsSpecVal },
  mixins: [],
  data () {
    return {
      active: 0,
      goodsInfo: {}
    }
  },
  created () {
  },
  methods: {
    next () {
      if (this.active++ >= 2) {
        this.active = 0
      }
    },
    prev () {
      if (this.active-- <= 0) {
        this.active = 0
        this.goodsInfo = {}
      }
    }
  }
}
</script>

<style scoped>

</style>
