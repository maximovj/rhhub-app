// common/api/api.js
import axios from 'axios'
import { useSettingsStore } from '@/common/stores/settingsStore'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1',
  withCredentials: true // 🔥 NECESARIO para cookies HttpOnly
})

// ---------------- REQUEST ----------------
api.interceptors.request.use(config => {
  const store = useSettingsStore()
  if (store.acceso_token) {
    config.headers.Authorization = `Bearer ${store.acceso_token}`
  }
  return config
})

// ---------------- RESPONSE ----------------
api.interceptors.response.use(
  response => response,
  async error => {
    const store = useSettingsStore()
    const originalRequest = error.config

    /*
    if (
      error.config?.url != "/autenticacion/login" && 
      error.response?.status === 401 &&
      !originalRequest._retry
    ) {
      originalRequest._retry = true

      try {
        const res = await api.post('/autenticacion/refresh')

        const nuevoToken = res.data.contenido.acceso_token
        store.setAccesoToken(nuevoToken)

        originalRequest.headers.Authorization = `Bearer ${nuevoToken}`
        return api(originalRequest)

      } catch (e) {
        store.desloguearse()
        return Promise.reject(e)
      }
    }
    */

    return Promise.reject(error)
  }
)

export default api
