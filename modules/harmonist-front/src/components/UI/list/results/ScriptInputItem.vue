<template>
  <div class="script">
    <my-label>{{input.name}}: {{input.type}}</my-label>
    <my-input placeholder="Value" :model-value="input.value"/>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {ScriptInput} from "@/services/ScriptService";
import MyLabel from "@/components/UI/primitives/MyLabel.vue";
import MyInput from "@/components/UI/primitives/MyInput.vue";

export default defineComponent({
  name: "ScriptInputItem",
  components: {MyInput, MyLabel},
  props: {
    input: {
      type: Object as () => ScriptInput,
      required: true
    }
  }
})
</script>

<style scoped>
  .script {
    display: flex;
  }
</style>