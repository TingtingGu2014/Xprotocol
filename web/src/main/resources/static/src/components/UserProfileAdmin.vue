<template id="sign-up-template">
    <div class="row" style="width:60%; margin: auto; margin-top: 7%; " ref="userProfileAdminDiv" id="userProfileDiv">
    
        <form class="col" enctype="multipart/form-data" v-bind:action="'/api/admin/userProfile/'+userUUID" 
        method="post" name="userProfileInfo" autocomplete="off">
            <div class="row">
                <h6 class="mb-0">User Profile:</h6>
            </div>
            <div class="row">
                <div class="q-field-short col-lg-6 col-md-12 col-sm-12 col-xs-12">
                    <q-field label="First Name" >
                        <q-input v-model="firstName" />
                    </q-field>
                </div>
                <div class="q-field-short col-lg-6 col-md-12 col-sm-12 col-xs-12">
                    <q-field label="Last Name" >
                        <q-input v-model="lastName" />
                    </q-field>
                </div>
            </div>

            <div class="row">
                <div class="q-field-short col-lg-6 col-md-12 col-sm-12 col-xs-12">
                    <q-field label="Alias" >
                        <q-input v-model="alias" />
                    </q-field>
                </div>
                <div class="q-field-short col-lg-6 col-md-12 col-sm-12 col-xs-12">
                    <q-field label="Email" >
                        <q-input type="email" v-model="email" />
                    </q-field>
                </div>
            </div>

            <div class="row">
                <div class="q-field-short col-lg-6 col-md-12 col-sm-12 col-xs-12">
                    <q-field label="Affiliation" >
                        <q-input v-model="affiliation" />
                    </q-field>
                </div>
                <div class="q-field-short col-lg-6 col-md-12 col-sm-12 col-xs-12">
                    <q-field label="Major" >
                        <q-input v-model="major" />
                    </q-field>
                </div>
            </div>

            <div class="row">
                <div class="q-field-long col-lg-12 col-md-12 col-sm-12 col-xs-12">
                    <q-field label="Street Address" >
                        <q-input v-model="address" />
                    </q-field>
                </div>
                <div class="q-field-short col-lg-4 col-md-12 col-sm-12 col-xs-12">
                    <q-field label="City" >
                        <q-input v-model="city" />
                    </q-field>
                </div>
                <div class="q-field-short col-lg-4 col-md-12 col-sm-12 col-xs-12">
                    <q-field label="State" >
                        <q-input v-model="state" />
                    </q-field>
                </div>
                <div class="q-field-short col-lg-4 col-md-12 col-sm-12 col-xs-12">
                    <q-field label="Zip Code" >
                        <q-input v-model="zipcode" />
                    </q-field>
                </div>
            </div>
            
            <div class="row ">  
                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                    <input type="hidden" name="roles" v-model="roles"/>
                    <q-field label="User Authority Roles" class="col-12 q-field-short" >                    
                        <label  class="col btn label-user-role col-lg-3 float-left" v-for="(role, index) in roles.split(',')" v-if="role != ''" >
                            {{index+1}}.&nbsp;{{role}}
                            <a v-bind:id="role" href="#" class="trash-can" v-on:click.prevent="removeRole">
                                <i class="fa fa-trash" aria-hidden="true" v-if="role != 'regular' "></i>                                    
                            </a>&nbsp;&nbsp;                                
                        </label>                            
                    </q-field>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-6 col-sm-12 text-center">
                    <q-field  class="label-user-role" >
                        <a href="#" class="trash-can" v-on:click.prevent="restoreRoles">
                            Restore roles&nbsp;&nbsp;&nbsp;<i class="fa fa-undo" aria-hidden="true"></i>                                    
                        </a>
                    </q-field>  
                </div>
                <div class="col-md-6 col-sm-12 text-center">
                    <q-field  class="btn label-user-role" >
                        <a href="#" class="trash-can" v-on:click.prevent="displayRoles">
                            Add other roles&nbsp;&nbsp;&nbsp;<i class="fa fa-user-plus" aria-hidden="true"></i>                                   
                        </a>
                    </q-field>
                </div>
            </div>
            <div class="row other-roles-row text-center" style="display:none" v-if="otherRoles != ''">
                <label class="col col-lg-3 col-form-label form-control-label primary">Roles to add</label>
                <div class="col  col-lg-9" role="group" aria-label="...">
                    <label  class="col btn label-user-role col-lg-3 " v-for="(role, index) in otherRoles.split(',')" v-if="role != ''">
                        {{index+1}}.&nbsp;{{role}}
                        <a v-bind:id="role" href="#" class="trash-can" v-on:click.prevent="removeOtherRole">
                            <i class="fa fa-plus-square" aria-hidden="true" v-if="role != 'regular' "></i>                                    
                        </a>&nbsp;&nbsp;                                
                    </label>                            
                </div>
            </div>
            <div class="row other-roles-row text-center" style="display:none" v-else>
                <label class="col col-lg-3 col-form-label form-control-label primary">No roles to be added</label>
            </div>
            
            <div class="row">
                <div class="col text-center">
                    <br>
                    <q-btn  color="blue" small icon="fa-floppy-o" v-on:click.prevent="onSubmit" style="margin-bottom: 15px; margin-right: 10px;">Save</q-btn>
                    <q-btn  color="positive" small icon="fa-home" @click.prevent="backHome" style="margin-bottom: 15px; margin-right: 10px;">Back to Home</q-btn>
                </div>
            </div>
        </form>
    </div>
</template>

<script>
    import axios from 'axios'
    import { mapGetters, mapMutations } from 'vuex'
    import {
        QField,
        QInput,
        QBtn,
        Toast,
    } from 'quasar'
    
    var Utils = require('../utils/Utils')
    
    var loggedIn = !Utils.isEmpty(Utils.readCookie('loggedIn'))
    if(loggedIn != true){
        document.location.href = '/login'
    }
    
    var currentUrl = window.location.href
    if(currentUrl.indexOf('admin/') >=0) {
        var isAdminUser = Utils.isAdminUser();
        if(!isAdminUser){
            document.location.href = '/errors/403'
        }
    }
            
    export default {
        data: function() {
            return {
                email: '',
                alias: '',
                userUUID: this.$route.params.userUUID,
                createdDate: '',
                firstName: '',
                lastName: '',
                major: '',
                address: '',
                affiliation: '',
                state: '',
                city: '',
                zipcode: '',
                roles: '',
                oldRoles: '',
                allRoles: '',
            }
        },
        computed: {
            otherRoles: function(){
                var allRoles = this.allRoles
                if(!Utils.isEmpty(allRoles)){
                    var roles = this.roles
                    var diff = Utils.getArraydifferences(allRoles.split(','), roles.split(','))
                    return diff.toString()
                }
                else{
                    return '';
                }
            },            
        },
        components: {
            QBtn, QField, QInput, Toast,
        },
        created: function() {
//            var data = {"user":{"userId":1,"firstName":"Sooner","lastName":"Zhao","email":"tao.zhao@xprotocol.com","password":"123","alias":"soonerZhao","userUUID":"5ce824f0-bdfe-11e7-9696-0b2512d9785a","roles":"anonymous,admin,regular","createdDate":"2017-04-25","active":true},"userDetails":{"userDetailsId":1,"userId":1,"address":"2202 Houston Ave","city":"norman","state":"OK","zipcode":"73072","major":"biology","affiliation":"University of Oklahoma"}};
            var loggedIn = !Utils.isEmpty(Utils.readCookie('loggedIn'));
            if(loggedIn === false){
                document.location.href = '/login';
            }
            else{
                var userUUID = this.userUUID
                Utils.getUserDetails(userUUID, true)
                .then((data) => {
                    console.log(data)
                    if(data){
                        this.resetUserProfileAdmin(data)
                    }         
                    else{
                        Toast.create.negative({html: `Something is wrong in pulling user profile. Status: `+response.status, duration: 3000})
                        return;
                    } 
                })
                .catch((err) => {
                    Alert.create({html: 'oops, something happened during pulling out user profile!'})
                    console.log(err)
                });
                
            }
        },
        mounted: function(){
            var refs = this.$refs
            for(var name in refs){
                if(name === 'userProfileAdminDiv'){
                    var userProfileDiv = refs[name]
                    var shortFields = userProfileDiv.getElementsByClassName("q-field-short")
                    if(!Utils.isEmpty(shortFields)){
                        for(var i = 0; i < shortFields.length; i++){
                            var labels = shortFields[i].getElementsByClassName("q-field-label")
                            labels[0].classList.add("col-md-3");
                        }
                    }
                    
                    var longFields = userProfileDiv.getElementsByClassName("q-field-long")
                    if(!Utils.isEmpty(longFields)){
                        for(var i = 0; i < longFields.length; i++){
                            var labels = longFields[i].getElementsByClassName("q-field-label")
                            labels[0].classList.add("col-md-2");
                        }
                    }
                }
            }
        },
        methods: {
            onSubmit: function (event) {
                var qs = require('qs');
                var userUUID = this.userUUID

                Utils.updateUserDetails(userUUID, qs.stringify(this.$data), true)
                .then( (response) => {
                    if(response.status === 200){                        
                        var data = response.data
                        this.resetUserProfileAdmin(data)
                        Toast.create.positive({html: `User profile has been saved.`, duration: 3000})
                    }
                            
                })
                .catch(function (error) {
                    Toast.create.negative({html: `Something is wrong in pulling user profile. Status: `+response.status, duration: 3000})
                    console.log(error);
                });
            },           
            resetUserProfileAdmin: function (data){
                var details = data.userDetails
                var user = data.user
                if(!Utils.isEmpty(user)){
                    this.email = user.email
                    this.alias = user.alias
                    this.userUUID = user.userUUID
                    this.createdDate = user.createdDate
                    this.firstName = user.firstName
                    this.lastName = user.lastName  
                    this.roles = user.roles
                    this.oldRoles = user.roles
                }
                else{
                    //throw new exception here
                }
                if(!Utils.isEmpty(details)){                    
                    this.major = details.major
                    this.address = details.address
                    this.affiliation = details.affiliation
                    this.city = details.city
                    this.state = details.state
                    this.zipcode = details.zipcode
                }                  
            },
            removeRole : function (event) {
                if (event){
                    event.preventDefault()
                }
                var target = event.currentTarget
                var role = target.id
                         
                var roles = this.roles
                var rolesArr = roles.split(',')
                var index = rolesArr.indexOf(role)
 
                if (index > -1) {
                   rolesArr.splice(index, 1);
                }
                
                this.roles = rolesArr.toString()
            },
            removeOtherRole: function (event) {
                if (event){
                    event.preventDefault()
                }
                var target = event.currentTarget
                var role = target.id
                
                var currentRoles = this.roles
                currentRoles += ',' + role
                this.roles = currentRoles
            },
            restoreRoles: function (event) {
                this.roles = this.oldRoles
            },
            displayRoles: function (event) {
                this.allRoles = 'protocol editor,anonymous,admin,regular'
                var refs = this.$refs
                for(var name in refs){
                    if(name === 'userProfileAdminDiv'){
                        var userProfileDiv = refs[name]
                        var otherRolesRow = userProfileDiv.getElementsByClassName("other-roles-row")
                        otherRolesRow[0].style.display = 'block'
                    }
                }
            },
            backHome: function(){
                this.$router.push('/')
            },
        }
    }
</script>

<style scoped>
div.q-field-short, div.q-field-long {
    padding-left: 25px;
    padding-right: 25px;
}
.label-user-role {
    margin-right: 25px;
}


</style>