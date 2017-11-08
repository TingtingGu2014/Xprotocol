<template>
    <div>
        <h3 style="font-weight: bold">{{title}}</h3>
        <Editor
            :editorName = "editorName"
            :body = "body"
            :max_height = "max_height"
            :max_width = "max_width"
            :min_height = "min_height"
            :min_width = "min_width"
            @content-change = "contentChange"
        >            
        </Editor>
        <br>
        <button type="button" class="btn btn-outline-success btn-lg"
            v-on:click.prevent="saveUserProtocol"
        >
            <span style="font-weight: bold">Save</span>
        </button>
    </div>
</template>

<script>
    import Editor from './Editor.vue'
    import { mapGetters, mapMutations } from 'vuex'
    
    var Utils = require('./Utils')
    var loggedIn = !Utils.isEmpty(Utils.readCookie('loggedIn'))
    
    
    export default {
        props: {
            protocolUUID: String,
            userUUID: String,
            editorName: String,
        },
        data: function(){
            return {                
                loggedIn: loggedIn,
                max_height: 500,
                max_width: 500,
                min_height: 300,
                min_width: 400,                
                title: '',
                body: '',
                projectUUIDs: [],
                projectTitles: [],
                files: [],
                comments: null,
                versions: [],
                keywords: [],
            }
        },
        computed: {
            ...mapGetters({                
                getProtocols: 'protocolModule/getProtocols',
                getProtocolsByUserUUID: 'protocolModule/getProtocolsByUserUUID',
                getProtocolsByUserUUIDANDProtocolUUID: 'protocolModule/getProtocolsByUserUUIDANDProtocolUUID',
            }),
        },
        components: {
            Editor,
        },
        methods: {
            saveUserProtocol: function(event){

                var protocolData = JSON.parse(JSON.stringify(this.$data))
                protocolData.userUUID = this.userUUID
                protocolData.protocolUUID = this.protocolUUID
                
                Utils.saveUserProtocol(protocolData)
                .then((data) => {
                    alert(data)
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
            }
        },
        created: function() {
            
            var protocol = this.getProtocolsByUserUUIDANDProtocolUUID(this.userUUID, this.protocolUUID)
            
            if(!Utils.isEmpty(protocol)){
                return protocol
            }
            
            Utils.getUserProtocol(this.userUUID, this.protocolUUID)
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
  p{
    color: #69C;
  }
</style>
