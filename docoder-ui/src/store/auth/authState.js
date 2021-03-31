/*=========================================================================================
  File Name: moduleAuthState.js
  Description: Auth Module State
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/

let isUserLoggedIn= () => {
  let userInfo = sessionStorage.getItem('userInfo');
  if(!userInfo){
    let userInfoLocal = localStorage.getItem('userInfo');
    if(userInfoLocal){
      sessionStorage.setItem("userInfo",userInfoLocal);
    }
  }
  return JSON.parse(userInfo);
};


const state = {
  userInfo:isUserLoggedIn()
};

export default state;
