// common/services/usuarios.service.js
import BaseService from '@/common/services/base.service'
import { useUiStore } from '../stores/uiStore'
import { useAlertStore } from '../stores/alertStore'

class UsuariosService extends BaseService {
  constructor() {
    super('/usuarios')
  }

}

export default new UsuariosService()
