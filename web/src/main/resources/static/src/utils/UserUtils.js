import {axiosInstance} from './AxiosConfig'
import * as utils from '../utils/Utils.js'
import { Notify } from 'quasar'

export function signIn(email, password) {
    
    return axiosInstance.post(
        '/api/signIn',
        {},
        {
            auth: {
                username: email,
                password: password,
            },
        }
    )
    .then( (response) => {
        var status = response.status;
        if(status == 200 || status == "200"){
            var data = response.data
            localStorage.userInfo = JSON.stringify(data) 
            localStorage.loggedIn = true
            return data
        }
        else{
            Notify.create({message: `Response code is not 200!`, timeout: 3000, color: 'negative'})
            return;
        }                                   
    })
    .catch( (error) => {
        console.log(error);
    });      
}

export function signOut() {
    return axiosInstance.post('/api/logout')
    .then( (response) => {
        var status = response.status;
        var data = response.data;

        if(status == 200 || status == 204){
            localStorage.userEmail = ''
            localStorage.userName = ''
            localStorage.userUUID = ''
            localStorage.userInfo = ''
            localStorage.userInfo = null
            var loggedIn = localStorage.loggedIn //utils.readCookie('loggedIn')
            if(loggedIn == true){
                utils.eraseCookie('loggedIn')
            }
            document.location.href = '/home'
            return data
        }
        else{
            Notify.create({message: `Response code is not 200!`, timeout: 3000, color: 'negative'})
        }                                   
      })
      .catch( (error) => {
        console.log(error);
      });
}

export function signUp(data) {
    return axiosInstance.post(
        '/api/signUp',
        data
    )
    .then( (response) => {
        var status = response.status;
        var responseData = response.data;

        if(status == 200){
            localStorage.userInfo = JSON.stringify(responseData) 
            localStorage.loggedIn = true
            return responseData
        }
        else{
            Notify.create({message: "Error status : " + status + "\nError message: "+response.message, druation: 3000, color: 'negative'})
            return false;
        }                                   
      })
      .catch( (error) => {
            Notify.create({message: error.message, druation: 3000, color: 'negative'})
            console.log(error);
      });     
}

export function getUserDetails(userUUID, isAdmin = false) {
    let url = ''
    if(isAdmin === true){
        url = '/api/admin/users/'+userUUID+'/userDetails'
    }
    else{
        url = '/api/users/'+userUUID+'/userDetails'
    }
    return axiosInstance.get(url).then( (response) => {
        if(response.status === 200){
            return response.data;
        }
        else{
            Notify.create({message: `Something is wrong in pulling user profile.`, timeout: 3000, color: 'negative'})
            return null
        }     
    })
}

export function getUserListByAdmin(){
    return axiosInstance.get('/api/admin/users')
    .then( (response) => {
        var status = response.status;
        var data = response.data
        if(status == 200 || status == "200"){
            return data
        }
        else{
            Notify.create({message: `Response code is not 200!`, timeout: 3000, color: 'negative'})
        }                                   
    })
    .catch( (error) => {
        console.log(error);
    });  
}

export function getAllUserRoles() {
    return axiosInstance.get('/api/admin/roles')
    .then( (response) => {
        var status = response.status;
        if(status == 200 || status == "200"){  
            var roleNames = ''
            var roles = response.data
            for(var i = 0; i < roles.length; i++){
                if(roles[i].hasOwnProperty("roleName")){
                    roleNames += roles[i].roleName + ","
                }
            }
            roleNames = roleNames.substr(0,roleNames.length-1)
            return roleNames
        }
        else{
            Notify.create({message: "status " + status + ", cannot get the roles!", timeout: 3000, color: 'negative'})
        }                                   
    })
    .catch( (error) => {
        console.log(error);
    }); 
}

export function updateUserDetails(userUUID, profile, isAdmin = false){
    
    if(utils.isEmpty(userUUID)){
        Notify.create({message: 'User UUID cannot be empty!', timeout: 3000, color: 'negative'})
        return null
    }
    
    if(utils.isEmpty(profile)){
        Notify.create({message: 'User profile data cannot be empty!', timeout: 3000, color: 'negative'})
        return null
    }
    
    let url = ''
    if(isAdmin === false){
        url = '/api/users/'+userUUID+'/userDetails'
    }
    else{
        url = '/api/admin/users/'+userUUID+'/userDetails'
    }
        
    return axiosInstance.post(url, profile )
    .then( (response) => {
        if(response.status === 200){
            Notify.create({message: 'Your profile information has been updated!', timeout: 3000, color: 'positive'})
            return response.data
        }

    })
    .catch(function (error) {
        Notify.create({message: 'Your profile is NOT updated, sorry!', timeout: 3000, color: 'positive'})
        console.log(error);
    });
}