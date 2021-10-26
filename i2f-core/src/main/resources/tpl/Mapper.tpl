#{[tpl,tplCondWhetherStringType],key="classpath:tpl/parts/tplCondWhetherStringType.tpl"}

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
    PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${ctx.basePackage}.dao.${ctx.meta.tableName@GenerateContext.castTableName}Dao">
    <cache/>

    <!--
    @author ${ctx.author}
    @date ${now@DateUtil.format}
    @desc ${ctx.meta.tableName@GenerateContext.castTableName}Mapper
            for table : ${ctx.meta.tableName}
    -->

    <resultMap id="beanMap" type="${ctx.basePackage}.model.${ctx.meta.tableName@GenerateContext.castTableName}Bean">
    #{[for,ctx.meta.columns],separator="
        ",template="<result property='${_item.colName@GenerateContext.castColumnName}' column='${_item.colName}'/>"}
    </resultMap>

    <select id="queryList" resultMap="beanMap">
        select
        #{[for,ctx.meta.columns],separator="
            ,",template="t1.${_item.colName}"}
        from #{ctx.meta.tableName} t1
        <where>
            #{[for,ctx.meta.columns],separator="
            ",ref="_tpl.tplCondWhetherStringType"}
        </where>
    </select>

    <insert id="insertList">
        insert into ${ctx.meta.tableName}
        (
            #{[for,ctx.meta.columns],separator="
            ,",template="${_item.colName}"}
        )
        values
        <foreach collection="list" item="item" separator=",">
        (
            #{[for,ctx.meta.columns],separator="
            ,",template="#{item.${_item.colName@GenerateContext.castColumnName}}"}
        )
        </foreach>
    </insert>

    <update id="updateOne">
        update ${ctx.meta.tableName}
        set
        <trim suffixOverrides=",">
            #{[for,ctx.meta.columns],separator="
            ",template="<if test='${_item.colName@GenerateContext.castColumnName} != null'>
                ${_item.colName} = #{${_item.colName@GenerateContext.castColumnName}}
            </if>"}
        </trim>
        <where>
            1!=1
        </where>
    </update>

    <delete id="deleteOne">
        delete from #{ctx.meta.tableName}
        where
            #{[for,ctx.meta.columns],separator="
            ",ref="_tpl.tplCondWhetherStringType"}
    </delete>
</mapper>
