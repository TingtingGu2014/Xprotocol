<template>
    <div>
        <div class="container">    
            <div class="row">
                <div class="col">
                <div class="card " style="margin-top: 100px" v-bind:style="{ height: (Number(height)+320)+'px'}">
                        <div class="card-block" >
                    <h4 class="card-header mb-3 text-center" v-bind:style="{}" style="font-weight: bolder; font-size: 15px; color: midnightblue">
                        <input class="form-control text-center" type="text" v-model="title" id="example-text-input">
                    </h4>
                    <VueTinymce id='terms' @content-change="contentChange"
                        :body='body' 
                        :max_height='max_height'
                        :max_width='max_width'
                        :min_height='min_height'
                        :min_width='min_width'
                        :height='height'
                        :width='width'
                    ></VueTinymce>                     
                    </div>
                    <div v-if="!preview" class="text-center" style="border-top: lightgray solid thin ">
                        <a href="#" class="btn" v-on:click="togglePreview">Show Preview</a>
                    </div>
                    </div>
                </div>

                <div class="col" v-if="preview">
                    <div class="card " style="margin-top: 100px" v-bind:style="{width: width+'px', height: (Number(height)+320)+'px'}">
                        <div class="card-block" style="overflow:scroll">
                          <h4 class="card-header mb-3 text-center" style="font-weight: bolder; font-size: 15px; color: midnightblue">{{title}}</h4>
                          <p id="pbody" v-html="body"></p>
                        </div>
                        <div v-if="preview" class="text-center">
                            <a href="#" class="btn" v-on:click="togglePreview">Hide Preview</a>
                        </div>
                    </div>       
                </div>
            </div>
        </div>
        <br>
        <div class="text-center">
            <button type="button" class="btn btn-outline-success btn-lg"
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
                preview: true,
            }
        },
        computed: {
            ...mapGetters({                
                getProtocols: 'protocolModule/getProtocols',
                getProtocolsByUserUUID: 'protocolModule/getProtocolsByUserUUID',
                getProtocolsByUserUUIDANDProtocolUUID: 'protocolModule/getProtocolsByUserUUIDANDProtocolUUID',
            }),
        },
        methods: {
            saveUserProtocol: function(event){

                var protocolData = JSON.parse(JSON.stringify(this.$data))
                protocolData.userUUID = this.userUUID
                protocolData.userProtocolUUID = this.userProtocolUUID

                Utils.saveUserProtocol(protocolData)
                .then((data) => {
                    this.resetUserProfile(data)
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
            togglePreview: function(){
                this.preview = !(this.preview)
            }
        },
        components: {
            VueTinymce
        },
        created: function() {
            
            var userUUID = this.$route.params.userUUID
            if(Utils.isEmpty(userUUID)){
                var message = 'User UUID cannot be empty!'
                alert(message)
                sessionStorage.errorMessage = message
                document.location.href = '/errors/400'
                return false
            }
            
            var userProtocolUUID = this.$route.params.userProtocolUUID
            if(Utils.isEmpty(userProtocolUUID) || userProtocolUUID == 'new'){
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
    }
</script>

<style>

</style>
