import Vue from 'vue'
import Vuex from 'vuex'

import example from './module-example'
import protocols from './protocols'
import users from './users'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    example,
    protocols,
    users
  }
})

export default store
