<template>
  <div>
    <div>
      <vx-card ref="filterCard" title="查询" class="user-list-filters mb-8" collapse-action refresh-content-action
               @refresh="queryHelper.refresh(that,'getListPage')">
        <div class="vx-row">
          <vs-input v-model="queryHelper.roleName" placeholder="角色名称" label-placeholder="角色名称"
                    class="ml-2"/>

          <vs-input v-model="queryHelper.roleDesc" placeholder="说明" label-placeholder="说明"
                    class="ml-2"/>

          <vs-button icon-pack="feather" icon="icon-search"
                     class="float-left mt-5 ml-2" @click="getListPage">查询
          </vs-button>
        </div>
      </vx-card>
    </div>
    <div class="vx-card">
      <div class="vx-row m-2 p-2">
        <vs-col vs-type="flex" vs-justify="center" vs-align="center" vs-w="1">
            <vs-button @click="addData" class="mr-2" color="#3EC9D6" type="filled" icon-pack="feather"
                       icon="icon-plus-circle">新增
            </vs-button>
        </vs-col>
        <vs-col vs-type="flex" vs-justify="center" vs-align="center" vs-w="1">
            <vs-button @click="saveData" class="mr-2" color="success" type="filled" icon-pack="feather"
                       icon="icon-save">保存
            </vs-button>
        </vs-col>
        <vs-col vs-type="flex" vs-justify="center" vs-align="center" vs-w="1">
            <vs-button @click="deleteData" class="mr-2" color="danger" type="filled" icon-pack="feather"
                       icon="icon-trash-2">删除
            </vs-button>
        </vs-col>
      </div>
      <ag-grid-vue
        ref="agGridTable"
        :components="components"
        :gridOptions="gridOptions"
        class="ag-theme-material ag-grid-table"
        :columnDefs="columnDefs"
        :defaultColDef="defaultColDef"
        :rowData="this.listPage.records"
        rowSelection="multiple"
        colResizeDefault="shift"
        :animateRows="true"
        :floatingFilter="false"
        :pagination="true"
        :suppressPaginationPanel="true"
        @cellValueChanged="onCellValueChanged"
        :enableRtl="$vs.rtl">
      </ag-grid-vue>

      <vx-card>
        <vs-pagination goto
                       :total="this.listPage.pages||0"
                       :max="this.listPage.pages||0"
                       v-model="queryHelper.current"
                       :prev-icon="$vs.rtl ? 'arrow_forward' : 'arrow_back'"
                       :next-icon="$vs.rtl ? 'arrow_back' : 'arrow_forward'"/>
      </vx-card>

      <vs-popup :title="'角色('+role.roleName+')--权限配置'" type="border" :active.sync="active">
        <permission-config :roleIdParam="role.id" :isDelete="isDelete"></permission-config>
      </vs-popup>
    </div>
  </div>
</template>
<script>
import BaseGrid from "../../../../components/grid/BaseGrid";
import {getRequest, postRequest} from "../../../../core/http/axiosClient";
import notify from "../../../../core/notify/notify";
import PermissionConfig from "./PermissionConfig";
import RoleCellRendererActions from "./cell-renderer/RoleCellRendererActions.vue";
import RoleCellRendererStatus from "./cell-renderer/RoleCellRendererStatus.vue";

export default {
  components: {PermissionConfig, BaseGrid, RoleCellRendererStatus,RoleCellRendererActions},
  extends: BaseGrid,
  mounted() {
    this.getListPage();
  },
  watch: {
    'queryHelper.current'() {
      this.getListPage();
    }
  },
  data() {
    return {
      that: this,
      active: false,
      role: '',
      isDelete: false,
      columnDefs: [
        {
          headerName: '角色名称',
          field: 'roleName',
          filter: true,
          width: 300,
          checkboxSelection: true,
          headerCheckboxSelectionFilteredOnly: true,
          headerCheckboxSelection: true
        },
        {
          headerName: '说明',
          field: 'roleDesc',
          width: 450,
          filter: true,
        },
        {
          headerName: '状态',
          field: 'status',
          width: 250,
          cellRendererFramework: 'RoleCellRendererStatus'
        },
        {
          headerName: '操作',
          field: 'transactions',
          width: 250,
          cellRendererFramework: 'RoleCellRendererActions'
        }
      ],
      components: {
        RoleCellRendererStatus,
        RoleCellRendererActions
      }
    }
  },
  methods: {
    addData() {
      this.api_addData({'roleName': '', 'roleDesc': '', 'dataFlag': 1})
    },
    getListPage() {
      this.$vs.loading();
      let that = this;
      getRequest('/user/role/listPage', this.queryHelper.getParams()).then(function (res) {
        if (res.success) {
          that.listPage = res.data;
        }
      })
    },
    saveData() {
      let allData = [].concat(this.getAddRecords()).concat(this.getModifyRecords());
      if (!allData || allData.length === 0) {
        notify.warning('没有要保存的数据！');
        return;
      }
      this.$vs.loading();
      let that = this;
      postRequest('/user/role/saveOrUpdateBatch', allData).then(function (res) {
        if (res.success) {
          that.getListPage();
          notify.success('操作已完成！');
          that.clearOnLoadAfter();
        }
      })
    },
    deleteData() {
      let selectedRows = this.gridApi.getSelectedRows();
      if (selectedRows.length == 0) {
        notify.warning('请至少选择一条数据！');
        return;
      }
      this.$vs.loading();
      let ids = selectedRows.map(data => data.id);
      if (ids.includes(this.roleId)) {
        this.isDelete = true;
      }
      let that = this;
      postRequest('/system/sysRole/deleteByIds', ids).then(function (res) {
        if (res.success) {
          that.getListPage();
          notify.success('操作已完成！')
          that.isDelete = false;
        }
      })
    }
  }
}
</script>
