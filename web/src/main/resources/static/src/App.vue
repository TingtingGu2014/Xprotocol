<template>
    <div id="app">
        <Navbar></Navbar>
        <router-view
            class="view"
            keep-alive
            transition
            transition-mode="out-in">
        </router-view>    
    </div>
</template>

<script>
    import Navbar from './Navbar.vue';
    import { mapGetters, mapMutations } from 'vuex'
    import { EventBus } from './EventBus.js';
    
    var Utils = require('./Utils')
    
    export default {
	name: 'app',
        components: {Navbar},
        data() {
            return {
                cookieTimer: null,
            }
        },
        methods: {
            ...mapMutations({                                
                setProtocols: 'protocolModule/setProtocols',
                setUserDetails: 'userModule/setUserDetails',
                setDetailsFetched: 'userModule/setDetailsFetched',
            }),
            clearStoreData: function () {                
                var loggedIn = !Utils.isEmpty(Utils.readCookie('loggedIn'))
                if(loggedIn !== true){
                    Utils.signOut()
                    .then((data) => {
                        if(data){                            
                            EventBus.$emit('session-change', 'signOut');
                        }                    
                    })
                    .catch((err) => {
                        alert("oops, something happened during signing in!")
                        console.log(err)
                    });
                    console.log('   ***  the user store and the protocol store have been cleared ***  ')
                }
                else{
                    console.log(' === the cookie monitor thread is running now ===')
                }
                return false
            },
        },
        created: function() {            
            EventBus.$on('session-change', (changeAct) => {
                if(changeAct == 'signIn'){
                    console.log(`${changeAct} just kicked in`)
                    this.cookieTimer = window.setInterval(this.clearStoreData, 2000)
                }
                else if(changeAct == 'signOut'){
                    console.log(`Oh, that's NOT nice. It's gotten clicks! :) ${changeAct}`)
                    this.setProtocols(null)
                    this.setUserDetails(null)
                    this.setDetailsFetched(false)
                    window.clearInterval(this.cookieTimer)
                }
            });
            var loggedIn = !Utils.isEmpty(Utils.readCookie('loggedIn'))
            if(loggedIn == true){
                this.cookieTimer = window.setInterval(this.clearStoreData, 2000)
            }
        }
    }
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  
}

h1, h2 {
  font-weight: normal;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  padding: 0;
  margin: 0;
    text-align: left;
}

a {
  color: #42b983;
  margin-right: 5px;
}

table.table-component__table {
    position: relative;
    //display: block;
    margin: 10px auto;
    padding: 0;
    width: 100%;
    height: auto;
    border-collapse: collapse;
    text-align: center;
}

th.table-component__th {
    text-align: center;
}

.table-hover tbody tr:hover {
    background-color: #E0FFFF;
}

input.table-component__filter__field {
    width: 30%;
    margin-bottom: 5px;
}

fieldset {
    border: 1px groove lightblue !important;
    padding: 0 1.4em 1.4em 1.4em !important;
    margin: 0 0 1.5em 0 !important;
    -webkit-box-shadow:  0px 0px 0px 0px #000;
            box-shadow:  0px 0px 0px 0px #000;
}

legend {
    width:auto; /* Or auto */
    padding:0 10px; /* To give a bit of padding on the left and right */
    border-bottom:none;
}
</style>
