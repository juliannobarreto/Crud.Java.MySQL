package jmysql;

import java.util.Scanner;

public class Utils {
	
	static Scanner input = new Scanner(System.in);
	
	public static void connecting() {
		System.out.println("Connecting...");
	}
	
	public static void disconnecting() {
		System.out.println("Disconnecting...");
	}
	
	public static  void list () {
		System.out.println("List products...");
	}
	
	public static void insert() {
		System.out.println("Insert products...");
	}
	
	public static void updating() {
		System.out.println("Updating products...");
	}
	
	public static void delete() {
		System.out.println("Delete products...");
	}
	
	public static void menu() {
		System.out.println("==================Product Management===============");
		System.out.println("Select an option: ");
		System.out.println("1 - List products.");
		System.out.println("2 - Insert products.");
		System.out.println("3 - Updating products.");
		System.out.println("4 - Delete products.");
		
		int option = Integer.parseInt(input.nextLine());
		if(option == 1) {
			list();
		}else if(option == 2) {
			insert();
		}else if(option == 3) {
			updating();
		}else if(option == 4) {
			delete();
		}else {
			System.out.println("Invalid option.");
		}
	}
}
