<if test="bean.${_item.colName@GenerateContext.castColumnName}!=null and bean.${_item.colName@GenerateContext.castColumnName}!=''">
                and t1.${_item.colName} = #{bean.${_item.colName@GenerateContext.castColumnName}}
            </if>
