// common/services/base.service.js
import api from '@/common/api/apiAxios'
import { apiExecutor } from '@/common/api/apiExecutor'

export default class BaseService {
  constructor(baseURL) {
    this.baseURL = baseURL
  }

  getAll(config = {}) {
    return apiExecutor(() => api.get(this.baseURL, config))
  }

  getById(id, config = {}) {
    return apiExecutor(() => api.get(`${this.baseURL}/${id}`, config))
  }

  create(data, config = {}) {
    return apiExecutor(() => api.post(this.baseURL, data, config))
  }

  update(id, data, config = {}) {
    return apiExecutor(() => api.put(`${this.baseURL}/${id}`, data, config))
  }

  delete(id, config = {}) {
    return apiExecutor(() => api.delete(`${this.baseURL}/${id}`, config))
  }

  custom(method, url, data = null, config = {}) {
    return apiExecutor(() => api.request({ method, url: this.baseURL + url, data, ...config }))
  }
}
