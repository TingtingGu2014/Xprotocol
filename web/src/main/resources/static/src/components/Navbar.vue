<template>
    <div>
        <q-toolbar slot="header" color="blue-grey-10" class="glossy navbar" style="width: 100%;">
            
            <div class="row items-center" style="width: 100%; vertical-align: middle;"> 
                <div class="col-xs-4 col-sm-3 col-md-2 col-lg-2 gt-md" style="order: 1">
                    <router-link :to="{ name: 'home'}"><span class="fa fa-home"></span> &nbsp;XProtocol</router-link>
                </div>
                <div class="col-xs-4 col-sm-3 col-md-2 col-lg-2 lt-lg" style="order: 1">
                    <a href="#" v-on:click="toggleLogin"><span class="fa fa-bars"></span> &nbsp;XProtocol</a>
                </div>
                <div class="col-xs-8 col-sm-9 col-md-10 col-lg-3" style="order: 2">
                    <form>
                            <!--<input class="" type="text" placeholder="Find Protocols">-->
                        <router-link :to="{ name: 'home'}" style="vertical-align: middle;">
                            <input class="form-control " type="text" placeholder="Search Protocols" >
                            <span class="fa fa-search"></span>
                        </router-link>
                    </form>
                </div>
                <div class="col-xs-12 col-sm-3 col-md-2 col-lg-1 gt-md move-right" style="order: 4" v-if="isAdminUser">
                    <router-link :to="{ name: 'admin' }"><i class="fa fa-sun-o"></i>&nbsp;Admin</router-link>
                </div>
                <div class="col move-right" style="order: 3">
                    <login-view :inNavBar="true"></login-view>
                </div>
            </div>
        </q-toolbar>
        
        <login-view v-if="showLoginSheet" :inNavBar="false"></login-view>
      <!--
        Replace following <div> with
        <router-view /> component
        if using subRoutes
      -->
<!--      <div class="layout-padding logo-container non-selectable no-pointer-events">
        <div class="logo" :style="position">
          <img src="~assets/quasar-logo-full.svg">
        </div>
      </div>-->
    </div>
</template>

<script>
    import LoginView from './LoginView.vue'
    import {
        dom,
        QToolbar,
        QCollapsible,
    } from 'quasar'
    
    var Utils = require('../utils/Utils')

    export default {
      name: 'index',
      components: {
        QToolbar,
        QCollapsible,
        LoginView,
      },
      data () {
        return {
            showLoginSheet: false,            
        }
      },
        computed: {
            isAdminUser: function () {
                return Utils.isAdminUser();
            },
        },
      methods: {
        toggleLogin: function(){
            this.showLoginSheet = !this.showLoginSheet
        },        
      },
      mounted () {
        
      },
      beforeDestroy () {
        
      }
    }
</script>

<style lang="stylus">
    
li.nav-li-item
    display:inline-block
    margin-right: 30px
    
.nav-left
    display: block
    float: left
    
input {
    width: 200px;
    padding: 0 20px;
}

input,
input::-webkit-input-placeholder {
    font-size: 13px;
    line-height: 2;
}
</style>
