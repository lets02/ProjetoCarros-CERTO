package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import Connection.ConnectionFactory;
import Model.Carros;

public class CarrosDAO {

    private Connection connection;
    private List<Carros> carros;

    public CarrosDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    public void criaTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS carros_lojacarros (MARCA VARCHAR(255),MODELO VARCHAR(255),ANO VARCHAR(255),PLACA VARCHAR(255) PRIMARY KEY, VALOR VARCHAR(255))";
        try (Statement stmt = this.connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela criada com sucesso.");
        } catch (SQLException e) {
            handleSQLException(e, "Erro ao criar a tabela: ");
            throw new RuntimeException("Erro ao criar a tabela: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(this.connection);
        }
    }

    public List<Carros> listarTodos() {
        PreparedStatement stmt = null;
        carros = new ArrayList<>();

        try {
            stmt = connection.prepareStatement("SELECT * FROM carros_lojacarros");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Carros carro = new Carros(
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("ano"),
                        rs.getString("placa"),
                        rs.getString("valor"));
                carros.add(carro);
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao listar todos os carros: " + ex.getMessage());
        } finally {
            ConnectionFactory.closeConnection(connection, stmt);
        }

        return carros;
    }

    public void cadastrar(String marca, String modelo, String ano, String placa, String valor) {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO carros_lojacarros (marca, modelo, ano, placa, valor) VALUES (?, ?, ?, ?, ?)";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, marca);
            stmt.setString(2, modelo);
            stmt.setString(3, ano);
            stmt.setString(4, placa);
            stmt.setString(5, valor);
            stmt.executeUpdate();
            System.out.println("Dados inseridos com sucesso");
            JOptionPane.showMessageDialog(null, "Você Cadastrou o carro com sucesso✅");
        } catch (SQLException e) {
            handleSQLException(e, "Erro ao inserir dados no banco de dados: ");
        } finally {
            ConnectionFactory.closeConnection(connection, stmt);
        }
    }

    public void atualizar(String marca, String modelo, String ano, String placa, String valor) {
        PreparedStatement stmt = null;
        String sql = "UPDATE carros_lojacarros SET marca = ?, modelo = ?, ano = ?, valor = ? WHERE placa = ?";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, marca);
            stmt.setString(2, modelo);
            stmt.setString(3, ano);
            stmt.setString(4, valor);
            stmt.setString(5, placa);
            stmt.executeUpdate();
            System.out.println("Dados atualizados com sucesso");
        } catch (SQLException e) {
            handleSQLException(e, "Erro ao atualizar dados no banco de dados: ");
        } finally {
            ConnectionFactory.closeConnection(connection, stmt);
        }
    }

    public void apagar(String placa) {
        PreparedStatement stmt = null;
        String sql = "DELETE FROM carros_lojacarros WHERE placa = ?";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, placa);
            stmt.executeUpdate();
            System.out.println("Dado apagado com sucesso");
        } catch (SQLException e) {
            handleSQLException(e, "Erro ao apagar dados no banco de dados: ");
        } finally {
            ConnectionFactory.closeConnection(connection, stmt);
        }
    }

    private void handleSQLException(SQLException e, String errorMessage) {
        if ("23505".equals(e.getSQLState())) {
            JOptionPane.showMessageDialog(null, "Erro: A placa inserida já existe na tabela.");
        } else {
            System.out.println(errorMessage + e.getMessage());
            throw new RuntimeException(errorMessage + e.getMessage(), e);
        }
    }
}
