<template id="sign-up-template">
    
    <div class="row" style="width:80%; margin: auto; margin-top: 7%; margin-bottom: 30px; overflow-y: auto" >
        <div class="row text-center">
            <span class="q-table-title">List of Your Protocols</span>
        </div>
        <br>
        <div class="row text-left">
            <router-link :to="{ path: 'users/'+userUUID+'/protocols/new'}">&nbsp;&nbsp;&nbsp;&nbsp;<i class="fa fa-plus-square-o" aria-hidden="true"></i>&nbsp;Create a New Protocol</router-link>
        </div>
        <q-data-table
            :data="tableDataForDisplay"
            :config="configs"
            :columns="columns"
            style="overflow-x: scroll"
        >
            <template slot="col-title" scope="cell">
                <table-router-link :linkData = "cell.data"></table-router-link>                
            </template>
        </q-data-table>
    </div>
</template>

<script>
    import {QDataTable} from 'quasar'
    import { mapGetters, mapMutations } from 'vuex'
    import TableRouterLink from './elements/TableRouterLink.vue'
    
    var Utils = require('../utils/Utils')
    
    export default {
        data: function() {
            return {
                columns: [
                    {field: 'title', label: 'Title', width: '100px', sort: true, filter: true},
                    {field: 'projects', label: 'Projects', width: '80px', filter: true},
                    {field: 'keywords', label: 'Key Words', width: '80px', filter: true},                    
                    {field: 'versions', label: 'Versions', width: '80px'},                    
                ], 
                rawData: [],
                configs: {
                    rowHeight: '50px',
                    bodyStyle: {
  //                    maxHeight: '500px'
                    },
                    pagination: {
                      rowsPerPage: 15,
                      options: [10, 15, 30, 50]
                    },
                    messages: {
                      noData: '<i>warning</i> You do not have any protocols to show.',
                      noDataAfterFiltering: '<i>warning</i> No protocols created'
                    },
                }
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
                        for (var key in row) {
                            let value = row[key]
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
                        }
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
            TableRouterLink,QDataTable
        },
    }
</script>

<style>

.q-table-title{
    font-size: 2.0vw;
    font-family: Helvetica, Arial, Verdana;
    padding: 5px 0px 5px 0px;
    margin: auto;
}

</style>