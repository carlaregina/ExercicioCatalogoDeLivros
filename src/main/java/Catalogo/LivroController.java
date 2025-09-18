package Catalogo;

import java.util.*;

public class LivroController {


    private final LivroService service;

    public LivroController(LivroService service) {
        this.service = service;
    }

    public Livro cadastrarLivro(String titulo, String autor, int anoPublicacao) {
        return service.cadastrarLivro(titulo, autor, anoPublicacao);
    }

    public Optional<Livro> buscarLivroPorId(UUID id) {
        return service.buscarPorId(id);
    }

    public List<Livro> listarLivros() {
        return service.listarTodos();
    }

}
