// common/services/permisos.service.js
import BaseService from '@/common/services/base.service'

class PermisosService extends BaseService {
  constructor() {
    super('/permisos')
  }
}

export default new PermisosService()