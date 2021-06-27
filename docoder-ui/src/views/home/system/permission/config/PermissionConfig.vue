<template>
  <vx-card>
    <vs-row>
      <!--用户列表-->
      <vs-col vs-w="3.5">
        <vs-table :multiple="clickMode===2&&nodeMode==='role'" :data="userPage.records">
          <template slot="thead">
            <vs-th>用户列表</vs-th>
            <vs-th>操作项</vs-th>
          </template>
          <template slot-scope="{data}">
            <vs-tr :key="indextr" v-for="(tr, indextr) in data"
                   :state="nodeMode==='user'&&indextr===clickIndex?'primary':nodeMode!=='user'&&rbacUsers.includes(''+data[indextr].id)?'success':''">
              <vs-td :data="data[indextr].userName">
                <vs-list-item :title="data[indextr].userName" :subtitle="data[indextr].nickname">
                  <template slot="avatar">
                    <vs-avatar/>
                  </template>
                </vs-list-item>
              </vs-td>
              <vs-td>
                <div class="vx-row">
                  <button class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                          @click="modeClick(1,'user',data[indextr].id,indextr)">
                    <vs-icon icon-pack="feather" icon="icon-settings">&nbsp;单配</vs-icon>
                  </button>
                  <button class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                          @click="modeClick(2,'user',data[indextr].id,indextr)">
                    <vs-icon icon-pack="feather" icon="icon-settings">&nbsp;多配</vs-icon>
                  </button>

                  <button v-if="clickMode===1&&nodeMode==='role'&&!rbacUsers.includes(''+data[indextr].id)"
                          class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                          @click="oneConfig('user',data[indextr].id,1)">
                    <vs-icon icon-pack="feather" icon="icon-folder-plus">&nbsp;新增</vs-icon>
                  </button>
                  <button v-if="clickMode===1&&nodeMode==='role'&&rbacUsers.includes(''+data[indextr].id)"
                          class="btn-delete text-danger border-none cursor-pointer px-2 py-1 mr-2 rounded"
                          @click="oneConfig('user',data[indextr].id,0)">
                    <vs-icon icon-pack="feather" icon="icon-trash-2">&nbsp;移除</vs-icon>
                  </button>
                  <button v-if="clickMode===1&&nodeMode==='permission'&&rbacUsers.includes(''+data[indextr].id)"
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
        <vs-table :data="rolePage.records">
          <template slot="thead">
            <vs-th>角色列表</vs-th>
            <vs-th>操作项</vs-th>
          </template>
          <template slot-scope="{data}">
            <vs-tr :key="indextr" v-for="(tr, indextr) in data"
                   :state="nodeMode==='role'&&indextr===clickIndex?'primary':nodeMode!=='role'&&rbacRoles.includes(''+data[indextr].id)?'success':''">
              <vs-td :data="data[indextr].roleName">
                <vs-list-item :title="data[indextr].roleName" :subtitle="data[indextr].roleDesc">
                  <template slot="avatar">
                    <vs-avatar/>
                  </template>
                </vs-list-item>
              </vs-td>
              <vs-td>
                <div class="vx-row">
                  <button class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                          @click="modeClick(1,'role',data[indextr].id,indextr)">
                    <vs-icon icon-pack="feather" icon="icon-settings">&nbsp;单配</vs-icon>
                  </button>
                  <button class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                          @click="modeClick(2,'role',data[indextr].id,indextr)">
                    <vs-icon icon-pack="feather" icon="icon-settings">&nbsp;多配</vs-icon>
                  </button>

                  <button
                    v-if="clickMode===1&&(nodeMode==='user'||nodeMode==='permission')&&!rbacRoles.includes(''+data[indextr].id)"
                    class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                    @click="oneConfig('role',data[indextr].id,1)">
                    <vs-icon icon-pack="feather" icon="icon-folder-plus">&nbsp;新增</vs-icon>
                  </button>
                  <button v-if="clickMode===1&&(nodeMode==='user'||nodeMode==='permission')&&rbacRoles.includes(''+data[indextr].id)"
                          class="btn-delete text-danger border-none cursor-pointer px-2 py-1 mr-2 rounded"
                          @click="oneConfig('role',data[indextr].id,0)">
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
        <vs-table :data="[1]">
          <template slot="thead">
            <vs-th>资源树</vs-th>
          </template>
          <template>
            <tr>
              <td>
                <v-tree ref="tree" :data="permissionTree" :halfcheck='true' :multiple="nodeMode==='role'" :tpl="tpl"/>
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
      rbacUsers: [],
      rbacRoles: [],
      rbacPermissions: [],
      clickMode:'',
      nodeMode: '',
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
      let addShow = this.clickMode===1 && this.nodeMode === 'role' && !this.rbacPermissions.includes('' + node.id);
      let deleteShow = this.clickMode===1 && this.nodeMode === 'role' && this.rbacPermissions.includes('' + node.id);
      let userShow = this.clickMode===1 && this.nodeMode === 'user' && this.rbacPermissions.includes('' + node.id);
      return <span>
        <span class={titleClass} domPropsInnerHTML={node.title}></span>
        <button class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                onClick={() => modeClick(1,'permission', node.id, node.id)}>
          <vs-icon icon-pack="feather" icon="icon-settings">&nbsp;单配</vs-icon>
        </button>
        <button class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                onClick={() => modeClick(2,'permission', node.id, node.id)}>
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
        }
      })
    },
    getRolePage() {
      let that = this;
      getRequest('/user/role/rbacPage', this.queryHelper.getParams()).then(function (res) {
        if (res.success) {
          that.rolePage = res.data;
        }
      })
    },
    getAllPermission() {
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
