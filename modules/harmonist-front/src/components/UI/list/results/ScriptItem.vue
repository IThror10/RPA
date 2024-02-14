<template>
  <div>
    <label>
      <input type="checkbox" v-model="expanded" @change="toggleExpansion"> Раскрыть информацию
    </label>
    <div v-if="expanded">
      Дополнительно
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {ScriptData, ScriptInput} from "@/services/ScriptService";

export default defineComponent({
  name: "ScriptItem",
  props: {
    script: {
      type: Object as () => ScriptData,
      required: true
    }
  },
  data(): {expanded: boolean} {
    return {
      expanded: false
    }
  },
  methods: {
    toggleExpansion() {
      this.expanded = !this.expanded;
    }
  }
})
</script>

<style scoped>

</style>