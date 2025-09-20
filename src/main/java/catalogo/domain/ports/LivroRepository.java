package catalogo.domain.ports;

import catalogo.domain.model.Livro;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LivroRepository {

    Livro salvar(Livro livro);

    Optional<Livro> buscarPorId(UUID id);

    List<Livro> listarTodos();

    void close();
}

