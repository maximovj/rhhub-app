import { createRouter, createWebHistory } from 'vue-router'
import { useSettingsStore } from '@/common/stores/settingsStore'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/acceder',
      name: 'acceder',
      component: () => import('@/views/AccederView.vue'),
      meta: { requiresGuest: true }
    },
    {
      path: '/panel',
      name: 'panel',
      component: () => import('@/views/PanelView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/usuarios',
      name: 'usuarios',
      component: () => import('@/views/UsuariosView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/configuracion',
      name: 'configuracion',
      component: () => import('@/views/ConfiguracionView.vue'),
      meta: { requiresAuth: true }
    },
    { path: '/:pathMatch(.*)*', redirect: '/acceder'},
  ],
})

// Guard para rutas protegidas usando Pinia
router.beforeEach((to, from, next) => {
  const settings = useSettingsStore()
  
  // Si la ruta requiere autenticación y no está logueado
  if (to.meta.requiresAuth && !settings.estaLogueado) {
    next({name: 'acceder'})
  }
  // Si la ruta es para invitados (login) y ya está autenticado
  else if (to.meta.requiresGuest && settings.estaLogueado) {
    next({name: 'panel'})
  }
  // En cualquier otro caso, permitir navegación
  else {
    next()
  }
})

export default router
