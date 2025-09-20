package catalogo.domain.ports;

import catalogo.domain.model.Livro;

public interface NotificadorDeLivroCadastrado {

    void notificar(Livro livro);
}
