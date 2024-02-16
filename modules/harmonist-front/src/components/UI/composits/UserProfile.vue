<template>
  <div>

  </div>
</template>

<script lang="ts">
import router from "@/router/router";
import {mapActions} from "vuex";

export default {
  name: "UserProfile",
  props: {
    username: {
      type: String,
      required: true
    },
    goToChannels: {
      type: Boolean,
      default: true,
    },
    goToEvents: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    ...mapActions({
      loggedOut: 'auth/loggedOut',
    }),
    async logout() {
      await this.loggedOut()
          .then(() => router.push('/'))
    },
    async visitChannels() {
      await router.push("/user/channel");
    },
    async visitEvents() {
      await router.push("/user/event")
    }
  }
}
</script>

<style scoped>

</style>