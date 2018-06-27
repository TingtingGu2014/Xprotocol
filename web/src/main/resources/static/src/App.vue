<template>
  <div id="q-app">
    
    <router-view
        class="view"
        keep-alive
        transition
        transition-mode="out-in">
    </router-view> 
  </div>
</template>

<script>
    import {EventBus} from './utils/EventBus.js'
    import { mapGetters, mapMutations } from 'vuex'
    
    export default {
        name: 'App',
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
                this.$utils.readCookie('loggedIn')
                    .then((data) => {
                        if(this.$utils.isEmpty(data) || data != "true"){
                            this.$userUtils.signOut()
                            .then((data) => {
                                EventBus.$emit('session-change', 'signOut');                 
                            })
                            .catch((err) => {
                                console.log(err)
                            });
                            console.log('   ***  the user store and the protocol store have been cleared ***  ')
                        }
                        else{
                            console.log(' === the cookie monitor thread is running now ===')
                        }
                    })
                    .catch((error) => {
                        this.$q.notify({message: 'Something is wrong when clear store data. error: '+error.message, timeout: 3000, color: 'negative'})
                    })
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
                    localStorage.loggedIn = "false"
                    window.clearInterval(this.cookieTimer)
                }
            });
            this.$utils.readCookie('loggedIn')
            .then((data) => {
                if(data == "true"){
                    this.cookieTimer = window.setInterval(this.clearStoreData, 2000)
                }
                else if(localStorage.loggedIn == "true"){
                    this.setProtocols(null)
                    this.setUserDetails(null)
                    this.setDetailsFetched(false)
                    localStorage.loggedIn = "false"
                    if(this.cookieTimer){
                        window.clearInterval(this.cookieTimer)
                    }
                }
            })
            .catch((error) => {
                this.$q.notify({message: 'Something is wrong when reading loggedIn cookie. error: '+error.message, timeout: 3000, color: 'negative'})
            })
                        
        }      
    }
</script>

<style>
	input {
        -webkit-border-radius: 10px; 

        /* Firefox 1-3.6 */
        -moz-border-radius: 10px; 

        /* Opera 10.5, IE 9, Safari 5, Chrome, Firefox 4, iOS 4, Android 2.1+ */
        border-radius: 5px; 
    }
    
    .inner-row-div {
        line-height:2em
    }
    
    .move-right {
        text-align: right;
    }
    
    fieldset {
        border:1px solid d5d9e0;
        border-radius: 5px;
        box-shadow: 0 0 3px rgba(0,0,0,.3);
    }
    
    fieldset legend {
        font-family: arial, "Times New Roman", Times, serif;
        font-size: 0.6rem;
        font-weight: bold;
        background-color: #ecf2f9;
        padding: 5px 10px 5px 10px;
        margin: 0 5px 0 5px;
        box-shadow: 0 0 5px rgba(0,0,0,.3);
         
    }
    .textarea-display {
        white-space: pre-wrap;
    }
    
    .q-card {
        border:1px solid #d5d9e0;
        border-radius: 5px;
        box-shadow: 0 0 3px rgba(0,0,0,.3);
    }
    
    .addNewTextarea {
        border:1px solid light-grey;
        border-radius: 5px;
        box-shadow: 0 0 3px rgba(0,0,0,.5);
        padding: 5px 10px 5px 10px;
        font-family: Georgia, "Times New Roman", Times, serif;
    }
</style>
