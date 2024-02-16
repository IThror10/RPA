<template>
  <div class = "sign_in_page">
    <common-form>
      <error-message v-show="hasError">{{errorMsg}}</error-message>
      <label-input id="li" type="text" label="Login" placeholder="Enter Login" v-model="form.username"/>
      <label-input id="pi" type="password" label="Password" placeholder="Enter Password" v-model="form.password" />
      <div class="buttons">
        <header-link class="link" to="/register">SignUp</header-link>
        <my-button class="button" @click="login">Login</my-button>
      </div>
    </common-form>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {UserService} from "@/services/UserService";
import {mapActions} from "vuex";
import ErrorMessage from "@/components/UI/primitives/ErrorMessage.vue";
import errorMixin from "@/components/mixins/errorMixin";
import MyButton from "@/components/UI/primitives/MyButton.vue";
import HeaderLink from "@/components/header/HeaderLink.vue";

export default defineComponent({
  name: "LoginPage",
  components: {HeaderLink, MyButton, ErrorMessage},
  mixins: [errorMixin],
  data() {
    return {
      form: {
        username: '',
        password: ''
      },
    }
  },
  computed: {
    us: () => new UserService()
  },
  methods: {
    ...mapActions({
      loggedIn: 'auth/loggedIn'
    }),

    async login() {
      const response = await this.us.authorizeUser(this.form);
      if (`userData` in response)
        await this
            .loggedIn({token: response.jsonAuth, data: response.userData})
            .then(() => this.$router.push('/profile'));
      else {
        this.errorMsg = response.error;
        setTimeout(() => this.errorMsg = '', 5000);
      }
    },
  }
})
</script>

<style scoped>
.sign_in_page {
  width: 500px;
  height: 60%;
  margin: auto 0;
}

.buttons {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.buttons button {
  margin: 20px 10px;
  padding: 50px;
}

#li, #pi {
  margin-top: 10px;
  margin-bottom: 30px;
}

.link {
  color: #688296;
  margin: 20px 40px 25px 20px;
}
</style>