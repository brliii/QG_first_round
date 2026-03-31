<template>
  <div style="padding: 20px;">
    <h2>我的报修单</h2>
    <div v-for="order in orders" :key="order.id" style="border:1px solid #ccc; margin:10px 0; padding:10px;">
      <div>设备：{{ order.deviceType }}</div>
      <div>描述：{{ order.description }}</div>
      <div>状态：{{ statusMap[order.status] }}</div>
      <div>优先级：{{ priorityMap[order.priority] }}</div>
      <div v-if="order.imageUrl">图片：<a v-if="order.imageUrl" :href="'http://localhost:8080' + order.imageUrl" target="_blank">查看图片</a>
        <span v-else>无图片</span></div>
      <button v-if="order.status === 0" @click="cancelOrder(order.id)">取消</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'

const orders = ref([])
const statusMap = { 0: '待处理', 1: '处理中', 2: '已完成', 3: '已取消' }
const priorityMap = { 1: '低', 2: '中', 3: '高' }

const fetchOrders = async () => {
  const res = await request.get('/api/order/user/orders')
  if (res.code === 200) {
    orders.value = res.data
  } else {
    alert('获取报修单失败')
  }
}

const cancelOrder = async (orderId) => {
  try {
    const res = await request.put(`/api/order/${orderId}/cancel`)
    if (res.code === 200) {
      alert('取消成功')
      fetchOrders()  // 刷新列表
    } else {
      alert(res.msg || '取消失败')
    }
  } catch (err) {
    alert('请求失败')
  }
}

onMounted(fetchOrders)
</script>