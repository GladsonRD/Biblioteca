/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package biblioteca.biblioteca;

import modelo.Livro;
import modelo.Emprestimo;
import controle.Biblioteca;
import dados.Dados;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author glads
 */
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Biblioteca biblioteca = new Biblioteca();

        // popula dados iniciais se necessário
        Dados.carregarDadosIniciais(biblioteca);

        int opcao;
        do {
            System.out.println("\n===== MENU BIBLIOTECA =====");
            System.out.println("1 - Cadastrar Livro");
            System.out.println("2 - Cadastrar Usuário");
            System.out.println("3 - Emprestar Livro");
            System.out.println("4 - Devolver Livro");
            System.out.println("5 - Consultas");
            System.out.println("6 - Listar todos (ordenados por código)");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            opcao = lerInteiro(sc);

            switch (opcao) {
                case 1 ->
                    cadastrarLivro(sc, biblioteca);
                case 2 ->
                    cadastrarUsuario(sc, biblioteca);
                case 3 ->
                    emprestarLivro(sc, biblioteca);
                case 4 ->
                    devolverLivro(sc, biblioteca);
                case 5 ->
                    menuConsultas(sc, biblioteca);
                case 6 ->
                    biblioteca.listarTodosLivrosOrdenadosPorCodigo();
                case 0 ->
                    System.out.println("Encerrando...");
                default ->
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);

        biblioteca.salvarDados();
        sc.close();
    }

    //============================================================
    // MENU — Cadastro Livro
    //============================================================
    private static void cadastrarLivro(Scanner sc, Biblioteca biblioteca) {
        System.out.println("\n--- Cadastro de Livro ---");
        System.out.print("Título: ");
        String titulo = sc.nextLine();
        System.out.print("Autor: ");
        String autor = sc.nextLine();
        System.out.print("Categoria: ");
        String categoria = sc.nextLine();
        System.out.print("Código(Ex:L000): ");
        String codigo = sc.nextLine();
        if (codigo == null || codigo.trim().isEmpty()) {
            System.out.println("❌ Código inválido. Voltando ao menu.");
            return;
        }
        System.out.print("Quantidade de cópias: ");
        int qtd = lerInteiro(sc);
        biblioteca.cadastrarLivro(titulo, autor, categoria, codigo, qtd);
    }

    //============================================================
    // MENU — Cadastro Usuário
    //============================================================
    private static void cadastrarUsuario(Scanner sc, Biblioteca biblioteca) {
        System.out.println("\n--- Cadastro de Usuário ---");
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Matrícula(Ex:U000): ");
        String matricula = sc.nextLine();
        if (matricula == null || matricula.trim().isEmpty()) {
            System.out.println("❌ Matrícula inválida. Voltando ao menu.");
            return;
        }
        System.out.print("E-mail: ");
        String email = sc.nextLine();
        System.out.print("Telefone: ");
        String telefone = sc.nextLine();
        biblioteca.cadastrarUsuario(nome, matricula, email, telefone);
    }

    //============================================================
    // MENU — Emprestimo
    //============================================================
    private static void emprestarLivro(Scanner sc, Biblioteca biblioteca) {
        System.out.println("\n--- Empréstimo ---");
        biblioteca.listarLivrosDisponiveis();
        System.out.print("\nDigite o código do livro (ou 0 para voltar): ");
        String codigo = sc.nextLine();
        if ("0".equals(codigo.trim())) {
            System.out.println("Voltando ao menu...");
            return;
        }
        System.out.print("Digite a matrícula do usuário (ou 0 para voltar): ");
        String matricula = sc.nextLine();
        if ("0".equals(matricula.trim())) {
            System.out.println("Voltando ao menu...");
            return;
        }
        if (biblioteca.buscarUsuarioPorMatricula(matricula) == null) {
            System.out.println("❌ Usuário não encontrado. Operação cancelada.");
            return;
        }
        System.out.print("Confirmar empréstimo? (S/N): ");
        String resp = sc.nextLine().trim().toUpperCase();
        if (!"S".equals(resp)) {
            System.out.println("Operação cancelada.");
            return;
        }
        biblioteca.emprestarLivro(codigo, matricula);
    }

    //============================================================
    // MENU — Devolução
    //============================================================
    private static void devolverLivro(Scanner sc, Biblioteca biblioteca) {
        System.out.println("\n--- Devolução ---");
        System.out.print("Digite a matrícula do usuário (ou 0 para voltar): ");
        String matricula = sc.nextLine();
        if ("0".equals(matricula.trim())) {
            System.out.println("Voltando ao menu...");
            return;
        }
        biblioteca.listarLivrosEmprestadosPorUsuario(matricula);
        System.out.print("\nDigite o código do livro que deseja devolver (ou 0 para voltar): ");
        String codigo = sc.nextLine();
        if ("0".equals(codigo.trim())) {
            System.out.println("Voltando ao menu...");
            return;
        }
        if (biblioteca.buscarUsuarioPorMatricula(matricula) == null) {
            System.out.println("❌ Usuário não encontrado. Operação cancelada.");
            return;
        }
        System.out.print("Confirmar devolução? (S/N): ");
        String resp = sc.nextLine().trim().toUpperCase();
        if (!"S".equals(resp)) {
            System.out.println("Operação cancelada.");
            return;
        }
        biblioteca.devolverLivro(codigo, matricula);
    }

    //============================================================
    // MENU — Consulta
    //============================================================
    private static void menuConsultas(Scanner sc, Biblioteca biblioteca) {
        int opcao;
        do {
            System.out.println("\n--- CONSULTAS ---");
            System.out.println("1 - Por Categoria");
            System.out.println("2 - Por Autor");
            System.out.println("3 - Por Status (disponíveis/indisponíveis)");
            System.out.println("4 - Livros emprestados por usuário");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");
            opcao = lerInteiro(sc);
            switch (opcao) {
                case 1 -> {
                    System.out.print("Categoria: ");
                    String categoria = sc.nextLine();
                    biblioteca.getLivros().stream()
                            .filter(l -> l.getCategoria().equalsIgnoreCase(categoria))
                            .forEach(l -> System.out.printf("- %s (Código: %s) | Disponíveis: %d%n", l.getTitulo(), l.getCodigo(), l.getDisponiveis()));
                }
                case 2 -> {
                    System.out.print("Autor: ");
                    String autor = sc.nextLine();
                    biblioteca.getLivros().stream()
                            .filter(l -> l.getAutor().equalsIgnoreCase(autor))
                            .forEach(l -> System.out.printf("- %s (Código: %s) | Disponíveis: %d%n", l.getTitulo(), l.getCodigo(), l.getDisponiveis()));
                }
                case 3 -> {
                    System.out.println("Mostrar (1) disponíveis ou (2) indisponíveis: ");
                    int opc = lerInteiro(sc);

                    if (opc == 1) {
                        System.out.println("\n--- LIVROS DISPONÍVEIS ---");
                        boolean achou = false;

                        for (Livro l : biblioteca.getLivros()) {
                            if (l.isDisponivel()) {
                                System.out.println(l);
                                achou = true;
                            }
                        }

                        if (!achou) {
                            System.out.println("❌ Não há livros disponíveis no momento.");
                        }

                    } else if (opc == 2) {
                        System.out.println("\n--- LIVROS INDISPONÍVEIS ---");
                        boolean achou = false;

                        for (Livro l : biblioteca.getLivros()) {
                            if (!l.isDisponivel()) {
                                System.out.println(l);
                                achou = true;
                            }
                        }

                        if (!achou) {
                            System.out.println("❌ Não há nenhum livro indisponível no momento.");
                        }

                    } else {
                        System.out.println("Opção inválida!");
                    }

                    break;
                }
                case 4 -> {
                    System.out.print("Digite a matrícula do usuário: ");
                    String mat = sc.nextLine().trim().toUpperCase();

                    List<Emprestimo> lista = biblioteca.listarLivrosEmprestadosPorUsuario(mat);

                    if (lista.isEmpty()) {
                        System.out.println("📭 Este usuário não possui livros emprestados.");
                    } else {
                        System.out.println("\n--- LIVROS EMPRESTADOS ---");
                        for (Emprestimo e : lista) {
                            System.out.println(e);
                            System.out.println("----------------------------------");
                        }
                    }
                }
            }
        } while (opcao != 0);
    }

    private static int lerInteiro(Scanner sc) {
        while (true) {
            try {
                String linha = sc.nextLine();
                return Integer.parseInt(linha.trim());
            } catch (Exception e) {
                System.out.print("Digite um número válido: ");
            }
        }
    }
}
