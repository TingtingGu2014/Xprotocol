
export const getProtocols = (state) => {
	return state.protocols
}

export const getProtocolsByUserUUID = (state) => (userUUID) => {
    var protocols = state.protocols
    console.log('loading protocols by user UUID from store...\n')
    if(this.$utils.isEmpty(protocols)){
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
}

export const getProtocolsByUserUUIDANDProtocolUUID = (state) => (userUUID, protocolUUID) => {
    var protocols = state.protocols
    console.log('loading protocol from store...\n')
    if(this.$utils.isEmpty(protocols)){
        return null
    }

    var protocolsByUser = protocols[userUUID]
    if(!this.$utils.isEmpty(protocolsByUser)){
        for (var protocolUUIDKey in protocolsByUser){
            if(protocolUUID === protocolUUIDKey){
                return protocolsByUser[protocolUUIDKey]
            }
        }
    }
    return null
}