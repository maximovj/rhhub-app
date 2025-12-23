<script setup> </script>

<template>
  <div class="h-screen bg-gray-50 dark:bg-gray-900 text-gray-800 dark:text-gray-200">
    <div class="flex flex-col h-full">

      <!-- HEADER -->
      <header class="sticky top-0 z-40 relative">
        <slot name="header">
          <CabeceraPorDefecto />
        </slot>
      </header>

      <!-- MAIN -->
      <main class="p-6 flex-1 overflow-auto space-y-6">
        <slot>
          <ContenidoPorDefecto />
        </slot>
      </main>
    </div>
  </div>
</template>

<style> </style>