<template>
  <vx-card title="权限配置">
    <vs-input class="inputx tree-search-input float-left mr-2" placeholder="查询..." v-model.lazy="searchword"/>
    <vs-button color="#3EC9D6" type="filled" @click="search"  icon-pack="feather" icon="icon-search">查询</vs-button>
    <v-tree ref="tree" :data="treeData" :tpl="tpl"/>

    <div class="demo-alignment">
      <vs-popup classContent="popup-example" title="权限详情" :active.sync="openPermissionDetail">
        <div class="vx-row mb-2">
          <div class="vx-col w-full">
            <vs-input class="w-full" name="权限名称"
                      v-validate="'required'" data-vv-validate-on="blur"
                      v-model="permissionData.permissionName"
                      icon-pack="feather" icon="icon-bookmark" icon-no-border label="名称"/>
            <span class="text-danger text-sm">{{ errors.first('权限名称') }}</span>
          </div>
        </div>
        <div class="vx-row mb-2">
          <div class="vx-col w-full">
            <vs-input class="w-full" name="资源URL"
                      v-validate="'required'" data-vv-validate-on="blur"
                      v-model="permissionData.resourceUrl"
                      icon-pack="feather" icon="icon-link-2" icon-no-border label="资源URL"/>
            <span class="text-danger text-sm">{{ errors.first('资源URL') }}</span>
          </div>
        </div>
        <div class="vx-row mb-2">
          <div class="vx-col w-full">
            <vs-input class="w-full" v-model="permissionData.icon" icon-pack="feather"
                      :icon="permissionData.icon||'icon-star'" icon-no-border label="图标"/>
          </div>
        </div>
        <div class="vx-row mb-2">
          <div class="vx-col w-full">
            <!--<v-select class="w-full" taggable push-tags :options="[{label: '菜单', value: '1'},{label: '按钮', value: '2'}]" dir="ltr"></v-select>-->
            <vs-select class="selectExample w-full" v-model="permissionData.permissionType" icon-pack="feather"
                       icon="icon-star" autocomplete readonly label="类型">
              <vs-select-item value="1" text="分类"/>
              <vs-select-item value="2" text="导航"/>
              <vs-select-item value="3" text="菜单"/>
              <vs-select-item value="4" text="按钮"/>
            </vs-select>
          </div>
        </div>
        <div class="vx-row mb-2">
          <div class="vx-col">
            <label>排序号</label>
            <vs-input-number v-model="permissionData.orderNum" label-placeholder="排序号"/>
          </div>
        </div>
        <div class="vx-row mb-6">
          <div class="vx-col w-full">
            <vs-textarea class="w-full" v-model="permissionData.permissionDesc" icon-no-border label="说明"/>
          </div>
        </div>
        <div class="vx-row">
          <div class="vx-col w-full">
            <vs-button class="mr-3 mb-2 bg-success" @click="saveData" :disabled="!validateForm">保存</vs-button>
          </div>
        </div>
      </vs-popup>
    </div>
  </vx-card>
</template>

<script>
    import {VTree} from 'vue-tree-halower'
    import {VueSelect} from 'vue-select'
    import {deleteRequest, getRequest, postRequest} from "../../../../core/http/axiosClient";
    import notify from "../../../../core/notify/notify";

    export default {
      components: {
        VTree,
          'v-select': VueSelect
      },
      computed: {
        validateForm() {
          return this.permissionData.permissionName !== '' && this.permissionData.resourceUrl !== '';
        }
      },
      mounted() {
        this.listAll();
      },
        data() {
            return {
                openPermissionDetail: false,
                searchword: '',
                treeData: [
                    {
                        title: '根权限',
                        id: 0,
                        icon: 'icon-home',
                        expanded: true,
                        children: []
                    }
                ],
                initData: {
                    permissionName: '',
                    permissionType: '1',
                    permissionDesc: '',
                    resourceUrl: '',
                    icon: 'icon-star',
                    orderNum: 1,
                    dataFlag: 1
                },
                permissionData: {
                    permissionName: '',
                    permissionType: '1',
                    permissionDesc: '',
                    resourceUrl: '',
                    icon: 'icon-star',
                    orderNum: 1,
                    dataFlag: 1
                },
                permissionList: [],
                moveId: ''
            }
        },
        methods: {
            search() {
                this.$refs.tree.searchNodes(this.searchword)
            },
            tpl(...args) {
                const {0: node} = args
                let titleClass = node.selected ? 'node-title node-selected' : 'node-title'
                if (node.searched) titleClass += ' node-searched'
                return <span>
                          <span class="text-primary">
                              &nbsp;&nbsp;
                            <vs-icon icon-pack="feather" icon={node.icon} svgClasses="h-4 w-4 mb-1 stroke-current text-primary" />
                          </span>
                          <span class={titleClass} domPropsInnerHTML={node.title} onClick={() => {
                              this.$refs.tree.nodeSelected(node)
                          }}>
                          </span>
                          <span class={this.moveId || node.permissionType == 4? 'hidden' : ''}>
                            <button class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                                    onClick={() => this.addNode(node)}>
                              <vs-icon icon-pack="feather" icon="icon-folder-plus">&nbsp;新增</vs-icon>
                            </button>
                          </span>
                          <span class={node.id == 0 || this.moveId ? 'hidden' : ''}>
                            <button
                                class="btn-delete text-info border-none cursor-pointer px-2 py-1 mr-2 rounded"
                                onClick={() => this.viewNode(node)}>
                            <vs-icon icon-pack="feather" icon="icon-eye">&nbsp;详情</vs-icon>
                            </button>
                            <button class="btn-delete text-danger border-none cursor-pointer px-2 py-1 mr-2 rounded"
                                    onClick={() => this.deleteNode(node)}>
                                <vs-icon icon-pack="feather" icon="icon-trash-2">&nbsp;删除</vs-icon>
                            </button>
                            <button
                                class="btn-delete text-warning border-none cursor-pointer px-2 py-1 mr-2 rounded"
                                onClick={() => this.moveNode(node)}>
                                    <vs-icon icon-pack="feather" icon="icon-navigation">&nbsp;移动</vs-icon>
                            </button>
                          </span>
                          <span class={!this.moveId ? 'hidden' : ''}>
                            <span class={this.moveId == node.id || node.permissionType == 4?'hidden':''}>
                              <button class="btn-delete text-warning border-none cursor-pointer px-2 py-1 mr-2 rounded"
                                  onClick={() => this.doMove(node)}>
                              <vs-icon icon-pack="feather" icon="icon-crosshair">&nbsp;放入</vs-icon>
                           </button>
                            </span>
                           <span class={this.moveId != node.id?'hidden':''}>
                             <button class="btn-delete text-warning border-none cursor-pointer px-2 py-1 mr-2 rounded"
                                 onClick={() => this.cancelMove()}>
                             <vs-icon icon-pack="feather" icon="icon-x-circle">&nbsp;取消</vs-icon>
                           </button>
                           </span>
                          </span>
                          <label class={node.id == 0? 'hidden' : 'label text-primary'}>
                                <small>{node.permissionType == 1 ? '分类' : node.permissionType == 2 ? '导航': node.permissionType == 3 ? '菜单' : '按钮'}&nbsp;</small>
                          </label>
                          <br/>
                          <span>
                            <vs-divider></vs-divider>
                          </span>
                        </span>
            },
            viewNode(node) {
                this.permissionData = {'id':node.id,'parentId':node.parent,'permissionName':node.permissionName,
                    'permissionType':node.permissionType,'permissionDesc':node.permissionDesc,'resourceUrl':node.resourceUrl,'icon':node.icon,'orderNum':node.orderNum};
                this.openPermissionDetail = true;
            },
            addNode(node) {
                this.permissionData.parentId = node.id;
                this.openPermissionDetail = true;
            },
            saveData() {
                this.openPermissionDetail = false;
                this.$vs.loading();
                let that = this;
                postRequest('/user/permission/saveOrUpdate', this.permissionData).then(function (res) {
                    if (res.success) {
                        that.permissionData = that.initData;
                        that.listAll();
                        notify.success('操作已完成！');
                    }
                })
            },
            moveNode(node) {
                this.moveId = node.id;
            },
            doMove(node) {
                this.$vs.loading();
                let that = this;
                postRequest('/user/permission/move',{'id':this.moveId,'parentId':node.id}).then(function (res) {
                    if (res.success) {
                        that.listAll();
                        that.moveId = '';
                        notify.success('操作已完成！');
                    }
                })
            },
            cancelMove() {
                this.moveId = '';
            },
            deleteNode(node) {
                this.$vs.loading();
                let that = this;
                deleteRequest('/user/permission/' + node.id).then(function (res) {
                    if (res.success) {
                        that.listAll();
                        notify.success('操作已完成！');
                    }
                })
            },
            listAll() {
                this.$vs.loading();
                let that = this;
                getRequest('/user/permission/listAll').then(function (res) {
                    if (res.success) {
                        that.permissionList = res.data;
                        that.renderTree();
                    }
                })
            },
            renderTree() {
                let dataList = this.permissionList;
                let pIdChildren = new Map();
                dataList.forEach(function (data) {
                    let parentId = data.parentId;
                    data.title = data.permissionName;

                    let childrenList = pIdChildren.get(parentId);
                    if (!childrenList) {
                        childrenList = [];
                        childrenList.push(data);
                        pIdChildren.set(parentId, childrenList);
                    } else {
                        childrenList.push(data);
                    }
                });
                for (let pid of pIdChildren.keys()) {
                    let childrenList = pIdChildren.get(pid);
                    for(let i=0;i<childrenList.length;i++){
                        let childrenNode = childrenList[i];
                        let cList = pIdChildren.get(childrenNode.id);
                        if (cList) {
                            this.$set(childrenNode,'expanded',!0);
                            cList = cList.sort(function (obj1, obj2) {
                                return obj1.orderNum - obj2.orderNum;
                            })
                        } else {
                            cList = [];
                        }
                        childrenNode.children = cList;
                    }
                }
                this.treeData[0].children = pIdChildren.get(0);
                pIdChildren = null;
            }
        }
    }
</script>

<style lang="scss">
  @import "@/assets/scss/vuexy/extraComponents/tree.scss";

  button.btn-async {
    background: rgba(var(--vs-warning), 0.15);
  }

  button.btn-delete {
    background: rgba(var(--vs-danger), 0.15);
  }
</style>
