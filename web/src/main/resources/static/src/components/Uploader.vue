<template>
    <div class="col-lg-5 col-md-8 col-sm-12">
        <q-field 
            label="Click the plus sign to select files:" 
            :label-width="6"             
            :error="hasError"
            :error-label="errorLabel.toString()"
            helper="Click the cloud button to upload"
            style="width: 100%"            
        >
            <q-uploader 
                ref="uploader"
                :url="url" name="file" 
                :multiple="isMultiple" 
                :headers="headers" 
                :disable="isDisabled"
                auto-expand 
                @add="checkAddedFile"
                @finish="clearFilesAfterUpload"
                @uploaded="uploadFileDone" 
                @remove:cancel="removeFileFromQueue"
            />
        </q-field>
    </div>
</template>

<script>
    import {QUploader} from 'quasar'
    
    const FILE_SIZE_LIMIT_ERROR_LABEL = "The file size is more than the size limit! " 
    const FILE_COUNT_LIMIT_ERROR_LABEL = "The file count is more than the limit!"
    
    export default {
        data: function(){
            return {
                fileCount: 0,
                hasError: false,
                errorLabel: [],
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
            fileCountLimit: {
                type: Number,
                default: 0,
                required: false
            },
            isMultiple: {
                type: Boolean,
                default: false,
                required: false
            },
            isDisabled: {
                type: Boolean,
                default: false,
                required: false
            },
        },
        computed: {

        },
        components: {
            QUploader,
        },
        methods: {
            filesChange: function(event){
                console.log(event)
                this.fileCount = event.target.files.length
            },
            checkAddedFile: function(files){
                var fileCountLimitNotified = false
                for(var i = 0; i < files.length; i++){
                    let file = files[i]
                    let fileSize = file.size
                    if(fileSize > this.fileSizeLimit * 1000000){
                        this.hasError = true
                        this.errorLabel.push(FILE_SIZE_LIMIT_ERROR_LABEL)
                        this.$q.notify({message: `This file "`+file.name+`" has size `+parseInt(fileSize / 1000000)+`MB, which is over the limit of `+this.fileSizeLimit+ `MB.`, color: 'negative'})
                    }
                    this.fileCount++
                    if(fileCountLimitNotified === false && this.fileCount > this.fileCountLimit){
                        this.hasError = true
                        fileCountLimitNotified = true
                        this.errorLabel.push(FILE_COUNT_LIMIT_ERROR_LABEL)
                        this.$q.notify({message: `Now you are trying to upload more files than limit!`, color: 'negative'})
                    }
                }    
            },
            removeFileFromQueue: function(file){
                let files = this.$refs.uploader.files
                let fileSizeLimitError = false
                let fileCountLimitError = false
                
                if(this.hasError === true){
                    for(var i = 0; i < files.length; i++){
                        if(files[i].name != file.name && files[i].size > this.fileSizeLimit * 1000000){                             
                            fileSizeLimitError = true
                            break;
                        }
                    }
                    // When the file has over limit size is the one to be removed:
                    if(fileSizeLimitError === false && file.size > this.fileSizeLimit * 1000000){
                        this.errorLabel = this.$utils.removeArrayElementByValue(this.errorLabel, FILE_SIZE_LIMIT_ERROR_LABEL)
                    }
                    if(this.fileCount > this.fileCountLimit + 1){
                        fileCountLimitError = true
                    }
                    else if(this.fileCount === this.fileCountLimit + 1){
                        this.errorLabel = this.$utils.removeArrayElementByValue(this.errorLabel, FILE_COUNT_LIMIT_ERROR_LABEL)
                    }
                    if(fileSizeLimitError === false && fileCountLimitError === false){
                        this.errorLabel = []
                        this.hasError = false
                    }                    
                }
                this.fileCount--
            },
            uploadFileDone: function(file, xhr){                
                this.$emit('uploadFileDone', file, xhr)
            },
            clearFilesAfterUpload(){
                this.$refs.uploader.files = []
                this.fileCount = 0
                this.hasError = false
                this.errorLabel = ''
            }
        }
    }
</script>

<style scoped>


</style>
