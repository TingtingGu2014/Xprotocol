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

    import ProtocolList from 'components/ProtocolList.vue'
    import CommentList from 'components/CommentList.vue'
    import Index from './index.vue'
    
    export default {
        name: 'home',
        data: function(){
            return {
                userUUID: '', 
                loggedIn: false,
            }
        },
        components: {
            ProtocolList, CommentList,
        },
        methods: {

        },
        created: function(){
            this.loggedIn = !this.$utils.isEmpty(this.$utils.readCookie('loggedIn'))
            if(this.loggedIn === true) {
                try{
                    let userInfo = JSON.parse(localStorage.userInfo)
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
