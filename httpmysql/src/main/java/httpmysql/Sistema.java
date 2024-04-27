package httpmysql;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Sistema {

	static final String URL = "jdbc:mysql://localhost:3306/";
	static final String BANCO_DE_DADOS = "banquinho";
	static final String USUARIO = "root";
	static final String SENHA = "1234";

	public static void main(String[] args) {
		System.out.println("sisteminha criado pra salvar endereços buscados");
		System.out.println("digite S para salvar novo endereço");
		Scanner scanner = new Scanner(System.in);
		String on = scanner.nextLine();
		
		
		while (on.equals("s")  || on.equals("S")) {
			try {
				Connection conexao = obterConexao();
				criarTabela(conexao);
				salvarEndereco(conexao, buscarEndereco());
				conexao.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			System.out.print("Se deseja continuar adicionando digite S");
			scanner = new Scanner(System.in);
			on = scanner.nextLine();
		}
		
		Connection conexao;
		try {
			conexao = obterConexao();
			exibirEnderecos(conexao);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	public static Connection obterConexao() throws SQLException {
		Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
		criarBancoSeNaoExistir(conexao);
		conexao.setCatalog(BANCO_DE_DADOS);
		return conexao;
	}

	public static void criarBancoSeNaoExistir(Connection conexao) throws SQLException {
		try (Statement statement = conexao.createStatement()) {
			// Cria o banco de dados se não existir
			String sql = "CREATE DATABASE IF NOT EXISTS " + BANCO_DE_DADOS;
			statement.executeUpdate(sql);
			System.out.println("Banco de dados criado com sucesso.");
		}
	}

	public static void criarTabela(Connection conexao) throws SQLException {
		try (Statement statement = conexao.createStatement()) {
			// Cria a tabela
			String sql = "CREATE TABLE IF NOT EXISTS endereco (\r\n"
					+ "    id INT AUTO_INCREMENT PRIMARY KEY,\r\n"
					+ "    cep VARCHAR(10) NOT NULL,\r\n"
					+ "    uf VARCHAR(2) NOT NULL,\r\n"
					+ "    cidade VARCHAR(255) NOT NULL,\r\n"
					+ "    vizinhanca VARCHAR(255),\r\n"
					+ "    rua VARCHAR(255) NOT NULL\r\n"
					+ ");";
			statement.executeUpdate(sql);
			System.out.println("Tabela criada com sucesso.");
		}
	}

	public static void salvarEndereco(Connection conexao, String[] endereco) throws SQLException {
		String sql = "INSERT INTO endereco (cep, uf, cidade, vizinhanca, rua) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement preparedStatement = conexao.prepareStatement(sql)) {
			preparedStatement.setString(1, endereco[0]);
			preparedStatement.setString(2, endereco[1]);
			preparedStatement.setString(3, endereco[2]);
			preparedStatement.setString(4, endereco[3]);
			preparedStatement.setString(5, endereco[4]);
			preparedStatement.executeUpdate();
			System.out.println("Endereço salvo no banco de dados com sucesso.");
		}
	}
	
	
	public static void exibirEnderecos(Connection conexao) throws SQLException {
        try (Statement statement = conexao.createStatement()) {
            String sql = "SELECT * FROM endereco";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String cep = resultSet.getString("cep");
                String uf = resultSet.getString("uf");
                String cidade = resultSet.getString("cidade");
                String vizinhanca = resultSet.getString("vizinhanca");
                String rua = resultSet.getString("rua");
                System.out.println("CEP: " + cep + ", UF: " + uf + ", Cidade: " + cidade + ", Vizinhança: " + vizinhanca + ", Rua: " + rua);
            }
        }
    }
//--------------------------------------------------------------------------------------

	public static String fazerRequisicaoGet(String url) {
		// Crie um cliente HTTP
		HttpClient httpClient = HttpClient.newHttpClient();

		// Crie uma solicitação GET com a URL especificada
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

		// Envie a solicitação assincronamente e obtenha a resposta como uma string
		CompletableFuture<String> future = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenApply(HttpResponse::body);

		// Aguarde a conclusão da solicitação e retorne a resposta
		try {
			return future.get();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String[] buscarEndereco() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Digite seu CEP: ");
		String buscarCep = scanner.nextLine();

		String url = "https://brasilapi.com.br/api/cep/v1/" + buscarCep;
		String resposta = fazerRequisicaoGet(url);
		// System.out.println("Resposta da API: " + resposta);

		String[] respostas = resposta.split(",");
		String cep = respostas[0].replace("\"", "").replace("{cep:", "");
		if (cep.length() > 8) {
			System.out.println("cep errado!");
			buscarEndereco();
		}

		String uf = respostas[1].replace("\"", "").replace("state:", "");
		String cidade = respostas[2].replace("\"", "").replace("city:", "");
		String vizinhanca = respostas[3].replace("\"", "").replace("neighborhood:", "");
		String rua = respostas[4].replace("\"", "").replace("street:", "");

		return new String[] { cep, uf, cidade, vizinhanca, rua };
	}

}
