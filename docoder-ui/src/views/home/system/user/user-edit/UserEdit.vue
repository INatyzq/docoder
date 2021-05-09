<!-- =========================================================================================
  File Name: UserEdit.vue
  Description: User Edit Page
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->

<template>
  <div id="page-user-edit">

    <vs-alert color="danger" title="用户不存在" :active.sync="user_not_found">
      <span>id为: {{ $route.params.userId }} 的用户没有找到。 </span>
    </vs-alert>

    <vx-card v-if="user_data">

      <div slot="no-body" class="tabs-container px-6 pt-6">

        <!-- Avatar Row -->
        <div class="vx-row mt-2">
          <div class="vx-col w-full">
            <div class="flex items-start flex-col sm:flex-row">
              <img ref="userAvatar" :src="userAvatar"
                   :onerror="defaultAvatar" class="mr-8 rounded h-24 w-24"/>
              <div>
                <p class="text-lg font-medium mb-2 mt-4 sm:mt-0">{{ user_data.nickname||user_data.userName }}</p>
                <input type="file" name="file" class="hidden" ref="update_avatar_input" @change="updateAvatar" accept="image/*">
                <vs-button type="border" class="mr-4" @click="$refs.update_avatar_input.click()">更换头像</vs-button>
              </div>
            </div>
          </div>
        </div>

        <vs-tabs v-model="activeTab" class="tab-action-btn-fill-conatiner mt-4">
          <vs-tab label="账户详情" icon-pack="feather" icon="icon-user">
            <div class="tab-text">
              <user-edit-tab-account class="mt-4" :user-id="userId"/>
            </div>
          </vs-tab>
          <vs-tab label="社交个性" icon-pack="feather" icon="icon-user">
            <div class="tab-text">
              <user-edit-tab-feature class="mt-4" :user-id="userId"/>
            </div>
          </vs-tab>
          <!--<vs-tab v-has-permission id="role" label="角色配置" icon-pack="feather" icon="icon-user">
            <div class="tab-text">
              <user-edit-tab-role class="mt-4" :data="user_data"/>
            </div>
          </vs-tab>
          <vs-tab label="密码重置" icon-pack="feather" icon="icon-user">
            <div class="tab-text">
              <user-edit-tab-password-reset class="mt-4" :data="user_data"/>
            </div>
          </vs-tab>-->
        </vs-tabs>

      </div>

    </vx-card>
  </div>
</template>

<script>
import UserEditTabAccount from './UserEditTabAccount.vue'
import {getRequest, postRequest} from "../../../../../core/http/axiosClient";
import UserEditTabFeature from "./UserEditTabFeature";
import UserEditTabRole from "./UserEditTabRole";
import UserEditTabPasswordReset from "./UserEditTabPasswordReset";
import {FILE_SERVER} from "@/core/utils/appConts";

export default {
  components: {
    /*UserEditTabRole,*/
    UserEditTabFeature,
    UserEditTabAccount,
    /*UserEditTabPasswordReset*/
  },
  created() {
    this.fetch_user_data(this.$route.params.userId)
  },
  watch: {
    activeTab() {
      //this.fetch_user_data(this.$route.params.userId)
    }
  },
  computed:{
    defaultAvatar() {
      return 'this.src="' + require('../../../../../assets/images/portrait/avatar.jpg') + '"';
    }
  },
  data() {
    return {
      user_not_found: false,
      userId:this.$route.params.userId,
      user_data:{},
      userAvatar: FILE_SERVER+this.$route.params.avatarUrl,
      activeTab: 0,
    }
  },
  methods: {
    updateAvatar: function () {
      let oldFileName = this.userAvatar.substring(this.userAvatar.lastIndexOf('/'));

      let file = this.$refs.update_avatar_input.files[0];
      let formData = new FormData();
      formData.set('file',file);
      formData.set('userId',this.userId);
      formData.set('oldFileName',oldFileName);
      let that = this;
      postRequest("/user/avatar",formData).then((res)=>{
        that.$store.dispatch('auth/refresh');
        that.userAvatar = FILE_SERVER+res.data+"?time="+new Date().getTime();
      })
    },
    fetch_user_data(userId) {
      if (!userId) {
        this.user_not_found = true;
        return;
      }
      this.$vs.loading();
      let that = this;
      getRequest('/user/' + userId).then(function (res) {
        if (res.success) {
          that.user_data = res.data;
          that.userAvatar = FILE_SERVER + res.data.avatarUrl
        } else {
          that.user_not_found = true;
        }
      });
    }
  }
}

</script>
