<template>
  <PlantillaBase>
    <!-- Cards -->
    <div class="grid grid-cols-1 sm:grid-cols-3 gap-6">
      <div class="bg-white dark:bg-gray-800 p-4 rounded">
        <p class="text-gray-500 text-sm">Usuarios</p>
        <p class="text-xl font-semibold">1,245</p>
      </div>
      <div class="bg-white dark:bg-gray-800 p-4 rounded">
        <p class="text-gray-500 text-sm">Ventas</p>
        <p class="text-xl font-semibold">$34,210</p>
      </div>
      <div class="bg-white dark:bg-gray-800 p-4 rounded">
        <p class="text-gray-500 text-sm">Productos</p>
        <p class="text-xl font-semibold">320</p>
      </div>
    </div>
  </PlantillaBase>
</template>

<script>
export default {
  // Definir propiedades
  data() {
    return {
      metaTitle: 'Panel',
    }
  },

  // Definir propiedades computadas
  computed: {
   
  },

  // Definir métodos
  methods: {
    
  },

  // Montaje
  mounted() {
    this.$utils.setMetaTitleWelcome(this.metaTitle);
  }
}
</script>
