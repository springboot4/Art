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
      <a-form-model-item label="主键" prop="id" hidden="true">
        <a-input v-model="form.id" :disabled="showable" />
      </a-form-model-item>
      <a-form-model-item
        label="活动名称"
        prop="promotionName"
      >
        <a-input v-model="form.promotionName" v-if="!showable" />
        <span v-else>{{ form.promotionName }}</span>
      </a-form-model-item>
      <a-form-model-item
        label="活动开始时间"
        prop="startTime"
      >
        <a-range-picker
          v-if="!showable"
          style="width: 100%"
          v-model="time"
          :show-time="{ format: 'HH:mm:ss' }"
          format="YYYY-MM-DD HH:mm:ss"
          :placeholder="['开始时间', '结束时间']"
        />
        <span v-else>{{ form.startTime }}至{{ form.endTime }}</span>
      </a-form-model-item>
      <a-form-model-item
        label="活动结束时间"
        prop="endTime"
        hidden="true" />
      <a-form-model-item
        label="优惠券活动类型"
        prop="couponActivityType"
      >
        <a-radio-group v-model="form.couponActivityType" v-if="!showable" :default-value="'registered'">
          <a-radio :value="'registered'">
            注册赠券
          </a-radio>
          <a-radio :value="'specify'">
            精确发券
          </a-radio>
        </a-radio-group>
        <span v-else>
          <span v-if="form.couponActivityType==='registered'">注册赠券</span>
          <span v-else>精确发券</span>
        </span>
      </a-form-model-item>
      <a-form-model-item
        label="发放范围"
        prop="activityScope"
        v-if="form.couponActivityType==='specify'"
      >
        <a-radio-group v-model="form.activityScope" v-if="!showable">
          <a-radio :value="'all'">
            全部会员
          </a-radio>
          <a-radio :value="'designated'">
            指定会员
          </a-radio>
        </a-radio-group>
        <span v-else>
          <span v-if="form.activityScope==='all'">
            全部会员
          </span>
          <span v-else>
            指定会员
          </span>
        </span>
      </a-form-model-item>
      <a-form-model-item
        label="选择会员"
        prop="members"
        v-if="form.couponActivityType==='specify'&&form.activityScope==='designated'&&!showable"
      >
        <a-transfer
          :data-source="memberData"
          show-search
          :filter-option="filterOption"
          :target-keys="targetMemberKeys"
          :render="item => item.title"
          @change="handleMemberChange"
        />
      </a-form-model-item>
    </a-form-model>
    <vxe-table
      v-if="!showable"
      border
      resizable
      :data="couponData"
      @checkbox-change="selectChangeEvent"
      :edit-config="{trigger: 'dblclick', mode: 'cell'}">
      <vxe-table-column type="checkbox" width="60"></vxe-table-column>
      <vxe-table-column field="couponName" title="优惠券名称"></vxe-table-column>
      <vxe-table-column field="consumeThreshold" title="消费门槛"></vxe-table-column>
      <vxe-table-column field="scopeType" title="使用范围"></vxe-table-column>
      <vxe-table-column field="getType" title="获取方式"></vxe-table-column>
      <vxe-table-column field="price" title="面额"></vxe-table-column>
      <vxe-table-column
        field="num"
        title="每人限领"
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
import moment from 'moment'
import { tableObj } from '@/views/modules/mall/promotion/coupon/ActivityTemplate'
import { FormMixin } from '@/mixins/FormMixin'
import { add, get, update } from '@/api/mall/promotion/couponActivity'
import { listMembers } from '@/api/mall/user/member'
import { listCoupon } from '@/api/mall/promotion/coupon'

export default {
  name: 'CouponActivityEdit',
  mixins: [FormMixin],
  data () {
    return {
      tableObj,
      selectCouponData: [],
      couponData: [],
      targetMemberKeys: [],
      memberData: [],
      time: undefined,
      form: {
        id: null,
        promotionName: null,
        startTime: null,
        endTime: null,
        scopeId: null,
        scopeType: null,
        activityScope: null,
        activityScopeInfo: null,
        couponActivityType: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        deleteFlag: null
      },
      rules: {}
    }
  },
  created () {
    this.listMembers()
    this.listCoupons()
  },
  methods: {
    selectChangeEvent ({ checked, records }) {
      if (checked) {
        this.selectCouponData = records
        console.log(records)
      }
    },
    // 查出活动优惠券列表
    listCoupons () {
      const query = { getType: 'activity' }
      listCoupon(query).then(res => {
        this.couponData = res.data
      })
    },
    // 所有列出会员信息
    listMembers () {
      const params = { columns: 'id,nick_name' }
      listMembers(params).then(res => {
        this.memberData = res.data
        this.memberData.forEach(item => {
          item.key = item.id
          item.title = item.nick_name
        })
      })
    },
    filterOption (inputValue, option) {
      return option.title.indexOf(inputValue) > -1
    },
    handleMemberChange (targetKeys) {
      this.targetMemberKeys = targetKeys
    },
    edit (id, type) {
      this.form = {}
      this.memberData = []
      this.couponData = []
      this.selectCouponData = []
      this.listMembers()
      this.listCoupons()
      if (['edit', 'show'].includes(type)) {
        this.confirmLoading = true
        get(id).then(res => {
          this.form = res.data
          console.log('this.form')
          this.confirmLoading = false
        })
      } else {
        this.confirmLoading = false
      }
    },
    handleOk () {
      this.$refs.form.validate(async valid => {
        if (valid) {
          this.selectCouponData.forEach(item => {
            item.couponId = item.id
            item.id = undefined
          })
          this.form.couponActivityItems = this.selectCouponData

          this.form.members = this.memberData.filter(item => this.targetMemberKeys.indexOf(item.key) > -1)

          this.form.startTime = moment(this.time[0]).format('YYYY-MM-DD HH:mm:ss')
          this.form.endTime = moment(this.time[1]).format('YYYY-MM-DD HH:mm:ss')

          this.confirmLoading = true
          if (this.type === 'add') {
            await add(this.form).catch(error => this.$message.error(error.msg))
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
