<template>
  <div>
    <!-- 允许自定义整个列 -->
    <slot v-if="column.slot && column.slot!=''" :name="column.slot" :meta="column">
    </slot>
    <!-- 常规列定义，带过滤功能 -->
    <template v-else>
      <el-table-column
        v-if="column.filter"
        :resizable="config.table.resizable"
        :align="config.table.align"
        :prop="column.prop"
        :label="column.label"
        :fixed="column.fixed"
        :sortable="column.sort"
        :width="column.width"
        :min-width="column.minWidth"
        :show-overflow-tooltip="column.tooltip"
        :filters="getFilters(column.prop)"
        :filter-method="filterHandler">
        <!-- 允许自定义表头 -->
        <template slot="header" slot-scope="scope">
          <template v-if="column.headerSlot && column.headerSlot!=''">
            <slot :name="column.headerSlot" :column="column">
              {{column.label}}
            </slot>
          </template>
          <template v-else>
            {{column.label}}
          </template>
        </template>
        <template slot-scope="scope">
          <!-- 支持点击的特殊样式 -->
          <template v-if="column.click">
            <el-link type="primary" v-if="column.href && column.href!=''" :href="column.href">{{ scope.row[column.prop] }}</el-link>
            <el-link type="primary" v-else @click="onTableColumnCellClick(scope.row,column.prop,scope)">{{ scope.row[column.prop] }}</el-link>
          </template>
          <!-- 支持输入的特殊样式 -->
          <template v-else-if="column.input && column.input!=''">
            <el-input
              v-model="scope.row[column.prop]"
              style="width: 100%"
              type="text"/>
          </template>
          <!-- 支持复选的特殊样式 -->
          <template v-else-if="column.checkbox">
            <el-checkbox
              v-model="scope.row[column.prop]"
              style="width: 100%"
              @change="onTableRowCheckboxChange(scope,column)">
              {{column.label}}
            </el-checkbox>
          </template>
          <!-- 支持单元格内容定义的样式 -->
          <template v-else-if="column.columnSlot && column.columnSlot!=''">
            <slot :name="column.columnSlot" :scope="scope">
              {{scope.row[column.prop]}}
            </slot>
          </template>
          <template v-else>
            <template v-if="column.hover && column.hover!=''">
              <el-popover
                placement="top-start"
                width="200"
                trigger="hover">
                <template slot="reference">
                  <div>{{scope.row[column.prop]}}</div>
                </template>
                 <div v-html="scope.row[column.hover]"></div>
              </el-popover>
            </template>
            <template v-else>
              <div>{{scope.row[column.prop]}}</div>
            </template>
          </template>
        </template>
      </el-table-column>
      <!-- 常规列定义，不带过滤功能 -->
      <el-table-column
        v-else
        :resizable="config.table.resizable"
        :align="config.table.align"
        :prop="column.prop"
        :label="column.label"
        :sortable="column.sort"
        :width="column.width"
        :min-width="column.minWidth"
        :fixed="column.fixed"
        :show-overflow-tooltip="column.tooltip">
        <!-- 允许自定义表头 -->
        <template slot="header" slot-scope="scope">
          <template v-if="column.headerSlot && column.headerSlot!=''">
            <slot :name="column.headerSlot" :column="column">
              {{column.label}}
            </slot>
          </template>
          <template v-else>
            {{column.label}}
          </template>
        </template>
        <template slot-scope="scope">
          <!-- 支持点击的特殊样式 -->
          <template v-if="column.click">
            <el-link type="primary" v-if="column.href && column.href!=''" :href="column.href">{{ scope.row[column.prop] }}</el-link>
            <el-link type="primary" v-else @click="onTableColumnCellClick(scope.row,column.prop,scope)">{{ scope.row[column.prop] }}</el-link>
          </template>
          <!-- 支持输入的特殊样式 -->
          <template v-else-if="column.input && column.input!=''">
            <el-input
              v-model="scope.row[column.prop]"
              style="width: 100%"
              type="text"/>
          </template>
          <!-- 支持复选的特殊样式 -->
          <template v-else-if="column.checkbox">
            <el-checkbox
              v-model="scope.row[column.prop]"
              style="width: 100%"
              @change="onTableRowCheckboxChange(scope,column)">
              {{column.label}}
            </el-checkbox>
          </template>
          <!-- 支持单元格内容定义的样式 -->
          <template v-else-if="column.columnSlot && column.columnSlot!=''">
            <slot :name="column.columnSlot" :scope="scope">
              {{scope.row[column.prop]}}
            </slot>
          </template>
          <template v-else>
            <template v-if="column.hover && column.hover!=''">
              <el-popover
                placement="top-start"
                width="200"
                trigger="hover">
                <template slot="reference">
                  <div>{{scope.row[column.prop]}}</div>
                </template>
                <div v-html="scope.row[column.hover]"></div>
              </el-popover>
            </template>
            <template v-else>
              <div>{{scope.row[column.prop]}}</div>
            </template>
          </template>
        </template>
      </el-table-column>
    </template>
  </div>
</template>

<script>
export default {
  name: 'LzTableColumn',
  components: {
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
    onTableInputChange(scope, column) {
      this.$emit('row-input-change', scope.row[column.prop], scope.row, scope, column)
    },
    onTableRowCheckboxChange(scope, column) {
      this.$emit('row-checkbox-change', scope.row[column.prop], scope.row, scope, column)
    },
    getFilters(prop) {
      const ret = []
      this.data.forEach(item => {
        const pitem = {
          text: item[prop],
          value: item[prop]
        }
        let isIn = false
        for (let i = 0; i < ret.length; i++) {
          if (ret[i].text === pitem.text) {
            isIn = true
            break
          }
        }
        if (!isIn) {
          ret.push(pitem)
        }
      })
      return ret
    },
    filterHandler(value, row, column) {
      const property = column['property']
      return row[property] === value
    },
    onTableColumnCellClick(row, prop, scope) {
      this.$emit('click', row[prop], row, prop)
    }
  },
  watch: {

  }
}
</script>
