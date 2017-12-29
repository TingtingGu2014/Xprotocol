<template id="sign-up-template">
    
    <div class="container"><br><br>
        <h3>List of Protocols</h3>
        <router-link :to="{ path: 'users/'+userUUID+'/protocols/new'}">&nbsp;&nbsp;&nbsp;&nbsp;<i class="fa fa-plus-square-o" aria-hidden="true"></i>&nbsp;New Protocol</router-link>
        <b-table :fields="fields" :items="tableDataForDisplay">            
            <template slot="index" scope="data">
              {{data.index + 1}}
            </template>
            
            <template slot="title" scope="data">
                <table-router-link :linkData = "data.value"></table-router-link>                
            </template>

        </b-table>
    </div>
</template>

<script>

    import { mapGetters, mapMutations } from 'vuex'
    import TableRouterLink from './vuetablecomponents/TableRouterLink.vue'
    
    var Utils = require('./Utils')
    
    export default {
        data: function() {
            return {
                fields: [                    
                    'index',                    
                    'title',                    
                    'projects',                    
                    { key: 'keywords', label: 'Key Words' },                    
                    { key: 'versions', label: 'Archived Versions' }
                ],
                rawData: [],
            }
        },
        props: {
            userUUID: String,
        },
        computed: {
            ...mapGetters({                
                getProtocolsByUserUUID: 'protocolModule/getProtocolsByUserUUID',
                getProtocolsByUserUUIDANDProtocolUUID: 'protocolModule/getProtocolsByUserUUIDANDProtocolUUID',
            }),
            tableDataForDisplay: function(){
                var items = []
                if(!Utils.isEmpty(this.rawData)){
                    for(var i = 0; i < this.rawData.length; i++){
                        items[i] = {}
                        var row = this.rawData[i]
                        $.each(row, function(key, value){
                            if('title' != key){
                                items[i][key] = value
                            }
                            else{
                                var templateData = {}
                                templateData.label = row.title
                                templateData.name = 'userProtocol'
                                templateData.params = {}
                                templateData.params.userUUID = row.userUUID
                                templateData.params.userProtocolUUID = row.userProtocolUUID
                                items[i].title = templateData
                            }
                        })
                    }
                }
                return items
            },
        },
        methods: {
                ...mapMutations({
                setProtocolsByUserUUID: 'protocolModule/setProtocolsByUserUUID',
                setProtocolByUserUUIDANDProtocolUUID: 'protocolModule/setProtocolByUserUUIDANDProtocolUUID',
            }),
        },
        beforeMount: function(){
        
            var userUUID = this.userUUID
            
            if(Utils.isEmpty(userUUID)){
                var message = 'User UUID cannot be empty!'
                alert(message)
                sessionStorage.errorMessage = message
                document.location.href = '/errors/400'
                return false
            }
            
            if(!Utils.isEmpty(localStorage.protocolListCount)){
                var protocolList = this.getProtocolsByUserUUID(userUUID)

                if(!Utils.isEmpty(protocolList) && Number(localStorage.protocolListCount) === protocolList.length){                
                    console.log(protocolList)
                    this.rawData = protocolList
                    return false
                }
            }

            Utils.getProtocolsByUserUUID(userUUID)
            .then((data) => {
                console.log(data)                
                this.rawData = data
                var protocolsData = {}
                protocolsData[userUUID] = data
                this.setProtocolsByUserUUID(protocolsData)
                localStorage.protocolListCount = Object.keys(data).length
            })
            .catch((err) => {
                alert("oops, something happened")
                console.log(err)
            });
        },
        components: {
            TableRouterLink,
        },
    }
</script>


</style>