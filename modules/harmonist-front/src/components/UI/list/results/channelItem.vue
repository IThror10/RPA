<template>
  <div class="value">
    <my-label>{{group.name}}</my-label>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import MyLabel from "@/components/UI/primitives/MyLabel.vue";
import {GroupData} from "@/services/GroupService";
export default defineComponent({
  name: "ChannelItem",
  components: {MyLabel},
  props: {
    group: {
      type: Object as () => GroupData,
      required: true
    }
  }
})
</script>

<style scoped>
.value {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-wrap: wrap;
}

</style>