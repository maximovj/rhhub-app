// common/utils/formatDate.js

/**
 * Formatea una fecha a "dd/mm/yyyy"
 * @param {Date | string} date - Fecha como objeto Date o string válido
 * @returns {string} Fecha formateada
 */
export function formatDate(date) {
  const d = new Date(date)
  if (isNaN(d)) return '' // Retorna vacío si la fecha es inválida
  const day = String(d.getDate()).padStart(2, '0')
  const month = String(d.getMonth() + 1).padStart(2, '0') // Mes empieza en 0
  const year = d.getFullYear()
  return `${day}/${month}/${year}`
}

/**
 * Formatea una fecha a "yyyy-mm-dd" (para inputs tipo date)
 */
export function formatDateISO(date) {
  const d = new Date(date)
  if (isNaN(d)) return ''
  const day = String(d.getDate()).padStart(2, '0')
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const year = d.getFullYear()
  return `${year}-${month}-${day}`
}
