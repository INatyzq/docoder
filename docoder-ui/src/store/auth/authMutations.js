/*=========================================================================================
  File Name: moduleAuthMutations.js
  Description: Auth Module Mutations
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/


export default {

  // /////////////////////////////////////////////
  // User/Account
  // /////////////////////////////////////////////

  // Updates user info in state and localstorage
  UPDATE_USER_INFO(state, payload) {
    if(payload){
      if (payload.rememberMe) {
        localStorage.setItem('userInfo', JSON.stringify(payload));
        sessionStorage.setItem('userInfo', JSON.stringify(payload));
      } else {
        sessionStorage.setItem('userInfo', JSON.stringify(payload));
      }
      state.userInfo = payload;
    }else{
      sessionStorage.clear();
      localStorage.clear();
    }
  },

}
