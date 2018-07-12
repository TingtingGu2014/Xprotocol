
export default [
    {
        path: '/',
        component: () => import('layouts/Layout'),
        children: [
          { path: '', component: () => import('pages/Home') },
          { path: '/home', name: 'home', redirect: '/' },
          { path: '/users/:userUUID', name: 'userHome', component: () => import('pages/UserHome') },
          { path: '/signup' , name: 'signUp', component: () => import('pages/SignUp'),  meta:{signUpType: 'signup'}},
          { path: '/userProfile/:userUUID' , name: 'userProfile', component: () => import('pages/UserProfile') },
          { path: '/admin/userProfile/:userUUID' , name: 'userProfileAdmin', component: () => import('pages/UserProfileAdmin')},
          { path: '/admin', name: 'admin', component: () => import('pages/Admin') },
          { path: '/users/:userUUID/protocols', name: 'userProtocolList', component: () => import('components/ProtocolList') },
          { path: '/users/:userUUID/protocols/:userProtocolUUID', name: 'protocol', component: () => import('pages/Protocol') },
          { path: '/login' , name: 'login', component: () => import('pages/SignUp'), meta:{signUpType: 'login'}},
          { path: '/signin' , name: 'signin', component: () => import('pages/SignUp'), meta:{signUpType: 'login'}},
          { path: '/userList', name: 'userList', component: () => import('pages/UserList') },
          { path: '/invalidsession', redirect: 'errors/440'},
          { path: '/expiredsession', redirect: 'errors/440'},          
          { path: '*', redirect: 'errors/404'},
        ]
    },
    { path: '/errors/:errorCode', name: 'errors', component: () => import('pages/Errors')},
    { // Always leave this as last one
      path: '*', redirect: 'errors/404'
    },
]
