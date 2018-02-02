<template>
    <div class="row" style="width: 100%; margin: auto" :ref="name+'ToggleDiv'">
        <textarea class="textarea-class" :id="name" :ref="name" v-model="model" rows="4" ></textarea>
    </div>
    
</template>

<script>
    import {QField, QInput, QBtn} from 'quasar'
    var Utils = require('../../utils/Utils')
    export default {
        data: function(){
            return {
                newValue: '',
                oldValue: '',
            }
        },
        props: {
            name: {
                type: String,
                default: 'textarea-component',
                required: false
            },
            model: {
                type: String,
                default: '',
                required: false
            },
            
        },
        beforeMount: function(){        
            
        },
        components: {
            QField, QInput, QBtn,
        },
        methods: {
            
        }
    }
</script>

<style scoped>
    .textarea-class {
        border-bottom: 1px solid #ffa000;
        box-shadow: 0 1px 0 0 #ffa000;
    }
</style>
