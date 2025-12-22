// common/constants/statusCodes.js

export const statusCodes = {
  SUCCESS: 200,
  CREATED: 201,
  NO_CONTENT: 204,
  BAD_REQUEST: 400,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  INTERNAL_ERROR: 500,
}

export const messages = {
  200: 'Operación exitosa',
  201: 'Recurso creado correctamente',
  204: 'Sin contenido',
  400: 'Solicitud incorrecta',
  401: 'No autorizado',
  403: 'Prohibido',
  404: 'No encontrado',
  500: 'Error interno del servidor',
}
