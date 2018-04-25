
export default [
    {
        path: '/',
        component: () => import('layouts/default'),
        children: [
          { path: '', component: () => import('pages/Home') },
          { path: '/home', name: 'home', redirect: '/' },
          { path: '/index', name: 'index', component: () => import('pages/index') },
          { path: '/signup' , name: 'signUp', component: () => import('pages/SignUp'),  meta:{signUpType: 'signup'}},
          { path: '/userProfile/:userUUID' , name: 'userProfile', component: () => import('pages/UserProfile') },
          { path: '/admin/userProfile/:userUUID' , name: 'userProfileAdmin', component: () => ('pages/UserProfileAdmin')},
          { path: '/admin', name: 'admin', component: () => import('pages/Admin') },
          { path: '/users/:userUUID/protocols', name: 'userProtocolList', component: () => ('components/ProtocolList') },
          { path: '/users/:userUUID/protocols/:userProtocolUUID', name: 'protocol', component: () => import('pages/Protocol') },
          { path: '/login' , name: 'login', component: () => ('pages/SignUp'), meta:{signUpType: 'login'}},
          { path: '/signin' , name: 'signin', component: () => ('pages/SignUp'), meta:{signUpType: 'login'}},
          { path: '/userList', name: 'userList', component: () => ('pages/UserList') },
          { path: '/invalidsession', redirect: 'errors/440'},
          { path: '/expiredsession', redirect: 'errors/440'},
          { path: '/errors/:errorCode', name: 'errors', component: () => ('pages/Errors')},
          { path: '*', redirect: 'errors/404'},
        ]
    },

    { // Always leave this as last one
      path: '*', redirect: 'errors/404'
    },
]
