<!-- =========================================================================================
    File Name: Login.vue
    Description: Login Page
    ----------------------------------------------------------------------------------------
    Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
      Author: Pixinvent
    Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->


<template>
  <div class="h-screen flex w-full bg-img vx-row no-gutter justify-center items-center">
    <div class="vx-col sm:w-1/2 md:w-1/2 lg:w-2/5 m-4">
      <vx-card>

        <div class="vx-card__title mb-6">
          <h2 class="text-center">账户激活邮件</h2>
        </div>

        <vs-alert icon="warning" active="true" color="warning" class="my-5" v-if="error">
          <strong>{{error}}</strong>
        </vs-alert>

        <img src="@/assets/images/views/auth/account/rocket.png" alt="coming-soon" class="mx-auto mb-2" width="150">
        <div class="count-down text-center" v-if="takeFourNumbers>0">
          <countdown>
            <template slot-scope="props">
              <div class="single-counter">
                <span class="timer">{{ Math.floor(takeFourNumbers/3600)&lt;10?'0'+Math.floor(takeFourNumbers/3600):Math.floor(takeFourNumbers/3600) }}</span>
                <span>小时</span>
              </div>

              <div class="single-counter">
                <span class="timer">{{ Math.floor(takeFourNumbers/60)&lt;10?'0'+Math.floor(takeFourNumbers/60):Math.floor(takeFourNumbers/60) }}</span>
                <span>分钟</span>
              </div>

              <div class="single-counter">
                <span class="timer">{{ Math.floor(takeFourNumbers%60)&lt;10?'0'+Math.floor(takeFourNumbers%60):Math.floor(takeFourNumbers%60) }}</span>
                <span>秒</span>
              </div>
            </template>
          </countdown>
        </div>


        <vs-alert icon="mail" active="true" color="primary" class="my-5" v-if="!error || (error && backendError)">
          如果没有收到账户激活码，请点击重发按钮重新发送激活邮件。
          <a @click="sendActiveMail(1)" href="#">
            <vs-icon color="success" icon-pack="feather" icon="icon-refresh-cw">
              重新发送
            </vs-icon>
          </a>
        </vs-alert>


        <vs-divider position="center">激活码</vs-divider>

        <div class="subscription">
          <vs-input type="text" v-model="activeCode" class="w-full mb-4"/>
          <vs-button class="w-full" color="success" :disabled="validateSubmit" @click="activeAccount">激活</vs-button>
        </div>
      </vx-card>
    </div>
  </div>
</template>


<script>
    import VueCountdown from '@chenfengyuan/vue-countdown'
    import {getRequest} from "../../../core/http/axiosClient";
    import notify from "../../../core/notify/notify";

    export default {
        data() {
            return {
                account: '',
                activeCode: '',
                takeFourNumbers: 0,
                sendTime: '',
                error: '',
                backendError:false
            }
        },
        components: {
            'countdown': VueCountdown
        },
        created() {
            this.account = this.$route.params.account;
            this.sendActiveMail(0)
        },
        computed:{
            validateSubmit(){
                return this.activeCode==='' || (this.error!=='' && !this.backendError);
            }
        },
        methods: {
            sendActiveMail(isSend) {
                if(isSend===1&&this.takeFourNumbers>0){
                    notify.warning('请在指定时间后重发邮件！')
                    return;
                }
                let that = this;
                getRequest('/system/sysUser/sendActiveEmail/' + isSend + '/' + that.account).then(function (data) {
                    that.sendTime = data.data;
                    if (that.sendTime) {
                        that.takeSend();
                    }
                    if (!data.success) {
                        that.error = data.msg;
                    }
                })
            },
            takeSend() {
                let sendTime = this.sendTime = parseInt(this.sendTime);
                this.takeFourNumbers = new Number((sendTime / 1000 + 30 * 60 - new Date().getTime() / 1000).toFixed(0)).valueOf();
                if (this.takeFourNumbers <= 0) {
                    return;
                }
                let that = this;
                let countdownFun = setInterval(function () {
                    if (that.takeFourNumbers <= 0) {
                        clearInterval(countdownFun)
                        that.takeFourNumbers = null;
                    }
                    that.takeFourNumbers--;
                }, 1000)
            },
            activeAccount(){
                let that = this;
                getRequest('/system/sysUser/active/'+this.account+'/'+this.activeCode).then(function (data) {
                    if(data.success){
                        notify.success('激活成功，前往登录')
                        that.$router.push('/side/auth/login');
                    }else{
                        that.error = data.msg;
                        that.backendError = true;
                    }
                })
            }
        }
    }

</script>

