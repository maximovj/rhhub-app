<script setup>
import { ref } from 'vue'

const menu = ref()

const toggleMenu = (event) => {
  menu.value.toggle(event)
}

const menuItems = [
  {
    label: 'Editar',
    icon: 'pi pi-pencil'
  },
  {
    label: 'Eliminar',
    icon: 'pi pi-trash'
  },
  {
    separator: true
  },
  {
    label: 'Exportar',
    icon: 'pi pi-upload'
  }
]
</script>

<template>
    <!-- Header -->
    <header class="flex items-center justify-between">

    <!-- Título y subtítulo -->
    <div class="space-y-1">
        <h1 class="text-2xl font-semibold text-gray-800">
        Dashboard
        </h1>
        <p class="text-sm text-gray-500">
        Resumen general de la información
        </p>
    </div>

    <!-- Botonera + menú popup -->
    <div class="flex items-center gap-2">

        <!-- Botón acción -->
        <Button
        label="Nuevo"
        icon="pi pi-plus"
        class="p-button-sm"
        />

        <!-- Botón icono -->
        <Button
        icon="pi pi-refresh"
        class="p-button-sm p-button-outlined"
        aria-label="Actualizar"
        />

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
    </div>

    </header>

    <!-- Card -->
    <Card class="shadow-sm">
    <template #title>
        Información principal
    </template>

    <template #content>
        <p class="text-gray-600 mb-4">
        Aquí puedes mostrar datos importantes del sistema, métricas o mensajes.
        </p>

        <!-- Skeleton -->
        <div class="space-y-3">
        <Skeleton width="100%" height="1.5rem" />
        <Skeleton width="80%" height="1.5rem" />
        <Skeleton width="60%" height="1.5rem" />
        </div>
    </template>
    </Card>
</template>