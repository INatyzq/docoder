<!-- =========================================================================================
  File Name: UserEditTabInformation.vue
  Description: User Edit Information Tab content
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->

<template>
  <div id="user-edit-tab-info">

    <!-- Content Row -->
    <div class="vx-col md:w-3/5 xl:w-3/5 mx-auto self-center">

      <vx-input-group class="m-2">
        <template slot="prepend">
          <div class="prepend-text bg-primary">
            <span>个性签名</span>
          </div>
        </template>
        <vs-input v-model="userFeature.signature"/>
      </vx-input-group>

      <vx-input-group class="m-2">
        <template slot="prepend">
          <div class="prepend-text bg-primary">
            <span>爱好</span>
          </div>
        </template>
        <vs-input v-model="userFeature.tags"/>
      </vx-input-group>

      <vx-input-group class="m-2">
        <template slot="prepend">
          <div class="prepend-text bg-primary">
            <span>标签</span>
          </div>
        </template>
        <vs-input v-model="userFeature.hobby"/>
      </vx-input-group>

    </div>

    <!-- Save & Reset Button -->
    <div class="vx-row">
      <div class="vx-col w-full">
        <div class="mt-8 flex flex-wrap items-center justify-end">
          <vs-button class="ml-auto mt-2" color="success" type="filled" icon-pack="feather" icon="icon-save"
                     @click="save" :disabled="!validateForm">保存
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
import {getRequest, postRequest} from "../../../../../core/http/axiosClient";
import notify from "../../../../../core/notify/notify";
import {FILE_SERVER} from "@/core/utils/appConts";

export default {
  components: {
    vSelect,
    Datepicker
  },
  props: {
    userId: {
      type: String,
      required: true
    }
  },
  created() {
    this.search(this.userId);
  },
  computed: {
    validateForm() {
      return !this.errors.any()
    }
  },
  data() {
    return {
      headers: {},
      languages: lang,
      userFeature: {},
    }
  },
  methods: {
    search(userId) {
      let that = this;
      getRequest('/user/feature/byUserId/' + userId).then((res) => {
        if (res.success) {
          that.userFeature = res.data || {};
        }
      });
    },
    save() {
      /* eslint-disable */
      if (!this.validateForm) return

      this.$vs.loading();
      this.userFeature.userId = this.userId;
      let that = this;
      postRequest('/user/feature', this.userFeature).then((res) => {
        if (res.success) {
          notify.success('保存操作完成！');
          //that.$store.dispatch('auth/refresh');
        }
      });
    }
  }
}
</script>
