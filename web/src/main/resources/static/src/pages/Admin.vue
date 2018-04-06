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
                        <!--<a href="/userlist" class="btn btn- btn-lg" role="button"><i class="fa fa-users fa-5x"></i><span class=""></span><br><br>User List</a>-->
                        <router-link :to="{ name: 'userList' }" class="adminLink"><i class="fa fa-users ">&nbsp;<span class="adminLinkText">User List</span></i></span></router-link>                    
                    </div>
                </div>
                <div class="col-xs-6 col-md-6 col-lg-6 ">
                    <div class="">                                    
                    <a href="#" class="btn btn- btn-lg adminLink" role="button" ><i class="fa fa-user-md">&nbsp;<span class="adminLinkText">User Management</span></i></span></a>
                    </div>
                </div>                   
            </div>
        </div>
    </div>
</template>

<script>
    
    var loggedIn = !this.$utils.isEmpty(this.$utils.readCookie('loggedIn'))
    if(loggedIn != true){
        document.location.href = '/login'
    }
    
    var isAdminUser = this.$utils.isAdminUser();
    if(!isAdminUser){
        document.location.href = '/errors/403'
    }
                
    export default {  
        computed: {

        },
    }
</script>

<style>
.adminLink{
    font-size: 2.0vw;
}

.adminLinkText{        
    font-family: Arial;
    font-weight: bold;
}

.adminHeading{
    font-size: 3.0vw;
}
</style>
