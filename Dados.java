/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dados;

import controle.Biblioteca;

/**
 *
 * @author glads
 */
public class Dados {

    /* Carrega dados iniciais apenas se o sistema estiver vazio.*/
    public static void carregarDadosIniciais(Biblioteca biblioteca) {

        // Se já existe algum livro, assume que já carregou os CSV
        if (!biblioteca.getLivros().isEmpty()) {
            return;
        }

        // Livros iniciais com quantidade
        biblioteca.cadastrarLivro("Dom Casmurro", "Machado de Assis", "Romance", "L001", 5);
        biblioteca.cadastrarLivro("O Pequeno Príncipe", "Antoine de Saint-Exupéry", "Infantil", "L002", 4);
        biblioteca.cadastrarLivro("1984", "George Orwell", "Ficção", "L003", 6);
        biblioteca.cadastrarLivro("Clean Code", "Robert C. Martin", "Programação", "L004", 3);
        biblioteca.cadastrarLivro("Dom Quixote", "Miguel de Cervantes", "Sátira", "L005", 2);
        biblioteca.cadastrarLivro("O Senhor dos Anéis", "J. R. R. Tolkien", "Fantasia", "L006", 10);

        // Usuários iniciais
        biblioteca.cadastrarUsuario("Ana Silva", "U001", "ana@gmail.com", "99999-1111");
        biblioteca.cadastrarUsuario("Carlos Souza", "U002", "carlos@gmail.com", "99999-2222");
        biblioteca.cadastrarUsuario("Fernanda Lima", "U003", "fernanda@gmail.com", "99999-3333");

        // Todos dados iniciais serão salvos automaticamente pelos
        // próprios métodos cadastrarLivro / cadastrarUsuario.
    }
}
