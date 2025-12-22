// src/plugins/common.js
import { defineAsyncComponent } from 'vue'

// Importar utils y constantes globales
import * as utils from '@/common/utils'
import * as constants from '@/common/constants'

export default {
  install(app) {
    // 1️⃣ Componentes normales (carga estática)
    const normalComponents = import.meta.glob('@/common/components/*.vue', { eager: true })
    for (const path in normalComponents) {
      const component = normalComponents[path].default
      const name = path.split('/').pop().replace('.vue', '')
      app.component(name, component)
    }

    // 2️⃣ Componentes grandes / lazy (carga dinámica)
    const lazyComponents = import.meta.glob('@/common/components/*.vue')
    for (const path in lazyComponents) {
      const name = path.split('/').pop().replace('.vue', '')

      // Evitar sobrescribir los que ya se cargaron como normales
      if (!(normalComponents[path])) {
        app.component(name, defineAsyncComponent(lazyComponents[path]))
      }
    }

    // 3️⃣ Iconos (carga dinámica)
    const icons = import.meta.glob('@/common/icons/*.vue')
    for (const path in icons) {
      const name = path.split('/').pop().replace('.vue', '')
      app.component(name, defineAsyncComponent(icons[path]))
    }

    // 4️⃣ Utils y constantes globales
    app.config.globalProperties.$utils = utils
    app.config.globalProperties.$constants = constants
  }
}
