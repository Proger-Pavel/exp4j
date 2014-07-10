/* 
 * Copyright 2014 Frank Asseg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package net.objecthunter.exp4j;

import java.util.*;

import net.objecthunter.exp4j.function.Function;
import net.objecthunter.exp4j.operator.Operator;
import net.objecthunter.exp4j.shuntingyard.ShuntingYard;

import com.sun.istack.internal.NotNull;

public class ExpressionBuilder {

    private final String expression;

    private final List<Function> userFunctions = new ArrayList<>(4);

    private final Map<String, Operator> userOperators;

    public ExpressionBuilder(@NotNull String expression) {
        this.expression = expression;
        this.userOperators = new HashMap<>(4);
    }

    public ExpressionBuilder function(Function function) {
        this.userFunctions.add(function);
        return this;
    }

    public ExpressionBuilder functions(Function... functions) {
        Collections.addAll(this.userFunctions, functions);
        return this;
    }

    public ExpressionBuilder functions(List<Function> functions) {
        this.userFunctions.addAll(functions);
        return this;
    }

    public ExpressionBuilder operator(Operator operator) {
        this.userOperators.put(operator.getSymbol(), operator);
        return this;
    }

    public ExpressionBuilder operator(Operator... operators) {
        for (Operator o : operators) {
            this.userOperators.put(o.getSymbol(), o);
        }
        return this;
    }

    public ExpressionBuilder operator(List<Operator> operators) {
        for (Operator o : operators) {
            this.userOperators.put(o.getSymbol(), o);
        }
        return this;
    }

    public Expression build() throws Exp4jException {
        if (expression.isEmpty()) {
            throw new Exp4jException("The expression can not be empty");
        }
        return new Expression(ShuntingYard.convertToRPN(this.expression, (Function[]) this.userFunctions
                .toArray(new Function[this.userFunctions.size()])));
    }

}
