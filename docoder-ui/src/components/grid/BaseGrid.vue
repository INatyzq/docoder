<!-- =========================================================================================
    File Name: AgGridTable.vue
    Description: Ag Grid table
    ----------------------------------------------------------------------------------------
    Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
    Author URL: http://www.themeforest.net/user/pixinvent
========================================================================================== -->


<template>
  <div id="ag-grid-demo">
    <vx-card>

      <!-- TABLE ACTION ROW -->
      <div class="flex flex-wrap justify-between items-center">

        <div class="flex flex-wrap items-center justify-between ag-grid-table-actions-left">
          <vs-button @click="api_addData" class="mr-4" color="success" type="filled" icon-pack="feather"
                     icon="icon-plus-circle">新增
          </vs-button>
          <vs-button class="mr-4" color="danger" type="filled" icon-pack="feather" icon="icon-trash-2">删除</vs-button>
          <vs-input class="mb-4 md:mb-0 mr-4" v-model="searchQuery" @input="updateSearchQuery" placeholder="Search..."/>
          <vs-button class="mb-4 md:mb-0" @click="gridApi.exportDataAsCsv()">导出cvs</vs-button>
        </div>

        <!-- ITEMS PER PAGE -->
        <div class="mb-4 md:mb-0 mr-4 ag-grid-table-actions-right">
          <vs-dropdown vs-trigger-click class="cursor-pointer">
            <!-- <vs-button class="btn-drop" type="line" color="primary" icon-pack="feather" icon="icon-chevron-down"></vs-button> -->
            <vs-dropdown-menu>

              <vs-dropdown-item @click="gridApi.paginationSetPageSize(20)">
                <span>20</span>
              </vs-dropdown-item>
              <vs-dropdown-item @click="gridApi.paginationSetPageSize(50)">
                <span>50</span>
              </vs-dropdown-item>
              <vs-dropdown-item @click="gridApi.paginationSetPageSize(100)">
                <span>100</span>
              </vs-dropdown-item>
              <vs-dropdown-item @click="gridApi.paginationSetPageSize(150)">
                <span>150</span>
              </vs-dropdown-item>
            </vs-dropdown-menu>
          </vs-dropdown>
        </div>

      </div>
      <ag-grid-vue
        ref="agGridTable"
        :gridOptions="gridOptions"
        class="ag-theme-material w-100 my-4 ag-grid-table"
        :columnDefs="columnDefs"
        :defaultColDef="defaultColDef"
        :rowData="this.listPage.records"
        rowSelection="multiple"
        colResizeDefault="shift"
        :animateRows="true"
        :floatingFilter="true"
        :pagination="true"
        :suppressPaginationPanel="true"
        @cellValueChanged="onCellValueChanged"
        :enableRtl="$vs.rtl">
      </ag-grid-vue>
      <vs-pagination
        :total="listPage.pages||0"
        :max="listPage.pages||0"
        v-model="queryHelper.current"/>

    </vx-card>
  </div>
</template>

<script>
    import {AgGridVue} from 'ag-grid-vue'
    import '@/assets/scss/vuexy/extraComponents/agGridStyleOverride.scss'
    import queryHelper from "../../core/utils/queryHelper";

    export default {
        name: 'BaseGrid',
        components: {
            AgGridVue
        },
        props: {},
        data() {
            return {
                queryHelper:queryHelper.new(),
                primaryKey: "id",
                dataListBak: new Map(),
                updateList: new Map(),
                insertList: [],
                listPage: {records:[]},
                searchQuery: '',
                gridOptions: {},
                maxPageNumbers: 7,
                gridApi: null,
                defaultColDef: {
                    sortable: true,
                    editable: true,
                    resizable: true,
                    suppressMenu: true
                },
                columnDefs: [
                    {
                        headerName: 'First Name',
                        field: 'firstname',
                        width: 175,
                        filter: true,
                        checkboxSelection: true,
                        headerCheckboxSelectionFilteredOnly: true,
                        headerCheckboxSelection: true
                    }
                ],
            }
        },
        watch: {
            '$store.state.windowWidth'(val) {
                if (val <= 576) {
                    this.maxPageNumbers = 4
                    this.gridOptions.columnApi.setColumnPinned('email', null)
                } else this.gridOptions.columnApi.setColumnPinned('email', 'left')
            },
            'listPage.records'() {
                let that = this;
                this.listPage.records.forEach(function (data) {
                    let newData = {};
                    for (let key in data) {
                        newData[key] = data[key];
                    }
                    that.dataListBak.set(newData[that.primaryKey], newData);
                });
                //this.clearOnLoadAfter();
            }
        },
        /*computed: {
            paginationPageSize() {
                if (this.gridApi) return this.gridApi.paginationGetPageSize()
                else return 50
            },
            totalPages() {
                if (this.gridApi) return this.gridApi.paginationGetTotalPages()
                else return 0
            },
            currentPage: {
                get() {
                    if (this.gridApi) return this.gridApi.paginationGetCurrentPage() + 1
                    else return 1
                },
                set(val) {
                    this.gridApi.paginationGoToPage(val - 1)
                }
            }
        },*/
        methods: {
            updateSearchQuery(val) {
                this.gridApi.setQuickFilter(val)
            },
            api_addData(record) {
                this.listPage.records.push(record);
                this.insertList.push(record);
            },
            onCellValueChanged(e) {
                let data = e.data;
                if (data[this.primaryKey]) {
                    let dataBak = this.dataListBak.get(data[this.primaryKey]);
                    let isModify = false;
                    let obj = {};
                    for (let key in dataBak) {
                        obj[key] = data[key];
                        if (dataBak[key] != data[key]) {
                            isModify = true;
                        }
                    }
                    if (isModify) {
                        this.updateList.set(data[this.primaryKey], obj)
                    } else {
                        this.updateList.delete(data[this.primaryKey])
                    }
                }
            },
            getAddRecords() {
                let records = [];
                if (this.insertList.length > 0) {
                    records = this.insertList;
                }
                return records;
            },
            getModifyRecords() {
                let records = [];
                if (this.updateList.size > 0) {
                    this.updateList.forEach(function (data) {
                        records.push(data);
                    });
                }
                return records;
            },
            clearOnLoadAfter(){
                this.insertList = [];
                this.updateList = new Map();
            }
        },
        mounted() {
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
