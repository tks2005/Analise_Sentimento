package br.com.analisesentimento.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.analisesentimento.DAO.TweetDAO;
import br.com.analisesentimento.classes.Dia;
import br.com.analisesentimento.classes.Tweet;
import br.com.analisesentimento.classificador.Classificador;

@ManagedBean(name = "gestaoBean")
@SessionScoped
public class GestaoBean {

  private Date dataInicio;
  private Date dataFim;
  private List<Tweet> tweets;
  private int risco;
  private List<Dia> dias;
  private Classificador classificador;

  public GestaoBean () {
    setTweets(new ArrayList<Tweet>());
    classificador = new Classificador();
    classificador.treinaPonderado();
  }

  public void geraDias () {
    
  }

  public boolean getRenderizaComponentes () {
    if (tweets.size() > 0)
      return true;
    else
      return false;
  }

  public String buscar () {
    TweetDAO dao = new TweetDAO();
    tweets = dao.getIntervaloData(risco, new java.sql.Date(dataInicio.getTime()), new java.sql.Date(dataFim.getTime()));
    for (Tweet tweet : tweets) {
      tweet.setClassificacao(classificador.classificaBayesiano(tweet.getTweet()));
    }

    return "gestao";
  }

  public String voltar () {
    zeraComponentes();
    return "index";
  }

  private void zeraComponentes () {
    dataFim = null;
    dataInicio = null;
    tweets.clear();
  }

  public Date getDataInicio () {
    return dataInicio;
  }

  public void setDataInicio (Date dataInicio) {
    this.dataInicio = dataInicio;
  }

  public Date getDataFim () {
    return dataFim;
  }

  public void setDataFim (Date dataFim) {
    this.dataFim = dataFim;
  }

  public List<Tweet> getTweets () {
    return tweets;
  }

  public void setTweets (List<Tweet> tweets) {
    this.tweets = tweets;
  }

  public int getRisco () {
    return risco;
  }

  public void setRisco (int risco) {
    this.risco = risco;
  }

  /**
   * @return the dias
   */
  public List<Dia> getDias () {
    return dias;
  }

  /**
   * @param dias
   *          the dias to set
   */
  public void setDias (List<Dia> dias) {
    this.dias = dias;
  }

  /**
   * @return the classificador
   */
  public Classificador getClassificador () {
    return classificador;
  }

  /**
   * @param classificador
   *          the classificador to set
   */
  public void setClassificador (Classificador classificador) {
    this.classificador = classificador;
  }

}
