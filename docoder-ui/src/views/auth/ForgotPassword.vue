<!-- =========================================================================================
    File Name: ForgotPassword.vue
    Description: FOrgot Password Page
    ----------------------------------------------------------------------------------------
    Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
      Author: Pixinvent
    Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->


<template>
  <div class="h-screen flex w-full bg-img">
    <div class="vx-col w-4/5 sm:w-4/5 md:w-3/5 lg:w-3/4 xl:w-3/5 mx-auto self-center">
      <vx-card>
        <div slot="no-body" class="full-page-bg-color">
          <div class="vx-row">
            <div class="vx-col hidden sm:hidden md:hidden lg:block lg:w-1/2 mx-auto self-center">
              <img src="@/assets/images/views/auth/forgot-password.png" alt="login" class="mx-auto">
            </div>
            <div class="vx-col sm:w-full md:w-full lg:w-1/2 mx-auto self-center d-theme-dark-bg">
              <div class="p-8">
                <div class="vx-card__title mb-8">
                  <h4 class="mb-4">重置您的密码</h4>
                  <p>请输入您的电子邮件地址，我们将向您发送有关重设密码的确认码。</p>
                </div>

                <vs-row class="mb-4">
                  <vs-alert icon="warning" active="true" color="warning" class="my-5" v-if="error">
                    <strong>{{error}}</strong>
                    <strong v-if="!error">占位</strong>
                  </vs-alert>
                  <vs-col vs-w="8">
                    <vs-input type="email" label-placeholder="邮箱地址" name="邮箱地址"
                              v-validate="'required|email'" data-vv-validate-on="blur"
                              icon-no-border icon="icon-mail" icon-pack="feather"
                              class="w-full" v-model="email"/>
                    <span class="text-danger text-sm">{{ errors.first('邮箱地址') }}</span>
                  </vs-col>
                  <vs-col vs-w="4">
                    <vs-button type="line" line-origin="right" class="ml-auto mt-4" icon-pack="feather"
                               icon="icon-send" :disabled="errors.first('邮箱地址')||!email" @click="sendEmail">发送
                    </vs-button>
                  </vs-col>
                </vs-row>

                <div v-if="isSendMail">
                  <vs-input ref="password" type="password" label-placeholder="新密码" name="userPwd"
                            data-vv-validate-on="blur" v-validate="'required|min:6|max:10'"
                            icon-no-border icon="icon-lock" icon-pack="feather"
                            class="w-full mt-8" v-model="userPwd"/>
                  <span class="text-danger text-sm">{{ errors.first('userPwd') }}</span>

                  <vs-input type="password" label-placeholder="新密码确认" name="confirm_password"
                            data-vv-validate-on="blur" v-validate="'required|min:6|max:10|confirmed:password'"
                            icon-no-border icon="icon-lock" icon-pack="feather"
                            class="w-full mt-8" v-model="confirm_password"/>
                  <span class="text-danger text-sm">{{ errors.first('confirm_password') }}</span>

                  <vs-input label-placeholder="重置确认码" class="w-full mt-8" name="确认码"
                            data-vv-validate-on="blur" v-validate="'required'"
                            icon-no-border icon="icon-code"
                            icon-pack="feather" v-model="resetCode"/>
                  <span class="text-danger text-sm">{{ errors.first('确认码') }}</span>
                </div>

                <vs-button type="border" to="/side/auth/login" class="px-4 w-full md:w-auto mt-4">返回登录</vs-button>
                <vs-button v-if="isSendMail" icon-pack="feather" icon="icon-check-circle"
                           class="float-right px-4 w-full md:w-auto mt-4" @click="resetPassword" :disabled="submitValidate">确认重置
                </vs-button>
              </div>
            </div>
          </div>
        </div>
      </vx-card>
    </div>
  </div>
</template>

<script>
    import {getRequest, postRequest} from "../../core/http/axiosClient";
    import notify from "../../core/notify/notify";

    export default {
        data() {
            return {
                error:'',
                isSendMail:true,
                email:'',
                userPwd:'',
                confirm_password:'',
                resetCode:''
            }
        },
        computed:{
            submitValidate(){
                return this.errors.has('email')||this.errors.has('userPwd')||this.errors.has('confirm_password')||this.errors.has('resetCode')||!this.email||!this.userPwd||!this.confirm_password||!this.resetCode
            }
        },
        methods:{
            sendEmail(){
                let that = this;
                getRequest('/system/sysUser/sendEmailResetPassword',{'email':this.email}).then(res=>{
                    if(res.success){
                        notify.success('密码重置邮件已发送！');
                        that.isSendMail = true;
                    }else{
                        notify.warning(res.msg)
                    }
                });
            },
            resetPassword(){
               let that = this;
               postRequest('/system/sysUser/forgetPassword',{'email':this.email,'userPwd':this.$md5(this.userPwd),'resetCode':this.resetCode}).then(res=>{
                   if(res.success){
                       notify.success('密码重置成功！');
                       that.$router.push('/side/auth/login');
                   }else{
                       that.error = res.msg;
                   }
               });
            }
        }
    }
</script>
