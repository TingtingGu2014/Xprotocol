<template id="sign-up-template">
    
    <div class="container"><br><br>
        <h3>You Made the Following Comments Recently</h3>        
        <b-table :fields="fields" :items="tableDataForDisplay">            
            
            <template slot="protocolTitle" scope="data">
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
                    { key: 'protocolTitle', label: 'Protocol Title',  'class': 'w-30'},      
                    { key: 'content', label: 'Contents', 'class': 'w-50'}, 
                    { key: 'time', label: 'Time', 'class': 'w-30'},
                ],
                rawData: [],
            }
        },
        props: {
            userUUID: String,
        },
        computed: {
            tableDataForDisplay: function(){
                var items = []
                if(!Utils.isEmpty(this.rawData)){
                    for(var i = 0; i < this.rawData.length; i++){
                        items[i] = {}
                        var row = this.rawData[i]
                        $.each(row, function(key, value){
                            if('commentUUID' == key){
                                var time = Utils.getTimeFromTimeUUID(value)
                                items[i].time = time
                            }
                            else if('protocolTitle' == key){
                                var templateData = {}
                                templateData.label = row.protocolTitle
                                templateData.name = 'userProtocol'
                                templateData.params = {}
                                templateData.params.userUUID = row.userUUID
                                templateData.params.userProtocolUUID = row.protocolUUID
                                items[i].protocolTitle = templateData
                            }
                            else{
                                items[i][key] = value
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
            
            Utils.getCommentsByUserUUID(userUUID)
            .then((data) => {
                console.log(data)                
                this.rawData = data
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