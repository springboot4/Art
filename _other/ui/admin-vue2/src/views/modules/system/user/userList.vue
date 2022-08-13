<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item label="用户名">
              <a-input v-model="queryParam.username" placeholder="用户名" />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <a-form-item label="手机号码">
              <a-input v-model="queryParam.mobile" placeholder="手机号码" />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <a-button type="primary" @click="queryPage">查询</a-button>
            <a-button style="margin-left: 8px" @click="resetQuery">重置</a-button>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <f-table
      :columns="tableObj.columns"
      :data="loadData"
      ref="table">
      <template v-slot:buttons>
        <a-button type="primary" icon="plus" @click="add">新建</a-button>
      </template>
      <template v-slot:avatar="{row}">
        <a-avatar size="large" :src="row.avatar" v-if="row.avatar" />
        <a-avatar size="large" icon="user" v-else />
      </template>
      <template v-slot:action="{row}">
        <a href="javascript:" @click="show(row)">查看</a>
        <a-divider type="vertical" />
        <a href="javascript:" @click="edit(row)">编辑</a>
      </template>
    </f-table>

    <!--  添加用户  -->
    <user-add @ok="resetQuery" ref="userAdd" />
    <!-- 编辑用户  -->
    <user-edit @ok="resetQuery" ref="userEdit" />
    <!-- 查看用户详情-->
    <user-show @ok="resetQuery" ref="userShow" />

  </a-card>
</template>

<script>
import UserAdd from '@/views/modules/system/user/UserAdd'
import UserEdit from '@/views/modules/system/user/UserEdit'
import UserShow from '@/views/modules/system/user/UserShow'
import { TableMixin } from '@/mixins/TableMixin'
import { tableObj } from './template'
import { fetchList } from '@/api/sys/user'

export default {
  name: 'UserList',
  components: {
    UserEdit,
    UserShow,
    UserAdd
  },
  mixins: [TableMixin],
  data () {
    return {
      tableObj,
      queryParam: {
        username: '',
        mobile: ''
      },
      loadData: (parameter) => {
        return fetchList(
          Object.assign(parameter, this.queryParam)
        ).then(res => {
          return res.data
        })
      }
    }
  },
  methods: {
    resetQuery () {
      this.queryParam = {}
      this.queryPage()
    },
    show (record) {
      this.$refs.userShow.init(record, 'show')
    },
    edit (record) {
      this.$refs.userEdit.init(record.userId, 'edit')
    },
    add () {
      this.$refs.userAdd.init('', 'add')
    }
  },
  created () {

  }
}
</script>
