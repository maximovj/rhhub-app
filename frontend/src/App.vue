<script setup>
import AlertCustom from "@/common/components/AlertCustom.vue";
import { useUiStore } from "@/common/stores/uiStore";

const ui = useUiStore();
</script>

<template>
  <router-view />

  <!-- Alerta global (controlada por alertStore) -->
  <AlertCustom class="fixed inset-0 bg-black/40 backdrop-blur-sm z-[9999]" />

  <!-- Loading global -->
  <div
    v-if="ui.loading"
    class="fixed inset-0 bg-black/40 backdrop-blur-sm z-[9999] grid place-items-center"
  >
    <div class="flex flex-col items-center gap-3">
      <ProgressSpinner
        style="width:50px;height:50px"
        strokeWidth="4"
        animationDuration=".7s"
      />
      <span class="text-white font-semibold text-sm">
        Cargando...
      </span>
    </div>
  </div>
</template>
