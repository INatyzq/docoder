<!-- =========================================================================================
File Name: RegisterJWT.vue
Description: Register Page for JWT
----------------------------------------------------------------------------------------
Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->


<template>
  <div class="clearfix">
    <vs-alert icon="warning" active="true" color="warning" class="my-5" v-if="error">
      <div v-if="error" class="vx-row">
        <strong>{{ error }}</strong>&nbsp;
        <vs-icon color="success" type="flat" icon-pack="feather" icon="icon-refresh-cw" @click="retrySubmit"></vs-icon>
      </div>
      <strong v-if="!error">error</strong>
    </vs-alert>

    <vs-input
      v-validate="'required|alpha_dash|min:6'"
      data-vv-validate-on="blur"
      icon-no-border icon="icon-user" icon-pack="feather"
      label-placeholder="用户名"
      name="userName"
      placeholder="用户名"
      v-model="userName"
      @blur="isRepeat('userName','用户名',userName)"
      class="w-full mt-8"/>
    <span class="text-danger text-sm">{{ errors.first('userName') }}</span>

    <vs-input
      v-validate="'required|email'"
      data-vv-validate-on="blur"
      icon-no-border icon="icon-mail" icon-pack="feather"
      name="email"
      type="email"
      label-placeholder="邮箱"
      @blur="isRepeat('email','邮箱',email)"
      placeholder="邮箱"
      v-model="email"
      class="w-full mt-6"/>
    <span class="text-danger text-sm">{{ errors.first('email') }}</span>

    <vs-input
      ref="password"
      type="password"
      data-vv-validate-on="blur"
      v-validate="'required|min:6|max:10'"
      icon-no-border icon="icon-lock" icon-pack="feather"
      name="userPwd"
      label-placeholder="密码"
      placeholder="密码"
      v-model="userPwd"
      class="w-full mt-6"/>
    <span class="text-danger text-sm">{{ errors.first('userPwd') }}</span>

    <vs-input
      type="password"
      v-validate="'required|min:6|max:10|confirmed:password'"
      data-vv-validate-on="blur"
      icon-no-border icon="icon-lock" icon-pack="feather"
      name="confirm_password"
      label-placeholder="确认密码"
      placeholder="确认密码"
      v-model="confirm_password"
      class="w-full mt-6"/>
    <span class="text-danger text-sm">{{ errors.first('confirm_password') }}</span>

    <vs-row>
      <vs-input
        v-validate="'required'"
        data-vv-validate-on="blur"
        icon-no-border icon="icon-check-circle" icon-pack="feather"
        name="captcha"
        label-placeholder="验证码"
        placeholder="验证码"
        v-model="captcha"
        class="w-50 mt-6"/>

      <vs-chip closable close-icon="refresh" class="w-50 mt-6" style="margin-left: 5%" @click="refreshCaptcha">
        <img :src="captchaImg" class="w-full" style="border-radius:5px;height: 30px;"/>
      </vs-chip>
    </vs-row>
    <vs-row class="text-danger text-sm">{{ errors.first('captcha') }}</vs-row>

    <!--<vs-checkbox v-model="isTermsConditionAccepted" class="mt-6">I accept the terms & conditions.</vs-checkbox>-->
    <vs-button type="border" to="/side/auth/login" class="mt-6">返回登录</vs-button>
    <vs-button class="float-right mt-6" @click="registerUserJWT" :disabled="!validateForm" icon-pack="feather" icon="icon-user-check">注册
    </vs-button>
  </div>
</template>

<script>

import {getRequest, postRequest} from "../../../core/http/axiosClient";
import notify from "../../../core/notify/notify";
import stringUtils from "../../../core/utils/stringUtils";

export default {
  data() {
    return {
      userName: '123456',
      email: '709390364@qq.com',
      userPwd: '123456',
      confirm_password: '123456',
      captcha: '',
      captchaImg: String | ArrayBuffer,
      hasField: false,
      actionCode: '',
      error: ''
    }
  },
  mounted: function () {
    this.registerPrepare();
  },
  computed: {
    validateForm() {
      return !this.errors.any() && this.displayName !== '' && this.email !== '' && this.password !== '' && this.confirm_password !== '' && this.captcha !== '' && !this.error
    }
  },
  methods: {
    registerPrepare() {
      let that = this;
      getRequest('/user/registerPrepare').then(function (res) {
        if (res.success) {
          that.actionCode = res.data.randomCode;
          that.captchaImg = res.data.captchaStr;
        }
      })
    },
    refreshCaptcha() {
      let that = this;
      getRequest('/user/refreshCaptcha/' + this.actionCode).then(function (res) {
        if (res.success) {
          that.actionCode = res.data.randomCode;
          that.captchaImg = res.data.captchaStr;
        }
      })
    },
    isRepeat(field, fieldName, fieldVal) {
      let that = this;
      fieldVal = fieldVal.trim();
      if (stringUtils.isBlank(fieldVal)) {
        return;
      }
      this.hasField = true;
      let form = new Object();
      form[field] = fieldVal;
      postRequest('/user/isRepeat', form).then(function (res) {
        if (res.data) {
          that.$validator.errors.add({'field': field, 'msg': '该' + fieldName + '已存在'});
        }
        that.hasField = false;
      })
    },
    registerUserJWT() {

      // If form is not validated or user is already login return
      //if (!this.validateForm ) return

      this.userPwd = this.$md5(this.userPwd);

      if (!this.actionCode) {
        this.error = '当前页面出错,正在跳转...';
        this.router.push('/side/auth/register');
      }

      const userDetails = {
        userName: this.userName,
        userPwd: this.userPwd,
        email: this.email,
        actionCode: this.actionCode,
        captcha: this.captcha
      }

      let that = this;
      postRequest('/user/register', userDetails).then(function (res) {
        if (res.success) {
          notify.success('注册成功，请登录！');
          that.$router.push('/side/auth/login');
          //that.$router.push('/side/auth/email_confirm/' + that.userName)
        } else {
          that.registerPrepare();
          if(res.isHandle){
            let data = JSON.parse(res.message);
            for(let field in data){
              that.$validator.errors.add({'field': field, 'msg': data[field]});
            }
          }
        }
      })
    },
    retrySubmit() {
      this.registerPrepare();
    }
  }
}
</script>
