// common/services/grupos.service.js
import BaseService from '@/common/services/base.service';

class GruposService extends BaseService {
    constructor() {
        super('/grupos');
    }

}

export default new GruposService();