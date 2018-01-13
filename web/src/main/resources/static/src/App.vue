<template>
  <!-- Don't drop "q-app" class -->
  <div id="q-app">
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
    import axios from 'axios'
    import Navbar from './components/Navbar.vue';
    import { mapGetters, mapMutations } from 'vuex'
    import { EventBus } from './utils/EventBus.js';
    
    var Utils = require('./utils/Utils')
    
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
    input {
        -webkit-border-radius: 10px; 

        /* Firefox 1-3.6 */
        -moz-border-radius: 10px; 

        /* Opera 10.5, IE 9, Safari 5, Chrome, Firefox 4, iOS 4, Android 2.1+ */
        border-radius: 10px; 
    }
    
    .inner-row-div {
        line-height:2em
    }
    
    .move-right {
        text-align: right;
    }

</style>
