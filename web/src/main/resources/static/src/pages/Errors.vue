<template>
    <div>
        <div class="row text-center" style="margin-top: 5%; padding: 5% 5% 5% 5%">
            <div class="col-12 text-center" ref="errorDiv">
                <span style='font-size: 2rem; font-family: Arial; font-weight: bold; color: red'><i class="fa fa-frown-o" aria-hidden="true"></i>
                    &nbsp;&nbsp;{{errorTitle}}
                </span>
            </div>
            <div class="col-12">                    
                  <br>Message: {{errorMessage}}                          
            </div>
            <div class="col-12">                    
                <br><q-btn color="positive" small icon="fa-home" @click.prevent="$router.push('/home')">Take me home</q-btn>                    
            </div>
          </div>          
      </div>
</template>

<script>

    export default {
        data: function(){
            var errorDetailMessage = sessionStorage.errorMessage
            errorDetailMessage = this.$utils.isEmpty(errorDetailMessage) ? '' : errorDetailMessage
            var error = this.$utils.getErrorPage(this.$route.params.errorCode, errorDetailMessage)
            sessionStorage.errorMessage = ''
            return {
                errorTitle: error.title,
                errorMessage: error.message,
            }
        },
        components: {}
    }
</script>

<style scoped>


</style>
