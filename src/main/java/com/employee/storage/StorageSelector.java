package com.employee.storage;

import java.util.Scanner;

public class StorageSelector {
	private static final Scanner sc = new Scanner(System.in);

	public static StorageType chooseStorage() {
		System.out.println("""
				Select storage Type
				1.File(Json)
				2.Postgres
				3.MySql
				4.SupaBase
				""");
		int choice = sc.nextInt();

		return switch (choice) {
		case 1 -> StorageType.FILE;
		case 2 -> StorageType.POSTGRES;
		case 3 -> StorageType.MYSQL;
		case 4->StorageType.SUPABASE;
		default -> throw new IllegalArgumentException("Invalid choice");
		};

	}

}
