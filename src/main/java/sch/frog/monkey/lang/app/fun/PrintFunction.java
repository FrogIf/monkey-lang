package sch.frog.monkey.lang.app.fun;

import sch.frog.monkey.lang.evaluator.EvalContext;
import sch.frog.monkey.lang.evaluator.Evaluator;
import sch.frog.monkey.lang.evaluator.IBuiltinFunction;
import sch.frog.monkey.lang.val.Value;
import sch.frog.monkey.lang.val.ValueType;

public class PrintFunction implements IBuiltinFunction {

    @Override
    public Value execute(Value[] arguments, EvalContext context) {
        return writeToOutput(arguments, context);
    }

    private Value writeToOutput(Value[] args, EvalContext context){
        if(args.length == 0){
            return Evaluator.error("print at least 1 argument");
        }
        Value log = args[0];
        if(log.getType() != ValueType.STRING){
            return Evaluator.error("print first argument must string, but " + log.getType());
        }
        if(args.length == 1){
            context.getOutput().write(log.inspect() + "\n");
        }else{
            String logExp = log.cast(String.class);
            StringBuilder sb = new StringBuilder();
            boolean escape = false;
            int index = 1;
            for(int i = 0, len = logExp.length(); i < len; i++){
                char ch = logExp.charAt(i);
                if(ch == '\\'){
                    escape = true;
                }else{
                    if(escape){
                        if(ch == '{' || ch == '}'){
                            sb.append(ch);
                        }else{
                            sb.append('\\');
                        }
                    }else{
                        if(ch == '{' && i + 1 < len && logExp.charAt(i + 1) == '}'){
                            if(args.length > index){
                                sb.append(args[index++].inspect());
                            }else{
                                sb.append("{}");
                            }
                            i++;
                        }else{
                            sb.append(ch);
                        }
                    }
                    escape = false;
                }
            }
            if(escape){ sb.append('\\'); }
            context.getOutput().write(sb + "\n");
        }
        return Value.VOID;
    }
}
