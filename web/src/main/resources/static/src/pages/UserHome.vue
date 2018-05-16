<template>
    <div>
    <!--<Index></Index>-->
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
    
    export default {
        name: 'userHome',
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
                    if(this.$utils.isEmpty(userInfo) || this.$utils.isEmpty(userInfo.userUUID)){
                        this.$q.notify({message: `User session information has error!`, color: 'negative'})
                        return false
                    }

                    let urlPathInfo = window.location.hash.substr(1).split('/')
                    let urlUserUUID = urlPathInfo[urlPathInfo.length-1]
                    if(userInfo.userUUID != urlUserUUID){
                        message = `The current user is NOT the user you request.\nYou need to log out and sign in with the requested user credentials.`
                        this.$q.notify({
                            message: message, 
                            color: 'negative'
                        })
                        sessionStorage.errorMessage = message
                        document.location.href = '/errors/400'
                        return false
                    }
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
