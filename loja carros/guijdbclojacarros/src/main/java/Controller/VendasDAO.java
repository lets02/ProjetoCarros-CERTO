package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Connection.ConnectionFactory;
import Model.Vendas;

public class VendasDAO {

    private Connection connection;

    public VendasDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    public void criaTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS vendas_lojacarros (DATA VARCHAR(255),CLIENTE VARCHAR(255),VALOR VARCHAR(255),CARRO VARCHAR(255) PRIMARY KEY)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela criada com sucesso.");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar a tabela: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(this.connection);
        }
    }

    public List<Vendas> listarTodos() {
        List<Vendas> vendas = new ArrayList<>();
        String query = "SELECT * FROM vendas_lojacarros";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vendas venda = new Vendas(
                        rs.getString("data"),
                        rs.getString("cliente"),
                        rs.getString("valor"),
                        rs.getString("carro")
                );
                vendas.add(venda);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(connection);
        }
        return vendas;
    }

    public void cadastrar(String data, String cliente, String valor, String carro) {
        String sql = "INSERT INTO vendas_lojacarros (cliente, data, carro, valor) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente);
            stmt.setString(2, data);
            stmt.setString(3, carro);
            stmt.setString(4, valor);
            stmt.executeUpdate();
            System.out.println("Dados inseridos com sucesso");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir dados no banco de dados.", e);
        } finally {
            ConnectionFactory.closeConnection(connection);
        }
    }

    public void atualizar(String data, String cliente, String valor, String carro) {
        String sql = "UPDATE vendas_lojacarros SET data = ?, cliente = ?, valor = ? WHERE carro = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, data);
            stmt.setString(2, cliente);
            stmt.setString(3, valor);
            stmt.setString(4, carro);
            stmt.executeUpdate();
            System.out.println("Dados atualizados com sucesso");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar dados no banco de dados.", e);
        } finally {
            ConnectionFactory.closeConnection(connection);
        }
    }

    public void apagar(String carro) {
        String sql = "DELETE FROM vendas_lojacarros WHERE carro = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, carro);
            stmt.executeUpdate();
            System.out.println("Dado apagado com sucesso");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao apagar dados no banco de dados.", e);
        } finally {
            ConnectionFactory.closeConnection(connection);
        }
    }
}
