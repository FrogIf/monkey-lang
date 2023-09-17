package sch.frog.monkey.lang.evaluator;

import io.github.frogif.calculator.number.impl.RationalNumber;
import sch.frog.monkey.lang.ast.ArrayExpression;
import sch.frog.monkey.lang.ast.ArrayIndexExpression;
import sch.frog.monkey.lang.ast.BOOL;
import sch.frog.monkey.lang.ast.BlockExpression;
import sch.frog.monkey.lang.ast.CallExpression;
import sch.frog.monkey.lang.ast.FunctionDefineExpression;
import sch.frog.monkey.lang.ast.GroupExpression;
import sch.frog.monkey.lang.ast.IAstNode;
import sch.frog.monkey.lang.ast.IExpressionStatement;
import sch.frog.monkey.lang.ast.Identifier;
import sch.frog.monkey.lang.ast.IfElseStatement;
import sch.frog.monkey.lang.ast.InfixExpression;
import sch.frog.monkey.lang.ast.LetStatement;
import sch.frog.monkey.lang.ast.MapExpression;
import sch.frog.monkey.lang.ast.MapIndexExpression;
import sch.frog.monkey.lang.ast.NothingStatement;
import sch.frog.monkey.lang.ast.Null;
import sch.frog.monkey.lang.ast.Number;
import sch.frog.monkey.lang.ast.PrefixExpression;
import sch.frog.monkey.lang.ast.Program;
import sch.frog.monkey.lang.ast.ReturnStatement;
import sch.frog.monkey.lang.ast.SignalStatement;
import sch.frog.monkey.lang.ast.StringExp;
import sch.frog.monkey.lang.ast.WhileStatement;
import sch.frog.monkey.lang.exception.EvalException;
import sch.frog.monkey.lang.exception.FunctionExistException;
import sch.frog.monkey.lang.val.FunctionDefine;
import sch.frog.monkey.lang.val.FunctionObj;
import sch.frog.monkey.lang.val.MapObject;
import sch.frog.monkey.lang.val.SignalValue;
import sch.frog.monkey.lang.val.Value;
import sch.frog.monkey.lang.val.ValueType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Evaluator {

    private final IAstNode program;

    private final EvalContext globalContext = new EvalContext();

    private static final HashMap<String, IBuiltinFunction> BuiltinFunctionMap = new HashMap<>();

    public static synchronized void addBuiltinFunction(String name, IBuiltinFunction function) throws FunctionExistException {
        if(BuiltinFunctionMap.containsKey(name)){
            throw new FunctionExistException(name);
        }
        BuiltinFunctionMap.put(name, function);
    }

    public Evaluator(IAstNode program) {
        this.program = program;
    }

    public Value eval() throws EvalException {
        return eval(program, globalContext);
    }

    private Value eval(IAstNode node, EvalContext context) throws EvalException {
        if(node instanceof Number){
            return ((Number) node).evaluate();
        }else if(node instanceof BOOL){
            return ((BOOL) node).evaluate();
        }else if(node instanceof StringExp){
            return ((StringExp) node).evaluate();
        }else if(node instanceof Null){
            return ((Null) node).evaluate();
        }else if(node instanceof CallExpression){
            return evaluateFunCallExpression((CallExpression) node, context);
        }else if(node instanceof MapExpression){
            return evaluateMapExpression((MapExpression)node, context);
        } else if (node instanceof MapIndexExpression) {
            return evaluateMapIndex((MapIndexExpression)node, context);
        } else if(node instanceof Program){
            return evaluateProgram((Program)node, context);
        }else if(node instanceof BlockExpression){
            return evaluateBlockExpression((BlockExpression)node, context);
        }else if(node instanceof ArrayExpression){
            return evaluateArrayExpression((ArrayExpression) node, context);
        }else if(node instanceof ArrayIndexExpression){
            return evaluateArrayIndex((ArrayIndexExpression) node, context);
        }else if(node instanceof PrefixExpression){
            return evaluatePrefix((PrefixExpression)node, context);
        }else if(node instanceof InfixExpression){
            return evaluateInfix((InfixExpression) node, context);
        }else if(node instanceof GroupExpression){
            return eval(((GroupExpression) node).getSubExp(), context);
        }else if(node instanceof IfElseStatement){
            return evaluateIfElse((IfElseStatement) node, context);
        }else if(node instanceof WhileStatement){
            return evalWhileStatement((WhileStatement)node, context);
        }else if(node instanceof ReturnStatement){
            return evaluateReturn((ReturnStatement) node, context);
        }else if(node instanceof LetStatement){
            return evaluateLet((LetStatement) node, context);
        }else if(node instanceof Identifier){
            return evaluateIdentifier((Identifier)node, context);
        }else if(node instanceof SignalStatement){
            return evaluateSignalStatement((SignalStatement)node);
        }else if(node instanceof FunctionDefineExpression){
            return evaluateFunctionDefine((FunctionDefineExpression) node, context);
        }else if(node instanceof NothingStatement){
            return Value.VOID;
        }else{
            return error("nonsupport node type : " + node.getClass());
        }
    }

    private Value evaluateMapIndex(MapIndexExpression node, EvalContext context) throws EvalException {
        IAstNode left = node.getLeft();
        Value mapVal = eval(left, context);
        if(mapVal.getType() != ValueType.MAP){
            return error("map index except callee map, but " + mapVal.getType().name());
        }
        IAstNode right = node.getRight();
        if(right instanceof Identifier){
            return mapVal.cast(MapObject.class).getOrDefault(((Identifier) right).literal(), Value.NULL);
        }else{
            return error("map index must identifier, but " + right.getClass());
        }
    }

    private Value evaluateMapExpression(MapExpression node, EvalContext context) throws EvalException {
        Map<String, IExpressionStatement> map = node.getMap();
        MapObject mapVal = new MapObject();
        for (Map.Entry<String, IExpressionStatement> entry : map.entrySet()) {
            mapVal.put(entry.getKey(), eval(entry.getValue(), context));
        }
        return new Value(mapVal);
    }

    private Value evaluateArrayIndex(ArrayIndexExpression arrayIndexExpression, EvalContext context) throws EvalException {
        IAstNode left = arrayIndexExpression.getLeft();
        Value arr = eval(left, context);
        if(arr.getType() != ValueType.ARRAY){
            return error("array index left is not array");
        }
        IAstNode right = arrayIndexExpression.getRight();
        Value index = eval(right, context);
        if(index.getType() != ValueType.NUMBER){
            return error("array index must number, but " + index.getType().name());
        }
        Integer i = index.cast(int.class);
        return arr.cast(Value[].class)[i];
    }

    private Value evaluateArrayExpression(ArrayExpression arrayExpression, EvalContext context) throws EvalException {
        List<IExpressionStatement> elements = arrayExpression.getElements();
        Value[] eleArr = new Value[elements.size()];
        int i = 0;
        for (IExpressionStatement element : elements) {
            Value val = eval(element, context);
            if(val.getType() == ValueType.SIGNAL){
                return val;
            }else{
                eleArr[i++] = val;
            }
        }
        return new Value(eleArr);
    }

    public Value evaluateFunCallExpression(CallExpression callExpression, EvalContext context) throws EvalException {
        IAstNode left = callExpression.getLeft();
        Value funVal;
        if(left instanceof Identifier){
            funVal = evaluateIdentifier((Identifier) left, context);
        }else{
            funVal = eval(left, context);
        }
        if(funVal == null || funVal.getType() != ValueType.FUNCTION){
            return error("callee is not function, but " + (funVal == null ? null : funVal.getType().name()));
        }
        FunctionObj funObj = funVal.cast(FunctionObj.class);
        if(funObj.type() == FunctionObj.Type.BUILTIN){
            IBuiltinFunction builtinFunction = funObj.getBuiltinFunction();
            List<IExpressionStatement> realArgExps = callExpression.getRealArgExps();
            Value[] realArgs = new Value[realArgExps.size()];
            int i = 0;
            for (IExpressionStatement realArgExp : realArgExps) {
                realArgs[i++] = eval(realArgExp, context);
            }
            return builtinFunction.execute(realArgs, context);
        }else{
            FunctionDefine funBody = funObj.getFunctionDefine();
            List<IExpressionStatement> realArgExps = callExpression.getRealArgExps();
            List<String> formatArgs = funBody.getFormatArgs();
            if(formatArgs.size() != realArgExps.size()){
                return error("function argument count not match");
            }
            Value[] realArgs = new Value[realArgExps.size()];
            int j = 0;
            for (IExpressionStatement realArgExp : realArgExps) {
                realArgs[j++] = eval(realArgExp, context);
            }
            EvalContext localContext = new EvalContext(context);
            for (int i = 0, len = formatArgs.size(); i < len; i++){
                localContext.addVariable(formatArgs.get(i), realArgs[i]);
            }
            Value val = eval(funBody.getBody(), localContext);
            if(val.getType() == ValueType.SIGNAL){
                SignalValue signal = val.cast(SignalValue.class);
                if(signal.type() == SignalValue.Type.RETURN){
                    return signal.val();
                }
            }
            return val;
        }
    }

    private Value evaluateFunctionDefine(FunctionDefineExpression expression, EvalContext context){
        List<Identifier> arguments = expression.getArguments();
        IExpressionStatement body = expression.getBody();
        ArrayList<String> argList = new ArrayList<>(arguments.size());
        for (Identifier argument : arguments) {
            argList.add(argument.literal());
        }
        return new Value(new FunctionObj(new FunctionDefine(argList, body, context)));
    }

    private Value evaluateSignalStatement(SignalStatement node) {
        String literal = node.literal();
        if("break".equals(literal)){
            return new Value(new SignalValue(SignalValue.Type.BREAK, null));
        }else if("continue".equals(literal)){
            return new Value(new SignalValue(SignalValue.Type.CONTINUE, null));
        }
        return error("unrecognized signal : " + literal);
    }

    private Value evalWhileStatement(WhileStatement node, EvalContext context) throws EvalException {
        IExpressionStatement condition = node.getCondition();
        IExpressionStatement loopBody = node.getLoopBody();
        while(eval(condition, context).cast(boolean.class)){
            Value v = eval(loopBody, context);
            if(v.getType() == ValueType.SIGNAL){
                SignalValue signal = v.cast(SignalValue.class);
                if(signal.type() == SignalValue.Type.BREAK){
                    break;
                }else if(signal.type() != SignalValue.Type.CONTINUE){
                    return v;
                }
            }
        }
        return Value.VOID;
    }

    private Value evaluateIdentifier(Identifier node, EvalContext context) {
        String varName = node.literal();
        Value val = context.getVariable(varName);
        if(val != null){
            return val;
        }

        IBuiltinFunction builtinFunction = BuiltinFunctionMap.get(varName);
        if(builtinFunction != null){
            return new Value(new FunctionObj(builtinFunction));
        }

        return error("no var name : " + varName);
    }

    private Value evaluateLet(LetStatement node, EvalContext context) throws EvalException {
        IExpressionStatement valueExp = node.getValueExp();
        Value val = valueExp == null ? Value.UNDEFINE : eval(node.getValueExp(), context);
        String varName = node.getVarName();
        globalContext.addVariable(varName, val);
        return val;
    }

    private Value evaluateBlockExpression(BlockExpression node, EvalContext context) throws EvalException {
        List<IAstNode> children = node.getChildren();
        Value result = Value.VOID;
        for (IAstNode child : children) {
            Value v = eval(child, context);
            if(v.getType() == ValueType.SIGNAL){
                return v;
            }
            result = v;
        }
        return result;
    }

    private Value evaluateReturn(ReturnStatement returnStatement, EvalContext context) throws EvalException {
        IExpressionStatement statement = returnStatement.returnExp();
        Value val = this.eval(statement, context);
        return new Value(new SignalValue(SignalValue.Type.RETURN, val));
    }

    private Value evaluateIfElse(IfElseStatement ifElseStatement, EvalContext context) throws EvalException {
        List<IfElseStatement.ConditionExpPair> pairs = ifElseStatement.getConditionExpPair();
        if(pairs.isEmpty()){
            return error("no if block");
        }
        for (IfElseStatement.ConditionExpPair pair : pairs) {
            IExpressionStatement condition = pair.getCondition();
            if(eval(condition, context).cast(boolean.class)){
                return eval(pair.getExpression(), context);
            }
        }
        IExpressionStatement elseExp = ifElseStatement.getElseExp();
        if(elseExp != null){
            return eval(elseExp, context);
        }
        return Value.VOID;
    }

    private Value evaluateInfix(InfixExpression infixExpression, EvalContext context) throws EvalException {
        String operator = infixExpression.operator();
        if ("=".equals(operator)) { // 赋值
            IAstNode left = infixExpression.getLeft();
            if(left instanceof Identifier){
                String name = ((Identifier) left).literal();
                Value val = eval(infixExpression.getRight(), context);
                context.setVariable(name, val);
                return val;
            }else{
                return error("assign left must identifier, but " + left.getClass());
            }
        }
        Value left = eval(infixExpression.getLeft(), context);
        if(left.getType() == ValueType.SIGNAL){
            return left;
        }
        Value right = eval(infixExpression.getRight(), context);
        if(right.getType() == ValueType.SIGNAL){
            return right;
        }
        switch (operator){
            case "+":
                return evaluateAdd(left, right, context);
            case "-":
                return new Value(left.cast(RationalNumber.class).sub(right.cast(RationalNumber.class)));
            case "*":
                return new Value(left.cast(RationalNumber.class).mult(right.cast(RationalNumber.class)));
            case "/":
                return new Value(left.cast(RationalNumber.class).div(right.cast(RationalNumber.class)));
            case ">":
                return new Value(left.cast(RationalNumber.class).compareTo(right.cast(RationalNumber.class)) > 0);
            case "<":
                return new Value(left.cast(RationalNumber.class).compareTo(right.cast(RationalNumber.class)) < 0);
            case ">=":
                return new Value(left.cast(RationalNumber.class).compareTo(right.cast(RationalNumber.class)) >= 0);
            case "<=":
                return new Value(left.cast(RationalNumber.class).compareTo(right.cast(RationalNumber.class)) <= 0);
            case "==":
                return evaluateEqual(left, right, context);
            case "!=":
                return evaluateEqual(left, right, context).cast(boolean.class) ? Value.FALSE : Value.TRUE;
            default:
                return error("nonsupport operator : " + operator);
        }
    }

    private Value evaluateAdd(Value left, Value right, EvalContext context){
        ValueType leftType = left.getType();
        ValueType rightType = right.getType();
        if(leftType == ValueType.SIGNAL){ return left; }
        else if(rightType == ValueType.SIGNAL){ return right; }

        if(leftType == ValueType.NUMBER && rightType == ValueType.NUMBER){
            return new Value(left.cast(RationalNumber.class).add(right.cast(RationalNumber.class)));
        }else if(leftType == ValueType.STRING){
            String leftStr = left.cast(String.class);
            String rightStr = rightType == ValueType.STRING ? right.cast(String.class) : right.inspect();
            return new Value(leftStr + rightStr);
        }else if(rightType == ValueType.STRING){
            return new Value(left.inspect() + right.cast(String.class));
        }else{
            return error("nonsupport add for " + leftType.name() + " and " + rightType.name());
        }
    }

    private Value evaluateEqual(Value left, Value right, EvalContext context) throws EvalException {
        if(left.getType() == ValueType.STRING && right.getType() == ValueType.STRING){
            return new Value(left.cast(String.class).equals(right.cast(String.class)));
        }else if(left.getType() == ValueType.NUMBER && right.getType() == ValueType.NUMBER){
            return new Value(left.cast(RationalNumber.class).compareTo(right.cast(RationalNumber.class)) == 0);
        }else if(left.getType() == ValueType.BOOL && right.getType() == ValueType.BOOL){
            return left.cast(boolean.class) == right.cast(boolean.class) ? Value.TRUE : Value.FALSE;
        }else{
            return error("== can't apply to type " + left.getType().name() + " and " + right.getType().name());
        }
    }

    private Value evaluatePrefix(PrefixExpression prefixExpression, EvalContext context) throws EvalException {
        String operator = prefixExpression.operator();
        Value right = eval(prefixExpression.getRight(), context);
        if("!".equals(operator)){
            if(right.getType() != ValueType.BOOL){
                throw new EvalException("value is not bool");
            }
            return right.cast(boolean.class) ? Value.FALSE : Value.TRUE;
        }else if("-".equals(operator)){
            if(right.getType() != ValueType.NUMBER){
                throw new EvalException("value is not number");
            }
            return new Value(right.cast(RationalNumber.class).not());
        }
        throw new EvalException("nonsupport prefix operator : " + operator);
    }


    private Value evaluateProgram(Program program, EvalContext context) throws EvalException {
        List<IAstNode> statements = program.getChildren();
        Value result = Value.VOID;
        for (IAstNode statement : statements) {
            Value v = eval(statement, context);
            if(v.getType() == ValueType.SIGNAL){
                SignalValue signal = v.cast(SignalValue.class);
                if(signal.type() == SignalValue.Type.RETURN){
                    return signal.val();
                }else{
                    return v;
                }
            }
            result = v;
        }
        return result;
    }

    public static Value error(String message){
        return new Value(new SignalValue(SignalValue.Type.ERROR, new Value(message)));
    }

}
