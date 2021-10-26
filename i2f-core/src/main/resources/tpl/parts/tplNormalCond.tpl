<if test='bean.${_item.colName@GenerateContext.castColumnName}!=null'>
                and t1.${_item.colName} = #{bean.${_item.colName@GenerateContext.castColumnName}}
            </if>
