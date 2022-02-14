<template>
  <div>
    <LzTable
      :prefer="table.prefer"
      :loading="table.loading"
      :data="table.data"
      :page="table.page">
    </LzTable>
  </div>
</template>

<script>
import LzTable from '@/components/lz/LzTable'
export default {
  name: 'LzDemo',
  components: {
    LzTable
  },
  props: {
  },
  data: function() {
    return {
      table: {
        loading: false,
        data: [
          {
            name: 'Zhang',
            age: 0,
            descr: 'Mr.Zhang',
            income_alipay: 10,
            income_wxpay: 20,
            outcome_alipay: 5,
            outcome_wxpay: 12,
            single: false
          },
          {
            name: 'Liu',
            age: 22,
            descr: 'Mr.Liu',
            income_alipay: 12,
            income_wxpay: 22,
            outcome_alipay: 7,
            outcome_wxpay: 12,
            single: true
          },
          {
            name: 'Chen',
            age: 23,
            descr: 'Mr.Chen',
            income_alipay: 18,
            income_wxpay: 24,
            outcome_alipay: 4,
            outcome_wxpay: 13,
            single: false
          }
        ],
        page: {
          index: 1,
          size: 20,
          total: 0
        },
        prefer: {
          options: { // 可选功能
            page: true, // 是否需要分页组件
            controls: true // 是否需要控制组件
          },
          table: { // 表格配置
            border: true, // 是否显示边框，显示边框时resizable才生效
            resizable: true, // 是否可拖拉大小
            stripe: true, // 是否显示行的斑马纹
            index: true, // 是否显示行号
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
            enable: true, // 是否启用展开行
            labelPosition: 'left', // 展开行的标签位置
            formInline: false, // 展开行是否inline
            labelWidth: '80px', // 展开行标签宽度
            itemStyle: '', // 项目样式
            formStyle: '' // 表单样式
          },
          expands: [ // 展开行配置列表
            {
              prop: 'name', // 展开行数据来源
              label: '名字', // 展开行标签
              style: '' // 展开行内容样式
            },
            {
              prop: 'age', // 展开行数据来源
              label: '年龄', // 展开行标签
              style: '' // 展开行内容样式
            },
            {
              prop: 'income_alipay', // 展开行数据来源
              label: '支付宝收入', // 展开行标签
              style: '' // 展开行内容样式
            },
            {
              prop: 'income_wxpay', // 展开行数据来源
              label: '微信收入', // 展开行标签
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
              slot: '', // 是否这里被一个叫做slot指定名称的slot作为一列
              align: 'left', // 列的对其方式
              columnSlot: '', // 列内容的slot
              headerSlot: '', // 列表头的slot
              click: false, // 是否是可点击的样式，将会触发click事件
              href: '', // 可点击的超链接
              tooltip: true, // 内容超长时是否悬浮显示
              hover: 'descr',
              sort: true, // 是否启用排序
              filter: true, // 是否启用过滤
              input: false, // 是否是输入列
              multiply: false, // 是否多级表头
              columns: [] // 多级表头的列配置，与此配置相同
            },
            {
              label: '资产', // 列名
              multiply: true, // 是否是输入列,
              align: 'center',
              columns: [
                {
                  label: '收入',
                  multiply: true, // 是否是输入列,
                  align: 'center',
                  columns: [
                    {
                      prop: 'income_alipay',
                      label: '支付宝',
                      align: 'center',
                      width: '120px'
                    },
                    {
                      prop: 'income_wxpay',
                      label: '微信',
                      align: 'center',
                      width: '120px'
                    }
                  ]
                },
                {
                  label: '支出',
                  multiply: true, // 是否是输入列,
                  align: 'center',
                  columns: [
                    {
                      prop: 'outcome_alipay',
                      label: '支付宝',
                      align: 'center',
                      width: '120px'
                    },
                    {
                      prop: 'outcome_wxpay',
                      label: '微信',
                      align: 'center',
                      width: '120px'
                    }
                  ]
                }
              ]
             },
            {
              prop: 'age', // 列数据来源
              label: '年龄', // 列名
              width: '120px', // 列宽
              input: true // 是否是输入列
            },
            {
              prop: 'single', // 列数据来源
              label: '独生子女', // 列名
              width: '120px', // 列宽
              checkbox: true // 是否是输入列
            }
          ],
          operation: { // 操作列按钮功能配置
            enable: true, // 是否开启操作列
            gutter: 20, // 按钮之间的间距
            label: '操作', // 操作列列名
            width: undefined, // 列宽
            minWidth: '', // 列最小宽度
            fixed: false, // 是否固定列，或者固定left/right
            align: 'center' // 列的对其方式
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
              style: ''
            },
            {
              text: 'search', // 按钮文本
              event: 'search', // 按钮触发的事件
              href: '', // 按钮可以直接指向一个超链接
              icon: 'el-icon-search', // 按钮可以有图标
              type: 'text', // 其余这些样式都是官网的属性
              size: 'small',
              plain: true,
              round: true,
              circle: true,
              style: ''
            }
          ]
        }
      }
    }
  },
  computed: {},
  methods: {

  },
  watch: {

  }
}
</script>
