// common/stores/authStore.js
import { defineStore } from 'pinia'
import { useSettingsStore } from './settingsStore';
import autenticacionService from '../services/autenticacion.service';
import { useAlertStore } from './alertStore';

export const useAuthStore = defineStore('auth', { 
  state: () => ({
    acceso_token: null,
    usuario: null,
  }),
  actions: {
    async init() {
      const settings = useSettingsStore();
      
      if(settings.estaLogueado || settings.recuerdame) {
        const renovarToken = await autenticacionService.refresh();
        console.log("renovarToken", renovarToken);
        if(renovarToken?.data?.codigo == 200) {
          const alert = useAlertStore();
          await alert.alert({
            title: 'Acceso',
            message: 'Tienes un sessión aún activa',
          });

        }
      }

    }
  },
});