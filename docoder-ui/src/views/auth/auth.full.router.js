/*=========================================================================================
  File Name: router.js
  Description: Routes for vue-router. Lazy loading is enabled.
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/

const AuthRouter = [
  // =============================================================================
  // FULL PAGE LAYOUTS
  // =============================================================================
  {
    path: 'auth',
    component: () => import('@/layouts/full-page/FullPage.vue'),
    children: [
      // =============================================================================
      // PAGES
      // =============================================================================
      {
        path: 'login',
        name: 'auth-login',
        component: () => import('@/views/auth/login/LoginX.vue'),
        meta: {
          rule: 'editor'
        }
      },
      {
        path: 'register',
        name: 'auth-register',
        component: () => import('@/views/auth/register/Register.vue')
      },
      {
        path: 'forgot_password',
        name: 'auth-forgetPassword',
        component: () => import('@/views/auth/ForgotPassword.vue')
      },
      {
        path: 'email_confirm/:account',
        name: 'auth-emailConfirm',
        component: () => import('@/views/auth/account/EmailConfirm.vue')
      }
    ]
  }
]

export default AuthRouter
