<template>
    <div>
        
        <Protocol
            :editorName = "editorName"
            :userUUID = "userUUID"
            :userProtocolUUID = "userProtocolUUID"
            :showPreview = "showPreview"
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
          showPreview: true,
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
h4 {
        text-align: center;
}
</style>
