<!-- =========================================================================================
  File Name: UserEditTabInformation.vue
  Description: User Edit Information Tab content
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
========================================================================================== -->

<template>
  <div id="user-edit-tab-info">

    <div class="vx-row">

      <div class="vx-col w-full">
        <div class="flex items-start flex-col sm:flex-row">
          <img ref="userAvatar" :src="userAvatar"
               :onerror="defaultAvatar" class="mr-8 rounded h-24 w-24"/>
          <!-- <vs-avatar :src="data.avatar" size="80px" class="mr-4" /> -->
          <div>
            <p class="text-lg font-medium mb-2 mt-4 sm:mt-0">{{ data_local.nickname||data_local.userName }}</p>

            <input type="file" name="file" class="hidden" ref="update_avatar_input" @change="updateAvatar" accept="image/*">

            <!-- Toggle comment of below buttons as one for actual flow & currently shown is only for demo -->
            <!--<vs-button class="mr-4 mb-4">修改</vs-button>-->
            <vs-button type="border" class="mr-4" @click="$refs.update_avatar_input.click()">更换头像</vs-button>

            <!--<vs-button type="border" color="danger">移除</vs-button>-->
          </div>
        </div>
      </div>
    </div>

    <!-- Content Row -->
    <div class="vx-row">
      <div class="vx-col md:w-1/2 w-full">
        <vs-input class="w-full mt-4" label="用户名" v-model="data_local.userName" v-validate="'required|alpha_num'"
                  name="userName" readonly/>

        <vs-input data-vv-validate-on="blur" class="w-full mt-4" label="邮箱" v-model="data_local.email" type="email"
                  name="邮箱" readonly/>

        <vs-input class="w-full mt-4" label="姓名" v-model="data_local.fullName" name="姓名"/>

        <vs-input v-validate="'idCard'" data-vv-validate-on="blur" class="w-full mt-4" label="身份证" v-model="data_local.idCard" name="idCard"/>
        <span class="text-danger text-sm">{{ errors.first('idCard') }}</span>
        <vs-input class="w-full mt-4" label="昵称" v-model="data_local.nickname" name="昵称"/>

      </div>

      <div class="vx-col md:w-1/2 w-full">

        <div class="w-full mt-4">
          <label class="vs-input--label">生日</label>
          <datepicker :language="language['zh']" format="yyyy-MM-dd" v-model="data_local.birthday"></datepicker>
        </div>

        <vs-input v-validate="'phone'" data-vv-validate-on="blur" class="w-full mt-4" label="手机" v-model="data_local.phone" name="phone"/>
        <span class="text-danger text-sm">{{ errors.first('phone') }}</span>

        <div class="w-full mt-4">
          <label class="text-sm">性别</label>
          <div class="mt-2">
            <vs-select v-model="data_local.sex" name="sex">
              <vs-select-item :key="index" :value="item.value" :text="item.text" v-for="(item,index) in [{text:'男',value:1},{text:'女',value:0}]" />
            </vs-select>
          </div>
          <!--<div class="mt-4">
            <vs-radio v-model="data_local.sex" vs-value="1" class="mr-4">男</vs-radio>
            <vs-radio v-model="data_local.sex" vs-value="2" class="mr-4">女</vs-radio>
          </div>-->
        </div>

        <vs-input class="w-full mt-4" label="所在城市" v-model="data_local.address" name="所在城市"/>
      </div>
    </div>

    <!-- Save & Reset Button -->
    <div class="vx-row">
      <div class="vx-col w-full">
        <div class="mt-8 flex flex-wrap items-center justify-end">
          <vs-button class="ml-auto mt-2" color="success" type="filled" icon-pack="feather" icon="icon-save"
                     @click="update" :disabled="!validateForm">保存
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
import moment from 'moment';
import {FILE_SERVER} from '@/core/utils/appConts';
import userService from "@/service/userService";

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
      language: lang,
      data_local: this.data,
      userAvatar: FILE_SERVER+this.data.avatarUrl,
      currentUser:userService.getUserDetail(),
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
    updateAvatar: function () {
      let file = this.$refs.update_avatar_input.files[0];
      let formData = new FormData();
      formData.set('file',file);
      let that = this;
      postRequest("/user/avatar",formData).then((res)=>{
        that.$store.dispatch('auth/refresh');
        that.userAvatar = FILE_SERVER+that.data_local.avatarUrl+"?time="+new Date().getTime();
      })
    },

    update() {
      /* eslint-disable */
      if (!this.validateForm) return

      this.$vs.loading();
      this.data_local.birthday = moment(this.data_local.birthday).format('YYYY-MM-DD');
      let that = this;
      let id = this.data_local.id;
      postRequest('/user/user', this.data_local).then((res) => {
        if (res.success) {
          notify.success('保存操作完成！');
          if(that.currentUser.id===id){
            that.$store.dispatch('auth/refresh');
          }
          that.search(id);
        }
      });
    },

    search(id) {
      let that = this;
      getRequest('/user/'+id).then((res) => {
        if (res.success) {
          that.data_local = res.data;
        }
      });
    }
  }
}
</script>
