import Vue from 'vue'

const vue = new Vue();

class Notify {

  success = (text,config) => {
    let defaultConfig  = {}
    defaultConfig['icon'] = 'icon-check-circle'
    defaultConfig['color'] = 'success'
    copyConfig(defaultConfig,config)
    createNotify(text,defaultConfig)
  }

  info = (text,config) => {
    let defaultConfig = {}
    defaultConfig['icon'] = 'icon-message-square'
    defaultConfig['color'] = 'dark'
    copyConfig(defaultConfig,config)
    createNotify(text,defaultConfig)
  }

  warning = (text,config) => {
    let defaultConfig = {}
    defaultConfig['icon'] = 'icon-alert-circle'
    defaultConfig['color'] = 'warning'
    copyConfig(defaultConfig,config)
    createNotify(text,defaultConfig)
  }

  danger = (text,config) => {
    let defaultConfig = {}
    defaultConfig['icon'] = 'icon-zap'
    defaultConfig['color'] = 'danger'
    copyConfig(defaultConfig,config)
    createNotify(text,defaultConfig)
  }

  show = (text,config) => {
    config = typeof(config)=="object"&&config!=null?config:{}
    createNotify(text,config)
  }

}

function copyConfig(defaultConfig,config) {
  for(let field in config){
    if(config[field]){
      defaultConfig[field] = config[field]
    }
  }
}

function createNotify(text,config) {
  let defaultConfig = {'title':'系统提示','text':text,'iconPack':'feather','icon':'icon-check','position':'top-center','color':'success'}
  if(config){
    for(let field in config){
      defaultConfig[field] = config[field]
    }
  }
  vue.$vs.notify({
    title: defaultConfig.title,
    text: defaultConfig.text,
    iconPack: defaultConfig.iconPack,
    icon: defaultConfig.icon,
    position: defaultConfig.position,
    color: defaultConfig.color
  })
}
export default new Notify();

