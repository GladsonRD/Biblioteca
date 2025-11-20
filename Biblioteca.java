/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controle;

import modelo.Livro;
import modelo.Usuario;
import modelo.Emprestimo;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author glads
 */
public class Biblioteca {

    private List<Livro> livros = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Emprestimo> emprestimos = new ArrayList<>();

    private final String ARQ_LIVROS = "livros.csv";
    private final String ARQ_USUARIOS = "usuarios.csv";
    private final String ARQ_EMPRESTIMOS = "emprestimos.csv";

    public Biblioteca() {
        carregarDados();
    }

    public void carregarDados() {
        carregarLivros();
        carregarUsuarios();
        carregarEmprestimos();
    }

    public void salvarDados() {
        salvarLivros();
        salvarUsuarios();
        salvarEmprestimos();
    }

    // ===================== LIVROS =====================
    // cadastra livro pedindo quantidade
    public void cadastrarLivro(String titulo, String autor, String categoria, String codigo, int quantidade) {
        // impede códigos repetidos
        if (codigo == null || codigo.trim().isEmpty()) {
            System.out.println("❌ Código inválido.");
            return;
        }
        codigo = codigo.trim().toUpperCase();
        for (Livro l : livros) {
            if (l.getCodigo() != null && l.getCodigo().trim().equalsIgnoreCase(codigo)) {
                System.out.println("❌ Já existe um livro com esse código.");
                return;
            }
        }

        Livro novo = new Livro(titulo, autor, categoria, codigo, Math.max(1, quantidade));
        novo.setId(livros.size() + 1);
        livros.add(novo);
        salvarLivros();
        System.out.println("✅ Livro cadastrado com sucesso!");
    }

    public Livro buscarLivroPorCodigo(String codigo) {
        if (codigo == null) {
            return null;
        }
        codigo = codigo.trim().toUpperCase();
        for (Livro l : livros) {
            if (l.getCodigo() != null && l.getCodigo().trim().equalsIgnoreCase(codigo)) {
                return l;
            }
        }
        return null;
    }

    public Livro buscarLivroPorID(int id) {
        return livros.stream().filter(l -> l.getId() == id).findFirst().orElse(null);
    }

    private void carregarLivros() {
        livros.clear();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ARQ_LIVROS), "UTF-8"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // ignorar possível BOM na primeira linha
                if (linha.length() > 0 && linha.charAt(0) == '\uFEFF') {
                    linha = linha.substring(1);
                }
                Livro l = Livro.fromCSV(linha);
                if (l != null) {
                    livros.add(l);
                }
            }
        } catch (IOException e) {
            // arquivo pode não existir inicialmente
        }
    }

    private void salvarLivros() {
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(ARQ_LIVROS), "UTF-8")) {
            // escrever BOM para compatibilidade com Excel
            osw.write('\uFEFF');
            for (Livro l : livros) {
                osw.write(l.toCSV());
                osw.write("\n");
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar livros: " + e.getMessage());
        }
    }

    public void listarLivrosDisponiveis() {
        System.out.println("\n📚 Livros disponíveis para empréstimo:");
        // ordenar por código
        List<Livro> dispon = livros.stream()
                .filter(Livro::isDisponivel)
                .sorted(Comparator.comparing(l -> l.getCodigo().toUpperCase()))
                .collect(Collectors.toList());

        if (dispon.isEmpty()) {
            System.out.println("Nenhum livro disponível no momento.");
            return;
        }
        int idx = 1;
        for (Livro l : dispon) {
            System.out.printf("%d. %s (Código: %s) - %d disponíveis%n", idx++, l.getTitulo(), l.getCodigo(), l.getDisponiveis());
        }
    }

    public void listarTodosLivrosOrdenadosPorCodigo() {
        List<Livro> sorted = livros.stream()
                .sorted(Comparator.comparing(l -> l.getCodigo().toUpperCase()))
                .collect(Collectors.toList());
        System.out.println("\n📚 Todos os livros (ordenados por código):");
        for (Livro l : sorted) {
            System.out.printf("- %s (Código: %s) | Categoria: %s | Qtd: %d | Disponíveis: %d%n",
                    l.getTitulo(), l.getCodigo(), l.getCategoria(), l.getQuantidade(), l.getDisponiveis());
        }
    }

    private boolean livroTemCodigoExistente(String codigo) {
        return buscarLivroPorCodigo(codigo) != null;
    }

    // ===================== USUÁRIOS =====================
    public void cadastrarUsuario(String nome, String matricula, String email, String telefone) {
        // //  Cadastra um novo usuário no sistema, verifica se matrícula já existe.
        if (matricula == null || matricula.trim().isEmpty()) {
            System.out.println("❌ Matrícula inválida.");
            return;
        }
        matricula = matricula.trim().toUpperCase();
        for (Usuario u : usuarios) {
            if (u.getMatricula() != null && u.getMatricula().trim().equalsIgnoreCase(matricula)) {
                System.out.println("❌ Matrícula já cadastrada.");
                return;
            }
        }
        Usuario novo = new Usuario(nome, matricula, email, telefone);
        novo.setId(usuarios.size() + 1);
        usuarios.add(novo);
        salvarUsuarios();
        System.out.println("✅ Usuário cadastrado com sucesso!");
    }

    public Usuario buscarUsuarioPorMatricula(String matricula) {
        if (matricula == null) {
            return null;
        }
        matricula = matricula.trim().toUpperCase();
        for (Usuario u : usuarios) {
            if (u.getMatricula() != null && u.getMatricula().trim().equalsIgnoreCase(matricula)) {
                return u;
            }
        }
        return null;
    }

    private void carregarUsuarios() {
        usuarios.clear();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ARQ_USUARIOS), "UTF-8"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.length() > 0 && linha.charAt(0) == '\uFEFF') {
                    linha = linha.substring(1);
                }
                Usuario u = Usuario.fromCSV(linha);
                if (u != null) {
                    usuarios.add(u);
                }
            }
        } catch (IOException e) {
            // ignora
        }
    }

    private void salvarUsuarios() {
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(ARQ_USUARIOS), "UTF-8")) {
            osw.write('\uFEFF');
            for (Usuario u : usuarios) {
                osw.write(u.toCSV());
                osw.write("\n");
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }

    public List<Emprestimo> listarLivrosEmprestadosPorUsuario(String matricula) {
        List<Emprestimo> lista = new ArrayList<>();

        if (matricula == null) {
            return lista;
        }

        matricula = matricula.trim().toUpperCase();

        for (Emprestimo e : emprestimos) {
            if (e.getUsuario() != null
                    && e.getUsuario().getMatricula().equalsIgnoreCase(matricula)
                    && e.emAberto()) {

                lista.add(e);
            }
        }

        return lista;
    }

    // ===================== EMPRÉSTIMOS =====================
    public void emprestarLivro(String codigo, String matricula) {
        if (codigo == null || matricula == null) {
            return;
        }
        codigo = codigo.trim().toUpperCase();
        matricula = matricula.trim().toUpperCase();

        Livro livro = buscarLivroPorCodigo(codigo);
        if (livro == null) {
            System.out.println("❌ Livro não encontrado.");
            return;
        }

        // exibe resumo do livro e quantidade disponível
        System.out.printf("Livro: %s (Código: %s) - Disponíveis: %d%n",
                livro.getTitulo(), livro.getCodigo(), livro.getDisponiveis());

        if (!livro.isDisponivel()) {
            System.out.println("❌ Não há cópias disponíveis.");
            return;
        }

        Usuario usuario = buscarUsuarioPorMatricula(matricula);
        if (usuario == null) {
            System.out.println("❌ Usuário não encontrado.");
            return;
        }

        // gerar confirmação no menu chamador (ou aqui se preferir)
        // efetiva o empréstimo: reduzir disponiveis, criar Emprestimo
        boolean emprestou = livro.emprestarCopia();
        if (!emprestou) {
            System.out.println("❌ Falha ao emprestar (sem cópias).");
            return;
        }

        Emprestimo emp = new Emprestimo(livro, usuario);
        emp.setId(emprestimos.size() + 1);
        emprestimos.add(emp);

        salvarLivros();
        salvarEmprestimos();

        System.out.println("✅ Empréstimo realizado com sucesso!");
        System.out.println("\n📅 Empréstimo registrado!");
        System.out.println("Data do empréstimo: " + emp.dataEmprestimoFmt());
        System.out.println("Data prevista de devolução: " + emp.dataPrevistaFmt());
        System.out.println(emp.situacaoPrazo());
    }

    public void devolverLivro(String codigo, String matricula) {
        if (codigo == null || matricula == null) {
            return;
        }
        codigo = codigo.trim().toUpperCase();
        matricula = matricula.trim().toUpperCase();

        Usuario usuario = buscarUsuarioPorMatricula(matricula);
        if (usuario == null) {
            System.out.println("❌ Usuário não encontrado.");
            return;
        }

        Livro livro = buscarLivroPorCodigo(codigo);
        if (livro == null) {
            System.out.println("❌ Livro não encontrado.");
            return;
        }

        for (Emprestimo e : emprestimos) {
            if (e.getLivro() != null && e.getLivro().getCodigo().equalsIgnoreCase(codigo)
                    && e.getUsuario() != null && e.getUsuario().getMatricula().equalsIgnoreCase(matricula)
                    && e.emAberto()) {

                // confirmação deve ser feita no menu chamador; aqui efetuamos a devolução
                e.registrarDevolucao(LocalDate.now());

                // atualiza cópia disponível
                livro.devolverCopia();

                salvarLivros();
                salvarEmprestimos();

                System.out.println("✅ Livro devolvido com sucesso!");
                double multa = e.getMulta();
                if (multa > 0.0) {
                    System.out.printf("⚠ Multa por atraso: R$ %.2f%n", multa);
                }
                return;
            }
        }

        System.out.println("❌ Este livro não está emprestado por este usuário.");
    }

    private void carregarEmprestimos() {
        emprestimos.clear();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ARQ_EMPRESTIMOS), "UTF-8"))) {
            String linha;
            while ((linha = br.readLine()) != null) {

                if (linha.length() > 0 && linha.charAt(0) == '\uFEFF') {
                    linha = linha.substring(1);
                }

                Emprestimo e = Emprestimo.fromCSV(linha);
                if (e != null) {

                    // Agora buscamos pelo código do livro e matrícula do usuário
                    Livro l = buscarLivroPorCodigo(e.getCodigoLivro());
                    Usuario u = buscarUsuarioPorMatricula(e.getMatriculaUsuario());

                    // Se encontrados, associamos aos objetos
                    if (l != null) {
                        e.setLivro(l);
                    }
                    if (u != null) {
                        e.setUsuario(u);
                    }

                    emprestimos.add(e);
                }
            }

        } catch (IOException ex) {
            // ignorar
        }
    }

    private void salvarEmprestimos() {
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(ARQ_EMPRESTIMOS), "UTF-8")) {
            osw.write('\uFEFF');
            for (Emprestimo e : emprestimos) {
                osw.write(e.toCSV());
                osw.write("\n");
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar empréstimos: " + e.getMessage());
        }
    }

    // ===================== HISTÓRICOS E LISTAGENS =====================
    public void listarUsuarios() {
        System.out.println("\n👥 Usuários cadastrados:");
        for (Usuario u : usuarios) {
            System.out.println(u);
        }
    }

    public void listarEmprestimos() {
        System.out.println("\n📘 Empréstimos registrados:");
        for (Emprestimo e : emprestimos) {
            System.out.println(e);
        }
    }

    // Getters (para menus e consultas)
    public List<Livro> getLivros() {
        return livros;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }
}
