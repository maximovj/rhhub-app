// common/stores/settingsStore.js
import { defineStore } from 'pinia'

export const useSettingsStore = defineStore('settings', {
    state: () => ({
        acceso_token: null,   // SOLO memoria
        usuario_id: null,
        usuario: null,
        grupo: null,
        rol: null,
        recuerdame: false,
        tema: 'light',
    }),

    getters: {
        estaLogueado: (state) => !!state.acceso_token,
        esAdministrador: (state) => state.rol === 'ADMIN',
    },

    actions: {
        loguearse(acceso_token, usuario_info, recuerdame) {
            this.acceso_token = acceso_token
            this.usuario_id = usuario_info?.usuario_id
            this.usuario = usuario_info?.usuario
            this.grupo = usuario_info?.grupo
            this.rol = usuario_info?.rol
            this.recuerdame = recuerdame
        },

        setAccesoToken(token) {
            this.acceso_token = token
        },

        desloguearse() {
            this.$reset()
        }
    },

    persist: {
        pick: [
            'usuario_id',
            'usuario',
            'grupo',
            'rol',
            'recuerdame',
            'tema',
        ],
        storage: localStorage
    }
})
