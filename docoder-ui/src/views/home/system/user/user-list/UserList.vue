<!-- =========================================================================================
  File Name: UserList.vue
  Description: User List page
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->

<template>

  <div>
    <vx-card ref="filterCard" title="查询" class="user-list-filters mb-8" collapse-action refresh-content-action
             @refresh="queryHelper.refresh(that,'getListPage')">
      <div class="vx-row">
          <vs-input v-model="queryHelper.userName" placeholder="用户名" label-placeholder="用户名"
                    class="ml-2"/>

          <vs-input v-model="queryHelper.email" placeholder="邮箱" label-placeholder="邮箱"
                    class="ml-2"/>

          <vs-input v-model="queryHelper.fullName" placeholder="姓名" label-placeholder="姓名"
                    class="ml-2"/>

          <vs-input v-model="queryHelper.phone" placeholder="手机号" label-placeholder="手机号"
                    class="ml-2"/>

          <vs-button icon-pack="feather" icon="icon-search"
                     class="float-left mt-5 ml-2" @click="getListPage">查询
          </vs-button>
      </div>
    </vx-card>

    <vx-card>
      <vs-table search :data="listPage.records" stripe>
        <template slot="thead">
          <vs-th>操作</vs-th>
          <vs-th sort-key="userName">头像</vs-th>
          <vs-th sort-key="userName">用户名</vs-th>
          <vs-th sort-key="nickname">昵称</vs-th>
          <vs-th sort-key="email">邮箱</vs-th>
          <vs-th sort-key="fullName">姓名</vs-th>
          <vs-th sort-key="sex">性别</vs-th>
          <vs-th sort-key="birthday">生日</vs-th>
          <vs-th sort-key="phone">手机</vs-th>
          <vs-th sort-key="status">状态</vs-th>
        </template>

        <template slot-scope="{data}">
          <vs-tr :key="indextr" v-for="(tr, indextr) in data">

            <vs-td :data="data[indextr].userName">
              <div :style="{'direction': $vs.rtl ? 'rtl' : 'ltr'}">
                <feather-icon icon="Edit3Icon" svgClasses="h-5 w-5 mr-4 hover:text-primary cursor-pointer"
                              @click="editRecord(data[indextr])"/>
                <feather-icon icon="Trash2Icon" svgClasses="h-5 w-5 hover:text-danger cursor-pointer"/>
              </div>
            </vs-td>

            <vs-td :data="data[indextr].avatarUrl">
              <vs-avatar :src="data[indextr].avatarUrl?FILE_SERVER+data[indextr].avatarUrl:defaultAvatar"
                         :onerror="defaultAvatar" class="flex-shrink-0 mr-2" size="30px" @click="$router.push(url)"/>
            </vs-td>

            <vs-td :data="data[indextr].userName">
              {{ data[indextr].userName }}
            </vs-td>

            <vs-td :data="data[indextr].nickname">
              {{ data[indextr].nickname }}
            </vs-td>

            <vs-td :data="data[indextr].email">
              {{ data[indextr].email }}
            </vs-td>

            <vs-td :data="data[indextr].fullName">
              {{ data[indextr].fullName }}
            </vs-td>

            <vs-td :data="data[indextr].sex">
              {{ data[indextr].sex }}
            </vs-td>

            <vs-td :data="data[indextr].birthday">
              {{ data[indextr].birthday }}
            </vs-td>

            <vs-td :data="data[indextr].phone">
              {{ data[indextr].phone }}
            </vs-td>

            <vs-td :data="data[indextr].status">
              <vs-chip class="ag-grid-cell-chip" :color="chipColor(data[indextr].status)">
                <span>{{ data[indextr].status == 0 ? '未激活' : data[indextr].status == 1 ? '已激活' : '注销' }}</span>
              </vs-chip>
            </vs-td>
          </vs-tr>
        </template>
      </vs-table>

      <vx-card>
        <vs-pagination goto
                       :total="this.listPage.pages||0"
                       :max="this.listPage.pages||0"
                       v-model="queryHelper.current"
                       :prev-icon="$vs.rtl ? 'arrow_forward' : 'arrow_back'"
                       :next-icon="$vs.rtl ? 'arrow_back' : 'arrow_forward'"/>
      </vx-card>
    </vx-card>
  </div>

</template>

<script>
import vSelect from 'vue-select'
import {getRequest} from "@/core/http/axiosClient";
import queryHelper from "../../../../../core/utils/queryHelper";
import {FILE_SERVER} from "@/core/utils/appConts";


export default {
  components: {
    vSelect
  },
  mounted() {
    this.getListPage();
  },
  computed: {
    url() {
      return '/apps/user/user-view/268'
    },
    defaultAvatar() {
      return require('../../../../../assets/images/portrait/avatar.jpg');
    },
    chipColor() {
      return (value) => {
        if (value === 1) return 'success'
        else if (value === 3) return 'danger'
        else if (value === 2) return 'warning'
        else return 'primary'
      }
    }
  },
  data() {
    return {
      FILE_SERVER:FILE_SERVER,
      queryHelper: queryHelper,
      listPage: {records: []},
    }
  },
  methods: {
    editRecord(rowData) {
      this.$router.push(`/app/home/system/user/user-edit/${rowData.id}`).catch(() => {
      })
    },
    getListPage() {
      this.$vs.loading();
      let that = this;
      getRequest('/user/listPage', this.queryHelper.getParams()).then(function (res) {
        if (res.success) {
          that.listPage = res.data;
        }
      })
    }
  }
}

</script>

<style lang="scss">
#page-user-list {
  .user-list-filters {
    .vs__actions {
      position: absolute;
      right: 0;
      top: 50%;
      transform: translateY(-58%);
    }
  }
}
</style>
