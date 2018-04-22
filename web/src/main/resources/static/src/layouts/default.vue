<template>
  <q-layout view="lHh Lpr lFf">
    <q-layout-header>
      <q-toolbar
        color="primary"
        :glossy="$q.theme === 'mat'"
        :inverted="$q.theme === 'ios'"
      >
<!--        <q-btn
          flat
          dense
          round
          @click="leftDrawerOpen = !leftDrawerOpen"
          aria-label="Menu"
        >
          <q-icon name="menu" />
        </q-btn>-->

        <q-toolbar-title>
          XProtocol
          <div slot="subtitle">Biggest protocol collections!</div>
        </q-toolbar-title>
        
        <div class="row items-center" style="width: 100%; vertical-align: middle;"> 
<!--            <div class="col-xs-4 col-sm-3 col-md-2 col-lg-2 gt-md" style="order: 1">
                <router-link :to="{ name: 'home'}"><span class="fa fa-home"></span> &nbsp;XProtocol</router-link>
            </div>-->
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
    </q-layout-header>


    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script>
import { openURL } from 'quasar'
import LoginView from 'pages/LoginView.vue' 

export default {
    name: 'LayoutDefault',
    data () {
        return {
          leftDrawerOpen: this.$q.platform.is.desktop,
          showLoginSheet: false,  
        }
    },
    methods: {
        openURL,
        toggleLogin: function(){
            this.showLoginSheet = !this.showLoginSheet
        },     
    },
    components: {
        LoginView,
    },
    computed: {
        isAdminUser: function () {
            return this.$utils.isAdminUser();
        },
    },

}
</script>

<style>
</style>
