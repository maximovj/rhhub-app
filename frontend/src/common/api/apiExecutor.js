// common/api/apiExecutor.js
export async function apiExecutor(apiCall, options = {}) {
  const {
    onSuccess,
    onError,
    onFinally,
    defaultErrorMessage = 'Ocurrió un error inesperado'
  } = options

  try {
    const response = await apiCall()
    onSuccess?.(response)
    return response
  } catch (error) {
    const mensaje =
      error?.response?.data?.mensaje ||
      error?.response?.data?.razon ||
      defaultErrorMessage

    onError?.(mensaje, error)
    throw error
  } finally {
    onFinally?.()
  }
}
