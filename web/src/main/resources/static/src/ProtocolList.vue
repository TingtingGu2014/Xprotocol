<template id="sign-up-template">
    
    <div class="container"><br><br>
        <h3>List of Protocols</h3>
        <router-link :to="{ path: 'users/'+userUUID+'/protocols/new'}">&nbsp;&nbsp;&nbsp;&nbsp;<i class="fa fa-plus-square-o" aria-hidden="true"></i>&nbsp;New Protocol</router-link>
        <VueDataTable 
            :tableData = "tableData"
            :tableColumns = "tableColumns"
            :showFilter = "showFilter"
        ></VueDataTable>
    </div>
</template>

<script>

    import { mapGetters, mapMutations } from 'vuex'
    import VueDataTable from './VueDataTable.vue'
    
    var Utils = require('./Utils')
    
    export default {
        data: function() {
            return {
                tableData: [],
                tableColumns: [
                    {show: 'title', label: 'Title', dataType: ''},
                    {show: 'project_titles', label: 'Projects', dataType: ''},
                    {show: 'keywords', label: 'Keywords', dataType: ''},
                    {show: 'versions', label: 'Archived Versions', dataType: ''},
                ],
                showFilter: true,
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
        },
        methods: {
            ...mapMutations({
            setProtocolsByUserUUID: 'protocolModule/setProtocolsByUserUUID',
            setProtocolByUserUUIDANDProtocolUUID: 'protocolModule/setProtocolByUserUUIDANDProtocolUUID',
        }),
        },
        beforeMount: function(){
            var protocolList = this.getProtocolsByUserUUID(this.userUUID)

            if(!Utils.isEmpty(protocolList)){
                this.tableData = protocolList
                return false
            }

            Utils.getProtocolsByUserUUID(this.userUUID)
            .then((data) => {
                console.log(data)
                this.tableData = data
                this.setProtocolsByUserUUID(this.userUUID, data)
            })
            .catch((err) => {
                alert("oops, something happened")
                console.log(err)
            });
        },
        components: {
            VueDataTable,
        },
    }
</script>


</style>