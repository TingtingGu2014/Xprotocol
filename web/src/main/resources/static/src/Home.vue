<template>
    <div>
        
        <ProtocolList 
            :userUUID = "userUUID"
        >            
        </ProtocolList>
        
    </div>
</template>

<script>
    import ProtocolList from './ProtocolList.vue'
    
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
//            userProtocolUUID: '70b99490-bdff-11e7-abbd-8d88b42d3590',
            userUUID: userUUID, //'70b99491-bdff-11e7-abbd-8d88b42d3590',
//            editorName: 'homeProtocolEditor',
//            showPreview: true,
            loggedIn: loggedIn,
        }
      },
      components: {
          ProtocolList,
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
