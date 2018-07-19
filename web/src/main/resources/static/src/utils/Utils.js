
import axios from 'axios'
import { Notify } from 'quasar'
import {SERVER_IP, SERVER_PORT} from './Constants'

export function isEmpty(obj){
    if(obj === null || typeof obj === 'undefined' || obj === ''){
        return true
    }
    if(obj instanceof Array && obj.length === 0){
        return true
    }
    if(obj instanceof Object){
        for(var key in obj) {
            if (obj.hasOwnProperty(key) || obj[key]) {
                return false;
            }
        }
        return true
    }
    return false
}

export function removeArrayElementByValue(array, element) {
    const index = array.indexOf(element)
    
    if (index !== -1) {
        array.splice(index, 1)
    }
    return array
}

// The following three functions are from https://scotch.io/tutorials/easily-create-read-and-erase-cookies-with-jquery 
export function createCookie(name,value,days) {    
    if (days) {
        var date = new Date();
        date.setTime(date.getTime()+(days*24*60*60*1000));
        var expires = "; expires="+date.toGMTString();
    }
    else var expires = "";
    var cookies = document.cookie;
    if(isEmpty(cookies)){
        cookieMaster.setCookieValue(SERVER_IP+':'+SERVER_PORT, name, value,
        function() {
            Notify.create({message: `Cookie created!`, timeout: 3000, color: 'positive'})
        },
        function(error) {
            Notify.create({message: `Error created!`, timeout: 3000, color: 'negative'})
        })
    }
    else{
        return Promise.resolve(function(resolve, reject) {
            document.cookie = name+"="+value+expires+"; path=/"
        });        
    }    
}

export function readCookie(name) {
    let cookies = document.cookie
    if(isEmpty(cookies)){
        return getCookieFromMaster(name)
        .then((data) => {
            if(isEmpty(data) || isEmpty(data.cookieValue)){
                return null
            }
            else{
                return data.cookieValue
            }
        })
        .catch((error) => {
            console.log('error: ' + error)
            return null
        })
    }
    else{
        return getCookieFromDocument(name)
                .then((data) => {
                    return data
                })
                .catch((error) => {
                    console.log('error: ' + error)
                    return null
                })
    }
}

export function getCookieFromDocument(name){
    var cookie = null
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0){
            cookie = c.substring(nameEQ.length,c.length)
        }
    }
    return Promise.resolve(cookie);
}

export function getCookieFromMaster(name){
    return new Promise((resolve, reject) => {
        cookieMaster.getCookieValue(SERVER_IP+':'+SERVER_PORT, name, function(data) {
            resolve(data);
        },
        (error) => {
            reject(error);
        })
    });
}

export function clearLoggedInCookie() {
    let cookies = document.cookie
    if(isEmpty(cookies)){
        return clearCookiesFromMaster()
        .then((data) => {
            return true
        })
        .catch((error) => {
            console.log('error: ' + error)
            return false
        })
    }
    else{
        return eraseCookie('loggedIn')
                .then(() => {
                    return true
                })
                .catch((error) => {
                    console.log('error: ' + error)
                    return false
                })
    }
}

export function clearCookiesFromMaster(){
    return new Promise((resolve, reject) => {
        cookieMaster.clearCookies( 
        ()=> {
            resolve('UIWebView cookies have been cleared');
        },
        (error) => {
            console.log(error)
            reject(error);
        })
    });
}

export function eraseCookie(name) {
    return createCookie(name,"",-1);
}

export function updateUserDetails(userUUID, profile, isAdmin = false){
    
    if(isEmpty(userUUID)){
        Notify.create({message: `User UUID cannot be empty!!`, timeout: 3000, color: 'negative'})
        return null
    }
    
    if(isEmpty(profile)){
        Notify.create({message: `User profile data cannot be empty!!`, timeout: 3000, color: 'negative'})
        return null
    }
    
    let url = ''
    if(isAdmin === false){
        url = '/api/users/'+userUUID+'/userDetails'
    }
    else{
        url = '/api/admin/users/'+userUUID+'/userDetails'
    }
        
    return axios.post(url, profile)
    .then( (response) => {
        if(response.status === 200){            
            Notify({message: 'Your profile information has been updated!', timeout: 3000, color: 'negative'})
            return response.data
        }

    })
    .catch(function (error) {        
        Notify({message: 'Your profile is NOT updated, sorry!', timeout: 3000, color: 'negative'})
        console.log(error);
    });
}

export function getUserName(){
    var loggedIn = !isEmpty(readCookie('loggedIn'))
    if(isEmpty(loggedIn)){
        return null
    }
    var userName = ''
    var userInfo = JSON.parse(localStorage.userInfo)
    if(!isEmpty(userInfo.alias)){
        userName = userInfo.alias
    }
    else if(!isEmpty(userInfo.firstName)){
        userName = userInfo.firstName
        if(!isEmpty(userInfo.lastName)){
            userName += ' ' + userInfo.lastName
        }
    }
    else {
        userName = userInfo.email
    }
    return userName
}

export function getUserNameFromUserObj(userObj){
    var userName = ''
    if(isEmpty(userObj)){
        throw "Cannot get user name from empty user object!"
    }
    if(!isEmpty(userObj.alias)){
        userName = userObj.alias
    }
    else if(!isEmpty(userObj.firstName)){
        userName = userObj.firstName
        if(!isEmpty(userObj.lastName)){
            userName += ' ' + userObj.lastName
        }
    }
    else {
        userName = userObj.email
    }
    return userName
}

export function isAdminUser () {
    var user = null
    try{
        if(!isEmpty(localStorage.userInfo)){
            user = JSON.parse(localStorage.userInfo)
        }
    }
    catch(err) {
        console.log(err.message)
    }
    if(!isEmpty(user)){
        var userRoles = user.roles
        if(!isEmpty(userRoles)) {
            var userRolesArr = userRoles.split(",");
            if(null != userRolesArr && userRolesArr.indexOf("admin") >= 0){
                return true;
            }
        }
    }
    return false;
}

export function getErrorPage(code, errorMessage) {
    var errors = require('./ErrorInfo')
    var errorsObj = errors['default']
    var error = errorsObj[code]
    if(!isEmpty(errorMessage)){
        error['message'] = error['message'] + "<br><b> Error message: </b>" + errorMessage
    }
    return error
}

export function loadErrorPages(status, message){
    
}

/**
 * Compare and return the differences of two string arrays
 * 
 * @param {type} strArr1 : string array to be compared
 * @param {type} strArr2 : string array for comparing
 * @returns {undefined}
 */
export function getArraydifferences(strArr1, strArr2){
    var strArrSorted1 = strArr1.sort()
    var strArrSorted2 = strArr2.sort()
    var res1 = []
    var res2 = []
    var l = 0
    var k = 0
    var i = 0
    var j = 0
    while(i < strArrSorted1.length) {
        while(j < strArrSorted2.length) {
            while(strArrSorted1[i] < strArrSorted2[j]){
                res1[k] = strArrSorted1[i]
                k++
                i++                
            }              
            if(strArrSorted1[i] == strArrSorted2[j]){                
                i++
                j++
                continue
            }
            while(strArrSorted1[i] > strArrSorted2[j]){
                res2[l] = strArrSorted2[j]
                l++
                j++
            }
        }
        res1[k] = strArrSorted1[i]
        k++
        i++
    }
    return res1
}

export function getUserProtocol(userUUID, userProtocolUUID){
    
    if(isEmpty(userProtocolUUID)){
        Notify.create({message: `Protocol UUID cannot be empty!!`, timeout: 3000, color: 'negative'})
        return false
    }
    
    var url = ''
    if(isEmpty(userUUID)){
        url = '/api/protocols/'+userProtocolUUID
    }
    else{
        url = '/api/users/' + userUUID +'/protocols/'+userProtocolUUID
    }
    
    return axios.get({
        url: url,
        dataType: 'json',
        headers: {'X-Requested-With': 'XMLHttpRequest'},
    })
    .then( (response) => {
        var status = response.status;
        if(status == 200 || status == "200"){              
            return response.data
        }
        else{
            Notify.create({message: `status " + status + ", cannot get the roles!`, timeout: 3000, color: 'negative'})
        }                                   
    })
    .catch( (error) => {
        console.log(error);
    }); 
}

export function saveUserProtocol(protocol){
    
    if(isEmpty(protocol)){
        Notify.create({message: `Protocol data is empty!`, timeout: 3000, color: 'negative'})
        return false
    }
    
    var userUUID = protocol.userUUID
    var userProtocolUUID = protocol.userProtocolUUID
    
    try{
        if(isEmpty(userUUID)){
            throw new exceptions.EmptyUserUUIDException(400, 'User UUID cannot be empty!')
        }
        else if(isEmpty(userProtocolUUID)){
            throw new exceptions.EmptyUserProtocolUUIDException(400, 'User UUID cannot be empty!')
        }
    }
    catch(exception){
        sessionStorage.errorMessage = exception.message
        document.location.href = '/errors/400'
        return false
    }
    
    delete protocol.loggedIn
    delete protocol.max_height
    delete protocol.max_width
    delete protocol.min_height
    delete protocol.min_width 

    
    var url = ''
    if(isEmpty(userUUID)){
        url = '/api/protocols/'+userProtocolUUID
    }
    else{
        url = '/api/users/' + userUUID +'/protocols/'+userProtocolUUID
    }
    
    return axios.post(
        url,
        {   
            protocol 
        }
    )
    .then( (response) => {
        var status = response.status;
        if(status == 200 || status == "200"){              
            return response.data
        }
        else{
            Notify.create({message: `status ` + status + `, cannot get the protocol information!`, timeout: 3000, color: 'negative'})
        }                                   
    })
    .catch( (error) => {
        console.log(error)
        return error
    });
    
}

export function saveComment(comment){
    
    if(isEmpty(comment)){
        Notify.create({message: `The comment data is empty!`, timeout: 3000, color: 'negative'})
        return false
    }
    
    var userUUID = comment.userUUID
    var commentUUID = comment.commentUUID
    var protocolUUID = comment.protocolUUID
    
    try{
        if(isEmpty(userUUID)){
            throw new exceptions.EmptyUserUUIDException(400, 'User UUID cannot be empty!')
        }
        else if(isEmpty(protocolUUID)){
            throw new exceptions.EmptyUserProtocolUUIDException(400, 'Protocol UUID cannot be empty!')
        }
        else if(isEmpty(commentUUID)){
            throw new exceptions.EmptyUserCommentUUIDException(400, 'Comment UUID cannot be empty!')
        }
    }
    catch(exception){
        sessionStorage.errorMessage = exception.message
        document.location.href = '/errors/400'
        return false
    }
    
    var url = ''
    if(isEmpty(userUUID)){
        url = '/api/comments/'+commentUUID
    }
    else{
        url = '/api/users/' + userUUID +'/comments/'+commentUUID
    }
    
    return axios.post( url, { comment} )
    .then( (response) => {
        var status = response.status;
        if(status == 200 || status == "200"){              
            return response.data
        }
        else{
            Notify.create({message: `status ` + status + `, cannot get the comment information!`, timeout: 3000, color: 'negative'})
        }                                   
    })
    .catch( (error) => {
        console.log(error);
    });
    
}

export function deleteComment(userUUID, commentUUID){
    
    try{
        if(isEmpty(userUUID)){
            throw new exceptions.EmptyUserUUIDException(400, 'User UUID cannot be empty!')
        }
        else if(isEmpty(commentUUID)){
            throw new exceptions.EmptyUserProtocolUUIDException(400, 'Comment UUID cannot be empty!')
        }
    }
    catch(exception){
        sessionStorage.errorMessage = exception.message
        document.location.href = '/errors/400'
        return false
    }
    
    var url = ''
    if(isEmpty(userUUID)){
        url = '/api/comments/'+commentUUID
    }
    else{
        url = '/api/users/' + userUUID +'/comments/'+commentUUID
    }
    
    return axios.delete( url )
    .then( (response) => {
        var status = response.status;
        if(status == 200 || status == "200"){              
            return response.status
        }
        else{
            Notify.create({message: `status ` + status + `, cannot delete the comment!`, timeout: 3000, color: 'negative'})
        }                                   
    })
    .catch( (error) => {
        console.log(error);
    });
    
}

export function getProtocolsByUserUUID(userUUID){
    if(isEmpty(userUUID)){
        Notify.create({message: `The user UUID is empty!`, timeout: 3000, color: 'negative'})
        return false
    }
    
    return axios.get( '/api/users/'+userUUID+'/protocols')
    .then( (response) => {
        var status = response.status;
        if(status == 200 || status == "200"){              
            return response.data
        }
        else{
            Notify.create({message: `status ` + status + `, cannot get the protocols!`, timeout: 3000, color: 'negative'})
        }                                   
    })
    .catch( (error) => {
        console.log(error);
    });
}

export function getCommentsByUserUUID(userUUID){
    if(isEmpty(userUUID)){
        Notify.create({message: `The user UUID is empty!`, timeout: 3000, color: 'negative'})
        return false
    }
    
    return axios.get('/api/users/'+userUUID+'/comments')
    .then( (response) => {
        var status = response.status;
        if(status == 200 || status == "200"){              
            return response.data
        }
        else{
            Notify.create({message: `status ` + status + `, cannot get the protocols!`, timeout: 3000, color: 'negative'})
        }                                   
    })
    .catch( (error) => {
        console.log(error);
    });
}

export function getTimeUUID(){
    var UUID = require('uuid-js')
    var uuid1 = UUID.create(1);
    return uuid1.toString()
}

export function getTimeFromTimeUUID(uuid){
    var dateFormat = require('dateformat')
    var uuid_arr = uuid.split( '-' ),
    time_str = [
        uuid_arr[ 2 ].substring( 1 ),
        uuid_arr[ 1 ],
        uuid_arr[ 0 ]
    ].join( '' );
    var time_int = parseInt( time_str, 16 );
        
    var int_time = time_int - 122192928000000000,
    int_millisec = Math.floor( int_time / 10000 );
    var date = new Date( int_millisec );
    dateFormat(date, "dddd, mmmm dS, yyyy, h:MM:ss");
    return date;
}

export function outputDateTime(date){
    var monthNames = [
    "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
  ];

  var day = date.getDate();
  var monthIndex = date.getMonth();
  var year = date.getFullYear();

  return monthNames[monthIndex] + ' ' + day + ', '+ year + ' ' + date.toLocaleTimeString();
}

// ** https://github.com/coobird/thumbnailator **
export function getFileLinkByProtocolUUID(protocolUUID, fileName){
    return window.location.hostname + '/protocols/' + protocolUUID + '/files/' + fileName
}

export function uploadFile(url, file, newFileName){
    var formData = new FormData()
    formData.append('file', file, newFileName)
    return axios.post(url, formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
    .then( (response) => {
        var status = response.status;
        if(status == 200 || status == "200"){              
            return response.data
        }
        else{
            Notify.create({message: `Cannot upload the file ` + file.name, timeout: 3000, color: 'negative'})
        }                                   
    })
    .catch( (error) => {
        console.log(error);
    }); 
}