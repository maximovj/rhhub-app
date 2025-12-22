<template>
  <div v-if="visible" class="custom-modal">
    <div class="custom-modal__panel">

      <!-- HEADER -->
      <div class="custom-modal__header" :style="{ background: headerColor }">
        <h5 class="custom-modal__title">{{ title }}</h5>
        <button class="custom-modal__close" @click="cancel()">×</button>
      </div>

      <!-- BODY -->
      <div class="custom-modal-body">
        <p class="modal-dup__single">{{ message }}</p>

        <!-- Prompt input -->
        <input 
          v-if="isPrompt"
          v-model="promptValue"
          class="prompt-input"
          type="text"
          :placeholder="promptPlaceholder"
        />
      </div>

      <!-- FOOTER -->
      <div class="custom-modal__footer">
        <!-- ALERT -->
        <button v-if="isAlert" class="btn-ok" @click="resolve(true)">ACEPTAR</button>

        <!-- CONFIRM: sí / no / cancelar -->
        <template v-if="isConfirm">
          
          <!-- Cancelar -->
          <button 
            v-if="confirmButtons.visible.cancel"
            class="btn-outline" 
            @click="resolve('cancel')"
          >
            {{ confirmButtons.cancel }}
          </button>

          <!-- No -->
          <button 
            v-if="confirmButtons.visible.no"
            class="btn-outline" 
            @click="resolve('no')"
          >
            {{ confirmButtons.no }}
          </button>

          <!-- Sí -->
          <button 
            v-if="confirmButtons.visible.yes"
            class="btn-ok" 
            @click="resolve('yes')"
          >
            {{ confirmButtons.yes }}
          </button>

        </template>

        <!-- PROMPT -->
        <template v-if="isPrompt">
          <button class="btn-outline" @click="resolve(null)">CANCELAR</button>
          <button class="btn-ok" @click="resolve(promptValue)">ACEPTAR</button>
        </template>
      </div>

    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      title: "",
      message: "",
      headerColor: "#91278F",
      resolver: null,
      
      // Control de tipo
      isAlert: false,
      isConfirm: false,
      isPrompt: false,

      // Prompt
      promptValue: "",
      promptPlaceholder: "",

      // Confirm buttons
      confirmButtons: {
        yes: "SI",
        no: "NO",
        cancel: "CANCELAR"
      }
    };
  },
  methods: {
    // === COMMON OPEN ===
    openBase(config) {
      this.visible = true;
      this.title = config.title || "";
      this.message = config.message || "";
      this.headerColor = config.headerColor || "#91278F"; // #0096ad

      return new Promise(resolve => {
        this.resolver = resolve;
      });
    },

    // === ALERT ===
    alert({ title = "Alerta", message, headerColor }) {
      this.isAlert = true;
      this.isConfirm = false;
      this.isPrompt = false;
      return this.openBase({ title, message, headerColor });
    },

    // === CONFIRM (con 3 botones y textos personalizados) ===
    confirm({
      title = "Confirmación",
      message,
      headerColor,
      buttons = {}
    }) {
      this.isAlert = false;
      this.isConfirm = true;
      this.isPrompt = false;

      // Visibilidad de botones
    const visibles = buttons.visible || ["yes", "no", "cancel"];

      // mezclar textos personalizados
      this.confirmButtons = {
        yes: buttons.yes || "SI",
        no: buttons.no || "NO",
        cancel: buttons.cancel || "CANCELAR",

        // Mostrar botones
        visible: {
          yes: visibles.includes("yes"),
          no: visibles.includes("no"),
          cancel: visibles.includes("cancel")
        }
      };

      return this.openBase({ title, message, headerColor });
    },

    // === PROMPT ===
    prompt({ 
      title = "INGRESAR VALOR", 
      message, 
      placeholder = "", 
      headerColor 
    }) {
      this.isAlert = false;
      this.isConfirm = false;
      this.isPrompt = true;

      this.promptValue = "";
      this.promptPlaceholder = placeholder;

      return this.openBase({ title, message, headerColor });
    },

    // === CLOSE ===
    resolve(value) {
      this.visible = false;
      if (this.resolver) {
        this.resolver(value);
        this.resolver = null;
      }
    },

    cancel() {
      // Cancelar según tipo:
      if (this.isConfirm) this.resolve(false);
      else if (this.isPrompt) this.resolve(null);
      else this.resolve(true); // alert only
    }
  }
};
</script>

<style scoped>
.custom-modal {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.45);
  display: grid;
  place-items: center;
  z-index: 10000;
}

.custom-modal__panel {
  width: min(380px, 90vw);
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.25);
  overflow: hidden;
  animation: modalIn 0.25s ease-out;
}

@keyframes modalIn {
  from { opacity: 0; transform: scale(0.9); }
  to { opacity: 1; transform: scale(1); }
}

.custom-modal__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  color: #fff;
}

.custom-modal__title {
  font-weight: 400;
  font-size: 1rem;
  margin: 0;
}

.custom-modal__close {
  border: none;
  background: transparent;
  color: #fff;
  font-size: 26px;
  cursor: pointer;
  line-height: 1;
}

.custom-modal-body {
  padding: 14px 16px;
  color: #4a4a59;
  text-align: center;
}

.modal-dup__single {
  margin: 0;
  font-size: 0.95rem;
  line-height: 1.55;
  white-space: pre-line;
}

.custom-modal__footer {
  padding: 12px;
  display: flex;
  justify-content: center;
  gap: 10px;
}

.btn-ok {
  background: #941c80;
  color: #fff;
  border: none;
  border-radius: 8px;
  padding: 10px 18px;
  font-weight: 700;
  cursor: pointer;
  transition: 0.2s;
}

.btn-outline {
  background: #fff;
  color: #941c80;
  border: 1px solid #941c80;
  border-radius: 8px;
  padding: 10px 18px;
  font-weight: 700;
  cursor: pointer;
}

.prompt-input {
  width: 90%;
  margin-top: 10px;
  padding: 8px 10px;
  border-radius: 6px;
  border: 1px solid #bbb;
  font-size: 0.9rem;
}
</style>
