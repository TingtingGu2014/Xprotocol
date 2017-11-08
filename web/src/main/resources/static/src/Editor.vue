<template id="editor-template">
    <div class="container">
        <form method="post">
            <textarea v-bind:id="editorName" v-model="body"></textarea>   
        </form>
    </div>
</template>     

<script>
            
    export default {
        
        props: {            
            editorName: String,            
            body: String,
            max_height: Number,
            max_width: Number,
            min_height: Number,
            min_width: Number,
        },
        data: function () {
            return {

            }
        },    
        mounted: function() {
            var vm = this
            tinymce.init({
                selector: '#'+this.editorName,
                plugins: [
                    'advlist autolink link image imagetools lists charmap print preview hr anchor pagebreak spellchecker',
                    'searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking',
                    'save table contextmenu directionality emoticons template paste textcolor'
                ],
                toolbar: 'insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright ' +
                         ' alignjustify | bullist numlist outdent indent | link image | print preview media fullpage ' +
                         ' | forecolor backcolor emoticons | code ',
                max_height: this.max_height,
                max_width: this.max_width,
                min_height: this.min_height,
                min_width: this.min_width, 
                init_instance_callback: function (editor) {
                    editor.on('blur', function (e) {
                        e.preventDefault()
                        var content = editor.getContent(self.value)                        
                        vm.$emit('content-change', content)
                    });
                }
            });
            
            tinymce.on
        },
        computed: {
            
        },
        methods: {
            
        }
    }

</script>

<style scoped>

</style>