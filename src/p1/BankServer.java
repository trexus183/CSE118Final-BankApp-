package p1;

public class BankServer {
	private Account[] accounts;
	private double interestRate;
	
	public BankServer() {
		this.interestRate = 0;
		this.accounts = new Account[0];
	}
	
	public void mature() { // Add money to all accounts as if one day passed and they accrued interest
		for (int i = 0; i < accounts.length; i++) { // Loop through customers, and for each one deposit the required amount
			System.out.println(accounts[i].getFirstName() + " earned $" + ((0.01 * interestRate * accounts[i].getBalance())/365) + " in interest!");
			System.out.println("Their new balance is $" + accounts[i].addBalance(((0.01 * interestRate * accounts[i].getBalance()))/365));
		}
	}
	
	public boolean setInterest(String password, double rate) { // Returns success status
		if (password.equals("301505403")) { // hashed version of "password"
			this.interestRate = rate;
			return true;
		} else {
			return false;
		}
	}
	
	public void createAccount(String email, String password, String fName, String lName, String phone) { // Add an account to the accounts array		
		Account newCustomer = new Account(email, password, fName, lName, phone, 0); // All data filled in by user (except balance)
		
		Account[] tempArr = accounts;
		accounts = new Account[accounts.length+1];
		
		for (int i = 0; i < tempArr.length; i++) {
			accounts[i] = tempArr[i];
		}
		
		accounts[accounts.length-1] = newCustomer;
	}
	
	public boolean isEmailTaken (String email) { // Check to see if the email is taken already
		for (int i = 0; i < accounts.length; i++) {
			if (accounts[i].getEmail() == email) {
				return true;
			}
		}
		return false;
	}
	
	public Account getAccount (String email, String password) { // Just returns a copy of an account based on email and pwd
		for (int i = 0; i < accounts.length; i++) {
			if (accounts[i].getEmail().equals(email)) {				
				if (accounts[i].comparePassword(password)) {
					return accounts[i];
				} else {
					return null;
				}
			}
		}
		return null;
	}
	
	
	public double deposit(String email, String password, double amount) {
		return getAccount(email, password).addBalance(amount); //TODO: Reciept
	}
	
	public double withdraw(String email, String password, double amount) {
		return getAccount(email, password).addBalance(-amount); //TODO: Reciept
	}
	
	public void delete(String email, String password) {
		for (int i = 0; i < accounts.length; i++) {
			if (accounts[i].getEmail().equals(email)) {
				Account[] temp = new Account[accounts.length-1];
				for (int j = 0; j < accounts.length; j++) {
					if (i != j) {
						temp[temp.length-1] = accounts[j];
					}
				}
				accounts = temp;
			}
		}
	}
}
