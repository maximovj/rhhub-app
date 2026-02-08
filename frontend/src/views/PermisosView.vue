<template>
  <PlantillaBase>
  <div class="card">

    <!-- Header -->
    <CustomHeaderPagina
      title="Gestión de Permisos"
      subtitle="Resumen general de la información"
    >
      <template #actions>
        <Button label="Nuevo" icon="pi pi-plus" />
        <Button icon="pi pi-refresh" outlined />
        <!-- Botón menú -->
          <Button
          icon="pi pi-ellipsis-v"
          class="p-button-sm p-button-text"
          @click="toggleMenu"
          aria-haspopup="true"
          aria-controls="menu_header"
          />

          <!-- Popup Menu -->
          <Menu
          id="menu_header"
          ref="menu"
          :model="menuItems"
          popup
          />
      </template>
    </CustomHeaderPagina>

    <!-- 🔎 Barra de filtros - Expandible -->
    <CustomCardFiltros v-model="filtersVisible" :filters="activeFilters">
      <template #title>
        <i class="pi pi-filter  text-primary" />
        <h4 class="text-sm font-medium">Filtros de búsqueda</h4>
      </template>

      <form @submit.prevent="aplicarBusqueda">
          <div v-if="filtersVisible" class="flex flex-col gap-4">
            <!-- Filtros -->
            
              <div class="grid grid-cols-1 md:grid-cols-3 gap-3">
                
                <span class="p-input-icon-left w-full">
                  <span><i class="pi pi-search" /> <span class="font-bold text-sm">Nombre</span></span>
                  <InputText
                    v-model="searchForm.nombre"
                    placeholder="Buscar por nombre..."
                    class="w-full"
                  />
                </span>

                <span class="p-input-icon-left w-full">
                  <span><i class="pi pi-box" /> <span class="font-bold text-sm">Módulo</span></span>
                  <InputText
                    v-model="searchForm.modulo"
                    placeholder="Buscar por módulo..."
                    class="w-full"
                  />
                </span>

                <span class="p-input-icon-left w-full">
                  <span><i class="pi pi-tag" /> <span class="font-bold text-sm">Estado</span></span>
                  <Select
                    v-model="searchForm.estado"
                    :options="estados"
                    optionLabel="label"
                    optionValue="value"
                    placeholder="Seleccionar estado..."
                    showClear
                    class="w-full"
                  >
                    <!-- Templates existentes -->
                  </Select>
                </span>

                <span class="w-full">
                  <span>
                    <i class="pi pi-calendar" />
                    <span class="font-bold text-sm">Fecha creación (Rango)</span>
                  </span>

                  <DatePicker
                    v-model="searchForm.fechaCreacion"
                    selectionMode="range"
                    dateFormat="yy-mm-dd"
                    showIcon
                    class="w-full"
                  />
                </span>

              </div>
            

            <!-- Acciones -->
            <div class="flex justify-between items-center">
              <Button
                label="Limpiar todo"
                icon="pi pi-filter-slash"
                severity="secondary"
                text
                size="small"
                @click="clearFilters"
              />

              <!-- Badges de filtros activos (interactivos) -->
              <CustomFiltrosActivos :filters="activeFilters" />

              <div class="flex gap-2">
                <Button
                  label="Buscar"
                  icon="pi pi-search"
                  severity="secondary"
                  size="small"
                  type="submit"
                />
              </div>
            </div>
          </div>
      </form>
    </CustomCardFiltros> 

    <!-- 📋 Tabla -->
    <GenericDataTable
      :value="permisos"
      :filters="filters"
      v-model:rows="rows"
    >
      <Column field="id" header="ID" sortable style="width: 80px" />
      <Column field="nombre" header="Nombre" sortable />
      <Column field="modulo" header="Módulo" sortable />
      <Column field="estado" header="Estado" sortable>
        <template #body="{ data }">
          <Tag
            :value="data.estado"
            :severity="getEstadoSeverity(data.estado)"
          />
        </template>
      </Column>
      <Column field="fechaCreacion" header="Fecha Creación" sortable>
        <template #body="{ data }">
          {{ formatDate(data.fechaCreacion) }}
        </template>
      </Column>
      <Column header="Acciones" style="width: 180px">
        <template #body="{ data }">
          <div class="flex gap-2">
            <!-- Editar -->
            <Button
              icon="pi pi-pencil"
              severity="warning"
              rounded
              text
              @click="editarPermiso(data)"
            />

            <!-- Eliminar -->
            <Button
              icon="pi pi-trash"
              severity="danger"
              rounded
              text
              @click="eliminarPermiso(data)"
            />
          </div>
        </template>
      </Column>
    </GenericDataTable>
    
  </div>
  </PlantillaBase>
</template>


<script>
import { FilterMatchMode } from '@primevue/core/api'

import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import Select from 'primevue/select'
import Tag from 'primevue/tag'
import { now } from '@vueuse/core'

export default {
  name: 'GestionPermisos',

  components: {
    DataTable,
    Column,
    InputText,
    Button,
    Select,
    Tag
  },

  data() {
    const fechaFin = new Date(now());
    const fechaInicio = new Date();
    fechaInicio.setFullYear(fechaFin.getFullYear() - 10);

    return {
      rowsDataTable: 10,
      permisos: [
        { id: 1, nombre: 'Ver Usuarios', modulo: 'Usuarios', estado: 'Activo', fechaCreacion: new Date('2024-01-10') },
        { id: 2, nombre: 'Crear Usuario', modulo: 'Usuarios', estado: 'Activo', fechaCreacion: new Date('2024-01-15') },
        { id: 3, nombre: 'Eliminar Usuario', modulo: 'Usuarios', estado: 'Inactivo', fechaCreacion: new Date('2024-02-01') },
        { id: 4, nombre: 'Ver Reportes', modulo: 'Reportes', estado: 'Activo', fechaCreacion: new Date('2024-02-10') },
        { id: 5, nombre: 'Exportar Reportes', modulo: 'Reportes', estado: 'Inactivo', fechaCreacion: new Date('2024-03-05') },
        { id: 6, nombre: 'Editar Perfil', modulo: 'Perfil', estado: 'Activo', fechaCreacion: new Date('2024-03-15') },
        { id: 7, nombre: 'Cambiar Contraseña', modulo: 'Perfil', estado: 'Activo', fechaCreacion: new Date('2024-04-01') },
        { id: 8, nombre: 'Acceso Dashboard', modulo: 'Dashboard', estado: 'Activo', fechaCreacion: new Date('2024-04-10') }
      ],

      searchForm: {
        nombre: null,
        modulo: null,
        estado: null,
        fechaCreacion: [fechaInicio, fechaFin]
      },

      filtersVisible: false,

      filters: {
        nombre: { value: null, matchMode: FilterMatchMode.CONTAINS },
        modulo: { value: null, matchMode: FilterMatchMode.CONTAINS },
        estado: { value: null, matchMode: FilterMatchMode.EQUALS },
        fechaCreacion: { 
          value: [fechaInicio, fechaFin],
          matchMode: FilterMatchMode.BETWEEN 
        }
      },

      estados: [
        { label: 'Activo', value: 'Activo', severity: 'success' },
        { label: 'Inactivo', value: 'Inactivo', severity: 'danger' }
      ],

      menu: null,

      menuItems: [
        { label: 'Editar', icon: 'pi pi-pencil' },
        { label: 'Eliminar', icon: 'pi pi-trash' },
        { separator: true },
        { label: 'Exportar', icon: 'pi pi-upload' }
      ]
    }
  },

  computed: {
  activeFilters() {
    return [
      this.searchForm.nombre && {
        icon: 'pi pi-search',
        key: 'nombre',
        label: 'Nombre',
        value: this.searchForm.nombre,
        onRemove: () => this.searchForm.nombre = null
      },
      this.searchForm.modulo && {
        icon: 'pi pi-box',
        key: 'modulo',
        label: 'Módulo',
        value: this.searchForm.modulo,
        onRemove: () => this.searchForm.modulo = null
      },
      this.searchForm.estado && {
        icon: 'pi pi-tag',
        key: 'estado',
        label: 'Estado',
        value: this.getEstadoLabel(this.searchForm.estado),
        onRemove: () => this.searchForm.estado = null
      },
      this.searchForm.fechaCreacion && 
      this.searchForm.fechaCreacion[0] && 
      this.searchForm.fechaCreacion[1] && {
        icon: 'pi pi-calendar',
        key: 'fechaCreacion',
        label: 'Fecha creación',
        value: `${this.formatDate(this.searchForm.fechaCreacion[0])} - ${this.formatDate(this.searchForm.fechaCreacion[1])}`,
        onRemove: () => this.searchForm.fechaCreacion = [null, null]
      }
    ].filter(Boolean)
  },
},

  methods: {
    toggleMenu(event) {
      this.$refs.menu.toggle(event)
    },

    aplicarBusqueda() {
      this.filters.nombre.value = this.searchForm.nombre
      this.filters.modulo.value = this.searchForm.modulo
      this.filters.estado.value = this.searchForm.estado
      this.filters.fechaCreacion.value = this.searchForm.fechaCreacion
    },

    clearFilters() {
      this.searchForm = {
        nombre: null,
        modulo: null,
        estado: null
      }
      this.aplicarBusqueda()
    },

    getEstadoSeverity(value) {
      const estado = this.estados.find(e => e.value === value)
      return estado ? estado.severity : null
    },

    getEstadoLabel(value) {
      const estado = this.estados.find(e => e.value === value)
      return estado ? estado.label : value
    },

    editarPermiso(permiso) {
      console.log('Editar:', permiso)
    },

    eliminarPermiso(permiso) {
      console.log('Eliminar:', permiso)
    },

    crearPermiso() {
      console.log('Crear nuevo permiso')
    },

    formatDate(date) {
      if (!date) return ''
      return date.toLocaleDateString()
    }
  }
}
</script>

<style scoped>
.card {
  padding: 1.5rem;
}
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.2s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
.p-chip {
  font-size: 0.75rem;
}
</style>