<template id="sign-up-template">
    
    <div class="row" style="width:80%; margin: auto; margin-top: 7%; margin-bottom: 30px; overflow-y: auto" >
        <div class="row text-center">
            <span class="q-table-title">List of Your Recent Comments</span>
        </div>
        <q-data-table
            :data="tableDataForDisplay"
            :config="configs"
            :columns="columns"
            style="overflow-x: scroll"
        >
            <template slot="col-protocolTitle" scope="cell">
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
                    {field: 'protocolTitle', label: 'Protocols', width: '120px', sort: true, filter: true},
                    {field: 'content', label: 'Comments', width: '120px', filter: true},
                    {field: 'time', label: 'Time', width: '80px', filter: true},                                       
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
                      noData: '<i>warning</i> You do not have any comments to show.',
                      noDataAfterFiltering: '<i>warning</i> No comments made'
                    },
                }
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
                        for (var key in row) {
                            let value = row[key]
                            if('commentUUID' == key){
                                var date = Utils.getTimeFromTimeUUID(value)
                                items[i].time = Utils.outputDateTime(date)
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
            TableRouterLink, QDataTable
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