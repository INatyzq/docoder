<template>
    <vs-chip class="ag-grid-cell-chip" :color="chipColor(params.value)">
      <span>{{ params.value == 1 ? '有效' : '无效' }}</span>
    </vs-chip>
</template>

<script>
export default {
  name: 'RoleCellRendererStatus',
  computed: {
    chipColor() {
      return (value) => {
        if (value === 1) return 'success'
        else return 'danger'
      }
    }
  }
}
</script>
