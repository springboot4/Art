<template>
  <div>
    <div style="margin-top: 20px;margin-bottom: 30px">
      <f-table
        :showPagination="false"
        :columns="tableObj.specColumns"
        :data="loadData"
        ref="table">
        <template v-slot:buttons>
          <a-button type="primary" icon="plus" @click="addAttr">添加</a-button>
        </template>
        <template v-slot:action="{row}">
          <a-button
            size="small"
            type="danger"
            shape="circle"
            @click.stop="remove(row)">
            <a-icon type="delete" />
          </a-button>
        </template>
      </f-table>
    </div>
    <div style="margin-bottom: 10px;">
      <a-button
        @click="handlePrev"
        style="margin-right: 10px">上一步，填写商品属性
      </a-button>
      <a-button
        type="primary"
        @click="handleNext"
        style="margin-right: 10px">提交
      </a-button>
    </div>
    <goods-category-spec-edit
      ref="goodsCategoryAttrEdit"
      @ok="handleOk"
      :attrValList="attrValList"
      :categoryId="this.goodsInfo.categoryId[this.goodsInfo.categoryId.length-1]"
      :attrType="1" />
  </div>
</template>

<script>
import { addGoods } from '@/api/mall/product/goods'
import { tableObj } from '@/views/modules/mall/product/goods/components/template'
import { TableMixin } from '@/mixins/TableMixin'
import GoodsCategorySpecEdit from '@/views/modules/mall/product/goods/components/GoodsCategorySpecEdit'

export default {
  name: 'GoodSpecVal',
  mixins: [TableMixin],
  components: { GoodsCategorySpecEdit },
  data () {
    return {
      tableObj,
      attrValList: [], // 当前填写完成的属性以及属性值
      loadData: () => {
        return new Promise(resolve => {
          const res = {}
          res.records = this.attrValList
          resolve(this.attrValList)
        })
      }
    }
  },
  props: {
    goodsInfo: {
      type: Object,
      default: () => {
      }
    }
  },
  methods: {
    handleOk (v) {
      this.attrValList = v
      this.goodsInfo.skuList = this.attrValList
      this.queryPage()
    },
    addAttr () {
      this.$refs.goodsCategoryAttrEdit.init('', 'add')
    },
    remove (row) {
      this.attrValList = this.attrValList.filter(item => item.name !== row.name)
      this.queryPage()
    },
    handlePrev () {
      this.$emit('prev')
    },
    handleNext () {
      this.goodsInfo.categoryId = this.goodsInfo.categoryId[this.goodsInfo.categoryId.length - 1]
      console.log('this.goodsInfo', this.goodsInfo)

      this.goodsInfo.price = this.goodsInfo.price * 100
      this.goodsInfo.originPrice = this.goodsInfo.originPrice * 100
      const skus = JSON.parse(JSON.stringify(this.goodsInfo.skuList))
      skus.map((item) => {
        item.price *= 100
        return item
      })
      this.goodsInfo.skuList = skus

      addGoods(this.goodsInfo).then(_ => {
        this.$message.success('保存成功!')
        this.goodsInfo = {}
        this.$emit('next')
        this.$router.push({ name: 'goodsList' })
      })
    }
  }
}
</script>

<style scoped>

</style>
