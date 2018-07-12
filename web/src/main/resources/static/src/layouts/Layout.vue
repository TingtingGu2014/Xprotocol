<template>
    <div class="toolbar-title">                      
        <q-layout view="lHh Lpr lFf" >
            <div class="row gt-lg">
                <desktop-navbar></desktop-navbar>
            </div>
            <div class="row lt-xl">
                <mobile-navbar></mobile-navbar>
            </div>                
            <q-page-container>
              <router-view />
            </q-page-container>
        </q-layout>
    </div>  
</template>

<script>
import DesktopNavbar from 'components/DesktopNavbar.vue' 
import MobileNavbar from 'components/MobileNavbar.vue'  

export default {
    name: 'LayoutDefault',
    data () {
        return {
          showLoginSheet: false,  
          search: '',
        }
    },
    methods: {
           
    },
    components: {
        DesktopNavbar,MobileNavbar,
    },
    computed: {
        
    },

}
</script>

<style>

</style>
