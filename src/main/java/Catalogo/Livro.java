package Catalogo;

import java.util.UUID;

public class Livro {


    private final UUID id;
    private final String titulo;
    private final String autor;
    private final int anoPublicacao;


    public Livro(UUID id, String titulo, String autor, int anoPublicacao) {

        this.id = (id == null) ? UUID.randomUUID() : id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
    }
    public String getTitulo() {
        return titulo;
    }

    public UUID getId() {
        return id;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public String getAutor() {
        return autor;
    }

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
