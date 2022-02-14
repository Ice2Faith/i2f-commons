<template>
  <div v-loading="loading">
    <el-table
      v-if="config.table"
      ref="table"
      :data="data"
      :stripe="config.table.stripe"
      :border="config.table.border"
      :row-class-name="config.table.rowClassName"
      :height="config.table.height"
      :max-height="config.table.maxHeight"
      :highlight-current-row="config.table.highlightCurrentRow"
      @cell-click="onTableCellClick"
      @cell-dblclick="onTableCellDbClick"
      @row-click="onTableRowClick"
      @row-dblclick="onTableRowDbClick"
      @current-change="onTableCurrentRowChange"
      @selection-change="onTableSelectionsChange">
      <!-- 允许开启复选 -->
      <el-table-column
        v-if="config.table.select"
        type="selection"
        fixed="left"
        width="55">
        <template slot="header" slot-scope="scope">
          <el-checkbox
            :indeterminate="setting.checkBox.isIndeterminate"
            v-model="setting.checkBox.checkAll"
            @change="onTableSelectAll">全选</el-checkbox>
          <el-divider direction="vertical"></el-divider>
          <span @click="onTableSelectAnti">反选</span>
        </template>
      </el-table-column>
      <!-- 允许开启行号 -->
      <el-table-column
        v-if="config.table.index"
        type="index"
        fixed="left"
        label="行号"
        width="50">
      </el-table-column>
      <el-table-column
        v-if="config.expand && config.expand.enable"
        fixed="left"
        type="expand">
        <template slot-scope="props">
          <el-form
            :style="config.expand.formStyle"
            :label-position="config.expand.labelPosition"
            :inline="config.expand.formInline"
            :label-width="config.expand.labelWidth">
            <template v-for="(item, index) in config.expands">
              <el-form-item
                :key="index"
                :style="config.expand.itemStyle"
                :label="item.label">
                <span :style="item.style">{{ props.row[item.prop] }}</span>
              </el-form-item>
            </template>
          </el-form>
        </template>
      </el-table-column>
      <template v-for="(column, index) in config.columns">
        <LzMultiTableColumn
          :key="index"
          :column="column"
          :config="config"
          :data="data"
          @click="onTableColumnCellClick"
          @row-input-change="onTableRowInputChange"
          @row-checkbox-change="onTableRowCheckboxChange">
        </LzMultiTableColumn>
      </template>
      <el-table-column
        v-if="config.operation.enable"
        :resizable="config.table.resizable"
        :align="config.operation.align"
        :label="config.operation.label"
        :width="config.operation.width"
        :min-width="config.operation.minWidth"
        :fixed="config.operation.fixed">
        <template slot-scope="scope">
          <el-row :gutter="config.operation.gutter">
            <template v-for="(item, index) in config.operations">
              <el-link v-if="item.href && item.href!=''" :href="item.href" :key="index">
                <el-button
                  :type="item.type"
                  :icon="item.icon"
                  :size="item.size"
                  :plain="item.plain"
                  :round="item.round"
                  :circle="item.circle"
                  :style="item.style"
                  @click="onTableOperationButton(item.event,scope)">{{ item.text }}</el-button>
              </el-link>
              <el-button
                v-else
                :key="index"
                :type="item.type"
                :icon="item.icon"
                :size="item.size"
                :plain="item.plain"
                :round="item.round"
                :circle="item.circle"
                :style="item.style"
                @click="onTableOperationButton(item.event,scope)">{{ item.text }}</el-button>
            </template>
          </el-row>
        </template>
      </el-table-column>
    </el-table>
    <div style="z-index: 10;"></div>
    <template v-if="config.options && config.options.page">
      <el-pagination
        :current-page="page.index"
        :page-size="page.size"
        :total="page.total"
        :layout="config.page.layout"
        :page-sizes="config.page.sizes"
        :small="config.page.small"
        :hide-on-single-page="config.page.autoHidden"
        @current-change="onPageIndexChange"
        @size-change="onPageSizeChange">
      </el-pagination>
    </template>
  </div>
</template>

<script>
import LzMultiTableColumn from '@/components/lz/components/LzMultiTableColumn'
export default {
  name: 'LzTable',
  components: {
    LzMultiTableColumn
  },
  props: {
    prefer: { // 参考defaultConfig函数的返回值
      type: Object,
      default: function() {
        return {}
      }
    },
    loading: {
      type: Boolean,
      default: false
    },
    data: {
      type: Array | Object,
      default: function() {
        return []
      }
    },
    page: {
      type: Object,
      default: function() {
        return {
          index: 1,
          size: 20,
          total: 0
        }
      }
    }
  },
  data: function() {
    return {
      setting: {
        checkBox: {
          checkAll: false,
          isIndeterminate: false
        }
      },
      // 组件配置，应该是prefer的全集,参考defaultConfig的返回值
      config: this.defaultConfig()
    }
  },
  computed: {},
  methods: {
    clearCurrentRow() {
      this.$refs.table.setCurrentRow()
    },
    onTableCurrentRowChange(idx) {
      this.$emit('current-row-change', this.data[idx], idx)
    },
    onTableSelectionsChange(selections) {
      this.$emit('selection-change', selections)
    },
    onTableColumnCellClick(value, row, prop) {
      this.$emit('click', value, row, prop)
    },
    onTableSelectAll() {
      if (this.setting.checkBox.checkAll) {
        this.data.forEach(item => {
          this.$refs.table.toggleRowSelection(item, this.setting.checkBox.checkAll)
        })
      } else {
        this.$refs.table.clearSelection()
      }
    },
    onTableSelectAnti() {
      this.$refs.table.toggleAllSelection()
    },
    onTableCellClick(row, column, cell, event) {
      this.$emit('cell-click', row, column, cell, event)
    },
    onTableCellDbClick(row, column, cell, event) {
      this.$emit('cell-dbclick', row, column, cell, event)
    },
    onTableRowClick(row, column, event) {
      this.$emit('row-click', row, column, event)
    },
    onTableRowDbClick(row, column, event) {
      this.$emit('row-dbclick', row, column, event)
    },
    onTableRowInputChange(value, row, scope, column) {
      this.$emit('row-input-change', value, row, scope, column)
    },
    onTableRowCheckboxChange(value, row, scope, column) {
      this.$emit('row-checkbox-change', value, row, scope, column)
    },
    onTableOperationButton(event, scope) {
      this.$emit(event, scope.row, scope)
    },
    onPageIndexChange(val) {
      const page = {
        index: val,
        size: this.page.size,
        total: this.page.total
      }
      this.config.page.index = val
      this.$emit('page-change', page)
      this.$emit('update:page', page)
    },
    onPageSizeChange(val) {
      const page = {
        index: this.page.index,
        size: val,
        total: this.page.total
      }
      this.$emit('page-change', page)
      this.$emit('update:page', page)
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
    defaultConfig() {
      return {
        options: { // 可选功能
          page: true, // 是否需要分页组件
          controls: true // 是否需要控制组件
        },
        table: { // 表格配置
          border: true, // 是否显示边框，显示边框时resizable才生效
          resizable: true, // 是否可拖拉大小
          stripe: true, // 是否显示行的斑马纹
          index: false, // 是否显示行号
          select: true, // 是否显示复选列
          rowClassName: '', // 行的样式名
          height: undefined, // 表格高度
          maxHeight: undefined, // 表格最大高度
          highlightCurrentRow: true // 是否高亮显示当前行，用于单选
        },
        page: { // 分页配置
          autoHidden: false, // 不满一页时是否自动隐藏
          small: false, // 是否以小样式显示
          sizes: [10, 20, 30, 50, 100, 200, 300, 500], // 下拉的页大小配置
          layout: 'total, sizes, prev, pager, next, jumper' // 分页的布局控件配置
        },
        expand: { // 展开行配置
          enable: false, // 是否启用展开行
          labelPosition: 'left', // 展开行的标签位置
          formInline: true, // 展开行是否inline
          labelWidth: '80px', // 展开行标签宽度
          itemStyle: '', // 项目样式
          formStyle: '' // 表单样式
        },
        expands: [ // 展开行配置列表
          {
            prop: 'name', // 展开行数据来源
            label: '名字', // 展开行标签
            style: '' // 展开行内容样式
          }
        ],
        columns: [ // 表格列配置列表
          {
            prop: 'name', // 列数据来源
            label: '名字', // 列名
            width: '120px', // 列宽
            minWidth: '', // 列最小宽度
            fixed: false, // 是否固定列，或者固定left/right
            slot: 'name', // 是否这里被一个叫做slot指定名称的slot作为一列
            align: 'left', // 列的对其方式
            columnSlot: 'colName', // 列内容的slot
            headerSlot: 'header', // 列表头的slot
            click: true, // 是否是可点击的样式，将会触发click事件
            href: '', // 可点击的超链接
            tooltip: true, // 内容超长时是否悬浮显示
            hover: '', // 鼠标悬浮时，要显示的其他prop字段
            sort: true, // 是否启用排序
            filter: true, // 是否启用过滤
            input: false, // 是否是输入列
            checkbox: false, // 是否复选项
            select: false, // 是否下拉选框
            selectList: [], // 下拉选框列表
            counter: false, // 是否计数器
            counterConfig: {
              min: 1,
              max: 100
            },
            multiply: false, // 是否多级表头
            columns: [] // 多级表头的列配置，与此配置相同
          }
        ],
        operation: { // 操作列按钮功能配置
          enable: false, // 是否开启操作列
          gutter: 20, // 按钮之间的间距
          label: '操作', // 操作列列名
          width: '120px', // 列宽
          minWidth: '', // 列最小宽度
          fixed: 'right', // 是否固定列，或者固定left/right
          align: 'left' // 列的对其方式
        },
        operations: [ // 操作列功能列表
          {
            text: '添加', // 按钮文本
            event: 'add', // 按钮触发的事件
            href: '', // 按钮可以直接指向一个超链接
            icon: 'el-icon-plus', // 按钮可以有图标
            type: 'primary', // 其余这些样式都是官网的属性
            size: 'small',
            plain: false,
            round: true,
            circle: false,
            hover: '', // 鼠标悬浮在按钮上时显示的内容
            style: ''
          }
        ]
      }
    }
  },
  watch: {
    prefer: {
      immediate: true,
      deep: true,
      handler: function(val, old) {
        let apply = this.defaultConfig()
        // TODO 将val的每个属性逐一覆盖到默认配置中，最后使用混合的配置作为组件使用配置
        if (val.options) {
          if (val.options.page !== undefined) {
            apply.options.page = val.options.page
          }
          if (val.options.controls !== undefined) {
            apply.options.controls = val.options.controls
          }
        }
        if (val.table) {
          if (val.table.border !== undefined) {
            apply.table.border = val.table.border
          }
          if (val.table.resizable !== undefined) {
            apply.table.resizable = val.table.resizable
          }
          if (val.table.stripe !== undefined) {
            apply.table.stripe = val.table.stripe
          }
          if (val.table.index !== undefined) {
            apply.table.index = val.table.index
          }
          if (val.table.select !== undefined) {
            apply.table.select = val.table.select
          }
          if (val.table.rowClassName !== undefined) {
            apply.table.rowClassName = val.table.rowClassName
          }
          if (val.table.height !== undefined) {
            apply.table.height = val.table.height
          }
          if (val.table.maxHeight !== undefined) {
            apply.table.maxHeight = val.table.maxHeight
          }
          if (val.table.highlightCurrentRow !== undefined) {
            apply.table.highlightCurrentRow = val.table.highlightCurrentRow
          }
        }
        if (val.expand) {
          if (val.expand.enable !== undefined) {
            apply.expand.enable = val.expand.enable
          }
          if (val.expand.labelPosition !== undefined) {
            apply.expand.labelPosition = val.expand.labelPosition
          }
          if (val.expand.formInline !== undefined) {
            apply.expand.formInline = val.expand.formInline
          }
          if (val.expand.labelWidth !== undefined) {
            apply.expand.labelWidth = val.expand.labelWidth
          }
          if (val.expand.itemStyle !== undefined) {
            apply.expand.itemStyle = val.expand.itemStyle
          }
          if (val.expand.formStyle !== undefined) {
            apply.expand.formStyle = val.expand.formStyle
          }
        }
        if (val.page) {
          if (val.page.autoHidden !== undefined) {
            apply.page.autoHidden = val.page.autoHidden
          }
          if (val.page.small !== undefined) {
            apply.page.small = val.page.small
          }
          if (val.page.sizes !== undefined) {
            apply.page.sizes = val.page.sizes
          }
          if (val.page.layout !== undefined) {
            apply.page.layout = val.page.layout
          }
        }
        if (val.expands) {
          apply.expands = val.expands
        }
        if (val.columns) {
          apply.columns = val.columns
        }
        if (val.operation) {
          if (val.operation.enable !== undefined) {
            apply.operation.enable = val.operation.enable
          }
          if (val.operation.gutter !== undefined) {
            apply.operation.gutter = val.operation.gutter
          }
          if (val.operation.label !== undefined) {
            apply.operation.label = val.operation.label
          }
          if (val.operation.width !== undefined) {
            apply.operation.width = val.operation.width
          }
          if (val.operation.minWidth !== undefined) {
            apply.operation.minWidth = val.operation.minWidth
          }
          if (val.operation.fixed !== undefined) {
            apply.operation.fixed = val.operation.fixed
          }
          if (val.operation.align !== undefined) {
            apply.operation.align = val.operation.align
          }
        }
        if (val.operations) {
          apply.operations = val.operations
        }
        this.config = apply
      }
    }
  }
}
</script>
