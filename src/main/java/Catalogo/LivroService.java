package Catalogo;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class LivroService {

    private final LivroRepository repository;

    public LivroService(LivroRepository repository) {
        this.repository = repository;
    }

    public Livro cadastrarLivro(String titulo, String autor, int anoPublicacao) {
        Livro livro = new Livro(null,titulo, autor, anoPublicacao);
        return repository.salvar(livro);
    }

    public Optional<Livro> buscarPorId(UUID id) {
        return repository.buscarPorId(id);
    }

    public List<Livro> listarTodos() {
        return repository.listarTodos();
    }

}

