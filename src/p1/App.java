package p1;

import java.util.Scanner;

public class App {
	// Initialize the server
	static BankServer server = new BankServer();
	
	// These variables are used throughout the project, and are therefore globablized
	static Scanner input = new Scanner(System.in);
	
	// These variables will be used to let the user reAuthenticate without having to type their email and password again
	static String authEmail = "";
	static String authPassword = "";
	
	public static void main(String[] args) {
		// We also define some local varaiables that will be used later
		String clientRequest = "";
		
		System.out.println("Welcome! To begin, you must create an account with us.");
		createAccount();
		
		while (!clientRequest.equalsIgnoreCase("quit")) {
			System.out.println("Enter a command");
			
			if (authEmail.equals("") && authPassword.equals("")) { // We need to make sure the user is authenticated before they are allowed to make requests
				System.out.println("You must authenticate to continue. Please enter either login, to login, or create, to create a new account");
				clientRequest = input.nextLine();
				switch (clientRequest.toLowerCase()) { // Process client requests
					case "create": // Create a new account
						createAccount();
						break;
						
					case "login": // Login to an existing account TODO: make it work lol
						System.out.println("enter your email");
						String email = input.nextLine();
						System.out.println("enter your password");
						String password = encrypt(input.nextLine());
						
						Account temp = server.getAccount(email, password);
						if (temp != null) {
							System.out.println("Logged in");
							authEmail = email;
							authPassword = password;
						} else {
							System.out.println("Incorrect email or password. Try again");
						}
						
						break;
					default:
						System.out.println("Command was not recognized");
						break;
				}
			} else { // If the user is authenitcated we let them into the system
				clientRequest = input.nextLine();
				switch (clientRequest.toLowerCase()) {
					case "help":
						System.out.println("Info: Print account information"
								+ "Logout: Log out of your account"
								+ "Deposit: Add money to your account"
								+ "Withdraw: Remove money from your account"
								+ "SetInterest: Set the global interest rate"
								+ "Mature: Mature all accounts by one day, according to interest rate"
								+ "Delete: Delete your account");
						break;
				
					case "logout": // Logs the user out of the current account, forcing them to reAuth
						authEmail = "";
						authPassword = "";
						break;
						
					case "info": // Just prints out the user's account information
						System.out.println(server.getAccount(authEmail, authPassword));
						
					case "deposit": { // Add money to an account (we need brackets to scope variables in the case)
						System.out.println("How much would you like to deposit?");
						double amount = Double.parseDouble(input.nextLine()); // Pull a double out of the console
						
							// User is caught in this loop if they enter a negative or a x`number to more than the hundreths place
						while (amount < 0 || Double.toString(amount).substring(Double.toString(amount).indexOf(".")).length() > 3) {
							System.out.println("Invalid amount entered\nPlease enter a valid amount to deposit");
							amount = Double.parseDouble(input.nextLine());
						}
						
							// Finally we send a request to the server, and print the server's response, which is the user's new balance
						System.out.println(
							"Your new balance is $"
							+ server.deposit(authEmail, authPassword, amount)
							+ "\nTransaction complete"
						);
						break;
					}
						
					case "withdraw": { // Remove money from an account (we need brackets to scope variables in the case)
						System.out.println("How much would you like to deposit?");
						double amount = Double.parseDouble(input.nextLine()); // Pull a double out of the console
						
							// User is caught in this loop if they enter a negative or a number to more than the hundreths place
						while (amount < 0 || Double.toString(amount).substring(Double.toString(amount).indexOf(".")).length() > 3) {
							System.out.println("Invalid amount entered\nPlease enter a valid amount to deposit");
							amount = Double.parseDouble(input.nextLine());
						}
						
							// Finally we send a request to the server, and print the server's response, which is the user's new balance
						System.out.println(
							"Your new balance is $"
							+ server.withdraw(authEmail, authPassword, amount)
							+ "\nTransaction complete"
						);
						break;
					}
						
					case "setinterest": // Set the global interest rate
						System.out.println("Enter admin password"); // Pulls a String and Double for the admin password and new interest rate
						String password = encrypt(input.nextLine());
						
						System.out.println("What should the yearly interest rate be? Enter a percentage");
						double rate = Double.parseDouble(input.nextLine());
						
						while (!server.setInterest(password, rate)) { // The admin password is, of course, "password"
							System.out.println("Password incorrect\nTry again, or enter quit to exit");
							
							System.out.println("Enter admin password");
							password = input.nextLine();
							
							System.out.println("What should the yearly interest rate be? Enter a percentage");
							rate = Double.parseDouble(input.nextLine());
						}
						
						System.out.println("Interest rate set to " + "% per year\nEnter another request");
						break;
						
					case "mature": //TODO: Allow interest to be calculated by day, rather than year
						System.out.println("Maturing accounts and collecting interest");
						
						server.mature();
						break;
						
					case "quit": // Exit the program. The actual stopping is managed by the while loop, so all we just don't do anything and break the switch
						break;
						
					case "delete":
						server.delete(authEmail, authPassword);
						authEmail = "";
						authPassword = "";
						break;
						
					default:
						System.out.println("Command was not recognized");
						break;
				}
			}
		}
	
		System.out.println("Program Terminated\n\n\n (I'll be back)");
	}
	
	public static String encrypt (String str) { // Generate a salted hash of the passed string
		return Integer.toString((str+"ACn3q#N%Kda#@w20").hashCode());
	}
	
	public static void createAccount() { // Lead the user through account setup
		System.out.println("Enter your email address");
		String email = input.nextLine();
		
		boolean isValidEmail = false;
		while (!isValidEmail) { // User is caught in this while loop until they enter a valid email
			if (email.indexOf("@") != -1 && email.indexOf(".") != 0 ) { // We validate that the email is correctly formatted TODO: Improve formatting check
				if (!server.isEmailTaken(email)) { // We also verify that the email is not already taken
					isValidEmail = true;
				} else {
					System.out.println("Email is already taken. Please enter a different email");
					email = input.nextLine();
				}
			} else {
				System.out.println("Please enter a valid email address");
				email = input.nextLine();
			}
		}
		
		// The email is the only user-entered data that must be unique, so we just register whatever else they enter automatically
		System.out.println("enter a password");
		String password = encrypt(input.nextLine());
		
		System.out.println("enter your first name");
		String fName = input.nextLine();
		
		System.out.println("enter your last name");
		String lName = input.nextLine();
		
		System.out.println("enter your phone number");
		String phone = input.nextLine();
		while (phone.length() != 10) { // We also verify that the phone number is correctly formatted TODO: Improve format checking
			System.out.println("Please enter a valid phone number");
			phone = input.nextLine();
		}
		
		server.createAccount(email, password, fName, lName, phone); // Once we have all their data, we ask the server to create the account
		
		authEmail = email; // Now that the account exists, we will automatically authenticate the user with their account in the future
		authPassword = password;
		
		System.out.println("Account created!\nYour account ID is " + server.getAccount(authEmail, authPassword).getAccountNumber());
	}
}