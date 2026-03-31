import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import Register from "@/views/Register.vue";
import StudentHome from '@/views/StudentHome.vue'
import StudentOrders from '@/views/StudentOrders.vue'
import AdminOrders from '@/views/AdminOrders.vue'

const routes = [
    {path: '/', redirect: '/login' },
    {path: '/login', component: Login },
    {path: '/register', component: Register },
    {path: '/student', component: StudentHome, meta: { requiresAuth: true, role: 1 } },
    {path: '/student/orders', component: () => import('@/views/StudentOrders.vue'), meta: { requiresAuth: true, role: 1 } },
    {path: '/admin', component: AdminOrders,meta:{requiresAuth:true,role: 2}}
]


const router = createRouter({
    history: createWebHistory(),
    routes
})

// 简单路由守卫：检查 token
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    if (to.meta.requiresAuth && !token) {
        next('/login')
    } else {
        next()
    }
})

export default router