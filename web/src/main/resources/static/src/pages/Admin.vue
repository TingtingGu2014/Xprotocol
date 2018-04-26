<template id="nav-template">
    <div class="row text-center" style="width:60%; margin: auto; margin-top: 7%; " ref="" id="">
        <div style="width: 100%">
            <div class=" text-center">
                <span class="adminHeading">Administrator Management Tools</span>
                <br><br>
            </div>
            <div class="row text-left">
                <div class="col-xs-6 col-md-6 col-lg-6 ">
                    <div class="">                                                    
                        <router-link :to="{ name: 'userList' }" class="big-icon-link"><i class="fa fa-users ">&nbsp;<span class="">User List</span></i></span></router-link>                    
                    </div>
                </div>
                <div class="col-xs-6 col-md-6 col-lg-6 ">
                    <div class="">                                    
                    <a href="#" class="big-icon-link" role="button" ><i class="fa fa-user-md">&nbsp;<span class="">User Management</span></i></span></a>
                    </div>
                </div>                   
            </div>
        </div>
    </div>
</template>

<script>
                
    export default {  
        computed: {

        },
        beforeCreate: function(){
            var loggedIn = !this.$utils.isEmpty(this.$utils.readCookie('loggedIn'))
            loggedIn = true
            if(loggedIn != true){
                this.$router.push({path: '/login'})
            }

            var isAdminUser = this.$utils.isAdminUser();
            isAdminUser = true
            if(!isAdminUser){
                this.$router.push({path: '/errors/403'})
            }
        }
    }
</script>

<style>

.adminHeading{
    font-size: 3.0vw;
}
</style>
