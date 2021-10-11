package jmysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Utils {
	
	static Scanner input = new Scanner(System.in);
	
	public static Connection connecting() {
		String CLASSE_DRIVER = "com.mysql.dbc.Driver";
		String USUARIO = "root";
		String SENHA = "198205";
		String URL_SERVIDOR = "jdbc:mysql://localhost:3306/jmysql?useSSL=false";
		
		try {
			Class.forName(CLASSE_DRIVER);
			return DriverManager.getConnection( URL_SERVIDOR, USUARIO, SENHA);
		} catch (Exception e) {
			if(e instanceof ClassNotFoundException) {
				System.out.println("Check the connection driver.");
			}else {
				System.out.println("Check if the server is active. ");
			}
			System.exit(-42);
			return null;
		}
	}
	
	public static void disconnecting(Connection conn) {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("Unable to close the connection.");
			}
		}
	}
	
	public static  void list () {
		String BUSCAR_TODOS = "SELECT * FROM produtos";
		
		try {
			Connection conn = connecting();
			PreparedStatement product = conn.prepareStatement(BUSCAR_TODOS);
			ResultSet res = product.executeQuery();
			
			res.last();
			int qtd = res.getRow();
			res.beforeFirst();
			
			if(qtd > 0) {
				System.out.println("listing products...");
				System.out.println("--------------------------");
				
				while(res.next()) {
					System.out.println("ID: " + res.getInt(1));
					System.out.println("Product: " + res.getString(2));
					System.out.println("Price: " + res.getFloat(3));
					System.out.println("Inventory: " + res.getInt(4));
					System.out.println("----------------------------");
				}			
			}else {
				System.out.println("There are no registered products.");
			}
			product.close();
			disconnecting(conn);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error searching for products.");
			System.exit(-42);
		}
	}
	
	public static void insert() {
		System.out.println("Enter the product name. ");
		String nome = input.nextLine();
		
		System.out.println("Enter the product price. ");
		float preco = input.nextFloat();
		
		System.out.println("Inform the quantity of the product.");
		int estoque = input.nextInt();
		
		String INSERT = "INSERT INTO produtos (nome, preco, estoque) VALUES (?, ?, ?)";
		// SQL Injection
		
		try {
			Connection conn = connecting();
			PreparedStatement salve = conn.prepareStatement(INSERT);
			
			salve.setString(1, nome);
			salve.setFloat(2, preco);
			salve.setInt(3, estoque);
			
			salve.executeLargeUpdate();
			salve.close();
			disconnecting(conn);
			System.out.println("The product " + nome + " was entered successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error saving the product.");
			System.exit(-42);
		}		
	}
	
	public static void updating() {
		System.out.println("Enter the product code: ");
		int id = Integer.parseInt(input.nextLine());
		
		String BUSCAR_POR_ID = "SELECT * FROM product WHERE id+?";
		
		try {
			Connection conn = connecting();
			PreparedStatement product = conn.prepareStatement(BUSCAR_POR_ID);
			product.setInt(1, id);
			ResultSet res = product.executeQuery();
			
			res.last();
			int qtd = res.getRow();
			res.beforeFirst();
			
			if(qtd > 0) {
				System.out.println("Enter the product name: ");
				String nome = input.nextLine();
				
				System.out.println("Inform the price of the product: ");
				float preco = input.nextFloat();
				
				System.out.println("Inform quantity in stock.");
				int estoque = input.nextInt();
				
				String ATUALIZAR = "UPDATE produto SET nome=?, preco=?, estoque=? WHERE id=?";
				PreparedStatement upd = conn.prepareStatement(ATUALIZAR);
				
				upd.setString(1, nome);
				upd.setFloat(2, preco);
				upd.setInt(3, estoque);
				upd.setInt(4, id);
				
				upd.executeUpdate();
				upd.close();
				disconnecting(conn);
				System.out.println("O produto " + nome + "foi atualizado com sucesso.");
			}else {
				System.out.println("There is no product with the entered id.");
			}
							
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro ao atualizar o produto");
			System.exit(-42);
		}	
	}
	
	public static void delete() {
		String DELETAR = "DELETE FROM produtos WHERE id=?";
		String BUSCAR_POR_ID = "SELECT * FROM produtos WHERE id=?";
		
		System.out.println("Informe o codigo do produto: ");
		int id = Integer.parseInt(input.nextLine());
		
		try {
			Connection conn = connecting();
			PreparedStatement produto = conn.prepareStatement(BUSCAR_POR_ID);
			produto.setInt(1, id);
			ResultSet res = produto.executeQuery();
			
			res.last();
			int qtd = res.getRow();
			res.beforeFirst();
			
			if (qtd > 0) {
				PreparedStatement del = conn.prepareStatement(DELETAR);
				del.setInt(1, id);
				del.executeUpdate();
				del.close();
				disconnecting(conn);
				System.out.println("The product was successfully deleted.");
			}else {
				System.out.println("There is no product with an informed id.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error deleting product.");
			System.exit(-42);
		}
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
