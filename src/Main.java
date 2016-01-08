/*
 * The main class for the Hearts Data software
 * 
 * @author Calob Symonds
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		Query query = new Query(input);
		
		while (true) {
			System.out.print("\nSelect an option (type 'help' for help): ");
			String option = input.nextLine();
			boolean helpMode = false;
			
			if (option.charAt(option.length() - 1) == '?') {
				helpMode = true;
				option = option.substring(0, option.length() - 1);
			}
			
			if (option.equalsIgnoreCase("list options")) {
				Method[] methods = query.getClass().getMethods();
				for (int i = 0; i < methods.length - 9; i++) {
					Method method = methods[i];
					System.out.println(methodToOption(method.getName()));
				}
			} else {
				Method method = null;
				
				try {
					method = query.getClass().getMethod(optionToMethod(option), boolean.class);
					method.invoke(query, helpMode);
				} catch (NoSuchMethodException e) {
					// The method doesn't exist in 'Query.java' (must be public with proper arguments)
					System.out.println("Option '" + option + "' is not valid.");
				} catch (InvocationTargetException e) {
					// An exception occurred in the execution of the method
					System.out.println("An exception was thrown in the query method!");
					e.printStackTrace();
				} catch (NullPointerException e) {
					// The 'query' object isn't initialized
					System.out.println("This exception shouldn't be reachable: something went wrong :P");
				} catch (IllegalArgumentException e) {
					System.out.println("This exception shouldn't be reachable: something went wrong :P");
				} catch (IllegalAccessException e) {
					System.out.println("This exception shouldn't be reachable: something went wrong :P");
				}
			}
			
			if (query.close()) {
				System.exit(0);
			}
		}
	}
	
	private static String optionToMethod(String option) {
		String[] split = option.split(" ");
		StringBuilder str = new StringBuilder(split[0].toLowerCase());
		for (int i = 1; i < split.length; i++) {
			str.append(split[i].substring(0, 1).toUpperCase() + split[i].substring(1).toLowerCase());
		}
		return str.toString();
	}
	
	private static String methodToOption(String method) {
		int start = 0;
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < method.length(); i++) {
			if (Character.isUpperCase(method.charAt(i))) {
				str.append(method.substring(start, i).toLowerCase() + " ");
				start = i++;
			}
		}
		str.append(method.substring(start).toLowerCase());
		return str.toString();
	}
}