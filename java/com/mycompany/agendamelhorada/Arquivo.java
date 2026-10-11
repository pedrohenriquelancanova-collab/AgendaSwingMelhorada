package com.mycompany.agendamelhorada;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JOptionPane;

public class Arquivo {

    private BufferedWriter bw;
    private BufferedReader br;
    private String nome = "contatos.txt";

    public Arquivo(String nome) {
        this.nome = nome;
    }

    public Arquivo() {

    }

    public void gravar(String texto) {
        try {
            bw = new BufferedWriter(new FileWriter(this.nome));

            bw.write(texto);
            bw.newLine();

            bw.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    public void gravar(Contato contato) {
        try {
            bw = new BufferedWriter(
                    new FileWriter(this.nome, true));

            bw.write(contato.getNome() + ";"
                    + contato.getEmail() + ";"
                    + contato.getTelefone());

            bw.newLine();
            bw.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao salvar contato: " + e.getMessage());
        }
    }

    public String ler() {
        StringBuilder conteudo = new StringBuilder();
        String linha;

        try {
            br = new BufferedReader(new FileReader(this.nome));

            while ((linha = br.readLine()) != null) {
                conteudo.append(linha).append("\n");
            }

            br.close();

            return conteudo.toString();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao ler arquivo: " + e.getMessage());

            return "#!ERRO!#";
        }
    }

    public Contato buscarPorNome(String nomeBuscado) {
        try {
            br = new BufferedReader(new FileReader(this.nome));

            String linha;

            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");

                if (dados.length >= 3
                        && dados[0].equalsIgnoreCase(nomeBuscado)) {

                    br.close();

                    return new Contato(
                            dados[0],
                            dados[1],
                            dados[2]
                    );
                }
            }

            br.close();
            return null;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao buscar contato: " + e.getMessage());

            return null;
        }
    }

    public boolean remover(String nomeBuscado) {
        java.io.File arquivoOriginal = new java.io.File(this.nome);
        java.io.File arquivoTemp = new java.io.File("temp_" + this.nome);
        boolean encontrado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivoOriginal)); BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoTemp))) {

            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");

                if (dados.length > 0 && dados[0].equalsIgnoreCase(nomeBuscado)) {
                    encontrado = true;
                    continue;
                }

                writer.write(linha);
                writer.newLine();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao remover contato: " + e.getMessage());
            return false;
        }

        if (encontrado) {
            if (arquivoOriginal.delete()) {
                arquivoTemp.renameTo(arquivoOriginal);
            }
        } else {
            arquivoTemp.delete();
        }

        return encontrado;
    }

    public boolean alterar(String nomeBuscado, Contato contatoAtualizado) {
        java.io.File arquivoOriginal = new java.io.File(this.nome);
        java.io.File arquivoTemp = new java.io.File("temp_" + this.nome);
        boolean encontrado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivoOriginal)); BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoTemp))) {

            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");

                if (dados.length > 0 && dados[0].equalsIgnoreCase(nomeBuscado)) {
                    writer.write(contatoAtualizado.getNome() + ";"
                            + contatoAtualizado.getEmail() + ";"
                            + contatoAtualizado.getTelefone());
                    encontrado = true;
                } else {
                    writer.write(linha);
                }
                writer.newLine();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao alterar contato: " + e.getMessage());
            return false;
        }

        if (encontrado) {
            if (arquivoOriginal.delete()) {
                arquivoTemp.renameTo(arquivoOriginal);
            }
        } else {
            arquivoTemp.delete();
        }

        return encontrado;
    }
}
