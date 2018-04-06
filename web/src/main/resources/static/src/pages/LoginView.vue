<template id="login-template">
    <div>
        <div class="gt-md" v-if="inNavBar">
            <form class="form-inline" v-if=loggedIn>        
                <!--<router-link to="/profile/userId">Go to notfound</router-link>-->
                <router-link :to="{ name: 'userProfile', params: { userUUID: userUUID }  }" >
                <!--<a href="#" v-on:click="getUserDetails">--> 
                    <span class="fa fa-user"></span>    
                    <span v-if="userAlias">
                        {{userAlias}}
                    </span>
                    <span v-else>
                        {{userEmail}}
                    </span>
                <!--</a>-->
                </router-link>

                 &nbsp;&nbsp;
                <a href="#" v-on:click="logoutsubmit"><span class="fa fa-sign-out"></span>&nbsp;Sign Out</a> 
            </form>
            <form class="form-inline" v-else>
                <input type="email" class="form-control " placeholder="email" v-model='emaillogin'>
                &nbsp;
                <input type="password" class="form-control " placeholder="password" v-model="passwordlogin">
                &nbsp;
                <a href="#" v-on:click="loginsubmit"><span class="fa fa-sign-in"></span>&nbsp;Sign In</a> 
                &nbsp;&nbsp;             
                <router-link :to="{ name: 'signUp'}"><span class="fa fa-user"></span>&nbsp;Sign Up</router-link>
            </form>
        </div>
        <div class="relative-position lt-lg" v-else>
            <q-list class="bottom" style="margin: auto;">
                <q-collapsible icon="perm_identity" v-bind:label="getCurrentUserName"  v-if="loggedIn">
                  <div>
                    <form class="form-inline">        
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <router-link :to="{ name: 'userProfile', params: { userUUID: userUUID }  }" >
                        <!--<a href="#" v-on:click="getUserDetails">--> 
                            <span class="fa fa-user"></span>    
                            <span>
                                &nbsp;&nbsp;Your Profile
                            </span>
                        <!--</a>-->
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
    import {
        QList,
        QCollapsible,
    } from 'quasar'
    
    import { mapGetters, mapMutations } from 'vuex'    
    import { EventBus } from '../utils/EventBus.js';
//    import * as Utils from '../utils/Utils.js'
    
    var userInfo = null
            
    export default {
        
        props: {
            inNavBar: {
                type:Boolean,
                required:true
            },
        },
        data: function () {
            return {
                loggedIn: loggedIn,
                userEmail: (this.$utils.isEmpty(userInfo) || this.$utils.isEmpty(userInfo.email)) ? '' : userInfo.email,
                userAlias: (this.$utils.isEmpty(userInfo) || this.$utils.isEmpty(userInfo.alias)) ? '' : userInfo.alias,
                userUUID: (this.$utils.isEmpty(userInfo) || this.$utils.isEmpty(userInfo.userUUID)) ? '' : userInfo.userUUID,
                emaillogin: '',
                passwordlogin: '',
            }
        },     
        computed: {
            reloadUserInfo: function() {
                try{
                    userInfo = JSON.parse(localStorage.userInfo)
                    if(!this.$utils.isEmpty(userInfo)){
                        this.userEmail = this.$utils.isEmpty(userInfo.email) ? '' : userInfo.email
                        this.userAlias = this.$utils.isEmpty(userInfo.alias) ? '' : userInfo.alias
                        this.userUUID = this.$utils.isEmpty(userInfo.userUUID) ? '' : userInfo.userUUID
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
            QCollapsible,QList,
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
                        EventBus.$emit('session-change', 'signIn');
                        document.location.href = '/home';
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
                        EventBus.$emit('session-change', 'signOut');
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
        beforeCreate: function(){
            this.loggedIn = this.$utils.isEmpty(this.$utils.readCookie('loggedIn'))
            userInfo = null
            if(this.loggedIn !== true){
                localStorage.userInfo = null
                localStorage.protocolListCount = null
            }
            else {
                try{
                    userInfo = JSON.parse(localStorage.userInfo)
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