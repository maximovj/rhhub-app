<template>
  <router-view />
  <AlertCustom ref="alertRef" />
</template>

<script>

export default {
  mounted() {
    // Exponemos el alert globalmente
    window.$alert = this.$refs.alertRef;
  }
};
</script>
