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
    
    export default {
        name: 'index',
        data: function(){
        return {
            name: 'Home',
            userUUID: userUUID, 
            loggedIn: loggedIn,
        }
        },
        components: {
            ProtocolList, CommentList
        },
        methods: {

        },
        beforeCreate: function(){
            this.loggedIn = !this.$utils.isEmpty(this.$utils.readCookie('loggedIn'))

            if(loggedIn === true) {
                try{
                    this.userInfo = JSON.parse(localStorage.userInfo)
                    this.userUUID = userInfo.userUUID
                }
                catch(err) {
                    console.log(err.message)
                    localStorage.userInfo = ''
                }
            }
        },
    }
</script>

<style lang="stylus">
    h4 {
        text-align: center;
    }
</style>
