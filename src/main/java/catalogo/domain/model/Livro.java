package catalogo.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Livro {

    private final UUID id;
    private final String titulo;
    private final String autor;
    private final int anoPublicacao;

    public Livro(UUID id, String titulo, String autor, int anoPublicacao) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.titulo = Objects.requireNonNull(titulo, "titulo é obrigatório").trim();
        this.autor = Objects.requireNonNull(autor, "autor é obrigatório").trim();
        this.anoPublicacao = anoPublicacao;
        validar();
    }

    private void validar() {
        if (titulo.isEmpty()) throw new IllegalArgumentException("titulo não pode ser vazio");
        if (autor.isEmpty()) throw new IllegalArgumentException("autor não pode ser vazio");
        if (anoPublicacao < 1400 || anoPublicacao > 2100)
            throw new IllegalArgumentException("anoPublicacao inválido: " + anoPublicacao);
    }

    public UUID getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getAnoPublicacao() { return anoPublicacao; }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", anoPublicacao=" + anoPublicacao +
                '}';
    }
}
