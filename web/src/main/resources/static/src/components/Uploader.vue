<template>
    <div class="row text-center">
        <q-uploader :url="url" name="file" :multiple="isMultiple" :headers="headers" @uploaded="uploadFileDone" />
    </div>
</template>

<script>
    import {QUploader} from 'quasar'
    
    export default {
        data: function(){
            return {
                fileCount: 0,
            }
        },
        props: {
            name: {
                type: String,
                default: '',
                required: true
            },
            url: {
                type: String,
                default: '',
                required: true
            },
            headers: {
                type: Object,
                default: null,
                required: false
            },
            fileSizeLimit: {
                type: Number,
                default: 0,
                required: false
            },
            isMultiple: {
                type: Boolean,
                default: false,
                required: false
            }
        },
        computed: {

        },
        components: {
            QUploader,
        },
        methods: {
            filesChange: function(event){
                console.log(event)
//                $event.target.name, $event.target.files); 
                this.fileCount = event.target.files.length
            },
            uploadFileDone: function(file, xhr){                
                this.$emit('uploadFileDone', file, xhr)
            }
        }
    }
</script>

<style scoped>


</style>
