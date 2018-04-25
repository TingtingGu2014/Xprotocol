<template id="sign-up-template">
    <q-page padding class="docs-table">
        <p class="caption">Basic example</p>
        <!--<div class="row" style="width:80%; margin: auto; margin-top: 7%; margin-bottom: 30px; overflow-y: auto" >-->
    <!--        <div class="row text-center">
                <span class="q-table-title">List of Your Protocols</span>
            </div>
            <br>
            <div class="row text-left">
                <router-link :to="{ path: 'users/'+userUUID+'/protocols/new'}">&nbsp;&nbsp;&nbsp;&nbsp;<i class="fa fa-plus-square-o" aria-hidden="true"></i>&nbsp;Create a New Protocol</router-link>
            </div>-->
            <q-table
                :data="tableDataForDisplay"
                :columns="columns"
                
                style="overflow-x: scroll"
            >
            
                <q-td auto-width slot="body-cell-title" slot-scope="props" :props="props">
                    <router-link 
                    :to="{ name: 'protocol', params: { userUUID: props.row.userUUID,  userProtocolUUID: props.row.userProtocolUUID }}"
                    class="qtable-item-link"
                >
                        &nbsp;&nbsp;{{props.row.title.label}}
                    </router-link>
                </q-td>
            </q-table>
        <!--</div>-->
    </q-page>
</template>

<script>
    
    import { mapGetters, mapMutations } from 'vuex'
    
    export default {
        data: function() {
            return {
                columns: [
                    {field: 'title', name: 'title', label: 'Title', align: 'left', sortable: true,},
                    {field: 'projects', name: 'projects', label: 'Projects', align: 'left', sortable: true},
                    {field: 'keywords', name: 'keywords', label: 'Key Words', align: 'left',},                    
                    {field: 'versions', name: 'versions', label: 'Versions',  align: 'left',},                    
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
                if(!this.$utils.isEmpty(this.rawData)){
                    for(var i = 0; i < this.rawData.length; i++){
                        items[i] = {}
                        var row = this.rawData[i]
                        for (var key in row) {
                            let value = row[key]
//                            items[i][key] = value
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
            
            if(this.$utils.isEmpty(userUUID)){
                var message = 'User UUID cannot be empty!'
                alert(message)
                sessionStorage.errorMessage = message
                document.location.href = '/errors/400'
                return false
            }
            
            if(!this.$utils.isEmpty(localStorage.protocolListCount)){
                var protocolList = this.getProtocolsByUserUUID(userUUID)

                if(!this.$utils.isEmpty(protocolList) && Number(localStorage.protocolListCount) === protocolList.length){                
                    console.log(protocolList)
                    this.rawData = protocolList
                    return false
                }
            }

            this.$utils.getProtocolsByUserUUID(userUUID)
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