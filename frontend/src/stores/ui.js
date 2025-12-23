// stores/ui.js
import { defineStore } from "pinia";

export const useUiStore = defineStore("ui", {
  state: () => ({
    loading: false
  }),
  actions: {
    startLoading() {
      setTimeout(()=>{}, 4500);
      this.loading = true;
    },
    stopLoading() {
      setTimeout(()=>{}, 4500);
      this.loading = false;
    }
  }
});
