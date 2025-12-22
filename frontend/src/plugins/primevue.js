import PrimeVue from 'primevue/config'
import 'primeicons/primeicons.css'

// Forms (PrimeVue Forms)
import { Form } from '@primevue/forms'

// UI
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import Password from 'primevue/password'
import Checkbox from 'primevue/checkbox'
import RadioButton from 'primevue/radiobutton'
import Dropdown from 'primevue/dropdown'
import Calendar from 'primevue/calendar'
import Textarea from 'primevue/textarea'
import InputNumber from 'primevue/inputnumber'
import ToggleSwitch from 'primevue/toggleswitch'

// Layout
import Card from 'primevue/card'
import Fieldset from 'primevue/fieldset'

// Data
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Paginator from 'primevue/paginator'

// Overlay
import Dialog from 'primevue/dialog'
import ConfirmDialog from 'primevue/confirmdialog'
import Sidebar from 'primevue/sidebar'
import Toast from 'primevue/toast'

// Menu
import Menubar from 'primevue/menubar'
import Menu from 'primevue/menu'
import TieredMenu from 'primevue/tieredmenu'
import Breadcrumb from 'primevue/breadcrumb'

// Services
import ToastService from 'primevue/toastservice'
import ConfirmationService from 'primevue/confirmationservice'

// Theme
import Aura from '@primeuix/themes/aura'

export default {
  install(app) {
    app.use(PrimeVue, { ripple: true })
    app.use(ToastService)
    app.use(ConfirmationService)

    // Forms
    app.component('Form', Form)

    // UI
    app.component('Button', Button)
    app.component('InputText', InputText)
    app.component('Password', Password)
    app.component('Checkbox', Checkbox)
    app.component('RadioButton', RadioButton)
    app.component('Dropdown', Dropdown)
    app.component('Calendar', Calendar)
    app.component('Textarea', Textarea)
    app.component('InputNumber', InputNumber)
    app.component('ToggleSwitch', ToggleSwitch)

    // Layout
    app.component('Card', Card)
    app.component('Fieldset', Fieldset)

    // Data
    app.component('DataTable', DataTable)
    app.component('Column', Column)
    app.component('Paginator', Paginator)

    // Overlay
    app.component('Dialog', Dialog)
    app.component('ConfirmDialog', ConfirmDialog)
    app.component('Sidebar', Sidebar)
    app.component('Toast', Toast)

    // Menu
    app.component('Menubar', Menubar)
    app.component('Menu', Menu)
    app.component('TieredMenu', TieredMenu)
    app.component('Breadcrumb', Breadcrumb)
  }
}
