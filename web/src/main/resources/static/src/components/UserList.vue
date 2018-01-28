<template id="sign-up-template">
    <div class="row" style="width:80%; margin: auto; margin-top: 7%; margin-bottom: 30px; overflow-y: auto" >
        <span class="q-table-title">User List</span>
        <q-data-table
            :data="tableData"
            :config="configs"
            :columns="columns"
            style="overflow-x: scroll"
        >

            <template slot="col-edit" scope="cell">                
                <table-router-link :linkData = "cell.data"></table-router-link>                
            </template>
        </q-data-table>
    </div>
</template>

<script>
    import {QDataTable} from 'quasar'
    import TableRouterLink from './elements/TableRouterLink.vue'
    
    var Utils = require('../utils/Utils')
    
    var loggedIn = !Utils.isEmpty(Utils.readCookie('loggedIn'))
    if(loggedIn != true){
        document.location.href = '/login'
    }
    
    var isAdminUser = Utils.isAdminUser();
    if(!isAdminUser){
        document.location.href = '/errors/403'
    }
    
    export default {
        data: function() {
            return {
                tableData: [],
                columns: [
                    {field: 'firstName', label: 'First Name', width: '80px', sort: true, filter: true},
                    {field: 'lastName', label: 'Last Name', width: '80px', sort: true, filter: true},
                    {field: 'email', label: 'Email', width: '120px', sort: true, filter: true},                    
                    {field: 'createdDate', label: 'Registeration Date', width: '60px', 
                            format (value, row) {
                                return new Date(value).toLocaleString()
                            }},
                    {field: 'active', label: 'Active', type: 'boolean', width: '40px'},
                    {field: 'edit', label: 'Edit', width: '30px'},
                ],          
                configs: {
                  rowHeight: '50px',
                  bodyStyle: {
//                    maxHeight: '500px'
                  },
                  pagination: {
                    rowsPerPage: 15,
                    options: [5, 10, 15, 30, 50, 500]
                  },
                  messages: {
                    noData: '<i>warning</i> No registered users to show.',
                    noDataAfterFiltering: '<i>warning</i> No registered users.'
                  },
                  responsive: true,
                }
            }
        },
        beforeMount: function(){
            Utils.getUserListByAdmin()
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
            QDataTable,TableRouterLink
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