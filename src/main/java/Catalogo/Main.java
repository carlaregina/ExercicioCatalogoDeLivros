package Catalogo;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {

        LivroService service = new LivroService(new JdbcLivroRepository());
        LivroController controller = new LivroController(service);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n1 - Cadastrar Livro");
            System.out.println("2 - Buscar Livro por ID");
            System.out.println("3 - Listar todos os Livros");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();
            switch (opcao) {
                case 1 -> {
                    System.out.print("Título: ");
                    String titulo = scanner.nextLine();
                    System.out.print("Autor: ");
                    String autor = scanner.nextLine();
                    System.out.print("Ano de Publicação: ");
                    int ano = scanner.nextInt();
                    Livro livro = controller.cadastrarLivro(titulo, autor, ano);
                    System.out.println("Livro cadastrado: " + livro);
                }
                case 2 -> {
                    System.out.print("ID do Livro: ");
                    String idStr = scanner.nextLine();
                    try {
                        UUID id = UUID.fromString(idStr);
                        Optional<Livro> livro = controller.buscarLivroPorId(id);
                        livro.ifPresentOrElse(
                                System.out::println,
                                () -> System.out.println("Livro não encontrado.")
                        );
                    } catch (IllegalArgumentException e) {
                        System.out.println("ID inválido.");
                    }
                }
                case 3 -> {
                    List<Livro> livros = controller.listarLivros();
                    livros.forEach(System.out::println);
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

