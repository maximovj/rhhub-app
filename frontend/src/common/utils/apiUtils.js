export function apiExecutarV1({
  tryFetch: {
    url,
    data,
    query,
    headers,
  } = {},

  alertFetch = {
    showSuccess: true,
    titleSuccess: "Aviso",
    messageSuccess: "Petición realizada exitosamente.",
    showError: true,
    titleError: "Aviso",
    messageError: "Hubo un error en el servicio.",
  },

  catchFn,
  finallyFn,
} = {}) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      try {
        if (alertFetch.showSuccess) {
          window.$alert.alert({
            title: alertFetch.titleSuccess,
            message: alertFetch.messageSuccess
          });
        }

        resolve({
          status: "success",
          message: "Correctamente."
        });

      } catch (error) {

        if (alertFetch.showError) {
          window.$alert.alert({
            title: alertFetch.titleError,
            message: alertFetch.messageError
          });
        }

        if (typeof catchFn === "function") {
          catchFn(error);
        } else {
          reject({
            status: "error",
            message: error.message
          });
        }

      } finally {

        if (typeof finallyFn === "function") {
          finallyFn();
        }

      }
    }, 4500);
  });
}

export function apiExecutarV2({
  data = null,
  showSuccess = true,
  successMessage = "Petición realizada exitosamente.",
  errorMessage = "Hubo un error en el servicio.",
  finallyFn
} = {}) {

  return new Promise((resolve, reject) => {

    setTimeout(() => {
      try {

        if (showSuccess) {
          window.$alert.alert({
            title: "Aviso",
            message: successMessage
          });
        }

        resolve({
          status: "success",
          data
        });

      } catch (error) {

        window.$alert.alert({
          title: "Error",
          message: errorMessage
        });

        reject({
          status: "error",
          message: error.message
        });

      } finally {

        if (typeof finallyFn === "function") {
          finallyFn();
        }

      }
    }, 4500);

  });
}

export async function apiExecutarV3({
  data = null,
  showSuccess = true,
  successMessage = "Petición realizada exitosamente.",
  errorMessage = "Hubo un error en el servicio."
} = {}) {

  try {

    await new Promise(r => setTimeout(r, 4500));

    if (showSuccess) {
      window.$alert.alert({
        title: "Aviso",
        message: successMessage
      });
    }

    return { status: "success", data };

  } catch (error) {

    window.$alert.alert({
      title: "Error",
      message: errorMessage
    });

    throw error;
  }
}
