<template>
  <div>
    <template v-if="column.multiply">
      <el-table-column
        :resizable="config.table.resizable"
        :align="config.table.align"
        :prop="column.prop"
        :label="column.label"
        :sortable="column.sort"
        :width="column.width"
        :min-width="column.minWidth"
        :fixed="column.fixed"
        :show-overflow-tooltip="column.tooltip">
      <template v-for="(item,index) in column.columns">
        <LzMultiTableColumn
          :key="index"
          :column="item"
          :config="config"
          :data="data"
          @click="onTableColumnCellClick"
          @row-input-change="onTableRowInputChange"
          @row-checkbox-change="onTableRowCheckboxChange">
          {{item}}
        </LzMultiTableColumn>
      </template>
      </el-table-column>
    </template>
    <template v-else>
      <LzTableColumn
        :column="column"
        :config="config"
        :data="data"
        @click="onTableColumnCellClick"
        @row-input-change="onTableRowInputChange">
      </LzTableColumn>
    </template>
  </div>
</template>

<script>
import LzTableColumn from '@/components/lz/components/LzTableColumn'
export default {
  name: 'LzMultiTableColumn',
  components: {
    LzTableColumn
  },
  props: {
    column: {
      type: Object,
      default: function() {
        return {}
      }
    },
    config: {
      type: Object,
      default: function() {
        return {}
      }
    },
    data: {
      type: Array | Object,
      default: function() {
        return []
      }
    }
  },
  data: function() {
    return {

    }
  },
  computed: {},
  methods: {
    onTableRowInputChange(value, row, scope, column) {
      this.$emit('row-input-change', value, row, scope, column)
    },
    onTableRowCheckboxChange(value, row, scope, column) {
      this.$emit('row-checkbox-change', value, row, scope, column)
    },
    onTableColumnCellClick(value, row, prop) {
      this.$emit('click', value, row, prop)
    }
  },
  watch: {

  }
}
</script>
