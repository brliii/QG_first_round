<template>
  <div style="padding: 20px; max-width: 300px; margin: 0 auto;">
    <h2>注册</h2>
    <input v-model="name" placeholder="姓名" />
    <input v-model="username" placeholder="学号/工号" />
    <input v-model="password" type="password" placeholder="密码" />
    <input v-model="confirmPassword" type="password" placeholder="确认密码" />
    <select v-model="role">
      <option value="1">学生</option>
      <option value="2">管理员</option>
    </select>
    <button @click="register">注册</button>
    <p>已有账号？<router-link to="/login">登录</router-link></p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

const name = ref('')
const username = ref('')
const password = ref('')
const confirmPassword = ref('')
const role = ref(1)
const router = useRouter()

const register = async () => {
  try {
    const res = await request.post('/api/user/register', {
      name: name.value,
      username: username.value,
      password: password.value,
      confirmPassword: confirmPassword.value,
      role: role.value
    })
    if (res.code === 200) {
      alert('注册成功，请登录')
      router.push('/login')
    } else {
      alert(res.msg || '注册失败')
    }
  } catch (err) {
    alert('请求失败')
  }
}
</script>