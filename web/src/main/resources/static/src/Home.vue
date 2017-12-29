<template>
    <div>
        
        <ProtocolList v-if="loggedIn"
            :userUUID = "userUUID"
        >   
        </ProtocolList>
        
        <CommentList v-if="loggedIn"
            :userUUID = "userUUID"
        ></CommentList>
        
    </div>
</template>

<script>
    import ProtocolList from './ProtocolList.vue'
    import CommentList from './CommentList.vue'
    
    var Utils = require('./Utils')
    var loggedIn = !Utils.isEmpty(Utils.readCookie('loggedIn'))
    var userInfo = null
    var userUUID = ''

    if(loggedIn === true) {
        try{
            userInfo = JSON.parse(localStorage.userInfo)
            userUUID = userInfo.userUUID
        }
        catch(err) {
            console.log(err.message)
            localStorage.userInfo = ''
        }
    }
    
    
    export default {
      data: function(){
        return {
            name: 'Home',
            userUUID: userUUID, 
            loggedIn: loggedIn,
        }
      },
      components: {
          ProtocolList, CommentList,
      },
      methods: {
        alertName: function(){
          alert(this.name);
        }
      }
    }
</script>

<style>
h4 {
        text-align: center;
}
</style>
