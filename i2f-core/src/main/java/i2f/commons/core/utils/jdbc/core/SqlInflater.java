package i2f.commons.core.utils.jdbc.core;

import i2f.commons.core.data.Pair;
import i2f.commons.core.data.Triple;
import i2f.commons.core.utils.jdbc.wrapper.QueryWrapper;
import i2f.commons.core.utils.jdbc.wrapper.base.SqlCondBaseWrapper;
import i2f.commons.core.utils.str.AppendUtil;
import i2f.commons.core.utils.str.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2021/9/28
 */
public class SqlInflater {

    public static AppendUtil.AppendBuilder sqlQueryCommon(AppendUtil.AppendBuilder builder, QueryWrapper wrapper, List<Object> params) {
        builder.add(" select ");
        sqlQueryColsPart(builder, wrapper)
                .add(" from ").add(wrapper.tableName);
        sqlJoinPart(builder, wrapper);
        String where = sqlBaseWhereConditionPart(wrapper, params);
        if (where != null) {
            where = where.trim();
            if (where.length() > 0) {
                where = StringUtil.trimStr(where, false, "and", "or");
                builder.add(" where ")
                        .add(where)
                        .add(" ");
            }
        }
        sqlGroupByPart(builder, wrapper, params);
        sqlOrderByPart(builder, wrapper);

        return builder;
    }

    public static  AppendUtil.AppendBuilder sqlQueryColsPart(AppendUtil.AppendBuilder builder, QueryWrapper wrapper) {
        if (wrapper.queryCols != null && wrapper.queryCols.size() > 0) {
            builder.add(" ").addCollection(false, ",", null, null, wrapper.queryCols);
        }
        return builder;
    }

    public static  AppendUtil.AppendBuilder sqlJoinPart(AppendUtil.AppendBuilder builder, QueryWrapper wrapper) {
        if (wrapper.joinPart != null && wrapper.joinPart.size() > 0) {
            for (Triple<String, String, String> item : wrapper.joinPart) {
                builder.add(" ")
                        .add(item.fst)
                        .add(" join ")
                        .add(item.sec)
                        .add(" on ")
                        .add(item.trd);
            }
        }
        return builder;
    }

    public static  String sqlBaseWhereConditionPart(SqlCondBaseWrapper wrapper, List<Object> params) {
        AppendUtil.AppendBuilder builder = AppendUtil.builder();
        sqlWhereConditionsPart(builder, wrapper, params);
        sqlFreeWhereConditionPart(builder, wrapper, params);
        sqlInWhereConditionPart(builder, wrapper, params);
        sqlLikeWhereConditionPart(builder, wrapper, params);
        sqlMultiLikesWhereConditionPart(builder, wrapper, params);
        return builder.done();
    }

    public static  AppendUtil.AppendBuilder sqlWhereConditionsPart(AppendUtil.AppendBuilder builder, SqlCondBaseWrapper wrapper, List<Object> params) {
        if (wrapper.whereConditions != null && wrapper.whereConditions.size() > 0) {
            for (Triple<String, String, Object> item : wrapper.whereConditions) {
                builder.add(" ").add(item.fst).add(" ").add(item.sec).add(" ? ");
                params.add(item.trd);
            }
        }
        return builder;
    }

    public static  AppendUtil.AppendBuilder sqlFreeWhereConditionPart(AppendUtil.AppendBuilder builder, SqlCondBaseWrapper wrapper, List<Object> params) {
        if (wrapper.freeWhereCondition != null && wrapper.freeWhereCondition.size() > 0) {
            for (Pair<String, String> item : wrapper.freeWhereCondition) {
                builder.add(" ").add(item.key).add(" ").add(item.val);
            }
        }
        return builder;
    }

    public static  AppendUtil.AppendBuilder sqlInWhereConditionPart(AppendUtil.AppendBuilder builder, SqlCondBaseWrapper wrapper, List<Object> params) {
        if (wrapper.inWhereCondition != null && wrapper.inWhereCondition.size() > 0) {
            for (Triple<String, String, Object[]> item : wrapper.inWhereCondition) {
                builder.add(" ").add(item.fst).add(" ").add(item.sec).add(" in ( ");
                boolean isFirst = true;
                for (Object pit : item.trd) {
                    if (!isFirst) {
                        builder.add(",");
                    }
                    builder.add(" ? ");
                    params.add(pit);
                    isFirst = false;
                }
                builder.add(" ) ");
            }
        }
        return builder;
    }

    public static  AppendUtil.AppendBuilder sqlLikeWhereConditionPart(AppendUtil.AppendBuilder builder, SqlCondBaseWrapper wrapper, List<Object> params) {
        if (wrapper.likeWhereCondition != null && wrapper.likeWhereCondition.size() > 0) {
            for (Triple<String, String, Object[]> item : wrapper.likeWhereCondition) {
                builder.add(" ").add(item.fst).add(" ( ");
                AppendUtil.AppendBuilder inBuilder = AppendUtil.builder();
                for (Object pit : item.trd) {
                    inBuilder.add(" ")
                            .add(" or ")
                            .add(item.sec)
                            .add(" like ")
                            .add(" '%'||?||'%' ");
                    params.add(pit);
                }
                String part = inBuilder.done();
                if (part != null) {
                    part = part.trim();
                    if (part.length() > 0) {
                        part = StringUtil.trimStr(part, false, "and", "or");
                        builder.add(" ")
                                .add(part)
                                .add(" ");
                    }
                }
                builder.add(" ) ");
            }
        }
        return builder;
    }

    public static  AppendUtil.AppendBuilder sqlMultiLikesWhereConditionPart(AppendUtil.AppendBuilder builder, SqlCondBaseWrapper wrapper, List<Object> params) {
        if (wrapper.multiLikeWhereCondition != null && wrapper.multiLikeWhereCondition.size() > 0) {
            for (Pair<String, Map<String, Object[]>> item : wrapper.multiLikeWhereCondition) {
                builder.add(" ").add(item.key).add(" ( ");
                AppendUtil.AppendBuilder inBuilder = AppendUtil.builder();
                for (Map.Entry<String, Object[]> mit : item.val.entrySet()) {
                    for (Object cur : mit.getValue()) {
                        inBuilder.add(" ")
                                .add(" or ")
                                .add(mit.getKey())
                                .add(" like ")
                                .add(" '%'||?||'%' ");
                        params.add(cur);
                    }
                }
                String part = inBuilder.done();
                if (part != null) {
                    part = part.trim();
                    if (part.length() > 0) {
                        part = StringUtil.trimStr(part, false, "and", "or");
                        builder.add(" ")
                                .add(part)
                                .add(" ");
                    }
                }
                builder.add(" ) ");
            }
        }
        return builder;
    }

    public static  AppendUtil.AppendBuilder sqlGroupByPart(AppendUtil.AppendBuilder builder, QueryWrapper wrapper, List<Object> params) {
        if (wrapper.groupBy != null && wrapper.groupBy.size() > 0) {
            builder.add(" ").add(" group by ")
                    .addCollection(false, ",", null, null, wrapper.groupBy);
        }
        String having = sqlHavingConditionsPart(wrapper, params);
        if (having != null) {
            having = having.trim();
            if (having.length() > 0) {
                having = StringUtil.trimStr(having, false, "and", "or");
                builder.add(" ").add(" having ")
                        .add(having)
                        .add(" ");
            }
        }
        return builder;
    }

    public static  String sqlHavingConditionsPart(QueryWrapper wrapper, List<Object> params) {
        AppendUtil.AppendBuilder builder = AppendUtil.builder();
        if (wrapper.havingConditions != null && wrapper.havingConditions.size() > 0) {
            for (Triple<String, String, Object> item : wrapper.havingConditions) {
                builder.add(" ")
                        .add(item.fst)
                        .add(" ")
                        .add(item.sec)
                        .add(" ? ");
                params.add(item.trd);
            }

        }
        return builder.done();
    }

    public static  AppendUtil.AppendBuilder sqlOrderByPart(AppendUtil.AppendBuilder builder, QueryWrapper wrapper) {
        if (wrapper.orderBy != null && wrapper.orderBy.size() > 0) {
            builder.add(" order by ");
            boolean isFirst = true;
            for (Pair<String, String> item : wrapper.orderBy) {
                if (!isFirst) {
                    builder.add(" , ");
                }
                builder.add(" ")
                        .add(item.key)
                        .add(" ")
                        .add(item.val);
                isFirst = false;
            }
        }
        return builder;
    }

    public static AppendUtil.AppendBuilder trimSql(AppendUtil.AppendBuilder builder,String sql,String prefix,String suffix,String ... trims){
        sql=StringUtil.trimFixStr(sql,true,prefix,suffix,trims);
        builder.add(sql);
        return builder;
    }
}
