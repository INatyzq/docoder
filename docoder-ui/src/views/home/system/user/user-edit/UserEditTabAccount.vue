<!-- =========================================================================================
  File Name: UserEditTabInformation.vue
  Description: User Edit Information Tab content
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->

<template>
  <div id="user-edit-tab-info">

    <!-- Avatar Row -->
    <div class="vx-row">
      <div class="vx-col w-full">
        <div class="flex items-start flex-col sm:flex-row">
          <img :src="data_local.userFeature.avatarUrl?'data:image/jpeg;base64,'+data_local.userFeature.avatarUrl:''"
               :onerror="defaultAvatar" class="mr-8 rounded h-24 w-24"/>
        </div>
      </div>
    </div>

    <!-- Content Row -->
    <div class="vx-row">
      <div class="vx-col md:w-1/2 w-full">
        <vs-input class="w-full mt-4" label="用户名" v-model="data_local.userName" v-validate="'required|alpha_num'"
                  name="userName" readonly/>

        <vs-input class="w-full mt-4" label="邮箱" v-model="data_local.email" type="email" v-validate="'required|email'"
                  name="邮箱" readonly/>

        <vs-input class="w-full mt-4" label="姓名" v-model="data_local.fullName" name="姓名"/>

        <vs-input class="w-full mt-4" label="身份证" v-model="data_local.idCard" name="身份证"/>

      </div>

      <div class="vx-col md:w-1/2 w-full">

        <vs-list class="w-full mt-2">
          <label class="vs-input--label">生日</label>
          <datepicker :language="languages['zh']" format="yyyy-MM-dd" v-model="data_local.birthday"></datepicker>
        </vs-list>

        <vs-input class="w-full mt-4" label="手机" v-model="data_local.phone" name="手机"/>

        <vs-list class="w-full mt-4">
          <label class="vs-input--label">性别</label>
          <vs-row class="mt-2">
            <vs-radio color="rgb(87, 251, 187)" v-model="data_local.sex" vs-value="1">男</vs-radio>
            <vs-radio color="#e48346" v-model="data_local.sex" vs-value="2" class="ml-4">女</vs-radio>
          </vs-row>
        </vs-list>

        <vs-input class="w-full mt-4" label="所在城市" v-model="data_local.address" name="所在城市"/>
      </div>
    </div>

    <!-- Save & Reset Button -->
    <div class="vx-row">
      <div class="vx-col w-full">
        <div class="mt-8 flex flex-wrap items-center justify-end">
          <vs-button class="ml-auto mt-2" color="success" type="filled" icon-pack="feather" icon="icon-save"
                     @click="save_changes" :disabled="!validateForm">保存
          </vs-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
    import vSelect from 'vue-select';
    import Datepicker from 'vuejs-datepicker';
    import * as lang from 'vuejs-datepicker/src/locale';
    import {postRequest} from "../../../../../core/http/axiosClient";
    import notify from "../../../../../core/notify/notify";
    import moment from 'moment';

    export default {
        components: {
            vSelect,
            Datepicker
        },
        props: {
            data: {
                type: Object,
                required: true
            }
        },
        data() {
            return {
                languages: lang,
                data_local: JSON.parse(JSON.stringify(this.data)),
            }
        },
        computed: {
            validateForm() {
                return !this.errors.any()
            },
            defaultAvatar() {
                return 'this.src="' + require('../../../../../assets/images/portrait/avatar.jpg') + '"';
            }
        },
        methods: {
            save_changes() {
                /* eslint-disable */
                if (!this.validateForm) return

                this.$vs.loading();
                delete this.data_local.userFeature;
                this.data_local.birthday = moment(this.data_local.birthday).format('YYYY-MM-DD');
                let that = this;
                postRequest('/system/sysUser', this.data_local).then((res) => {
                    if (res.success) {
                        notify.success('保存操作完成！');
                        that.$store.dispatch('auth/refresh');
                    }
                });
            },
            reset_data() {
                this.data_local = JSON.parse(JSON.stringify(this.data))
            }
        }
    }
</script>
