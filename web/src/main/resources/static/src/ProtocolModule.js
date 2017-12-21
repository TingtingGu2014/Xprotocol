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
            else{
                console.log('protocols for user: '+userUUID+':\n')
                console.log(protocols)
            }
            var protocolArr = []
            var protocolsByUser = protocols[userUUID]
            for (var protocolUUID in protocolsByUser){
                protocolArr.push(protocolsByUser[protocolUUID])
            }
            return protocolArr
        },
        getProtocolsByUserUUIDANDProtocolUUID: state => (userUUID, protocolUUID) => {
            var protocols = state.protocols
            console.log('loading protocol from store...\n')
            if(Utils.isEmpty(protocols)){
                return null
            }

            var protocolsByUser = protocols[userUUID]
            if(!Utils.isEmpty(protocolsByUser)){
                for (var protocolUUIDKey in protocolsByUser){
                    if(protocolUUID === protocolUUIDKey){
                        return protocolsByUser[protocolUUIDKey]
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
    setProtocolsByUserUUID(state, protocolsData) {
        var protocols = state.protocols
        if(Utils.isEmpty(protocols)){
            protocols = {}
        }
        try{
            if(protocolsData instanceof Object){
                var keys = Object.keys(protocolsData)
                var userUUID = keys[0]
                var protocolsByUser = protocols[userUUID]
                if(Utils.isEmpty(protocolsByUser)){
                    protocolsByUser = []
                }
                protocolsByUser = protocolsData[userUUID]
                var protocolsByUserInStore = protocols[userUUID]
                if(Utils.isEmpty(protocolsByUserInStore)){
                    protocolsByUserInStore = {}
                }
                for (var i = 0; i < protocolsByUser.length; i++){
                    protocolsByUserInStore[protocolsByUser[i].userProtocolUUID] = protocolsByUser[i]
                }                
                protocols[userUUID] = protocolsByUserInStore;
                state.protocols = protocols
            }
            else{
                throw 'ProtocolsData is not an object!'
            }            
        }
        catch(ex){
            console.log('error: \n' + ex)
            return null
        }
    },
    setProtocolByUserUUIDANDProtocolUUID(state, protocol) {
        var protocols = state.protocols
        if(Utils.isEmpty(protocols)){
            protocols = {}
        }
        var protocolsByUser = protocols[protocol.userUUID]
        if(Utils.isEmpty(protocolsByUser)){
            protocolsByUser = {}
        }
        protocolsByUser[protocol.userProtocolUUID] = protocol
        protocols[protocol.userUUID] = protocolsByUser;
        state.protocols = protocols
    },
  }
}

export default protocolModule