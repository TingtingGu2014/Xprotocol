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
    <!--                    <div v-if="!preview" class="text-center" style="border-top: lightgray solid thin ">
                            <a href="#" class="btn" v-on:click="toggleEditor">Show Preview</a>
                        </div>-->
                    </div>
                </div>

                <div class="col">
                    <div class="card " style="margin-top: 100px" v-bind:style="{height: (Number(height)+320)+'px'}">
                        <div class="card-block" style="overflow:scroll">
                          <h4 class="card-header mb-3 text-center" style="font-weight: bolder; font-size: 15px; color: midnightblue">{{title}}</h4>
                          <p id="pbody" v-html="body"></p>
                        </div>
                        <div class="text-center">
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
                        <legend>Protocol Files:</legend>
                        <div>
                            <ul class="list-group">
                                <li class="list-group-item" v-if="!files || files.length == 0">There are no file associated with this protocol</li>
                                <li class="list-group-item" v-else v-for="file in displayFiles " v-html="file"></li>
                            </ul>
                            <br><br>
                        </div>
                        <div class="form-group">
                            <form class="form-inline">
                            <label for="uploadFileForProtocol">Select a file to upload:&nbsp;&nbsp;</label>
                            <input type="file" class="form-control-file" id="uploadFileForProtocol">
                            <button type="button" class="btn btn-primary"><i class="fa fa-upload" aria-hidden="true"></i>&nbsp;&nbsp;Add a new file</button>
                            </form>
                        </div>
                    </fieldset>
                </div>
            </div>
        </div>
        <br>
        <div class="text-center">
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
                                                '&nbsp;&nbsp;&nbsp;&nbsp;<a href="' + currentName + '?name='+originalName+'" class="btn btn-secondary" target="_blank">Download</a>' +
                                                '&nbsp;&nbsp;&nbsp;&nbsp;<button id="' + currentName + '" type="button" class="btn btn-secondary imgDelBtn">Delete</button>'
                                fileHtmls.push(fileRowHtml)
                            }
                        }
                    }
                }
                return fileHtmls
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
                    this.resetUserProfile(data)
                    document.location.href = '/home'
                })
                .catch((err) => {
                    alert("oops, something happened")
                    console.log(err)
                });
            },
            resetUserProfile: function (protocol){
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
                this.resetUserProfile(protocol)
                return false
            }

            Utils.getUserProtocol(this.userUUID, this.userProtocolUUID)
            .then((data) => {
                console.log(data)
                this.resetUserProfile(data)
            })
            .catch((err) => {
                alert("oops, something happened")
                console.log(err)
            });
        },
        updated: function(){
            $(".imgDelBtn").click(function(){
                var location = ($this).attr('id')
                Utils.deleteProtocolFile(this.userUUID, this.userProtocolUUID, location)
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

.container {
    margin-top: 15px;
}

.protocolUpdt{
    margin-bottom: 15px;
}
</style>
