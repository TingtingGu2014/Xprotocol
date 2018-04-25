import Vue from 'vue'
import Vuex from 'vuex'

import protocolModule from './protocolModule'
import userModule from './userModule'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    protocolModule,
    userModule
  }
})

export default store
