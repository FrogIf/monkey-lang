package sch.frog.monkey.lang.val;

import io.github.frogif.calculator.number.impl.IntegerNumber;
import io.github.frogif.calculator.number.impl.NumberRoundingMode;
import io.github.frogif.calculator.number.impl.RationalNumber;
import sch.frog.monkey.lang.evaluator.IBuiltinFunction;
import sch.frog.monkey.lang.exception.ValueCastException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Value {

    public static final Value NULL = new Value(ValueType.NULL);

    public static final Value VOID = new Value(ValueType.VOID);

    public static final Value UNDEFINE = new Value(ValueType.UNDEFINE);

    public static final Value TRUE = new Value(true);

    public static final Value FALSE = new Value(false);

    private static final Map<Class<?>, TypeConvertor> javaTypeConvertorMap = new HashMap<>();

    static {
        javaTypeConvertorMap.put(int.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, int.class); }
            RationalNumber num = (RationalNumber) val.val;
            IntegerNumber intNum = num.toInteger();
            if(intNum == null){ throw new ValueCastException(num + " can't cast to int"); }
            return Integer.parseInt(intNum.toString());
        });
        javaTypeConvertorMap.put(Integer.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, Integer.class); }
            RationalNumber num = (RationalNumber) val.val;
            IntegerNumber intNum = num.toInteger();
            if(intNum == null){ throw new ValueCastException(num + " can't cast to integer"); }
            return Integer.parseInt(intNum.toString());
        });
        javaTypeConvertorMap.put(long.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, long.class); }
            RationalNumber num = (RationalNumber) val.val;
            IntegerNumber intNum = num.toInteger();
            if(intNum == null){ throw new ValueCastException(num + " can't cast to long"); }
            return Long.parseLong(intNum.toString());
        });
        javaTypeConvertorMap.put(Long.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, Long.class); }
            RationalNumber num = (RationalNumber) val.val;
            IntegerNumber intNum = num.toInteger();
            if(intNum == null){ throw new ValueCastException(num + " can't cast to Long"); }
            return Long.parseLong(intNum.toString());
        });
        javaTypeConvertorMap.put(double.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, double.class); }
            RationalNumber num = (RationalNumber) val.val;
            String plainString = num.toPlainString(10, NumberRoundingMode.HALF_UP);
            return Double.parseDouble(plainString);
        });
        javaTypeConvertorMap.put(Double.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, Double.class); }
            RationalNumber num = (RationalNumber) val.val;
            String plainString = num.toPlainString(10, NumberRoundingMode.HALF_UP);
            return Double.parseDouble(plainString);
        });
        javaTypeConvertorMap.put(float.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, float.class); }
            RationalNumber num = (RationalNumber) val.val;
            String plainString = num.toPlainString(10, NumberRoundingMode.HALF_UP);
            return Float.parseFloat(plainString);
        });
        javaTypeConvertorMap.put(Float.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, Float.class); }
            RationalNumber num = (RationalNumber) val.val;
            String plainString = num.toPlainString(10, NumberRoundingMode.HALF_UP);
            return Float.parseFloat(plainString);
        });
        javaTypeConvertorMap.put(IntegerNumber.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, RationalNumber.class); }
            IntegerNumber intNum = ((RationalNumber) val.val).toInteger();
            if(intNum == null){ throw new ValueCastException(val.val + " can't cast to integerNumber"); }
            return intNum;
        });
        javaTypeConvertorMap.put(RationalNumber.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, RationalNumber.class); }
            return val.val;
        });
        javaTypeConvertorMap.put(BigDecimal.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, BigDecimal.class); }
            RationalNumber rationalNumber = (RationalNumber) val.val;
            BigDecimal num = new BigDecimal(rationalNumber.getNumerator().toPlainString());
            BigDecimal den = new BigDecimal(rationalNumber.getDenominator().toPlainString());
            return num.divide(den, 10, RoundingMode.HALF_UP);
        });
        javaTypeConvertorMap.put(BigInteger.class, val -> {
            if(val.type != ValueType.NUMBER){ throw new ValueCastException(val.type, BigInteger.class); }
            IntegerNumber intNum = ((RationalNumber) val.val).toInteger();
            if(intNum == null){ throw new ValueCastException(val.val + " can't cast to integerNumber"); }
            return new BigInteger(intNum.toPlainString());
        });
        javaTypeConvertorMap.put(boolean.class, val -> {
            if(val.type != ValueType.BOOL){ throw new ValueCastException(val.type, boolean.class); }
            return val.val;
        });
        javaTypeConvertorMap.put(Boolean.class, val -> {
            if(val.type != ValueType.BOOL){ throw new ValueCastException(val.type, Boolean.class); }
            return val.val;
        });
        javaTypeConvertorMap.put(String.class, val -> {
            if(val.type != ValueType.STRING){ throw new ValueCastException(val.type, String.class); }
            return val.val;
        });
        javaTypeConvertorMap.put(SignalValue.class, val -> {
            if(val.type != ValueType.SIGNAL){ throw new ValueCastException(val.type, SignalValue.class); }
            return val.val;
        });
        javaTypeConvertorMap.put(FunctionObj.class, val -> {
            if(val.type != ValueType.FUNCTION){ throw new ValueCastException(val.type, FunctionObj.class); }
            return val.val;
        });
        javaTypeConvertorMap.put(Value[].class, val -> {
            if(val.type != ValueType.ARRAY){ throw new ValueCastException(val.type, Value[].class); }
            return val.val;
        });
        javaTypeConvertorMap.put(MapObject.class, val -> {
            if(val.type != ValueType.MAP){ throw new ValueCastException(val.type, MapObject.class); }
            return val.val;
        });
    }

    private final ValueType type;

    private final Object val;

    private Value(ValueType type){
        this.val = null;
        this.type = type;
    }

    private Value(ValueType type, Object val) {
        this.type = type;
        this.val = val;
        if(val == null){
            throw new IllegalArgumentException("val can't null");
        }
    }

    public Value(RationalNumber num){
        this(ValueType.NUMBER, num);
    }

    public Value(String str){
        this(ValueType.STRING, str);
    }

    public Value(boolean bl){
        this(ValueType.BOOL, bl);
    }

    public Value(SignalValue signal){
        this(ValueType.SIGNAL, signal);
    }

    public Value(FunctionObj funObj){
        this(ValueType.FUNCTION, funObj);
    }

    public Value(IBuiltinFunction builtinFunction){
        this(ValueType.FUNCTION, builtinFunction);
    }

    public Value(Value[] arr){
        this(ValueType.ARRAY, arr);
    }

    public Value(MapObject map) {
        this(ValueType.MAP, map);
    }

    public ValueType getType() {
        return type;
    }

    public Object getVal() {
        return val;
    }

    public String inspect(){
        if(type == ValueType.NUMBER){
            return numToStr();
        }else if(type == ValueType.SIGNAL){
            SignalValue signal = cast(SignalValue.class);
            return "signal: " + signal.type().name() + ", val : " + (signal.val() == null ? "null" : signal.val().inspect());
        }
        return val == null ? "null" : val.toString();
    }

    private String numToStr(){
        RationalNumber num = (RationalNumber) val;
        IntegerNumber intNum = num.toInteger();
        if(intNum != null){
            return intNum.toPlainString();
        }
        String decimal = num.toPlainString(10, NumberRoundingMode.HALF_UP);
        for(int i = decimal.length() - 1; i > 0; i--){
            char ch = decimal.charAt(i);
            if(ch != '0'){
                return decimal.substring(0, i + 1);
            }
        }
        throw new IllegalStateException(decimal + " format incorrect");
    }

    public <T> T cast(Class<T> clazz){
        if(this.type == ValueType.VOID){
            throw new ValueCastException(this.type, clazz);
        }else if(this.type == ValueType.NULL){
            return null;
        }
        TypeConvertor convertor = javaTypeConvertorMap.get(clazz);
        if(convertor == null){
            throw new ValueCastException(this.type, clazz);
        }
        return (T) convertor.convert(this);
    }

    private interface TypeConvertor{
        Object convert(Value val);
    }
}
