package br.com.analisesentimento.classificador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.analisesentimento.utils.LeitorArquivos;

public class Classificador {

  public List<String> vocabularioPositivo;
  public List<String> vocabularioNegativo;

  public double positivas;
  public double negativas;
  public double neutras;
  public double acertos;
  public double erros;

  public double verdadeiroPositivo;
  public double falsoPositivo;
  public double verdadeiroNegativo;
  public double falsoNegativo;
  public double verdadeiroNeutro;
  public double falsoNeutro;

  private double pPositivo;
  private double pNegativo;
  private double pNeutro;

  private List<String> vocabulario;
  private List<String> emoticonsPositivos;
  private List<String> emoticonsNegativos;

  HashMap<String, Double> matrizProbabilidade;

  public Classificador () {
    vocabulario = new ArrayList<String>();
    matrizProbabilidade = new HashMap<String, Double>();
  }

  public String[] getSentencaAnalisadaBayes (String sentenca) {
    String[] st = new String[2];

    int polaridade = classificaBayesiano(sentenca);

    if (polaridade == 1) {
      st[0] = sentenca;
      st[1] = "positivo";
      positivas++;
    }
    else if (polaridade == 0) {
      st[0] = sentenca;
      st[1] = "negativo";
      negativas++;
    }
    else {
      st[0] = sentenca;
      st[1] = "neutro";
      neutras++;
    }

    return st;
  }

  public String[] getSentencaAnalisadaBayesPonderada (String[] sentenca) {

    int polaridade = classificaBayesiano(sentenca[0]);

    if (polaridade == 1) {
      sentenca[2] = "positivo";
      positivas++;
      if (sentenca[1] == sentenca[2]) {
        verdadeiroPositivo++;
        acertos++;
      }
      else {
        erros++;
        falsoPositivo++;
      }
    }
    else if (polaridade == 0) {
      sentenca[2] = "negativo";
      negativas++;
      if (sentenca[1] == sentenca[2]) {
        verdadeiroNegativo++;
        acertos++;
      }
      else {
        erros++;
        falsoNegativo++;
      }
    }
    else {
      sentenca[2] = "neutro";
      neutras++;
      if (sentenca[1] == sentenca[2]) {
        verdadeiroNeutro++;
        acertos++;
      }
      else {
        erros++;
        falsoNeutro++;
      }
    }

    return sentenca;
  }

  public int classificaBayesiano (String tweet) {
    List<String> palavras = new ArrayList<String>();
    String[] sp = tweet.split(" ");
    for (int i = 0; i < sp.length; i++) {
      String temp = sp[i];
      if (vocabulario.contains(temp))
        palavras.add(temp);
    }

    double phPositivo = pPositivo;
    for (String st : palavras) {
      String chave = st + "_1";
      double temp = matrizProbabilidade.get(chave);
      phPositivo = phPositivo * temp;
    }

    double phNegativo = pNegativo;
    for (String st : palavras) {
      String chave = st + "_0";
      double temp = matrizProbabilidade.get(chave);
      phNegativo = phNegativo * temp;
    }

    double phNeutro = pNeutro;
    for (String st : palavras) {
      String chave = st + "_2";
      double temp = matrizProbabilidade.get(chave);
      phNeutro = phNeutro * temp;
    }

    if (phPositivo > phNegativo && phPositivo > phNeutro)
      return 1;

    if (phNegativo > phPositivo && phNegativo > phNeutro)
      return 0;

    else
      return 2;
  }

  public void treinaPonderado () {
    List<String> positivas = LeitorArquivos.getListaPositivasTreinamento();
    List<String> negativas = LeitorArquivos.getListaNegativasTreinamento();
    List<String> neutras = LeitorArquivos.getListaNeutrasTreinamento();
    List<String[]> positivasPonderadas = getListaPonderada(positivas, "positivo");
    List<String[]> negativasPonderadas = getListaPonderada(negativas, "negativo");
    List<String[]> neutrasPonderadas = getListaPonderada(neutras, "neutro");
    List<String[]> aux = new ArrayList<String[]>();
    aux.addAll(positivasPonderadas);
    aux.addAll(negativasPonderadas);
    aux.addAll(neutrasPonderadas);
    treinamentoBayesianoPonderado(aux);
  }

  public void treinamentoBayesiano (List<String> positivas, List<String> negativas, List<String> neutras) {
    List<String> temp = new ArrayList<String>();
    temp.addAll(positivas);
    temp.addAll(negativas);
    temp.addAll(neutras);

    montaVocabulario(temp);

    for (int i = 0; i < 2; i++) {
      List<String> hipotese;
      if (i == 1) {
        hipotese = positivas;
        double val1 = hipotese.size();
        double val2 = temp.size();
        pPositivo = val1 / val2;
      }
      else if (i == 0) {
        hipotese = negativas;
        double val1 = hipotese.size();
        double val2 = temp.size();
        pNegativo = val1 / val2;
      }
      else {
        hipotese = neutras;
        double val1 = hipotese.size();
        double val2 = temp.size();
        pNeutro = val1 / val2;
      }

      HashMap<String, Integer> mapaPalavras = new HashMap<String, Integer>();
      List<String> texto = new ArrayList<String>();

      for (String st : hipotese) {
        String[] sp = st.split(" ");
        for (int j = 0; j < sp.length; j++) {
          String t = sp[j];
          texto.add(t);
          if (mapaPalavras.containsKey(t)) {
            int qtde = mapaPalavras.get(t);
            qtde++;
            mapaPalavras.put(t, qtde);
          }
          else {
            mapaPalavras.put(t, 1);
          }
        }
      }

      int n = texto.size();

      for (String st : vocabulario) {
        int nk;

        if (texto.contains(st))
          nk = mapaPalavras.get(st);
        else
          nk = 0;

        String chave = st + "_" + i;
        double prob = calculaPwh(nk, n);
        matrizProbabilidade.put(chave, prob);
      }
    }
  }

  public void treinamentoBayesianoPonderado (List<String[]> treinamento) {
    int qtdePositivas = 0;
    int qtdeNegativas = 0;
    int qtdeNeutras = 0;

    List<String> positivas = new ArrayList<String>();
    List<String> negativas = new ArrayList<String>();
    List<String> neutras = new ArrayList<String>();

    for (String[] st : treinamento) {
      if (st[1] == "positivo") {
        qtdePositivas++;
        positivas.add(st[0]);
      }
      if (st[1] == "negativo") {
        qtdeNegativas++;
        negativas.add(st[0]);
      }
      if (st[1] == "neutro") {
        qtdeNeutras++;
        neutras.add(st[0]);
      }
    }

    List<String> temp = new ArrayList<String>();
    temp.addAll(positivas);
    temp.addAll(negativas);
    temp.addAll(neutras);

    montaVocabulario(temp);

    double tam = temp.size();

    pPositivo = qtdePositivas / tam;
    pNegativo = qtdeNegativas / tam;
    pNeutro = qtdeNeutras / tam;

    for (int i = 0; i <= 2; i++) {
      List<String> hipotese;
      if (i == 1) {
        hipotese = positivas;
      }
      else if (i == 0) {
        hipotese = negativas;
      }
      else {
        hipotese = neutras;
      }

      HashMap<String, Integer> mapaPalavras = new HashMap<String, Integer>();
      List<String> texto = new ArrayList<String>();

      for (String st : hipotese) {
        String[] sp = st.split(" ");
        for (int j = 0; j < sp.length; j++) {
          String t = sp[j];
          texto.add(t);
          if (mapaPalavras.containsKey(t)) {
            int qtde = mapaPalavras.get(t);
            qtde++;
            mapaPalavras.put(t, qtde);
          }
          else {
            mapaPalavras.put(t, 1);
          }
        }
      }

      int n = texto.size();

      for (String st : vocabulario) {
        int nk;

        if (texto.contains(st))
          nk = mapaPalavras.get(st);
        else
          nk = 0;

        String chave = st + "_" + i;
        double prob = calculaPwh(nk, n);
        matrizProbabilidade.put(chave, prob);
      }
    }
  }

  public double calculaPwh (int vezesPalavras, int totalPalavras) {
    double pwh = vezesPalavras;
    pwh = pwh++;

    double denominador = totalPalavras;
    denominador = denominador + vocabulario.size();

    pwh = pwh / denominador;

    return pwh;
  }

  public void montaVocabulario (List<String> palavras) {
    for (String st : palavras) {
      String[] sp = st.split(" ");
      for (int i = 0; i < sp.length; i++) {
        String temp = sp[i];
        if (!vocabulario.contains(temp))
          vocabulario.add(temp);
      }
    }
  }

  public List<String[]> getListaPonderada (List<String> lista, String tipo) {
    List<String[]> ponderada = new ArrayList<String[]>();

    for (String s : lista) {
      String[] temp = new String[3];
      temp[0] = s;
      temp[1] = tipo;
      ponderada.add(temp);
    }

    return ponderada;
  }

  public String[] getSentencaAnalisadaScore (String sentenca) {
    String[] st = new String[2];

    String[] split = sentenca.split(" ");

    int score = 0;

    for (int i = 0; i < split.length; i++) {
      String temp = split[i];
      if (vocabularioNegativo.contains(temp))
        score--;
      else if (vocabularioPositivo.contains(temp))
        score++;
    }

    if (score > 0) {
      st[0] = sentenca;
      st[1] = "positivo";
      positivas++;
    }
    else if (score < 0) {
      st[0] = sentenca;
      st[1] = "negativo";
      negativas++;
    }
    else {
      st[0] = sentenca;
      st[1] = "neutro";
      neutras++;
    }

    return st;
  }

  public List<String[]> classificaTweetsScore (List<String> tweets) {
    List<String[]> tweetsClassificados = new ArrayList<String[]>();

    vocabularioPositivo = LeitorArquivos.getPositivas();
    vocabularioNegativo = LeitorArquivos.getNegativas();
    positivas = 0;
    negativas = 0;
    neutras = 0;

    for (String tw : tweets) {
      String[] analisada = getSentencaAnalisadaScore(tw);
      tweetsClassificados.add(analisada);
    }

    return tweetsClassificados;
  }

  public List<String[]> classificaTweetsBayes (List<String> tweets) {
    List<String[]> tweetsClassificados = new ArrayList<String[]>();

    for (String tw : tweets) {
      String[] analisada = getSentencaAnalisadaBayes(tw);
      tweetsClassificados.add(analisada);
    }

    return tweetsClassificados;
  }

  public List<String[]> classificaTweetsPonderadoBayes (List<String[]> tweets) {
    List<String[]> tweetsClassificados = new ArrayList<String[]>();

    for (String tw[] : tweets) {
      String[] analisada = getSentencaAnalisadaBayesPonderada(tw);
      tweetsClassificados.add(analisada);
    }

    return tweetsClassificados;
  }

  public void zera () {
    positivas = 0;
    negativas = 0;
    neutras = 0;

  }

}
