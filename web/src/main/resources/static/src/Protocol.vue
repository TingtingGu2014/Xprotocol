<template>
    <div>
    
        <div class="container">    
            <div class="row">
                <div class="col">
                    <br><h4>{{title}}</h4><br>
                    <VueTinymce id='terms' @content-change="contentChange"
                        :body='body' 
                        :max_height='max_height'
                        :max_width='max_width'
                        :min_height='min_height'
                        :min_width='min_width'
                        :height='height'
                        :width='width'
                        :showPreview='showPreview'
                    ></VueTinymce> 
                </div>

                <div class="col" v-if="showPreview">
                    <div class="card " style="margin-top: 100px" v-bind:style="{width: width+'px', height: (Number(height)+180)+'px'}">
                        <div class="card-block" style="overflow:scroll">
                          <h4 class="card-header mb-3 text-center" style="font-weight: bolder; font-size: 15px; color: midnightblue">{{title}}</h4>
                          <p id="pbody" v-html="body"></p>
                        </div>
                    </div>       
                </div>
            </div>
        </div>
        <br>
        <button type="button" class="btn btn-outline-success btn-lg"
            v-on:click.prevent="saveUserProtocol"
        >
            <span style="font-weight: bold">Save</span>
        </button>
    </div>
</template>

<script>
    import VueTinymce from './Editor.vue'
    import { mapGetters, mapMutations } from 'vuex'
    
    var Utils = require('./Utils')
    var loggedIn = !Utils.isEmpty(Utils.readCookie('loggedIn'))
    
  export default {
    name: 'example',
    props: {
        userProtocolUUID: String,
        userUUID: String,
        editorName: String,
        showPreview: Boolean,
    },
    data () {
        return {
            loggedIn: loggedIn,
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
    },
    components: {
        VueTinymce
    },
    created: function() {

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
        return false   
    },
}
</script>

<style>

</style>
