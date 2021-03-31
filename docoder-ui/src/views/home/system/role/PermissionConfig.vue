<template>
  <div class="card-box">
    <vs-row>
      <vs-button @click="savePermissionConfig" color="success" type="filled" icon-pack="feather" icon="icon-save">保存</vs-button>&nbsp;&nbsp;
      <vs-button color="dark" @click="backPermissionConfig" type="filled" icon-pack="feather" icon="icon-refresh-cw">回退</vs-button>
    </vs-row>
    <br>
    <vs-row>
      <vs-input class="inputx tree-search-input float-left mr-2" placeholder="查询..." v-model.lazy="searchword"/>
      <vs-button color="#3EC9D6" type="filled" @click="search"  icon-pack="feather" icon="icon-search">查询</vs-button>
    </vs-row>
    <v-tree ref="tree" :canDeleteRoot="true" :data="treeData" :tpl="tpl" :selectAlone="true"/>
  </div>
</template>

<script>
    import {VTree} from 'vue-tree-halower';
    import notify from "../../../../core/notify/notify";
    import {getRequest, postRequest} from "../../../../core/http/axiosClient";

    export default {
        name: "PermissionConfig",
        props: {
            roleIdParam: String|Number,
            isDelete: Boolean
        },
        watch: {
            roleIdParam() {
                this.refresh();
                this.roleId = this.$options.propsData.roleIdParam;
                if(this.roleId){
                    this.listAllPermission();
                }
            },
            isDelete(){
                this.permissionList = [];
                this.refresh();
                this.renderTree();
            }
        },
        components: {VTree},
        data() {
            return {
                searchword: '',
                roleId: Number,
                treeData: [
                    {
                        title: '根权限',
                        id: 0,
                        icon: 'HomeIcon',
                        expanded: true,
                        isParent: true,
                        children: []
                    }
                ],
                permissionList: [],
                permissionStatusMap: new Map(),
                permissionConf: new Map()
            }
        },
        methods: {
            search() {
                this.$refs.tree.searchNodes(this.searchword)
            },
            listAllPermission() {
                if (!this.roleId) {
                    return;
                }
                this.$vs.loading();
                let that = this;
                getRequest('/system/sysPermission/listAllWithRoleId/' + this.roleId).then(function (res) {
                    if (res.success) {
                        that.permissionList = res.data;
                        that.renderTree();
                    }
                })
            },
            tpl(...args) {
                const {0: node} = args
                return <span class="inline-flex">
                          <span class="text-primary">
                              &nbsp;&nbsp;
                             <feather-icon icon={node.icon||'StarIcon'} svgClasses="h-4 w-4 mb-1 stroke-current text-primary" />
                              &nbsp;&nbsp;
                          </span>
                          <span class={node.id == 0 ? 'hidden' : ''}>
                            <span onClick={() => this.setPermission(node, true)}>
                              <vs-checkbox color="success" value={node.selected}/>
                            </span>
                          </span>
                          <span domPropsInnerHTML={node.title}>
                          <span class={node.id == 0 ? 'hidden' : ''}>
                              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                          </span>
                          </span>
                          <span class={!node.isParent ? 'hidden' : ''}>
                              <vs-row>
                                  <vs-col vs-type="flex" vs-w="7">
                                    <vs-switch color="dark" onClick={() => this.cancelChecked(node)}>
                                      <span slot="on">取消选择</span>
                                      <span slot="off">取消选择</span>
                                    </vs-switch>
                                  </vs-col>
                                  <vs-col vs-type="flex" vs-w="5">
                                    <vs-switch value="true" color="dark" icon="icon-check"
                                               onClick={() => this.checkedAll(node)}>
                                      <span slot="on">全选</span>
                                      <span slot="off">全选</span>
                                    </vs-switch>
                                  </vs-col>
                              </vs-row>
                          </span>
                          <label class={node.id == 0 ? 'hidden' : 'label text-primary'}>
                                <small>&nbsp;&nbsp;&nbsp;{node.permissionType == 1 ? '分类' : node.permissionType == 2 ? '导航': node.permissionType == 3 ? '菜单' : '按钮'}</small>
                          </label>
                          <br/>
                          <span>
                            <vs-divider></vs-divider>
                          </span>
                        </span>
            },
            setPermission(node, isSingle) {
                if (node.id == 0) {
                    return;
                }
                if (isSingle) {
                    this.$refs.tree.nodeSelected(node);
                }
                let exist = this.permissionStatusMap.get(node.id);
                if (exist) {
                    if (node.selected) {
                        this.permissionConf.delete(node.id);
                    } else {
                        this.permissionConf.set(node.id, {'id': node.rpId});
                    }
                } else if (node.selected) {
                    this.permissionConf.set(node.id, {
                        'roleId': this.roleId,
                        'permissionId': node.id,
                        'dataFlag': 1
                    });
                }
                node.checkedCount++;
            },
            cancelChecked(node) {
                if (node.selected) {
                    this.$refs.tree.nodeSelected(node);
                }
                this.setPermission(node);
                let childrenList = node.children;
                let that = this;
                childrenList.forEach(function (children) {
                    if (children.selected) {
                        that.$refs.tree.nodeSelected(children);
                    }
                    that.setPermission(children);
                    that.cancelChecked(children);
                })
            },
            checkedAll(node) {
                if (!node.selected) {
                    this.$refs.tree.nodeSelected(node);
                }
                this.setPermission(node);
                let childrenList = node.children;
                let that = this;
                childrenList.forEach(function (children) {
                    if (!children.selected) {
                        that.$refs.tree.nodeSelected(children);
                    }
                    that.setPermission(children);
                    that.checkedAll(children);
                })
            },
            savePermissionConfig() {
                if (this.permissionConf.size == 0) {
                    notify.warning('没有变更的数据需要保存！');
                    return;
                }
                this.$vs.loading();
                let dataList = [];
                this.permissionConf.forEach(function (data) {
                    dataList.push(data);
                });
                let that = this;
                postRequest('/system/rolePermission/rpConfig/' + this.roleId, dataList).then(function (res) {
                    if (res.success) {
                        that.permissionList = res.data;
                        notify.success('操作已完成！');
                        that.refresh();
                        that.renderTree();
                    }
                })
            },
            backPermissionConfig() {
                if (this.permissionConf.size == 0) {
                    notify.warning('没有变更的数据需要回退！');
                    return;
                }
                this.refresh();
                this.listAllPermission(this.roleId);
                notify.success('回退完成！');
            },
            renderTree() {
                let dataList = this.permissionList;
                let pIdChildren = new Map();
                for(let i=0;i<dataList.length;i++){
                    let data = dataList[i];
                    let parentId = data.parentId;
                    data.title = data.permissionName;
                    //data.expanded = true;
                    if (data.withRole != -1) {
                        //data.selected = true;
                        data.checkedCount = 1;
                        this.permissionStatusMap.set(data.id, data);
                        this.$refs.tree.nodeSelected(data);
                    } else {
                        data.checkedCount = 0;
                    }

                    let childrenList = pIdChildren.get(parentId);
                    if (!childrenList) {
                        childrenList = [];
                        childrenList.push(data);
                        pIdChildren.set(parentId, childrenList);
                    } else {
                        childrenList.push(data);
                    }
                }
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
                        if (cList && cList.length > 0 && childrenNode.permissionType != 4) {
                            childrenNode.isParent = true;
                        }
                    }
                }
                this.treeData[0].children = pIdChildren.get(0);
            },
            refresh() {
                this.permissionConf = new Map();
                this.permissionStatusMap = new Map();
            }
        }
    }
</script>
<style lang="scss">
  @import "@/assets/scss/vuexy/extraComponents/tree.scss";
</style>
