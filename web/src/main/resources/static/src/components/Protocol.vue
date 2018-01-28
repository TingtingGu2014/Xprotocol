<template>
        <div style="width:90%; margin: auto; " ref="protocolDiv" id="protocolDiv">
            <div class="row">   
                <div class="col-lg-6 col-md-12" v-if="showEditor">
                    <div class="q-card " style="margin-top: 50px" v-bind:style="{ height: (Number(height)+350)+'px'}">
                        <q-card-title style="font-family: Georgia, 'Times New Roman', Times, serif; ">
                            <q-input v-model="title" placeholder="Add some text..."  />
                          <!--<h4 class="card-header mb-3 text-center" style="font-weight: bolder; font-size: 15px; color: midnightblue">{{title}}</h4>-->                          
                        </q-card-title>
                        <q-card-separator />
                        <q-card-main>
                            
                            <VueTinymce 
                                @content-change="contentChange"
                                @editor-file-uploaded="editorFileUploaded"
                                :id='userProtocolUUID'
                                :userUUID='userUUID'
                                :body='body' 
                                :height='height'
                                :width='width'
                            ></VueTinymce>    
                            <div class="text-center" slot="footer" v-if="isProtocolAuthor">
                                <q-btn small color="blue" style="margin-top:25px;">
                                    <span v-if="saveEditor"><i class="fa fa-floppy-o" aria-hidden="true"></i>&nbsp;&nbsp;Save</span>                                        
                                </q-btn>
                            </div>
                        </q-card-main>
                    </div>
                </div>

                <div class="col-md-12" v-bind:class="{ 'col-lg-6' :  showEditor }">
                    <q-card style="margin-top: 50px" v-bind:style="{height: (Number(height)+350)+'px'}">
                        <q-card-title style="font-family: Georgia, 'Times New Roman', Times, serif; ">
                            {{title}}                          
                        </q-card-title>
                        <q-card-separator />
                        <q-card-main>
                            <p id="pbody" v-html="body" v-bind:style="{height: (Number(height)+145)+'px'}"></p>
                            <div class="text-center" slot="footer" v-if="isProtocolAuthor">
                                <q-btn small color="blue" v-on:click="toggleEditor" style="margin-top:5px;">
                                    <span v-if="showEditor"><i class="fa fa-window-close-o" aria-hidden="true"></i>&nbsp;&nbsp;Hide Editor</span>
                                    <span v-else><i class="fa fa-pencil-square-o" aria-hidden="true"></i>&nbsp;&nbsp;Show Editor</span>
                                </q-btn>
                            </div>
                        </q-card-main>                        
                    </q-card>       
                </div>
            </div>
            <div class="row">
                <div class="col">
                <br><br>
                    <fieldset class="text-center" style="width:100%;">
                        <legend>Protocol Associated Files:</legend>
                        <div>
                            <ul class="list-group">
                                <span class="" v-if="!files || files.length == 0">There are no file associated with this protocol</span>
                                <li class="" v-else v-for="file in displayFiles " v-html="file"></li>
                            </ul>
                            <br><br>
                        </div>
                        <div class="" v-if="isProtocolAuthor">
                            <form class="form-inline">
                                <label for="uploadFileForProtocol">Select a file to upload:&nbsp;&nbsp;</label>
                                <input type="file" style="width:20%" id="uploadFileForProtocol">
                                <q-btn color="blue" small icon="fa-upload" @click.prevent="uploadProtocolFile">Add File</q-btn>
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
   
                            <span  class="col-md-4 col-lg-3 col-sm-6 text-left" v-for="(keyword, index) in keywords" >
                                <span style="font-size:1rem; " >
                                    {{index+1}}.&nbsp;{{keyword}}
                                </span>
                                <a v-bind:id="keyword" href="#" v-if="isProtocolAuthor" v-on:click.prevent="removeKeyword">
                                    <i class="fa fa-trash" ></i>                                    
                                </a>
                            </span>                            
                            <br>
                        </div>
                        
                        <div v-else>                            
                            <p style="font-size:1rem; margin-top: 5px"><span class="badge badge-info">No Keywords for this protocol</span></p>     
                            <br>
                        </div>
                        <div class="form-group text-center" v-if="isProtocolAuthor">
                            <form class="form-inline">
                                
                                <br>
                                <div class="row" style="font-size:2rem;width:50%;margin: auto">
                                    <div class="col-8">
                                        <q-input type="text" class="" id="addKeywordInput" v-model="newKeyword" placeholder="Type new keyword here:" />
                                    </div>
                                    <div class="col">
                                        <q-btn class="" color="blue" small icon="fa-key" @click.prevent="addKeyword">Add Keyword</q-btn>   
                                    </div>
                                </div>
                                
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
                                        <span class="textarea-display" v-html="value" style="text-align: left"></span>
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
                            <div class="col-12 toggle-editor">
                                <q-field>
                                    <q-input ref="addCommentInput" class="addNewTextarea" type="textarea" v-model="newComment" color="teal-9" :min-rows="3" :max-rows="8" />
                                </q-field>            
                            </div>
                            <div class="col text-center toggle-editor">
                                <q-btn color="blue" small icon="fa-floppy-o" @click.prevent="addComment">Save</q-btn>                                
                            </div>
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
    import {QCard, QCardTitle, QCardSeparator, QCardMain, QBtn, QField, QInput,QLayout,} from 'quasar'
    import ToggleInput from './elements/ToggleInput.vue'
    import VueTinymce from './Editor.vue'
    import { mapGetters, mapMutations } from 'vuex'
    
    var Utils = require('../utils/Utils')
    
    export default {
        name: 'example',
        data () {
            return {
                userProtocolUUID: this.$route.params.userProtocolUUID,
                userUUID: this.$route.params.userUUID,
                height: "300",
                width: "100%",
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
                newComment: '',
                newKeyword: '',
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
                if(!this.$utils.isEmpty(files)){
                    for(var i = 0; i < files.length; i++){
                        var fileArr = files[i].split('____')
                        var originalName = fileArr[1]
                        var currentName = fileArr[0]
                        var fileRowHtml = '<span>' + originalName + '</span>'
                        var originalNameArr = originalName.split('.')
                        var fileExtension = ''
                        if(originalNameArr.length === 2){
                            fileExtension = originalNameArr[1].toLowerCase()
                            if(this.$utils.imageExtensions.indexOf(fileExtension) >= 0){
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
                return true
            },            
            isLoggedInUser: function(){
            
               
                return true
            },
        },
        methods: {
            saveEditor: function(event){
                
            },
            saveUserProtocol: function(event){

                var protocolData = JSON.parse(JSON.stringify(this.$data))
                protocolData.userUUID = this.userUUID
                protocolData.userProtocolUUID = this.userProtocolUUID
                
                if(this.$utils.isEmpty(protocolData.userUUID) || protocolData.userUUID === 'new'){
                    alert('User UUID cannot be empty!')
                    return false;
                }
                
                if(Utithis.$utilsls.isEmpty(protocolData.title)){
                    alert('Protocol titile cannot be empty!')
                    return false;
                }
                
                if(this.$utils.isEmpty(protocolData.userProtocolUUID) || protocolData.userProtocolUUID === 'new'){
                    alert('Protocol UUID cannot be empty!')
                    return false;
                }

                this.$utils.saveUserProtocol(protocolData)
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
                var refs = this.$refs
                var protocolDiv = refs['protocolDiv']
                if(Utilthis.$utilss.isEmpty(protocolDiv)){
                    this.$toast.create.negative({html: "Cannot get the protocol Div!", druation: 3000})
                }                
                var pbody = document.getElementById('pbody')
                if(Utithis.$utilsls.isEmpty(protocolDiv)){
                    this.$toast.create.negative({html: "Cannot get the protocol display Div!", druation: 3000})
                    return false
                } 
                pbody.innerHTML = content
            },
            editorFileUploaded: function(content){
                if(Utithis.$utilsls.isEmpty(this.files)){
                    this.files = []
                }
                this.files.push(content)
            },
            toggleEditor: function(){
                this.showEditor = !(this.showEditor)
            },
            uploadProtocolFile: function(event){
                var url = '/api/users/' + this.userUUID + '/protocols/' + this.userProtocolUUID + '/files'
                var fileInput = document.getElementById('uploadFileForProtocol')
                if(this.$utils.isEmpty(fileInput)){
                    this.$toast.create.negative({html: "Cannot get the file input field!", druation: 3000})
                    return false
                } 
                var files = fileInput['files']
                if(Utithis.$utilsls.isEmpty(files)){
                    this.$toast.create.negative({html: "No files selected!", druation: 3000})
                    return false
                }
                var file = files[0]
                var protocolFiles = this.files
                if (this.$utils.isEmpty(file)) {
                    Alert.create({html: "No files selected!", druation: 3000})
                    return false
                } else {
                    var originalName = file.name
                    var newName = 'blobid' + (new Date()).getTime() + '.' + file.name.split('.').pop()                
                    this.$utils.uploadFile(url, file, newName)
                    .then((data) => {
                        protocolFiles.push(data['location'] + '____' + originalName)
                        this.$toast.create.positive({html: 'File ' + originalName + ' has been successfully uploaded!', duration: 3000})
                    })
                    .catch((err) => {
                        this.$toast.create.negative({html: 'Somthing is wrong! error:'+err.message, duration: 3000})
                        console.log(err)
                    });
                }
                
            },
            addKeyword: function(){
          
                this.keywords.push(this.newKeyword)
                this.newKeyword = ''
            },
            removeKeyword: function(event){
                var id = event.target.id
                if(this.$utils.isEmpty(id)){
                    id = event.currentTarget.id
                }
                var keywords = this.keywords
                keywords = this.$utils.removeArrayElementByValue(keywords, id)                
            },
            addComment: function(){
            
                var addKeyInput = this.$refs['addCommentInput']
                if(this.$utils.isEmpty(addKeyInput)){
                    this.$toast.create.negative({html: 'The add comment input does not exist!', duration: 3000})
                    return false
                }

                var addCommentInputVal = addKeyInput.value
                if(this.$utils.isEmpty(addCommentInputVal)){
                    this.$toast.create.negative({html: 'Please type in something for a new comment!', duration: 3000})
                    return false
                }
                if(this.$utils.isEmpty(this.comments)){
                    this.comments = {}
                }
                var newCommentUUID = this.$utils.getTimeUUID()
                
                var commentUserName = this.$utils.getUserName()                
                
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
                                
                this.$utils.saveComment(comment)
                .then((data) => {
                    console.log(data)                     
                    $("#addCommentInput").val('')
                })
                .catch((err) => {
                    alert("oops, something happened")
                    console.log(err)
                });
                
                var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.userUUID, this.userProtocolUUID)
                if(this.$utils.isEmpty(protocol.comments)){
                    protocol.comments = {}
                }
                protocol.comments[commentKey] = addCommentInputVal
                
                this.$utils.saveUserProtocol(protocol)
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
                var date = this.$utils.getTimeFromTimeUUID(uuid)
                return this.$utils.outputDateTime(date) 
            },
            isCommentAuthor: function(commentKey){
                var loggedIn = !this.$utils.isEmpty(this.$utils.readCookie('loggedIn'))
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
                if(this.$utils.isEmpty(id)){
                    id = event.currentTarget.id
                }
                var keyArr = id.split('-edit-')
                var key = keyArr[0]
                if(id.indexOf('-edit-cancel') > 0){
                    this.$utils.removeArrayElementByValue(this.hiddenCommentEditors, key)
                }
                else if(id.indexOf('-edit-show') > 0){
                    if(this.$utils.isEmpty(this.hiddenCommentEditors)){
                        this.hiddenCommentEditors = []
                    }
                    if(this.hiddenCommentEditors.indexOf(key) < 0){
                        this.hiddenCommentEditors.push(key)
                    }
                }                
            },          
            updateComment: function(event){
                var id = event.target.id
                if(this.$utils.isEmpty(id)){
                    id = event.currentTarget.id
                }
                var keyArr = id.split('-edit-')
                var commentKey = keyArr[0]
                var val = $("#"+commentKey+"-editCommentInput").val()                
                
                var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.userUUID, this.userProtocolUUID)
                if(this.$utils.isEmpty(protocol)){
                    alert("Cannot find the protocol for this comment!")
                    return false
                }
                
                if(this.$utils.isEmpty(protocol.comments)){
                    protocol.comments = {}
                }
                protocol.comments[commentKey] = val
                
                this.$utils.saveUserProtocol(protocol)
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
                
                this.$utils.saveComment(comment)
                .then((data) => {
                    console.log(data)
                    this.$set(this.comments, commentKey, val)   
                    if(this.hiddenCommentEditors.indexOf(commentKey) >= 0){
                        this.$utils.removeArrayElementByValue(this.hiddenCommentEditors, commentKey)
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
                if(this.$utils.isEmpty(id)){
                    id = event.currentTarget.id
                }
                var keyArr = id.split('-edit-')
                var commentKey = keyArr[0]
                
                var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.userUUID, this.userProtocolUUID)
                if(this.$utils.isEmpty(protocol)){
                    alert("Cannot find the protocol for this comment!")
                    return false
                }
                
                if(Utthis.$utilsils.isEmpty(protocol.comments)){
                    return false
                }
                
                this.$delete(this.comments, commentKey)
                
                this.$utils.saveUserProtocol(protocol)
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
                
                this.$utils.deleteComment(userUUID, commentUUID)
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
            VueTinymce, QCard, QCardTitle, QCardSeparator, QCardMain, QBtn, QField, QInput, QLayout, ToggleInput,
        },
        created: function() {
        
            
            
        },
        updated: function(){
            var files = this.files
//            $(".imgDelBtn").click(function(event){
//                var del = confirm('Are you sure to delete this file?')
//                if(del == false){
//                    return false
//                }                
//                alert('File '+fileName+' will NOT be deleted until you click the save button below!')
//                var location = event.target.id
//                var fileName = event.target.name
//                this.files = Utils.removeArrayElementByValue(files, location+'____'+fileName)
//            })
        },
    }
</script>

<style>
.q-card-title {
    height: 3em;
    font-weight: bold;
    text-align: center;
}

.layout-footer {
    height: 3em;
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
