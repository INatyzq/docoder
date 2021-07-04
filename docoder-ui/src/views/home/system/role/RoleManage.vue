<template>
  <div>
    <div>
      <vx-card ref="filterCard" title="查询" class="user-list-filters mb-8" collapse-action refresh-content-action
               @refresh="queryHelper.refresh(that,'getPage')">
        <div class="vx-row">
          <vs-input v-model="queryHelper.roleName" placeholder="角色名称" label-placeholder="角色名称"
                    class="ml-2"/>

          <vs-input v-model="queryHelper.roleDesc" placeholder="说明" label-placeholder="说明"
                    class="ml-2"/>

          <vs-button icon-pack="feather" icon="icon-search"
                     class="float-left mt-5 ml-2" @click="getPage">查询
          </vs-button>
        </div>
      </vx-card>
    </div>
    <vx-card>

      <div class="vx-row p-2">
        <vs-col vs-type="flex" vs-justify="center" vs-align="center" vs-w="1">
          <vs-button @click="openRecordWin" class="mr-2" color="primary" type="filled"
                     icon-pack="feather" icon="icon-plus-circle">新增
          </vs-button>
        </vs-col>
        <vs-col vs-type="flex" vs-justify="center" vs-align="center" vs-w="1">
          <vs-button @click="deleteRecords()" class="mr-2" color="danger" type="filled"
                     icon-pack="feather" icon="icon-trash-2">删除
          </vs-button>
        </vs-col>
      </div>

      <vs-table multiple v-model="selected" :data="dataPage.records" stripe>
        <template slot="thead">
          <vs-th>操作</vs-th>
          <vs-th sort-key="userName">角色名称</vs-th>
          <vs-th sort-key="userName">描述</vs-th>
          <vs-th sort-key="nickname">状态</vs-th>
        </template>

        <template slot-scope="{data}">
          <vs-tr :data="row" :key="indextr" v-for="(row, indextr) in data">

            <vs-td :data="row.roleName">
              <div :style="{'direction': $vs.rtl ? 'rtl' : 'ltr'}">
                <feather-icon icon="Edit3Icon" title="编辑"  svgClasses="text-primary h-5 w-5 mr-4 hover:text-primary cursor-pointer"
                    @click="openRecordWin(row)"/>
                <feather-icon icon="Trash2Icon" title="删除" svgClasses="text-danger h-5 w-5 mr-4 hover:text-danger cursor-pointer"
                    @click="deleteRecords([row.id])"/>
              </div>
            </vs-td>

            <vs-td :data="row.roleName">
              {{ row.roleName }}
            </vs-td>

            <vs-td :data="row.roleDesc">
              {{ row.roleDesc }}
            </vs-td>

            <vs-td :data="row.status">
              <vs-chip class="ag-grid-cell-chip" :color="chipColor(row.status)">
                <span>{{ row.status == 0 ? '未激活' : row.status == 1 ? '已激活' : '注销' }}</span>
              </vs-chip>
            </vs-td>
          </vs-tr>
        </template>
      </vs-table>
      <div class="m-2">
        <vs-pagination goto
                       :total="this.dataPage.pages||0"
                       :max="this.dataPage.pages||0"
                       v-model="queryHelper.current"
                       :prev-icon="$vs.rtl ? 'arrow_forward' : 'arrow_back'"
                       :next-icon="$vs.rtl ? 'arrow_back' : 'arrow_forward'"/>
      </div>
    </vx-card>

    <!--新增面板-->
    <div class="demo-alignment">
      <vs-popup classContent="popup-example" title="角色详情" :active.sync="showAdd">
        <div class="vx-row mb-2">
          <div class="vx-col w-full">
            <vs-input class="w-full"
                      v-validate="'required'" data-vv-validate-on="blur"
                      v-model="newRecord.roleName"
                      icon-pack="feather" icon="icon-bookmark" icon-no-border label="角色名称"/>
            <span class="text-danger text-sm">{{ errors.first('角色名称') }}</span>
          </div>
        </div>
        <div class="vx-row mb-2">
          <div class="vx-col w-full">
            <vs-input class="w-full"
                      v-model="newRecord.roleDesc"
                      icon-pack="feather" icon="icon-info" icon-no-border label="角色描述"/>
          </div>
        </div>
        <div class="vx-row">
          <div class="vx-col w-full">
            <vs-button class="mr-3 mb-2 bg-success" @click="saveData" :disabled="!validateForm">保存</vs-button>
          </div>
        </div>
      </vs-popup>
    </div>
  </div>
</template>
<script>
import {getRequest, postRequest} from "../../../../core/http/axiosClient";
import notify from "../../../../core/notify/notify";
import queryHelper from "@/core/utils/queryHelper";
import dialog from "@/core/notify/dialog";

export default {
  mounted() {
    this.getPage();
  },
  watch: {
    'queryHelper.current'() {
      this.getPage();
    }
  },
  computed: {
    validateForm() {
      return !this.errors.any() && this.newRecord.roleName;
    },
    chipColor() {
      return (value) => {
        if (value === 1) return 'success'
        else if (value === 3) return 'danger'
        else if (value === 2) return 'warning'
        else return 'primary'
      }
    }
  },
  data() {
    return {
      queryHelper: queryHelper.new(),
      dataPage: {records: []},
      showAdd:false,
      newRecord:{'roleName':'','roleDesc':''},
      selected:[]
    }
  },
  methods: {
    getPage() {
      this.$vs.loading();
      let that = this;
      getRequest('/user/role/listPage', this.queryHelper.getParams()).then(function (res) {
        if (res.success) {
          that.dataPage = res.data;
        }
      })
    },
    openRecordWin(row){
      this.showAdd = true;
      this.newRecord = row?row:{};
    },
    saveData() {
      if(this.errors.any()){
        return ;
      }
      this.$vs.loading();
      let that = this;
      postRequest('/user/role/saveOrUpdateBatch', [this.newRecord]).then(function (res) {
        if (res.success) {
          that.getPage();
          notify.success('操作已完成！');
          that.showAdd = false;
        }
      })
    },
    deleteRecords(ids) {
      ids = ids?ids:this.selected.map(data => data.id);
      if (ids.length === 0) {
        notify.warning('请至少选择一条数据！');
        return;
      }
      let that = this;
      dialog.confirm('确定删除选中的记录吗？',function (){
        that.$vs.loading();
        postRequest('/user/role/deleteByIds', ids).then(function (res) {
          if (res.success) {
            that.getPage();
            notify.success('操作已完成！');
            that.selected = [];
          }
        })
      })
    },
  }
}
</script>
