<template id="sign-up-template">
    
    <div class="row text-center" style="width:80%; margin: auto; margin-top: 7%; margin-bottom: 30px; overflow-y: auto" ref="signUpDiv">        
        <form class="signUpForm" style="margin: auto;">
            <div class="row">
                <span style="margin-bottom: 15px;">Please sign in or register as a user to use XProtocol services.</span>
            </div>
            <div class="row" v-if="path.lastIndexOf('signup') >= 0">
                <div class="q-field-short col-lg-6 col-md-6 col-sm-12 col-xs-12">
                    <q-field label="Email:" >
                        <q-input type="email" v-model="email" placeholder="" />
                    </q-field>
                    <span v-if="email.length > 1">{{ email_message }}</span>
                </div>
                <div class="q-field-short col-lg-6 col-md-6 col-sm-12 col-xs-12">
                    <q-field label="Nick Name:" >
                        <q-input v-model="alias" />
                    </q-field>
                </div>
            </div>
            <div class="row" v-if="path.lastIndexOf('signin') >= 0 || path.lastIndexOf('login') >= 0">
                <div class="q-field-long col-lg-12 col-md-12 col-sm-12 col-xs-12">
                    <q-field label="Email:" >
                        <q-input type="email" v-model="email" placeholder="" />
                    </q-field>
                    <span v-if="email.length > 1">{{ email_message }}</span>
                </div>
            </div>
            <br>           
            <div class="row" v-if="path.lastIndexOf('signup') >= 0">
                <div class="q-field-short col-lg-6 col-md-6 col-sm-12 col-xs-12">
                    <q-field label="First Name:" >
                        <q-input v-model="firstName" />
                    </q-field>
                </div>
                <div class="q-field-short col-lg-6 col-md-6 col-sm-12 col-xs-12">
                    <q-field label="Last Name:" >
                        <q-input v-model="lastName" />
                    </q-field>
                </div>
            </div>
            <br>
            <div class="row">
                <div class="q-field-long col-lg-12 col-md-12 col-sm-12 col-xs-12">
                    <q-field label="Password:" v-if="path.lastIndexOf('signup') >= 0">
                        <vue-password v-model="password" class="" :user-inputs="[email]" placeholder="Password"></vue-password>
                    </q-field>
                    <q-field label="Password:" v-else>
                        <q-input type="password" class="" v-model="password" />
                    </q-field>
                </div>
            </div>
            <br>
            <div class="row" v-if="path.lastIndexOf('signup') >= 0">
                <div class="q-field-long col-lg-12 col-md-12 col-sm-12 col-xs-12">
                    <q-field label="Confirm Password:">
                        <q-input type="password" class="" v-model="password2" />
                    </q-field>
                </div>
            </div>
            <br>
            <div class="row" style="maring: auto">
                <div class="col text-center">
                    <br>
                    <q-btn  color="blue" small icon="fa-floppy-o" v-on:click.prevent="signupsubmit" style="margin-bottom: 15px; margin-right: 10px;">
                        <span v-if="path.lastIndexOf('signup') >= 0">Sign Up</span>
                        <span v-if="path.lastIndexOf('signin') >= 0 || path.lastIndexOf('login') >= 0">Sign In</span>
                    </q-btn>
                    <q-btn  color="blue" small icon="fa-home" @click.prevent="$router.push('/home')" style="margin-bottom: 15px; margin-right: 10px;">Cancel</q-btn>
                </div>
            </div>            
        </form>
    </div>
</template>

<script>
    import {
        QField,
        QInput,
        QBtn,
        Toast,
        Alert, 
    } from 'quasar'
    import { mapGetters, mapMutations } from 'vuex'
    import { EventBus } from '../utils/EventBus.js';
    
    var Utils = require('../utils/Utils')
    
    var loggedIn = !Utils.isEmpty(Utils.readCookie('loggedIn'))
    if(loggedIn == true){
        document.location.href = '/home'
    }
    
    import VuePassword from 'vue-password'
    export default {
        data: function() {
            return {
                email: '',
                firstName: '',
                lastName: '',
                alias: '',
                password: '',
                password2: '',
                path: '',
            }
        },
        computed: {
            ...mapGetters({
                isUserDetailsFetched: 'userModule/isUserDetailsFetched',
                getUserDetails: 'userModule/getUserDetails',
            })
        },
        components: {
            VuePassword, QField, QInput, QBtn, Toast,
        },
        watch: {
            email: function(value) {
                this.validate_email(value, 'email_message')
            },
            '$route' (to, from) {console.log(to.meta.signUpType + ' === ' + from)
                this.path = to.meta.signUpType
            },
        },
        mounted: function(){
            var url = window.location.toString().toLowerCase()
            if(!Utils.isEmpty(url)){
                var urlArr = url.split('/')
                for(var k = urlArr.length-1; k >= 0; k--){
                    if(urlArr[k].indexOf('signup') >= 0){
                        this.path = 'signup'
                        break
                    }
                    else if ((urlArr[k].indexOf('signin') >= 0) || (urlArr[k].indexOf('login') >= 0)){
                        this.path = 'login'
                        break
                    }
                }
            }
            var refs = this.$refs
            for(var name in refs){
                if(name === 'signUpDiv'){
                    var userProfileDiv = refs[name]
                    var shortFields = userProfileDiv.getElementsByClassName("q-field-short")
                    if(!Utils.isEmpty(shortFields)){
                        for(var i = 0; i < shortFields.length; i++){
                            var labels = shortFields[i].getElementsByClassName("q-field-label")
                            labels[0].classList.add("col-md-5");
                        }
                    }
                    
                    var longFields = userProfileDiv.getElementsByClassName("q-field-long")
                    if(!Utils.isEmpty(longFields)){
                        for(var i = 0; i < longFields.length; i++){
                            var labels = longFields[i].getElementsByClassName("q-field-label")
                            labels[0].classList.add("col-md-4");
                        }
                    }
                }
            }
        },
        methods: {
            validate_email (value, email_message) {
                if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(value)) {
                    this[email_message] = '';
                    return true;
                } else {
                    this[email_message] = 'Keep typing...waiting for a valid email';
                    return false;
                }
            },
            signupsubmit: function (message, event) {
            
                if(this.path.lastIndexOf('signup') >= 0){
                    if(Utils.isEmpty(this.email) || Utils.isEmpty(this.password) || Utils.isEmpty(this.password2)){
                        Alert.create({html: `Please fill your email and password for registration.`, duration: 3000})
                        return false;
                    }
                    if(this.password != this.password2){
                        Toast.create.negative({html: `The two passwords are different!`, duration: 3000})
                        return false;
                    }
                    if(Utils.isEmpty(this.alias) && (Utils.isEmpty(this.firstName) || Utils.isEmpty(this.lastName))) {
                        Toast.create.negative({html: `Your name and nick name cannot be empty at the same time.`, duration: 3000})
                        return false;
                    }
                }                

                if (event){
                    event.preventDefault()
                }
                
                var qs = require('qs');
                var url = '';
                if(this.path.lastIndexOf('signup') >= 0) {
                    Utils.signUp(qs.stringify(this.$data))
                    .then((data) => {
                        if(data){
                            EventBus.$emit('session-change', 'signIn');
                            this.setDetailsFetched(false)
                            document.location.href = '/home';
                        } 
                        else{
                            Alert.create({html: "Received invalid data after user signing up!", druation: 3000})
                        }
                    })
                    .catch((err) => {
                        Alert.create({html: "Cannot sing up new user! Error: "+err.message, druation: 3000})
                        console.log(err)
                    });
                    
                }
                else {
                    Utils.signIn(this.email, this.password)                
                    .then((data) => {
                        if(data){
                            EventBus.$emit('session-change', 'signIn');
                            this.setDetailsFetched(false)
                            document.location.href = '/home';
                        }                    
                    })
                    .catch((err) => {
                        alert("oops, something happened during signing in!")
                        console.log(err)
                    });
                  
                }
                                              
            },
            set_current_user: function() {
                alert('new user!');
            },
            ...mapMutations({                
                setUserDetails: 'userModule/setUserDetails',
                setDetailsFetched: 'userModule/setDetailsFetched',
            }),
        }
    }
</script>

<style>

div.q-field-short, div.q-field-long {
    padding-left: 25px;
    padding-right: 25px;
}

</style>