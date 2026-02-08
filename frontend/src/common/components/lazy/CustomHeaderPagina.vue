<template>
  <header class="flex items-center justify-between mb-4">
    <div class="space-y-1">
      <h1 class="text-2xl font-semibold text-gray-800">
        {{ title }}
      </h1>
      <p class="text-sm text-gray-500">
        {{ subtitle }}
      </p>
    </div>

    <div class="flex items-center gap-2">
      <slot name="actions" />
    </div>
  </header>
</template>

<script>
export default {
  name: 'PageHeader',
  props: {
    title: String,
    subtitle: String
  }
}
</script>
