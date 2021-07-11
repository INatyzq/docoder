<template>
  <div class="flex items-center">
    <vs-avatar :src="params.data.avatarUrl?FILE_SERVER+params.data.avatarUrl:defaultAvatar" :onerror="defaultAvatar" class="flex-shrink-0 mr-2" size="30px" @click="$router.push(url)" />
    <router-link :to="url" @click.stop.prevent class="text-inherit hover:text-primary">{{ params.value }}</router-link>
  </div>
</template>

<script>
import {FILE_SERVER} from "@/core/utils/appConts";

export default {
  name: 'CellRendererLink',
  computed: {
    url () {
      return '/apps/user/user-view/268'

      // Below line will be for actual product
      // Currently it's commented due to demo purpose - Above url is for demo purpose
      // return "/apps/user/user-view/" + this.params.data.id
    },
    defaultAvatar() {
      return require('../../../../../../assets/images/portrait/avatar.jpg');
    }
  },
  data(){
    return {
      FILE_SERVER:FILE_SERVER
    }
  }
}
</script>
