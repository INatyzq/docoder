import Vue from 'vue'

const vue = new Vue();

class Dialog {

  confirm = (text,func) => {
    vue.$vs.dialog({
      type: 'confirm',
      color: 'waring',
      title: `确认提示`,
      acceptText:'确定',
      cancelText:'取消',
      text: text,
      accept: func
    })
  }

}

export default new Dialog();

