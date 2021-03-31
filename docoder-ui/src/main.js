/*=========================================================================================
  File Name: main.js
  Description: main vue(js) file
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/


import Vue from 'vue'
import App from './App.vue'
require('./core/directives/globalDirective');

// Vuesax Component Framework
import Vuesax from 'vuesax'
import 'material-icons/iconfont/material-icons.css' //Material Icons
import 'vuesax/dist/vuesax.css'; // Vuesax
Vue.use(Vuesax)


// axios
import axios from "./axios.js"
Vue.prototype.$http = axios

// axios
import md5 from "js-md5"
Vue.prototype.$md5 = md5


// Theme Configurations
import '../themeConfig.js'


// Globally Registered Components
import './globalComponents.js'


// Styles: SCSS
import './assets/scss/main.scss'


// Tailwind
import '@/assets/css/main.css'


// Vue Router
import router from './router'


// Vuex Store
import store from './store/store'


// Vuejs - Vue wrapper for hammerjs
import { VueHammer } from 'vue2-hammer'
Vue.use(VueHammer)


// PrismJS
import 'prismjs'
import 'prismjs/themes/prism-tomorrow.css'


// Feather font icon
require('./assets/css/iconfont.css')

// VeeValidate
require('./core/vee-validate/validate')

// Tour
import VueTour from 'vue-tour'
Vue.use(VueTour)
require('vue-tour/dist/vue-tour.css')

// Vue select css
// Note: In latest version you have to add it separately
 import 'vue-select/dist/vue-select.css';

import moment from 'vue-moment';
Vue.use(moment);


Vue.config.productionTip = false

let appVue = new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
export default appVue;
