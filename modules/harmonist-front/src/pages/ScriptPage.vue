<template>
  <div class="script-page">
    <common-form class="form">
      <div class="data">
        <label-input id="ln" type="text" label="Name" placeholder="Enter Script Name" v-model="script.name"/>
        <label-input id="lv" type="text" label="Version" placeholder="Enter Version" v-model="script.version"/>
        <label-input id="los" type="text" label="OS" placeholder="Windows | Darwin" v-model="script.os"/>
      </div>

      <my-area class="desc" v-model="script.description">Description</my-area>
      <div class="buttons">
        <my-button :classes="'add'" @click="compileScript">Compile</my-button>
        <my-button :classes="'add'" @click="createScript">Create</my-button>
        <my-button :classes="'add'" @click="updateScript" :disabled="script.id == -1">Update</my-button>
        <my-button :classes="'add'" @click="clearScript">Clear</my-button>
      </div>
      <div v-if="hasError">
        <error-message v-if="hasError">{{errorMsg}}</error-message>
      </div>
      <textarea class="textarea" v-model="script.code" @keydown.tab.prevent="insertTab"></textarea>

      <common-list :not-empty=true class="list">
        <list-item v-for="(cur, index) in $data.script.inputData" :key="index" class="input-item">
          <flex-script-input-value :model-value="cur"/>
          <my-button :classes="'remove'" @click="() => removeInput(index)"/>
        </list-item>
        <my-button :classes="'add'" @click="script.inputData.push({name: 'name', type: 'int', value: 5})">Add Input</my-button>
      </common-list>
    </common-form>

    <separate-line class="separate"/>
    <my-label>Managing scripts</my-label>
    <common-form class="form">
      <common-list :not-empty="$data.execute != null && $data.execute.length > 0">
        <list-item v-for="(cur, index) in $data.execute" :key="index" class="item">
          <script-item :script="cur"/>
        </list-item>
      </common-list>
    </common-form>

    <separate-line class="separate"/>
    <my-label>Executing scripts</my-label>
    <common-form class="form">
      <common-list :not-empty="$data.execute != null && $data.execute.length > 0">
        <list-item v-for="(cur, index) in $data.execute" :key="index" class="item">
          <script-item :script="cur"/>
        </list-item>
      </common-list>
    </common-form>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {ManagingScript, ScriptData, ScriptInput, ScriptService} from "@/services/ScriptService";
import CommonForm from "@/components/UI/primitives/CommonForm.vue";
import CommonList from "@/components/UI/list/CommonList.vue";
import ListItem from "@/components/UI/list/ListItem.vue";
import ScriptItem from "@/components/UI/list/results/ScriptItem.vue";
import MyLabel from "@/components/UI/primitives/MyLabel.vue";
import SeparateLine from "@/components/UI/primitives/SeparateLine.vue";
import LabelInput from "@/components/UI/composits/LabelInput.vue";
import MyArea from "@/components/UI/composits/MyArea.vue";
import MyButton from "@/components/UI/primitives/MyButton.vue";
import errorMixin from "@/components/mixins/errorMixin";
import FlexScriptInputValue from "@/components/UI/list/results/FlexScriptInputValue.vue";
import ErrorMessage from "@/components/UI/primitives/ErrorMessage.vue";
import {RobotService} from "@/services/RobotService";

export default defineComponent({
  name: "ScriptPage",
  mixins: [errorMixin],
  components: {
    ErrorMessage,
    FlexScriptInputValue,
    MyButton, MyArea, LabelInput, SeparateLine, MyLabel, ScriptItem, ListItem, CommonList, CommonForm},
  computed: {
    ss: () => new ScriptService(),
    rs: () => new RobotService(),
  },
  data(): {script: ScriptData, execute: ScriptData[] | null, manage: ManagingScript[] | null} {
    return {
      script: {
        id: -1,
        code: "move 0.5 0.5",
        name: "",
        description: "",
        os: "Windows",
        version: "0.1",
        inputData: []
      },
      execute: null,
      manage: null
    }
  },
  async created() {
    this.$data.execute = await this.ss.getExecutingScripts() as ScriptData[]
    this.$data.manage =  await this.ss.getManagingScripts() as ManagingScript[]
    console.log(this.$data.manage)
  },
  methods: {
    async createScript() {
      const res = await this.ss.createScript(this.script);
      if ('id' in res) {
        this.script = res;
        this.$data.execute = await this.ss.getExecutingScripts() as ScriptData[]
        this.$data.manage = await this.ss.getManagingScripts() as ManagingScript[]
      } else {
        this.errorMsg = res.error;
        setTimeout(() => this.errorMsg = '', 5000);
      }
    },
    async updateScript() {
      const res = await this.ss.updateScript(this.script, this.script.id);
      if ('id' in res) {
        this.script = res;
      } else {
        this.errorMsg = res.error;
        setTimeout(() => this.errorMsg = '', 5000);
      }
    },
    async clearScript() {
      this.script.id = -1;
      this.script.name = '';
      this.script.description = '';
      this.script.inputData = [];
    },
    async compileScript() {
      const res = await this.rs.compileScript(this.script);
      console.log(res);
    },
    async removeInput(index: number) {
      this.$data.script.inputData.splice(index, 1);
    },

    insertTab(event: KeyboardEvent) {
      if (event.key === 'Tab') {
        event.preventDefault();

        const textarea = event.target as HTMLTextAreaElement;
        const { selectionStart, selectionEnd } = textarea;
        this.script.code = this.script.code.substring(0, selectionStart) + '\t' + this.script.code.substring(selectionEnd);
        this.$nextTick(() => {
          textarea.selectionStart = textarea.selectionEnd = selectionStart + 1;
        });
      }
    }
  }
})
</script>

<style scoped>
  .script-page {
    width: 80%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;
  }

  .data {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }

  .separate {
    margin: 40px 40px 25px;
  }

  .desc {
    width: 80%;
    margin-top: 20px
  }

  .textarea {
    width: 90%;
    height: 200px;

    background-color: #f8f8f8;
    font-size: 18px;

    padding: 5px;
    margin-top: 20px;
  }

  .list {
    margin-top: 20px;
    display: flex;
    align-items: start;
    justify-content: flex-start;
    flex-direction: column;
  }

  .buttons, .list {
    width: 90%;
  }

  .buttons {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }
  .buttons * {
    justify-content: space-between;
    margin: 20px;
  }

  .input-item {
    display: flex;
    flex-direction: row;
  }

  .input-item * {
    margin: 0px 10px;
  }

  .input {
    display: flex;
    flex-direction: row;
    align-items: flex-start;
  }
  .input * {
    margin: 0px 20px;
  }
</style>