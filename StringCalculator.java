import java.util.*;
import java.util.regex.*;

// Entry Point
public class StringCalculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Calculator calculator = new Calculator();
        String input = sc.nextLine();
        System.out.println("Result: " + calculator.calculate(input));
    }
}

// Calculator Class
class Calculator {
    public int calculate(String input) {
        Expression expression = new Expression(input);
        return expression.evaluate();
    }
}

// Expression Class
class Expression {
    private final List<Token> tokens;

    public Expression(String input) {
        this.tokens = tokenize(input);
    }

    public int evaluate() {
        Queue<Token> queue = new LinkedList<>(tokens);
        int result = Integer.parseInt(queue.poll().getValue());

        while (!queue.isEmpty()) {
            Operator operator = Operator.of(queue.poll().getValue());
            int nextValue = Integer.parseInt(queue.poll().getValue());
            result = operator.apply(result, nextValue);
        }

        return result;
    }

    private List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        // 정규식을 사용하여 숫자와 연산자를 분리
        String regex = "\\d+|[+\\-*/]";
        Matcher matcher = Pattern.compile(regex).matcher(input);
        while (matcher.find()) {
            tokens.add(new Token(matcher.group()));
        }
        return tokens;
    }
}

// Token Class
class Token {
    private final String value;

    public Token(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

// Operator Enum
enum Operator {
    ADD("+", (a, b) -> a + b),
    SUBTRACT("-", (a, b) -> a - b),
    MULTIPLY("*", (a, b) -> a * b),
    DIVIDE("/", (a, b) -> a / b);

    private final String symbol;
    private final Operation operation;

    Operator(String symbol, Operation operation) {
        this.symbol = symbol;
        this.operation = operation;
    }

    public static Operator of(String symbol) {
        return Arrays.stream(values())
                .filter(op -> op.symbol.equals(symbol))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid operator: " + symbol));
    }

    public int apply(int a, int b) {
        return operation.apply(a, b);
    }

    interface Operation {
        int apply(int a, int b);
    }
}

// Test Code
class StringCalculatorTest {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();

        assert calculator.calculate("2 + 3") == 5 : "Test 1 Failed";
        assert calculator.calculate("2+3*4") == 14 : "Test 2 Failed";
        assert calculator.calculate("10 / 2 - 1") == 4 : "Test 3 Failed";
        assert calculator.calculate("1+2+3") == 6 : "Test 4 Failed";

        System.out.println("테스트 통과");
    }
}
