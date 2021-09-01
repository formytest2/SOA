package com.tranboot.client.druid.sql.visitor;

import com.tranboot.client.druid.DruidRuntimeException;
import com.tranboot.client.druid.sql.SQLUtils;
import com.tranboot.client.druid.sql.ast.SQLExpr;
import com.tranboot.client.druid.sql.ast.SQLObject;
import com.tranboot.client.druid.sql.ast.expr.SQLBetweenExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLBinaryOperator;
import com.tranboot.client.druid.sql.ast.expr.SQLCaseExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCharExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLHexExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLIdentifierExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLInListExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNullExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLNumericLiteralExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLQueryExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLUnaryExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLUnaryOperator;
import com.tranboot.client.druid.sql.ast.expr.SQLVariantRefExpr;
import com.tranboot.client.druid.sql.ast.expr.SQLCaseExpr.Item;
import com.tranboot.client.druid.sql.ast.statement.SQLExprTableSource;
import com.tranboot.client.druid.sql.ast.statement.SQLSelect;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectItem;
import com.tranboot.client.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.tranboot.client.druid.sql.dialect.mysql.visitor.MySqlEvalVisitorImpl;
import com.tranboot.client.druid.sql.dialect.oracle.visitor.OracleEvalVisitor;
import com.tranboot.client.druid.sql.visitor.functions.Ascii;
import com.tranboot.client.druid.sql.visitor.functions.Bin;
import com.tranboot.client.druid.sql.visitor.functions.BitLength;
import com.tranboot.client.druid.sql.visitor.functions.Char;
import com.tranboot.client.druid.sql.visitor.functions.Concat;
import com.tranboot.client.druid.sql.visitor.functions.Elt;
import com.tranboot.client.druid.sql.visitor.functions.Function;
import com.tranboot.client.druid.sql.visitor.functions.Greatest;
import com.tranboot.client.druid.sql.visitor.functions.Hex;
import com.tranboot.client.druid.sql.visitor.functions.If;
import com.tranboot.client.druid.sql.visitor.functions.Insert;
import com.tranboot.client.druid.sql.visitor.functions.Instr;
import com.tranboot.client.druid.sql.visitor.functions.Isnull;
import com.tranboot.client.druid.sql.visitor.functions.Lcase;
import com.tranboot.client.druid.sql.visitor.functions.Least;
import com.tranboot.client.druid.sql.visitor.functions.Left;
import com.tranboot.client.druid.sql.visitor.functions.Length;
import com.tranboot.client.druid.sql.visitor.functions.Locate;
import com.tranboot.client.druid.sql.visitor.functions.Lpad;
import com.tranboot.client.druid.sql.visitor.functions.Ltrim;
import com.tranboot.client.druid.sql.visitor.functions.Now;
import com.tranboot.client.druid.sql.visitor.functions.OneParamFunctions;
import com.tranboot.client.druid.sql.visitor.functions.Reverse;
import com.tranboot.client.druid.sql.visitor.functions.Right;
import com.tranboot.client.druid.sql.visitor.functions.Substring;
import com.tranboot.client.druid.sql.visitor.functions.Trim;
import com.tranboot.client.druid.sql.visitor.functions.Ucase;
import com.tranboot.client.druid.sql.visitor.functions.Unhex;
import com.tranboot.client.druid.util.HexBin;
import com.tranboot.client.druid.util.Utils;
import com.tranboot.client.druid.wall.WallVisitor;
import com.tranboot.client.druid.wall.spi.WallVisitorUtils;
import com.tranboot.client.druid.wall.spi.WallVisitorUtils.WallConditionContext;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SQLEvalVisitorUtils {
    private static Map<String, Function> functions = new HashMap();

    public SQLEvalVisitorUtils() {
    }

    public static Object evalExpr(String dbType, String expr, Object... parameters) {
        SQLExpr sqlExpr = SQLUtils.toSQLExpr(expr, dbType);
        return eval(dbType, sqlExpr, (Object[])parameters);
    }

    public static Object evalExpr(String dbType, String expr, List<Object> parameters) {
        SQLExpr sqlExpr = SQLUtils.toSQLExpr(expr);
        return eval(dbType, sqlExpr, (List)parameters);
    }

    public static Object eval(String dbType, SQLObject sqlObject, Object... parameters) {
        Object value = eval(dbType, sqlObject, Arrays.asList(parameters));
        if (value == SQLEvalVisitor.EVAL_VALUE_NULL) {
            value = null;
        }

        return value;
    }

    public static Object getValue(SQLObject sqlObject) {
        return sqlObject instanceof SQLNumericLiteralExpr ? ((SQLNumericLiteralExpr)sqlObject).getNumber() : sqlObject.getAttributes().get("eval.value");
    }

    public static Object eval(String dbType, SQLObject sqlObject, List<Object> parameters) {
        return eval(dbType, sqlObject, parameters, true);
    }

    public static Object eval(String dbType, SQLObject sqlObject, List<Object> parameters, boolean throwError) {
        SQLEvalVisitor visitor = createEvalVisitor(dbType);
        visitor.setParameters(parameters);
        sqlObject.accept(visitor);
        Object value = getValue(sqlObject);
        if (value == null && throwError && !sqlObject.getAttributes().containsKey("eval.value")) {
            throw new DruidRuntimeException("eval error : " + SQLUtils.toSQLString(sqlObject, dbType));
        } else {
            return value;
        }
    }

    public static SQLEvalVisitor createEvalVisitor(String dbType) {
        if ("mysql".equals(dbType)) {
            return new MySqlEvalVisitorImpl();
        } else if ("mariadb".equals(dbType)) {
            return new MySqlEvalVisitorImpl();
        } else if ("h2".equals(dbType)) {
            return new MySqlEvalVisitorImpl();
        } else {
            return (SQLEvalVisitor)(!"oracle".equals(dbType) && !"AliOracle".equals(dbType) ? new SQLEvalVisitorImpl() : new OracleEvalVisitor());
        }
    }

    static void registerBaseFunctions() {
        functions.put("now", Now.instance);
        functions.put("concat", Concat.instance);
        functions.put("concat_ws", Concat.instance);
        functions.put("ascii", Ascii.instance);
        functions.put("bin", Bin.instance);
        functions.put("bit_length", BitLength.instance);
        functions.put("insert", Insert.instance);
        functions.put("instr", Instr.instance);
        functions.put("char", Char.instance);
        functions.put("elt", Elt.instance);
        functions.put("left", Left.instance);
        functions.put("locate", Locate.instance);
        functions.put("lpad", Lpad.instance);
        functions.put("ltrim", Ltrim.instance);
        functions.put("mid", Substring.instance);
        functions.put("substr", Substring.instance);
        functions.put("substring", Substring.instance);
        functions.put("right", Right.instance);
        functions.put("reverse", Reverse.instance);
        functions.put("len", Length.instance);
        functions.put("length", Length.instance);
        functions.put("char_length", Length.instance);
        functions.put("character_length", Length.instance);
        functions.put("trim", Trim.instance);
        functions.put("ucase", Ucase.instance);
        functions.put("upper", Ucase.instance);
        functions.put("lcase", Lcase.instance);
        functions.put("lower", Lcase.instance);
        functions.put("hex", Hex.instance);
        functions.put("unhex", Unhex.instance);
        functions.put("greatest", Greatest.instance);
        functions.put("least", Least.instance);
        functions.put("isnull", Isnull.instance);
        functions.put("if", If.instance);
        functions.put("md5", OneParamFunctions.instance);
        functions.put("bit_count", OneParamFunctions.instance);
        functions.put("soundex", OneParamFunctions.instance);
        functions.put("space", OneParamFunctions.instance);
    }

    public static boolean visit(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
        String methodName = x.getMethodName().toLowerCase();
        Function function = visitor.getFunction(methodName);
        if (function == null) {
            function = (Function)functions.get(methodName);
        }

        if (function != null) {
            Object result = function.eval(visitor, x);
            if (result != SQLEvalVisitor.EVAL_ERROR) {
                x.getAttributes().put("eval.value", result);
            }

            return false;
        } else {
            SQLExpr first;
            Object param1Value;
            SQLExpr param1;
            Object param0Value;
            if ("mod".equals(methodName)) {
                if (x.getParameters().size() != 2) {
                    return false;
                }

                first = (SQLExpr)x.getParameters().get(0);
                param1 = (SQLExpr)x.getParameters().get(1);
                first.accept(visitor);
                param1.accept(visitor);
                param0Value = first.getAttributes().get("eval.value");
                param1Value = param1.getAttributes().get("eval.value");
                if (param0Value == null || param1Value == null) {
                    return false;
                }

                long intValue0 = castToLong(param0Value);
                long intValue1 = castToLong(param1Value);
                long result = intValue0 % intValue1;
                if (result >= -2147483648L && result <= 2147483647L) {
                    int intResult = (int)result;
                    x.putAttribute("eval.value", intResult);
                } else {
                    x.putAttribute("eval.value", result);
                }
            } else {
                Object firstResult;
                if ("abs".equals(methodName)) {
                    if (x.getParameters().size() != 1) {
                        return false;
                    }

                    first = (SQLExpr)x.getParameters().get(0);
                    first.accept(visitor);
                    firstResult = first.getAttributes().get("eval.value");
                    if (firstResult == null) {
                        return false;
                    }

                    if (firstResult instanceof Integer) {
                        param0Value = Math.abs((Integer)firstResult);
                    } else if (firstResult instanceof Long) {
                        param0Value = Math.abs((Long)firstResult);
                    } else {
                        param0Value = castToDecimal(firstResult).abs();
                    }

                    x.putAttribute("eval.value", param0Value);
                } else {
                    double doubleValue;
                    double doubleValue0;
                    if ("acos".equals(methodName)) {
                        if (x.getParameters().size() != 1) {
                            return false;
                        }

                        first = (SQLExpr)x.getParameters().get(0);
                        first.accept(visitor);
                        firstResult = first.getAttributes().get("eval.value");
                        if (firstResult == null) {
                            return false;
                        }

                        doubleValue = castToDouble(firstResult);
                        doubleValue0 = Math.acos(doubleValue);
                        if (Double.isNaN(doubleValue0)) {
                            x.putAttribute("eval.value", (Object)null);
                        } else {
                            x.putAttribute("eval.value", doubleValue0);
                        }
                    } else if ("asin".equals(methodName)) {
                        if (x.getParameters().size() != 1) {
                            return false;
                        }

                        first = (SQLExpr)x.getParameters().get(0);
                        first.accept(visitor);
                        firstResult = first.getAttributes().get("eval.value");
                        if (firstResult == null) {
                            return false;
                        }

                        doubleValue = castToDouble(firstResult);
                        doubleValue0 = Math.asin(doubleValue);
                        if (Double.isNaN(doubleValue0)) {
                            x.putAttribute("eval.value", (Object)null);
                        } else {
                            x.putAttribute("eval.value", doubleValue0);
                        }
                    } else if ("atan".equals(methodName)) {
                        if (x.getParameters().size() != 1) {
                            return false;
                        }

                        first = (SQLExpr)x.getParameters().get(0);
                        first.accept(visitor);
                        firstResult = first.getAttributes().get("eval.value");
                        if (firstResult == null) {
                            return false;
                        }

                        doubleValue = castToDouble(firstResult);
                        doubleValue0 = Math.atan(doubleValue);
                        if (Double.isNaN(doubleValue0)) {
                            x.putAttribute("eval.value", (Object)null);
                        } else {
                            x.putAttribute("eval.value", doubleValue0);
                        }
                    } else {
                        double doubleValue1;
                        double result;
                        if ("atan2".equals(methodName)) {
                            if (x.getParameters().size() != 2) {
                                return false;
                            }

                            first = (SQLExpr)x.getParameters().get(0);
                            param1 = (SQLExpr)x.getParameters().get(1);
                            first.accept(visitor);
                            param1.accept(visitor);
                            param0Value = first.getAttributes().get("eval.value");
                            param1Value = param1.getAttributes().get("eval.value");
                            if (param0Value == null || param1Value == null) {
                                return false;
                            }

                            doubleValue0 = castToDouble(param0Value);
                            doubleValue1 = castToDouble(param1Value);
                            result = Math.atan2(doubleValue0, doubleValue1);
                            if (Double.isNaN(result)) {
                                x.putAttribute("eval.value", (Object)null);
                            } else {
                                x.putAttribute("eval.value", result);
                            }
                        } else if (!"ceil".equals(methodName) && !"ceiling".equals(methodName)) {
                            if ("cos".equals(methodName)) {
                                if (x.getParameters().size() != 1) {
                                    return false;
                                }

                                first = (SQLExpr)x.getParameters().get(0);
                                first.accept(visitor);
                                firstResult = first.getAttributes().get("eval.value");
                                if (firstResult == null) {
                                    return false;
                                }

                                doubleValue = castToDouble(firstResult);
                                doubleValue0 = Math.cos(doubleValue);
                                if (Double.isNaN(doubleValue0)) {
                                    x.putAttribute("eval.value", (Object)null);
                                } else {
                                    x.putAttribute("eval.value", doubleValue0);
                                }
                            } else if ("sin".equals(methodName)) {
                                if (x.getParameters().size() != 1) {
                                    return false;
                                }

                                first = (SQLExpr)x.getParameters().get(0);
                                first.accept(visitor);
                                firstResult = first.getAttributes().get("eval.value");
                                if (firstResult == null) {
                                    return false;
                                }

                                doubleValue = castToDouble(firstResult);
                                doubleValue0 = Math.sin(doubleValue);
                                if (Double.isNaN(doubleValue0)) {
                                    x.putAttribute("eval.value", (Object)null);
                                } else {
                                    x.putAttribute("eval.value", doubleValue0);
                                }
                            } else if ("log".equals(methodName)) {
                                if (x.getParameters().size() != 1) {
                                    return false;
                                }

                                first = (SQLExpr)x.getParameters().get(0);
                                first.accept(visitor);
                                firstResult = first.getAttributes().get("eval.value");
                                if (firstResult == null) {
                                    return false;
                                }

                                doubleValue = castToDouble(firstResult);
                                doubleValue0 = Math.log(doubleValue);
                                if (Double.isNaN(doubleValue0)) {
                                    x.putAttribute("eval.value", (Object)null);
                                } else {
                                    x.putAttribute("eval.value", doubleValue0);
                                }
                            } else if ("log10".equals(methodName)) {
                                if (x.getParameters().size() != 1) {
                                    return false;
                                }

                                first = (SQLExpr)x.getParameters().get(0);
                                first.accept(visitor);
                                firstResult = first.getAttributes().get("eval.value");
                                if (firstResult == null) {
                                    return false;
                                }

                                doubleValue = castToDouble(firstResult);
                                doubleValue0 = Math.log10(doubleValue);
                                if (Double.isNaN(doubleValue0)) {
                                    x.putAttribute("eval.value", (Object)null);
                                } else {
                                    x.putAttribute("eval.value", doubleValue0);
                                }
                            } else if ("tan".equals(methodName)) {
                                if (x.getParameters().size() != 1) {
                                    return false;
                                }

                                first = (SQLExpr)x.getParameters().get(0);
                                first.accept(visitor);
                                firstResult = first.getAttributes().get("eval.value");
                                if (firstResult == null) {
                                    return false;
                                }

                                doubleValue = castToDouble(firstResult);
                                doubleValue0 = Math.tan(doubleValue);
                                if (Double.isNaN(doubleValue0)) {
                                    x.putAttribute("eval.value", (Object)null);
                                } else {
                                    x.putAttribute("eval.value", doubleValue0);
                                }
                            } else if ("sqrt".equals(methodName)) {
                                if (x.getParameters().size() != 1) {
                                    return false;
                                }

                                first = (SQLExpr)x.getParameters().get(0);
                                first.accept(visitor);
                                firstResult = first.getAttributes().get("eval.value");
                                if (firstResult == null) {
                                    return false;
                                }

                                doubleValue = castToDouble(firstResult);
                                doubleValue0 = Math.sqrt(doubleValue);
                                if (Double.isNaN(doubleValue0)) {
                                    x.putAttribute("eval.value", (Object)null);
                                } else {
                                    x.putAttribute("eval.value", doubleValue0);
                                }
                            } else if (!"power".equals(methodName) && !"pow".equals(methodName)) {
                                if ("pi".equals(methodName)) {
                                    x.putAttribute("eval.value", 3.141592653589793D);
                                } else if ("rand".equals(methodName)) {
                                    x.putAttribute("eval.value", Math.random());
                                } else if ("chr".equals(methodName) && x.getParameters().size() == 1) {
                                    first = (SQLExpr)x.getParameters().get(0);
                                    firstResult = getValue(first);
                                    if (firstResult instanceof Number) {
                                        int intValue = ((Number)firstResult).intValue();
                                        char ch = (char)intValue;
                                        x.putAttribute("eval.value", Character.toString(ch));
                                    }
                                } else if ("current_user".equals(methodName)) {
                                    x.putAttribute("eval.value", "CURRENT_USER");
                                }
                            } else {
                                if (x.getParameters().size() != 2) {
                                    return false;
                                }

                                first = (SQLExpr)x.getParameters().get(0);
                                param1 = (SQLExpr)x.getParameters().get(1);
                                first.accept(visitor);
                                param1.accept(visitor);
                                param0Value = first.getAttributes().get("eval.value");
                                param1Value = param1.getAttributes().get("eval.value");
                                if (param0Value == null || param1Value == null) {
                                    return false;
                                }

                                doubleValue0 = castToDouble(param0Value);
                                doubleValue1 = castToDouble(param1Value);
                                result = Math.pow(doubleValue0, doubleValue1);
                                if (Double.isNaN(result)) {
                                    x.putAttribute("eval.value", (Object)null);
                                } else {
                                    x.putAttribute("eval.value", result);
                                }
                            }
                        } else {
                            if (x.getParameters().size() != 1) {
                                return false;
                            }

                            first = (SQLExpr)x.getParameters().get(0);
                            first.accept(visitor);
                            firstResult = first.getAttributes().get("eval.value");
                            if (firstResult == null) {
                                return false;
                            }

                            doubleValue = castToDouble(firstResult);
                            result = (int)Math.ceil(doubleValue);
                            if (Double.isNaN((double)result)) {
                                x.putAttribute("eval.value", (Object)null);
                            } else {
                                x.putAttribute("eval.value", result);
                            }
                        }
                    }
                }
            }

            return false;
        }
    }

    public static boolean visit(SQLEvalVisitor visitor, SQLCharExpr x) {
        x.putAttribute("eval.value", x.getText());
        return true;
    }

    public static boolean visit(SQLEvalVisitor visitor, SQLHexExpr x) {
        String hex = x.getHex();
        byte[] bytes = HexBin.decode(hex);
        if (bytes == null) {
            x.putAttribute("eval.value", SQLEvalVisitor.EVAL_ERROR);
        } else {
            String val = new String(bytes);
            x.putAttribute("eval.value", val);
        }

        return true;
    }

    public static boolean visit(SQLEvalVisitor visitor, SQLBinaryExpr x) {
        String text = x.getValue();
        long[] words = new long[text.length() / 64 + 1];

        int i;
        for(i = 0; i < text.length(); ++i) {
            char ch = text.charAt(i);
            if (ch == '1') {
                i = i >> 6;
                words[i] |= 1L << i;
            }
        }

        Object val;
        if (words.length == 1) {
            val = words[0];
        } else {
            byte[] bytes = new byte[words.length * 8];

            for(i = 0; i < words.length; ++i) {
                Utils.putLong(bytes, i * 8, words[i]);
            }

            val = new BigInteger(bytes);
        }

        x.putAttribute("eval.value", val);
        return false;
    }

    public static SQLExpr unwrap(SQLExpr expr) {
        if (expr == null) {
            return null;
        } else {
            if (expr instanceof SQLQueryExpr) {
                SQLSelect select = ((SQLQueryExpr)expr).getSubQuery();
                if (select == null) {
                    return null;
                }

                if (select.getQuery() instanceof SQLSelectQueryBlock) {
                    SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)select.getQuery();
                    if (queryBlock.getFrom() == null && queryBlock.getSelectList().size() == 1) {
                        return ((SQLSelectItem)queryBlock.getSelectList().get(0)).getExpr();
                    }
                }
            }

            return expr;
        }
    }

    public static boolean visit(SQLEvalVisitor visitor, SQLBetweenExpr x) {
        SQLExpr testExpr = unwrap(x.getTestExpr());
        testExpr.accept(visitor);
        if (!testExpr.getAttributes().containsKey("eval.value")) {
            return false;
        } else {
            Object value = testExpr.getAttribute("eval.value");
            SQLExpr beginExpr = unwrap(x.getBeginExpr());
            beginExpr.accept(visitor);
            if (!beginExpr.getAttributes().containsKey("eval.value")) {
                return false;
            } else {
                Object begin = beginExpr.getAttribute("eval.value");
                if (lt(value, begin)) {
                    x.getAttributes().put("eval.value", x.isNot());
                    return false;
                } else {
                    SQLExpr endExpr = unwrap(x.getEndExpr());
                    endExpr.accept(visitor);
                    if (!endExpr.getAttributes().containsKey("eval.value")) {
                        return false;
                    } else {
                        Object end = endExpr.getAttribute("eval.value");
                        if (gt(value, end)) {
                            x.getAttributes().put("eval.value", x.isNot());
                            return false;
                        } else {
                            x.getAttributes().put("eval.value", !x.isNot());
                            return false;
                        }
                    }
                }
            }
        }
    }

    public static boolean visit(SQLEvalVisitor visitor, SQLNullExpr x) {
        x.getAttributes().put("eval.value", SQLEvalVisitor.EVAL_VALUE_NULL);
        return false;
    }

    public static boolean visit(SQLEvalVisitor visitor, SQLCaseExpr x) {
        Object value;
        if (x.getValueExpr() != null) {
            x.getValueExpr().accept(visitor);
            if (!x.getValueExpr().getAttributes().containsKey("eval.value")) {
                return false;
            }

            value = x.getValueExpr().getAttribute("eval.value");
        } else {
            value = null;
        }

        Iterator var3 = x.getItems().iterator();

        Item item;
        Object conditionValue;
        do {
            if (!var3.hasNext()) {
                if (x.getElseExpr() != null) {
                    x.getElseExpr().accept(visitor);
                    if (x.getElseExpr().getAttributes().containsKey("eval.value")) {
                        x.getAttributes().put("eval.value", x.getElseExpr().getAttribute("eval.value"));
                    }
                }

                return false;
            }

            item = (Item)var3.next();
            item.getConditionExpr().accept(visitor);
            if (!item.getConditionExpr().getAttributes().containsKey("eval.value")) {
                return false;
            }

            conditionValue = item.getConditionExpr().getAttribute("eval.value");
        } while((x.getValueExpr() == null || !eq(value, conditionValue)) && (x.getValueExpr() != null || !(conditionValue instanceof Boolean) || (Boolean)conditionValue != Boolean.TRUE));

        item.getValueExpr().accept(visitor);
        if (item.getValueExpr().getAttributes().containsKey("eval.value")) {
            x.getAttributes().put("eval.value", item.getValueExpr().getAttribute("eval.value"));
        }

        return false;
    }

    public static boolean visit(SQLEvalVisitor visitor, SQLInListExpr x) {
        SQLExpr valueExpr = x.getExpr();
        valueExpr.accept(visitor);
        if (!valueExpr.getAttributes().containsKey("eval.value")) {
            return false;
        } else {
            Object value = valueExpr.getAttribute("eval.value");
            Iterator var4 = x.getTargetList().iterator();

            Object itemValue;
            do {
                if (!var4.hasNext()) {
                    x.getAttributes().put("eval.value", x.isNot());
                    return false;
                }

                SQLExpr item = (SQLExpr)var4.next();
                item.accept(visitor);
                if (!item.getAttributes().containsKey("eval.value")) {
                    return false;
                }

                itemValue = item.getAttribute("eval.value");
            } while(!eq(value, itemValue));

            x.getAttributes().put("eval.value", !x.isNot());
            return false;
        }
    }

    public static boolean visit(SQLEvalVisitor visitor, SQLQueryExpr x) {
        if (WallVisitorUtils.isSimpleCountTableSource((WallVisitor)null, x.getSubQuery())) {
            x.putAttribute("eval.value", 1);
            return false;
        } else {
            if (x.getSubQuery().getQuery() instanceof SQLSelectQueryBlock) {
                SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)x.getSubQuery().getQuery();
                boolean nullFrom = false;
                if (queryBlock.getFrom() == null) {
                    nullFrom = true;
                } else if (queryBlock.getFrom() instanceof SQLExprTableSource) {
                    SQLExpr expr = ((SQLExprTableSource)queryBlock.getFrom()).getExpr();
                    if (expr instanceof SQLIdentifierExpr && "dual".equalsIgnoreCase(((SQLIdentifierExpr)expr).getName())) {
                        nullFrom = true;
                    }
                }

                if (nullFrom) {
                    List<Object> row = new ArrayList(queryBlock.getSelectList().size());

                    for(int i = 0; i < queryBlock.getSelectList().size(); ++i) {
                        SQLSelectItem item = (SQLSelectItem)queryBlock.getSelectList().get(i);
                        item.getExpr().accept(visitor);
                        Object cell = item.getExpr().getAttribute("eval.value");
                        row.add(cell);
                    }

                    List<List<Object>> rows = new ArrayList(1);
                    rows.add(row);
                    queryBlock.putAttribute("eval.value", rows);
                    x.getSubQuery().putAttribute("eval.value", rows);
                    x.putAttribute("eval.value", rows);
                    return false;
                }
            }

            return false;
        }
    }

    public static boolean visit(SQLEvalVisitor visitor, SQLUnaryExpr x) {
        WallConditionContext wallConditionContext = WallVisitorUtils.getWallConditionContext();
        if (x.getOperator() == SQLUnaryOperator.Compl && wallConditionContext != null) {
            wallConditionContext.setBitwise(true);
        }

        x.getExpr().accept(visitor);
        Object val = x.getExpr().getAttribute("eval.value");
        if (val == SQLEvalVisitor.EVAL_ERROR) {
            x.putAttribute("eval.value", SQLEvalVisitor.EVAL_ERROR);
            return false;
        } else if (val == null) {
            x.putAttribute("eval.value", SQLEvalVisitor.EVAL_VALUE_NULL);
            return false;
        } else {
            switch(x.getOperator()) {
                case BINARY:
                case RAW:
                    x.putAttribute("eval.value", val);
                    break;
                case NOT:
                case Not:
                    Boolean booleanVal = castToBoolean(val);
                    if (booleanVal != null) {
                        x.putAttribute("eval.value", !booleanVal);
                    }
                    break;
                case Plus:
                    x.putAttribute("eval.value", val);
                    break;
                case Negative:
                    x.putAttribute("eval.value", multi(val, -1));
                    break;
                case Compl:
                    x.putAttribute("eval.value", ~castToInteger(val));
            }

            return false;
        }
    }

    public static boolean visit(SQLEvalVisitor visitor, SQLBinaryOpExpr x) {
        SQLExpr left = unwrap(x.getLeft());
        SQLExpr right = unwrap(x.getRight());
        left.accept(visitor);
        right.accept(visitor);
        WallConditionContext wallConditionContext = WallVisitorUtils.getWallConditionContext();
        if (x.getOperator() == SQLBinaryOperator.BooleanOr) {
            if (wallConditionContext != null && (left.getAttribute("eval.value") == Boolean.TRUE || right.getAttribute("eval.value") == Boolean.TRUE)) {
                wallConditionContext.setPartAlwayTrue(true);
            }
        } else if (x.getOperator() == SQLBinaryOperator.BooleanAnd) {
            if (wallConditionContext != null && (left.getAttribute("eval.value") == Boolean.FALSE || right.getAttribute("eval.value") == Boolean.FALSE)) {
                wallConditionContext.setPartAlwayFalse(true);
            }
        } else if (x.getOperator() == SQLBinaryOperator.BooleanXor) {
            if (wallConditionContext != null) {
                wallConditionContext.setXor(true);
            }
        } else if ((x.getOperator() == SQLBinaryOperator.BitwiseAnd || x.getOperator() == SQLBinaryOperator.BitwiseNot || x.getOperator() == SQLBinaryOperator.BitwiseOr || x.getOperator() == SQLBinaryOperator.BitwiseXor) && wallConditionContext != null) {
            wallConditionContext.setBitwise(true);
        }

        Object leftValue = left.getAttribute("eval.value");
        Object rightValue = right.getAttributes().get("eval.value");
        if (x.getOperator() == SQLBinaryOperator.Like && isAlwayTrueLikePattern(x.getRight())) {
            x.putAttribute("hasTrueLike", Boolean.TRUE);
            x.putAttribute("eval.value", Boolean.TRUE);
            return false;
        } else if (x.getOperator() == SQLBinaryOperator.NotLike && isAlwayTrueLikePattern(x.getRight())) {
            x.putAttribute("eval.value", Boolean.FALSE);
            return false;
        } else {
            boolean leftHasValue = left.getAttributes().containsKey("eval.value");
            boolean rightHasValue = right.getAttributes().containsKey("eval.value");
            if (!leftHasValue && !rightHasValue) {
                SQLExpr leftEvalExpr = (SQLExpr)left.getAttribute("eval.expr");
                SQLExpr rightEvalExpr = (SQLExpr)right.getAttribute("eval.expr");
                if (leftEvalExpr != null && leftEvalExpr.equals(rightEvalExpr)) {
                    switch(x.getOperator()) {
                        case Like:
                        case Equality:
                        case GreaterThanOrEqual:
                        case LessThanOrEqual:
                        case NotLessThan:
                        case NotGreaterThan:
                            x.putAttribute("eval.value", Boolean.TRUE);
                            return false;
                        case NotEqual:
                        case NotLike:
                        case GreaterThan:
                        case LessThan:
                            x.putAttribute("eval.value", Boolean.FALSE);
                            return false;
                    }
                }
            }

            if (!leftHasValue) {
                return false;
            } else if (!rightHasValue) {
                return false;
            } else {
                if (wallConditionContext != null) {
                    wallConditionContext.setConstArithmetic(true);
                }

                leftValue = processValue(leftValue);
                rightValue = processValue(rightValue);
                if (leftValue != null && rightValue != null) {
                    boolean second;
                    boolean matchResult;
                    boolean first;
                    String result;
                    String input;
                    Object value = null;
                    switch(x.getOperator()) {
                        case Like:
                            result = castToString(rightValue);
                            input = castToString(left.getAttributes().get("eval.value"));
                            matchResult = like(input, result);
                            x.putAttribute("eval.value", matchResult);
                            break;
                        case Equality:
                            value = eq(leftValue, rightValue);
                            x.putAttribute("eval.value", value);
                            break;
                        case GreaterThanOrEqual:
                            value = gteq(leftValue, rightValue);
                            x.putAttribute("eval.value", value);
                            break;
                        case LessThanOrEqual:
                            value = lteq(leftValue, rightValue);
                            x.putAttribute("eval.value", value);
                        case NotLessThan:
                        case NotGreaterThan:
                        default:
                            break;
                        case NotEqual:
                            value = !eq(leftValue, rightValue);
                            x.putAttribute("eval.value", value);
                            break;
                        case NotLike:
                            result = castToString(rightValue);
                            input = castToString(left.getAttributes().get("eval.value"));
                            matchResult = !like(input, result);
                            x.putAttribute("eval.value", matchResult);
                            break;
                        case GreaterThan:
                            value = gt(leftValue, rightValue);
                            x.putAttribute("eval.value", value);
                            break;
                        case LessThan:
                            value = lt(leftValue, rightValue);
                            x.putAttribute("eval.value", value);
                            break;
                        case Add:
                            value = add(leftValue, rightValue);
                            x.putAttribute("eval.value", value);
                            break;
                        case Subtract:
                            value = sub(leftValue, rightValue);
                            x.putAttribute("eval.value", value);
                            break;
                        case Multiply:
                            value = multi(leftValue, rightValue);
                            x.putAttribute("eval.value", value);
                            break;
                        case Divide:
                            value = div(leftValue, rightValue);
                            x.putAttribute("eval.value", value);
                            break;
                        case RightShift:
                            value = rightShift(leftValue, rightValue);
                            x.putAttribute("eval.value", value);
                            break;
                        case BitwiseAnd:
                            value = bitAnd(leftValue, rightValue);
                            x.putAttribute("eval.value", value);
                            break;
                        case BitwiseOr:
                            value = bitOr(leftValue, rightValue);
                            x.putAttribute("eval.value", value);
                            break;
                        case Is:
                            if (rightValue == SQLEvalVisitor.EVAL_VALUE_NULL && leftValue != null) {
                                value = leftValue == SQLEvalVisitor.EVAL_VALUE_NULL;
                                x.putAttribute("eval.value", value);
                            }
                            break;
                        case IsNot:
                            if (leftValue == SQLEvalVisitor.EVAL_VALUE_NULL) {
                                x.putAttribute("eval.value", false);
                            } else if (leftValue != null) {
                                x.putAttribute("eval.value", true);
                            }
                            break;
                        case RegExp:
                        case RLike:
                            result = castToString(rightValue);
                            input = castToString(left.getAttributes().get("eval.value"));
                            matchResult = Pattern.matches(result, input);
                            x.putAttribute("eval.value", matchResult);
                            break;
                        case NotRegExp:
                        case NotRLike:
                            result = castToString(rightValue);
                            input = castToString(left.getAttributes().get("eval.value"));
                            matchResult = !Pattern.matches(result, input);
                            x.putAttribute("eval.value", matchResult);
                            break;
                        case Concat:
                            result = leftValue.toString() + rightValue.toString();
                            x.putAttribute("eval.value", result);
                            break;
                        case BooleanAnd:
                            first = eq(leftValue, true);
                            second = eq(rightValue, true);
                            x.putAttribute("eval.value", first && second);
                            break;
                        case BooleanOr:
                            first = eq(leftValue, true);
                            second = eq(rightValue, true);
                            x.putAttribute("eval.value", first || second);
                    }

                    return false;
                } else {
                    return false;
                }
            }
        }
    }

    private static Object processValue(Object value) {
        if (value instanceof List) {
            List list = (List)value;
            if (list.size() == 1) {
                return processValue(list.get(0));
            }
        } else if (value instanceof Date) {
            return ((Date)value).getTime();
        }

        return value;
    }

    private static boolean isAlwayTrueLikePattern(SQLExpr x) {
        if (x instanceof SQLCharExpr) {
            String text = ((SQLCharExpr)x).getText();
            if (text.length() > 0) {
                char[] var2 = text.toCharArray();
                int var3 = var2.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    char ch = var2[var4];
                    if (ch != '%') {
                        return false;
                    }
                }

                return true;
            }
        }

        return false;
    }

    public static boolean visit(SQLEvalVisitor visitor, SQLNumericLiteralExpr x) {
        x.getAttributes().put("eval.value", x.getNumber());
        return false;
    }

    public static boolean visit(SQLEvalVisitor visitor, SQLVariantRefExpr x) {
        if (!"?".equals(x.getName())) {
            return false;
        } else {
            Map<String, Object> attributes = x.getAttributes();
            int varIndex = x.getIndex();
            if (varIndex != -1 && visitor.getParameters().size() > varIndex) {
                boolean containsValue = attributes.containsKey("eval.value");
                if (!containsValue) {
                    Object value = visitor.getParameters().get(varIndex);
                    if (value == null) {
                        value = SQLEvalVisitor.EVAL_VALUE_NULL;
                    }

                    attributes.put("eval.value", value);
                }
            }

            return false;
        }
    }

    public static Boolean castToBoolean(Object val) {
        if (val == null) {
            return null;
        } else if (val == SQLEvalVisitor.EVAL_VALUE_NULL) {
            return null;
        } else if (val instanceof Boolean) {
            return (Boolean)val;
        } else if (val instanceof Number) {
            return ((Number)val).intValue() > 0;
        } else if (val instanceof String) {
            return !"1".equals(val) && !"true".equalsIgnoreCase((String)val) ? false : true;
        } else {
            throw new IllegalArgumentException(val.getClass() + " not supported.");
        }
    }

    public static String castToString(Object val) {
        return val == null ? null : val.toString();
    }

    public static Byte castToByte(Object val) {
        if (val == null) {
            return null;
        } else if (val instanceof Byte) {
            return (Byte)val;
        } else {
            return val instanceof String ? Byte.parseByte((String)val) : ((Number)val).byteValue();
        }
    }

    public static Short castToShort(Object val) {
        if (val != null && val != SQLEvalVisitor.EVAL_VALUE_NULL) {
            if (val instanceof Short) {
                return (Short)val;
            } else {
                return val instanceof String ? Short.parseShort((String)val) : ((Number)val).shortValue();
            }
        } else {
            return null;
        }
    }

    public static Integer castToInteger(Object val) {
        if (val == null) {
            return null;
        } else if (val instanceof Integer) {
            return (Integer)val;
        } else if (val instanceof String) {
            return Integer.parseInt((String)val);
        } else {
            if (val instanceof List) {
                List list = (List)val;
                if (list.size() == 1) {
                    return castToInteger(list.get(0));
                }
            }

            if (val instanceof Boolean) {
                return (Boolean)val ? 1 : 0;
            } else if (val instanceof Number) {
                return ((Number)val).intValue();
            } else {
                throw new DruidRuntimeException("cast error");
            }
        }
    }

    public static Long castToLong(Object val) {
        if (val == null) {
            return null;
        } else if (val instanceof Long) {
            return (Long)val;
        } else if (val instanceof String) {
            return Long.parseLong((String)val);
        } else {
            if (val instanceof List) {
                List list = (List)val;
                if (list.size() == 1) {
                    return castToLong(list.get(0));
                }
            }

            if (val instanceof Boolean) {
                return (Boolean)val ? 1L : 0L;
            } else {
                return ((Number)val).longValue();
            }
        }
    }

    public static Float castToFloat(Object val) {
        if (val != null && val != SQLEvalVisitor.EVAL_VALUE_NULL) {
            return val instanceof Float ? (Float)val : ((Number)val).floatValue();
        } else {
            return null;
        }
    }

    public static Double castToDouble(Object val) {
        if (val != null && val != SQLEvalVisitor.EVAL_VALUE_NULL) {
            return val instanceof Double ? (Double)val : ((Number)val).doubleValue();
        } else {
            return null;
        }
    }

    public static BigInteger castToBigInteger(Object val) {
        if (val == null) {
            return null;
        } else if (val instanceof BigInteger) {
            return (BigInteger)val;
        } else {
            return val instanceof String ? new BigInteger((String)val) : BigInteger.valueOf(((Number)val).longValue());
        }
    }

    public static Number castToNumber(String val) {
        if (val == null) {
            return null;
        } else {
            try {
                return Byte.parseByte(val);
            } catch (NumberFormatException var9) {
                try {
                    return Short.parseShort(val);
                } catch (NumberFormatException var8) {
                    try {
                        return Integer.parseInt(val);
                    } catch (NumberFormatException var7) {
                        try {
                            return Long.parseLong(val);
                        } catch (NumberFormatException var6) {
                            try {
                                return Float.parseFloat(val);
                            } catch (NumberFormatException var5) {
                                try {
                                    return Double.parseDouble(val);
                                } catch (NumberFormatException var4) {
                                    try {
                                        return new BigInteger(val);
                                    } catch (NumberFormatException var3) {
                                        try {
                                            return new BigDecimal(val);
                                        } catch (NumberFormatException var2) {
                                            return 0;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static Date castToDate(Object val) {
        if (val == null) {
            return null;
        } else if (val instanceof Date) {
            return (Date)val;
        } else if (val instanceof Number) {
            return new Date(((Number)val).longValue());
        } else if (val instanceof String) {
            return castToDate((String)val);
        } else {
            throw new DruidRuntimeException("can cast to date");
        }
    }

    public static Date castToDate(String text) {
        if (text != null && text.length() != 0) {
            String format;
            if (text.length() == "yyyy-MM-dd".length()) {
                format = "yyyy-MM-dd";
            } else {
                format = "yyyy-MM-dd HH:mm:ss";
            }

            try {
                return (new SimpleDateFormat(format)).parse(text);
            } catch (ParseException var3) {
                throw new DruidRuntimeException("format : " + format + ", value : " + text, var3);
            }
        } else {
            return null;
        }
    }

    public static BigDecimal castToDecimal(Object val) {
        if (val == null) {
            return null;
        } else if (val instanceof BigDecimal) {
            return (BigDecimal)val;
        } else if (val instanceof String) {
            return new BigDecimal((String)val);
        } else if (val instanceof Float) {
            return new BigDecimal((double)(Float)val);
        } else {
            return val instanceof Double ? new BigDecimal((Double)val) : BigDecimal.valueOf(((Number)val).longValue());
        }
    }

    public static Object rightShift(Object a, Object b) {
        if (a != null && b != null) {
            return !(a instanceof Long) && !(b instanceof Long) ? castToInteger(a) >> castToInteger(b) : castToLong(a) >> castToLong(b);
        } else {
            return null;
        }
    }

    public static Object bitAnd(Object a, Object b) {
        if (a != null && b != null) {
            if (a != SQLEvalVisitor.EVAL_VALUE_NULL && b != SQLEvalVisitor.EVAL_VALUE_NULL) {
                if (a instanceof String) {
                    a = castToNumber((String)a);
                }

                if (b instanceof String) {
                    b = castToNumber((String)b);
                }

                return !(a instanceof Long) && !(b instanceof Long) ? castToInteger(a) & castToInteger(b) : castToLong(a) & castToLong(b);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static Object bitOr(Object a, Object b) {
        if (a != null && b != null) {
            if (a != SQLEvalVisitor.EVAL_VALUE_NULL && b != SQLEvalVisitor.EVAL_VALUE_NULL) {
                if (a instanceof String) {
                    a = castToNumber((String)a);
                }

                if (b instanceof String) {
                    b = castToNumber((String)b);
                }

                return !(a instanceof Long) && !(b instanceof Long) ? castToInteger(a) | castToInteger(b) : castToLong(a) | castToLong(b);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static Object div(Object a, Object b) {
        if (a != null && b != null) {
            if (a != SQLEvalVisitor.EVAL_VALUE_NULL && b != SQLEvalVisitor.EVAL_VALUE_NULL) {
                if (a instanceof String) {
                    a = castToNumber((String)a);
                }

                if (b instanceof String) {
                    b = castToNumber((String)b);
                }

                if (!(a instanceof BigDecimal) && !(b instanceof BigDecimal)) {
                    if (!(a instanceof Double) && !(b instanceof Double)) {
                        if (!(a instanceof Float) && !(b instanceof Float)) {
                            if (!(a instanceof BigInteger) && !(b instanceof BigInteger)) {
                                if (!(a instanceof Long) && !(b instanceof Long)) {
                                    if (!(a instanceof Integer) && !(b instanceof Integer)) {
                                        if (!(a instanceof Short) && !(b instanceof Short)) {
                                            if (!(a instanceof Byte) && !(b instanceof Byte)) {
                                                throw new IllegalArgumentException(a.getClass() + " and " + b.getClass() + " not supported.");
                                            } else {
                                                return castToByte(a) / castToByte(b);
                                            }
                                        } else {
                                            return castToShort(a) / castToShort(b);
                                        }
                                    } else {
                                        Integer intA = castToInteger(a);
                                        Integer intB = castToInteger(b);
                                        if (intB == 0) {
                                            if (intA > 0) {
                                                return 1.0D / 0.0;
                                            } else {
                                                return intA < 0 ? -1.0D / 0.0 : 0.0D / 0.0;
                                            }
                                        } else {
                                            return intA / intB;
                                        }
                                    }
                                } else {
                                    Long longA = castToLong(a);
                                    Long longB = castToLong(b);
                                    if (longB == 0L) {
                                        if (longA > 0L) {
                                            return 1.0D / 0.0;
                                        } else {
                                            return longA < 0L ? -1.0D / 0.0 : 0.0D / 0.0;
                                        }
                                    } else {
                                        return longA / longB;
                                    }
                                }
                            } else {
                                return castToBigInteger(a).divide(castToBigInteger(b));
                            }
                        } else {
                            Float floatA = castToFloat(a);
                            Float floatB = castToFloat(b);
                            return floatA != null && floatB != null ? floatA / floatB : null;
                        }
                    } else {
                        Double doubleA = castToDouble(a);
                        Double doubleB = castToDouble(b);
                        return doubleA != null && doubleB != null ? doubleA / doubleB : null;
                    }
                } else {
                    BigDecimal decimalA = castToDecimal(a);
                    BigDecimal decimalB = castToDecimal(b);
                    if (decimalB.scale() < decimalA.scale()) {
                        decimalB = decimalB.setScale(decimalA.scale());
                    }

                    try {
                        return decimalA.divide(decimalB);
                    } catch (ArithmeticException var5) {
                        return decimalA.divide(decimalB, 4);
                    }
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static boolean gt(Object a, Object b) {
        if (a != null && a != SQLEvalVisitor.EVAL_VALUE_NULL) {
            if (b != null && a != SQLEvalVisitor.EVAL_VALUE_NULL) {
                if (!(a instanceof String) && !(b instanceof String)) {
                    if (!(a instanceof BigDecimal) && !(b instanceof BigDecimal)) {
                        if (!(a instanceof BigInteger) && !(b instanceof BigInteger)) {
                            if (!(a instanceof Long) && !(b instanceof Long)) {
                                if (!(a instanceof Integer) && !(b instanceof Integer)) {
                                    if (!(a instanceof Short) && !(b instanceof Short)) {
                                        if (!(a instanceof Byte) && !(b instanceof Byte)) {
                                            if (!(a instanceof Date) && !(b instanceof Date)) {
                                                throw new IllegalArgumentException(a.getClass() + " and " + b.getClass() + " not supported.");
                                            } else {
                                                Date d1 = castToDate(a);
                                                Date d2 = castToDate(b);
                                                if (d1 == d2) {
                                                    return false;
                                                } else if (d1 == null) {
                                                    return false;
                                                } else if (d2 == null) {
                                                    return true;
                                                } else {
                                                    return d1.compareTo(d2) > 0;
                                                }
                                            }
                                        } else {
                                            return castToByte(a) > castToByte(b);
                                        }
                                    } else {
                                        return castToShort(a) > castToShort(b);
                                    }
                                } else {
                                    return castToInteger(a) > castToInteger(b);
                                }
                            } else {
                                return castToLong(a) > castToLong(b);
                            }
                        } else {
                            return castToBigInteger(a).compareTo(castToBigInteger(b)) > 0;
                        }
                    } else {
                        return castToDecimal(a).compareTo(castToDecimal(b)) > 0;
                    }
                } else {
                    return castToString(a).compareTo(castToString(b)) > 0;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean gteq(Object a, Object b) {
        return eq(a, b) ? true : gt(a, b);
    }

    public static boolean lt(Object a, Object b) {
        if (a == null) {
            return true;
        } else if (b == null) {
            return false;
        } else if (!(a instanceof String) && !(b instanceof String)) {
            if (!(a instanceof BigDecimal) && !(b instanceof BigDecimal)) {
                if (!(a instanceof BigInteger) && !(b instanceof BigInteger)) {
                    if (!(a instanceof Long) && !(b instanceof Long)) {
                        if (!(a instanceof Integer) && !(b instanceof Integer)) {
                            if (!(a instanceof Short) && !(b instanceof Short)) {
                                if (!(a instanceof Byte) && !(b instanceof Byte)) {
                                    if (!(a instanceof Date) && !(b instanceof Date)) {
                                        throw new IllegalArgumentException(a.getClass() + " and " + b.getClass() + " not supported.");
                                    } else {
                                        Date d1 = castToDate(a);
                                        Date d2 = castToDate(b);
                                        if (d1 == d2) {
                                            return false;
                                        } else if (d1 == null) {
                                            return true;
                                        } else if (d2 == null) {
                                            return false;
                                        } else {
                                            return d1.compareTo(d2) < 0;
                                        }
                                    }
                                } else {
                                    return castToByte(a) < castToByte(b);
                                }
                            } else {
                                return castToShort(a) < castToShort(b);
                            }
                        } else {
                            Integer intA = castToInteger(a);
                            Integer intB = castToInteger(b);
                            return intA < intB;
                        }
                    } else {
                        return castToLong(a) < castToLong(b);
                    }
                } else {
                    return castToBigInteger(a).compareTo(castToBigInteger(b)) < 0;
                }
            } else {
                return castToDecimal(a).compareTo(castToDecimal(b)) < 0;
            }
        } else {
            return castToString(a).compareTo(castToString(b)) < 0;
        }
    }

    public static boolean lteq(Object a, Object b) {
        return eq(a, b) ? true : lt(a, b);
    }

    public static boolean eq(Object a, Object b) {
        if (a == b) {
            return true;
        } else if (a != null && b != null) {
            if (a != SQLEvalVisitor.EVAL_VALUE_NULL && b != SQLEvalVisitor.EVAL_VALUE_NULL) {
                if (a.equals(b)) {
                    return true;
                } else if (!(a instanceof String) && !(b instanceof String)) {
                    if (!(a instanceof BigDecimal) && !(b instanceof BigDecimal)) {
                        if (!(a instanceof BigInteger) && !(b instanceof BigInteger)) {
                            if (!(a instanceof Long) && !(b instanceof Long)) {
                                if (!(a instanceof Integer) && !(b instanceof Integer)) {
                                    if (!(a instanceof Short) && !(b instanceof Short)) {
                                        if (!(a instanceof Boolean) && !(b instanceof Boolean)) {
                                            if (!(a instanceof Byte) && !(b instanceof Byte)) {
                                                if (!(a instanceof Date) && !(b instanceof Date)) {
                                                    throw new IllegalArgumentException(a.getClass() + " and " + b.getClass() + " not supported.");
                                                } else {
                                                    Date d1 = castToDate(a);
                                                    Date d2 = castToDate(b);
                                                    if (d1 == d2) {
                                                        return true;
                                                    } else {
                                                        return d1 != null && d2 != null ? d1.equals(d2) : false;
                                                    }
                                                }
                                            } else {
                                                return castToByte(a).equals(castToByte(b));
                                            }
                                        } else {
                                            return castToBoolean(a).equals(castToBoolean(b));
                                        }
                                    } else {
                                        return castToShort(a).equals(castToShort(b));
                                    }
                                } else {
                                    Integer inta = castToInteger(a);
                                    Integer intb = castToInteger(b);
                                    return inta != null && intb != null ? inta.equals(intb) : false;
                                }
                            } else {
                                return castToLong(a).equals(castToLong(b));
                            }
                        } else {
                            return castToBigInteger(a).compareTo(castToBigInteger(b)) == 0;
                        }
                    } else {
                        return castToDecimal(a).compareTo(castToDecimal(b)) == 0;
                    }
                } else {
                    return castToString(a).equals(castToString(b));
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static Object add(Object a, Object b) {
        if (a == null) {
            return b;
        } else if (b == null) {
            return a;
        } else if (a != SQLEvalVisitor.EVAL_VALUE_NULL && b != SQLEvalVisitor.EVAL_VALUE_NULL) {
            if (a instanceof String && !(b instanceof String)) {
                a = castToNumber((String)a);
            }

            if (b instanceof String && !(a instanceof String)) {
                b = castToNumber((String)b);
            }

            if (!(a instanceof BigDecimal) && !(b instanceof BigDecimal)) {
                if (!(a instanceof BigInteger) && !(b instanceof BigInteger)) {
                    if (!(a instanceof Double) && !(b instanceof Double)) {
                        if (!(a instanceof Float) && !(b instanceof Float)) {
                            if (!(a instanceof Long) && !(b instanceof Long)) {
                                if (!(a instanceof Integer) && !(b instanceof Integer)) {
                                    if (!(a instanceof Short) && !(b instanceof Short)) {
                                        if (!(a instanceof Boolean) && !(b instanceof Boolean)) {
                                            if (!(a instanceof Byte) && !(b instanceof Byte)) {
                                                if (a instanceof String && b instanceof String) {
                                                    return castToString(a) + castToString(b);
                                                } else {
                                                    throw new IllegalArgumentException(a.getClass() + " and " + b.getClass() + " not supported.");
                                                }
                                            } else {
                                                return castToByte(a) + castToByte(b);
                                            }
                                        } else {
                                            int aI = 0;
                                            int bI = 0;
                                            if (castToBoolean(a)) {
                                                aI = 1;
                                            }

                                            if (castToBoolean(b)) {
                                                bI = 1;
                                            }

                                            return aI + bI;
                                        }
                                    } else {
                                        return castToShort(a) + castToShort(b);
                                    }
                                } else {
                                    return castToInteger(a) + castToInteger(b);
                                }
                            } else {
                                return castToLong(a) + castToLong(b);
                            }
                        } else {
                            return castToFloat(a) + castToFloat(b);
                        }
                    } else {
                        return castToDouble(a) + castToDouble(b);
                    }
                } else {
                    return castToBigInteger(a).add(castToBigInteger(b));
                }
            } else {
                return castToDecimal(a).add(castToDecimal(b));
            }
        } else {
            return SQLEvalVisitor.EVAL_VALUE_NULL;
        }
    }

    public static Object sub(Object a, Object b) {
        if (a == null) {
            return null;
        } else if (b == null) {
            return a;
        } else if (a != SQLEvalVisitor.EVAL_VALUE_NULL && b != SQLEvalVisitor.EVAL_VALUE_NULL) {
            if (!(a instanceof Date) && !(b instanceof Date)) {
                if (a instanceof String) {
                    a = castToNumber((String)a);
                }

                if (b instanceof String) {
                    b = castToNumber((String)b);
                }

                if (!(a instanceof BigDecimal) && !(b instanceof BigDecimal)) {
                    if (!(a instanceof BigInteger) && !(b instanceof BigInteger)) {
                        if (!(a instanceof Double) && !(b instanceof Double)) {
                            if (!(a instanceof Float) && !(b instanceof Float)) {
                                if (!(a instanceof Long) && !(b instanceof Long)) {
                                    if (!(a instanceof Integer) && !(b instanceof Integer)) {
                                        if (!(a instanceof Short) && !(b instanceof Short)) {
                                            if (!(a instanceof Boolean) && !(b instanceof Boolean)) {
                                                if (!(a instanceof Byte) && !(b instanceof Byte)) {
                                                    throw new IllegalArgumentException(a.getClass() + " and " + b.getClass() + " not supported.");
                                                } else {
                                                    return castToByte(a) - castToByte(b);
                                                }
                                            } else {
                                                int aI = 0;
                                                int bI = 0;
                                                if (castToBoolean(a)) {
                                                    aI = 1;
                                                }

                                                if (castToBoolean(b)) {
                                                    bI = 1;
                                                }

                                                return aI - bI;
                                            }
                                        } else {
                                            return castToShort(a) - castToShort(b);
                                        }
                                    } else {
                                        return castToInteger(a) - castToInteger(b);
                                    }
                                } else {
                                    return castToLong(a) - castToLong(b);
                                }
                            } else {
                                return castToFloat(a) - castToFloat(b);
                            }
                        } else {
                            return castToDouble(a) - castToDouble(b);
                        }
                    } else {
                        return castToBigInteger(a).subtract(castToBigInteger(b));
                    }
                } else {
                    return castToDecimal(a).subtract(castToDecimal(b));
                }
            } else {
                return SQLEvalVisitor.EVAL_ERROR;
            }
        } else {
            return SQLEvalVisitor.EVAL_VALUE_NULL;
        }
    }

    public static Object multi(Object a, Object b) {
        if (a != null && b != null) {
            if (a instanceof String) {
                a = castToNumber((String)a);
            }

            if (b instanceof String) {
                b = castToNumber((String)b);
            }

            if (!(a instanceof BigDecimal) && !(b instanceof BigDecimal)) {
                if (!(a instanceof BigInteger) && !(b instanceof BigInteger)) {
                    if (!(a instanceof Double) && !(b instanceof Double)) {
                        if (!(a instanceof Float) && !(b instanceof Float)) {
                            if (!(a instanceof Long) && !(b instanceof Long)) {
                                if (!(a instanceof Integer) && !(b instanceof Integer)) {
                                    if (!(a instanceof Short) && !(b instanceof Short)) {
                                        if (!(a instanceof Byte) && !(b instanceof Byte)) {
                                            throw new IllegalArgumentException(a.getClass() + " and " + b.getClass() + " not supported.");
                                        } else {
                                            return castToByte(a) * castToByte(b);
                                        }
                                    } else {
                                        Short shortA = castToShort(a);
                                        Short shortB = castToShort(b);
                                        return shortA != null && shortB != null ? shortA * shortB : null;
                                    }
                                } else {
                                    return castToInteger(a) * castToInteger(b);
                                }
                            } else {
                                return castToLong(a) * castToLong(b);
                            }
                        } else {
                            return castToFloat(a) * castToFloat(b);
                        }
                    } else {
                        return castToDouble(a) * castToDouble(b);
                    }
                } else {
                    return castToBigInteger(a).multiply(castToBigInteger(b));
                }
            } else {
                return castToDecimal(a).multiply(castToDecimal(b));
            }
        } else {
            return null;
        }
    }

    public static boolean like(String input, String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("pattern is null");
        } else {
            StringBuilder regexprBuilder = new StringBuilder(pattern.length() + 4);
            int STAT_NOTSET = 0;
            int STAT_RANGE = 1;
            int STAT_LITERAL = 2;
            int stat = 0;
            int blockStart = -1;

            for(int i = 0; i < pattern.length(); ++i) {
                char ch = pattern.charAt(i);
                String block;
                if (stat == 2 && (ch == '%' || ch == '_' || ch == '[')) {
                    block = pattern.substring(blockStart, i);
                    regexprBuilder.append("\\Q");
                    regexprBuilder.append(block);
                    regexprBuilder.append("\\E");
                    blockStart = -1;
                    stat = 0;
                }

                if (ch == '%') {
                    regexprBuilder.append(".*");
                } else if (ch == '_') {
                    regexprBuilder.append('.');
                } else if (ch == '[') {
                    if (stat == 1) {
                        throw new IllegalArgumentException("illegal pattern : " + pattern);
                    }

                    stat = 1;
                    blockStart = i;
                } else if (ch == ']') {
                    if (stat != 1) {
                        throw new IllegalArgumentException("illegal pattern : " + pattern);
                    }

                    block = pattern.substring(blockStart, i + 1);
                    regexprBuilder.append(block);
                    blockStart = -1;
                } else {
                    if (stat == 0) {
                        stat = 2;
                        blockStart = i;
                    }

                    if (stat == 2 && i == pattern.length() - 1) {
                        block = pattern.substring(blockStart, i + 1);
                        regexprBuilder.append("\\Q");
                        regexprBuilder.append(block);
                        regexprBuilder.append("\\E");
                    }
                }
            }

            if (!"%".equals(pattern) && !"%%".equals(pattern)) {
                String regexpr = regexprBuilder.toString();
                return Pattern.matches(regexpr, input);
            } else {
                return true;
            }
        }
    }

    public static boolean visit(SQLEvalVisitor visitor, SQLIdentifierExpr x) {
        x.putAttribute("eval.expr", x);
        return false;
    }

    static {
        registerBaseFunctions();
    }
}
