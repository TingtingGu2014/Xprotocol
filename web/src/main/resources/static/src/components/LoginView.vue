<template id="login-template">
    <div>
        <div class="gt-md" v-if="inNavBar" style="float:right">
            <div class="row login-form" v-if="loggedIn">        
                <router-link :to="{ name: 'userProfile', params: { userUUID: userInfo.userUUID }  }" class="link-with-bg" >
                    <span class="fa fa-user"></span>    
                    <span v-if="userInfo.alias">
                        {{userInfo.alias}}
                    </span>
                    <span v-else>
                        {{userInfo.email}}
                    </span>
                </router-link>

                &nbsp;&nbsp;&nbsp;
                <a href="#" v-on:click="logoutsubmit" class="link-with-bg"><span class="fa fa-sign-out"></span>&nbsp;Sign Out</a> 
                &nbsp;&nbsp;&nbsp;
                <div style="order: 4" v-if="isAdminUser">
                    <router-link :to="{ name: 'admin' }" class="link-with-bg"><i class="fa fa-sun-o"></i>&nbsp;Administrator</router-link>
                </div>
            </div>
            <form class="row login-form" v-else>
                <input type="email" class="col" placeholder="email" v-model='emaillogin'>
                &nbsp;
                <input type="password" class="col" placeholder="password" v-model="passwordlogin">
                &nbsp;
                <a href="#" v-on:click="loginsubmit" class="col link-with-bg"><span class="fa fa-sign-in"></span>&nbsp;Sign In</a> 
                &nbsp;&nbsp;             
                <router-link :to="{ name: 'signUp'}" class="col link-with-bg"><span class="fa fa-user"></span>&nbsp;Sign Up</router-link>
            </form>
        </div>
        <div class="relative-position lt-xl" style="outline: none !important;width:100%;background-color: #2a7996; z-index: 10; border: 1px solid white;border-radius: 15px;" v-else>
            <q-list class="bottom" style="margin: auto; ">
                <q-collapsible icon="perm_identity" v-bind:label="getCurrentUserName"  @show="loginViewOpen" @hide="loginViewClose" v-if="loggedIn">
                  <div>
                    <form class="">        
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <router-link :to="{ name: 'userProfile', params: { userUUID: userInfo.userUUID }  }" >
                            <span class="fa fa-user"></span>    
                            <span>
                                &nbsp;&nbsp;Your Profile
                            </span>
                        </router-link>

                         &nbsp;&nbsp;
                        <a href="#" v-on:click="logoutsubmit"><span class="fa fa-sign-out"></span>&nbsp;Sign Out</a> 
                    </form>
                  </div>
                </q-collapsible>
                <div v-else>
                    <q-collapsible icon="fa-sign-in" label="Log In"  class="row" @show="loginViewOpen" @hide="loginViewClose">                  
                        <form class="">
                            <input type="email" class="collapsible-input" placeholder="email" v-model='emaillogin'>                       
                            <input type="password" class="collapsible-input" placeholder="password" v-model="passwordlogin">     
                            <div class="row" style="float:right">
                                <a href="#" class="link-with-bg singin-btn" v-on:click="loginsubmit" >&nbsp;Sign In</a> <br>
                            </div>
                        </form> 

                    </q-collapsible>

                    <q-collapsible icon="fa-user" label="Sign Up"   @show="collapseLinkClick('signup')" >
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<router-link :to="{ name: 'signUp'}"><span class="fa fa-user"></span>&nbsp;Sign Up</router-link>
                    </q-collapsible>
                </div>
                <q-collapsible icon="fa-sun-o" label="Admin"  @show="loginViewOpen" @hide="loginViewClose" v-if="isAdminUser">
                  <div>
                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<router-link :to="{ name: 'userList' }" style='font-family: Arial'><span class="fa fa-users"></span>&nbsp;User List</router-link>                    
                  </div>
                </q-collapsible>
                
                <q-collapsible icon="fa-home" label="Home"   @show="collapseLinkClick('home')" ref='homeLink'></q-collapsible>
            </q-list>                                                                                                                                                                                                                                                                                                                        
        </div>
    </div>
</template>     

<script>
    
    import { mapGetters, mapMutations } from 'vuex'    
    import { EventBus } from '../utils/EventBus.js';
            
    export default {
        
        props: {
            inNavBar: {
                type:Boolean,
                required:true
            },
        },
        data: function () {
            return {
                loggedIn: false,
                userInfo: null,
                emaillogin: '',
                passwordlogin: '',
                showCollapseList: false,
            }
        },     
        computed: {
            reloadUserInfo: function() {
                try{                    
                    if(!this.$utils.isEmpty(localStorage.userInfo)){
                        this.userInfo = JSON.parse(localStorage.userInfo)
                    }
                }
                catch(err) {
                    console.log(err.message)
                    localStorage.userInfo = ''
                }
            },
            getCurrentUserName(){
                return this.$utils.getUserName()
            },
            ...mapGetters({
                isUserDetailsFetched: 'userModule/isUserDetailsFetched',
                getUserDetails: 'userModule/getUserDetails',
            }),
            isAdminUser: function () {
                return this.$utils.isAdminUser();
            },            
        },
        components:{
            
        },
        methods: {
            loginsubmit: function (message, event) {
                
                if(this.$utils.isEmpty(this.emaillogin) || this.$utils.isEmpty(this.passwordlogin)){
                    this.$q.notify({message: `Please fill your email and password before sign in!`, color: 'negative'})
                    return;
                }
                
                if (event){
                    event.preventDefault()
                }
                
                this.$utils.signIn(this.emaillogin, this.passwordlogin)                
                .then((data) => {
                    if(data){
                        EventBus.$emit('session-change', 'signIn');
                        location.reload();
                    }                    
                })
                .catch((err) => {
                    this.$q.notify({message: `User sign in with error: `+err.message, color: 'negative'})
                    console.log(err)
                });
                
            },
            logoutsubmit: function (message, event) {
                if (event){
                    event.preventDefault()
                }
                
                this.$utils.signOut()
                .then((data) => {
                    if(data){                            
                        EventBus.$emit('session-change', 'signOut');
                        location.reload();
                    }                    
                })
                .catch((err) => {
                    this.$q.notify({message: `User sign out with error: `+err.message, color: 'negative'})
                    console.log(err)
                });
            },
            ...mapMutations({                
                setUserDetails: 'userModule/setUserDetails',
                setDetailsFetched: 'userModule/setDetailsFetched',
                setProtocols: 'protocolModule/setProtocols'
            }),
            collapseLinkClick: function(path){
                this.$emit('collapseLinkClick')                
                this.$router.push('/'+path)
//                return false
            },
            loginViewOpen(){
                console.log(this)
            },
            loginViewClose(){
            }
        },
        created: function(){
            let loggedIn = this.$utils.readCookie('loggedIn')
            this.loggedIn = loggedIn == 'true' ? true : false
            if(this.loggedIn !== true){
                localStorage.userInfo = null
                localStorage.protocolListCount = null
                this.userInfo = null
            }
            else {
                try{
                    if(!this.$utils.isEmpty(localStorage.userInfo)){
                        this.userInfo = JSON.parse(localStorage.userInfo)
                    }
                    else{
                        this.loggedIn = false
                        this.$utils.createCookie('loggedIn', false, 30)
                    }
                }
                catch(err) {
                    console.log(err.message)
                    localStorage.userInfo = ''
                }
            }
            this.showCollapseList = this.inNavBar
        },
    }
    
    function make_base_auth(user, password) {
        var tok = 'Basic ' + user + ':' + password;
        return tok;
    }
</script>

<style scoped>
.login-form {
    padding-bottom: 10px;
}

.q-list {
    border: none;
}

.singin-btn{
    margin: 10px 10px 0 0; 
    border: 1px solid white;
    padding: 5px;
    border-radius: 15px;
}

.collapsible-input{
    width: 90%;
}
</style>