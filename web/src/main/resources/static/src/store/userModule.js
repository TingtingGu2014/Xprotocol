const userModule = {
  // This makes your getters, mutations, and actions accessed by, eg: 'myModule/myModularizedNumber' instead of mounting getters, mutations, and actions to the root namespace.
  namespaced: true,
  state: {
    detailsFetched: false,
    details: null,
  },
  getters: {
    isUserDetailsFetched: state => state.detailsFetched,    
    getUserDetails: state => state.details,
  },
  mutations: {
    setDetailsFetched(state, newDetailsFetched) {
      state.detailsFetched = newDetailsFetched
    },
    setUserDetails(state, newDetails) {
      state.details = newDetails
    },
  }
}

export default userModule