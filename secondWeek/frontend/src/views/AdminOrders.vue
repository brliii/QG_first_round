<template>
  <div style="padding: 20px;">
    <h2>报修单管理</h2>
    <select v-model="filterStatus">
      <option value="">全部</option>
      <option value="0">待处理</option>
      <option value="1">处理中</option>
      <option value="2">已完成</option>
      <option value="3">已取消</option>
    </select>
    <select v-model="filterPriority">
      <option value="">全部优先级</option>
      <option value="1">低</option>
      <option value="2">中</option>
      <option value="3">高</option>
    </select>
    <button @click="fetchOrders">查询</button>
  </div>

    <div v-for="order in orders" :key="order.id" style="border:1px solid #ccc; margin:10px 0; padding:10px;">
      <div>ID: {{ order.id }} - 用户: {{ order.userId }}</div>
      <div>设备：{{ order.deviceType }}</div>
      <div>描述：{{ order.description }}</div>
      <div>状态：{{ statusMap[order.status] }}</div>
      <div>优先级：{{ priorityMap[order.priority] }}</div>
      <div v-if="order.imageUrl">图片：<a v-if="order.imageUrl" :href="'http://localhost:8080' + order.imageUrl" target="_blank">查看图片</a>
        <span v-else>无图片</span></div>
      <select v-model="order.newStatus" @change="updateStatus(order.id, order.newStatus)">
        <option v-for="(label, val) in statusMap" :value="val">{{ label }}</option>
      </select>
      <button @click="deleteOrder(order.id)" style="background-color: #ff4d4f; color: white; border: none; padding: 4px 8px; border-radius: 4px;">删除</button>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'

const orders = ref([])
const filterStatus = ref('')
const filterPriority = ref('')
const statusMap = { 0: '待处理', 1: '处理中', 2: '已完成', 3: '已取消' }
const priorityMap = { 1: '低', 2: '中', 3: '高' }

const fetchOrders = async () => {
  const params = {}
  if (filterStatus.value !== '') params.status = filterStatus.value
  if (filterPriority.value !== '') params.priority = filterPriority.value
  const res = await request.get('/api/order/admin/list', { params })
  if (res.code === 200) {
    orders.value = res.data.map(o => ({ ...o, newStatus: o.status }))
  }
}

const updateStatus = async (orderId, newStatus) => {
  const res = await request.put(`/api/order/admin/status/${orderId}`, null, {
    params: { newStatus }
  })
  if (res.code === 200) {
    alert('状态更新成功')
    fetchOrders()
  } else {
    alert(res.msg)
  }
}

const deleteOrder = async (orderId) => {
  if (confirm('确定删除吗？')) {
    const res = await request.delete(`/api/order/admin/${orderId}`)
    if (res.code === 200) {
      alert('删除成功')
      fetchOrders()
    } else {
      alert(res.msg)
    }
  }
}
</script>