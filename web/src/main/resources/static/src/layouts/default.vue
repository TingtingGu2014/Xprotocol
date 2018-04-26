<template>
  <q-layout view="lHh Lpr lFf">
    <q-layout-header>
      <q-toolbar
        color="primary"
        :glossy="$q.theme === 'mat'"
        :inverted="$q.theme === 'ios'"
      >

            <div class="toolbar-title">                
                <router-link :to="{ name: 'home'}" class="link-with-bg"><span class="fa fa-home"></span> &nbsp;XProtocol!</router-link>
                <div class="toolbar-subtitle">Biggest protocol collections!</div>
            </div>
            <div class="col-xs-4 col-sm-3 col-md-2 col-lg-2 lt-lg" style="order: 1">
                <a href="#" v-on:click="toggleLogin"><span class="fa fa-bars"></span> &nbsp;XProtocol</a>
            </div>
            <div class="move-left" style="order: 2">
                <form>
                    <input class="form-control " type="text" placeholder="Search Protocols" >&nbsp;&nbsp;
                    <span class="fa fa-search"></span>
                </form>
            </div>
            <div class="col-xs-12 col-sm-3 col-md-2 col-lg-1 gt-md move-right" style="order: 4" v-if="isAdminUser">
                <router-link :to="{ name: 'admin' }" class="link-with-bg"><i class="fa fa-sun-o"></i>&nbsp;Admin</router-link>
            </div>
            <div class="col move-right" style="order: 3">
                <login-view :inNavBar="true"></login-view>
            </div>

      </q-toolbar>
    </q-layout-header>


    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script>

import LoginView from 'components/LoginView.vue' 

export default {
    name: 'LayoutDefault',
    data () {
        return {
          showLoginSheet: false,  
        }
    },
    methods: {
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
