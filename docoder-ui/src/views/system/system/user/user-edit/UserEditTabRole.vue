<!-- =========================================================================================
  File Name: DataListListView.vue
  Description: Data List - List View
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->

<template>
  <div id="data-list-list-view" class="data-list-container">

    <vs-table ref="table" :data="listPage.records">

      <div slot="header" class="flex flex-wrap-reverse items-center flex-grow justify-between">
        <div class="flex flex-wrap-reverse items-center data-list-btn-container">
          <vs-button class="ml-auto mt-2 mb-4" color="success" type="filled" icon-pack="feather" icon="icon-save" @click="saveData" >保存</vs-button>
        </div>
      </div>


      <template slot="thead">
        <vs-th sort-key="checked">持有</vs-th>
        <vs-th sort-key="roleName">角色名称</vs-th>
      </template>

      <template slot-scope="{data}">
        <tbody>
        <vs-tr :data="tr" :key="indextr" v-for="(tr, indextr) in data" :class="tr.checked?'is-selected':''">

          <vs-td>
            <div class="vx-row">
                  <vs-checkbox icon-pack="feather" icon="icon-user-check" color="success" v-model="tr.checked" @change="selectedRole(tr)"></vs-checkbox>
              </div>
          </vs-td>

          <vs-td>
            <p class="product-name font-medium truncate">{{ tr.roleName }}</p>
          </vs-td>

        </vs-tr>
        </tbody>
      </template>
    </vs-table>
    <br/>
    <vs-pagination
      :total="this.listPage.pages||0"
      :max="this.listPage.pages||0"
      v-model="queryHelper.current"
      :prev-icon="$vs.rtl ? 'arrow_forward' : 'arrow_back'"
      :next-icon="$vs.rtl ? 'arrow_back' : 'arrow_forward'"/>

  </div>
</template>

<script>
    import queryHelper from "../../../../../core/utils/queryHelper";
    import {postRequest} from "../../../../../core/http/axiosClient";
    import notify from "../../../../../core/notify/notify";

    export default {
        props: {
            data: {
                type: Object,
                required: true
            }
        },
        data() {
            return {
                queryHelper:queryHelper,
                listPage:{records:[]},
                updateRecords: new Map(),
                userId:JSON.parse(JSON.stringify(this.data)).id
            }
        },
        watch:{
            'queryHelper.current'() {
                this.getListPage();
            }
        },
        mounted() {
            this.getListPage();
        },
        methods:{
            getListPage(){
                this.$vs.loading();
                let that = this;
                postRequest('/system/sysRole/listPageWithUserId/'+this.userId,this.queryHelper).then((res)=>{
                   if(res.success){
                       that.mapData(res.data.records);
                       that.listPage = res.data;
                   }
                });
            },
            selectedRole(data){
                let roleId = data.id;
                if(data.checked){
                    if(data.urId){
                        this.updateRecords.delete(roleId);
                    }else{
                        let saveData = {};
                        saveData.userId = this.userId;
                        saveData.roleId = roleId;
                        this.updateRecords.set(roleId,saveData);
                    }
                }else{
                    if(data.urId){
                        let saveData = {};
                        saveData.id = data.urId;
                        this.updateRecords.set(roleId,saveData);
                    }else{
                        this.updateRecords.delete(roleId);
                    }
                }

            },
            saveData(){
                if(this.updateRecords.size==0){
                    notify.warning('没有需要变更的数据！');
                    return;
                }
                let records = [];
                this.updateRecords.forEach(data=>records.push(data));
                this.$vs.loading();
                let that = this;
                postRequest('/system/userRole/batch/'+this.userId,records).then(res=>{
                    if(res.success){
                        notify.success('更新操作完成！');
                        that.getListPage();
                        that.updateRecords.clear();
                        that.$store.dispatch('auth/refresh');
                    }
                })
            },
            mapData(dataList){
                dataList.forEach(data=>{
                    if(data.urId){
                        data.checked = true;
                    }
                });
            }
        }
    }
</script>

<style lang="scss">
  #data-list-list-view {
    .vs-con-table {

      /*
        Below media-queries is fix for responsiveness of action buttons
        Note: If you change action buttons or layout of this page, Please remove below style
      */
      @media (max-width: 689px) {
        .vs-table--search {
          margin-left: 0;
          max-width: unset;
          width: 100%;

          .vs-table--search-input {
            width: 100%;
          }
        }
      }

      @media (max-width: 461px) {
        .items-per-page-handler {
          display: none;
        }
      }

      @media (max-width: 341px) {
        .data-list-btn-container {
          width: 100%;

          .dd-actions,
          .btn-add-new {
            width: 100%;
            margin-right: 0 !important;
          }
        }
      }

      .product-name {
        max-width: 23rem;
      }

      .vs-table--header {
        display: flex;
        flex-wrap: wrap;
        margin-left: 1.5rem;
        margin-right: 1.5rem;

        > span {
          display: flex;
          flex-grow: 1;
        }

        .vs-table--search {
          padding-top: 0;

          .vs-table--search-input {
            padding: 0.9rem 2.5rem;
            font-size: 1rem;

            & + i {
              left: 1rem;
            }

            &:focus + i {
              left: 1rem;
            }
          }
        }
      }

      .vs-table {
        border-collapse: separate;
        border-spacing: 0 1.3rem;
        padding: 0 1rem;

        tr {
          box-shadow: 0 4px 20px 0 rgba(0, 0, 0, .05);

          td {
            padding: 20px;

            &:first-child {
              border-top-left-radius: .5rem;
              border-bottom-left-radius: .5rem;
            }

            &:last-child {
              border-top-right-radius: .5rem;
              border-bottom-right-radius: .5rem;
            }
          }

          td.td-check {
            padding: 20px !important;
          }
        }
      }

      .vs-table--thead {
        th {
          padding-top: 0;
          padding-bottom: 0;

          .vs-table-text {
            text-transform: uppercase;
            font-weight: 600;
          }
        }

        th.td-check {
          padding: 0 15px !important;
        }

        tr {
          background: none;
          box-shadow: none;
        }
      }

      .vs-table--pagination {
        justify-content: center;
      }
    }
  }
</style>
