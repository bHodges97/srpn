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
		long operandA, operandB, result;
		for (int i = 0; i < sentence.length(); ++i) {
			char c = sentence.charAt(i);
			//build a numeric string
			if (Character.isDigit(c)) {
				currentNum += c;
				continue;
			}
			//add numeric string to stack and reset the string
			addNum(currentNum);
			currentNum = "";
			//handle special chars
			if (c == 'r') {//print a pseudo random number
				addNum("" + random.rand());
			} else if (c == 'd') {//print every number currently in the stack
				for (Integer number : stack) {
					System.out.println(number);
				}
			} else if (c == '=') {//Print the number at the top of the stack
				if (stack.empty()) {
					System.out.println("Stack empty.");//nothing is in the stack
				} else {
					System.out.println(stack.peek());//print element at top of stack
				}
			} else if (c == '-' && i < sentence.length() - 1 && Character.isDigit(sentence.charAt(i + 1))) {
				currentNum = "-";//this is used to make negative numbers
				continue;
			}
			//skip the rest of the loop if not an arithmetic operation
			if (c == 'r' || c == 'd' || c == '=' || c == ' ') {
				continue;
			}else if(!(c+"").matches("[%\\^\\+\\-\\*/=]")){//check for unrecognised letters
				System.out.println("Unrecognised operator or operand \"" + c + "\"");//print the error
				continue;
			}
			//pop 2 numbers from the stack
			if (stack.size() < 2) {//underflow if not enough operands
				System.out.println("Stack underflow.");
				continue;
			} else {
				operandB = stack.pop();//remove operandB first
				operandA = stack.pop();
				result = 0;//initialise result to make java happy
			}
			//handle the operators.
			switch (c) {
			case '%':				
				result = operandA % operandB;//operandA mod orperandB
				break;
			case '^':
				if (operandB < 0) {//check for negative power
					System.out.println("Negative power");
					stack.push((int)operandA);//push back onto stack
					stack.push((int)operandB);
					continue;
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
					stack.push((int)operandA);//push back onto stack
					stack.push((int)operandB);
					continue;
				} else {//normal divide
					result = operandA / operandB;
				}
			}
			result = result > Integer.MAX_VALUE ? Integer.MAX_VALUE : result;//Handle the saturation
			result = result < Integer.MIN_VALUE ? Integer.MIN_VALUE : result;//Also cover negatives
			stack.push((int) result);//Add the result to stack
		}
		addNum(currentNum);//Add the last number to stack.
	}

	/**
	 * Push a numeric string on to the stack
	 * @param in The input string.
	 */
	private void addNum(String in) {
		if (in.isEmpty())
			return;
		if (stack.size() == 23) {//a stack size limit that srpn.out has
			System.out.println("Stack overflow.");
			return;
		}
		long value;
		if (in.length() > 11) {//in case the number is greater than the range of long
			value = in.charAt(0) == '-' ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		} else {
			value = Long.valueOf(in);
			value = value > Integer.MAX_VALUE ? Integer.MAX_VALUE : value;//reduce to int range
			value = value < Integer.MIN_VALUE ? Integer.MIN_VALUE : value;
		}
		stack.push((int)value);
	}
}
