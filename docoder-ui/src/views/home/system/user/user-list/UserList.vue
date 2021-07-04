<!-- =========================================================================================
  File Name: UserList.vue
  Description: User List page
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->

<template>

  <div>
    <vx-card ref="filterCard" title="查询" class="user-list-filters mb-8" collapse-action refresh-content-action
             @refresh="queryHelper.refresh(that,'getPage')">
      <div class="vx-row">
          <vs-input v-model="queryHelper.userName" placeholder="用户名" label-placeholder="用户名"
                    class="ml-2"/>

          <vs-input v-model="queryHelper.email" placeholder="邮箱" label-placeholder="邮箱"
                    class="ml-2"/>

          <vs-input v-model="queryHelper.fullName" placeholder="姓名" label-placeholder="姓名"
                    class="ml-2"/>

          <vs-input v-model="queryHelper.phone" placeholder="手机号" label-placeholder="手机号"
                    class="ml-2"/>

          <vs-button icon-pack="feather" icon="icon-search"
                     class="float-left mt-5 ml-2" @click="getPage">查询
          </vs-button>
      </div>
    </vx-card>

    <vx-card>

      <div class="vx-row p-2">
        <vs-col vs-type="flex" vs-justify="center" vs-align="center" vs-w="1">
          <vs-button @click="actionRecords('启用')" class="mr-2" color="success" type="filled"
                     icon-pack="feather" icon="icon-unlock">启用
          </vs-button>
        </vs-col>
        <vs-col vs-type="flex" vs-justify="center" vs-align="center" vs-w="1">
          <vs-button @click="actionRecords('禁用')" class="mr-2" color="warning" type="filled"
                     icon-pack="feather" icon="icon-lock">禁用
          </vs-button>
        </vs-col>
        <vs-col vs-type="flex" vs-justify="center" vs-align="center" vs-w="1">
          <vs-button @click="actionRecords('删除')" class="mr-2" color="danger" type="filled"
                     icon-pack="feather" icon="icon-trash-2">删除
          </vs-button>
        </vs-col>
      </div>

      <vs-table multiple v-model="selected" :data="listPage.records" stripe>
        <template slot="thead">
          <vs-th>操作</vs-th>
          <vs-th sort-key="userName">头像</vs-th>
          <vs-th sort-key="userName">用户名</vs-th>
          <vs-th sort-key="nickname">昵称</vs-th>
          <vs-th sort-key="email">邮箱</vs-th>
          <vs-th sort-key="fullName">姓名</vs-th>
          <vs-th sort-key="sex">性别</vs-th>
          <vs-th sort-key="birthday">生日</vs-th>
          <vs-th sort-key="phone">手机</vs-th>
          <vs-th sort-key="status">状态</vs-th>
        </template>

        <template slot-scope="{data}">
          <vs-tr :data="row" :key="indextr" v-for="(row, indextr) in data">

            <vs-td :data="row.userName">
              <div :style="{'direction': $vs.rtl ? 'rtl' : 'ltr'}">
                <feather-icon icon="Edit3Icon" title="编辑" svgClasses="text-primary h-5 w-5 mr-4 hover:text-primary cursor-pointer"
                              @click="editRecord(row)"/>
                <feather-icon v-if="row.status===1" icon="LockIcon" title="禁用" svgClasses="text-warning h-5 w-5 mr-4 hover:text-primary cursor-pointer"
                              @click="actionRecords('禁用',[row.id])"/>
                <feather-icon v-if="row.status===2" icon="UnlockIcon" title="启用" svgClasses="text-success h-5 w-5 mr-4 hover:text-primary cursor-pointer"
                              @click="actionRecords('启用',[row.id])"/>
                <feather-icon icon="Trash2Icon" title="删除" svgClasses="text-danger h-5 w-5 mr-4 hover:text-danger cursor-pointer"
                              @click="actionRecords('删除',[row.id])"/>
              </div>
            </vs-td>

            <vs-td :data="row.avatarUrl">
              <vs-avatar :src="row.avatarUrl?FILE_SERVER+row.avatarUrl:defaultAvatar"
                         :onerror="defaultAvatar" class="flex-shrink-0 mr-2" size="30px" @click="$router.push(url)"/>
            </vs-td>

            <vs-td :data="row.userName">
              {{ row.userName }}
            </vs-td>

            <vs-td :data="row.nickname">
              {{ row.nickname }}
            </vs-td>

            <vs-td :data="row.email">
              {{ row.email }}
            </vs-td>

            <vs-td :data="row.fullName">
              {{ row.fullName }}
            </vs-td>

            <vs-td :data="row.sex">
              {{ row.sex }}
            </vs-td>

            <vs-td :data="row.birthday">
              {{ row.birthday }}
            </vs-td>

            <vs-td :data="row.phone">
              {{ row.phone }}
            </vs-td>

            <vs-td :data="row.status">
              <vs-chip class="ag-grid-cell-chip" :color="chipColor(row.status)">
                <span>{{ row.status == 0 ? '未激活' : row.status == 1 ? '已激活' : '禁用' }}</span>
              </vs-chip>
            </vs-td>
          </vs-tr>
        </template>
      </vs-table>

      <vx-card>
        <vs-pagination goto
                       :total="this.listPage.pages||0"
                       :max="this.listPage.pages||0"
                       v-model="queryHelper.current"
                       :prev-icon="$vs.rtl ? 'arrow_forward' : 'arrow_back'"
                       :next-icon="$vs.rtl ? 'arrow_back' : 'arrow_forward'"/>
      </vx-card>
    </vx-card>
  </div>

</template>

<script>
import vSelect from 'vue-select'
import {getRequest, postRequest} from "@/core/http/axiosClient";
import queryHelper from "../../../../../core/utils/queryHelper";
import {FILE_SERVER} from "@/core/utils/appConts";
import notify from "@/core/notify/notify";
import dialog from "@/core/notify/dialog";


export default {
  components: {
    vSelect
  },
  mounted() {
    this.getPage();
  },
  watch: {
    'queryHelper.current'() {
      this.getPage();
    }
  },
  computed: {
    url() {
      return '/apps/user/user-view/268'
    },
    defaultAvatar() {
      return require('../../../../../assets/images/portrait/avatar.jpg');
    },
    chipColor() {
      return (value) => {
        if (value === 1) return 'success'
        else if (value === 0) return 'warning'
        else if (value === 2) return 'danger'
        else return 'primary'
      }
    }
  },
  data() {
    return {
      FILE_SERVER:FILE_SERVER,
      queryHelper: queryHelper.new(),
      listPage: {records: []},
      selected:[]
    }
  },
  methods: {
    editRecord(rowData) {
      this.$router.push(`/app/home/system/user/user-edit/${rowData.id}`).catch(() => {
      })
    },
    getPage() {
      this.$vs.loading();
      let that = this;
      getRequest('/user/listPage', this.queryHelper.getParams()).then(function (res) {
        if (res.success) {
          that.listPage = res.data;
        }
      })
    },
    actionRecords(action,ids){
      ids = ids?ids:this.selected.map(data => data.id);
      if (ids.length === 0) {
        notify.warning('请至少选择一条数据！');
        return;
      }
      let actionFlag = {'msgTitle':action,'url':action==='启用'?'ableByIds':action==='禁用'?'disableByIds':'deleteByIds'}
      let that = this;
      dialog.confirm('确定'+actionFlag.msgTitle+'选中的记录吗？',function (){
        that.$vs.loading();
        postRequest('/user/'+actionFlag.url, ids).then(function (res) {
          if (res.success) {
            that.getPage();
            notify.success('操作已完成！')
            that.selected = [];
          }
        })
      })
    }
  }
}

</script>

<style lang="scss">
#page-user-list {
  .user-list-filters {
    .vs__actions {
      position: absolute;
      right: 0;
      top: 50%;
      transform: translateY(-58%);
    }
  }
}
</style>
