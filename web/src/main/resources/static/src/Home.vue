<template>
    <div>
        <br><h4>Check out the newly added protocols!</h4><br>
        <Protocol
            :editorName = "editorName"
            :userUUID = "userUUID"
            :userProtocolUUID = "userProtocolUUID"
        >            
        </Protocol>
    </div>
</template>

<script>
    import Protocol from './Protocol.vue'
    
    var Utils = require('./Utils')
    var loggedIn = !Utils.isEmpty(Utils.readCookie('loggedIn'))
    
    
    export default {
      data: function(){
        return {
          name: 'Home',
          userProtocolUUID: '70b99490-bdff-11e7-abbd-8d88b42d3590',
          userUUID: '70b99491-bdff-11e7-abbd-8d88b42d3590',
          editorName: 'homeProtocolEditor',
          loggedIn: loggedIn,
        }
      },
      components: {
          Protocol
      },
      methods: {
        alertName: function(){
          alert(this.name);
        }
      }
    }
</script>

<style>
  p{
    color: #69C;
  }
</style>
