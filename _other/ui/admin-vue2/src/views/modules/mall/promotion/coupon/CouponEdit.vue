<template>
  <a-modal
    :title="title"
    :width="1280"
    :visible="visible"
    :confirmLoading="confirmLoading"
    :maskClosable="false"
    @cancel="handleCancel"
  >
    <a-form-model
      ref="form"
      :model="form"
      :rules="rules"
      :label-col="labelCol"
      :wrapper-col="wrapperCol"
    >
      <a-form-model-item label="主键" prop="id" hidden="true" />
      <a-form-model-item label="结束时间" prop="endTime" hidden="true" />
      <a-form-model-item
        label="活动名称"
        prop="promotionName"
      >
        <a-input v-model="form.promotionName" v-if="!showable" />
        <span v-else>{{ form.promotionName }}</span>
      </a-form-model-item>
      <a-form-model-item
        label="优惠券名称"
        prop="couponName"
      >
        <a-input v-model="form.couponName" v-if="!showable" />
        <span v-else>{{ form.couponName }}</span>
      </a-form-model-item>
      <a-form-model-item
        label="折扣方式"
        prop="couponType"
      >
        <a-select v-model="form.couponType" v-if="!showable">
          <a-select-option
            v-for="item in couponTypes"
            :key="item.value"
            :label="item.label"
            :value="item.value">
            {{ item.label }}
          </a-select-option>
        </a-select>
        <span v-else>
          <span v-if="form.couponType==='discount'">折扣券</span>
          <span v-else>直减券</span>
        </span>
      </a-form-model-item>
      <a-form-model-item
        label="面额"
        prop="price"
        v-if="form.couponType==='cash'"
      >
        <a-input-number v-model="form.price" v-if="!showable" style="width: 100%" :min="0" />
        <span v-else>{{ form.price }}</span>
      </a-form-model-item>
      <a-form-model-item
        label="折扣"
        prop="couponDiscount"
        v-if="form.couponType==='discount'"
      >
        <a-input-number v-model="form.couponDiscount" v-if="!showable" :min="0" :max="10" style="width: 100%" />
        <span v-else>{{ form.couponDiscount }}折</span>
      </a-form-model-item>
      <a-form-model-item
        label="获取方式"
        prop="getType"
      >
        <a-select v-model="form.getType" v-if="!showable">
          <a-select-option
            v-for="item in getTypes"
            :key="item.value"
            :label="item.label"
            :value="item.value">
            {{ item.label }}
          </a-select-option>
        </a-select>
        <span v-else>
          <span v-if="form.getType==='free'">直接领取</span>
          <span v-else>活动发放</span>
        </span>
      </a-form-model-item>
      <a-form-model-item
        label="发行数量"
        prop="publishNum"
        v-if="form.getType==='free'"
      >
        <a-input-number v-model="form.publishNum" v-if="!showable" style="width: 100%" />
        <span v-else>{{ form.publishNum }}张</span>
      </a-form-model-item>
      <a-form-model-item
        label="每人限领"
        prop="couponLimitNum"
        v-if="form.getType==='free'"
      >
        <a-input-number v-model="form.couponLimitNum" v-if="!showable" style="width: 100%" />
        <span v-else>{{ form.couponLimitNum }}张</span>
      </a-form-model-item>
      <a-form-model-item
        label="消费门槛"
        prop="consumeThreshold"
      >
        <a-input-number v-model="form.consumeThreshold" v-if="!showable" style="width: 100%" />
        <span v-else>{{ form.consumeThreshold }}元</span>
      </a-form-model-item>
      <a-form-model-item
        label="活动时间"
        prop="startTime">
        <a-radio-group v-model="form.rangeDayType" v-if="!showable">
          <a-radio :value="'dynamictime'">
            有效期
          </a-radio>
          <a-radio :value="'fixedtime'">
            起止时间
          </a-radio>
        </a-radio-group>
        <span v-if="form.rangeDayType&&form.rangeDayType==='dynamictime'">
          <a-input-number v-model="form.effectiveDays" v-if="!showable" style="width: 100%" :min="1" />
          <div v-else>领取后{{ form.effectiveDays }}天内有效</div>
        </span>
        <span v-if="form.rangeDayType&&form.rangeDayType==='fixedtime'&&!showable">
          <a-range-picker
            style="width: 100%"
            v-model="time"
            :show-time="{ format: 'HH:mm:ss' }"
            format="YYYY-MM-DD HH:mm:ss"
            :placeholder="['开始时间', '结束时间']"
          />
        </span>
        <span v-if="form.rangeDayType&&form.rangeDayType==='fixedtime'&&showable">
          {{ form.startTime }} 至 {{ form.endTime }}
        </span>
      </a-form-model-item>
      <a-form-model-item
        label="活动范围"
        prop="scopeType">
        <a-radio-group v-model="form.scopeType" v-if="!showable">
          <a-radio :value="'all'">
            全场通用
          </a-radio>
          <a-radio :value="'portion_goods'">
            指定商品
          </a-radio>
        </a-radio-group>
        <span v-if="form.scopeType&&form.scopeType==='portion_goods'&&!showable">
          <a-transfer
            :data-source="skuData"
            show-search
            :filter-option="filterOption"
            :target-keys="targetKeys"
            :render="item => item.name"
            @change="handleChange"
          />
        </span>
        <span v-if="showable">
          <span v-if="form.scopeType==='all'">全品类</span>
          <span v-else>指定商品</span>
        </span>
      </a-form-model-item>
      <a-form-model-item
        label="活动描述"
        prop="description"
      >
        <a-textarea v-model="form.description" v-if="!showable" />
        <span v-else>{{ form.description }}</span>
      </a-form-model-item>
    </a-form-model>

    <template v-slot:footer>
      <a-button key="cancel" @click="handleCancel">取消</a-button>
      <a-button v-if="!showable" key="forward" :loading="confirmLoading" type="primary" @click="handleOk">保存
      </a-button>
    </template>
  </a-modal>
</template>

<script>
import { FormMixin } from '@/mixins/FormMixin'
import { get, add, update } from '@/api/mall/promotion/coupon'
import { listSku } from '@/api/mall/product/goods'
import moment from 'moment'

export default {
  name: 'CouponEdit',
  mixins: [FormMixin],
  data () {
    return {
      skuData: [],
      targetKeys: [],
      time: [],
      form: {
        id: null,
        promotionName: null,
        startTime: null,
        endTime: null,
        couponName: null,
        getType: null,
        consumeThreshold: null,
        couponDiscount: null,
        couponLimitNum: null,
        couponType: null,
        description: null,
        price: null,
        publishNum: null,
        receivedNum: null,
        scopeId: null,
        scopeType: null,
        usedNum: null,
        rangeDayType: null,
        effectiveDays: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        deleteFlag: null
      },
      rules: {},
      getTypes: [
        {
          label: '直接领取',
          value: 'free'
        },
        {
          label: '活动发放',
          value: 'activity'
        }
      ],
      couponTypes: [
        {
          label: '折扣券',
          value: 'discount'
        },
        {
          label: '直减券',
          value: 'cash'
        }
      ]
    }
  },
  created () {
    this.getSkuList()
  },
  methods: {
    getSkuList () {
      this.skuData = []
      this.targetKeys = []
      listSku().then(res => {
        this.skuData = res.data
        // eslint-disable-next-line no-return-assign
        this.skuData.forEach(item => {
          item.title = item.name
          item.key = item.id
          item.skuId = item.id
          item.goodsId = item.spuId
          item.goodsName = item.name
          item.id = undefined
        })
      })
    },
    filterOption (inputValue, option) {
      return option.name.indexOf(inputValue) > -1
    },
    handleChange (targetKeys) {
      this.targetKeys = targetKeys
      this.form.scopeId = this.targetKeys.join()
    },
    edit (id, type) {
      this.resetForm()
      if (['edit', 'show'].includes(type)) {
        this.confirmLoading = true
        get(id).then(res => {
          this.form = res.data
          this.confirmLoading = false
        })
      } else {
        this.confirmLoading = false
      }
    },
    handleOk () {
      this.$refs.form.validate(async valid => {
        if (valid) {
          const keys = this.targetKeys
          const skus = this.skuData
          if (this.form.scopeType === 'portion_goods') {
            this.form.promotionGoodsList = skus.filter(item => keys.indexOf(item.key) > -1)
          }

          if (this.form.rangeDayType === 'fixedtime') {
            this.form.startTime = moment(this.time[0]).format('YYYY-MM-DD HH:mm:ss')
            this.form.endTime = moment(this.time[1]).format('YYYY-MM-DD HH:mm:ss')
          }
          this.confirmLoading = true
          if (this.type === 'add') {
            await add(this.form).catch(err => this.$message.error(err.msg))
          } else if (this.type === 'edit') {
            await update(this.form)
          }
          setTimeout(() => {
            this.confirmLoading = false
            this.$emit('ok')
            this.visible = false
          }, 200)
        } else {
          return false
        }
      })
    },
    resetForm () {
      this.$nextTick(() => {
        this.$refs.form.resetFields()
      })
    }
  }
}
</script>

<style scoped>

</style>
