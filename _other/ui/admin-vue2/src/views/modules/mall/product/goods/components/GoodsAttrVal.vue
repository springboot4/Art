<template>
  <div>
    <div style="margin-top: 20px;margin-bottom: 30px">
      <f-table
        :showPagination="false"
        :columns="tableObj.columns"
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
        style="margin-right: 10px">上一步，填写商品信息
      </a-button>
      <a-button
        type="primary"
        @click="handleNext"
        style="margin-right: 10px">下一步，设置商品规格
      </a-button>
    </div>
    <goods-category-attr-edit
      ref="goodsCategoryAttrEdit"
      @ok="handleOk"
      :attrValList="attrValList"
      :categoryId="this.goodsInfo.categoryId[this.goodsInfo.categoryId.length-1]"
      :attrType="2" />
  </div>
</template>

<script>
import { tableObj } from '@/views/modules/mall/product/goods/components/template'
import { TableMixin } from '@/mixins/TableMixin'
import GoodsCategoryAttrEdit from '@/views/modules/mall/product/goods/components/GoodsCategoryAttrEdit'

export default {
  name: 'GoodAttrVal',
  mixins: [TableMixin],
  components: { GoodsCategoryAttrEdit },
  data () {
    return {
      tableObj,
      attrValList: [],
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
      default: () => {}
    }
  },
  methods: {
    handleOk (v) {
      this.attrValList = v
      this.goodsInfo.attrList = this.attrValList
      this.queryPage()
    },
    addAttr () {
      this.$refs.goodsCategoryAttrEdit.init('', 'add')
    },
    remove (row) {
      this.attrValList = this.attrValList.filter(item => item.attributeId !== row.attributeId)
      this.queryPage()
    },
    handlePrev () {
      this.$emit('prev')
    },
    handleNext () {
      this.$emit('next')
    }
  }
}
</script>

<style scoped>

</style>
