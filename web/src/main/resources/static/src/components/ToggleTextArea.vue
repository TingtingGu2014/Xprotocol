<template>
    <div style="width: 100%; padding: 0 10px 0 10px; " :ref="name+'ToggleDiv'">
        <div class="row">
        <div class="col-12 toggle-editor" style="display: none;">
            <q-field :label="label" >
                <q-input id="toggle-editor" type="textarea" ref="toggleEditor" :min-rows="4" :max-rows="8" class="textarea-class"
                v-model="newValue" 
            />
            </q-field>            
        </div>
        </div>
        <div class="row" style="margin-top: .5em;">
        <div class="col text-center toggle-editor" style="display: none;">
            <q-btn 
                class="toggle-btn" 
                color="blue" 
                icon="fa-check" 
                size="sm"
                :id="name + '-toggle-btn-done'" 
                @click.prevent="toggleBtnClick"
            >
                &nbsp;&nbsp;Save
            </q-btn>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <q-btn 
                class="toggle-btn" 
                color="positive" 
                size="sm"
                icon="fa-times" 
                :id="name + '-toggle-btn-cancel'" 
                @click.prevent="toggleBtnClick"
            >
                &nbsp;&nbsp;Cancel
            </q-btn>
            
            <q-btn 
                v-for="btn in otherBtns" 
                :key="btn['id']" 
                size="sm"
                class="toggle-btn" 
                :color="btn['color']" 
                :icon="btn['icon']" 
                :id="btn['id']" @click.prevent="otherBtnClick" 
                style="margin-left: 30px;"
            >
                &nbsp;&nbsp;{{btn['label']}}
            </q-btn>
        </div>
        </div>
        <div class="row">
        <div class="col toggle-display" ref="toggleDisplay">
            <a :id="'displayLink-' + name" href="#" @click.prevent="toggleBtnClick">
                <q-field :label="label">
                    <p class="display-p" v-html="newValue"></p>
                </q-field>            
            </a>
        </div>
        </div>
    </div>
    
</template>

<script>

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
            label: {
                type: String,
                default: '',
                required: false
            },
            inputValue: {
                type: String,
                default: '\n\n',
                required: false
            },
            otherBtns: {
                type: Array,
                default: null,
                required: false,
            }
        },
        beforeMount: function(){        
            this.oldValue = this.inputValue
            this.newValue = this.inputValue
        },
        components: {
        },
        methods: {
            toggleBtnClick: function(event){
                var id = event.target.id
                if(this.$utils.isEmpty(id)){
                    id = event.currentTarget.id
                }
                if(id.lastIndexOf('-btn-done') > 0){
                    var editor = this.$refs.toggleEditor
                    var val = editor.value
                    if(editor){
                        this.newValue = val
                        this.oldValue = val
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
                    this.$emit('toggleTextAreaValueChange', this.newValue, this.name)
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
            },
            otherBtnClick: function(event){
                var id = event.target.id
                if(this.$utils.isEmpty(id)){
                    id = event.currentTarget.id
                    if(this.$utils.isEmpty(id)){
                        this.$toast.create.negative({html: 'Cannot get ID from button click!', duration: 3000})
                        return false
                    }                    
                }
                this.$emit('otherBtnClick', id)
            }
        }
    }
</script>

<style scoped>
    #toggle-display {
        font-family: Georgia, "Times New Roman", Times, serif;
    }
    .display-p {
        white-space: pre-wrap;
//        background: #e6f7ff;
        color: #133913;
        font-family: Georgia, "Times New Roman", Times, serif;
        margin: 0 30px 0 30px;
        padding: 8px 8px 8px 8px;
        over-flow: auto;
    }
    .textarea-class {
        border-bottom: 1px solid #b4e1f7;
        box-shadow: 0 1px 0 0 #ffa000;
        padding: 10px 12px 5px 12px;
    }
    [id^="displayLink"] {
        text-decoration: none;        
    }
    [id^="displayLink"]:hover {
        color: #027be3;
        font-weight: bold;
    }
</style>
