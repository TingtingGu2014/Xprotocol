<template>
    <div>
        <router-link v-bind:to="{ name: name, params: params }" >
            <span v-html="label"></span>
        </router-link>
    </div>
</template>

<script>
    
    export default {
        data: function(){
            return {
                name: '',
                params: {},
                label: '',                
            }
        },
        props: {
            linkData: Object,
        },
        beforeMount: function(){
            this.name = this.linkData.name ? this.linkData.name : ''
            this.params = this.linkData.params ? this.linkData.params : null
            this.label = this.linkData.label ? this.linkData.label : ''            
        },
    }
</script>

<style>

</style>
