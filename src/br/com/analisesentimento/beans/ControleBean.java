package br.com.analisesentimento.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class ControleBean {

  public ControleBean () {
  }

  public String classificarTweets () {
    return "classificacao";
  }

  public String index () {
    return "index";
  }

}
