<!-- =========================================================================================
  File Name: UserList.vue
  Description: User List page
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->

<template>

  <div id="page-user-list">

    <vx-card ref="filterCard" title="查询" class="user-list-filters mb-8" collapse-action refresh-content-action
             @refresh="queryHelper.refresh(that,'getListPage')">
      <div class="vx-row">
        <div class="vx-col md:w-1/5 sm:w-1/2 w-full">
          <vs-input v-model="queryHelper.userName" placeholder="用户名" label-placeholder="用户名"
                    class="mb-2 md:mb-0"/>
        </div>

        <div class="vx-col md:w-1/5 sm:w-1/2 w-full">
          <vs-input v-model="queryHelper.email" placeholder="邮箱" label-placeholder="邮箱"
                    class="mb-2 md:mb-0"/>
        </div>

        <div class="vx-col md:w-1/5 sm:w-1/2 w-full">
          <vs-input v-model="queryHelper.fullName" placeholder="姓名" label-placeholder="姓名"
                    class="mb-2 md:mb-0"/>
        </div>

        <div class="vx-col md:w-1/5 sm:w-1/2 w-full">
          <vs-input v-model="queryHelper.phone" placeholder="手机号" label-placeholder="手机号"
                    class="mb-2 md:mb-0"/>
        </div>

        <div class="vx-col md:w-1/5 sm:w-1/2 w-full">
          <vs-button icon-pack="feather" icon="icon-search"
                     class="float-left px-4 mt-4" @click="getListPage">查询
          </vs-button>
        </div>
      </div>
    </vx-card>

    <div class="vx-card p-6">

      <!-- AgGrid Table -->
      <ag-grid-vue
        ref="agGridTable"
        :components="components"
        :gridOptions="gridOptions"
        class="ag-theme-material w-100 my-4 ag-grid-table"
        :columnDefs="columnDefs"
        :defaultColDef="defaultColDef"
        :rowData="listPage.records"
        rowSelection="multiple"
        colResizeDefault="shift"
        :animateRows="true"
        :floatingFilter="false"
        :pagination="true"
        :paginationPageSize="queryHelper.size"
        :suppressPaginationPanel="true"
        :enableRtl="$vs.rtl">
      </ag-grid-vue>

      <vs-pagination
        :total="this.listPage.pages||0"
        :max="this.listPage.pages||0"
        v-model="queryHelper.current"
        :prev-icon="$vs.rtl ? 'arrow_forward' : 'arrow_back'"
        :next-icon="$vs.rtl ? 'arrow_back' : 'arrow_forward'"/>

    </div>
  </div>

</template>

<script>
import {AgGridVue} from 'ag-grid-vue'
import '@/assets/scss/vuexy/extraComponents/agGridStyleOverride.scss'
import vSelect from 'vue-select'

// Cell Renderer
import CellRendererLink from './cell-renderer/CellRendererLink.vue'
import CellRendererSex from './cell-renderer/CellRendererSex.vue'
import CellRendererStatus from './cell-renderer/CellRendererStatus.vue'
import CellRendererVerified from './cell-renderer/CellRendererVerified.vue'
import CellRendererActions from './cell-renderer/CellRendererActions.vue'
import {getRequest} from "../../../../../core/http/axiosClient";
import queryHelper from "../../../../../core/utils/queryHelper";


export default {
  components: {
    AgGridVue,
    vSelect,

    // Cell Renderer
    CellRendererLink,
    CellRendererSex,
    CellRendererStatus,
    CellRendererVerified,
    CellRendererActions
  },
  data() {
    return {
      that: this,
      queryHelper: queryHelper,
      listPage: {},

      // AgGrid
      gridApi: null,
      gridOptions: {},
      defaultColDef: {
        sortable: true,
        resizable: true,
        suppressMenu: true
      },
      columnDefs: [
        {
          headerName: 'ID',
          field: 'id',
          width: 125,
          filter: true,
          checkboxSelection: true,
          headerCheckboxSelectionFilteredOnly: true,
          headerCheckboxSelection: true
        },
        {
          headerName: '操作',
          field: 'transactions',
          width: 150,
          cellRendererFramework: 'CellRendererActions'
        },
        {
          headerName: '用户名',
          field: 'userName',
          filter: true,
          width: 210,
          cellRendererFramework: 'CellRendererLink'
        },
        {
          headerName: '昵称',
          field: 'nickname',
          filter: true,
          width: 150
        },
        {
          headerName: '邮箱',
          field: 'email',
          filter: true,
          width: 225
        },
        {
          headerName: '姓名',
          field: 'fullName',
          filter: true,
          width: 200
        },
        {
          headerName: '性别',
          field: 'sex',
          filter: true,
          width: 150,
          cellRendererFramework: 'CellRendererSex'
        },
        /*{
            headerName: '身份证',
            field: 'idCard',
            filter: true,
            width: 150
        },*/
        {
          headerName: '生日',
          field: 'birthday',
          filter: true,
          width: 150
        },
        {
          headerName: '手机',
          field: 'phone',
          filter: true,
          width: 200
        },
        /*{
            headerName: '地址',
            field: 'address',
            filter: true,
            width: 150
        },*/
        {
          headerName: '状态',
          field: 'status',
          width: 150,
          cellRendererFramework: 'CellRendererStatus'
        }
      ],

      // Cell Renderer Components
      components: {
        CellRendererLink,
        CellRendererSex,
        CellRendererStatus,
        CellRendererVerified,
        CellRendererActions
      }
    }
  },
  methods: {
    getListPage() {
      this.$vs.loading();
      let that = this;
      getRequest('/user/listPage', this.queryHelper.getParams()).then(function (res) {
        if (res.success) {
          that.listPage = res.data;
        }
      })
    }
  },
  mounted() {
    this.getListPage();
    this.gridApi = this.gridOptions.api

    /* =================================================================
      NOTE:
      Header is not aligned properly in RTL version of agGrid table.
      However, we given fix to this issue. If you want more robust solution please contact them at gitHub
    ================================================================= */
    if (this.$vs.rtl) {
      const header = this.$refs.agGridTable.$el.querySelector('.ag-header-container')
      header.style.left = `-${String(Number(header.style.transform.slice(11, -3)) + 9)}px`
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
