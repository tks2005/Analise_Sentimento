package br.com.analisesentimento.beans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import twitter4j.Status;
import twitter4j.TwitterException;
import br.com.analisesentimento.classes.Tweet;
import br.com.analisesentimento.classificador.Classificador;
import br.com.analisesentimento.twitterUtils.ConverteStatusParaTweet;
import br.com.analisesentimento.twitterUtils.FerramentaTwitter;
import br.com.analisesentimento.utils.LimpadorTexto;

@ManagedBean(name = "classificacaoBean")
@SessionScoped
public class ClassificacaoBean {

  private Classificador classificador;
  private String termoBusca;
  private List<Tweet> tweets;
  private List<Tweet> tweetsSelecionados;
  private FerramentaTwitter twitter;
  private int classificacao;

  public ClassificacaoBean () {
    classificador = new Classificador();
    classificador.treinaPonderado();
    tweets = new ArrayList<Tweet>();
    twitter = new FerramentaTwitter();
  }

  public String classificarTweets () {

    for (Tweet tweet : tweetsSelecionados) {
      tweet.setClassificacao(classificacao);
    }

    treinaClassificador(tweetsSelecionados);
    tweetsSelecionados.clear();

    return "classificacao";
  }

  public void treinaClassificador (List<Tweet> tweetsTreinamento) {
    List<String> tws = LimpadorTexto.ajustaTweetsTreinamento(tweetsTreinamento);
    File arquivo;

    if (classificacao == 1) {
      arquivo = new File("C:/Users/tiago.santana/workspace/Analise_Sentimento/WebContent/teste_positivas.txt");
    }
    else if (classificacao == 0) {
      arquivo = new File("C:/Users/tiago.santana/workspace/Analise_Sentimento/WebContent/teste_negativas.txt");
    }
    else {
      arquivo = new File("C:/Users/tiago.santana/workspace/Analise_Sentimento/WebContent/teste_neutras.txt");
    }

    try {
      FileWriter fos = new FileWriter(arquivo, true);

      for (String tweet : tws) {
        fos.write("\r\n" + tweet);
      }
      fos.close();
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String buscarEClassificar () {
    List<Status> tempTweets = new ArrayList<Status>();
    tweets = new ArrayList<Tweet>();
    try {
      tempTweets = twitter.getTweets(termoBusca);
      for (Status status : tempTweets) {
        Tweet tweet = ConverteStatusParaTweet.converte(status);
        tweet.setClassificacao(classificador.classificaBayesiano(tweet.getTweet()));
        tweets.add(tweet);
      }

    }
    catch (TwitterException e) {
      e.printStackTrace();
    }
    return "classificacao";
  }

  public String voltar () {
    zeraComponentes();
    return "index";
  }

  private void zeraComponentes () {
    termoBusca = null;
    tweets.clear();
    tweetsSelecionados.clear();
    ;
  }

  public boolean getRenderizaComponentes () {
    if (tweets.size() > 0)
      return true;
    else
      return false;
  }

  public Classificador getClassificador () {
    return classificador;
  }

  public void setClassificador (Classificador classificador) {
    this.classificador = classificador;
  }

  /**
   * @return the termoBusca
   */
  public String getTermoBusca () {
    return termoBusca;
  }

  /**
   * @param termoBusca
   *          the termoBusca to set
   */
  public void setTermoBusca (String termoBusca) {
    this.termoBusca = termoBusca;
  }

  public List<Tweet> getTweets () {
    return tweets;
  }

  public void setTweets (List<Tweet> tweets) {
    this.tweets = tweets;
  }

  public List<Tweet> getTweetsSelecionados () {
    return tweetsSelecionados;
  }

  public void setTweetsSelecionados (List<Tweet> tweetsSelecionados) {
    this.tweetsSelecionados = tweetsSelecionados;
  }

  public int getClassificacao () {
    return classificacao;
  }

  public void setClassificacao (int classificacao) {
    this.classificacao = classificacao;
  }

}
