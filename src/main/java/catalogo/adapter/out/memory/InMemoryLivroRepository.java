package catalogo.adapter.out.memory;

import catalogo.domain.model.Livro;
import catalogo.domain.ports.LivroRepository;

import java.util.*;

public class InMemoryLivroRepository implements LivroRepository {

    private final Map<UUID, Livro> banco = new HashMap<>();

    @Override
    public Livro salvar(Livro livro) {
        banco.put(livro.getId(), livro);
        return livro;
    }

    @Override
    public Optional<Livro> buscarPorId(UUID id) {
        return Optional.ofNullable(banco.get(id));
    }

    @Override
    public List<Livro> listarTodos() {
        return new ArrayList<>(banco.values());
    }

    @Override
    public void close() {

    }


}
