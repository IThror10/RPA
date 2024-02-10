<template>
  <header>
    <h2 class="title">RPAutomation</h2>
    <div class="links">
      <HeaderLink v-if="authorized" class="link" id="name" to="/profile">{{login}}</HeaderLink>
      <HeaderLink v-if="authorized" class="link" to="/script">Scripts</HeaderLink>
      <HeaderLink v-if="authorized" class="link" to="/online">Online</HeaderLink>
      <HeaderLink v-if="authorized" class="link" to="/login" @click="logout">Exit</HeaderLink>
    </div>
  </header>
</template>

<script lang="ts">
  import {defineComponent} from "vue";
  import store from '@/store';
  import {mapActions} from "vuex";
  import router from "@/router/router";
  import HeaderLink from "@/components/header/HeaderLink.vue";

  export default defineComponent({
    name: 'AppHeader',
    components: {HeaderLink},
    computed: {
      authorized: () => store.state.auth.isAuth,
      login: () => store.state.auth.data?.login
    },
    methods: {
      ...mapActions({
        loggedOut: 'auth/loggedOut',
      }),
      async logout() {
        await this.loggedOut()
            .then(() => router.push('/'))
      },
    }
  });
</script>

<style scoped>
  header {
    background-color: #445b54;
    color: #fff;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 15px;
  }

  .title {
    font-size: 27px;
    /*font-family: 'Signika', sans-serif;*/
    font-family: 'Rouge Script', cursive;
    font-weight: 320;
    /*line-height: 25px;*/
  }


  .links * {
    margin: 0px 20px;
    justify-content: center;
  }

  #name {
    font-weight: bold;
  }
</style>