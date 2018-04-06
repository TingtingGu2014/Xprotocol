//import { isEmpty } from '../../utils/Utils.js';

export const setProtocols = (state) => {
	state.protocols = protocols
}

export const setProtocolsByUserUUID = (state, protocolsData) => {
    var protocols = state.protocols
    if(this.$utils.isEmpty(protocols)){
        protocols = {}
    }
    try{
        if(protocolsData instanceof Object){
            var keys = Object.keys(protocolsData)
            var userUUID = keys[0]
            var protocolsByUser = protocols[userUUID]
            if(this.$utils.isEmpty(protocolsByUser)){
                protocolsByUser = []
            }
            protocolsByUser = protocolsData[userUUID]
            var protocolsByUserInStore = protocols[userUUID]
            if(this.$utils.isEmpty(protocolsByUserInStore)){
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
}

export const setProtocolByUserUUIDANDProtocolUUID = (state, protocol) =>{
    var protocols = state.protocols
        if(this.$utils.isEmpty(protocols)){
            protocols = {}
        }
        var protocolsByUser = protocols[protocol.userUUID]
        if(this.$utils.isEmpty(protocolsByUser)){
            protocolsByUser = {}
        }
        protocolsByUser[protocol.userProtocolUUID] = protocol
        protocols[protocol.userUUID] = protocolsByUser;
        state.protocols = protocols
}