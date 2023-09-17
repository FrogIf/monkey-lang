package sch.frog.monkey.lang.evaluator;

import sch.frog.monkey.lang.exception.EvalException;
import sch.frog.monkey.lang.val.Value;

import java.util.HashMap;

public class EvalContext {

    private final HashMap<String, Value> variableMap = new HashMap<>();

    private EvalContext parent;

    public EvalContext(EvalContext parent) {
        this.parent = parent;
    }

    public EvalContext(){}

    public void setVariable(String name, Value val) throws EvalException {
        if(!variableMap.containsKey(name)){
            if(parent == null){
                throw new EvalException("variable " + name + " not exist");
            }else{
                parent.setVariable(name, val);
            }
        }
        variableMap.put(name, val);
    }

    public void addVariable(String name, Value val) throws EvalException {
        if(variableMap.containsKey(name)){
            throw new EvalException("variable " + name + " has exist");
        }
        variableMap.put(name, val);
    }

    public Value getVariable(String name){
        Value v = variableMap.get(name);
        if(v == null && parent != null){
            return parent.getVariable(name);
        }
        return v;
    }

    public IOutput getOutput(){
        return GeneralOutput.out;
    }

}
