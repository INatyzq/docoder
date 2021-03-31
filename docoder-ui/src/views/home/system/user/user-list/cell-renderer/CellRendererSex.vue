<template>
    <vs-chip class="ag-grid-cell-chip">
      <span>{{ params.value==1?'男':'女' }}</span>
    </vs-chip>
</template>
<script>
    export default {
        name: 'CellRendererSex'
    }
</script>
