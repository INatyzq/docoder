import Vue from 'vue'

Vue.directive('hasPermission',{
  bind:function (el) {
    el.style.display = 'none';
  }
});
