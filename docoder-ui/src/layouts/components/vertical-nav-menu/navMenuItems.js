/*=========================================================================================
  File Name: sidebarItems.js
  Description: Sidebar Items list. Add / Remove menu items from here.
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/


export default [
  {
    url: null,
    name: '系统配置',
    tag: '2',
    tagColor: 'warning',
    icon: 'SettingsIcon',
    i18n: 'Dashboard',
    submenu: [
      {
        url: '/app/home/system/permission',
        name: '权限管理',
        slug: 'dashboard-analytics',
        i18n: 'Analytics'
      },
      {
        url: '/app/home/system/role',
        name: '角色管理',
        slug: 'dashboard-analytics',
        i18n: 'Analytics'
      }
    ]
  }
]
