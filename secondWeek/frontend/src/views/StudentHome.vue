<template>
  <div style="padding: 20px;">
    <h2>学生首页</h2>

    <!-- 未绑定时显示绑定表单 -->
    <div v-if="!bound">
      <h3>绑定宿舍</h3>
      <input v-model="building" placeholder="楼栋" />
      <input v-model="room" placeholder="房间号" />
      <button @click="bindDorm">绑定</button>
    </div>

    <!-- 已绑定时显示报修创建和修改宿舍 -->
    <div v-else>
      <h3>创建报修单</h3>
      <input v-model="deviceType" placeholder="设备类型" />
      <textarea v-model="description" placeholder="问题描述"></textarea>
      <select v-model="priority">
        <option value="1">低</option>
        <option value="2">中</option>
        <option value="3">高</option>
      </select>
      <input type="file" @change="handleFile" />
      <button @click="createOrder">提交报修</button>

      <!-- 修改宿舍按钮 -->
      <div style="margin-top: 20px;">
        <button @click="showBindForm = true">修改宿舍</button>
      </div>
    </div>

    <!-- 修改宿舍弹窗（或内嵌表单） -->
    <div v-if="showBindForm" style="margin-top: 20px; border: 1px solid #ccc; padding: 10px;">
      <h3>修改宿舍</h3>
      <input v-model="newBuilding" placeholder="新楼栋" />
      <input v-model="newRoom" placeholder="新房间号" />
      <button @click="updateDorm">确认修改</button>
      <button @click="showBindForm = false">取消</button>
    </div>

    <div style="margin-top: 20px;">
      <router-link to="/student/orders">我的报修单</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'

const bound = ref(false)
const building = ref('')
const room = ref('')
const deviceType = ref('')
const description = ref('')
const priority = ref('2')
const image = ref(null)

// 修改宿舍相关
const showBindForm = ref(false)
const newBuilding = ref('')
const newRoom = ref('')

// 获取用户信息，判断是否已绑定宿舍
const fetchUser = async () => {
  const res = await request.get('/api/user/info')
  if (res.code === 200) {
    bound.value = !!(res.data.dormBuilding && res.data.dormRoom)
    // 可预先填充当前宿舍信息（可选）
    newBuilding.value = res.data.dormBuilding || ''
    newRoom.value = res.data.dormRoom || ''
  }
}
onMounted(fetchUser)

// 初次绑定宿舍
const bindDorm = async () => {
  const token = localStorage.getItem('token')
  try {
    const res = await request.put('/api/user/bindDorm', null, {
      params: { building: building.value, room: room.value },
      headers: { 'Authorization': `Bearer ${token}` }
    })
    if (res.code === 200) {
      alert('绑定成功')
      bound.value = true
      building.value = ''
      room.value = ''
    } else {
      alert(res.msg || '绑定失败')
    }
  } catch (err) {
    alert('绑定失败，请检查网络')
  }
}

// 修改宿舍（复用绑定接口）
const updateDorm = async () => {
  const token = localStorage.getItem('token')
  try {
    const res = await request.put('/api/user/bindDorm', null, {
      params: { building: newBuilding.value, room: newRoom.value },
      headers: { 'Authorization': `Bearer ${token}` }
    })
    if (res.code === 200) {
      alert('修改成功')
      showBindForm.value = false
      // 重新获取用户信息，更新 bound 状态（其实不变）
      fetchUser()
    } else {
      alert(res.msg || '修改失败')
    }
  } catch (err) {
    alert('修改失败，请检查网络')
  }
}

const handleFile = (e) => {
  image.value = e.target.files[0]
}

const createOrder = async () => {
  const formData = new FormData()
  formData.append('deviceType', deviceType.value)
  formData.append('description', description.value)
  formData.append('priority', priority.value)
  if (image.value) {
    formData.append('image', image.value)
  }
  try {
    const res = await request.post('/api/order/create', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    if (res.code === 200) {
      alert('报修单创建成功')
      deviceType.value = ''
      description.value = ''
      priority.value = '2'
      image.value = null
    } else {
      alert(res.msg || '创建失败')
    }
  } catch (err) {
    alert('创建失败，请检查网络')
  }
}
</script>