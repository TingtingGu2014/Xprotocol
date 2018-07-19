<template id="sign-up-template">
    
    <div class="row" style="width:80%; margin: auto; margin-top: 7%; margin-bottom: 30px; overflow-y: auto" >
        <div class="row text-center">
            <span class="q-table-title">List of Your Protocols</span>
        </div>

        <q-table
            :data="tableDataForDisplay"
            :columns="columns"
            :pagination.sync="pagination"
            style="width: 100%"
        >
            <div slot="top" slot-scope="props" class="row flex-left fit">
                <router-link 
                   :to="{ path: 'users/'+userUUID+'/protocols/new'}"
                   class="link-wo-bg"
               >
                <i class="fa fa-plus-square-o" aria-hidden="true"></i>&nbsp;Create a New Protocol
            </router-link>
            </div>
         
            <q-td slot="body-cell-title" slot-scope="props" :props="props" class="protocol-title-td">
                <router-link 
                    :to="{ name: 'protocol', params: { userUUID: props.row.userUUID,  userProtocolUUID: props.row.userProtocolUUID }}"
                    class="qtable-item-link"
                >
                    &nbsp;&nbsp;{{props.row.title}}
                </router-link>
            </q-td>            
        </q-table>
    </div>
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
                pagination: {
                    sortBy: null, 
                    descending: false,
                    page: 1,
                    rowsPerPage: 10
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
                if(!this.$utils.isEmpty(this.rawData)){
                    for(var i = 0; i < this.rawData.length; i++){
                        items[i] = {}
                        var row = this.rawData[i]
                        for (var key in row) {
                            items[i][key] = row[key]                            
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
                console.log(err)
                this.$q.notify({message: "Cannot load the protocols. Error: "+err.message, duration: 3000, color: "negative"})
            });
        },
        components: {
            
        },
    }
</script>

<style>

.q-table thead th {

    white-space: normal;
}

.q-table tbody td {

    white-space: normal;
}

.protocol-title-td {
    width: 20%;
}
</style>