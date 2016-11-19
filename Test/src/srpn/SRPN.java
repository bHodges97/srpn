package srpn;

import java.util.Scanner;
import java.util.Stack;

/**
 * A reverse polish calculator that mimics srpn.out.Can take inputs as one line or multiple lines.
 * Supported operations are +,-,*,/,^,% also d prints current stack contents and r for a pseudo random number.
 * Using = returns the value stored at the top of the stack.
 */
public class SRPN {

	Rand random = new Rand();
	Stack<Integer> stack = new Stack<Integer>();
	/**
	 * Main method
	 * @param args No args used.
	 */
	public static void main(String[] args) {
		SRPN srpn  = new SRPN();
		srpn.run();
	}
	
	/**
	 * Constructor
	 */
	public SRPN() {
	}
	
	/**
	 * loops infinitely and waits for next line
	 */
	public void run() {
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			String nextLine = scanner.nextLine();//read a newline
			nextLine = nextLine.split("#")[0];// remove comments
			addToStack(nextLine);//put everything into the stack			
		}
	}
	
	/**
	 * Take the input sentence and push it onto the stack.
	 * @param sentence The sentence to add to the stack.
	 */
	private void addToStack(String sentence) {
		String currentNum = "";		
		for (int i = 0; i < sentence.length(); ++i) {
			char c = sentence.charAt(i);
			//build a numeric string
			if(c == '-' && currentNum == "" && i < sentence.length() - 1 && Character.isDigit(sentence.charAt(i+1))){
				currentNum = "-";
			}else if (Character.isDigit(c)) {
				currentNum += c;
			}else{
				addNum(currentNum);
				currentNum = "";
				proccessOperator(c);
			}
		}
		addNum(currentNum);
	}
	private void proccessOperator(char c){
		long result,operandA,operandB;
		switch(c){
		case 'r':
			addNum("" + random.rand());
			return;
		case 'd':
			for (Integer number : stack) {
					System.out.println(number);
			}
			return;
		case '=':
			if (stack.empty()) {
				System.out.println("Stack empty.");//nothing is in the stack
			} else {
				System.out.println(stack.peek());//print element at top of stack
			}
			return;
		}
		if (stack.size() < 2) {//underflow if not enough operands
			System.out.println("Stack underflow.");
			return;
		} else {
			operandB = stack.pop();//remove operandB first
			operandA = stack.pop();
			result = 0;//initialise result to make java happy
		}
		switch (c){
		case '%':		
			result = operandA % operandB;//operandA mod orperandB
			break;
		case '^':
			if (operandB < 0) {//check for negative power
				System.out.println("Negative power");
				stackPush(operandA,operandB);
				return;
			}
			result = (long) Math.pow(operandA, operandB);// operandA to the power of operandB
			break;
		case '+':
			result = operandA + operandB;
			break;
		case '-':
			result = operandA - operandB;
			break;
			case '*':
			result = operandA * operandB;
			break;
		case '/':
			if (operandB == 0) {//check for divide by 0
				System.out.println("Divide by 0.");
				stackPush(operandA,operandB);
				return;
			} else {//normal divide
				result = operandA / operandB;
			}
		default:
			System.out.println("Unrecognised operator or operand \"" + c + "\"");
			stackPush(operandA,operandB);
			return;
		}
		addNum(""+result);		
	}
	
	private void stackPush(long a,long b){
		stack.push((int)a);//push back onto stack
		stack.push((int)b);
	}
	/**
	 * Push a numeric string on to the stack
	 * @param in The input string.
	 */
	private void addNum(String in) {
		long value;
		if (in.isEmpty()){
			return;		
		}else if (stack.size() == 23) {//a stack size limit that srpn.out has
			System.out.println("Stack overflow.");
			return;
		}
		if(in.charAt(in.indexOf('-')+1) == '0'){
			in = in.split("8|9")[0];
		}		
		if (in.length() > 11) {//in case the number is greater than the range of long
			value = in.charAt(0) == '-' ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		} else {
			value = in.charAt(in.indexOf('-')+1) == '0'?Long.parseLong(in,8):Long.valueOf(in);
			value = value > Integer.MAX_VALUE ? Integer.MAX_VALUE : value;//reduce to int range
			value = value < Integer.MIN_VALUE ? Integer.MIN_VALUE : value;
		}
		stack.push((int)value);
	}
}
