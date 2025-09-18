package Catalogo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JdbcLivroRepository implements LivroRepository{

    private final Connection connection;

    public JdbcLivroRepository() {
        try {
            connection = DriverManager.getConnection("jdbc:h2:mem:livrosdb;DB_CLOSE_DELAY=-1");
            criarTabela();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco H2", e);
        }
    }

    private void criarTabela() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS livros (
                id UUID PRIMARY KEY,
                titulo VARCHAR(255),
                autor VARCHAR(255),
                ano_publicacao INT
            )
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    @Override
    public Livro salvar(Livro livro) {
        String sql = "INSERT INTO livros (id, titulo, autor, ano_publicacao) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, livro.getId());
            stmt.setString(2, livro.getTitulo());
            stmt.setString(3, livro.getAutor());
            stmt.setInt(4, livro.getAnoPublicacao());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar livro", e);
        }
        return livro;
    }

    @Override
    public Optional<Livro> buscarPorId(UUID id) {
        String sql = "SELECT * FROM livros WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearLivro(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Livro> listarTodos() {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livros";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                livros.add(mapearLivro(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar livros", e);
        }
        return livros;
    }

    private Livro mapearLivro(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        String titulo = rs.getString("titulo");
        String autor = rs.getString("autor");
        int ano = rs.getInt("ano_publicacao");
        Livro livro = new Livro(null,titulo, autor, ano);
        try {
            var idField = Livro.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(livro, id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao setar ID", e);
        }
        return livro;
    }
}
