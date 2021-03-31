/*=========================================================================================
  File Name: moduleAuth.js
  Description: Auth Module
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/


import state from './authState.js'
import mutations from './authMutations.js'
import actions from './authActions.js'
import getters from './authGetters.js'

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
}
