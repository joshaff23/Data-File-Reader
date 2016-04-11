package shaffer.j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InvoiceLam {

	private static Scanner inputFile;
	static File file = new File("invoices.dat");
	public static List<Invoice> invoices = new ArrayList<Invoice>();

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("Welcome to invoice");

		openFile();
		displayMenu();
		getInput();

	}

	public static void openFile() {
		try {
			inputFile = new Scanner(file);
		} catch (IOException e) {
			System.out.println("Error: could not open file.");
			System.exit(0);
		}

		while (inputFile.hasNext()) {
			String stringRead = inputFile.nextLine();

			Scanner parse = new Scanner(stringRead).useDelimiter("\\t");
			try {
				Integer id = parse.nextInt();
				String dept = parse.next();
				String desc = parse.next();
				Integer quant = parse.nextInt();
				BigDecimal price = parse.nextBigDecimal();

				Invoice invoiceTemp = new Invoice(id, dept, desc, quant, price);
				invoices.add(invoiceTemp);
			} catch (InputMismatchException e) {
				System.out.println("Format error.");
			}
		}
	}

	public static void getInput() throws FileNotFoundException {
		String request = "";
		Scanner input = new Scanner(System.in);

		System.out.print("Selection: ");

		do {
			request = input.nextLine();

			if (request.trim() == null || request.trim().equals("")) {
				System.out.println("Oops try again.");
			} else {
				switch (request) {
				case "1":
					showRawData();
					break;
				case "2":
					showInvoice();
					break;
				case "3":
					invoiceByDept();
					break;
				case "4":
					invoiceByQty();
					break;
				case "5":
					deptSummary();
					break;
				case "6":
					exit();
					break;
				default:
					System.out.println("Oops try again.");
					break;
				}
			}
		} while (true);
	}

	public static void displayMenu() throws FileNotFoundException {
		System.out.printf("%nMain Menu %n");
		System.out.printf("\t1. Show raw data%n");
		System.out.printf("\t2. Show invoice%n");
		System.out.printf("\t3. Show invoice by DEPT%n");
		System.out.printf("\t4. Show invoice by QTY%n");
		System.out.printf("\t5. Show department summary%n");
		System.out.printf("\t6. EXIT%n%n");

		getInput();
	}

	public static void showRawData() throws FileNotFoundException {
		System.out.printf("%nRaw Data%n%n");

		try (Stream<String> invoiceFile = Files.lines(Paths.get("invoices.dat"), Charset.defaultCharset())) {
			invoiceFile.forEach(System.out::println);
		} catch (IOException ex) {
			System.out.println("Error with file.");
		}

		displayMenu();
	}

	public static void showInvoice() throws FileNotFoundException {
		System.out.printf("%nInvoices by Default.%n%n");

		System.out.printf("ID\tDEPT\t\tDESCRIPTION\t\t\tQTY\tPRICE%n====\t=========="
				+ "\t==============================\t====\t======%n");
		invoices.stream().forEach(System.out::println);

		displayMenu();
	}

	public static void invoiceByDept() throws FileNotFoundException {
		System.out.printf("%nInvoices by Department.%n%n");

		Function<Invoice, String> byDept = Invoice::getDepartment;
		Comparator<Invoice> listByDept = Comparator.comparing(byDept);

		System.out.printf("ID\tDEPT\t\tDESCRIPTION\t\t\tQTY\tPRICE%n====\t=========="
				+ "\t==============================\t====\t======%n");
		invoices.stream().sorted(listByDept).forEach(System.out::println);

		displayMenu();
	}

	public static void invoiceByQty() throws FileNotFoundException {
		System.out.printf("%nInvoices by Quantity.%n%n");

		Function<Invoice, Integer> byQty = Invoice::getQuantity;
		Comparator<Invoice> listByQty = Comparator.comparing(byQty);

		System.out.printf("ID\tDEPT\t\tDESCRIPTION\t\t\tQTY\tPRICE%n====\t=========="
				+ "\t==============================\t====\t======%n");
		invoices.stream().sorted(listByQty).forEach(System.out::println);

		displayMenu();
	}

	public static void deptSummary() throws FileNotFoundException {
		System.out.printf("%nDepartment Summary.%n%n");

		Map<String, List<Invoice>> deptGroup = invoices.stream().collect(Collectors.groupingBy(i -> i.getDepartment()));

		deptGroup.forEach((dept, invs) -> {
			System.out.printf("%s%n", dept);
			System.out
					.printf("  Sold: %4d  Total: %10s%n",
							invs.stream().map(a -> a.getQuantity())
									.reduce(0,
											(x, y) -> x
													+ y),
							NumberFormat.getCurrencyInstance()
									.format(invs.stream()
											.map(a -> a.getPrice().multiply(new BigDecimal(a.getQuantity())))
											.reduce(BigDecimal.ZERO, (x, y) -> x.add(y)).doubleValue())

			);
		});

		displayMenu();
	}

	public static void exit() {
		System.out.println("Good bye");
		System.exit(0);
	}
}
