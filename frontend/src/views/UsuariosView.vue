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

    <!-- Tabla -->
    <div class="bg-white dark:bg-gray-800 p-4 rounded">
          <h2 class="text-md font-medium mb-2">Usuarios recientes</h2>

          <DataTable :value="users" class="p-datatable-sm p-datatable-striped
         bg-gray-800 text-gray-200">
            <Column field="id" header="ID" />
            <Column field="name" header="Nombre" />
            <Column field="email" header="Email" />
            <Column field="role" header="Rol" />
            <Column header="Acciones">
              <template #body>
                <Button icon="pi pi-pencil" class="p-button-text p-button-info mr-2" />
                <Button icon="pi pi-trash" class="p-button-text p-button-danger" />
              </template>
            </Column>
          </DataTable>
    </div>
  </PlantillaBase>
</template>

<script>
import { useAuthStore } from '@/stores/auth';

export default {
  // Definir propiedades
  data() {
    return {
      metaTitle: 'Usuarios',
      showIconToolbar: false,
      users: [
        { id: 1, name: 'Juan Pérez', email: 'juan@example.com', role: 'Admin' },
        { id: 2, name: 'María López', email: 'maria@example.com', role: 'Editor' },
        { id: 3, name: 'Carlos Sánchez', email: 'carlos@example.com', role: 'Usuario' },
      ]
    }
  },

  // Definir propiedades computadas
  computed: {
    route() {
      return this.$route
    }
  },

  // Definir métodos
  methods: {
    isActive(path) {
      return this.route.path.startsWith(path) ? 'p-button-primary' : ''
    },
    async salir() { 
      const desicion = await window.$alert.confirm({ 
        message: '¿Seguro que deseas salir?',
        buttons: {visible: ["yes", "cancel"] } 
      });

      if(desicion == 'yes') {
        const auth = useAuthStore();
        auth.logout();
        window.location.href = "/";
      }
    },
    async cargarUsuarios() {
      try {

        const data = await this.$utils.apiExecutar({
          url: "/api/users",
          method: "post",
          data: { size: 10, page: 0 },
          successMessage: "Usuarios cargados"
        });

        this.users = data;

      } catch (e) {
        console.error(e);
      }
    }
  },

  // Montaje
  mounted() {
    this.cargarUsuarios();
    this.$utils.setMetaTitleWelcome(this.metaTitle);
  }
}
</script>
