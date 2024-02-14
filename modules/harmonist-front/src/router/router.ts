import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router';

const routes: RouteRecordRaw[] = [
    {
        path: '/',
        name: 'Sign In',
        component: () => import('@/pages/LoginPage.vue')
    },
    {
        path: '/register',
        name: 'Sign Up',
        component: () => import('@/pages/RegisterPage.vue')
    },
    {
        path: '/profile',
        name: 'User',
        component: () => import('@/pages/UserPage.vue')
    },
    {
        path: '/online',
        name: 'Online robot',
        component: () => import('@/pages/InteractivePage.vue')
    },
    {
        path: '/script',
        name: 'Scripts',
        component: () => import('@/pages/ScriptPage.vue')
    },
    {
        path: '/:catchAll(.*)',
        name: 'PageNotFound',
        component: () => import('@/pages/ErrorNotFound.vue')
    },
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

export default router