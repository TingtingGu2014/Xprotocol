/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var Utils = require('./Utils')

const protocolModule = {
  // This makes your getters, mutations, and actions accessed by, eg: 'myModule/myModularizedNumber' instead of mounting getters, mutations, and actions to the root namespace.
  namespaced: true,
  state: {
      protocols: [],
  },
  getters: {    
        getProtocols: state => state.protocols,
        getProtocolsByUserUUID: state => (userUUID) => {
            return state.protocols[userUUID]
        },
        getProtocolsByUserUUIDANDProtocolUUID: state => (userUUID, protocolUUID) => {
            var protocols = state.protocols[userUUID]
            if(!Utils.isEmpty(protocols)){
                return protocols[protocolUUID]
            }
            return null
        }
  },
  mutations: {
    setProtocols(state, protocols) {
        state.protocols = protocols
    },
    setProtocolsByUserUUID(state, protocols) {
        state.protocols[protocol.userUUID] = protocols
    },
    setProtocolByUserUUIDANDProtocolUUID(state, protocol) {
        var protocols = state.protocols
        if(Utils.isEmpty(protocolsByUser)){
            protocols = []
        }
        var protocolsByUser = protocols[protocol.userUUID]
        if(Utils.isEmpty(protocolsByUser)){
            protocolsByUser = []
        }
        protocolsByUser[protocol.userProtocolUUID] = protocol
    },
  }
}

export default protocolModule