<template>
    <div>
        <div class="row gt-sm">
            <ProtocolList v-if="loggedIn"
                :userUUID = "userUUID"
            >   
            </ProtocolList>

            <CommentList v-if="loggedIn"
                :userUUID = "userUUID"
            ></CommentList>
            
            <ProtocolToUserList></ProtocolToUserList>
        </div>
        <!-- This search box appears on small screens -->
        <div class="lt-md search-row-small" v-bind:style="{marginTop: searchBoxMarginTop, width: '87%'}">
            <div class="row">
                <input type="text" name="search" class="search" placeholder="Find Protocols...">
            </div>
            <div class="row">
                <label for="search" class="col-2" style="margin-top: 15px;margin-left: 10%">
                    <a href="#" v-on:click="" class="search-link">&nbsp;&nbsp;
                    <span class="fa fa-search" ></span></a>
                </label> 
            </div>
        </div>
    </div>
</template>

<script>

    import ProtocolList from 'components/ProtocolList.vue'
    import CommentList from 'components/CommentList.vue'    
    import ProtocolToUserList from 'components/ProtocolToUserList.vue'
    
    export default {
        name: 'home',
        data: function(){
            return {
                userUUID: '', 
                loggedIn: false,
                searchBoxMarginTop: '75%',
            }
        },
        components: {
            ProtocolList, CommentList, ProtocolToUserList
        },
        watch: {
            searchBoxMarginTop: function() {
                let mobileNavbar = document.getElementsByClassName('mobil-navbar')[0]
                let searchBox = document.getElementsByClassName('search-row-small')[0]
                if(!this.$utils.isEmpty(mobileNavbar) && !this.$utils.isEmpty(searchBox)){
                    let bottom = mobileNavbar.offsetTop + mobileNavbar.offsetHeight
                    let top = searchBox.offsetTop
                    if(bottom - top > 200){
                        alert(top)  
                    }
                    else{
                        alert( bottom + 200)
                    }
                }
            }
        },
        methods: {
            
        },
        created: function(){
            this.loggedIn = localStorage.loggedIn == "true"
            if(this.loggedIn === true) {
                try{
                    let userInfo = JSON.parse(localStorage.userInfo)
                    if(this.$utils.isEmpty(userInfo) || this.$utils.isEmpty(userInfo.userUUID)){
                        this.$q.notify({message: `User session information has error!`, color: 'negative'})
                        return false
                    }
                    
                    this.userUUID = userInfo.userUUID
                }
                catch(err) {
                    console.log(err.message)
                    localStorage.userInfo = ''
                }
            }
            
            this.$utils.readCookie('XSRF-TOKEN')
            .then((data) => {
                if(this.$utils.isEmpty(data)){
//                    this.$router.go()
                    this.$reqUtils.getHomePage()
                    .then((data) => {                       
                        location.reload(true)
                        return false
                    })
                    .catch((error) => {
                        this.$q.notify({message: error.message, color: 'negative'})
                    })
                }
                else{                    
                    let xsrf = localStorage.xsrf
                    if(this.$utils.isEmpty(xsrf) || xsrf != data){
                        localStorage.xsrf = data
                    }
                }                
            })
            .catch((error) => {
                console.log(`Cannot read XSRF cookie!`)
            })
        },
    }
</script>

<style lang="stylus" scoped>

    h4 {
        text-align: center;
    }
    
    div.search-row-small {
        position: absolute;
        left: 5%;
        text-align: left;
    }
    
    input.search {
        width: 100%;
        outline: none;
        z-index:1;
    }

</style>
