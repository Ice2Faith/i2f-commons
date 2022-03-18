#{[tpl,tplStringCond],key="classpath:tpl/parts/tplStringCond.tpl"}
#{[tpl,tplNormalCond],key="classpath:tpl/parts/tplNormalCond.tpl"}

#{[if,_item],test="_item.javaType == _@String.class",ref="_tpl.tplStringCond"}
#{[if,_item],test="_item.javaType != _@String.class",ref="_tpl.tplNormalCond"}
