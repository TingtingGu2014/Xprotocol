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
                                <q-btn small color="blue" style="margin-top:25px;" v-on:click.prevent="saveUserProtocol">
                                    <span><i class="fa fa-floppy-o" aria-hidden="true"></i>&nbsp;&nbsp;Save</span>                                        
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
                        <legend>Protocol Associated Files</legend>
                        <div class="displayFileDiv">
                            <q-list highlight style="padding-left: 10%">
                                <q-item v-if="!files || files.length == 0">There are no file associated with this protocol</q-item>
                                <q-item v-else v-for="fileInfo in displayFiles" style="margin: 0 4rem 2rem 0">
                                    <q-item-side left>
                                        <img :src="fileInfo.currentName" class="responsive" alt="Protocol file without preview" style="width: 7rem; height: auto">
                                    </q-item-side>
                                    <q-item-main>
                                        <a :href="fileInfo.currentName + '?name='+fileInfo.originalName" target="_blank">
                                            <i class="fa fa-download" style="font-">&nbsp;&nbsp;<span class="fileBtn">DOWNLOAD</span></i>
                                        </a>                                    
                                        <a class="imgDelBtn" href="javascript:void(0)" :id="fileInfo.currentName" :name="fileInfo.originalName" @click.prevent="deleteProtocolFiles">
                                            <i class="fa fa-trash-o">&nbsp;&nbsp;<span class="fileBtn">DELETE</span></i>
                                        </a>                                    
                                    </q-item-main>
                                </q-item>
                            </q-list>
                        </div>
                        
                        <div class="protocolUploadDiv" v-if="isProtocolAuthor">
                            <form class="row">
                                <br>
                                <q-field label="Select a file to upload:" class="col-md-8 col-sm-12" style="margin-bottom: 10px;">
                                    <input type="file" id="uploadFileForProtocol" style="width: 100%;"/>                                    
                                </q-field>
                                <q-field class="col col-sm-12">
                                    <q-btn color="blue" small align="left" icon="fa-upload" @click.prevent="uploadProtocolFile">Add File</q-btn>
                                </q-field>
                            </form>
                        </div>
                    </fieldset>
                </div>
            </div>
            <div class="row">
                <div class="col">
                <br><br>
                    <fieldset class="form-group text-center" style="width:100%;">
                        <legend>Protocol Key Words</legend>
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
                    <fieldset class="text-center" style="width:100%;">
                        <legend>Comments</legend>
                        <q-list class="" v-if="comments && Object.keys(comments).length > 0" v-for="(value, key) in comments">
                            <q-item>
                                <q-item-main>
                                    <div class="row">
                                        <small class="col-5 text-left comment-signature">{{getCommentUserNameFromCommentKey(key)}} says:</small>
                                        <small class="col-7 text-right">{{getCommentDateFromCommentKey(key)}}</small>
                                    </div>
                                    <div class="row" style="width: 100%;">
                                        <toggle-textarea :name="key" :inputValue="value" @toggleTextAreaValueChange="updateComment"></toggle-textarea>
                                    </div>
                                </q-item-main>
                                <q-item-side right link color="red-4" >
                                <a href="# " :id="key + '-edit-delete'" class="commentDelBtn" @click.prevent="deleteComment" ><i class="fa fa-trash-o">&nbsp;&nbsp;Delete</i></a>
                                </q-item-side>
                            </q-item>
                        </q-list>
                        <br>
                        <hr>
                        <form v-if="isLoggedInUser">
                            <div class="col-12 toggle-editor">
                                <q-field>
                                    <q-input ref="addCommentInput" class="addNewTextarea" type="textarea" v-model="newComment" color="teal-9" :min-rows="3" :max-rows="8" />
                                </q-field>            
                            </div>
                            <div class="col text-center toggle-editor">
                                <q-btn color="blue" small icon="fa-floppy-o" @click.prevent="addComment">Add Comment</q-btn>                                
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
    import {QCard, QCardTitle, QCardSeparator, QCardMain, QBtn, QField, QInput,QLayout,QList, QItem, QItemSide, QItemMain} from 'quasar'
    import ToggleTextarea from './elements/ToggleTextArea.vue'
    import VueTinymce from './Editor.vue'
    import { mapGetters, mapMutations } from 'vuex'
    
    var FileUtils = require('../utils/FileUtils')
    
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
                            if(this.$utils.isEmpty(fileExtension)){
                                fileExtension = 'none'
                            }
                            let fileInfo = new Object()
                            fileInfo.currentName = currentName
                            fileInfo.originalName = originalName
                            fileInfo.extension = fileExtension
                                               
                            fileHtmls.push(fileInfo)
                        }
                    }
                }
                return fileHtmls
            },
            isProtocolAuthor: function(){
                var loggedIn = !this.$utils.isEmpty(this.$utils.readCookie('loggedIn'))
                if(!loggedIn === true){
                    return false
                }
                var userInfo = JSON.parse(localStorage.userInfo)
                var currentUserUUID = userInfo.userUUID
                return currentUserUUID == this.userUUID ? true : false
            },            
            isLoggedInUser: function(){
                var loggedIn = !this.$utils.isEmpty(this.$utils.readCookie('loggedIn'))
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
            saveEditor: function(event){
                saveUserProtocol()
            },
            saveUserProtocol: function(){

                var protocolData = JSON.parse(JSON.stringify(this.$data))
                protocolData.userUUID = this.userUUID
                protocolData.userProtocolUUID = this.userProtocolUUID
                
                if(this.$utils.isEmpty(protocolData.userUUID) || protocolData.userUUID === 'new'){
                    alert('User UUID cannot be empty!')
                    return false;
                }
                
                if(this.$utils.isEmpty(protocolData.title)){
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
                if(this.$utils.isEmpty(protocolDiv)){
                    this.$toast.create.negative({html: "Cannot get the protocol Div!", druation: 3000})
                }                
                var pbody = document.getElementById('pbody')
                if(this.$utils.isEmpty(protocolDiv)){
                    this.$toast.create.negative({html: "Cannot get the protocol display Div!", druation: 3000})
                    return false
                } 
                pbody.innerHTML = content
                this.body = content
            },
            editorFileUploaded: function(content){
                if(this.$utils.isEmpty(this.files)){
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
                if(this.$utils.isEmpty(files)){
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
                        
                        let newFileVal = data['location'] + '____' + originalName
                        protocolFiles.push(newFileVal)
                
                        // Update protocol in the storage:
                        var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.userUUID, this.userProtocolUUID)
                        if(this.$utils.isEmpty(protocol.files)){
                            protocol.files = []
                        }
                        protocol.files.push(newFileVal)

                        this.$utils.saveUserProtocol(protocol)
                        .then((data) => {
                            this.setProtocolByUserUUIDANDProtocolUUID(protocol)
                            console.log(data)                    
                        })
                        .catch((err) => {
                            alert("oops, something happened")
                            console.log(err)
                        });
                
                        this.$toast.create.positive({html: 'File ' + originalName + ' has been successfully uploaded!', duration: 3000})
                    })
                    .catch((err) => {
                        this.$toast.create.negative({html: 'Somthing is wrong! error:'+err.message, duration: 3000})
                        console.log(err)
                    });
                }
                
            },
            deleteProtocolFiles: function(event){
                var del = confirm('Are you sure to delete this file?')
                if(del == false){
                    return event.preventDefault()
                }                
                var target = event.target
                if(this.$utils.isEmpty(target) || this.$utils.isEmpty(target.id)){
                    target = event.currentTarget
                    if(this.$utils.isEmpty(target) || this.$utils.isEmpty(target.id)){
                        this.$toast.create.negative({html: 'Cannot find DOM target ID to delete protocol file!', duration: 3000})
                        return false
                    }
                }
                var location = target.id
                var fileName = target.name
                
                FileUtils.deleteProtocolFile(location)
                .then((data) => {
                    console.log(data) 
                    if(data.toString() == '200'){
                        this.files = this.$utils.removeArrayElementByValue(this.files, location+'____'+fileName)
                        var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.userUUID, this.userProtocolUUID)
                        protocol.files = this.files
                        
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
                    }
                })
                .catch((err) => {
                    alert("oops, something happened")
                    console.log(err)
                });
                return false
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
                let newVal = this.newComment
                let userInfo = JSON.parse(localStorage.userInfo)
                if(this.$utils.isEmpty(newVal)){
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
                comment.content = newVal
                comment.path = ''
                
                var commentKey = userInfo.userUUID + '____' + commentUserName + '____' + newCommentUUID
                this.$set(this.comments, commentKey, newVal)  
                                
                this.$utils.saveComment(comment)
                .then((data) => {
                    console.log(data)                     
                    this.newComment = ''
                })
                .catch((err) => {
                    alert("oops, something happened")
                    console.log(err)
                });
                
                var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.userUUID, this.userProtocolUUID)
                if(this.$utils.isEmpty(protocol.comments)){
                    protocol.comments = {}
                }
                protocol.comments[commentKey] = newVal
                
                this.$utils.saveUserProtocol(protocol)
                .then((data) => {
                    console.log(data)   
                    this.setProtocolByUserUUIDANDProtocolUUID(protocol)
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
            updateComment: function(newVal, key){

                if(this.$utils.isEmpty(key)){
                    this.$toast.create.negative({html: "Cannot get the comment key information!", druation: 3000})
                    return false
                }            
                
                var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.userUUID, this.userProtocolUUID)
                if(this.$utils.isEmpty(protocol)){
                    this.$toast.create.negative({html: "Cannot find the protocol for this comment!", druation: 3000})
                    return false
                }
                
                if(this.$utils.isEmpty(protocol.comments)){
                    protocol.comments = {}
                }
                protocol.comments[key] = newVal
                
                this.$utils.saveUserProtocol(protocol)
                .then((data) => {
                    console.log(data) 
                    this.setProtocolByUserUUIDANDProtocolUUID(protocol)
                })
                .catch((err) => {
                    this.$toast.create.negative({html: "Cannot update protocol: " + this.userProtocolUUID + " comment : " + key, druation: 3000})
                    console.log(err)
                });
                
                var keyArr = key.split('____')
                var userInfo = JSON.parse(localStorage.userInfo)
                var comment = {}
                comment.protocolUserUUID = this.userUUID
                comment.protocolUUID = this.userProtocolUUID
                comment.commentUUID = keyArr[2]
                comment.userUUID = userInfo.userUUID
                comment.protocolTitle = this.title
                comment.content = newVal
                comment.path = ''
                
                this.$utils.saveComment(comment)
                .then((data) => {
                    console.log(data)
                    this.$set(this.comments, key, newVal)   
//                    if(this.hiddenCommentEditors.indexOf(commentKey) >= 0){
//                        this.$utils.removeArrayElementByValue(this.hiddenCommentEditors, commentKey)
//                    }
                })
                .catch((err) => {
                    this.$toast.create.negative({html: "Cannot update protocol: " + this.userProtocolUUID + " comment : " + comment.commentUUID, druation: 3000})
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
                
                if(this.$utils.isEmpty(protocol.comments)){
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
            VueTinymce, QCard, QCardTitle, QCardSeparator, QCardMain, QBtn, QField, QInput, QLayout,QItem, QList, QItemSide, QItemMain,
            ToggleTextarea,
        },
        created: function() {
        
            try{
                var userUUID = this.$route.params.userUUID
                if(this.$utils.isEmpty(userUUID)){
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
                this.userProtocolUUID = this.$utils.getTimeUUID()
                this.showEditor = true
                return false
            }
            
            var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.userUUID, this.userProtocolUUID)

            if(!this.$utils.isEmpty(protocol)){
                this.resetUserProtocol(protocol)
                return false
            }

            this.$utils.getUserProtocol(this.userUUID, this.userProtocolUUID)
            .then((data) => {
                console.log(data)
                this.resetUserProtocol(data)
            })
            .catch((err) => {
                alert("oops, something happened")
                console.log(err)
            });
            
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

.list-group .list-group-item {
    border-left-width: 0;
    border-right-width: 0;
}

span.fileBtn {
    margin-right: 10px;
    margin-bottom: 10px;
    font-family: Arial;
    font-size:12px;
    font-weight: bolder;
}

.q-item-section+.q-item-section {
    margin-left: 20px !important;
}

.comment-signature {
    font-weight: bold;
    font-size: 15px;
}

.commentDelBtn {
    font-weight: bolder;
    font-family: Arial;
    font-size: .8rem;
}

div.protocolUploadDiv .q-field-label-inner {
    float: right;
}
</style>
