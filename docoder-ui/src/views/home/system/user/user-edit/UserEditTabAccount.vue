<!-- =========================================================================================
  File Name: UserEditTabInformation.vue
  Description: User Edit Information Tab content
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
========================================================================================== -->

<template>
  <div id="user-edit-tab-info">

    <!-- Content Row -->
    <div class="vx-row">
      <div class="vx-col md:w-1/3 w-full">
        <vs-input class="w-full mt-4" label="用户名" v-model="data_local.userName" v-validate="'required|alpha_num'"
                  name="userName" readonly/>

        <vs-input data-vv-validate-on="blur" class="w-full mt-4" label="邮箱" v-model="data_local.email" type="email"
                  name="邮箱" readonly/>

        <vs-input class="w-full mt-4" label="昵称" v-model="data_local.nickname" name="昵称"/>

      </div>

      <div class="vx-col md:w-1/3 w-full">

        <vs-input class="w-full mt-4" label="姓名" v-model="data_local.fullName" name="姓名"/>

        <vs-input v-validate="'idCard'" data-vv-validate-on="blur" class="w-full mt-4" label="身份证" v-model="data_local.idCard" name="idCard"/>
        <span class="text-danger text-sm">{{ errors.first('idCard') }}</span>

        <div class="w-full mt-4">
          <vs-row>
            <vs-col vs-w="6">
              <label class="text-sm">性别</label>
              <vs-select class="selectExample" v-model="data_local.sex" name="sex">
                <vs-select-item :key="index" :value="item.value" :text="item.text" v-for="(item,index) in [{text:'男',value:1},{text:'女',value:0}]" />
              </vs-select>
            </vs-col>
            <vs-col vs-w="6">
              <label class="text-sm text-warning">密码</label>
              <vs-input type="password" class="w-full" v-model="password" placeholder="可输入需要修改的密码" name="password"/>
            </vs-col>
          </vs-row>
        </div>

      </div>

      <div class="vx-col md:w-1/3 w-full">

        <div class="w-full mt-4">
          <label class="vs-input--label">生日</label>
          <datepicker :language="language['zh']" format="yyyy-MM-dd" v-model="data_local.birthday"></datepicker>
        </div>

        <vs-input v-validate="'phone'" data-vv-validate-on="blur" class="w-full mt-4" label="手机" v-model="data_local.phone" name="phone"/>
        <span class="text-danger text-sm">{{ errors.first('phone') }}</span>

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
import stringUtil from "@/core/utils/stringUtil";

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
  created(){
    this.search(this.userId);
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
      language: lang,
      data_local: {},
      currentUser:userService.getUserDetail(),
      password:''
    }
  },
  methods: {
    update() {
      /* eslint-disable */
      if (!this.validateForm) return

      this.$vs.loading();
      this.data_local.birthday = moment(this.data_local.birthday).format('YYYY-MM-DD');
      let that = this;
      let id = this.data_local.id;

      if(stringUtil.isNotBlank(this.password)){
        this.data_local.userPwd = this.$md5(this.password)
      }

      postRequest('/user/user', this.data_local).then((res) => {
        if (res.success) {
          notify.success('保存操作完成！');
          this.password = '';
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
