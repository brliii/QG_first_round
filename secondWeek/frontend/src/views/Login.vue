<template>
  <div style="padding: 20px; max-width: 300px; margin: 0 auto;">
    <h2>登录</h2>
    <input v-model="username" placeholder="学号/工号" style="width: 100%; margin-bottom: 10px;" />
    <input v-model="password" type="password" placeholder="密码" style="width: 100%; margin-bottom: 10px;" />
    <button @click="login" style="width: 100%;">登录</button>
    <p>没有账号？<router-link to="/register">注册</router-link></p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

const username = ref('')
const password = ref('')
const router = useRouter()

const login = async () => {
  try {
    const res = await request.post('/api/user/login', {
      username: username.value,
      password: password.value
    })
    if (res.code === 200) {
      localStorage.setItem('token', res.data.token)
      //根据角色跳转
      if (res.data.user.role === 1) {
        router.push('/student')
      } else {
        router.push('/admin')
      }
    } else {
      alert(res.msg || '登录失败')
    }
  } catch (err) {
    alert('请求失败，请检查后端是否启动')
  }
}
</script>