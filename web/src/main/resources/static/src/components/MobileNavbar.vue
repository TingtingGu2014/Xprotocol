<template id="login-template">
    <div class="mobil-navbar">
        <div class="row navbar-body" style="padding-top: 9px; ">
            <ul class="nav">
                <li class="home">
                     <div class="">
                        <a href="#" v-on:click="toggleShowMenu" class="link-with-bg" style="font-size: 1.2em"><span class="fa fa-bars"></span> &nbsp;XProtocol</a>
                    </div>
                    
                    <div class="col-xs-12 col-sm-3 col-md-2 col-lg-1 gt-md move-right" style="order: 4" v-if="isAdminUser">
                        <router-link :to="{ name: 'admin' }" class="link-with-bg"><i class="fa fa-sun-o"></i>&nbsp;Admin</router-link>
                    </div>
                </li>
                <li class="search">
                    <div class="row search-row-md gt-sm">
                        <input type="text" name="search" v-model="search" class="search col-9" placeholder="Find Protocols...">
                        <label for="search" class="col-2" style="margin-top: 0px;">
                            <a href="#" v-on:click="searchProtocol" class="search-link">&nbsp;&nbsp;
                            <span class="fa fa-search" ></span></a>
                        </label> 
                    </div>
                    
                </li>
            </ul>
        </div>
        <div class="row menu-list">
            <login-view v-if="showMenuSheet" :inNavBar="false" @collapseLinkClick="closeShowMenu"></login-view>
        </div>
    </div>

</template>     

<script>
    import LoginView from 'components/LoginView.vue'
    export default {

        data: function () {
            return {
                search: '',
                showMenuSheet: false,
            }
        },     
        components: {
            LoginView,
        },
        watch: {

        },
        computed: {
            isAdminUser: function () {
                return this.$utils.isAdminUser();
            },
        },
        methods: {
            toggleShowMenu: function(){
                let self = this
                self.showMenuSheet = !self.showMenuSheet
                setTimeout(function(){
                    if(self.showMenuSheet === true){
                        self.showMenuSheet = false
                    }
                }, 30000)
            },  
            closeShowMenu: function(){
                this.showMenuSheet = false
            },
            searchProtocol: function(){
                this.search = this.search + 'ok'
            },
            
        },
        created: function(){
            
        }
    }
</script>

<style>

body.other-page {   
    background-color: #2d87a8;
}

@media screen and (max-width: 1024px){
    body.home-page {
        background-image: url("../statics/images/church.jpg");
    }
}

.navbar-body {
  margin: 0;
  padding: 0;

}
 
.nav ul {
  list-style: none;
  text-align: center;
  padding: 0;
  margin: 0;
}
.nav li {
  font-family: 'Oswald', sans-serif;
  font-size: 1.0em;
  line-height: 40px;
  height: 40px;
  list-style-type: none;
}
 
.nav a {
  text-decoration: none;
  color: #d5d9e0;
  display: block;
  transition: .3s background-color;
}
/* 
.nav a:hover {
  color: #03a326;
}*/

li.home {
    width: 120px;
    position: absolute;
    left: 1em;
}

li.search {
    position: absolute;
    left: 10em;
    text-align: left !important;
}

li.login-view {
    position: absolute;    
    right: 20px;
    width: 500px ;
}
 
@media screen and (min-width: 600px) {
  .nav li {
    border-bottom: none;
    height: 50px;
    line-height: 50px;
    font-size: 1.0em;
  }
 
  /* Option 1 - Display Inline */
  .nav li {
    display: inline-block;
    margin-right: -4px;
    border-bottom: none;
  }
 
  /* Options 2 - Float
  .nav li {
    float: left;
  }
  .nav ul {
    overflow: auto;
    width: 600px;
    margin: 0 auto;
  }
  .nav {
    background-color: #444;
  }
  */
}

li.search {
    width: 400px !important;
}

input.search {
    width: 80%;
    height: 35px;
    -webkit-transition: width 0.4s ease-in-out;
    transition: width 0.4s ease-in-out;
    background: none;
    border: 1.5px solid white;
    color: white;
    align: left;
    margin-top: .4em;
    margin-left: 20px;
    border-radius: 25px;
    padding: 0 20px 0 20px;
}

form input {
    width: 80%;
    height: 35px;
    -webkit-transition: width 0.4s ease-in-out;
    transition: width 0.4s ease-in-out;
    background: none;
    border: 1.5px solid white;
    color: white;
    align: left;
    margin-top: 7px;
    margin-left: 20px;
    border-radius: 25px;
    padding: 0 20px 0 20px;
}

div.menu-list {
    position: absolute;        
    text-align: left;
}

@media screen and (min-width: 834px){
    div.menu-list {
        left: 2%;
        margin-top: 4%;
    }
}

@media screen and (max-width: 833px){
    div.menu-list {
        left: 4%;
        margin-top: 7%;
    }
}

</style>