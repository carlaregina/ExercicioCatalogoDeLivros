package catalogo.domain.service;

import catalogo.domain.model.Livro;
import catalogo.domain.ports.LivroRepository;
import catalogo.domain.ports.NotificadorDeLivroCadastrado;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LivroService {


    private final LivroRepository repository;
    private final List<NotificadorDeLivroCadastrado> notificadores;

    public LivroService(LivroRepository repository, List<NotificadorDeLivroCadastrado> notificadores) {
        this.repository = repository;
        this.notificadores = notificadores;
    }


    public Livro cadastrarLivro(String titulo, String autor, int anoPublicacao) {

        Livro livro = new Livro(null, titulo, autor, anoPublicacao);
        Livro salvo = repository.salvar(livro);
        notificarCadastro(salvo);
        return salvo;

    }

    public Optional<Livro> buscarPorId(UUID id) {
        return repository.buscarPorId(id);
    }

    public List<Livro> listarTodos() {
        return repository.listarTodos();
    }


    private void notificarCadastro(Livro livro) {
        for (NotificadorDeLivroCadastrado n : notificadores) {
            try {
                n.notificar(livro);
            } catch (Exception e) {

                System.err.println("[NOTIF] Falha ao notificar: " + e.getMessage());
            }
        }
    }


}

