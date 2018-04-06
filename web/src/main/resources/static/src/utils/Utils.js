import axios from 'axios'

export const imageExtensions = ['bmp','tif','tiff','gif','jpeg','jpg','jif','jfif','jp2','jpx','j2k','j2c','pcd','png','pdf']

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
    document.cookie = name+"="+value+expires+"; path=/";
}

export function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}

export function eraseCookie(name) {
    createCookie(name,"",-1);
}

export function getUserDetails(userUUID, isAdmin = false) {
    let url = ''
    if(isAdmin === true){
        url = '/api/admin/users/'+userUUID+'/userDetails'
    }
    else{
        url = '/api/users/'+userUUID+'/userDetails'
    }
    return axios({
        method: 'get',
        dataType: 'json',
        url: url,
    }).then( (response) => {
        if(response.status === 200){
            return response.data;
        }
        else{
            this.$q.notify({message: `Something is wrong in pulling user profile.`, timeout: 3000})
            return null
        }     
    })
    .catch(function (error) {
        this.$q.notify({message: error.message, timeout: 3000})
        console.log(error);
    });
}

export function getUserListByAdmin(){
    return axios({
        method: 'get',
        url: '/api/admin/users',
        dataType: 'json',
        headers: {'X-Requested-With': 'XMLHttpRequest'},
    })
    .then( (response) => {
        var status = response.status;
        var data = response.data
        if(status == 200 || status == "200"){
            return data
        }
        else{
            this.$q.notify("not 200");
        }                                   
    })
    .catch( (error) => {
        console.log(error);
    });  
}

export function updateUserDetails(userUUID, profile, isAdmin = false){
    
    if(isEmpty(userUUID)){
        this.$q.notify({message: 'User UUID cannot be empty!'})
        return null
    }
    
    if(isEmpty(profile)){
        this.$q.notify({message: 'User profile data cannot be empty!'})
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
            this.$q.notify({message: 'Your profile information has been updated!', timeout: 3000})
            return response.data
        }

    })
    .catch(function (error) {
        this.$q.notify({message: 'Your profile is NOT updated, sorry!', timeout: 3000})
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

export function signUp(data) {
    return axios({
        method: 'POST',
        url: '/api/signUp',
        dataType: 'json',
        data: data,
        headers: {
            'X-Requested-With': 'XMLHttpRequest',
        },
    })
    .then( (response) => {
        var status = response.status;
        var responseData = response.data;

        if(status == 200){
            localStorage.userInfo = JSON.stringify(responseData) 
            return responseData
        }
        else{
            this.$q.notify({message: "Error status : " + status + "\nError message: "+response.message, druation: 3000})
            return false;
        }                                   
      })
      .catch( (error) => {
            this.$q.notify({message: error.message, druation: 3000})
            console.log(error);
      });     
}

export function signIn(email, password) {
    return axios({
        method: 'post',
        url: '/api/signIn',
        dataType: 'json',
        headers: {'X-Requested-With': 'XMLHttpRequest'},
        auth: {
            username: email,
            password: password,
        },
    })
    .then( (response) => {
        var status = response.status;
        if(status == 200 || status == "200"){
            var data = response.data
            localStorage.userInfo = JSON.stringify(data) 
            return data
        }
        else{
            this.$q.notify("not 200");
            return;
        }                                   
    })
    .catch( (error) => {
        console.log(error);
    });      
}

export function signOut() {
    return axios({
        method: 'post',
        url: '/api/logout',
        dataType: 'json',
    })
    .then( (response) => {
        var status = response.status;
        var data = response.data;

        if(status == 200 || status == 204){
            localStorage.userEmail = ''
            localStorage.userName = ''
            localStorage.userUUID = ''
            localStorage.userInfo = ''
            var loggedIn = readCookie('loggedIn')
            if(loggedIn == 'true'){
                eraseCookie('loggedIn')
            }
            this.loggedIn = false
            this.userEmail = ''
            this.userAlias = ''
            this.userUUID = ''
            document.location.href = '/home'
            return data
        }
        else{
            this.$q.notify("not 200 "+status);
        }                                   
      })
      .catch( (error) => {
        console.log(error);
      });
}

export function isAdminUser () {
    var user = null
    try{
        user = JSON.parse(localStorage.userInfo)
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

export function getAllUserRoles() {
    return axios({
        method: 'get',
        url: '/api/admin/roles',
        dataType: 'json',
        headers: {'X-Requested-With': 'XMLHttpRequest'},
    })
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
            this.$q.notify("status " + status + ", cannot get the roles!");
        }                                   
    })
    .catch( (error) => {
        console.log(error);
    }); 
}

export function getUserProtocol(userUUID, userProtocolUUID){
    
    if(isEmpty(userProtocolUUID)){
        this.$q.notify('The protocol UUID is empty!')
        return false
    }
    
    var url = ''
    if(isEmpty(userUUID)){
        url = '/api/protocols/'+userProtocolUUID
    }
    else{
        url = '/api/users/' + userUUID +'/protocols/'+userProtocolUUID
    }
    
    return axios({
        method: 'get',
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
            this.$q.notify("status " + status + ", cannot get the roles!");
        }                                   
    })
    .catch( (error) => {
        console.log(error);
    }); 
}

export function saveUserProtocol(protocol){
    
    if(isEmpty(protocol)){
        this.$q.notify('The protocol data is empty!')
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
    
    return axios({
        method: 'post',
        url: url,
        dataType: 'json',
        data: protocol,
        headers: {'X-Requested-With': 'XMLHttpRequest'},
    })
    .then( (response) => {
        var status = response.status;
        if(status == 200 || status == "200"){              
            return response.data
        }
        else{
            this.$q.notify("status " + status + ", cannot get the protocol information!");
        }                                   
    })
    .catch( (error) => {
        console.log(error);
    });
    
}

export function saveComment(comment){
    
    if(isEmpty(comment)){
        this.$q.notify('The comment data is empty!')
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
    
    return axios({
        method: 'post',
        url: url,
        dataType: 'json',
        data: comment,
        headers: {'X-Requested-With': 'XMLHttpRequest'},
    })
    .then( (response) => {
        var status = response.status;
        if(status == 200 || status == "200"){              
            return response.data
        }
        else{
            this.$q.notify("status " + status + ", cannot get the comment information!");
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
    
    return axios({
        method: 'delete',
        url: url,
        dataType: 'json',
        data: null,
        headers: {'X-Requested-With': 'XMLHttpRequest'},
    })
    .then( (response) => {
        var status = response.status;
        if(status == 200 || status == "200"){              
            return response.status
        }
        else{
            this.$q.notify("status " + status + ", cannot delete the comment!");
        }                                   
    })
    .catch( (error) => {
        console.log(error);
    });
    
}

export function getProtocolsByUserUUID(userUUID){
    if(isEmpty(userUUID)){
        this.$q.notify('The user UUID is empty!')
        return false
    }
    
    return axios({
        method: 'get',
        url: '/api/users/'+userUUID+'/protocols',
        dataType: 'json',
        headers: {'X-Requested-With': 'XMLHttpRequest'},
    })
    .then( (response) => {
        var status = response.status;
        if(status == 200 || status == "200"){              
            return response.data
        }
        else{
            this.$q.notify("status " + status + ", cannot get the protocols!");
        }                                   
    })
    .catch( (error) => {
        console.log(error);
    });
}

export function getCommentsByUserUUID(userUUID){
    if(isEmpty(userUUID)){
        this.$q.notify('The user UUID is empty!')
        return false
    }
    
    return axios({
        method: 'get',
        url: '/api/users/'+userUUID+'/comments',
        dataType: 'json',
        headers: {'X-Requested-With': 'XMLHttpRequest'},
    })
    .then( (response) => {
        var status = response.status;
        if(status == 200 || status == "200"){              
            return response.data
        }
        else{
            this.$q.notify("status " + status + ", cannot get the protocols!");
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
            this.$q.notify( "Cannot upload the file "+file.name);
        }                                   
    })
    .catch( (error) => {
        console.log(error);
    }); 
}