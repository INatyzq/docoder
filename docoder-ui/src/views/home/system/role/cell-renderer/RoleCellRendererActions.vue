<template>
  <div :style="{'direction': $vs.rtl ? 'rtl' : 'ltr'}">
    <feather-icon icon="Trash2Icon" svgClasses="h-5 w-5 hover:text-danger cursor-pointer" @click="deleteRecord"/>
  </div>
</template>

<script>
import {postRequest} from "@/core/http/axiosClient";
import notify from "@/core/notify/notify";

export default {
  name: 'RoleCellRendererActions',
  methods: {
    deleteRecord() {
      let ids = [this.params.data.id];
      let me = this.$parent.$parent;
      postRequest('/user/role/deleteBatch',ids).then(function (res){
        if (res.success) {
          me.getListPage();
          notify.success('删除成功！');
        }
      })
    }
  }
}
</script>
