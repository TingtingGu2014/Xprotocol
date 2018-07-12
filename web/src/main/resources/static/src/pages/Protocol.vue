<template>
        <div style="width:90%; margin: auto; " ref="protocolDiv" id="protocolDiv">
            <div class="row">   
                <div class="col-lg-6 col-md-12" v-if="showEditor">
                    <div class="q-card " style="margin: 80px 10px 0 10px; background-color: #278596;" v-bind:style="{ height: (Number(height)+350)+'px'}">
                        <q-card-title style="font-family: Georgia, 'Times New Roman', Times, serif; color: #d5d9e0;">
                            <q-input v-model="title" placeholder="Add some text..."  />                         
                        </q-card-title>
                        <q-card-separator />
                        <q-card-main>
                            
                            <VueTinymce 
                                @content-change="contentChange"
                                @editor-file-uploaded="editorFileUploaded"
                                :id='userProtocolUUID'
                                :userUUID='protocolUserUUID'
                                :body='body' 
                                :height='height'
                                :width='width'
                            ></VueTinymce>    
                            <div class="text-center" v-if="isProtocolAuthor">
                                <q-btn size="sm" color="blue" style="margin-top:25px;" v-on:click.prevent="saveUserProtocol">
                                    <span><i class="fa fa-floppy-o" aria-hidden="true"></i>&nbsp;&nbsp;Save</span>                                        
                                </q-btn>
                            </div>
                        </q-card-main>
                    </div>
                </div>

                <div class="col-md-12" v-bind:class="{ 'col-lg-6' :  showEditor }">
                    <q-card style="margin: 80px 10px 0 10px; background-color: #278596;" v-bind:style="{height: (Number(height)+350)+'px'}" >
                        <q-card-title style="font-family: Georgia, 'Times New Roman', Times, serif; color: #d5d9e0;">
                            {{title}}                          
                        </q-card-title>
                        <q-card-separator />
                        <q-card-main>
                            <p id="pbody" v-html="body" v-bind:style="{height: (Number(height)+145)+'px'}"></p>
                            <div class="text-center" v-if="isProtocolAuthor">
                                <q-btn size="sm" color="blue" v-on:click="toggleEditor" style="margin-top:5px;">
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
                        <div class="displayFileDiv" >
                            <q-list highlight class="row">
                                <q-item v-if="!files || files.length == 0" style="margin:auto">There are no files associated with this protocol</q-item>
                                <q-item dense 
                                    highlight="false" 
                                    v-else 
                                    v-for="fileInfo in displayFiles" 
                                    class="col-5"
                                    :key="fileInfo.currentName" 
                                    style="margin: .5em 4rem 2rem 1em; color: #d5d9e0"
                                >
                                    <q-item-side left>
                                        <img :src="fileInfo.currentName" class="responsive" alt="No preview" width="40em">
                                    </q-item-side>
                                    <q-item-main>
                                        <a :href="fileInfo.currentName + '?name='+fileInfo.originalName" target="_blank" class="form-link-wo-bg protocol-file-list-link">
                                        <i class="fa fa-download" style="font-">&nbsp;&nbsp;<span class="protocol-file-list-link-text">DOWNLOAD</span></i>
                                        </a>        
                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                        <a class="imgDelBtn form-link-wo-bg protocol-file-list-link" href="javascript:void(0)" 
                                            :id="fileInfo.currentName" 
                                            :name="fileInfo.originalName" 
                                            @click.prevent="deleteProtocolFiles"
                                            v-if="isProtocolAuthor"
                                        >
                                            <i class="fa fa-trash-o">&nbsp;&nbsp;<span class="protocol-file-list-link-text">DELETE</span></i>
                                        </a>                                    
                                    </q-item-main>
                                </q-item>
                            </q-list>
                        </div>
                        
                        <div class="protocolUploadDiv row" style="padding-top: 1em" v-if="">
                            <uploader 
                                ref="uploader"
                                :name="'protocolUploader'" 
                                :url="protocolFileUploadUrl" 
                                :fileSizeLimit="getFileSizeLimit"
                                :fileCountLimit="fileCountLimit"
                                :isMultiple=true
                                :isDisabled="fileCountLimit <= 0"
                                :headers="{ 'X-XSRF-TOKEN' :  getCsrfCookie }"
                                @uploadFileDone="updateProtocolAfterFileUpload"
                                style="margin:auto"
                            ></uploader>
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
                                <span style="font-size:1rem; color: #d5d9e0;" >
                                    {{index+1}}.&nbsp;{{keyword}}
                                </span>
                                <a v-bind:id="keyword" href="#" v-if="isProtocolAuthor" v-on:click.prevent="removeKeyword" class="form-link-wo-bg">
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
                                    <div class="col-7">
                                        <input type="text" id="addKeywordInput" v-model="newKeyword"placeholder="Type new keyword here:" />
                                    </div>
                                    <div class="col">
                                        <q-btn class="" color="blue" size="sm" icon="fa-key" @click.prevent="addKeyword">&nbsp;&nbsp;Add Keyword</q-btn>   
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
                        <q-list class="" v-if="comments && Object.keys(comments).length > 0">
                            <q-item v-for="(value, key) in comments" :key="key">
                                <q-item-main>
                                    <div class="row">
                                        <small class="col-5 text-left comment-signature">{{getCommentUserNameFromCommentKey(key)}} said:</small>
                                        <small class="col-7 text-right">{{getCommentDateFromCommentKey(key)}}</small>
                                    </div>
                                    <div style="width: 100%;" v-if="isLoggedInUser && userUUID == getCommentUserUUIDFromCommentKey(key)">
                                        <div class="row">
                                            <toggle-textarea 
                                                :name="key" 
                                                :inputValue="value" 
                                                :otherBtns="[{'color': 'blue', 'icon': 'fa-trash-o', 'id': key, 'label': 'DELETE'}]"
                                                @toggleTextAreaValueChange="updateComment" 
                                                @otherBtnClick="deleteComment"
                                            >                                            
                                            </toggle-textarea>
                                            <q-tooltip anchor="top middle" self="bottom middle" :offset="[10, 10]">
                                                <q-icon name="fa-hand-pointer-o" /><strong>Tap to edit your comment</strong>
                                            </q-tooltip>
                                        </div>
                                    </div>
                                    <div v-else>
                                        <div class="row">
                                            <p class="col commentBody" style="width: 100%;" v-html="value"></p>          
                                        </div>                                        
                                    </div>                                    
                                </q-item-main>
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
                            <div class="col text-center toggle-editor" style="margin-top: 20px">
                                <q-btn color="blue" size="sm" icon="fa-floppy-o" @click.prevent="addComment">&nbsp;&nbsp;Add Comment</q-btn>                                
                            </div>
                        </form>
                    </fieldset>                
            </div>
        </div>
        <br>
        <br>
    </div>
</template>

<script>
    import {QCard, QCardTitle, QCardSeparator, QCardMain, QTooltip} from 'quasar'
    import ToggleTextarea from 'components/ToggleTextArea.vue'
    import VueTinymce from 'components/Editor.vue'
    import Uploader from 'components/Uploader.vue'
    import { mapGetters, mapMutations } from 'vuex'
    
    var FileUtils = require('../utils/FileUtils')
    var ProtocolUtils = require('../utils/ProtocolUtils')
    var userInfo = JSON.parse(localStorage.userInfo)
    var userUUID = !userInfo ? '' : userInfo.userUUID
    
    export default {
        name: 'example',
        data () {
            return {
                userUUID: this.$utils.isEmpty(userUUID) ? '' : userUUID,
                userProtocolUUID: this.$route.params.userProtocolUUID,
                protocolUserUUID: this.$route.params.userUUID,
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
                        var originalName = fileArr[1].replace(/_wsws_/g, ' ')
                        var currentName = fileArr[0]
                        var fileRowHtml = '<span>' + originalName + '</span>'                        
                        var fileExtension = originalName.split('.').pop()
                        let fileInfo = new Object()
                        fileInfo.currentName = currentName
                        fileInfo.originalName = originalName
                        fileInfo.extension = fileExtension

                        fileHtmls.push(fileInfo)
                    }
                }
                return fileHtmls
            },
            isProtocolAuthor: function(){                    
                return this.userUUID === this.protocolUserUUID 
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
            protocolFileUploadUrl: function(){
                return '/api/users/'+this.userUUID+'/protocols/'+this.userProtocolUUID+'/files'
            },
            getCsrfCookie: function(){
                return this.$utils.readCookie('XSRF-TOKEN')
            },
            fileCountLimit: function(){
                let length = 0
                if(!this.$utils.isEmpty(this.files)){
                    length = this.files.length
                }
                return ProtocolUtils.MAXIMUM_PROTOCOL_FILE_NUMBER - length
            },
            getFileSizeLimit: function(){
                return ProtocolUtils.MAXIMUM_PROTOCOL_FILE_SIZE
            },
        },
        watch: {
        },
        methods: {
            saveEditor: function(event){
                saveUserProtocol()
            },
            saveUserProtocol: function(){

                var protocolData = JSON.parse(JSON.stringify(this.$data))
                protocolData.userUUID = this.protocolUserUUID
                protocolData.userProtocolUUID = this.userProtocolUUID
                
                if(this.$utils.isEmpty(protocolData.userUUID) || protocolData.userProtocolUUID === 'new'){                    
                    this.$q.notify({message: `User UUID cannot be empty!`, color: 'negative'})
                    return false;
                }
                
                if(this.$utils.isEmpty(protocolData.title)){                    
                    this.$q.notify({message: `Protocol titile cannot be empty!`, color: 'negative'})
                    return false;
                }
                
                if(this.$utils.isEmpty(protocolData.userProtocolUUID) || protocolData.userProtocolUUID === 'new'){                    
                    this.$q.notify({message: `Protocol UUID cannot be empty!`, color: 'negative'})
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
                    this.$q.notify({message: `Cannot save this protocol with error: `+err.message, color: 'negative'})
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
                    this.$q.notify({message: `Cannot get the protocol Div!`, color: 'negative'})
                }                
                var pbody = document.getElementById('pbody')
                if(this.$utils.isEmpty(protocolDiv)){                    
                    this.$q.notify({message: `Cannot get the pbody!`, color: 'negative'})
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
                var fileName = content.split('____')[0]
                fileName = fileName.split('files/')[1]
                var editorBody = document.getElementById(this.userProtocolUUID + '_ifr').contentWindow.document.getElementsByTagName('body')[0] 
                if(editorBody){
                    var images = editorBody.getElementsByTagName('img')
                    if(!this.$utils.isEmpty(images) && images.length > 0){
                        for(let i = 0; i < images.length; i++){
                            let src = images[i].src
                            if(src.indexOf(fileName) >= 0){
                                images[i].setAttribute("alt", "Image unavailable!")
                                break
                            }
                        }
                    }
                }
                this.$q.notify({message: "File "+ this.getFileOriginalNameFromFileKey(content) +" has been added successfully. It won't be saved until you click the 'Save' button.", color: 'warning', timeout: 3000})                
            },
            toggleEditor: function(){
                this.showEditor = !(this.showEditor)
            },
            // sample fileKey: 
            // /api/users/5ce824f0-bdfe-11e7-9696-0b2512d9785a/protocols/5f444c90-d5e8-11e7-0000-000000000000/files/blobid1517607614524.jpg____digital_book.jpg
            getFileOriginalNameFromFileKey(fileKey){
                let fileKeyArr = fileKey.split('____')
                if(this.$utils.isEmpty(fileKeyArr) || fileKeyArr.length != 2){
                    this.$q.notify({message: `Invalid file key!`, color: 'negative'})
                    return false
                }
                return fileKeyArr[1]
            },
            
            updateProtocolAfterFileUpload: function(file, xhr){
                if(xhr.status != 200){
                    return xhr.getResponseHeader("newName")
                }
                var originalName = xhr.getResponseHeader("originalName")
                var newName = xhr.getResponseHeader("newName")         
                let data = JSON.parse(xhr.responseText)
                let newFileVal = data['location'] + '____' + originalName   
                if(this.$utils.isEmpty(this.files)){
                    this.files = []
                }

                // Update protocol in the storage:
                var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.protocolUserUUID, this.userProtocolUUID)
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
                    this.$q.notify({message: `Cannot save the protocol after uploading the file, error: `+err.message, color: 'negative'})
                    console.log(err)
                });
                this.$q.notify({message: 'File ' + originalName + ' has been successfully uploaded!', color: 'positive', timeout: 3000})   
            },
            deleteProtocolFiles: function(event){
                var vm = this
                var del = confirm('Are you sure to delete this file?')
                if(del == false){
                    return event.preventDefault()
                }                
                var target = event.target
                if(this.$utils.isEmpty(target) || this.$utils.isEmpty(target.id)){
                    target = event.currentTarget
                    if(this.$utils.isEmpty(target) || this.$utils.isEmpty(target.id)){
                        this.$q.notify({message: `Cannot find DOM target ID to delete protocol file!`, color: 'negative'})
                        return false
                    }
                }
                var location = target.id
                var originalName = target.name
                
                FileUtils.deleteProtocolFile(location)
                .then((response) => {
                    let status = response.status
                    let customizedStatus = parseInt(response.data.status)
                    if(vm.$utils.isEmpty(status)){
                        throw error("Cannot get status code from response!")
                    }
                    if(customizedStatus >= 550){
                        vm.$q.notify({message: `Deleting protocol file not working:`+response.data.message, color: 'negative'})
                    }
                    if(status == '200' || customizedStatus >= 550){
                        let fileName = originalName.replace(/\s/g, '_wsws_')
                        let files = vm.files
                        if(!vm.$utils.isEmpty(files) && files.length > 0){
                            files = vm.$utils.removeArrayElementByValue(files, location+'____'+fileName)
                        }
                        var protocol = vm.getProtocolsByUserUUIDANDProtocolUUID(vm.protocolUserUUID, vm.userProtocolUUID)
                        protocol.files = files
                        vm.files = files
                        
                        vm.$utils.saveUserProtocol(protocol)
                        .then((data) => {
                            vm.$q.notify({message: `Deleting file: `+originalName+` succeed!`, color: 'positive', timeout: 3000})
                        })
                        .catch((err) => {
                            vm.$q.notify({message: `Deleting protocol file failed! `+err.message, color: 'negative'})
                            console.log(error.response)
                        });
                    }
                })
                .catch((error) => {                    
                    vm.$q.notify({message: `Deleting protocol file not working!`+err.message, color: 'negative'})
                    console.log(error.response)
                });
                return false
            },
            addKeyword: function(){
                
                if(this.$utils.isEmpty(this.newKeyword)){                    
                    this.$q.notify({message: `Please type in something for a new keyword!`, color: 'warning'})
                    return false
                }
                
                if(this.$utils.isEmpty(this.keywords)){
                    this.keywords = []
                }
          
                this.keywords.push(this.newKeyword)
                
                var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.protocolUserUUID, this.userProtocolUUID)
                protocol.keywords = this.keywords
                
                this.$utils.saveUserProtocol(protocol)
                .then((data) => { 
                    this.setProtocolByUserUUIDANDProtocolUUID(data)
                    this.newKeyword = ''
                })
                .catch((err) => {                    
                    this.$q.notify({message: `Cannot save this protocol with error: `+err.toString(), color: 'negative'})
                    console.log(err)
                });
                                
            },
            removeKeyword: function(event){
                var id = event.target.id
                if(this.$utils.isEmpty(id)){
                    id = event.currentTarget.id
                }
                var keywords = this.keywords
                keywords = this.$utils.removeArrayElementByValue(keywords, id)     
                
                var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.protocolUserUUID, this.userProtocolUUID)
                if(this.$utils.isEmpty(protocol.keywords)){
                    protocol.keywords = {}
                }
                this.keywords = keywords
                protocol.keywords = this.keywords
                
                this.$utils.saveUserProtocol(protocol)
                .then((data) => { 
                    this.setProtocolByUserUUIDANDProtocolUUID(data)
                    this.$q.notify({message: `Keyword has been deleted successfully.`, color: 'positive'})                    
                })
                .catch((err) => {                    
                    this.$q.notify({message: `Cannot delete keyword. `+err.message, color: 'negative'})
                    console.log(err)
                });
            },
            addComment: function(){
                let newVal = this.newComment
                if(this.$utils.isEmpty(newVal)){
                    this.$q.notify({message: `Please type in something for a new comment!`, color: 'warning'})
                    return false
                }
                
                if(this.$utils.isEmpty(this.comments)){
                    this.comments = {}
                }
                var newCommentUUID = this.$utils.getTimeUUID()
                
                var commentUserName = this.$utils.getUserName()                
                
                var comment = {}
                comment.protocolUserUUID = this.protocolUserUUID
                comment.protocolUUID = this.userProtocolUUID
                comment.commentUUID = newCommentUUID
                comment.userUUID = this.userUUID
                comment.protocolTitle = this.title
                comment.content = newVal
                comment.path = ''
                
                var commentKey = this.userUUID + '____' + commentUserName + '____' + newCommentUUID
                this.$set(this.comments, commentKey, newVal)  
                                
                this.$utils.saveComment(comment)
                .then((data) => {
                    console.log(data)                     
                    this.newComment = ''
                })
                .catch((err) => {                    
                    this.$q.notify({message: `Cannot save this comment with error: `+err.message, color: 'negative'})
                    console.log(err)
                });
                
                var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.protocolUserUUID, this.userProtocolUUID)
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
                    this.$q.notify({message: `Cannot save this protocol with the new comment. Error: `+err.message, color: 'negative'})
                    console.log(err)
                });
                
            },
            // comment key: userUUID____userName____protocolUUID
            getCommentUserNameFromCommentKey(key){
                var keyArr = key.split('____')
                if(!keyArr || keyArr.length != 3){
                    this.$q.notify({message: 'The comment key: ' + key + ' is wrong! Error: '+err.message, color: 'negative'})                    
                }
                return keyArr[1]
            },
            getCommentUserUUIDFromCommentKey(key){
                var keyArr = key.split('____')
                if(!keyArr || keyArr.length != 3){
                    this.$q.notify({message: 'The comment key: ' + key + ' is wrong!', color: 'negative'})                    
                }
                return keyArr[0]
            },
            getCommentDateFromCommentKey(key){
                var keyArr = key.split('____')
                if(!keyArr || keyArr.length != 3){                    
                    this.$q.notify({message: 'The comment key: ' + key + ' is wrong!', color: 'negative'})
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
                var currentUserUUID = this.userUUID
                var keyArr = commentKey.split('____')
                if(!keyArr || keyArr.length != 3){                    
                    this.$q.notify({message: 'The comment key: ' + key + ' is wrong!', color: 'negative'})
                }
                var commentUserUUID = keyArr[0]
                return currentUserUUID == commentUserUUID
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
                    this.$q.notify({message: `Cannot get the comment key information!`, color: 'negative'})
                    return false
                }            
                
                var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.protocolUserUUID, this.userProtocolUUID)
                if(this.$utils.isEmpty(protocol)){                    
                    this.$q.notify({message: `Cannot find the protocol for this comment!`, color: 'negative'})
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
                    this.$q.notify({message: "Cannot update protocol: " + this.userProtocolUUID + " comment : " + key + '. Error:'+err.message, color: 'negative'})
                    console.log(err)
                });
                
                var keyArr = key.split('____')
                var comment = {}
                comment.protocolUserUUID = this.protocolUserUUID
                comment.protocolUUID = this.userProtocolUUID
                comment.commentUUID = keyArr[2]
                comment.userUUID = this.userUUID
                comment.protocolTitle = this.title
                comment.content = newVal
                comment.path = ''
                
                this.$utils.saveComment(comment)
                .then((data) => {
                    console.log(data)
                    this.$set(this.comments, key, newVal)   
                })
                .catch((err) => {                    
                    this.$q.notify({message: "Cannot update protocol: " + this.userProtocolUUID + " comment : " + comment.commentUUID+'. Error:'+err.message, color: 'negative'})
                    console.log(err)
                });
                
            },
            deleteComment: function(id){
                var del = confirm('Are you sure to delete this comment?')
                if(del == false){
                    return false
                } 
                if(this.$utils.isEmpty(id)){                    
                    this.$q.notify({message: `Cannot find the comment ID!`, color: 'negative'})
                    return false
                }
                var keyArr = id.split('-edit-')
                var commentKey = keyArr[0]
                
                var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.protocolUserUUID, this.userProtocolUUID)
                if(this.$utils.isEmpty(protocol)){                    
                    this.$q.notify({message: `Cannot find the protocol for this comment!`, color: 'negative'})
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
                    this.$q.notify({message: `Protocol with deleted comment saved`, color: 'positive'})                    
                })
                .catch((err) => {
                    this.$q.notify({message: `Cannot save the protocol with deleted comment. Error:`+err.message, color: 'negative'})                    
                    console.log(err)
                });
                
                var keyArr = commentKey.split('____')
                var commentUUID = keyArr[2]
                var userUUID = this.userUUID
                
                this.$utils.deleteComment(userUUID, commentUUID)
                .then((status) => {
                    this.$q.notify({message: `Deleted the comment `+commentUUID, color: 'positive'})                        
                })
                .catch((err) => {
                    this.$q.notify({message: `Deleting comment failed. Error:`+err.message, color: 'negative'})                        
                    console.log(err)
                });
            },   
            
        },
        components: {
            VueTinymce, ToggleTextarea, Uploader,
            QCard, QCardTitle, QCardSeparator, QCardMain, QTooltip
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
            
            var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.protocolUserUUID, this.userProtocolUUID)

            if(!this.$utils.isEmpty(protocol)){
                this.resetUserProtocol(protocol)
                return false
            }

            this.$utils.getUserProtocol(this.protocolUserUUID, this.userProtocolUUID)
            .then((data) => {
                console.log(data)
                this.resetUserProtocol(data)
            })
            .catch((err) => {                
                this.$q.notify({message: `Cannot get this protocol with error: `+err.message, color: 'negative'})
                console.log(err)
            });
            
        },
        updated: function(){
            var pbody = document.getElementById('pbody')
            if(pbody){
                var images = pbody.getElementsByTagName('img')
                if(images && images.length > 0){
                    for(let i = 0; i < images.length; i++){
                        images[i].alt = "Image not available"
                    }
                }
            }
            this.body = pbody.innerHTML
            var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.protocolUserUUID, this.userProtocolUUID)
            if(!this.$utils.isEmpty(protocol)){
                protocol.body = pbody.innerHTML
            }
        }
    }
</script>

<style scoped>
.q-card-title {
    height: 3em;
    font-weight: bold;
    text-align: center;
}

.q-list {
    color: #d5d9e0;
}

.layout-footer {
    height: 3em;
}

.protocolUpdt{
    margin-bottom: 15px;
}

#pbody {
    word-wrap: break-word;
    overflow: auto;
}

#pbody img {
    font-size: 12px;
    color: DodgerBlue;
    padding: 2px 2px 2px 2px;
    margin: 0 5px 0 5px;
    font-family: "Times New Roman", Times, serif;
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

div.protocolUploadDiv .q-field-label-inner {
    float: right;
}

.commentBody {
    white-space: pre-wrap;
    background: #e6f7ff;
    color: #d5d9e0;
    font-family: Georgia, "Times New Roman", Times, serif;
    margin: 0 40px 0 40px;
    padding: 8px 8px 8px 8px;
    text-decoration: none !important;
}

.del-comment-btn {
    border: 2px solid lightgreen; 
    padding: 2px 10px 2px 10px;
    background-color: #2196f3; 
    color: white;
    box-shadow: 0 1px 5px rgba(0,0,0,.2), 0 2px 2px rgba(0,0,0,.14), 0 3px 1px -2px rgba(0,0,0,.12);
    text-transform: uppercase;
    border-radius: 2px;
    margin-top: 5px;
}

.del-comment-btn-span {
    font-family: Arial, Helvetica, sans-serif; 
    font-size: 11px; 
    font-weight: 500;
}

input[type="text"]
{
    font-size:.5em;
}

.protocol-file-list-link {
    font-size: 1em;
    color: #d5d9e0;
}

.protocol-file-list-link-text {
    font-family: Arial, Helvetica, sans-serif;
    font-weight: bold;
}

a.form-link-wo-bg:hover{
    color: #90ee90;
    font-weight: bold;
}

input[type="text"]
{
    font-size:.5em;
}

input #addKeywordInput {
    color: #d5d9e0;
    font-size: .5em;
}

.badge-info {
    color: #d5d9e0;
}

fieldset {
    background-color: #278596;
}

::placeholder { /* Chrome, Firefox, Opera, Safari 10.1+ */
    color: #d5d9e0;
    font-size: 1em;
    opacity: 1; /* Firefox */
}

:-ms-input-placeholder { /* Internet Explorer 10-11 */
    color: #d5d9e0;
    font-size: 1em;
}

::-ms-input-placeholder { /* Microsoft Edge */
    color: #d5d9e0;
    font-size: 1em;
}
</style>
