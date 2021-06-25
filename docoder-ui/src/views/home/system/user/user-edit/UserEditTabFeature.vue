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

      <div>
        <vx-input-group class="mb-base">
          <template slot="prepend">
            <div class="prepend-text bg-primary">
              <span>个性签名</span>
            </div>
          </template>
          <vs-textarea v-model="userFeature.signature"/>
          <template slot="append">
            <div class="append-text btn-addon">
              <vs-button color="primary" @click="save()">保存</vs-button>
            </div>
          </template>
        </vx-input-group>
      </div>

      <div>
        <vx-input-group class="mb-base xl:w-2/5 mt-1">
          <template slot="prepend">
            <div class="prepend-text bg-primary">
              <span>标签</span>
            </div>
          </template>
          <vs-input v-model="userFeatureInput.tags"/>
          <template slot="append">
            <div class="append-text btn-addon">
              <vs-button color="primary" @click="saveItem('tags')">新增</vs-button>
            </div>
          </template>
        </vx-input-group>

        <div v-if="userFeature.tags" class="demo-alignment" style="margin-top: -40px;margin-bottom: 20px;">
          <vs-chip @click="removeItem('tags',item)" closable color="#24c1a0" close-icon="close" :key="index" :value="item" v-for="(item,index) in userFeature.tags.split('、')">
            {{ item }}
          </vs-chip>
        </div>
      </div>

      <div>
        <vx-input-group class="mb-base xl:w-2/5">
          <template slot="prepend">
            <div class="prepend-text bg-primary">
              <span>爱好</span>
            </div>
          </template>
          <vs-input v-model="userFeatureInput.hobby"/>
          <template slot="append">
            <div class="append-text btn-addon">
              <vs-button color="primary" @click="saveItem('hobby')">新增</vs-button>
            </div>
          </template>
        </vx-input-group>

        <div v-if="userFeature.hobby" class="demo-alignment" style="margin-top: -40px">
          <vs-chip @click="removeItem('hobby',item)" closable color="#24c1a0" close-icon="close" :key="index" :value="item" v-for="(item,index) in userFeature.hobby.split('、')">
            {{ item }}
          </vs-chip>
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
  created() {
    this.search(this.userId);
  },
  data() {
    return {
      headers: {},
      languages: lang,
      userFeature: {},
      userFeatureInput: {},
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
      this.$vs.loading();
      this.userFeature.userId = this.userId;
      postRequest('/user/feature', this.userFeature).then((res) => {
        if (res.success) {
          notify.success('保存操作完成！');
        }
      });
    },
    saveItem(itemName) {
      let val = this.userFeatureInput[itemName];
      if (stringUtil.isBlank(val)) {
        notify.warning('请输入信息！');
        return;
      }
      let items = val.split('、');
      items = items.filter(item => stringUtil.isNotBlank(item));
      let dataVal = '';
      for (let i = 0; i < items.length; i++) {
        let item = items[i];
        if (stringUtil.isBlank(item)) {
          continue;
        }
        dataVal += item;
        if (i < items.length - 1) {
          dataVal += '、';
        }
      }
      this.userFeature[itemName] = this.userFeature[itemName] + (stringUtil.isNotBlank(this.userFeature[itemName]) ? '、' : '') + dataVal

      this.$vs.loading();
      this.userFeature.userId = this.userId;
      let that = this;
      postRequest('/user/feature', this.userFeature).then((res) => {
        if (res.success) {
          notify.success('保存操作完成！');
          that.userFeatureInput[itemName] = '';
        }
      });
    },

    removeItem(itemName,val){
      let dataVal = '、'+this.userFeature[itemName]+'、';
      let remItem = '、'+val+'、'
      let index = dataVal.indexOf(remItem);
      let newVal = dataVal.substring(0,index+1) + dataVal.substring(index+remItem.length);
      newVal = newVal.substring(1,newVal.length-1);
      this.userFeature[itemName] = newVal==='、'?'':newVal;
      postRequest('/user/feature', this.userFeature).then((res) => {
        if (res.success) {
          notify.success('已移除');
        }
      });
    }
  }
}
</script>
