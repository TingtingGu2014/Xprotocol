<template id="login-template">
    <div>
        <div class="row navbar-body" style="padding-top: 9px; ">
            <div class="nav">
                <ul>
                    <li class="home">
                        <router-link :to="{ name: 'home'}"><span class="fa fa-home"></span> &nbsp;XProtocol!</router-link>
                    </li>
                    <li class="search">
                        <div class="row">
                            <input type="text" name="search" class="search col-9" style="margin-top: 7px;" placeholder="Search Protocols...">
                            <label for="search" class="col-2" style="margin-top: 2px;">
                                <a href="#" v-on:click="searchProtocol" class="search-link">&nbsp;&nbsp;
                                <span class="fa fa-search" ></span></a>
                            </label> 
                        </div>
                    </li>
                    <li class="login-view">
                        <login-view :inNavBar="true"></login-view>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>     

<script>
    import LoginView from 'components/LoginView.vue'
    export default {

        data: function () {
            return {
                search: '',
                isHomePage: true,
                body: null,
            }
        },     
        components: {
            LoginView,
        },
        watch: {
            '$route' (to, from) {
                let toRoute = to.fullPath
                if(!this.$utils.isEmpty(toRoute) && toRoute !== '/' && toRoute.toLowerCase() !== 'home'){
                    this.isHomePage = false
                }
                else {
                    this.isHomePage = true
                }                
            },
            isHomePage: function(){                
                this.setBackground()
            }
        },
        computed: {
            setPageBackground: function(){
                
                
            }
        },
        methods: {
            searchProtocol: function(){
                alert('search')
            },
            setBackground: function(){
                let body = this.body
                if(this.isHomePage === true){                
                    if(!body.classList.contains("home-page")){
                        body.classList.add("home-page")
                    }
                    if(body.classList.contains("other-page")){
                        body.classList.remove("other-page")
                    }
                }
                else{
                    if(body.classList.contains("home-page")){
                        body.classList.remove("home-page")
                    }
                    if(!body.classList.contains("other-page")){
                        body.classList.add("other-page")
                    }
                }
            }
        },
        created: function(){
            this.body = document.getElementsByTagName("BODY")[0]
            try{                    
                let route = this.$router.history.current.name
                if(!this.$utils.isEmpty(route) && route != 'home' && route != '/'){
                    this.isHomePage = false
                }
                else{
                    this.isHomePage = true
                }
            }
            catch(err) {
                this.isHomePage = true
            }
            this.setBackground()
        }
    }
</script>

<style>

body.other-page {   
    background-color: #2d87a8;
}

@media screen and (min-width: 1200px){
    body.home-page {
        background-image: url("../statics/images/lili_flower.jpg"), url("../statics/images/church.jpg");
    }
}

@media screen and (max-width: 1199px){
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
}
 
.nav a {
  text-decoration: none;
  color: white;
  display: block;
  transition: .3s background-color;
}
 
.nav a:hover {
  color: #03a326;
}

li.home {
    width: 120px;
    position: absolute;
    left: 0;
}

li.search {
    position: absolute;
    left: 110px;
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

</style>