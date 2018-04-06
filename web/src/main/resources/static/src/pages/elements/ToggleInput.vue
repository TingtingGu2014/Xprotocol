<template>
    <div class="row" style="width: 100%; margin: auto" :ref="name+'ToggleDiv'">
        <div class="col-md-8 col-sm-12 toggle-editor">
            <q-field :label="label" >
                <q-input id="toggle-editor" ref="toggleEditor" color="teal-9" 
                :class="editorClasses" :style="editorStyles" :type="inputType" 
                v-model="newValue" 
            />
            </q-field>            
        </div>
        <div class="col text-center toggle-editor">
            <q-btn class="toggle-btn" color="blue" small icon="fa-check" :id="name + '-toggle-btn-done'" @click.prevent="toggleBtnClick">Done</q-btn>
            <q-btn class="toggle-btn" color="positive" small icon="fa-times" :id="name + '-toggle-btn-cancel'" @click.prevent="toggleBtnClick">Cancel</q-btn>
        </div>
        
        <div class="col toggle-display" style="display: none" ref="toggleDisplay">
            <a id="displayLink" href="#" @click.prevent="toggleBtnClick">
                <q-field :label="label">
                    <span class="display-span" :style="displayStyles" >{{newValue}}</span>
                </q-field>            
            </a>
        </div>
    </div>
    
</template>

<script>
    import {QField, QInput, QBtn} from 'quasar'
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
                required: true
            },
            inputType: {
                type: String,
                default: 'text',
                required: false
            },
            label: {
                type: String,
                default: '',
                required: false
            },
            displayClasses: {
                type: String,
                default: '',
                required: false
            },
            displayStyles: {
                type: String,
                default: '',
                required: false
            },
            editorClasses: {
                type: String,
                default: '',
                required: false
            },
            editorStyles: {
                type: String,
                default: '',
                required: false
            },
            inputValue: {
                type: String,
                default: '',
                required: false
            },
        },
        beforeMount: function(){        
            this.oldValue = this.inputValue
            this.newValue = this.inputValue
        },
        components: {
            QField, QInput, QBtn,
        },
        methods: {
            toggleBtnClick: function(event){
                var id = event.target.id
                if(this.$utils.isEmpty(id)){
                    id = event.currentTarget.id
                }
                if(id.lastIndexOf('-btn-done') > 0){
                    var editor = this.$refs.toggleEditor
                    if(editor){
                        this.newValue = editor.value
                        this.oldValue = editor.value
                    }
                    var refs = this.$refs
                    var div = refs[this.name+'ToggleDiv']
                    var editors = div.getElementsByClassName('toggle-editor')
                    if(editors){
                        for(var i = 0; i < editors.length; i++){
                            editors[i].style.display = 'none'
                        }
                    }
                    var displayDiv = refs['toggleDisplay']
                    displayDiv.style.display = 'block'
                    this.$emit('toggleInputValueChange', this.newValue)
                }
                else if(id.lastIndexOf('-btn-cancel') > 0){
                    this.newValue = this.oldValue
                    var displayDiv = this.$refs.toggleDisplay
                    displayDiv.style.display = 'block'
                    var refs = this.$refs
                    var div = refs[this.name+'ToggleDiv']
                    var editors = div.getElementsByClassName('toggle-editor')
                    if(editors){
                        for(var i = 0; i < editors.length; i++){
                            editors[i].style.display = 'none'
                        }
                    }
                }
                else if(id.indexOf('displayLink') >= 0){
                    var refs = this.$refs
                    var div = refs[this.name+'ToggleDiv']
                    var editors = div.getElementsByClassName('toggle-editor')
                    if(editors){
                        for(var i = 0; i < editors.length; i++){
                            editors[i].style.display = 'block'
                        }
                    }
                    var displayDiv = refs['toggleDisplay']
                    if(displayDiv){
                        displayDiv.style.display = 'none'
                    }
                }
            }
        }
    }
</script>

<style scoped>
    #toggle-editor {
        margin-left: 30px;
        font-family: Georgia, "Times New Roman", Times, serif;
        font-color: blue !important;
    }
    #toggle-display {
        font-family: Georgia, "Times New Roman", Times, serif;
    }
    .toggle-btn {
        margin-right: 7%;
        margin-top: 10px;
    }
    .display-span {

    }
</style>
