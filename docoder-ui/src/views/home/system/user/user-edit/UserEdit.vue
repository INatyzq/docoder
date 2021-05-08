<!-- =========================================================================================
  File Name: UserEdit.vue
  Description: User Edit Page
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->

<template>
  <div id="page-user-edit">

    <vs-alert color="danger" title="用户不存在" :active.sync="user_not_found">
      <span>id为: {{ $route.params.userId }} 的用户没有找到。 </span>
    </vs-alert>

    <vx-card v-if="user_data">

      <div slot="no-body" class="tabs-container px-6 pt-6">

        <vs-tabs v-model="activeTab" class="tab-action-btn-fill-conatiner">
          <vs-tab label="账户详情" icon-pack="feather" icon="icon-user">
            <div class="tab-text">
              <user-edit-tab-account class="mt-4" :data="user_data"/>
            </div>
          </vs-tab>
          <vs-tab label="社交个性" icon-pack="feather" icon="icon-user">
            <div class="tab-text">
              <user-edit-tab-feature class="mt-4" :data="user_data"/>
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
    import {getRequest} from "../../../../../core/http/axiosClient";
    import UserEditTabFeature from "./UserEditTabFeature";
    import UserEditTabRole from "./UserEditTabRole";
    import UserEditTabPasswordReset from "./UserEditTabPasswordReset";

    export default {
        components: {
            /*UserEditTabRole,*/
            UserEditTabFeature,
            UserEditTabAccount,
            /*UserEditTabPasswordReset*/
        },
        data() {
            return {
                user_data: null,
                user_not_found: false,

                /*
                  This property is created for fetching latest data from API when tab is changed

                  Please check it's watcher
                */
                activeTab: 0
            }
        },
        watch: {
            activeTab() {
                this.fetch_user_data(this.$route.params.userId)
            }
        },
        methods: {
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
                    } else {
                        that.user_not_found = true;
                    }
                });
            }
        },
        created() {
            this.fetch_user_data(this.$route.params.userId)
        }
    }

</script>
