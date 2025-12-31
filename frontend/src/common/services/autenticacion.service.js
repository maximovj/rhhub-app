// common/services/autenticacion.service.js
import BaseService from '@/common/services/base.service'
import { useSettingsStore } from '@/common/stores/settingsStore'
import { useUiStore } from '../stores/uiStore'
import { useAlertStore } from '../stores/alertStore'

class AutenticacionService extends BaseService {
  constructor() {
    super('/autenticacion')
  }

  async login(usuario, contrasena, recuerdame) {
    const ui = useUiStore();
    const store = useSettingsStore();
    const alert = useAlertStore();
    ui.loading = true;

    return this.custom('post', '/login', { usuario, contrasena, recuerdame })
      .then(res => {
        store.loguearse(
          res.data.contenido.acceso_token,
          res.data.contenido.info_usuario,
          recuerdame
        )
        return res
      })
      .catch( err => {
        alert.alert({ 
          title:'Iniciar sesión',
          message: err.response.data?.error || err.message,
        })
      })
      .finally(() => {
        ui.loading = false;
      })
  }

  async logout() {
    const store = useSettingsStore()
    return this.custom('post', '/logout', store.usuario)
      .finally(() => store.desloguearse())
  }

  refresh() {
    return this.custom('post', '/refresh')
  }
}

export default new AutenticacionService()
