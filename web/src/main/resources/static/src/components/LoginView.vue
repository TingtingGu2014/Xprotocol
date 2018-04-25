<template id="login-template">
    <div>
        <div class="gt-md" v-if="inNavBar">
            <form class="form-inline" v-if="loggedIn">        
                <router-link :to="{ name: 'userProfile', params: { userUUID: userInfo.userUUID }  }" >
                    <span class="fa fa-user"></span>    
                    <span v-if="userInfo.alias">
                        {{userInfo.alias}}
                    </span>
                    <span v-else>
                        {{userInfo.email}}
                    </span>
                </router-link>

                 &nbsp;&nbsp;
                <a href="#" v-on:click="logoutsubmit" class="link-with-bg"><span class="fa fa-sign-out"></span>&nbsp;Sign Out</a> 
            </form>
            <form class="form-inline" v-else>
                <input type="email" class="form-control " placeholder="email" v-model='emaillogin'>
                &nbsp;
                <input type="password" class="form-control " placeholder="password" v-model="passwordlogin">
                &nbsp;
                <a href="#" v-on:click="loginsubmit" class="link-with-bg"><span class="fa fa-sign-in"></span>&nbsp;Sign In</a> 
                &nbsp;&nbsp;             
                <router-link :to="{ name: 'signUp'}" class="link-with-bg"><span class="fa fa-user"></span>&nbsp;Sign Up</router-link>
            </form>
        </div>
        <div class="relative-position lt-lg" v-else>
            <q-list class="bottom" style="margin: auto;">
                <q-collapsible icon="perm_identity" v-bind:label="getCurrentUserName"  v-if="loggedIn">
                  <div>
                    <form class="form-inline">        
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
                <q-collapsible icon="fa-sign-in" label="Log In" v-else>
                  <div>
                    <form class="form-inline">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="email" class="form-control " placeholder="email" v-model='emaillogin'>
                        &nbsp;
                        <input type="password" class="form-control " placeholder="password" v-model="passwordlogin">
                        &nbsp;
                        <a href="#" v-on:click="loginsubmit">&nbsp;Sign In</a> <br>

                    </form> 
                  </div>
                </q-collapsible>

                <q-collapsible icon="fa-user" label="Sign Up">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<router-link :to="{ name: 'signUp'}"><span class="fa fa-user"></span>&nbsp;Sign Up</router-link>
                </q-collapsible>

                <q-collapsible icon="fa-sun-o" label="Admin" v-if="isAdminUser">
                  <div>
                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<router-link :to="{ name: 'userList' }" style='font-family: Arial'><span class="fa fa-users"></span>&nbsp;User List</router-link>                    
                  </div>
                </q-collapsible>
                
                <q-collapsible icon="fa-home" label="Home" @open="goHome" ref='homeLink'></q-collapsible>
            </q-list>                                                                                                                                                                                                                                                                                                                        
        </div>
    </div>
</template>     

<script>
    
    import { mapGetters, mapMutations } from 'vuex'    
//    import { EventBus } from '../utils/EventBus.js';
            
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
                    alert("Please fill your email and password before sign in.");
                    return;
                }
                
                if (event){
                    event.preventDefault()
                }
                
                this.$utils.signIn(this.emaillogin, this.passwordlogin)                
                .then((data) => {
                    if(data){
//                        EventBus.$emit('session-change', 'signIn');
                        location.reload();
                    }                    
                })
                .catch((err) => {
                    alert("oops, something happened during signing in!")
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
//                        EventBus.$emit('session-change', 'signOut');
                        location.reload();
                    }                    
                })
                .catch((err) => {
                    alert("oops, something happened during signing in!")
                    console.log(err)
                });
            },
            ...mapMutations({                
                setUserDetails: 'userModule/setUserDetails',
                setDetailsFetched: 'userModule/setDetailsFetched',
                setProtocols: 'protocolModule/setProtocols'
            }),
            goHome: function(event){
                let homeLink = this.$refs['homeLink']
                if(!this.$utils.isEmpty(homeLink)){
                    homeLink.close()
                }
                this.$router.push('/')
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
        },
    }
    
    function make_base_auth(user, password) {
        var tok = 'Basic ' + user + ':' + password;
        return tok;
    }
</script>

<style scoped>
    input.form-control {
        width: 100px !important;
    }
</style>