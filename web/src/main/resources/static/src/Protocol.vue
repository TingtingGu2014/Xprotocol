<template>
    <div>
        <div class="container">    
            <div class="row">
                <div class="col" v-if="showEditor">
                    <div class="card " style="margin-top: 100px" v-bind:style="{ height: (Number(height)+320)+'px'}">
                            <div class="card-block" >
                                <h4 class="card-header mb-3 text-center" v-bind:style="{}" style="font-weight: bolder; font-size: 15px; color: midnightblue">
                                    <input class="form-control text-center" type="text" v-model="title" id="example-text-input" required>
                                </h4>
                                <VueTinymce 
                                    @content-change="contentChange"
                                    @editor-file-uploaded="editorFileUploaded"
                                    :id='userProtocolUUID'
                                    :userUUID='userUUID'
                                    :body='body' 
                                    :max_height='max_height'
                                    :max_width='max_width'
                                    :min_height='min_height'
                                    :min_width='min_width'
                                    :height='height'
                                    :width='width'
                                ></VueTinymce>                     
                            </div>
                    </div>
                </div>

                <div class="col">
                    <div class="card " style="margin-top: 100px" v-bind:style="{height: (Number(height)+320)+'px'}">
                        <div class="card-block">
                          <h4 class="card-header mb-3 text-center" style="font-weight: bolder; font-size: 15px; color: midnightblue">{{title}}</h4>
                          <p id="pbody" v-html="body"></p>
                        </div>
                        <div class="text-center" v-if="isProtocolAuthor">
                            <a href="#" class="btn" v-on:click="toggleEditor">
                                <span v-if="showEditor">Hide Editor</span>
                                <span v-else>Show Editor</span>
                            </a>
                        </div>
                    </div>       
                </div>
            </div>
            <div class="row">
                <div class="col">
                <br><br>
                    <fieldset class="form-group text-center" style="width:100%;">
                        <legend>Protocol Associated Files:</legend>
                        <div>
                            <ul class="list-group">
                                <li class="list-group-item" v-if="!files || files.length == 0">There are no file associated with this protocol</li>
                                <li class="list-group-item" v-else v-for="file in displayFiles " v-html="file"></li>
                            </ul>
                            <br><br>
                        </div>
                        <div class="form-group" v-if="isProtocolAuthor">
                            <form class="form-inline">
                            <label for="uploadFileForProtocol">Select a file to upload:&nbsp;&nbsp;</label>
                            <input type="file" class="form-control-file" id="uploadFileForProtocol">
                            <button type="button" class="btn btn-primary" v-on:click.prevent="uploadProtocolFile"><i class="fa fa-upload" aria-hidden="true"></i>&nbsp;&nbsp;Add a new file</button>
                            </form>
                        </div>
                    </fieldset>
                </div>
            </div>
            <div class="row">
                <div class="col">
                <br><br>
                    <fieldset class="form-group text-center" style="width:100%;">
                        <legend>Protocol Key Words:</legend>
                        <div class="row" v-if="keywords && keywords.length > 0">
                            <div class="col  col-lg-12" role="group" aria-label="...">
                                <span  class="col col-lg-4 float-left text-left" v-for="(keyword, index) in keywords" >
                                    <span style="font-size:1rem; " >
                                        {{index+1}}.&nbsp;{{keyword}}
                                    </span>
                                    <a v-bind:id="keyword" href="#" class="trash-can" v-if="isProtocolAuthor" v-on:click.prevent="removeKeyword">
                                        <i class="fa fa-trash" aria-hidden="true"></i>                                    
                                    </a>
                                </span>                            
                            </div>
                            <br>
                        </div>
                        
                        <div v-else>                            
                            <p style="font-size:2rem;"><span class="badge badge-info">No Keywords for this protocol</span></p>     
                            <br>
                        </div>
                        <div class="form-group text-center" v-if="isProtocolAuthor">
                            <form class="form-inline">
                                <p style="font-size:2rem;">
                                    <br>
                                    <input type="text" class="form-control mb-2 mr-sm-2 mb-sm-0" id="addKeywordInput">
                                    <button type="button" class="btn btn-primary" v-on:click.prevent="addKeyword">Add a new keyword</button>
                                </p>
                            </form>
                        </div>
                    </fieldset>
                </div>
            </div>
            <br><br>
            <div class="row">
                <div class="col">
                    <fieldset class="form-group text-center" style="width:100%;">
                        <legend>Comments:</legend>
                        <div v-if="comments && Object.keys(comments).length > 0">
                            <div class="list-group" v-if="comments && Object.keys(comments).length > 0" v-for="(value, key) in comments">
                                <span class="list-group-item list-group-item-action flex-column align-items-start borderless">
                                    <div class="d-flex w-100 justify-content-between">
                                        <h5 class="mb-1">{{getCommentUserNameFromCommentKey(key)}} says:</h5>
                                        <small>{{getCommentDateFromCommentKey(key)}}</small>
                                    </div>
                                    <div id="key + '-edit-delete-div'" class="d-flex w-100 justify-content-between" v-if="!hiddenCommentEditors || hiddenCommentEditors.indexOf(key) < 0">
                                        <span class="mb-1" v-html="value" style="text-align: left"></span>
                                        <span class="mb-1" style="width: 15%;" v-if="isLoggedInUser">
                                            <a v-bind:id="key + '-edit-show'" href="#" v-on:click.prevent="toggleCommentEditBtn">
                                                <i class="fa fa-pencil-square-o" aria-hidden="true"></i>
                                            </a>                                            
                                            <a v-bind:id="key + '-edit-delete'" href="#" v-on:click.prevent="deleteComment">
                                                <i class="fa fa-trash-o" aria-hidden="true"></i>
                                            </a>
                                        </span>
                                    </div>     
                                    <div id="key + '-edit-input-div'" class="d-flex w-100 justify-content-between" v-else>
                                        <span style="width:100%">
                                            <p><textarea class="form-control" v-bind:id="key + '-editCommentInput'" rows="5">{{value}}</textarea></p>
                                            <!--<a v-bind:id="key + '-edit-commit'" href="#" v-on:click.prevent="editComment">OK</a>-->
                                            <b-button v-bind:id="key + '-edit-commit'" :size="sm" :variant="outline-sucess" v-on:click.prevent="updateComment" >
                                                Commit &nbsp;&nbsp;<i class="fa fa-check" aria-hidden="true"></i>
                                            </b-button>
                                            <b-button v-bind:id="key + '-edit-cancel'" :size="sm" :variant="outline-sucess" v-on:click.prevent="toggleCommentEditBtn">
                                                Cancel&nbsp;&nbsp;<i class="fa fa-undo" aria-hidden="true"></i>
                                            </b-button>
                                        </span>
                                    </div>
                                </span>
                            </div>
                        </div>
                        <form v-if="isLoggedInUser">
                            <p style="font-size:2rem;">    
                                <br>
                                <button type="button" class="btn btn-primary" v-on:click.prevent="addComment" style="margin-bottom: 5px;">Add a comment</button>
                                <br>                               
                                <textarea class="form-control" id="addCommentInput" rows="5"></textarea>
                            </p>
                        </form>
                    </fieldset>                
            </div>
        </div>
        <br>
        <div class="text-center" v-if="isProtocolAuthor">
            <button type="button" class="btn btn-outline-success btn-lg protocolUpdt"
                v-on:click.prevent="saveUserProtocol"
            >
                <span style="font-weight: bold">Save</span>
            </button>
        </div>
    </div>
</template>

<script>
    import VueTinymce from './Editor.vue'
    import { mapGetters, mapMutations } from 'vuex'
    
    var Utils = require('./Utils')
    
    export default {
        name: 'example',
        data () {
            return {
                userProtocolUUID: this.$route.params.userProtocolUUID,
                userUUID: this.$route.params.userUUID,
                max_height: "500",
                max_width: "600",
                min_height: "200",
                min_width: "200",
                height: "300",
                width: "500",
                title: '',
                body: '',
                projectUUIDs: [],
                projectTitles: [],
                files: [],
                comments: null,
                versions: [],
                keywords: [],
                hiddenCommentEditors: [],
                showEditor: false,                
            }
        },
        computed: {
            ...mapGetters({                
                getProtocols: 'protocolModule/getProtocols',
                getProtocolsByUserUUID: 'protocolModule/getProtocolsByUserUUID',
                getProtocolsByUserUUIDANDProtocolUUID: 'protocolModule/getProtocolsByUserUUIDANDProtocolUUID',
            }),
            displayFiles: function(){
                var files = this.files
                var fileHtmls = []
                if(!Utils.isEmpty(files)){
                    for(var i = 0; i < files.length; i++){
                        var fileArr = files[i].split('____')
                        var originalName = fileArr[1]
                        var currentName = fileArr[0]
                        var fileRowHtml = '<span>' + originalName + '</span>'
                        var originalNameArr = originalName.split('.')
                        var fileExtension = ''
                        if(originalNameArr.length === 2){
                            fileExtension = originalNameArr[1].toLowerCase()
                            if(Utils.imageExtensions.indexOf(fileExtension) >= 0){
                                fileRowHtml += '&nbsp;&nbsp;&nbsp;&nbsp;<img src=\"' + currentName + '\" alt=\"File not found\" width=\"80\" height=\"80\" >' +
                                               '&nbsp;&nbsp;&nbsp;&nbsp;<a href="' + currentName + '?name='+originalName+'" class="btn btn-secondary" target="_blank">Download</a>'
                                if(this.isProtocolAuthor){
                                    fileRowHtml += '&nbsp;&nbsp;&nbsp;&nbsp;<button id="' + currentName + '" type="button" class="btn btn-secondary imgDelBtn" name="'+originalName+'">Delete</button>'
                                }                                                 
                                fileHtmls.push(fileRowHtml)
                            }
                        }
                    }
                }
                return fileHtmls
            },
            isProtocolAuthor: function(){
                var loggedIn = !Utils.isEmpty(Utils.readCookie('loggedIn'))
                if(!loggedIn === true){
                    return false
                }
                var userInfo = JSON.parse(localStorage.userInfo)
                var currentUserUUID = userInfo.userUUID
                return currentUserUUID == this.userUUID ? true : false
            },            
            isLoggedInUser: function(){
            
                var loggedIn = !Utils.isEmpty(Utils.readCookie('loggedIn'))
                if(!loggedIn === true){
                    return false
                }
                
                var userInfo = JSON.parse(localStorage.userInfo)
                if(!userInfo){
                    return false
                }
                
                var currentUserUUID = userInfo.userUUID
                if(!currentUserUUID){
                    return false
                }
                
                return true
            },
        },
        methods: {
            saveUserProtocol: function(event){

                var protocolData = JSON.parse(JSON.stringify(this.$data))
                protocolData.userUUID = this.userUUID
                protocolData.userProtocolUUID = this.userProtocolUUID
                
                if(Utils.isEmpty(protocolData.userUUID) || protocolData.userUUID === 'new'){
                    alert('User UUID cannot be empty!')
                    return false;
                }
                
                if(Utils.isEmpty(protocolData.title)){
                    alert('Protocol titile cannot be empty!')
                    return false;
                }
                
                if(Utils.isEmpty(protocolData.userProtocolUUID) || protocolData.userProtocolUUID === 'new'){
                    alert('Protocol UUID cannot be empty!')
                    return false;
                }

                Utils.saveUserProtocol(protocolData)
                .then((data) => {
                    if(protocolData.userProtocolUUID === 'new'){
                        var protocolListCount = Number(localStorage.protocolListCount) + 1
                        localStorage.protocolListCount = protocolListCount
                    }
                    this.resetUserProtocol(data)
                    document.location.href = '/home'
                })
                .catch((err) => {
                    alert("oops, something happened")
                    console.log(err)
                });
            },
            resetUserProtocol: function (protocol){
                this.title = protocol.title
                this.body = protocol.body
                this.projectUUIDs = protocol.projectUUIDs
                this.projectTitles = protocol.projectTitles
                this.files = protocol.files
                this.comments = protocol.comments
                this.versions = protocol.versions
                this.keywords = protocol.keywords
                this.setProtocolByUserUUIDANDProtocolUUID(protocol)
            },
            ...mapMutations({                                
                setProtocols: 'protocolModule/setProtocols',
                setProtocolsByUserUUID: 'protocolModule/setProtocolsByUserUUID',
                setProtocolByUserUUIDANDProtocolUUID: 'protocolModule/setProtocolByUserUUIDANDProtocolUUID',
            }),
            contentChange: function(content){
                this.body = content
                $('#pbody').html(content)
            },
            editorFileUploaded: function(content){
                if(Utils.isEmpty(this.files)){
                    this.files = []
                }
                this.files.push(content)
            },
            toggleEditor: function(){
                this.showEditor = !(this.showEditor)
            },
            uploadProtocolFile: function(event){
                var url = '/api/users/' + this.userUUID + '/protocols/' + this.userProtocolUUID + '/files'
                var files = $("#uploadFileForProtocol").prop("files")
                var file = files[0]
                var protocolFiles = this.files
                if (Utils.isEmpty(file)) {
                    alert("Please select a files to upload")
                } else {
                    var originalName = file.name
                    var newName = 'blobid' + (new Date()).getTime() + '.' + file.name.split('.').pop()                
                    Utils.uploadFile(url, file, newName)
                    .then((data) => {
                        protocolFiles.push(data['location'] + '____' + originalName)
                        alert('File ' + originalName + ' has been successfully uploaded!')
                    })
                    .catch((err) => {
                        alert("oops, something happened")
                        console.log(err)
                    });
                }
                
            },
            addKeyword: function(){
                var addKeywordInputVal = $("#addKeywordInput").val()
                if(Utils.isEmpty(addKeywordInputVal)){
                    alert('Please type in a new keyword!')
                    return false
                }
                if(Utils.isEmpty(this.keywords)){
                    this.keywords = []
                }
                this.keywords.push(addKeywordInputVal)
            },
            addComment: function(){
            
                var addCommentInputVal = $("#addCommentInput").val().replace(/(?:\r\n|\r|\n)/g, '<br/>')
                if(Utils.isEmpty(addCommentInputVal)){
                    alert('Please type in something for a new comment!')
                    return false
                }
                if(Utils.isEmpty(this.comments)){
                    this.comments = {}
                }
                var newCommentUUID = Utils.getTimeUUID()
                
                var commentUserName = ''
                var userInfo = JSON.parse(localStorage.userInfo)
                if(!Utils.isEmpty(userInfo.alias)){
                    commentUserName = userInfo.alias
                }
                else if(!Utils.isEmpty(userInfo.firstName)){
                    commentUserName = userInfo.firstName
                    if(!Utils.isEmpty(userInfo.lastName)){
                        commentUserName += ' ' + userInfo.lastName
                    }
                }
                else {
                    commentUserName = userInfo.email
                }
                
                var comment = {}
                comment.protocolUserUUID = this.userUUID
                comment.protocolUUID = this.userProtocolUUID
                comment.commentUUID = newCommentUUID
                comment.userUUID = userInfo.userUUID
                comment.protocolTitle = this.title
                comment.content = addCommentInputVal
                comment.path = ''
                
                var commentKey = userInfo.userUUID + '____' + commentUserName + '____' + newCommentUUID
                this.$set(this.comments, commentKey, addCommentInputVal)  
                                
                Utils.saveComment(comment)
                .then((data) => {
                    console.log(data)                     
                    $("#addCommentInput").val('')
                })
                .catch((err) => {
                    alert("oops, something happened")
                    console.log(err)
                });
                
                var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.userUUID, this.userProtocolUUID)
                if(Utils.isEmpty(protocol.comments)){
                    protocol.comments = {}
                }
                protocol.comments[commentKey] = addCommentInputVal
                
                Utils.saveUserProtocol(protocol)
                .then((data) => {
                    console.log(data)                    
                })
                .catch((err) => {
                    alert("oops, something happened")
                    console.log(err)
                });
                
            },
            // comment key: userUUID____userName____protocolUUID
            getCommentUserNameFromCommentKey(key){
                var keyArr = key.split('____')
                if(!keyArr || keyArr.length != 3){
                    alert('The comment key: ' + key + ' is wrong!')
                }
                return keyArr[1]
            },
            getCommentDateFromCommentKey(key){
                var keyArr = key.split('____')
                if(!keyArr || keyArr.length != 3){
                    alert('The comment key: ' + key + ' is wrong!')
                }
                var uuid = keyArr[2]
                var date = Utils.getTimeFromTimeUUID(uuid)
                return Utils.outputDateTime(date) 
            },
            isCommentAuthor: function(commentKey){
                var loggedIn = !Utils.isEmpty(Utils.readCookie('loggedIn'))
                if(!loggedIn === true){
                    return false
                }
                var userInfo = JSON.parse(localStorage.userInfo)
                var currentUserUUID = userInfo.userUUID
                var keyArr = commentKey.split('____')
                if(!keyArr || keyArr.length != 3){
                    alert('The comment key: ' + commentKey + ' is wrong!')
                }
                var commentUserUUID = keyArr[0]
                return currentUserUUID == commentUserUUID ? true : false
            },
            toggleCommentEditBtn: function(event){
                var id = event.target.id
                if(Utils.isEmpty(id)){
                    id = event.currentTarget.id
                }
                var keyArr = id.split('-edit-')
                var key = keyArr[0]
                if(id.indexOf('-edit-cancel') > 0){
                    Utils.removeArrayElementByValue(this.hiddenCommentEditors, key)
                }
                else if(id.indexOf('-edit-show') > 0){
                    if(Utils.isEmpty(this.hiddenCommentEditors)){
                        this.hiddenCommentEditors = []
                    }
                    if(this.hiddenCommentEditors.indexOf(key) < 0){
                        this.hiddenCommentEditors.push(key)
                    }
                }                
            },          
            updateComment: function(event){
                var id = event.target.id
                if(Utils.isEmpty(id)){
                    id = event.currentTarget.id
                }
                var keyArr = id.split('-edit-')
                var commentKey = keyArr[0]
                var val = $("#"+commentKey+"-editCommentInput").val()                
                
                var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.userUUID, this.userProtocolUUID)
                if(Utils.isEmpty(protocol)){
                    alert("Cannot find the protocol for this comment!")
                    return false
                }
                
                if(Utils.isEmpty(protocol.comments)){
                    protocol.comments = {}
                }
                protocol.comments[commentKey] = val
                
                Utils.saveUserProtocol(protocol)
                .then((data) => {
                    console.log(data) 
                    console.log('Protocol with new comment content saved')
                })
                .catch((err) => {
                    alert("oops, something happened")
                    console.log(err)
                });
                
                var keyArr = commentKey.split('____')
                var userInfo = JSON.parse(localStorage.userInfo)
                var comment = {}
                comment.protocolUserUUID = this.userUUID
                comment.protocolUUID = this.userProtocolUUID
                comment.commentUUID = keyArr[2]
                comment.userUUID = userInfo.userUUID
                comment.protocolTitle = this.title
                comment.content = val
                comment.path = ''
                
                Utils.saveComment(comment)
                .then((data) => {
                    console.log(data)
                    this.$set(this.comments, commentKey, val)   
                    if(this.hiddenCommentEditors.indexOf(commentKey) >= 0){
                        Utils.removeArrayElementByValue(this.hiddenCommentEditors, commentKey)
                    }
                })
                .catch((err) => {
                    alert("oops, something happened")
                    console.log(err)
                });
                
            },
            deleteComment: function(event){
                var del = confirm('Are you sure to delete this comment?')
                if(del == false){
                    return false
                } 
                var id = event.target.id
                if(Utils.isEmpty(id)){
                    id = event.currentTarget.id
                }
                var keyArr = id.split('-edit-')
                var commentKey = keyArr[0]
                
                var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.userUUID, this.userProtocolUUID)
                if(Utils.isEmpty(protocol)){
                    alert("Cannot find the protocol for this comment!")
                    return false
                }
                
                if(Utils.isEmpty(protocol.comments)){
                    return false
                }
                
                this.$delete(this.comments, commentKey)
                
                Utils.saveUserProtocol(protocol)
                .then((data) => {
                    console.log(data) 
                    this.setProtocolByUserUUIDANDProtocolUUID(data)
                    console.log('Protocol with deleted comment saved')
                })
                .catch((err) => {
                    alert("oops, something happened")
                    console.log(err)
                });
                
                var keyArr = commentKey.split('____')
                var userInfo = JSON.parse(localStorage.userInfo)
                var commentUUID = keyArr[2]
                var userUUID = userInfo.userUUID
                
                Utils.deleteComment(userUUID, commentUUID)
                    .then((status) => {
                        console.log('Delete comment status: ' + status)                        
                    })
                    .catch((err) => {
                        alert("oops, something happened")
                        console.log(err)
                    });
                }
        },
        components: {
            VueTinymce
        },
        created: function() {
        
            try{
                var userUUID = this.$route.params.userUUID
                if(Utils.isEmpty(userUUID)){
                    throw new EmptyUserUUIDException(400, 'User UUID cannot be empty!')
                }
            }
            catch(exception){
                if (e instanceof EmptyUserUUIDException) {
                    sessionStorage.errorMessage = message
                    document.location.href = '/errors/400'
                    return false
                } else {
                    return false;
                }
            }
            
            // When userProtocolUUID == 'new' this is a new protocol
            var userProtocolUUID = this.$route.params.userProtocolUUID
            if(userProtocolUUID == 'new'){
                this.userProtocolUUID = Utils.getTimeUUID()
                this.showEditor = true
                return false
            }
            
            var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.userUUID, this.userProtocolUUID)

            if(!Utils.isEmpty(protocol)){
                this.resetUserProtocol(protocol)
                return false
            }

            Utils.getUserProtocol(this.userUUID, this.userProtocolUUID)
            .then((data) => {
                console.log(data)
                this.resetUserProtocol(data)
            })
            .catch((err) => {
                alert("oops, something happened")
                console.log(err)
            });
            
        },
        updated: function(){
            var files = this.files
            $(".imgDelBtn").click(function(event){
                var del = confirm('Are you sure to delete this file?')
                if(del == false){
                    return false
                }                
                alert('File '+fileName+' will NOT be deleted until you click the save button below!')
                var location = event.target.id
                var fileName = event.target.name
                this.files = Utils.removeArrayElementByValue(files, location+'____'+fileName)
            })
        },
    }
</script>

<style>
.card {
    border: 1px groove add8e6;
    padding: 0 1.4em 1.4em!important;
    margin: 0 0 1.5em!important;
    box-shadow: 0 0 0 0 #000;
}

.card-block {
    overflow: scroll;
}
::-webkit-scrollbar {
    width: 0px;
    background: transparent; /* make scrollbar transparent */
}

.container {
    margin-top: 15px;
}

.protocolUpdt{
    margin-bottom: 15px;
}

#pbody {
    word-wrap: break-word;
}

hr {
  margin-top: 1rem;
  margin-bottom: 1rem;
  border: 0;
  border-top: 2px solid rgb(169, 178, 180);
}
/*
how to css list group border: https://arkuuu.de/blog/bootstrap4-card-heading-list-group

.card > .list-group .list-group-item {
//    border-left-width: 0;
//    border-right-width: 0;
}
.card > .list-group .list-group-item:last-of-type {
//    border-bottom-width: 0;
}
.card > .list-group .list-group-item:first-of-type {
//    border-top-width: 0;
}

*/

.list-group .list-group-item {
    border-left-width: 0;
    border-right-width: 0;
}
</style>
