package catalogo;

import catalogo.adapter.in.controller.LivroController;
import catalogo.adapter.out.jdbc.JdbcLivroRepository;
import catalogo.adapter.out.memory.InMemoryLivroRepository;
import catalogo.adapter.out.notification.ConsoleNotificadorDeLivroCadastrado;
import catalogo.adapter.out.notification.SmtpEmailSettings;
import catalogo.adapter.out.notification.SmtpNotificadorDeLivroCadastrado;
import catalogo.domain.ports.LivroRepository;
import catalogo.domain.ports.NotificadorDeLivroCadastrado;
import catalogo.domain.model.Livro;
import catalogo.domain.service.LivroService;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        boolean usarJdbc = Arrays.stream(args).noneMatch(a -> a.equalsIgnoreCase("inmemory"));

        List<NotificadorDeLivroCadastrado> notificadores = new ArrayList<>();

        notificadores.add(new ConsoleNotificadorDeLivroCadastrado());

        SmtpEmailSettings smtpCfg = SmtpEmailSettings.fromEnv();
        if (smtpCfg.isConfigured()) {
            notificadores.add(new SmtpNotificadorDeLivroCadastrado(smtpCfg));
            System.out.println("[EMAIL] SMTP habilitado: host=" + smtpCfg.host + " from=" + smtpCfg.from + " to=" + smtpCfg.to);
        } else {
            System.out.println("[EMAIL] SMTP não configurado (usando apenas console).");
        }

        if (usarJdbc) {
            JdbcLivroRepository jdbc = new JdbcLivroRepository();
            try (
                 Scanner scanner = new Scanner(System.in)) {
                executarLoop(new LivroController(new LivroService(jdbc, notificadores)), scanner);
            } catch (Exception e) {
                System.err.println("Erro fatal: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            try (Scanner scanner = new Scanner(System.in)) {
                LivroRepository repo = new InMemoryLivroRepository();
                executarLoop(new LivroController(new LivroService(repo, notificadores)), scanner);
            } catch (Exception e) {
                System.err.println("Erro fatal: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void executarLoop(LivroController controller, Scanner scanner) {
        while (true) {
            System.out.println("\n1 - Cadastrar Livro");
            System.out.println("2 - Buscar Livro por ID");
            System.out.println("3 - Listar todos os Livros");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            int opcao;
            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida.");
                continue;
            }

            switch (opcao) {
                case 1 -> {
                    System.out.print("Título: ");
                    String titulo = scanner.nextLine();
                    System.out.print("Autor: ");
                    String autor = scanner.nextLine();
                    System.out.print("Ano de Publicação: ");
                    int ano;
                    try {
                        ano = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Ano inválido.");
                        continue;
                    }
                    try {
                        Livro livro = controller.cadastrarLivro(titulo, autor, ano);
                        System.out.println("Livro cadastrado: " + livro);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Falha de validação: " + e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.print("ID do Livro: ");
                    String idStr = scanner.nextLine();
                    try {
                        UUID id = UUID.fromString(idStr);
                        controller.buscarLivroPorId(id).ifPresentOrElse(
                                System.out::println,
                                () -> System.out.println("Livro não encontrado.")
                        );
                    } catch (IllegalArgumentException e) {
                        System.out.println("ID inválido.");
                    }
                }
                case 3 -> {
                    List<Livro> livros = controller.listarLivros();
                    if (livros.isEmpty()) {
                        System.out.println("Nenhum livro cadastrado.");
                    } else {
                        livros.forEach(System.out::println);
                    }
                }
                case 0 -> {
                    System.out.println("Encerrando...");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }
}
