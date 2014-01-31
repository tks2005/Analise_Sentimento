package br.com.analisesentimento.classes;

public class Dia {

  private double positivos;
  private double negativos;
  private double neutros;

  public Dia (double positivos, double negativos, double neutros) {
    this.positivos = positivos;
    this.negativos = negativos;
    this.neutros = neutros;
  }

  public Dia () {

  }

  public double getTotal () {
    return positivos + negativos + neutros;
  }

  /**
   * @return the positivos
   */
  public double getPositivos () {
    return positivos;
  }

  /**
   * @param positivos
   *          the positivos to set
   */
  public void setPositivos (int positivos) {
    this.positivos = positivos;
  }

  /**
   * @return the negativos
   */
  public double getNegativos () {
    return negativos;
  }

  /**
   * @param negativos
   *          the negativos to set
   */
  public void setNegativos (int negativos) {
    this.negativos = negativos;
  }

  /**
   * @return the neutros
   */
  public double getNeutros () {
    return neutros;
  }

  /**
   * @param neutros
   *          the neutros to set
   */
  public void setNeutros (int neutros) {
    this.neutros = neutros;
  }

}
