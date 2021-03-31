<template>
  <div :style="{'direction': $vs.rtl ? 'rtl' : 'ltr'}">
    <vs-button color="primary" type="line" icon-pack="feather" icon="icon-settings" @click="openPermissionConfig">权限配置</vs-button>
  </div>
</template>

<script>
    export default {
        name: 'RoleCellRendererActions',
        methods: {
            openPermissionConfig () {
                this.$parent.$parent.active = true;
                this.$parent.$parent.role = this.params.data;
            }
        }
    }
</script>
