import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

function load (component) {
  // '@' is aliased to src/components
  return () => import(`@/${component}.vue`)
}

export default new VueRouter({
  /*
   * NOTE! VueRouter "history" mode DOESN'T works for Cordova builds,
   * it is only to be used only for websites.
   *
   * If you decide to go with "history" mode, please also open /config/index.js
   * and set "build.publicPath" to something other than an empty string.
   * Example: '/' instead of current ''
   *
   * If switching back to default "hash" mode, don't forget to set the
   * build publicPath back to '' so Cordova builds work again.
   */

  mode: 'hash',
  scrollBehavior: () => ({ y: 0 }),

  routes: [
    { path: '/', name: 'home', component: load('Home') },
    { path: '/userProfile/:userUUID' , name: 'userProfile', component: load('UserProfile') },
    { path: '/admin/userProfile/:userUUID' , name: 'userProfileAdmin', component: load('UserProfileAdmin')},
    { path: '/signup' , name: 'signUp', component: load('SignUp'),  meta:{signUpType: 'signup'}},
    { path: '/login' , name: 'login', component: load('SignUp'), meta:{signUpType: 'login'}},
    { path: '/userList', name: 'userList', component: load('UserList') },
    { path: '/users/:userUUID/protocols', name: 'userProtocolList', component: load('ProtocolList') },
    { path: '/users/:userUUID/protocols/:userProtocolUUID', name: 'userProtocol', component: load('Protocol') },
    { path: '/admin', name: 'admin', component: load('Admin') },
    { path: '/home', redirect: '/' },
    { path: '/invalidsession', redirect: 'errors/440'},
    { path: '/expiredsession', redirect: 'errors/440'},
    { path: '/errors/:errorCode', name: 'errors', component: load('Errors')},
    { path: "*", component: load('PageNotFound') }
  ]
})
