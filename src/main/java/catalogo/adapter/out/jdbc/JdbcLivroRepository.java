package catalogo.adapter.out.jdbc;

import catalogo.domain.model.Livro;
import catalogo.domain.ports.LivroRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JdbcLivroRepository implements LivroRepository {


    private final Connection connection;

    public JdbcLivroRepository() {
        try {
            this.connection = DriverManager.getConnection("jdbc:h2:mem:livrosdb;DB_CLOSE_DELAY=-1");
            criarTabela();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco H2", e);
        }
    }

    private void criarTabela() throws SQLException {
        final String sql = """
            CREATE TABLE IF NOT EXISTS livros (
                id UUID PRIMARY KEY,
                titulo VARCHAR(255) NOT NULL,
                autor VARCHAR(255) NOT NULL,
                ano_publicacao INT NOT NULL
            )
            """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    @Override
    public Livro salvar(Livro livro) {
        final String sql = "INSERT INTO livros (id, titulo, autor, ano_publicacao) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, livro.getId());
            stmt.setString(2, livro.getTitulo());
            stmt.setString(3, livro.getAutor());
            stmt.setInt(4, livro.getAnoPublicacao());
            stmt.executeUpdate();
            return livro;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar livro", e);
        }
    }

    @Override
    public Optional<Livro> buscarPorId(UUID id) {
        final String sql = "SELECT id, titulo, autor, ano_publicacao FROM livros WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapearLivro(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro por ID", e);
        }
    }

    @Override
    public List<Livro> listarTodos() {
        final List<Livro> livros = new ArrayList<>();
        final String sql = "SELECT id, titulo, autor, ano_publicacao FROM livros";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) livros.add(mapearLivro(rs));
            return livros;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar livros", e);
        }
    }

    private Livro mapearLivro(ResultSet rs) throws SQLException {

        UUID id = (UUID) rs.getObject("id");
        return new Livro(
                id,
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getInt("ano_publicacao")
        );
    }


    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {

        }
    }
}
