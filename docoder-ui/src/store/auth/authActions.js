/*=========================================================================================
  File Name: moduleAuthActions.js
  Description: Auth Module Actions
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/

import {getRequest, postRequest} from "../../core/http/axiosClient";
import notify from "../../core/notify/notify";
import userService from "@/service/userService";

export default {

  loginJWT({commit}, payload) {

    return new Promise((resolve, reject) => {
      postRequest(payload.url, payload.userDetails).then(function (res) {
        if (res.success) {
          notify.success('欢迎回来：' + res.data.userName, {'position': 'top-right', 'icon': 'icon-heart'});
          // Update user details
          commit('auth/UPDATE_USER_INFO', res.data, {root: true})
        }
        resolve(res)
      })
        .catch(error => reject(error))
    })
  },

  logout({commit}) {
    commit('auth/UPDATE_USER_INFO', null, {root: true})
  },

  refresh({commit}){
    let userDetail = userService.getUserDetail();
    if(!userDetail){
      notify.warning('刷新失败：用户信息获取失败！');
      return false;
    }
    getRequest('/user/refresh/'+userDetail.id).then(res=>{
      if(res.success){
        let userDetail = res.data;
        commit('auth/UPDATE_USER_INFO', userDetail, {root: true})
      }else{
        notify.danger('用户信息刷新失败：请联系管理员！');
      }
    });
  }

}
