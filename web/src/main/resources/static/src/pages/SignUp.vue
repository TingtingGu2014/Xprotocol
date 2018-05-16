<template id="sign-up-template">
    
    <div class=" " style="width: 80%; margin: auto; margin-top: 7%; margin-bottom: 30px; overflow-y: auto" ref="signUpDiv">        
        <form class="col-12" style="">
            <br>
            <div class="row signup-form-heading" style="" v-if="isSignUpPath">Please register to use more XProtocol services:</div>
            <div class="row signup-form-heading" style="" v-else>Please sign in to use more XProtocol services:</div>
            <br>
            <div class="row input-row" v-if="isSignUpPath">
                <q-field
                    class="col-md"
                  :label-width="3"
                  label="Email:"
                  helper="A valid email address to contact"
                      :error="$v.userEmail.$error"
                      @blur="$v.userEmail.$touch"
                >
                    <q-input v-model="userEmail" color="white" type="email" suffix="" />
                </q-field>
                <br>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <q-field
                    class="col-md"
                    :label-width="3"
                    label="Nick Name:"
                    helper="Please give yourself a easily to remember nick name"
                >
                    <q-input dark color="white" v-model="alias" />
                </q-field>
            </div>
            <div class="row input-row" v-else>
                <q-field
                    class="col-md"
                  :label-width="3"
                  label="Email:"
                  helper="A valid email address to contact"
                      :error="$v.userEmail.$error"
                      @blur="$v.userEmail.$touch"
                >
                    <q-input v-model="userEmail" color="white" type="email" suffix="" />
                </q-field>
            </div>
            <br>           
            <div class="row input-row" v-if="isSignUpPath">
            
                <q-field
                    class="col-md"
                  :label-width="3"
                  label="First Name:"
                  helper=""
                >
                    <q-input dark color="white" v-model="firstName" />
                </q-field>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <q-field
                    class="col-md"
                    :label-width="3"
                    label="Last Name:"
                    helper=""
                >
                    <q-input dark color="white" v-model="lastName" />
                </q-field>
            </div>
            <br>
            <div class="input-row">
            
                <div class="row" v-if="isSignUpPath">
                    <q-field
                        class="col-md"
                      :label-width="3"
                      label="Password:"    
                      helper="Has to be at least 6 characters"
                      :error="$v.userPassword.$error"
                      @blur="$v.userPassword.$touch"
                    >
                        <q-input dark color="white" type="password" v-model="userPassword" />
                    </q-field>
                    
                    <q-field
                        class="col-md"
                      :label-width="3"
                      label="Confirm Password:"
                      >
                        <q-input dark color="white" type="password" v-model="userPassword2" />
                    </q-field>
                </div>
<!--                    <q-field label="Password:" v-if="path.lastIndexOf('signup') >= 0">
                        <vue-password v-model="password" class="" :user-inputs="[email]" placeholder="Password"></vue-password>
                    </q-field>-->
                    
                <div class="row" v-else>
                    <q-field
                        class="col-md"
                      :label-width="3"
                      label="Password:"    
                      helper="Has to be at least 6 characters"
                      :error="$v.userPassword.$error"
                      @blur="$v.userPassword.$touch"
                    >
                        <q-input dark color="white" type="password" v-model="userPassword" />
                    </q-field>
                
                </div>

            </div>
            <br>
            <div class="row" style="maring: auto">
                <div class="col text-center">
                    <br>
                    <q-btn  color="blue" small icon="fa-floppy-o" v-on:click.prevent="signupsubmit" style="margin-bottom: 15px; margin-right: 10px;">
                        <span v-if="isSignUpPath">&nbsp;Sign Up</span>
                        <span v-else>&nbsp;Sign In</span>
                    </q-btn>
                    <q-btn  color="blue" small icon="fa-home" @click.prevent="$router.push('/home')" style="margin-bottom: 15px; margin-right: 10px;">&nbsp;Home</q-btn>
                </div>
            </div>            
        </form>
    </div>
</template>

<script>

    import { mapGetters, mapMutations } from 'vuex'    
    import { EventBus } from '../utils/EventBus.js';
    import { required, email, sameAs, minLength } from 'vuelidate/lib/validators'
    
    export default {
        data: function() {
            return {
                userEmail: '',
                firstName: '',
                lastName: '',
                alias: '',
                userPassword: '',
                userPassword2: '',
                path: '',
            }
        },
        computed: {
            isSignUpPath: function(){
                return (!this.$utils.isEmpty(this.path) && this.path.lastIndexOf('signup') >= 0)
            },
            ...mapGetters({
                isUserDetailsFetched: 'userModule/isUserDetailsFetched',
                getUserDetails: 'userModule/getUserDetails',
            })
        },
        components: {
            
        },
        validations: {
            userEmail: {
                required, email
            },
            userPassword: {
                required,
                minLength: minLength(3)
            },
            userPassword2: {
                required,
                sameAsPassword: sameAs('userPassword')
            },
            
        },
        watch: {
            userEmail: function(value) {
                this.validate_email(value, 'email_message')
            },
            '$route' (to, from) {
                this.path = to.meta.signUpType
            },
        },
        beforeCreate: function(){
            var loggedIn = !this.$utils.isEmpty(this.$utils.readCookie('loggedIn'))
            if(loggedIn == true){
                this.$router.push('/home')
            }
        },
        mounted: function(){
            var url = window.location.toString().toLowerCase()
            if(!this.$utils.isEmpty(url)){
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
            
                this.$v.userEmail.$touch()
                
                if (this.$v.userEmail.$error) {
                    this.$q.notify('This is NOT a valid email.')
                    return false
                }
                
                this.$v.userPassword.$touch()
                
                if (this.$v.userPassword.$error) {
                    this.$q.notify('This is NOT a valid password.')
                    return false
                }
                
                if(this.isSignUpPath === true){
                    this.$v.userPassword2.$touch()

                    if (this.$v.userPassword2.$error) {
                        this.$q.notify('This is NOT a valid confirmation password.')
                        return false
                    }
                    
                    if(this.$utils.isEmpty(this.alias) && (this.$utils.isEmpty(this.firstName) || this.$utils.isEmpty(this.lastName))) {
                        this.$q.notify({message: `Your name and nick name cannot be empty at the same time!`, timeout: 3000, color: 'negative'})
                        return false;
                    }
                }              

                if (event){
                    event.preventDefault()
                }
                
                var qs = require('qs');
                var url = '';
                if(this.path.lastIndexOf('signup') >= 0) {
                    this.$utils.signUp(qs.stringify(this.$data))
                    .then((data) => {
                        if(data){
                            EventBus.$emit('session-change', 'signIn');
                            this.setDetailsFetched(false)
                            this.$router.push('/home')
                        } 
                        else{
                            this.$q.notify({message: `Received invalid data after user signing up!`, timeout: 3000, color: 'negative'})
                        }
                    })
                    .catch((err) => {
                        this.$q.notify({message: 'Cannot sing up new user! Error: '+err.message, timeout: 3000, color: 'negative'})
                        console.log(err)
                    });
                    
                }
                else {
                    this.$utils.signIn(this.userEmail, this.userPassword)                
                    .then((data) => {
                        if(data){
                            EventBus.$emit('session-change', 'signIn');
                            this.setDetailsFetched(false)
                            this.$router.push('/home')
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

div.signup-form-heading {
    text-align: left; color: #d5d9e0; font-weight: bold; font-size: 1.2em;
}

div.input-row{
    margin-top: 2em;
}

@media screen and (min-width: 1200px){
    q-field{
        margin-right: 1em;
    }
}

@media screen and (max-width: 1199px){
    
}

</style>