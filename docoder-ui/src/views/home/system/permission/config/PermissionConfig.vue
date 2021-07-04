<template>
  <vx-card>
    <vs-row>
      <vs-col vs-w="12">
        <vs-table :data="[1]" class="text-center">
          <vs-tr><td>授权管理</td></vs-tr>
          <vs-tr>
            <td>
              <button v-if="clickMode!==50" class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                      @click="allConfig()">
                <vs-icon icon-pack="feather" icon="icon-settings">&nbsp;全局配置</vs-icon>
              </button>
              <button v-if="clickMode===50" class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                      @click="allOk()">
                <vs-icon icon-pack="feather" icon="icon-check-circle">&nbsp;确定</vs-icon>
              </button>
              <button v-if="clickMode===50" class="btn-delete text-danger border-none cursor-pointer px-2 py-1 mr-2 rounded"
                      @click="allCancel()">
                <vs-icon icon-pack="feather" icon="icon-trash-2">&nbsp;取消</vs-icon>
              </button>
            </td>
          </vs-tr>
        </vs-table>
      </vs-col>
    </vs-row>
    <vs-row>
      <!--用户列表-->
      <vs-col vs-w="3.5">
        <vs-table :data="[1]" class="text-center">
          <template slot="thead">
            <vs-th>用户配置</vs-th>
          </template>
          <vs-tr>
            <vs-td>
              <vs-input icon-pack="feather" icon="icon-search" placeholder="用户名|昵称" v-model="userQueryHelper.searchVal" @blur="getUserPage" class="is-label-placeholder" />
            </vs-td>
          </vs-tr>
          <vs-tr>
            <vs-td v-if="clickMode===30">
              <button class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                @click="moreConfig('user')">
                <vs-icon icon-pack="feather" icon="icon-check-circle">&nbsp;确定</vs-icon>
              </button>
            </vs-td>
          </vs-tr>
        </vs-table>
        <vs-table :data="userPage.records">
          <template slot="thead">
            <vs-th v-if="(clickMode===1&&nodeMode==='role')||clickMode===30||clickMode===50">选择</vs-th>
            <vs-th>用户列表</vs-th>
            <vs-th>操作项</vs-th>
          </template>
          <template slot-scope="{data}">
            <vs-tr :data="row" :key="indextr" v-for="(row, indextr) in data"
                   :state="nodeMode==='user'&&indextr===clickIndex?'primary':nodeMode!=='user'&&rbacUsers.includes(row.id)?'success':''">
              <vs-td v-if="(clickMode===1&&nodeMode==='role')||clickMode===30||clickMode===50">
                <vs-checkbox v-model="row.checked" @change="nodeCheck('user',row)"/>
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

                  <!--<button v-if="clickMode===1&&nodeMode==='role'&&!rbacUsers.includes(row.id)"
                          class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                          @click="oneConfig('user',row.id,1)">
                    <vs-icon icon-pack="feather" icon="icon-folder-plus">&nbsp;新增</vs-icon>
                  </button>
                  <button v-if="clickMode===1&&nodeMode==='role'&&rbacUsers.includes(row.id)"
                          class="btn-delete text-danger border-none cursor-pointer px-2 py-1 mr-2 rounded"
                          @click="oneConfig('user',row.id,0)">
                    <vs-icon icon-pack="feather" icon="icon-trash-2">&nbsp;移除</vs-icon>
                  </button>-->
                  <button v-if="clickMode===1&&nodeMode==='permission'&&rbacUsers.includes(row.id)"
                          class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded">
                    <vs-icon icon-pack="feather" icon="icon-check-circle"></vs-icon>
                  </button>
                </div>
              </vs-td>
            </vs-tr>
            <vs-tr>
              <vs-td colspan="3">
                <vs-pagination goto
                               :total="userPage.pages||0"
                               :max="userPage.pages||0"
                               v-model="userQueryHelper.current"
                               :prev-icon="$vs.rtl ? 'arrow_forward' : 'arrow_back'"
                               :next-icon="$vs.rtl ? 'arrow_back' : 'arrow_forward'"/>
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
            <vs-td>
              <vs-input icon-pack="feather" icon="icon-search" placeholder="角色名|角色描述" v-model="roleQueryHelper.searchVal" @blur="getRolePage" class="is-label-placeholder" />
            </vs-td>
          </vs-tr>
          <vs-tr>
            <vs-td v-if="clickMode===20||clickMode===40">
              <button class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                      @click="moreConfig('role')">
                <vs-icon icon-pack="feather" icon="icon-check-circle">&nbsp;确定</vs-icon>
              </button>
            </vs-td>
          </vs-tr>
        </vs-table>
        <vs-table :data="rolePage.records">
          <template slot="thead">
            <vs-th v-if="(clickMode===1&&nodeMode!=='role')||clickMode===20||clickMode===40||clickMode===50">选择</vs-th>
            <vs-th>角色列表</vs-th>
            <vs-th>操作项</vs-th>
          </template>
          <template slot-scope="{data}">
            <vs-tr :data="row" :key="indextr" v-for="(row, indextr) in data"
                   :state="nodeMode==='role'&&indextr===clickIndex?'primary':nodeMode!=='role'&&rbacRoles.includes(row.id)?'success':''">
              <vs-td v-if="(clickMode===1&&nodeMode!=='role')||clickMode===20||clickMode===40||clickMode===50">
                <vs-checkbox v-model="row.checked" @change="nodeCheck('role',row)"/>
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

                  <!--<button
                    v-if="clickMode===1&&(nodeMode==='user'||nodeMode==='permission')&&!rbacRoles.includes(row.id)"
                    class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                    @click="oneConfig('role',row.id,1)">
                    <vs-icon icon-pack="feather" icon="icon-folder-plus">&nbsp;新增</vs-icon>
                  </button>
                  <button v-if="clickMode===1&&(nodeMode==='user'||nodeMode==='permission')&&rbacRoles.includes(row.id)"
                          class="btn-delete text-danger border-none cursor-pointer px-2 py-1 mr-2 rounded"
                          @click="oneConfig('role',row.id,0)">
                    <vs-icon icon-pack="feather" icon="icon-trash-2">&nbsp;移除</vs-icon>
                  </button>-->
                </div>
              </vs-td>
            </vs-tr>
            <vs-tr>
              <vs-td colspan="3">
                <vs-pagination goto
                               :total="rolePage.pages||0"
                               :max="rolePage.pages||0"
                               v-model="roleQueryHelper.current"
                               :prev-icon="$vs.rtl ? 'arrow_forward' : 'arrow_back'"
                               :next-icon="$vs.rtl ? 'arrow_back' : 'arrow_forward'"/>
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
            <vs-td>
              <vs-input icon-pack="feather" icon="icon-search" placeholder="资源名称|Url" v-model="treeSearchword" @blur="$refs.tree.searchNodes(treeSearchword)" class="is-label-placeholder" />
            </vs-td>
          </vs-tr>
          <vs-tr>
            <vs-td v-if="clickMode===30">
              <button class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded"
                @click="moreConfig('permission')">
                <vs-icon icon-pack="feather" icon="icon-check-circle">&nbsp;确定</vs-icon>
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
                <v-tree ref="tree" :data="permissionTree" :halfcheck="true" :allowGetParentNode="true" :multiple="(clickMode===1&&nodeMode==='role')||clickMode===30||clickMode===50" @node-check="treeNodeCheck" :tpl="tpl"/>
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
  watch: {
    'userQueryHelper.current'() {
      this.getUserPage();
    },
    'roleQueryHelper.current'() {
      this.getRolePage();
    }
  },
  data() {
    return {
      queryHelper: queryHelper.new(),
      userQueryHelper: queryHelper.new(),
      roleQueryHelper: queryHelper.new(),
      userPage: {records: []},
      rolePage: {records: []},
      rbacUsers: [],
      rbacRoles: [],
      rbacPermissions: [],
      //1:单配 20:用户多配 30:角色多配 40:权限多配 50:全部配置
      clickMode:0,
      nodeMode: '',
      nodeModeId: '',
      clickIndex: '',
      userSearch:'',
      roleSearch:'',
      permissionTree: [
        {
          title: '根权限',
          id: 0,
          icon: 'icon-home',
          expanded: true,
          children: []
        }
      ],
      trees:[],
      treeSearchword:'',
      permissionList: []
    }
  },
  methods: {
    /*<button style={addShow ? '' : 'display:none'}
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
          <vs-icon icon-pack="feather" icon="icon-check-circle"></vs-icon>
        </button>*/
    tpl(...args) {
      let {0: node, 2: parent, 3: index} = args
      if(node.checked===true && parent){parent.meHalfcheck = true;}
      let configShow = node.id!==0;
      let userShow = configShow && this.clickMode===1 && this.nodeMode === 'user' && this.rbacPermissions.includes(node.id);
      let titleClass = userShow||this.clickIndex === node.id ? 'node-title text-success' : 'node-title'
      if (node.searched) titleClass += ' node-searched'
      let rootBtnsShow = node.id ===0;
      let addShow = configShow && this.clickMode===1 && this.nodeMode === 'role' && !this.rbacPermissions.includes(node.id);
      let deleteShow = configShow && this.clickMode===1 && this.nodeMode === 'role' && this.rbacPermissions.includes(node.id);
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
        <button style={userShow ? '' : 'display:none'}
                class="btn-delete text-success border-none cursor-pointer px-2 py-1 mr-2 rounded">
          <vs-icon icon-pack="feather" icon="icon-check-circle"></vs-icon>
        </button>
      </span>
    },
    getUserPage() {
      let that = this;
      getRequest('/user/rbacPage', this.userQueryHelper.getParams()).then(function (res) {
        if (res.success) {
          that.userPage = res.data;
          if(that.userPage.records.length>0){
            that.userPage.records.forEach(data=>{
              let id = data.id;
              if(that.rbacUsers.includes(id)){
                data.checked = true;
              }
            })
          }
          that.getAllPermission();
        }
      })
    },
    getRolePage() {
      let that = this;
      getRequest('/user/role/rbacPage', this.roleQueryHelper.getParams()).then(function (res) {
        if (res.success) {
          that.rolePage = res.data;
          if(that.rolePage.records.length>0){
            that.rolePage.records.forEach(data=>{
              let id = data.id;
              if(that.rbacRoles.includes(id)){
                data.checked = true;
              }
            })
          }
        }
      })
    },
    getAllPermission() {
      this.$vs.loading();
      let that = this;
      getRequest('/user/permission/tree').then(function (res) {
        if (res.success) {
          //that.permissionList = res.data;
          that.trees = res.data;
          that.trees.forEach(data=>{
            that.treeChecked(that,data,true);
          })
          that.permissionTree[0].children = res.data;
          //that.renderTree(that);
        }
      })
    },
    treeChecked(that,tree,checked){
      let id = tree.id;
      if(checked===true && that.rbacPermissions.includes(id)){
         tree.checked = true;
      }else{
        tree.checked = false;
        delete tree.checked;
      }
      let children = tree.children;
      if(children && children.length>0){
        children.forEach(node=>this.treeChecked(that,node,checked));
      }
    },
    renderTree(that) {
      let dataList = this.permissionList;
      let pIdChildren = new Map();
      dataList.forEach(function (data) {
        if(that.rbacPermissions.includes(data.id)){
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

      this.queryHelper.mode = nodeMode;
      this.queryHelper.id = id;

      this.userQueryHelper.mode = nodeMode;
      this.userQueryHelper.id = id;

      this.roleQueryHelper.mode = nodeMode;
      this.roleQueryHelper.id = id;
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
      let modeId = this.nodeModeId;
      if(selectedMode==='user'){
        let binds = this.buildUserAction(this.nodeModeId);
        this.userRoleBind(binds);
      }else if(selectedMode==='role'){
        if(this.nodeMode==='user'){
          let binds = this.buildRoleAction('userId',this.nodeModeId);
          this.userRoleBind(binds);
        }
        if(this.nodeMode==='permission'){
          let binds = this.buildRoleAction('permissionId',this.nodeModeId);
          this.rolePermissionBind(binds);
        }
      }else if(selectedMode==='permission'){
        let binds = this.buildPermissionAction(modeId);
        this.rolePermissionBind(binds);
      }
    },
    nodeCheck(checkMode,data){
      if(this.clickMode===1){
        if (this.nodeMode === 'user' && checkMode === 'role' ||
          this.nodeMode === 'role' && checkMode === 'user') {
          let action = data.checked?1:0;
          let nodeModeKey = this.nodeMode+'Id';
          let nodeTypeKey = checkMode+'Id';
          let bind = {'action':action};
          bind[nodeModeKey] = this.nodeModeId;
          bind[nodeTypeKey] = data.id;
          this.userRoleBind([bind]);
        }
      }/*else{
        let id = data.id;
        if(data.checked){
          data.checked = true
          if(checkMode==='user'){
            this.userSelected.push(id);
          }else{
            this.roleSelected.push(id);
          }
        }else{
          data.checked = false
          if(checkMode==='user'){
            this.userSelected = this.userSelected.filter(val=>val!==id);
          }else {
            this.roleSelected = this.roleSelected.filter(val=>val!==id);
          }
        }
      }*/
    },
    //node,checked,index
    treeNodeCheck(){
      if(this.clickMode!==1){
        return ;
      }
      let binds = this.buildPermissionAction(this.nodeModeId);

      /*let action = checked?1:0;
      let bind = {'roleId':this.nodeModeId,'permissionId':node.id,'action':action};*/
      this.rolePermissionBind(binds);
    },
    buildUserAction(roleId){
      let actions = [];
      let allChecked = this.userPage.records.filter(data=>data.checked===true).map(data=>data.id);
      allChecked.forEach(userId=>{
        if(!this.rbacUsers.includes(userId)){
          actions.push({'userId':userId,'roleId':roleId,'action':1})
        }
      })
      this.rbacUsers.forEach(userId=>{
        if(!allChecked.includes(userId)){
          actions.push({'userId':userId,'roleId':roleId,'action':0})
        }
      })
      return actions;
    },
    buildRoleAction(mode,modeId){
      let actions = [];
      let allChecked = this.rolePage.records.filter(data=>data.checked===true).map(data=>data.id);
      allChecked.forEach(roleId=>{
        if(!this.rbacRoles.includes(roleId)){
          let action = {'roleId':roleId,'action':1};
          action[mode] = modeId;
          actions.push(action)
        }
      })
      this.rbacRoles.forEach(roleId=>{
        if(!allChecked.includes(roleId)){
          let action = {'roleId':roleId,'action':0};
          action[mode] = modeId;
          actions.push(action)
        }
      })
      return actions;
    },
    buildPermissionAction(roleId){
      let allChecked = this.$refs.tree.getCheckedNodes();
      let allIds =  allChecked.map(checked=>checked.id)||[];
      allChecked.forEach(node=>{
        let pids = node.pids;
        let childrenIds = node.childrenIds
        if(pids && pids.length>0){
          allIds = allIds.concat(pids);
        }
        if(childrenIds && childrenIds.length>0){
          allIds = allIds.concat(childrenIds);
        }
      });
      allIds = allIds.map(id=>id);
      allIds = Array.from(new Set(allIds));
      let actions = [];
      allIds.filter(id=>id!=='0').forEach(id=>{
        if(!this.rbacPermissions.includes(id)){
          actions.push({'roleId':roleId,'permissionId':id,'action':1})
        }
      })
      this.rbacPermissions.filter(id=>id!=='-1').forEach(id=>{
        if(!allIds.includes(id)){
          actions.push({'roleId':roleId,'permissionId':id,'action':0})
        }
      })
      return actions;
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
    },
    allConfig(){
      this.clickMode = 50;
      this.cancelAllChecked();
    },
    allOk(){
      let userIds = this.userPage.records.filter(data=>data.checked===true).map(data=>data.id);
      let roleIds = this.rolePage.records.filter(data=>data.checked===true).map(data=>data.id);
      let permissionIds = this.$refs.tree.getCheckedNodes().map(item=>item.id);
      let flag = (userIds&&userIds.length>0?'a':'')+(roleIds&&roleIds.length>0?'b':'')+(permissionIds&&permissionIds.length>0?'c':'');
      let data = {'userIds':userIds,'roleIds':roleIds,'permissionIds':permissionIds}

      if(flag===''){
        this.allCancel(this);
        return ;
      }
      if(flag.length<2){
        notify.warning('缺少绑定关系！')
        return ;
      }
      if(flag==='ac'){
        notify.warning('不允许直接给用户分配权限！')
        return ;
      }
      let that = this;
      postRequest('/user/ur/bindAll', data).then(function (res) {
        if (res.success) {
          notify.success("操作成功！");
          that.allCancel(that);
        }
      });
    },
    allCancel(that){
      let me = that;
      if(!that){
        me = this;
      }
      me.clickMode = 0;
      me.cancelAllChecked();
    },
    cancelAllChecked(that){
      let me = that;
      if(!that){
        me = this;
      }
      me.nodeMode='';
      me.clickIndex=-1;
      me.userPage.records.filter(data=>data.checked===true).map(data=>data.checked=false);
      me.rolePage.records.filter(data=>data.checked===true).map(data=>data.checked=false);
      me.rbacUsers = [];
      me.rbacRoles = [];
      me.rbacPermissions = [];
      me.getAllPermission();
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
