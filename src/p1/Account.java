package p1;
import java.util.UUID;

public class Account {
	private String email, password, lastName, firstName, phone;
	private UUID accountNumber;
	private double balance;
	
	public Account(String email, String password, String firstName, String lastName, String phone, double balance) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.accountNumber = UUID.randomUUID();
		this.balance = balance;
	}
	
	public String getEmail() {
		return email;
	}
	
	public boolean comparePassword(String enteredPassword) {
		if (enteredPassword.equals(this.password)) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getFirstName() {
		return firstName; 
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public UUID getAccountNumber() {
		return accountNumber;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public double addBalance(double amount) {
		return this.balance += amount;
	}
	
	public String toString() {
		return  "Account Number: " + accountNumber + "\n" +
				"Name: " +	firstName + " " + lastName + "\n" +
				"Phone: " + phone + "\n" +
				"Balance: $" + balance;
	}
}
