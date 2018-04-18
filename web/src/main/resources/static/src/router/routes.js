
export default [
    {
        path: '/',
        component: () => import('components/Navbar.vue'),
        children: [
          { path: '', component: () => import('pages/Home') },
          { path: '/home', redirect: '/' },
          { path: '/signup' , name: 'signUp', component: () => import('pages/SignUp'),  meta:{signUpType: 'signup'}},
        ]
    },

//  { path: '/', name: 'home', component: () => import(`pages/Home.vue`) },
  // { path: '/userProfile/:userUUID' , name: 'userProfile', component: load('UserProfile') },
  // { path: '/admin/userProfile/:userUUID' , name: 'userProfileAdmin', component: load('UserProfileAdmin')},
  // { path: '/signup' , name: 'signUp', component: load('SignUp'),  meta:{signUpType: 'signup'}},
//    { path: '/login' , name: 'login', component: load('SignUp'), meta:{signUpType: 'login'}},
  // { path: '/signin' , name: 'signin', component: load('SignUp'), meta:{signUpType: 'login'}},
  // { path: '/userList', name: 'userList', component: load('UserList') },
  // { path: '/users/:userUUID/protocols', name: 'userProtocolList', component: load('ProtocolList') },
  // { path: '/users/:userUUID/protocols/:userProtocolUUID', name: 'userProtocol', component: load('Protocol') },
  // { path: '/admin', name: 'admin', component: load('Admin') },
  // { path: '/home', redirect: '/' },
  // { path: '/invalidsession', redirect: 'errors/440'},
  // { path: '/expiredsession', redirect: 'errors/440'},
  // { path: '/errors/:errorCode', name: 'errors', component: load('Errors')},
  // { path: "*", component: load('PageNotFound') }

  
  { // Always leave this as last one
    path: '*',
    component: () => import('pages/404')
  },
]
