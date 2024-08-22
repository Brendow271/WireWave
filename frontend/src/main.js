import 'vuetify/styles'
import './styles/main.scss'

import { createApp } from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import router from "./router.js";

const app = createApp(App)

app.use(vuetify)
app.use(router)

app.mount('#app')