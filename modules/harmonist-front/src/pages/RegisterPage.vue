<template>
  <div class = "sign_up_page">
    <common-form>
      <error-message v-show="hasError">{{errorMsg}}</error-message>
      <label-input class="in" type="text" label="Login" placeholder="Enter Login" v-model="form.login"/>
      <label-input class="in" type="password" label="Password" placeholder="Enter Password" v-model="form.password" />
      <label-input class="in" type="email" label="Email" placeholder="Enter Email" v-model="form.email" />
      <label-input class="in" type="phone" label="Phone" placeholder="Enter Phone" v-model="form.phone" />
      <div class="buttons">
        <header-link class="link" to="/">SignIn</header-link>
        <my-button class="button" @click="register">Register</my-button>
      </div>
    </common-form>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import HeaderLink from "@/components/header/HeaderLink.vue";
import MyButton from "@/components/UI/primitives/MyButton.vue";
import ErrorMessage from "@/components/UI/primitives/ErrorMessage.vue";
import errorMixin from "@/components/mixins/errorMixin";
import {UserService} from "@/services/UserService";
import {mapActions} from "vuex";

export default defineComponent({
  name: "RegisterPage",
  components: {HeaderLink, MyButton, ErrorMessage},
  mixins: [errorMixin],
  data() {
    return {
      form: {
        login: '',
        password: '',
        email: '',
        phone: ''
      },
    }
  },
  computed: {
    us: () => new UserService(),
  },
  methods: {
    ...mapActions({
      loggedIn: 'auth/loggedIn'
    }),

    async register() {
      const response = await this.us.registerUser(this.form);
      if (`jsonAuth` in response)
        await this
            .loggedIn({token: response.jsonAuth, username: response.userData})
            .then(() => this.$router.push('/profile'));
      else
        this.errorMsg = response.error;
    },
  }
})
</script>

<style scoped>
.sign_up_page {
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

.in {
  margin-top: 10px;
  margin-bottom: 30px;
}

.link {
  color: #688296;
  margin: 20px 40px 25px 20px;
}
</style>