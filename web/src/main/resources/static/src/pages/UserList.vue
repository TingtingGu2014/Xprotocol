<template id="sign-up-template">
    <div class="row" style="width:80%; margin: auto; margin-top: 7%; margin-bottom: 30px; overflow-y: auto" >
        <span class="q-table-title">User List</span>
        <q-table
            :data="tableData"
            :pagination.sync="pagination"
            :columns="columns"
            style="width: 100%"
        >
            <q-td slot="body-cell-edit" slot-scope="props" :props="props">
                <router-link 
                    :to="{ name: 'userProfileAdmin', params: { userUUID: props.row.userUUID }}"
                    class="qtable-item-link"
                >
                    <span class="fa fa-pencil" aria-hidden="true" ></span>
                </router-link>
            </q-td>
        </q-table>
    </div>
</template>

<script>
    
    export default {
        data: function() {
            return {
                tableData: [],
                columns: [
                    {field: 'firstName', name: 'firstName', label: 'First Name', align: 'left', sortable: true, filter: true},
                    {field: 'lastName', name: 'firstName', label: 'Last Name', align: 'left', sortable: true, filter: true},
                    {field: 'email', name: 'firstName', label: 'Email', align: 'left', sortable: true, filter: true},                    
                    {field: 'createdDate', name: 'firstName', label: 'Registeration Date', align: 'left',  
                            format (value, row) {
                                return new Date(value).toLocaleString()
                            }},
                    {field: 'active', name: 'active', label: 'Active', type: 'boolean', align: 'left', },
                    {field: 'edit', name: 'edit', label: 'Edit', align: 'left', },
                ],          
                pagination: {
                    sortBy: null, 
                    descending: false,
                    page: 1,
                    rowsPerPage: 10
                },
            }
        },
        beforeCreate: function(){
            var loggedIn = !this.$utils.isEmpty(this.$utils.readCookie('loggedIn'))
            if(loggedIn != true){
                this.$router.push('/login')
            }

            var isAdminUser = this.$utils.isAdminUser();
            if(!isAdminUser){
                this.$router.push('/errors/403')
            }
        },
        beforeMount: function(){
            this.$utils.getUserListByAdmin()
            .then((data) => {
                if(data){
                    for(var i in data) {
                        var row = data[i];
                        var editData = {}
                        editData.name = 'userProfileAdmin'
                        editData.params = {}
                        editData.params.userUUID = row.userUUID
                        editData.label = '<span class=\"fa fa-pencil\" aria-hidden=\"true\" ></span>'
                        row['edit'] = editData
                    }
                    this.tableData = data
                }                    
            })
            .catch((err) => {
                Alert.create({html: 'oops, something happened during pulling out the user list!'})
                console.log(err)
            });
        },
        components: {
        },
    }
</script>

<style>
</style>