<!-- =========================================================================================
  File Name: UserEditTabInformation.vue
  Description: User Edit Information Tab content
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->

<template>
  <div id="user-edit-tab-info">

    <!-- Avatar Row -->
    <div class="vx-col w-full">
      <div class="flex items-start flex-col sm:flex-row">
        <img ref="userAvatar" :src="userAvatar"
             :onerror="defaultAvatar" class="mr-8 rounded h-24 w-24"/>
        <!-- <vs-avatar :src="data.avatar" size="80px" class="mr-4" /> -->
        <div>
          <p class="text-lg font-medium mb-2 mt-4 sm:mt-0">{{ data_local.nickname||data_local.userName }}</p>
        </div>
      </div>
    </div>

      <!-- Content Row -->
      <div class="vx-row">
        <div class="vx-col md:w-1/2 w-full">
          <vs-input class="w-full mt-4" label="昵称" v-model="data_local.nickName"/>

          <vs-input class="w-full mt-4" label="等级" v-model="userFeature.level" readonly/>


        </div>

        <div class="vx-col md:w-1/2 w-full">

          <vs-input class="w-full mt-4" label="积分" v-model="userFeature.integral" readonly/>

          <vs-input class="w-full mt-4" label="VIP" v-model="userFeature.vip" name="VIP" readonly/>

        </div>

        <div class="vx-col w-full mt-4">
          <vs-textarea v-model="userFeature.hobby" label="爱好"/>

          <vs-textarea v-model="userFeature.sign" label="个性签名"/>
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
import {FILE_SERVER} from "@/core/utils/appConts";

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
  watch: {
    'data'() {
      let imgSrc = '';
      if (this.userFeature.avatarUrl) {
        imgSrc = 'data:image/jpeg;base64,' + this.userFeature.avatarUrl;
        this.$children[0].srcs = [{'src': imgSrc}]
      }
    }
  },
  mounted() {
    this.userFeature.userId = this.userId;
    this.headers.Authorization = this.$store.state.auth.userInfo.token;
  },
  computed: {
    validateForm() {
      return !this.errors.any()
    },
    defaultAvatar() {
      return 'this.src="' + require('../../../../../assets/images/portrait/avatar.jpg') + '"';
    }
  },
  data() {
    return {
      headers: {},
      languages: lang,
      data_local: this.data,
      userFeature: {},
      userId: JSON.parse(JSON.stringify(this.data)).id,
      userAvatar: FILE_SERVER+this.data.avatarUrl,
    }
  },
  methods: {
    successUpload(event) {
      if (JSON.parse(event.currentTarget.response).success) {
        notify.success('头像上传成功！');
        this.$store.dispatch('auth/refresh');
      } else {
        notify.danger('头像上传失败！');
        this.$children[0].srcs = null;
      }
    },
    save_changes() {
      /* eslint-disable */
      if (!this.validateForm) return

      delete this.userFeature.avatarUrl;
      this.$vs.loading();
      let that = this;
      postRequest('/system/userFeature', this.userFeature).then((res) => {
        if (res.success) {
          notify.success('保存操作完成！');
          that.$store.dispatch('auth/refresh');
        }
      });
    }
  }
}
</script>
