package i2f.tools;

/*
--------------------------
name:公式计算器
author:Ugex.Savelar
date:2020-7-25
--------------------------
dev-src:cpp
re-impl:java
--------------------------
基于双栈（数字栈，符号栈）实现的字符串公式计算器
原理：
扫描整个输入公式，从左向右扫描
如果是数字，直接入栈到数字栈
如果是符号
	如果符号栈为空，或者是左括号，无条件入栈到符号栈
	如果符号栈不为空
		和符号栈栈顶符号，比较优先级
			如果高于栈顶优先级，直接入栈到符号栈
			如果低于栈顶优先级，从数字栈取出两个数字，从符号栈取出一个符号，进行运算，
				将运算结果放入数字栈，将当前符号入栈到符号栈
			如果当前扫描到的符号是右括号）
				不断取出两数字和一符号运算，知道符号栈栈顶符号为左括号（
				弹出栈顶的左括号（
也就是说，数字和左括号（无条件入栈，右括号）始终不入栈，
遇到右括号），就不断弹栈计算，知道消去栈中弹出的第一个左括号（，
其他时候，当前的符号优先级高，直接入栈，优先级低则弹栈计算，再入栈

伪代码流程：
	直到 输入公式扫描结束
		如果 扫描到字符
			获取数字
			入栈-数字栈
			移动下标
		否则
			获取当前符号
			如果 不支持当前符号
				退出循环、直接返回
			如果 符号栈空
				入栈当前符号
			否则
				获取符号栈顶符号
				获得栈顶符号和当前符号的优先级
				如果 优先级获取出错的标志
					退出循环、直接返回
				否则如果 消除括号的优先级标志
					出栈符号栈
				否则如果 当前符号优先级 低于 栈顶符号
					出栈两个数字和一个符号进行计算，并将计算结果重新入栈数字栈--查看注解1
					如果 当前符号 是 右括号
						继续下一轮循环，跳过本轮循环
					入栈 当前符号 到 符号栈
				否则如果 当前符号优先级 高于 栈顶符号
					入栈 当前符号 到 符号栈
			移动下标
	直到 符号栈 为空
		出栈两个数字和一个符号进行计算，并将计算结果重新入栈数字栈--查看注解1

	返回 数字栈栈顶，这就是最终的结果

-----注解1：
	出栈数字2 从 数字栈
	出栈数字1 从 数字栈
	出栈运算符号 从 符号栈
	计算 数字1 运算符号 数字2 保存到 结果
	入栈 结果 到 数字栈
----注意，这里是区分了数字1和数字2的，因为除法减法是需要区分的
*/

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FormulaCalculator {
    public static String buildPrepareFormula(String preFormula, List<String> flags, List<String> values) {
        String ret = preFormula;
        for (int i = 0; i < flags.size(); i++) {
            ret.replaceAll(flags.get(i), values.get(i));
        }
        return ret;
    }

    public static double hex2Number(String str, int base) {
        return new FormulaCalculator().getNumber(str, base);
    }

    public static String number2Hex(double num, int base, int decimal) {
        String hex = "";
        if (base < 2 || base > 16 || decimal < 0)
            return hex;

        String map = "0123456789ABCDEF";

        char[] temp = new char[128];
        int otc = (int) num;
        double flo = num - otc;
        int len = 0;
        while (otc != 0) {
            int pnum = otc % base;
            temp[len++] = map.charAt(pnum);
            otc = (int) otc / base;
        }
        int tlen = len - 1;
        if (tlen <= 0)
            hex += '0';
        while (tlen >= 0) {
            hex += temp[tlen];
            tlen--;
        }
        hex += '.';
        int dotIndex = hex.length();
        int i = 0;
        while (flo != 0.0 && i < decimal) {
            int pnum = (int) (flo * base);
            hex += map.charAt(pnum);
            flo = flo * base - pnum;
            i++;
        }
        if (dotIndex == hex.length())
            hex += '0';

        return hex;
    }
    /////////////////////////////////////////////

    public static String getUseHelpStr() {
        String help = "公式计算器使用简介：\tUgex.Savelar\n" +
                "\t常规双目运算符号：+- * / % ^()\n" +
                "\t\t加、减、乘、除、取模、求幂\n" +
                "\t\t用法：3 + 2 * 5 - (2 ^ 3) % 5\n" +
                "\t拓展双目运算符号：sqrt log adds muls and or xor\n" +
                "\t\tN次根、对数、累加、累乘、位与、位或、位异或\n" +
                "\t\t用法：2 sqrt 4 + 1 adds5 + 7 and 3\n" +
                "\t\t说明：2 sqrt4表示4开2次根，1 adds5表示1 +...+5\n" +
                "\t\t\t2log8表示求以2为底8的对数, 7 and3表示7位与3\n" +
                "\t拓展单目运算符：\n" +
                "\t\t !neg per abs radian angle sin cos tan arcsin arccos arctan recip epow ln numpi nume numgsec\n" +
                "\t\t阶乘 负号 百分号 绝对值 转弧度 转角度 三角函数族 求倒数 e的x次幂 自然对数 n倍圆周率 n倍自然常数 n倍黄金分割率\n" +
                "\t\t用法：3 !+5 neg + 50 per + (60 radian)sin\n" +
                "\t\t说明：3 !表示求3的阶乘，5 neg表示负5, 50 per表示百分之50\n" +
                "\t\t\t60radian表示将60转换为弧度制，(60 radian)sin表示求sin60角度\n" +
                "\t\t[注意]:三角函数族计算需要使用弧度制，拥有运算符转换\n" +
                "\t内建符号：\n" +
                "\t\tdehex\n" +
                "\t\t解析2 - 16 进制数，用法：16 dehex0c\n" +
                "\t\t【注意】：dehex【之后】紧跟解析数，不能有空格等符号，否则出错\n" +
                "\t\t【注意】：dehex【之前】可以有空格等符号，和其他运算符一致\n" +
                "\t\t出错写法：16 dehexc, 16 dehex 0 c\n" +
                "\t\t正确写法：16d ehex0c, 8 dehex14, 2d ehex1100\n" +
                "\t\t如果该符号之后第一个有效字符不是0 - 9，请在前面加上0，如上的c就是这个情况\n" +
                "\t\t否则将会按照运算符去处理识别，而出错\n" +
                "\t注意事项：\n" +
                "\t\t运算符不区分大小写\n" +
                "\t\t空格空白符号可以任意添加，只要不中断运算符\n" +
                "\t\t空白符号指：ASCII字符：空格 回车 换行 制表符\n" +
                "\t\t计算结果请检查isSuccess标志位，如果为false，计算失败，错误发生\n" +
                "\t\t你可以使用getLastErrStr() 获取错误描述\n" +
                "\t\t使用getLastResult() 获取计算结果，如果发生错误就是中间计算结果\n" +
                "\t\t\t如果未发生错误，就是正确结果\n" +
                "\t综合使用案例：\n" +
                "\t\t7 and 3 + 1 or 2 + 3 xor 0 + (0 not)and 3 + " +
                "(60 radian)sin + (30 radian)cos + (30 radian)tan + 3 !+25 per + 10 neg + 180 radian + " +
                "2 sqrt 4 + 2 log 8 + 1 adds 5 + 1 muls 3 + " +
                "((3.14 * 2 - 6 / (2 % 10)) + (3 ^ 1)) / (4 / 2) + " +
                "((3 + 5 * (4 % (3 + 7)) / (2 ^ 2)) / 2 / 2) ^ 3 + " +
                "3 * 2 * 2 / 4 + 5 * (2 - 4) + 6 / 3 + 12 % 10 + 2 ^ 3 +" +
                "1 recip + 1 epow + 1 nume ln +2 numpi + 1 numgsec +" +
                "16 dehex0c + 8 dehex14 - 2 dehex1100\n" +
                "\t\t计算结果为：79.460495\n" +
                "        ";
        return help;
    }

    public FormulaCalculator() {
        m_flags = new ArrayList<String>();
        m_ruleTable = null;
        m_flagStack = new Stack<String>();
        m_numberStack = new Stack<Double>();
        m_indexOfSingleNumberOperatorBegin = 0;

        m_flags.add("+");    //写法参考：3+2-5*4/3
        m_flags.add("-");
        m_flags.add("*");
        m_flags.add("/");
        m_flags.add("%");
        m_flags.add("^");
        m_flags.add("(");
        m_flags.add(")");

        m_flags.add("sqrt")
        ;    // 对m开n次根: n sqrt m //写法参考：2 sqrt 4 + 2 log 8 adds 3 ，其实和传统运算符一样，操作数在两边
        m_flags.add("log");    // 计算log以a为底b的对数： a log b //log(num2)/log(num1)//换底公式
        m_flags.add("adds");    //计算a累加到b: a adds b
        m_flags.add("muls");    //计算a累乘到b: a muls b

        m_flags.add("and");    //二进制运算
        m_flags.add("or");
        m_flags.add("xor");

        m_indexOfSingleNumberOperatorBegin = m_flags.size(); //单目双目运算符分割记录

        m_flags.add("not");

        m_flags.add("!");        //阶乘		//写法参考：2! + (6 neg) abs ，其实和阶乘写法一样，操作数在前
        m_flags.add("neg");    //取负数	//负号
        m_flags.add("per");    //取百分值	//百分号
        m_flags.add("abs");    //取绝对值
        m_flags.add("radian");    //取弧度制
        m_flags.add("angle");        //取角度制
        m_flags.add("sin");        //三角函数族，默认使用弧度进行计算，特别说明
        m_flags.add("cos");
        m_flags.add("tan");
        m_flags.add("arcsin");
        m_flags.add("arccos");
        m_flags.add("arctan");

        m_flags.add("recip");
        m_flags.add("epow");
        m_flags.add("ln");
        m_flags.add("numpi");
        m_flags.add("nume");
        m_flags.add("numgsec");

        int len = m_flags.size();
        m_ruleTable = new char[len * len];

        char[][] tpTable =
                {
                        //+		-	*	/	%	^	(	)		sqrt	log		adds	muls		and	or	xor	not		!	neg	per	abs	radian	angle	sin	cos	tan	arcsin	arccos	arctan		recip	epow	ln	numpi	nume	numgsec
                        {
                                0, 0, 0, 0, 0, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  +
                        {
                                0, 0, 0, 0, 0, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  -
                        {
                                1, 1, 0, 0, 0, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  *
                        {
                                1, 1, 0, 0, 0, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  /
                        {
                                1, 1, 1, 1, 1, 1, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  %
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  ^
                        {
                                1, 1, 1, 1, 1, 1, 1, 99, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1
                        },        //  (
                        {
                                0, 0, 0, 0, 0, 0, 9, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  )
                        //拓展双目运算符
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  sqrt
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  log
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  adds
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  muls
                        //
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  and
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  or
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  xor
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  not
                        //单目运算符
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  !
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  neg
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  per
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  abs
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  radian
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  angle
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  sin
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  cos
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  tan
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  arcsin
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  arccos
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  arctan
                        //拓展单目运算符
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  recip
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  epow
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  ln
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  numpi
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  nume
                        {
                                1, 1, 1, 1, 1, 0, 1, 99, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },        //  numgsec

                };

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                At2(i, j, tpTable[i][j]);
            }
        }

    }

    /*
    给定算术表达式，即可进行计算，返回计算结果，
    并且你需要接受计算是否成功的标志，
    一旦发生错误，返回值为0，成功标志为false
    但是你可以通过getLastErrStr()获取错误描述
    可能过程中，是存在一定步骤之前的正确结果的，
    你可以通过getLastResult()获取计算结果，此方法如果之前计算标志成功，那么就是最终的计算结果
    base参数用于指定运算使用的进制，默认十进制，这将会决定整个计算的数字部分的解析
    */
    public double calculate(String formula, int base) {
        m_flagStack.clear();
        m_numberStack.clear();

        if (formula.trim().length() == 0) {
            throw new RuntimeException("输入串长度被截断或是无意义空串，请重试");
        }

        int i = 0;
        int formulaLen = formula.length();
        while (i < formulaLen) {
            p_addCount = 0;
            if (isNumberChar(formula.charAt(i), base)) {
                double num = getNumber(formula.substring(i), base);
                m_numberStack.push(num);
                i += p_addCount;
            } else {
                String flg = getFlag(formula.substring(i), base);
                flg = flg.trim();
                if (flg.length() > 0) {
                    if (flg.equalsIgnoreCase("dehex")) {
                        int baseCur = Integer.parseInt(String.valueOf(m_numberStack.pop().intValue()));
                        int bakAddCount = p_addCount;
                        p_addCount = 0;
                        double num = getNumber(formula.substring(i + bakAddCount), baseCur);
                        m_numberStack.push(num);
                        i += bakAddCount + p_addCount;
                        p_addCount = bakAddCount;
                        continue;
                    }
                    if (isLegalOperator(flg) == false) {
                        throw new RuntimeException(String.format("未识别的运算符：%s", flg));
                    }

                    if (m_flagStack.size() == 0)
                        m_flagStack.push(flg);
                    else {
                        String top = m_flagStack.peek();
                        int priority = getPriortyTag(flg, top);
                        if (priority == 99) {
                            throw new RuntimeException(String.format("表达式错误：index:%d,value:%s", i, flg));
                        } else if (priority == 9) {
                            m_flagStack.pop();
                        } else if (priority == 0) {
                            if (calculateOnceStack() == false) {
                                throw new RuntimeException(String.format("公式错误，不匹配的操作数,操作数个数异常：index:%d,value:%s", i, flg));
                            }
                            //注意，这个continue至关重要，主要作用是处理最里层的括号内的运算
                            //由于后面的下标移动是统一的，如果这里不判断进行continue
                            //将会导致下标持续向前走，导致计算括号出现错误
                            if (flg.equalsIgnoreCase(")"))
                                continue;
                            m_flagStack.push(flg);
                        } else if (priority == 1) {
                            m_flagStack.push(flg);
                        }
                    }

                }
                i += p_addCount;
            }
        }
        //处理最后栈中剩余的数据
        //特别注意：此时栈中的符号依然存在优先级关系，如果直接不断取出栈顶计算，那么就算没有优先级，也会应为不遵循从左到右的计算规则而导致计算出错
        //常见的：2*2-4*1+2 sqrt 4，将会导致计算流程变为：((2*2)-((4*1)+(2 sqrt 4)))=-2,而不是正确的结果2=(((2*2)-(4*1))+(2 sqrt 4))
        //这种错误将是致命的，因此，正确的善后计算过程是：
		/*
		如果 符号栈内数量大于等于2， 那就需要比较优先级，下面的描述中：真栈顶==真实的栈顶部元素，次栈顶==除了栈顶的栈顶元素
			如果 真栈顶的优先级 大于 次栈顶的优先级
				那么 直接弹出数字栈两个数字和符号栈一个符号进行计算，将计算结果入栈【符号不在入栈了，需要注意】
			否则
				那么 先保存符号栈顶和数字栈的栈顶，在去正常的弹栈计算，将计算结果入栈【再把先前保存的符号和数字入栈】
		否则
			直接弹栈计算，得出最终结果

		*/
        while (m_flagStack.size() > 0) {
            boolean isCalSec = false;
            if (m_flagStack.size() >= 2)//如果符号栈还有两个以上的运算符，那就需要优先级比较
            {
                //先弹栈比较优先级
                String endTop = m_flagStack.pop();
                String preTop = m_flagStack.pop();
                int priority = getPriortyTag(endTop, preTop);
                if (priority == 1) {
                    //真栈顶的优先级高，则恢复栈
                    m_flagStack.push(preTop);
                    m_flagStack.push(endTop);

                    //弹栈计算,入栈结果
                    isCalSec = calculateOnceStack();
                } else if (priority == 0) {
                    //真栈顶的优先级低，则保存两栈的栈顶，因为我这里实现的时候，是弹栈比较的优先级，因此需要回复次栈顶成为临时栈顶
                    double topNum = m_numberStack.pop();
                    m_flagStack.push(preTop);

                    //弹栈计算，入栈结果
                    isCalSec = calculateOnceStack();

                    //恢复保存的两真栈顶
                    m_numberStack.push(topNum);
                    m_flagStack.push(endTop);
                } else {
                    //由于最后收尾的时候，符号栈不存在左括号了，但是如果还是存在，那就是出错了
                    throw new RuntimeException("堆栈异常或者符号栈异常");
                }
            } else {
                //如果符号栈只有一个符号了，没得商量，直接计算，得到最终结果
                isCalSec = calculateOnceStack();
            }

            if (isCalSec == false) {
                throw new RuntimeException("公式错误，不匹配的操作数,操作数个数异常：index:end,value:end");
            }
        }

        if (m_numberStack.size() == 0) {
            throw new RuntimeException("不能识别的公式，无运算结果");
        }
        if (m_numberStack.size() != 1) {
            throw new RuntimeException("公式存在错误或不完整，但是依然满足计算需求规则");
        }
        return m_numberStack.peek();
    }

    public double getLastResult() {
        if (m_numberStack.size() == 0)
            return 0;
        return m_numberStack.peek();
    }


    private String getFlag(String str, int base) {
        String ret = "";
        int strLen = str.length();
        if (isNumberChar(str.charAt(0), base) == true) {
            p_addCount = 0;
            return ret;
        }
        int i = 0;
        if (!(str.charAt(i) >= 'A' && str.charAt(i) <= 'Z')
                && !(str.charAt(i) >= 'a' && str.charAt(i) <= 'z')) {
            ret += str.charAt(i);
            p_addCount = 1;
            return ret;
        }
        while (i < strLen && isNumberChar(str.charAt(i), base) == false) {
            if (!(str.charAt(i) >= 'A' && str.charAt(i) <= 'Z')
                    && !(str.charAt(i) >= 'a' && str.charAt(i) <= 'z')) {
                break;
            }
            ret += str.charAt(i);
            i++;
        }
        p_addCount = i;
        return ret;
    }

    private double getNumber(String str, int base) {
        double ret = 0;
        if (isNumberChar(str.charAt(0), base) == false) {
            p_addCount = 0;
            return 0;
        }
        int i = 0;
        while (i < str.length() && isNumberChar(str.charAt(i), base)) {
            ret *= base;
            ret += getNumCharValue(str.charAt(i), base);
            i++;
        }
        if (i < str.length() && str.charAt(i) == '.') {
            i++;
            double lbit = 1;
            while (i < str.length() && isNumberChar(str.charAt(i), base)) {
                lbit *= base;
                ret += getNumCharValue(str.charAt(i), base) / lbit;
                i++;
            }
        }
        p_addCount = i;
        return ret;
    }

    private static int getNumCharValue(char ch, int base) {
        String map = "0123456789ABCDEF";
        if (ch >= 'a' && ch <= 'z')
            ch &= ~32;
        int i = 0;
        while (i < map.length() && i < base) {
            if (ch == map.charAt(i))
                return i;
            i++;
        }
        return -1;
    }

    private static boolean isNumberChar(char ch, int base) {
        String map = "0123456789ABCDEF";
        if (ch >= 'a' && ch <= 'z')
            ch &= ~32;
        int i = 0;
        while (i < map.length() && i < base) {
            if (ch == map.charAt(i))
                return true;
            i++;
        }
        return false;
    }

    private boolean isLegalOperator(String ope) {
        for (int i = 0; i < m_flags.size(); i++) {
            if (ope.equalsIgnoreCase(m_flags.get(i)))
                return true;
        }
        return false;
    }

    private boolean calculateOnceStack() {
        String top = m_flagStack.peek();
        if (isSingleNumberOperator(top))//单目运算符号，只取出一个操作数计算即可
        {
            if (m_numberStack.size() < 1) {
                return false;
            }
            double num2 = 0;
            double num1 = m_numberStack.pop();
            String ope = m_flagStack.pop();

            double result = calculateOperator(num1, num2, ope);
            m_numberStack.push(result);

        } else {
            if (m_numberStack.size() < 2) {
                return false;
            }
            double num2 = m_numberStack.pop();
            double num1 = m_numberStack.pop();
            String ope = m_flagStack.pop();

            double result = calculateOperator(num1, num2, ope);
            m_numberStack.push(result);
        }

        return true;
    }

    private boolean isSingleNumberOperator(String ope) {
        for (int i = m_indexOfSingleNumberOperatorBegin; i < m_flags.size(); i++) {
            if (ope.equalsIgnoreCase(m_flags.get(i)))
                return true;
        }
        return false;
    }

    private double calculateOperator(double num1, double num2, String ope) {
        double dc_NUM_PI = 3.141592653549626;
        double dc_NUM_E = 2.718281828459045;
        double dc_NUM_GSEC = 0.618033988749894;
        //+	-	*	/	%	^	(	)		sqrt	log	adds	muls		and	or	not	xor		!	neg	per	abs	radian	angle	sin	cos	tan	arcsin	arccos	arctan recip	epow	ln	numpi	nume	numgsec
        if (ope.equalsIgnoreCase("+")) {
            return num1 + num2;
        } else if (ope.equalsIgnoreCase("-")) {
            return num1 - num2;
        } else if (ope.equalsIgnoreCase("*")) {
            return num1 * num2;
        } else if (ope.equalsIgnoreCase("/")) {
            return num1 / num2;
        } else if (ope.equalsIgnoreCase("%")) {
            return ((long) num1) % ((long) num2);
        } else if (ope.equalsIgnoreCase("^")) {
            return Math.pow(num1, num2);
        } else if (ope.equalsIgnoreCase("sqrt"))//n次根号m==m的1/n次幂
        {
            return Math.pow(num2, 1.0 / num1);
        } else if (ope.equalsIgnoreCase("log")) {
            return Math.log(num2) / Math.log(num1);
        } else if (ope.equalsIgnoreCase("adds")) {
            double ret = 0;
            int step = num1 < num2 ? 1 : -1;
            long beg = (long) num1;
            long end = (long) num2;
            for (long i = beg; i <= end; i += step)
                ret += i;
            return ret;
        } else if (ope.equalsIgnoreCase("muls")) {
            double ret = 1;
            int step = num1 < num2 ? 1 : -1;
            long beg = (long) num1;
            long end = (long) num2;
            for (long i = beg; i <= end; i += step)
                ret *= i;
            return ret;
        } else if (ope.equalsIgnoreCase("and")) {
            return ((long) num1) & ((long) num2);
        } else if (ope.equalsIgnoreCase("or")) {
            return ((long) num1) | ((long) num2);
        } else if (ope.equalsIgnoreCase("xor")) {
            return ((long) num1) ^ ((long) num2);
        } else if (ope.equalsIgnoreCase("not")) {
            return ~((long) num1);
        } else if (ope.equalsIgnoreCase("!")) //!	neg	per	abs	radian	angle	sin	cos	tan	arcsin	arccos	arctan
        {
            double ret = 1;
            long beg = 1;
            long end = (long) num1;
            for (long i = beg; i <= end; i++)
                ret *= i;
            return ret;
        } else if (ope.equalsIgnoreCase("neg")) {
            return 0 - num1;
        } else if (ope.equalsIgnoreCase("per")) {
            return num1 / 100.0;
        } else if (ope.equalsIgnoreCase("abs")) {
            return Math.abs(num1);
        } else if (ope.equalsIgnoreCase("radian")) {
            return num1 / 180.0 * 3.141592653549;
        } else if (ope.equalsIgnoreCase("angle")) {
            return num1 / 3.141562653549 * 180.0;
        } else if (ope.equalsIgnoreCase("sin")) {
            return Math.sin(num1);
        } else if (ope.equalsIgnoreCase("cos")) {
            return Math.cos(num1);
        } else if (ope.equalsIgnoreCase("tan")) {
            return Math.tan(num1);
        } else if (ope.equalsIgnoreCase("arcsin")) {
            return Math.asin(num1);
        } else if (ope.equalsIgnoreCase("arccos")) {
            return Math.acos(num1);
        } else if (ope.equalsIgnoreCase("arctan")) {
            return Math.atan(num1);
        } else if (ope.equalsIgnoreCase("recip"))//recip	epow	ln	numpi	nume	numgsec
        {
            return 1.0 / num1;
        } else if (ope.equalsIgnoreCase("epow")) {
            return Math.pow(dc_NUM_E, num1);
        } else if (ope.equalsIgnoreCase("ln")) {
            return Math.log(num1) / Math.log(dc_NUM_E);
        } else if (ope.equalsIgnoreCase("numpi")) {
            return num1 * dc_NUM_PI;
        } else if (ope.equalsIgnoreCase("nume")) {
            return num1 * dc_NUM_E;
        } else if (ope.equalsIgnoreCase("numgsec")) {
            return num1 * dc_NUM_GSEC;
        }
        return 0;
    }

    private int getPriortyTag(String cur, String top) {
        int curI = getFlagIndex(cur);
        int topI = getFlagIndex(top);
        if (curI == -1 || topI == -1)
            return 99;
        return At(curI, topI);
    }

    private int getFlagIndex(String flg) {
        int ret = -1;
        for (int i = 0; i < m_flags.size(); i++) {
            if (flg.equalsIgnoreCase(m_flags.get(i))) {
                ret = i;
                break;
            }
        }
        return ret;
    }


    //cur line ,top row
    private char At(int cur, int top) {
        return m_ruleTable[cur * m_flags.size() + top];
    }

    private void At2(int cur, int top, char val) {
        m_ruleTable[cur * m_flags.size() + top] = val;
    }

    private int p_addCount;
    private int m_indexOfSingleNumberOperatorBegin;
    private char[] m_ruleTable;
    private List<String> m_flags;
    private Stack<Double> m_numberStack;
    private Stack<String> m_flagStack;
}

