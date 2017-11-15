<template>    
    <div class="row align-items-center justify-content-center">
        <form method="post">
            <textarea :id="id" v-model="body"></textarea>  
        </form>
    </div>
</template>

<script>

    export default {
        name: "tinymce",
        props: {     
            id: {
                type: String,
                required: true
            },
            body: {
                type: String,
                default: ''
            },
            max_height: String,
            max_width: String,
            min_height: String,
            min_width: String,
            height: String,
            width: String,
        },
        data: function () {
            return {

            }
        },    
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
                max_height: this.max_height,
                max_width: this.max_width,
                min_height: this.min_height,
                min_width: this.min_width,
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