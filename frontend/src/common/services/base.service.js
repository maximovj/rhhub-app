// common/services/base.service.js
import api from '@/common/api/apiAxios'
import { apiExecutor } from '@/common/api/apiExecutor'
const HEADER_KEY = import.meta.env.VITE_APP_HEADER_KEYNAME
const HEADER_VALUE = import.meta.env.VITE_APP_HEADER_KEYVALUE

export default class BaseService {
  constructor(baseURL) {
    this.baseURL = baseURL
  }

  getAll(config = {}) {
    const headers = { [HEADER_KEY]: HEADER_VALUE, ...config.headers }
    return apiExecutor(() => api.get(this.baseURL, { ...config, headers }))
  }

  getById(id, config = {}) {
    const headers = { [HEADER_KEY]: HEADER_VALUE, ...config.headers }
    return apiExecutor(() => api.get(`${this.baseURL}/${id}`, { ...config, headers }))
  }

  create(data, config = {}) {
    const headers = { [HEADER_KEY]: HEADER_VALUE, ...config.headers }
    return apiExecutor(() => api.post(this.baseURL, data, { ...config, headers }))
  }

  update(id, data, config = {}) {
    const headers = { [HEADER_KEY]: HEADER_VALUE, ...config.headers }
    return apiExecutor(() => api.put(`${this.baseURL}/${id}`, data, { ...config, headers }))
  }

  delete(id, config = {}) {
    const headers = { [HEADER_KEY]: HEADER_VALUE, ...config.headers }
    return apiExecutor(() => api.delete(`${this.baseURL}/${id}`, { ...config, headers }))
  }

  custom(method, url, data = null, config = {}) {
    const headers = { [HEADER_KEY]: HEADER_VALUE, ...config.headers }
    const requestConfig = { method, url: this.baseURL + url, ...config, headers }

    if (method.toLowerCase() === 'get' && data) {
      requestConfig.params = data
    } else if (data) {
      requestConfig.data = data
    }

    return apiExecutor(() => api.request(requestConfig))
  }

}
