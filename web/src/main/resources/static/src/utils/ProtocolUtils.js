import {axiosInstance} from './AxiosConfig'
import * as utils from '../utils/Utils.js'
import { Notify } from 'quasar'

export const MAXIMUM_PROTOCOL_FILE_NUMBER = 3
export const MAXIMUM_PROTOCOL_FILE_SIZE = 25

export function deleteProtocolFile(location){
    return axios({
        method: 'delete',
        url: location,
        dataType: 'json',
        headers: {'X-Requested-With': 'XMLHttpRequest'},
    })
    .then( (response) => {
        var status = response.status;
        if(status == 200 || status == "200"){              
            alert('The file at '+location+' has been successfully deleted!')
        }
        else{
            alert("status " + status + ": cannot delete the file at "+location+"!");
        }  
        return status
    })
    .catch( (error) => {
        console.log(error);
    });
}

export function getRecentPublicProtocols(){
    return axiosInstance.get("/api/protocolToUsers").then( (response) => {
        if(response.status === 200){
            let data = response.data
            let protocolToUsers = data.protocolToUsers
            let users = data.users
            let usersMap = {}    
            let protocolToUserData = []
            if(utils.isEmpty(protocolToUsers)){
                this.rawData = null
                return false
            }
            if(!utils.isEmpty(users)){
                for(var i in users){
                    let user = users[i]
                    usersMap[user.userUUID] = user
                }
            }
            for(var j in protocolToUsers){
                let protocolToUser = protocolToUsers[j]
                let userUUID = protocolToUser.userUUID
                let user = usersMap[userUUID]
                let protocolUUID = protocolToUser.protocolUUID
                if(!utils.isEmpty(user)){
                    protocolToUser.userName = utils.getUserNameFromUserObj(user)
                    protocolToUser.email = user.email
                }
                protocolToUser.time = utils.outputDateTime(utils.getTimeFromTimeUUID(protocolUUID))
                protocolToUserData.push(protocolToUser)
            }
            return protocolToUserData
        }
        else{
            Notify.create({message: `Something is wrong in retrieving new public protocols.`, timeout: 3000, color: 'negative'})
            return null
        }     
    })
}