<template>
  <vx-card>
    <vs-row>
      <!--用户列表-->
      <vs-col vs-w="3.5">
        <vs-table :data="[1]" class="text-center">
          <template slot="thead">
            <vs-th>用户配置</vs-th>
          </template>
          <vs-tr>
            <vs-td v-if="clickMode===30">
              <button class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                @click="moreConfig('user')">
                <vs-icon icon-pack="feather" icon="icon-settings">&nbsp;确定</vs-icon>
              </button>
            </vs-td>
          </vs-tr>
        </vs-table>
        <vs-table :data="userPage.records">
          <template slot="thead">
            <vs-th v-if="clickMode===30">选择</vs-th>
            <vs-th>用户列表</vs-th>
            <vs-th>操作项</vs-th>
          </template>
          <template slot-scope="{data}">
            <vs-tr :data="row" :key="indextr" v-for="(row, indextr) in data"
                   :state="nodeMode==='user'&&indextr===clickIndex?'primary':nodeMode!=='user'&&rbacUsers.includes(''+row.id)?'success':''">
              <vs-td v-if="clickMode===30">
                <vs-checkbox :checked="row.checked" @change="nodeCheck('user',row)"/>
              </vs-td>
              <vs-td :data="row.userName">
                <vs-list-item :title="row.userName" :subtitle="row.nickname">
                  <template slot="avatar">
                    <vs-avatar/>
                  </template>
                </vs-list-item>
              </vs-td>
              <vs-td>
                <div class="vx-row">
                  <button class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                          @click="modeClick(1,'user',row.id,indextr)">
                    <vs-icon icon-pack="feather" icon="icon-settings">&nbsp;单配</vs-icon>
                  </button>
                  <button class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                          @click="modeClick(20,'user',row.id,indextr)">
                    <vs-icon icon-pack="feather" icon="icon-settings">&nbsp;多配</vs-icon>
                  </button>

                  <button v-if="clickMode===1&&nodeMode==='role'&&!rbacUsers.includes(''+row.id)"
                          class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                          @click="oneConfig('user',row.id,1)">
                    <vs-icon icon-pack="feather" icon="icon-folder-plus">&nbsp;新增</vs-icon>
                  </button>
                  <button v-if="clickMode===1&&nodeMode==='role'&&rbacUsers.includes(''+row.id)"
                          class="btn-delete text-danger border-none cursor-pointer px-2 py-1 mr-2 rounded"
                          @click="oneConfig('user',row.id,0)">
                    <vs-icon icon-pack="feather" icon="icon-trash-2">&nbsp;移除</vs-icon>
                  </button>
                  <button v-if="clickMode===1&&nodeMode==='permission'&&rbacUsers.includes(''+row.id)"
                          class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded">
                    <vs-icon icon-pack="feather" icon="icon-star"></vs-icon>
                  </button>
                </div>
              </vs-td>
            </vs-tr>
          </template>
        </vs-table>
      </vs-col>
      <!--角色列表-->
      <vs-col vs-w="3.5">
        <vs-table :data="[1]" class="text-center">
          <template slot="thead">
            <vs-th>角色配置</vs-th>
          </template>
          <vs-tr>
            <vs-td v-if="clickMode===20||clickMode===40">
              <button class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                      @click="moreConfig('role')">
                <vs-icon icon-pack="feather" icon="icon-settings">&nbsp;确定</vs-icon>
              </button>
            </vs-td>
          </vs-tr>
        </vs-table>
        <vs-table :data="rolePage.records">
          <template slot="thead">
            <vs-th v-if="clickMode===20||clickMode===40">选择</vs-th>
            <vs-th>角色列表</vs-th>
            <vs-th>操作项</vs-th>
          </template>
          <template slot-scope="{data}">
            <vs-tr :data="row" :key="indextr" v-for="(row, indextr) in data"
                   :state="nodeMode==='role'&&indextr===clickIndex?'primary':nodeMode!=='role'&&rbacRoles.includes(''+row.id)?'success':''">
              <vs-td v-if="clickMode===20||clickMode===40">
                <vs-checkbox :checked="row.checked" @change="nodeCheck('role',row)"/>
              </vs-td>
              <vs-td :data="row.roleName">
                <vs-list-item :title="row.roleName" :subtitle="row.roleDesc">
                  <template slot="avatar">
                    <vs-avatar/>
                  </template>
                </vs-list-item>
              </vs-td>
              <vs-td>
                <div class="vx-row">
                  <button class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                          @click="modeClick(1,'role',row.id,indextr)">
                    <vs-icon icon-pack="feather" icon="icon-settings">&nbsp;单配</vs-icon>
                  </button>
                  <button class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                          @click="modeClick(30,'role',row.id,indextr)">
                    <vs-icon icon-pack="feather" icon="icon-settings">&nbsp;多配</vs-icon>
                  </button>

                  <button
                    v-if="clickMode===1&&(nodeMode==='user'||nodeMode==='permission')&&!rbacRoles.includes(''+row.id)"
                    class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                    @click="oneConfig('role',row.id,1)">
                    <vs-icon icon-pack="feather" icon="icon-folder-plus">&nbsp;新增</vs-icon>
                  </button>
                  <button v-if="clickMode===1&&(nodeMode==='user'||nodeMode==='permission')&&rbacRoles.includes(''+row.id)"
                          class="btn-delete text-danger border-none cursor-pointer px-2 py-1 mr-2 rounded"
                          @click="oneConfig('role',row.id,0)">
                    <vs-icon icon-pack="feather" icon="icon-trash-2">&nbsp;移除</vs-icon>
                  </button>
                </div>
              </vs-td>
            </vs-tr>
          </template>
        </vs-table>
      </vs-col>
      <!--权限树-->
      <vs-col vs-w="5">
        <vs-table :data="[1]" class="text-center">
          <template slot="thead">
            <vs-th>权限配置</vs-th>
          </template>
          <vs-tr>
            <vs-td v-if="clickMode===30">
              <button class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                @click="moreConfig('permission')">
                <vs-icon icon-pack="feather" icon="icon-settings">&nbsp;确定</vs-icon>
              </button>
            </vs-td>
          </vs-tr>
        </vs-table>
        <vs-table :data="[1]">
          <template slot="thead">
            <vs-th>资源树</vs-th>
          </template>
          <template>
            <tr>
              <td>
                <v-tree ref="tree" :data="permissionTree" :halfcheck='true' :multiple="clickMode===30" :tpl="tpl"/>
              </td>
            </tr>
          </template>
        </vs-table>
      </vs-col>
    </vs-row>
  </vx-card>
</template>

<script>
import {getRequest, postRequest} from "@/core/http/axiosClient";
import queryHelper from "@/core/utils/queryHelper";
import {VTree} from 'vue-tree-halower'
import notify from "@/core/notify/notify";

export default {
  components: {
    VTree
  },
  created() {
    window.modeClick = this.modeClick
    window.oneConfig = this.oneConfig
  },
  mounted() {
    this.getUserPage();
    this.getRolePage();
    this.getAllPermission();
  },
  data() {
    return {
      queryHelper: queryHelper,
      userPage: {records: []},
      rolePage: {records: []},
      userSelected:[],
      roleSelected:[],
      rbacUsers: [],
      rbacRoles: [],
      rbacPermissions: [],
      //1:单配 20:用户多配 30:角色多配 40:权限多配
      clickMode:'',
      nodeMode: 0,
      nodeModeId: '',
      clickIndex: '',
      permissionTree: [
        {
          title: '根权限',
          id: 0,
          icon: 'icon-home',
          expanded: true,
          children: []
        }
      ],
      permissionList: []
    }
  },
  methods: {
    tpl(...args) {
      let {0: node, 2: parent, 3: index} = args
      let titleClass = this.clickIndex === node.id ? 'node-title text-primary' : 'node-title'
      //let titleClass = node.selected ? 'node-title node-selected' : 'node-title'
      if (node.searched) titleClass += ' node-searched'
      let rootBtnsShow = node.id ===0;
      let configShow = node.id!==0;
      let addShow = configShow && this.clickMode===1 && this.nodeMode === 'role' && !this.rbacPermissions.includes('' + node.id);
      let deleteShow = configShow && this.clickMode===1 && this.nodeMode === 'role' && this.rbacPermissions.includes('' + node.id);
      let userShow = configShow && this.clickMode===1 && this.nodeMode === 'user' && this.rbacPermissions.includes('' + node.id);
      return <span>
        <span class={titleClass} domPropsInnerHTML={node.title}></span>
        <span style={rootBtnsShow ? '' : 'display:none'}>

        </span>
        <button style={configShow ? '' : 'display:none'} class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                onClick={() => modeClick(1,'permission', node.id, node.id)}>
          <vs-icon icon-pack="feather" icon="icon-settings">&nbsp;单配</vs-icon>
        </button>
        <button style={configShow ? '' : 'display:none'} class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                onClick={() => modeClick(40,'permission', node.id, node.id)}>
          <vs-icon icon-pack="feather" icon="icon-settings">&nbsp;多配</vs-icon>
        </button>

        <button style={addShow ? '' : 'display:none'}
                class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                onClick={() => oneConfig('permission',node.id,1)}>
          <vs-icon icon-pack="feather" icon="icon-folder-plus">&nbsp;新增</vs-icon>
        </button>
        <button style={deleteShow ? '' : 'display:none'}
                class="btn-delete text-danger border-none cursor-pointer px-2 py-1 mr-2 rounded"
                onClick={() => oneConfig('permission',node.id,0)}>
          <vs-icon icon-pack="feather" icon="icon-trash-2">&nbsp;移除</vs-icon>
        </button>
        <button style={userShow ? '' : 'display:none'}
                class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded">
          <vs-icon icon-pack="feather" icon="icon-star"></vs-icon>
        </button>
      </span>
    },
    getUserPage() {
      let that = this;
      getRequest('/user/rbacPage', this.queryHelper.getParams()).then(function (res) {
        if (res.success) {
          that.userPage = res.data;
          if(that.userPage.records.length>0){
            that.userPage.records.forEach(data=>{
              let id = data.id;
              if(that.rbacUsers.includes(''+id)){
                data.checked = true;
                that.userSelected.push(id);
              }
            })
          }
          that.getAllPermission();
        }
      })
    },
    getRolePage() {
      let that = this;
      getRequest('/user/role/rbacPage', this.queryHelper.getParams()).then(function (res) {
        if (res.success) {
          that.rolePage = res.data;
          if(that.rolePage.records.length>0){
            that.rolePage.records.forEach(data=>{
              let id = data.id;
              if(that.rbacRoles.includes(''+id)){
                data.checked = true;
                that.roleSelected.push(id);
              }
            })
          }
        }
      })
    },
    getAllPermission() {
      this.$vs.loading();
      let that = this;
      getRequest('/user/permission/listAll').then(function (res) {
        if (res.success) {
          that.permissionList = res.data;
          that.renderTree(that);
        }
      })
    },
    renderTree(that) {
      let dataList = this.permissionList;
      let pIdChildren = new Map();
      dataList.forEach(function (data) {
        if(that.rbacPermissions.includes('' + data.id)){
          data.checked = true;
        }

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
        for (let i = 0; i < childrenList.length; i++) {
          let childrenNode = childrenList[i];
          let cList = pIdChildren.get(childrenNode.id);
          if (cList) {
            this.$set(childrenNode, 'expanded', !0);
            cList = cList.sort(function (obj1, obj2) {
              return obj1.orderNum - obj2.orderNum;
            })
          } else {
            cList = [];
          }
          childrenNode.children = cList;
        }
      }
      this.permissionTree[0].children = pIdChildren.get(0);
      pIdChildren = null;
    },
    modeClick(clickMode,nodeMode,id,indextr) {
      this.clickMode = clickMode;
      this.nodeMode = nodeMode;
      this.nodeModeId = id;
      this.clickIndex = indextr;
      queryHelper.mode = nodeMode;
      queryHelper.id = id;
      let that = this;
      getRequest('/user/permission/rbac', this.queryHelper.getParams()).then(function (res) {
        if (res.success) {
          that.rbacUsers = res.data.userIds || [];
          that.rbacRoles = res.data.roleIds || [];
          that.rbacPermissions = res.data.permissionIds || [];
        }
      })
      if (nodeMode !== 'user') {
        that.getUserPage();
      }
      if (nodeMode !== 'role') {
        that.getRolePage();
      }
    },
    oneConfig(nodeType,id,action) {
      let nodeModeKey = this.nodeMode+'Id';
      let nodeTypeKey = nodeType+'Id';
      let bind = {'action':action};
      bind[nodeModeKey] = this.nodeModeId;
      bind[nodeTypeKey] = id;
      if (this.nodeMode === 'user' && nodeType === 'role' ||
        this.nodeMode === 'role' && nodeType === 'user') {
        this.userRoleBind([bind]);
      }
      if (this.nodeMode === 'role' && nodeType === 'permission' ||
        this.nodeMode === 'permission' && nodeType === 'role') {
        this.rolePermissionBind([bind]);
      }
    },
    moreConfig(selectedMode){
      if(selectedMode==='permission'){
        let selectedNodes = this.$refs.tree.getCheckedNodes();
        selectedNodes.map(node=>node.id)
      }
    },
    nodeCheck(checkMode,data){
      let id = data.id;
      if(data.checked===true){
        data.checked = false
        if(checkMode==='user'){
          this.userSelected = this.userSelected.filter(val=>val!==id);
        }else {
          this.roleSelected = this.roleSelected.filter(val=>val!==id);
        }
      }else{
        data.checked = true
        if(checkMode==='user'){
          this.roleSelected.push(id)
        }
      }
    },
    userRoleBind(bindArray) {
      let that = this;
      postRequest('/user/ur/bindBatch', bindArray).then(function (res) {
        if (res.success) {
          notify.success("操作成功！");
          that.modeClick(1,that.nodeMode,that.nodeModeId,that.clickIndex);
        }
      });
    },
    rolePermissionBind(bindArray) {
      let that = this;
      postRequest('/user/rp/bindBatch', bindArray).then(function (res) {
        if (res.success) {
          notify.success("操作成功！");
          that.modeClick(1,that.nodeMode,that.nodeModeId,that.clickIndex);
        }
      });
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
