package br.com.analisesentimento.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author richard.santana
 * 
 */
public class BDUtil {

	private static Connection conn;

	public static Connection getInstance() {
		if (conn == null) {
			BDUtil util = new BDUtil();
			conn = util.criaConexao();
		}
		return conn;
	}

	private Connection criaConexao() {
		String conexao = "jdbc:sqlserver://HOMOLOGA-DEV\\SQL2008EX;databaseName=PESQUISA_TWEET;user=pesquisa_tweet;password=1234;";
		Connection conn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
					.newInstance();
			conn = DriverManager.getConnection(conexao);
			System.out.println("Conexão obtida com sucesso.");
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception e) {
			System.out
					.println("Problemas ao tentar conectar com o banco de dados: "
							+ e);
		}
		return conn;
	}
}
