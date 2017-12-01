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
      protocols: {},
  },
  getters: {    
        getProtocols: state => state.protocols,
        getProtocolsByUserUUID: state => (userUUID) => {
            var protocols = state.protocols
            console.log('loading protocols by user UUID from store...\n')
            if(Utils.isEmpty(protocols)){
                return null
            }
            return protocols[userUUID]
        },
        getProtocolsByUserUUIDANDProtocolUUID: state => (userUUID, protocolUUID) => {
            var protocols = state.protocols
            console.log('loading protocol from store...\n')
            if(Utils.isEmpty(protocols)){
                return null
            }
            var userProtocols = protocols[userUUID]
            if(!Utils.isEmpty(userProtocols) && Array.isArray(userProtocols)){
                for(var i = 0; i < userProtocols.length; i++){
                    if(userProtocols[i].userProtocolUUID === protocolUUID){
                        return userProtocols[i]
                    }
                }
            }
            return null
        }
  },
  mutations: {
    setProtocols(state, protocols) {
        state.protocols = protocols
    },
    setProtocolsByUserUUID(state, userUUID, data) {
        var protocols = state.protocols
        if(Utils.isEmpty(protocols)){
            protocols = {}
        }
        protocols[userUUID] = data
    },
    setProtocolByUserUUIDANDProtocolUUID(state, protocol) {
        console.log(protocol.userUUID)
        $.each(protocol, function(key, value){
            console.log('\n '+key+' = '+value)
        })
        var protocols = state.protocols
        if(Utils.isEmpty(protocolsByUser)){
            protocols = {}
        }
        var protocolsByUser = protocols[protocol.userUUID]
        if(Utils.isEmpty(protocolsByUser)){
            protocolsByUser = []
        }
        protocolsByUser.push(protocol)
        protocols[protocol.userUUID] = protocolsByUser;
        state.protocols = protocols
        console.log('protocol has been stored into store.')
    },
  }
}

export default protocolModule