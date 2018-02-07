<template>    
    <div class="row align-items-center justify-content-center">
        <form method="post">
            <textarea :id="id" v-model="body"></textarea>  
            <input type="hidden" id="filePickerName" >
        </form>
    </div>
</template>

<script>
    var Utils = require('../utils/Utils')
    
    export default {
        name: "tinymce",
        props: {     
            id: {
                type: String,
                required: true
            },
            userUUID: String,
            body: {
                type: String,
                default: ''
            },
//            max_height: String,
//            max_width: String,
//            min_height: String,
//            min_width: String,
            height: String,
            width: String,
        },
        data: function () {
            return {

            }
        },    
        /**
            Image Upload code source: https://www.tinymce.com/docs/demo/file-picker/
        **/
        mounted: function() {
            var vm = this
            tinymce.init({
                selector: '#'+this.id,
                plugins: [
                    'advlist autolink link image imagetools lists charmap print preview hr anchor pagebreak spellchecker',
                    'searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking',
                    'save table contextmenu directionality emoticons template paste textcolor'
                ],
                toolbar: 'insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright ' +
                         ' alignjustify | bullist numlist outdent indent | link image | print preview media fullpage ' +
                         ' | forecolor backcolor emoticons | code ',
                width: this.width,
                height: this.height,
//                max_height: this.max_height,
//                max_width: this.max_width,
//                min_height: this.min_height,
//                min_width: this.min_width,
                paste_data_images: true,
                image_advtab: true,
                automatic_uploads: true,
                file_picker_types: 'image', 
                images_reuse_filename: true,
                style_formats: [
                    {title: 'Image Left', selector: 'img', styles: {
                      'float' : 'left',
                      'margin': '0 10px 0 10px'
                    }},
                    {title: 'Image Right', selector: 'img', styles: {
                      'float' : 'right',
                      'margin': '0 10px 0 10px'
                    }}
                ],
                
                file_picker_callback: function(cb, value, meta) {
                    var input = document.createElement('input');
                    input.setAttribute('type', 'file');
                    input.setAttribute('accept', 'image/*');

                    // Note: In modern browsers input[type="file"] is functional without 
                    // even adding it to the DOM, but that might not be the case in some older
                    // or quirky browsers like IE, so you might want to add it to the DOM
                    // just in case, and visually hide it. And do not forget do remove it
                    // once you do not need it anymore.

                    input.onchange = function() {
                        var file = this.files[0];

                        var reader = new FileReader();
                        reader.onload = function () {
                            // Note: Now we need to register the blob in TinyMCEs image blob
                            // registry. In the next release this part hopefully won't be
                            // necessary, as we are looking to handle it internally.
                            var id = 'blobid' + (new Date()).getTime();
                            var blobCache =  tinymce.activeEditor.editorUpload.blobCache;
                            var base64 = reader.result.split(',')[1];
                            var blobInfo = blobCache.create(id, file, base64);
                            blobCache.add(blobInfo);

                            // call the callback and populate the Title field with the file name
                            cb(blobInfo.blobUri(), { title: file.name });
                        };
                        var picker = document.getElementById('filePickerName')
                        picker.value = file.name
                        reader.readAsDataURL(file);
                    };

                    input.click();
                },
                
                images_upload_handler: function (blobInfo, success, failure) {
                    var xhr, formData, json={};
                    var csrf = Utils.readCookie('XSRF-TOKEN')
                    xhr = new XMLHttpRequest();
                    xhr.withCredentials = false;                    
                    xhr.open('POST', '/api/users/'+vm.userUUID+'/protocols/'+vm.id+'/files');
                    xhr.setRequestHeader('X-XSRF-TOKEN', csrf)

                    xhr.onload = function() {                      

                        if (xhr.status != 200) {
                          failure('HTTP Error: ' + xhr.status);
                          return;
                        }

                        json = JSON.parse(xhr.responseText);

                        if (!json || typeof json.location != 'string') {
                          failure('Invalid JSON: ' + xhr.responseText);
                          return;
                        }
                        console.log('location =' + json.location);
                        
                        // ** trigger the uploaded file event for the protocol object **
                        var originalFileName = document.getElementById('filePickerName').value                       
                        var fileName = json.location+'____'+originalFileName                        
                        console.log('file name = '+fileName)                    
                        vm.$emit('editor-file-uploaded', fileName)
                    
                        success(json.location);
                    };

                    formData = new FormData();
                    formData.append('file', blobInfo.blob(), blobInfo.filename());

                    xhr.send(formData);
                    
                    return json.location
                },

                init_instance_callback: function (editor) {
                    editor.on('Blur', function (e) {
                        e.preventDefault()
                        var content = editor.getContent(self.value) 
                        vm.$emit('content-blur', content)
                    });
                    
                    editor.on('KeyUp', function (e) {
                        e.preventDefault()
                        var content = editor.getContent(self.value) 
                        vm.$emit('content-change', content)
                    });
                    
                    editor.on('MouseUp', function (e) {
                        e.preventDefault()
                        var content = editor.getContent(self.value) 
                        vm.$emit('content-change', content)
                    });
                }
            });
            
        },
        beforeDestroy () {
            tinymce.execCommand('mceRemoveEditor', false, this.id)
        },
    }
</script>