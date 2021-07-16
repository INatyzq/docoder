<template>
  <vx-card title="目录配置">
    <vs-input class="inputx tree-search-input float-left mr-2" placeholder="查询..." v-model.lazy="searchword"/>
    <vs-button color="#3EC9D6" type="filled" @click="search"  icon-pack="feather" icon="icon-search">查询</vs-button>
    <v-tree ref="tree" :data="treeData" :tpl="tpl"/>

    <div class="demo-alignment">
      <vs-popup classContent="popup-example" title="目录详情" :active.sync="opendirDetail">
        <div class="vx-row mb-2">
          <div class="vx-col w-full">
            <vs-input class="w-full"
                      v-validate="'required'" data-vv-validate-on="blur"
                      v-model="dirData.dirName"
                      icon-pack="feather" icon="icon-bookmark" icon-no-border label="名称"/>
            <span class="text-danger text-sm">{{ errors.first('目录名称') }}</span>
          </div>
        </div>
        <div class="vx-row mb-2">
          <div class="vx-col w-full">
            <vs-input class="w-full" v-model="dirData.icon" icon-pack="feather"
                      :icon="dirData.icon||'icon-star'" icon-no-border label="图标"/>
          </div>
        </div>
        <div class="vx-row mb-2">
          <div class="vx-col">
            <label>排序号</label>
            <vs-input-number v-model="dirData.orderNum" label-placeholder="排序号"/>
          </div>
        </div>
        <div class="vx-row mb-6">
          <div class="vx-col w-full">
            <vs-textarea class="w-full" v-model="dirData.dirDesc" icon-no-border label="说明"/>
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
import {deleteRequest, getRequest, postRequest} from "@/core/http/axiosClient";
import notify from "../../../core/notify/notify";

export default {
  components: {
    VTree,
    'v-select': VueSelect
  },
  computed: {
    validateForm() {
      return this.dirData.dirName !== '';
    }
  },
  mounted() {
    this.getTree();
  },
  data() {
    return {
      opendirDetail: false,
      searchword: '',
      treeData: [
        {
          dirName: '根目录',
          id: 0,
          icon: 'icon-home',
          expanded: true,
          children: []
        }
      ],
      initData: {
        dirName: '',
        dirType: '1',
        dirDesc: '',
        resourceUrl: '',
        icon: 'icon-star',
        orderNum: 1,
        dataFlag: 1
      },
      dirData: {
        dirName: '',
        dirType: '1',
        dirDesc: '',
        resourceUrl: '',
        icon: 'icon-star',
        orderNum: 1,
        dataFlag: 1
      },
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
        <span class={titleClass} domPropsInnerHTML={node.dirName} onClick={() => {
        this.$refs.tree.nodeSelected(node)
      }}>
    </span>
      <span class={this.moveId || node.dirType == 4? 'hidden' : ''}>
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
    <span class={this.moveId == node.id || node.dirType == 4?'hidden':''}>
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
        <small>{node.dirType == 1 ? '分类' : node.dirType == 2 ? '导航': node.dirType == 3 ? '菜单' : '按钮'}&nbsp;</small>
      </label>
      <br/>
      <span>
      <vs-divider></vs-divider>
      </span>
      </span>
    },
    viewNode(node) {
      this.dirData = {'id':node.id,'parentId':node.parent,'dirName':node.dirName,
        'dirType':node.dirType,'dirDesc':node.dirDesc,'resourceUrl':node.resourceUrl,'icon':node.icon,'orderNum':node.orderNum};
      this.opendirDetail = true;
    },
    addNode(node) {
      this.dirData.parentId = node.id;
      this.opendirDetail = true;
    },
    saveData() {
      this.opendirDetail = false;
      this.$vs.loading();
      let that = this;
      postRequest('/content/noteDir/saveOrUpdate', this.dirData).then(function (res) {
        if (res.success) {
          that.dirData = that.initData;
          that.getTree();
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
      postRequest('/content/noteDir/move',{'id':this.moveId,'parentId':node.id}).then(function (res) {
        if (res.success) {
          that.getTree();
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
      deleteRequest('/content/noteDir/' + node.id).then(function (res) {
        if (res.success) {
          that.getTree();
          notify.success('操作已完成！');
        }
      })
    },
    getTree() {
      this.$vs.loading();
      let that = this;
      getRequest('/content/noteDir/tree').then(function (res) {
        if (res.success) {
          that.treeData[0].children = res.data;
          that.renderTree();
        }
      })
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
