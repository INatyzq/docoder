/*=========================================================================================
  File Name: router.js
  Description: Routes for vue-router. Lazy loading is enabled.
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
==========================================================================================*/


import Vue from 'vue'
import Router from 'vue-router'
import userService from "@/service/userService";
import stringUtil from "@/core/utils/stringUtil";

const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

Vue.use(Router)

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  scrollBehavior() {
    return {x: 0, y: 0}
  },
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('./views/Home.vue')
    },
    {
      // =============================================================================
      // MAIN LAYOUT ROUTES
      // =============================================================================

      path: '/app',
      component: () => import('./layouts/main/Main.vue'),
      children: [
        // =============================================================================
        // 系统设置
        // =============================================================================
        {
          path: 'home/system/permission',
          name: 'home-system-permission',
          component: () => import('@/views/system/system/permission/PermissionManage.vue'),
          meta: {
            breadcrumb: [
              {title: '主页'},
              {title: '系统设置'},
              {title: '权限管理', active: true}
            ],
            pageTitle: '权限管理',
            rule: 'editor'
          }
        },
        {
          path: 'home/system/role',
          name: 'home-system-role',
          component: () => import('@/views/system/system/role/RoleManage.vue'),
          meta: {
            breadcrumb: [
              {title: '主页'},
              {title: '系统设置'},
              {title: '角色管理', active: true}
            ],
            pageTitle: '角色管理',
            rule: 'editor'
          }
        },
        {
          path: 'home/system/user/user-list',
          name: 'home-system-user-user-list',
          component: () => import('@/views/system/system/user/user-list/UserList.vue'),
          meta: {
            breadcrumb: [
              {title: '主页'},
              {title: '系统设置'},
              {title: '用户列表', active: true}
            ],
            pageTitle: '用户列表'
          }
        },
        {
          path: 'home/system/permission-config',
          name: 'home-system-user-permission-config',
          component: () => import('@/views/system/system/permission/config/PermissionConfig'),
          meta: {
            breadcrumb: [
              {title: '主页'},
              {title: '系统设置'},
              {title: '权限配置', active: true}
            ],
            pageTitle: '权限配置'
          }
        },
        {
          path: 'home/system/user/user-view',
          name: 'home-system-user-user-view',
          component: () => import('@/views/system/system/user/UserView.vue'),
          meta: {
            breadcrumb: [
              {title: '主页'},
              {title: '系统设置'},
              {title: '用户列表', active: true}
            ],
            pageTitle: '我的账户'
          }
        },
        {
          path: 'home/system/user/user-edit/:userId',
          name: 'home-system-user-user-edit',
          component: () => import('@/views/system/system/user/user-edit/UserEdit.vue'),
          meta: {
            breadcrumb: [
              {title: '主页'},
              {title: '系统设置'},
              {title: '用户列表', url: '/app/home/system/user/user-list'},
              {title: '用户详情', active: true}
            ],
            pageTitle: '用户详情'
          }
        },
        // =============================================================================
        // 账户设置
        // =============================================================================
        {
          path: 'home/system/account',
          name: 'home-system-account',
          component: () => import('@/views/system/account/UserView.vue'),
          meta: {
            breadcrumb: [
              {title: '主页'},
              {title: '账户设置'},
              {title: '我的账户', active: true}
            ],
            pageTitle: '我的账户'
          }
        },

        // =============================================================================
        // 内容
        // =============================================================================
        {
          path: 'home/content/notes/dir-setting',
          name: 'home-content-notes-dir-setting',
          component: () => import('@/views/content/notes/NoteDirSetting.vue'),
          meta: {
            breadcrumb: [
              {title: '主页'},
              {title: '笔记'},
              {title: '目录设置', active: true}
            ],
            pageTitle: '目录设置'
          }
        },
        {
          path: 'home/content/notes/edit',
          name: 'home-content-notes-edit',
          component: () => import('@/views/content/notes/NoteEdit.vue'),
          meta: {
            breadcrumb: [
              {title: '主页'},
              {title: '笔记'},
              {title: '写笔记', active: true}
            ],
            pageTitle: '写笔记'
          }
        },
        {
          path: 'home/content/notes/view-board',
          name: 'home-content-notes-view-board',
          component: () => import('@/views/content/notes/NoteViewBoard.vue'),
          meta: {
            breadcrumb: [
              {title: '主页'},
              {title: '笔记'},
              {title: '笔记看板', active: true}
            ],
            pageTitle: '笔记看板'
          }
        },

        {
          path: 'error-404',
          name: 'error-404',
          component: () => import('./views/pages/Error404.vue')
        },
        {
          path: 'error-500',
          name: 'error-500',
          component: () => import('./views/pages/Error500.vue')
        }
      ],
    },
    // =============================================================================
    // FULL PAGE LAYOUTS
    // =============================================================================
    {
      path: '/side',
      component: () => import('@/layouts/full-page/FullPage.vue'),
      children: [
        {
          path: 'auth/login',
          name: 'auth-login',
          component: () => import('@/views/auth/login/LoginX.vue'),
          meta: {
            rule: 'editor'
          }
        },
        {
          path: 'auth/register',
          name: 'auth-register',
          component: () => import('@/views/auth/register/Register.vue')
        },
        {
          path: 'auth/forgot_password',
          name: 'auth-forgetPassword',
          component: () => import('@/views/auth/ForgotPassword.vue')
        },
        {
          path: 'auth/email_confirm/:account',
          name: 'auth-emailConfirm',
          component: () => import('@/views/auth/account/EmailConfirm.vue')
        }
      ]
    },
    {
      path: '/pages',
      component: () => import('@/layouts/full-page/FullPage.vue'),
      children: [
        {
          path: 'error-500',
          name: 'error-500',
          component: () => import('./views/pages/Error500.vue')
        },
        {
          path: 'error-404',
          name: 'error-404',
          component: () => import('./views/pages/Error404.vue')
        }
      ]

    },
    // Redirect to 404 page, if no match found
    {
      path: '*',
      redirect: '/pages/error-404'
    }
  ],
})

router.afterEach(() => {
  // Remove initial loading
  const appLoading = document.getElementById('loading-bg')
  if (appLoading) {
    appLoading.style.display = "none";
  }
})

router.beforeEach((to, from, next) => {

  const isUserLoggedIn = stringUtil.isNotBlank(userService.getUserDetail().token);
  if (
    !to.path.startsWith("/side/auth") &&
    to.path !== "/side/auth/login" &&
    to.path !== "/side/auth/forgot-password" &&
    to.path !== "/side/error-404" &&
    to.path !== "/side/error-500" &&
    to.path !== "/side/register" &&
    to.path !== "/callback" &&
    to.path !== "/pages/comingsoon" &&
    (!isUserLoggedIn)
  ) {
    router.push({path: '/side/auth/login'})
  }

  // If auth required, check login. If login fails redirect to login page
  /*if (to.meta.authRequired) {
    if (!(auth.isAuthenticated() || firebaseCurrentUser)) {
      router.push({path: '/pages/login', query: {to: to.path}})
    }
  }*/

  return next()
  // Specify the current path as the customState parameter, meaning it
  // will be returned to the application after auth
  // auth.login({ target: to.path });

})

export default router
