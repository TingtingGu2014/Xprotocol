// === DEFAULT / CUSTOM STYLE ===
// WARNING! always comment out ONE of the two require() calls below.
// 1. use next line to activate CUSTOM STYLE (./src/themes)
// require(`./themes/app.${__THEME}.styl`)
// 2. or, use next line to activate DEFAULT QUASAR STYLE
require(`quasar/dist/quasar.${__THEME}.css`)
// ==============================

// Uncomment the following lines if you need IE11/Edge support
// require(`quasar/dist/quasar.ie`)
// require(`quasar/dist/quasar.ie.${__THEME}.css`)

import Vue from 'vue'
import Quasar from 'quasar'
import router from './router'
import Vuex from 'vuex'
import axios from 'axios'

Vue.config.productionTip = false
Vue.use(Vuex)
Vue.use(Quasar) // Install Quasar Framework

if (__THEME === 'mat') {
  require('quasar-extras/roboto-font')
}
import 'quasar-extras/material-icons'

import userModule from './modules/UserModule.js';
import protocolModule from './modules/ProtocolModule.js'
import { Toast } from 'quasar'

var Utils = require('./utils/Utils')

Vue.prototype.$toast = Toast
Vue.prototype.$utils = Utils

const store = new Vuex.Store({
    modules: {
        userModule : userModule,
        protocolModule : protocolModule,
    },
});

axios.interceptors.response.use(function (response) {
    if(response.status !== 200 && response.status !== 204){
        Toast.create.negative({html: "This is not a 200 response: "+response.status+"! "})
    }
    else{
        console.log('current response url = '+response.request.responseURL)
        console.log(response)
    }
    return response;
}, function (error) {
    var status = error.response.status
    var message = error.response.data.message
    Toast.create.negative({html: "Error response: "+status+", message: "+message})    
    sessionStorage.errorMessage = message
    router.push('/errors/' + status)
//    document.location.href = '/errors/' + status
//    alert(error.response.status + " is not a good response!")
    return Promise.reject(error);
});

Quasar.start(() => {
  /* eslint-disable no-new */
  new Vue({
    el: '#q-app',
    router,
    store,
    render: h => h(require('./App').default)
  })
})

Vue.config.devtools = true;
