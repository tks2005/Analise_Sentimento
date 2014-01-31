package br.com.analisesentimento.classes;

import java.util.Calendar;

public class Tweet {

  /*
   * Data criação (data de criação) Favoritos (quantas vezes foi marcado como favorito Id (id do tweet) Retweets (quantas vezes
   * foi retweetado) Tweet (texto do tweet) ehRetweet (boolean se é retweet) idUser (id do user) nomeUser (nome do user)
   */

  private Long id;
  private Calendar dataCriacao;
  private Integer favoritos;
  private Integer retweets;
  private String tweet;
  private Boolean ehRetweet;
  private Long idUser;
  private String nomeUser;
  private int classificacao;
  
  public String getFormatacao(){
    if (classificacao == 1)
      return "celulaPositiva";
    else if (classificacao == 0)
      return "celulaNegativa";
    else
      return null;
    
  }

  public Long getId () {
    return id;
  }

  public void setId (Long id) {
    this.id = id;
  }

  public Calendar getDataCriacao () {
    return dataCriacao;
  }

  public void setDataCriacao (Calendar dataCriacao) {
    this.dataCriacao = dataCriacao;
  }

  public Integer getFavoritos () {
    return favoritos;
  }

  public void setFavoritos (Integer favoritos) {
    this.favoritos = favoritos;
  }

  public Integer getRetweets () {
    return retweets;
  }

  public void setRetweets (Integer retweets) {
    this.retweets = retweets;
  }

  public String getTweet () {
    return tweet;
  }

  public void setTweet (String tweet) {
    this.tweet = tweet;
  }

  public Boolean getEhRetweet () {
    return ehRetweet;
  }

  public void setEhRetweet (Boolean ehRetweet) {
    this.ehRetweet = ehRetweet;
  }

  public Long getIdUser () {
    if (idUser != null)
      return idUser;
    else
      return new Long(0);
  }

  public void setIdUser (Long idUser) {
    this.idUser = idUser;
  }

  public String getNomeUser () {
    return "@" + nomeUser;
  }

  public void setNomeUser (String nomeUser) {
    this.nomeUser = nomeUser;
  }

  public int getClassificacao () {
    return classificacao;
  }

  public void setClassificacao (int classificacao) {
    this.classificacao = classificacao;
  }

}
