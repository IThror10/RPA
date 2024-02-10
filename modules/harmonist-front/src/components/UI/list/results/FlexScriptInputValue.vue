<template>
  <div class="input">
    <label-input v-model="input.name" label="Name" type="text"/>
    <label-input v-model="input.type" label="Type" type="text"/>
    <label-input v-model="input.value" label="Value" type="text"/>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType, ref, watch } from 'vue';
import { ScriptInput } from '@/services/ScriptService';
import LabelInput from '@/components/UI/composits/LabelInput.vue';

export default defineComponent({
  name: 'FlexScriptInputValue',
  components: { LabelInput },
  props: {
    modelValue: Object as PropType<ScriptInput>,
  },
  emits: ['update:modelValue'],
  setup(props, { emit }) {
    const input = ref(props.modelValue);

    watch(input, () => {
      emit('update:modelValue', input.value);
    });

    return { input };
  },
});
</script>

<style scoped>
  .input {
    display: flex;
    flex-direction: row;
    align-items: flex-start;
  }
  .input * {
    margin: 0px 20px;
  }
</style>