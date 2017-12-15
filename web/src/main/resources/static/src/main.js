import Vue from 'vue'
import VueRouter from 'vue-router'
import Vuex from 'vuex'
import BootstrapVue from 'bootstrap-vue'
import VuePassword from 'vue-password'

Vue.use(VueRouter)
Vue.use(Vuex)
Vue.use(BootstrapVue);
Vue.component(VuePassword)
 
import App from './App.vue'
import routejs from './Router.js'
import userModule from './UserModule.js';
import protocolModule from './ProtocolModule.js'
import exceptions from './Exceptions.js'

var Utils = require('./Utils')


const router = new VueRouter({
  mode: 'history',
  routes: routejs
})

const store = new Vuex.Store({
    modules: {
        userModule : userModule,
        protocolModule : protocolModule,
    },
});

//axios.interceptors.request.use(function (config) {
//    alert('ok');
//    return config;
//  }, function (error) {
//    // Do something with request error
//    alert('not ok');
//    return Promise.reject(error);
//  });

axios.interceptors.response.use(function (response) {
    if(response.status !== 200 && response.status !== 204){
        alert("This is not a 200 response!")
    }
    return response;
}, function (error) {
    var status = error.response.status
    var message = error.response.data.message
    sessionStorage.errorMessage = message
    document.location.href = '/errors/' + status
//    alert(error.response.status + " is not a good response!")
    return Promise.reject(error);
});

new Vue({
    router,
    store,
    el: '#app',
    render:h => h(App)
});

Vue.config.devtools = true;