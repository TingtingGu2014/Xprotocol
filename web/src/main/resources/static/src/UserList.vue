<template id="sign-up-template">
    
    <div class="container"><br><br>
    <h3>List of Users</h3>
        <b-table :fields="fields" :items="tableData">            
            <template slot="index" scope="data">
              {{data.index + 1}}
            </template>
            
            <template slot="edit" scope="data">
                <RouterLink :linkData = "data.value"></RouterLink>                
            </template>

        </b-table>
    </div>
</template>

<script>
    
    import RouterLink from './vuetablecomponents/RouterLink.vue'
    
    var Utils = require('./Utils')
    
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
                fields: [
                    {key: 'firstName', label: 'First Name'},
                    {key: 'lastName', label: 'Last Name'},
                    {key: 'email', label: 'Email'},
                    {key: 'userUUID', label: 'UUID'},
                    {key: 'createdDate', label: 'Registeration Date', dataType: 'date:DD/MM/YYYY'},
                    {key: 'active', label: 'Active', dataType: 'boolean'},
                    'edit',
                ],          
            }
        },
        beforeMount: function(){
            axios({
                method: 'get',
                url: '/api/admin/users',
                dataType: 'json',
                headers: {'X-Requested-With': 'XMLHttpRequest'},
            })
            .then( (response) => {
                var status = response.status;
                var data = response.data
                if(status == 200 || status == "200"){
                    console.log(data)
                    for(var i in data) {
                        var row = data[i];
                        var editData = {}
                        editData.name = 'userProfileAdmin'
                        editData.params = {}
                        editData.params.userUUID = row.userUUID
                        editData.label = '<span class=\"fa fa-pencil\" aria-hidden=\"true\" ></span>'
                        row['edit'] = editData
//                        row["edit"] = "<a href=\"/admin/userProfile/" + row.userUUID + "\"><span class=\"fa fa-pencil\" aria-hidden=\"true\" ></span></a>";
                    }
                    this.tableData = data
                }
                else{
                    alert("not 200");
                }                                   
            })
            .catch( (error) => {
                console.log(error);
            });  
        },
        components: {
            RouterLink,
        },
    }
</script>


</style>