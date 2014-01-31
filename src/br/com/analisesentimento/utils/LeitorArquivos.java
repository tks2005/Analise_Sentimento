package br.com.analisesentimento.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import twitter4j.Status;

public class LeitorArquivos {

  public static List<String> getDias (String arquivo) {
    List<String> positivas = new ArrayList<String>();
    String arq = arquivo + ".txt";

    File positivo = new File(arq);

    try {
      LimpadorTexto limp = new LimpadorTexto();
      Scanner sc = new Scanner(positivo);
      while (sc.hasNext()) {
        String palavra = sc.nextLine().toLowerCase();
        palavra = limp.retiraAcentuacao(palavra);
        palavra = limp.corrigeString(palavra);
        positivas.add(palavra);
      }
      sc.close();

    }
    catch (FileNotFoundException e) {
      System.out.println("ERRO AO LER O ARQUIVO");
      e.printStackTrace();
    }

    return positivas;
  }

  public static List<String> getLista (String arquivo) {
    List<String> positivas = new ArrayList<String>();

    File positivo = new File(arquivo);

    try {
      LimpadorTexto limp = new LimpadorTexto();
      Scanner sc = new Scanner(positivo);
      while (sc.hasNext()) {
        String palavra = sc.nextLine().toLowerCase();
        palavra = limp.retiraAcentuacao(palavra);
        palavra = limp.corrigeString(palavra);
        positivas.add(palavra);
      }
      sc.close();

    }
    catch (FileNotFoundException e) {
      System.out.println("ERRO AO LER O ARQUIVO");
      e.printStackTrace();
    }

    return positivas;
  }

  public static List<String> getListaPositivasTreinamento () {
    List<String> positivas = new ArrayList<String>();

    FacesContext context = FacesContext.getCurrentInstance();
    ServletContext serv = (ServletContext) context.getExternalContext().getContext();
    String temp = serv.getRealPath("teste_positivas.txt");

    File positivo = new File(temp);

    try {
      LimpadorTexto limp = new LimpadorTexto();
      Scanner sc = new Scanner(positivo);
      while (sc.hasNext()) {
        String palavra = sc.nextLine().toLowerCase();
        palavra = limp.retiraAcentuacao(palavra);
        palavra = limp.corrigeString(palavra);
        positivas.add(palavra);
      }
      sc.close();

    }
    catch (FileNotFoundException e) {
      System.out.println("ERRO AO LER O ARQUIVO");
      e.printStackTrace();
    }

    return positivas;
  }

  public static List<String> getListaNegativasTreinamento () {
    List<String> positivas = new ArrayList<String>();

    FacesContext context = FacesContext.getCurrentInstance();
    ServletContext serv = (ServletContext) context.getExternalContext().getContext();
    String temp = serv.getRealPath("teste_negativas.txt");

    File positivo = new File(temp);

    try {
      LimpadorTexto limp = new LimpadorTexto();
      Scanner sc = new Scanner(positivo);
      while (sc.hasNext()) {
        String palavra = sc.nextLine().toLowerCase();
        palavra = limp.retiraAcentuacao(palavra);
        palavra = limp.corrigeString(palavra);
        positivas.add(palavra);
      }
      sc.close();

    }
    catch (FileNotFoundException e) {
      System.out.println("ERRO AO LER O ARQUIVO");
      e.printStackTrace();
    }

    return positivas;
  }

  public static List<String> getListaNeutrasTreinamento () {
    List<String> positivas = new ArrayList<String>();

    FacesContext context = FacesContext.getCurrentInstance();
    ServletContext serv = (ServletContext) context.getExternalContext().getContext();
    String temp = serv.getRealPath("teste_neutras.txt");

    File positivo = new File(temp);

    try {
      LimpadorTexto limp = new LimpadorTexto();
      Scanner sc = new Scanner(positivo);
      while (sc.hasNext()) {
        String palavra = sc.nextLine().toLowerCase();
        palavra = limp.retiraAcentuacao(palavra);
        palavra = limp.corrigeString(palavra);
        positivas.add(palavra);
      }
      sc.close();

    }
    catch (FileNotFoundException e) {
      System.out.println("ERRO AO LER O ARQUIVO");
      e.printStackTrace();
    }

    return positivas;
  }

  public static List<String> getPositivas () {
    List<String> positivas = new ArrayList<String>();

    File positivo = new File("positivas.txt");

    try {
      LimpadorTexto limp = new LimpadorTexto();
      Scanner sc = new Scanner(positivo);
      while (sc.hasNext()) {
        String palavra = sc.nextLine().toLowerCase();
        palavra = limp.retiraAcentuacao(palavra);
        positivas.add(palavra);
      }
      sc.close();

    }
    catch (FileNotFoundException e) {
      System.out.println("ERRO AO LER O ARQUIVO");
      e.printStackTrace();
    }

    return positivas;
  }

  public static List<String> getNegativas () {
    List<String> negativas = new ArrayList<String>();

    File negativo = new File("negativas.txt");

    try {
      LimpadorTexto limp = new LimpadorTexto();
      Scanner sc = new Scanner(negativo);
      while (sc.hasNext()) {
        String palavra = sc.nextLine().toLowerCase();
        palavra = limp.retiraAcentuacao(palavra);
        negativas.add(palavra);
      }
      sc.close();

    }
    catch (FileNotFoundException e) {
      System.out.println("ERRO AO LER O ARQUIVO");
      e.printStackTrace();
    }

    return negativas;
  }

  @SuppressWarnings("unchecked")
  public static List<Status> getBase () {
    File base = new File("base.txt");
    List<Status> accessToken = new ArrayList<Status>();
    try {
      FileInputStream in = new FileInputStream(base);
      ObjectInputStream objIn = new ObjectInputStream(new BufferedInputStream(in));
      accessToken = (List<Status>) objIn.readObject();
      objIn.close();
    }
    catch (Exception e) {

    }
    return accessToken;
  }

  public static void salvarArquivo (List<Status> todosResultados) {
    File arquivo = new File("base.txt");
    try {
      FileOutputStream out = new FileOutputStream(arquivo);
      ObjectOutputStream objOut = new ObjectOutputStream(new BufferedOutputStream(out));
      objOut.writeObject(todosResultados);
      objOut.close();
    }
    catch (Exception e) {
    }

  }

}
