<template id="sign-up-template">
    <div class="row" style="width:60%; margin: auto; margin-top: 7%; " ref="userProfileDiv" id="userProfileDiv">
    
        <form class="col" enctype="multipart/form-data" v-bind:action="'/api/userProfile/'+userUUID" 
        method="post" name="userProfileInfo" autocomplete="off">
            <div class="row">
                <h6 class="mb-0">Your Profile:</h6>
            </div>
            <div class="row form-row">
                <div class="q-field-short col-lg-6 col-md-6 col-sm-12 col-xs-12">
                    <q-field label="First Name" >
                        <q-input v-model="firstName" />
                    </q-field>
                </div>
                <div class="q-field-short col-lg-6 col-md-6 col-sm-12 col-xs-12">
                    <q-field label="Last Name" >
                        <q-input v-model="lastName" />
                    </q-field>
                </div>
            </div>

            <div class="row form-row">
                <div class="q-field-short col-lg-6 col-md-6 col-sm-12 col-xs-12">
                    <q-field label="Alias" >
                        <q-input v-model="alias" />
                    </q-field>
                </div>
                <div class="q-field-short col-lg-6 col-md-6 col-sm-12 col-xs-12">
                    <q-field label="Email" >
                        <q-input type="email" v-model="email" />
                    </q-field>
                </div>
            </div>

            <div class="row form-row">
                <div class="q-field-short col-lg-6 col-md-6 col-sm-12 col-xs-12">
                    <q-field label="Affiliation" >
                        <q-input v-model="affiliation" />
                    </q-field>
                </div>
                <div class="q-field-short col-lg-6 col-md-6 col-sm-12 col-xs-12">
                    <q-field label="Major" >
                        <q-input v-model="major" />
                    </q-field>
                </div>
            </div>

            <div class="row form-row">
                <div class="q-field-long col-lg-12 col-md-12 col-sm-12 col-xs-12">
                    <q-field label="Street Address" >
                        <q-input v-model="address" />
                    </q-field>
                </div>
                <div class="q-field-short col-lg-4 col-md-4 col-sm-12 col-xs-12">
                    <q-field label="City" >
                        <q-input v-model="city" />
                    </q-field>
                </div>
                <div class="q-field-short col-lg-4 col-md-4 col-sm-12 col-xs-12">
                    <q-field label="State" >
                        <q-input v-model="state" />
                    </q-field>
                </div>
                <div class="q-field-short col-lg-4 col-md-4 col-sm-12 col-xs-12">
                    <q-field label="Zip Code" >
                        <q-input v-model="zipcode" />
                    </q-field>
                </div>
            </div>
            
            <div class="row form-row" style="margin-top: 5px">
                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                    <q-field label="User Authority Roles" class="col-12 q-field-short">
                        <input type="hidden" name="roles" v-model="roles"/>
                        <div style='margin-left:20px' class="text-left">
                            <q-btn rounded outline size="sm" v-if="role != ''" v-for="(role, index) in roles.split(',')" :key="index" @click.prevent="doNothing" style="margin-right:10px; margin-bottom: 10px;color: #90EE90">
                                {{index+1}}.&nbsp;{{role}}
                            </q-btn>  
                        </div>
                    </q-field>
                </div>
            </div>
            
            <div class="row form-row">
                <div class="col text-center">
                    <br>
                    <q-btn  color="blue" small icon="fa-floppy-o" v-on:click.prevent="onSubmit" style="margin-bottom: 15px; margin-right: 10px;">&nbsp;Save</q-btn>
                    <q-btn  color="positive" small icon="fa-home" @click.prevent="backHome" style="margin-bottom: 15px; margin-right: 10px;">&nbsp;Back to Home</q-btn>
                </div>
            </div>
        </form>
    </div>
</template>

<script>

    import { mapGetters, mapMutations } from 'vuex'
            
    export default {
        data: function() {
            return {
                email: '',
                alias: '',
                userUUID: '',
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
            }
        },
        computed: {
            ...mapGetters({                
                isUserDetailsFetched: 'userModule/isUserDetailsFetched',                
                getUserDetails: 'userModule/getUserDetails',
            })
        },
        components: {
            
        },
        created: function() {           
            
            var loggedIn = localStorage.loggedIn == "true"
            if(loggedIn === false){
                document.location.href = '/login';
            }
            else{
                var userUUID = ''
                try {
                    var userInfo = JSON.parse(localStorage.userInfo)
                    userUUID = userInfo.userUUID
                }
                catch(err) {
                    console.log(err.message)
                }
                var detailsFetched = this.isUserDetailsFetched
                if(!detailsFetched == true){
                    this.$userUtils.getUserDetails(userInfo.userUUID)                
                    .then((data) => {
                        console.log(data)
                        if(data){
                            this.resetUserProfile(data)
                        }                    
                    })
                    .catch((err) => {
                        this.$q.notify({message: `Something is wrong in pulling user profile. Status: `+err.message, color: 'negative', duration: 3000})
                        console.log(err)
                    });
                    
                }
                else{                    
                    if(detailsFetched == true){
                        try{
                            var user = JSON.parse(localStorage.userInfo)
                        }
                        catch(err) {
                            console.log(err.message)
                        }
                        this.email = user.email
                        this.alias = user.alias
                        this.userUUID = user.userUUID
                        this.createdDate = user.createdDate
                        this.firstName = user.firstName
                        this.lastName = user.lastName    
                        this.roles = user.roles
                    }
                    var details = this.getUserDetails
                    this.major = details.major
                    this.address = details.address
                    this.affiliation = details.affiliation
                    this.city = details.city
                    this.state = details.state
                    this.zipcode = details.zipcode   
                }
            }
            
        },
        beforeCreate: function(){
            var loggedIn = localStorage.loggedIn == "true"
            if(loggedIn != true){
                this.$router.push('login')
                return false
            }
            var currentUrl = window.location.href
            if(currentUrl.indexOf('admin/') >=0) {
                var isAdminUser = this.$utils.isAdminUser();
                if(!isAdminUser){
                    document.location.href = '/errors/403'
                }
            }
        },
        mounted: function(){
            var refs = this.$refs
            for(var name in refs){
                if(name === 'userProfileDiv'){
                    var userProfileDiv = refs[name]
                    var shortFields = userProfileDiv.getElementsByClassName("q-field-short")
                    if(!this.$utils.isEmpty(shortFields)){
                        for(var i = 0; i < shortFields.length; i++){
                            var labels = shortFields[i].getElementsByClassName("q-field-label")
                            labels[0].classList.add("col-md-3");
                        }
                    }
                    
                    var longFields = userProfileDiv.getElementsByClassName("q-field-long")
                    if(!this.$utils.isEmpty(longFields)){
                        for(var i = 0; i < longFields.length; i++){
                            var labels = longFields[i].getElementsByClassName("q-field-label")
                            labels[0].classList.add("col-md-2");
                        }
                    }
                }
            }
        },
        watch: {
        },
        methods: {
            onSubmit: function (message, event) {
                
                var qs = require('qs');
                var userInfo = null
                try{
                    userInfo = JSON.parse(localStorage.userInfo)
                }
                catch(err) {
                    console.log(err.message)
                }
                
                this.$userUtils.updateUserDetails(userInfo.userUUID, qs.stringify(this.$data))                
                .then((data) => {
                    if(data){
                        this.resetUserProfile(data)
                    }                    
                })
                .catch((err) => {
                    this.$q.notify({message: `Something is wrong in saving user profile. Status: `+err.message, color: 'negative', duration: 3000})
                    console.log(err)
                });
                    
            },           
            resetUserProfile: function (data){
                var details = data.userDetails
                var user = data.user
                this.setUserDetails(details)
                if(!this.$utils.isEmpty(details)){                    
                    this.major = details.major
                    this.address = details.address
                    this.affiliation = details.affiliation
                    this.city = details.city
                    this.state = details.state
                    this.zipcode = details.zipcode
                    this.setDetailsFetched(true)
                }  
                if(this.$utils.isEmpty(user)) {
                    try{
                        user = JSON.parse(localStorage.userInfo)
                    }
                    catch(err) {
                        console.log(err.message)
                    }
                }
                if(!this.$utils.isEmpty(user)){
                    this.email = user.email
                    this.alias = user.alias
                    this.userUUID = user.userUUID
                    this.createdDate = user.createdDate
                    this.firstName = user.firstName
                    this.lastName = user.lastName  
                    this.roles = user.roles
                    localStorage.userInfo = JSON.stringify(user)
                }
            },
            ...mapMutations({                                
                setUserDetails: 'userModule/setUserDetails',
                setDetailsFetched: 'userModule/setDetailsFetched',                
            }),
            backHome: function(){
                this.$router.push('/')
            },
            doNothing: function(){
                return false
            }
        }
    }
</script>

<style scoped>
div.q-field-short, div.q-field-long {
    padding-left: 25px;
    padding-right: 25px;
}

</style>