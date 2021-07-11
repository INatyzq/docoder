<!-- =========================================================================================
  File Name: UserView.vue
  Description: User View page
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->

<template>
  <div id="page-user-view">

    <vs-alert color="danger" title="User Not Found" :active.sync="user_not_found">
      <span>User record with id: {{ $route.params.userId }} not found. </span>
      <span>
        <span>Check </span><router-link :to="{name:'page-user-list'}" class="text-inherit underline">All Users</router-link>
      </span>
    </vs-alert>

    <div id="user-data" v-if="userData">

      <vx-card title="我的账户" class="mb-base">

        <!-- Avatar -->
        <div class="vx-row">

          <!-- Avatar Col -->
          <div class="vx-col" id="avatar-col">
            <div class="img-container mb-4">
              <img :src="userData.avatarUrl" class="rounded w-full" />
            </div>
          </div>

          <!-- Information - Col 1 -->
          <div class="vx-col flex-1" id="account-info-col-1">
            <table>
              <tr>
                <td class="font-semibold">昵称</td>
                <td>{{ userData.nickname }}</td>
              </tr>
              <tr>
                <td class="font-semibold">姓名</td>
                <td>{{ userData.fullName }}</td>
              </tr>
              <tr>
                <td class="font-semibold">邮箱</td>
                <td>{{ userData.email }}</td>
              </tr>
            </table>
          </div>
          <!-- /Information - Col 1 -->

          <!-- Information - Col 2 -->
          <div class="vx-col flex-1" id="account-info-col-2">
            <table>
              <tr>
                <td class="font-semibold">生日</td>
                <td>{{ userData.birthday }}</td>
              </tr>
              <tr>
                <td class="font-semibold">手机</td>
                <td>{{ userData.phone }}</td>
              </tr>
              <tr>
                <td class="font-semibold">状态</td>
                <td>{{ userData.status }}</td>
              </tr>
            </table>
          </div>
          <!-- /Information - Col 2 -->
          <div class="vx-col w-full flex" id="account-manage-buttons">
            <vs-button icon-pack="feather" :icon="isEdit?'icon-x-circle':'icon-edit'" class="mr-4" :color="isEdit?'danger':'primary'"
              @click="isEdit = !isEdit">{{isEdit?'取消编辑':'编辑'}}</vs-button>
          </div>

        </div>
      </vx-card>

      <!--编辑-->
      <vx-card title="编辑账户信息" class="mb-base" v-if="isEdit">
        <UserEdit v-if="userData"/>
      </vx-card>

      <div class="vx-row">
        <div class="vx-col lg:w-2/2 w-full">
          <vx-card title="用户个性" class="mb-base">
            <vs-list>
              <vs-list-header title="个性签名"></vs-list-header>
              <vs-list-item :title="userData.feature.signature"></vs-list-item>

              <vs-list-header title="标签"></vs-list-header>
              <vs-list-item :title="userData.feature.tags"></vs-list-item>

              <vs-list-header title="爱好"></vs-list-header>
              <vs-list-item :title="userData.feature.hobby"></vs-list-item>
            </vs-list>
          </vx-card>
        </div>
      </div>

      <!-- Permissions -->
      <!--<vx-card>

        <div class="vx-row">
          <div class="vx-col w-full">
            <div class="flex items-end px-3">
              <feather-icon svgClasses="w-6 h-6" icon="LockIcon" class="mr-2" />
              <span class="font-medium text-lg leading-none">Permissions</span>
            </div>
            <vs-divider />
          </div>
        </div>

        <div class="block overflow-x-auto">
          <table class="w-full permissions-table">
            <tr>
              &lt;!&ndash;
                You can also use `Object.keys(Object.values(data_local.permissions)[0])` this logic if you consider,
                our data structure. You just have to loop over above variable to get table headers.
                Below we made it simple. So, everyone can understand.
               &ndash;&gt;
              <th class="font-semibold text-base text-left px-3 py-2" v-for="heading in ['Module', 'Read', 'Write', 'Create', 'Delete']" :key="heading">{{ heading }}</th>
            </tr>

            <tr v-for="(val, name) in userData.permissions" :key="name">
              <td class="px-3 py-2">{{ name }}</td>
              <td v-for="(permission, name) in val" class="px-3 py-2" :key="name+permission">
                <vs-checkbox v-model="val[name]" class="pointer-events-none" />
              </td>
            </tr>
          </table>
        </div>

      </vx-card>-->
    </div>
  </div>
</template>

<script>

import {getRequest} from "@/core/http/axiosClient";
import userService from "@/service/userService";
import UserEdit from "@/views/system/system/user/user-edit/UserEdit";

export default {
  components: {UserEdit},
  created() {
    this.getUserDetail();
  },
  data () {
    return {
      userId:'',
      userData: null,
      user_not_found: false,
      isEdit:false
    }
  },
  methods: {
    getUserDetail(){
      this.userId = userService.getUserDetail().id;
      let that = this;
      getRequest('/user/getUserDetail/'+this.userId).then((res) => {
        if (res.success) {
          that.$route.params.userId = res.data.id;
          that.userData = res.data;
        }else{
          that.user_not_found = true;
        }
      });
    }
  },

}

</script>

<style lang="scss">
#avatar-col {
  width: 10rem;
}

#page-user-view {
  table {
    td {
      vertical-align: top;
      min-width: 140px;
      padding-bottom: .8rem;
      word-break: break-all;
    }

    &:not(.permissions-table) {
      td {
        @media screen and (max-width:370px) {
          display: block;
        }
      }
    }
  }
}

// #account-info-col-1 {
//   // flex-grow: 1;
//   width: 30rem !important;
//   @media screen and (min-width:1200px) {
//     & {
//       flex-grow: unset !important;
//     }
//   }
// }


@media screen and (min-width:1201px) and (max-width:1211px),
only screen and (min-width:636px) and (max-width:991px) {
  #account-info-col-1 {
    width: calc(100% - 12rem) !important;
  }

  // #account-manage-buttons {
  //   width: 12rem !important;
  //   flex-direction: column;

  //   > button {
  //     margin-right: 0 !important;
  //     margin-bottom: 1rem;
  //   }
  // }

}

</style>
