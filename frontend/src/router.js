import ProductListing from "./views/ProductListing.vue";
import AuthenticationPage from "./views/AuthenticationPage.vue";
import RegistrationPage from "./views/RegistrationPage.vue";
import BucketPage from "./views/BucketPage.vue";

import { createRouter, createWebHistory } from 'vue-router'

const routes = [
    {
        path: '/',
        name: 'ProductListing',
        component: ProductListing
    },
    {
        path: '/authentication',
        name: 'AuthenticationPage',
        component: AuthenticationPage
    },
    {
        path: '/registration',
        name: 'RegistrationPage',
        component: RegistrationPage
    },
    {
        path: '/bucket',
        name: 'BucketPage',
        component: BucketPage
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router