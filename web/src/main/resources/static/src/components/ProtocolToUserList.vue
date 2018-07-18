<template id="sign-up-template">
    
    <div class="row" style="width:80%; margin: auto; margin-top: 7%; margin-bottom: 30px; overflow-y: auto" >
        <div class="row text-center">
            <span class="q-table-title">Recent new protocols</span>
        </div>

        <q-table
            :data="tableDataForDisplay"
            :columns="columns"
            :pagination.sync="pagination"
            style="width: 100%"
        >
         
            <q-td slot="body-cell-title" slot-scope="props" :props="props" class="protocol-title-td">
                <router-link 
                    :to="{ name: 'protocol', params: { userUUID: props.row.userUUID,  userProtocolUUID: props.row.protocolUUID }}"
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
                    {field: 'userName', name: 'author', label: 'Author', align: 'left', sortable: true},                     
                    {field: 'email', name: 'email', label: 'Author Email', align: 'left', sortable: false}, 
                    {field: 'time', name: 'time', label: 'Publish Time', align: 'left', sortable: true}, 
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
                getRecentProtocolToUsers: 'protocolModule/getRecentProtocolToUsers',
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
                setRecentProtocolToUsers: 'protocolModule/setRecentProtocolToUsers',
            }),
        },
        beforeMount: function(){
            
            var recentProtocolToUsers = this.getRecentProtocolToUsers()
            if(this.$utils.isEmpty(recentProtocolToUsers)){
                this.$protocolUtils.getRecentPublicProtocols()
                .then((data) => {
                    console.log(data)                
                    this.rawData = data
                    this.setRecentProtocolToUsers(data)
                })
                .catch((err) => {
                    console.log(err)
                    this.$q.notify({message: "Cannot load the new pubic protocols. Error: "+err.message, duration: 3000, color: "negative"})
                });
            }
            else{
                this.rawData = recentProtocolToUsers
            }
            
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