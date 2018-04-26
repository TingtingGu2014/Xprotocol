<template id="sign-up-template">
    
    <div class="row" style="width:80%; margin: auto; margin-top: 7%; margin-bottom: 30px; overflow-y: auto" >
        <div class="row text-center">
            <span class="q-table-title">Your Recent Comments</span>
        </div>
        <q-table
            :data="tableDataForDisplay"
            :pagination.sync="pagination"
            :columns="columns"
            style="width: 100%"
        >
            <q-td slot="body-cell-protocolTitle" slot-scope="props" :props="props">
                <router-link 
                    :to="{ name: 'protocol', params: { userUUID: props.row.protocolUserUUID,  userProtocolUUID: props.row.protocolUUID }}"
                    class="qtable-item-link"
                >
                    {{props.row.protocolTitle}}
                </router-link>
            </q-td>
            
            <q-td slot="body-cell-content" slot-scope="props" :props="props" class="comment-content-td">
                {{ props.row.content }}
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
                    {field: 'protocolTitle', name: 'protocolTitle', label: 'Protocols', align: 'left', sortable: true, filter: true},
                    {field: 'content', name: 'content', label: 'Comments', align: 'left', sortable: true, filter: true},
                    {field: 'time', name: 'time', label: 'Time', align: 'left', sortable: true, filter: true},                                       
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
            tableDataForDisplay: function(){
                var items = []
                if(!this.$utils.isEmpty(this.rawData)){
                    for(var i = 0; i < this.rawData.length; i++){
                        items[i] = {}
                        var row = this.rawData[i]
                        for (var key in row) {
                            let value = row[key]
                            if('commentUUID' == key){
                                var date = this.$utils.getTimeFromTimeUUID(value)
                                items[i].time = this.$utils.outputDateTime(date)
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
            
            if(this.$utils.isEmpty(userUUID)){
                var message = 'User UUID cannot be empty!'
                alert(message)
                sessionStorage.errorMessage = message
                document.location.href = '/errors/400'
                return false
            }
            
            this.$utils.getCommentsByUserUUID(userUUID)
            .then((data) => {
//                console.log(data)                
                this.rawData = data
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

.q-table thead th {

    white-space: normal;
}

.q-table tbody td {

    white-space: normal;
}

.comment-content-td {
    width: 50%;
}

</style>