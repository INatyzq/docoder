<!-- =========================================================================================
    File Name: ResetPassword.vue
    Description: Reset Password Page
    ----------------------------------------------------------------------------------------
    Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
      Author: Pixinvent
    Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->


<template>
  <div class="flex w-full">
    <div class="vx-col sm:w-3/5 md:w-3/5 lg:w-3/4 xl:w-3/5 mx-auto self-center">
      <div slot="no-body" class="full-page-bg-color">
        <div class="vx-row">
          <div class="vx-col sm:w-full md:w-full lg:w-1/2 mx-auto self-center  d-theme-dark-bg">
            <div class="p-8">
              <vs-input type="password" data-vv-validate-on="blur"
                        v-validate="'required|min:6|max:10'" name="旧密码"
                        icon-no-border icon="icon icon-lock" icon-pack="feather"
                        label-placeholder="旧密码" v-model="oldPwd"
                        class="w-full mt-6"/>
              <span class="text-danger text-sm">{{ errors.first('旧密码') }}</span>

              <vs-input type="password" data-vv-validate-on="blur"
                        v-validate="'required|min:6|max:10'" name="新密码"
                        icon-no-border icon="icon icon-lock" icon-pack="feather"
                        label-placeholder="新密码" v-model="newPwd"
                        class="w-full mt-6"/>
              <span class="text-danger text-sm">{{ errors.first('新密码') }}</span>

              <div class="flex flex-wrap justify-between flex-col-reverse sm:flex-row mt-6">
                <vs-button class="ml-auto" color="success"  type="filled" icon-pack="feather" icon="icon-save" @click="save_changes" :disabled="!validateForm">保存</vs-button>
              </div>

            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
    import {postRequest} from "../../../../../core/http/axiosClient";
    import notify from "../../../../../core/notify/notify";

    export default {
        data() {
            return {
                oldPwd: '',
                newPwd: ''
            }
        },
        computed:{
            validateForm() {
                return !this.errors.any() && this.oldPwd && this.newPwd;
            }
        },
        methods:{
            save_changes(){
                let that = this;
                postRequest('/system/sysUser/resetPassword',{'oldPwd':this.$md5(this.oldPwd),'newPwd':this.$md5(this.newPwd)}).then(res=>{
                    if(res.success){
                        notify.success('密码重置成功，请重新登录！');
                        that.$router.push('/side/auth/login');

                    }else{
                        that.$validator.errors.add({'field': '旧密码', 'msg': res.msg});
                    }
                })
            }
        }
    }
</script>
