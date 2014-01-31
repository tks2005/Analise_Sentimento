package br.com.analisesentimento.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import br.com.analisesentimento.classes.Dia;

public class GeradorKRI {

  /*
   * 
   * 
   * GERADORES DOS GRAFICOS
   */

  public void calculaKRI (Dia dia) {
    CategoryDataset dataset = geraDatasetDia(dia);
    JFreeChart chart = geraBarChart(dataset);
    File img = new File("kriDia.jpg");
    try {
      ChartUtilities.saveChartAsJPEG(img, chart, 500, 500);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void calculaKRI (List<Dia> dias) {
    CategoryDataset dataset = geraDatasetDias(dias);
    JFreeChart chart = geraBarChart(dataset);
    File img = new File("kriDias.jpg");
    try {
      ChartUtilities.saveChartAsJPEG(img, chart, 500, 500);
    }
    catch (IOException e) {
      e.printStackTrace();
    }

  }

  public void calculaEvolucaoKRI (List<Dia> dias) {
    XYDataset dataset = geraDatasetEvolucao(dias);
    JFreeChart chart = geraSplineChart(dataset);
    File img = new File("kriEvolucao.jpg");
    try {
      ChartUtilities.saveChartAsJPEG(img, chart, 500, 500);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void calculaMediasMoveisPositivo (List<Dia> dias) {
    XYDataset dataset = geraDatasetMMPositivo(calculaMMEPositivo(dias));
    JFreeChart chart = geraSplineChart(dataset);
    File img = new File("kriMediasMoveisPositivo.jpg");
    try {
      ChartUtilities.saveChartAsJPEG(img, chart, 500, 500);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void calculaMediasMoveisNegativo (List<Dia> dias) {
    XYDataset dataset = geraDatasetMMNegativo(calculaMMENegativo(dias));
    JFreeChart chart = geraSplineChart(dataset);
    File img = new File("kriMediasMoveisNegativos.jpg");
    try {
      ChartUtilities.saveChartAsJPEG(img, chart, 500, 500);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void calculaMME (List<Dia> dias) {
    XYDataset dataset = geraDatasetMM(calculaMMEPositivo(dias), calculaMMENegativo(dias));
    JFreeChart chart = geraSplineChart(dataset);
    File img = new File("kriMME.jpg");
    try {
      ChartUtilities.saveChartAsJPEG(img, chart, 500, 500);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void calculaMMA (List<Dia> dias) {
    XYDataset dataset = geraDatasetMM(calculaMMAPositivo(dias), calculaMMANegativo(dias));
    JFreeChart chart = geraSplineChart(dataset);
    File img = new File("kriMMA.jpg");
    try {
      ChartUtilities.saveChartAsJPEG(img, chart, 500, 500);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*
   * 
   * 
   * METODOS AUXILIARES
   */

  public double somatoriaPositivo (List<Dia> dias, int parametro) {
    double somatoria = 0;

    for (int i = 0; i <= parametro; i++) {
      Dia dia = dias.get(i);
      double x = dia.getPositivos();
      double p = dia.getTotal();
      double tempP = (x / p) * 100;
      somatoria = somatoria + tempP;
    }

    return somatoria;
  }
  
  public double somatoriaNegativo (List<Dia> dias, int parametro) {
    double somatoria = 0;

    for (int i = 0; i <= parametro; i++) {
      Dia dia = dias.get(i);
      double x = dia.getNegativos();
      double p = dia.getTotal();
      double tempP = (x / p) * 100;
      somatoria = somatoria + tempP;
    }

    return somatoria;
  }

  public double[] calculaMMEPositivo (List<Dia> dias) {
    double[] vetorMedias = new double[dias.size()];

    for (int i = 0; i < vetorMedias.length; i++) {
      Dia dia = dias.get(i);
      double x = dia.getPositivos();
      double p = dia.getTotal();
      double tempP = (x / p) * 100;

      if (i > 0) {
        double tamanho = vetorMedias.length;
        double k = 2 / (tamanho + 1);
        double mediaMovel = (tempP * k)+ (vetorMedias[i - 1] * (1 - k));
        vetorMedias[i] = mediaMovel;
      }
      else
        vetorMedias[i] = tempP;
    }

    return vetorMedias;
  }

  public double[] calculaMMAPositivo (List<Dia> dias) {
    double[] vetorMedias = new double[dias.size()];

    for (int i = 0; i < vetorMedias.length; i++) {
      if (i > 0) {
        double tamanho = i + 1;
        double mediaMovel = somatoriaPositivo(dias, i) / tamanho;
        vetorMedias[i] = mediaMovel;
      }
      else {
        Dia dia = dias.get(i);
        double x = dia.getPositivos();
        double p = dia.getTotal();
        double tempP = (x / p) * 100;
        vetorMedias[i] = tempP;
      }

    }
    return vetorMedias;
  }

  public double[] calculaMMENegativo (List<Dia> dias) {
    double[] vetorMedias = new double[dias.size()];

    for (int i = 0; i < vetorMedias.length; i++) {
      Dia dia = dias.get(i);
      double x = dia.getNegativos();
      double p = dia.getTotal();
      double tempN = (x / p) * 100;

      if (i > 0) {
        double tamanho = vetorMedias.length;
        double k = 2 / (tamanho + 1);
        double mediaMovel = (tempN * k)+ (vetorMedias[i - 1] * (1 - k));
        vetorMedias[i] = mediaMovel;
      }
      else
        vetorMedias[i] = tempN;

    }
    return vetorMedias;
  }

  public double[] calculaMMANegativo (List<Dia> dias) {
    double[] vetorMedias = new double[dias.size()];

    for (int i = 0; i < vetorMedias.length; i++) {
      if (i > 0) {
        double tamanho = i + 1;
        double mediaMovel = somatoriaNegativo(dias, i) / tamanho;
        vetorMedias[i] = mediaMovel;
      }
      else {
        Dia dia = dias.get(i);
        double x = dia.getNegativos();
        double p = dia.getTotal();
        double tempP = (x / p) * 100;
        vetorMedias[i] = tempP;
      }

    }
    return vetorMedias;
  }

  /*
   * 
   * 
   * GERADORES DOS SETS PARA OS GRAFICOS
   */

  public CategoryDataset geraDatasetDia (Dia dia) {

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    String temp = "Dia";
    dataset.addValue(dia.getPositivos(), "Positivos", temp);
    dataset.addValue(dia.getNegativos(), "Negativos", temp);
    dataset.addValue(dia.getNeutros(), "Neutros", temp);

    return dataset;
  }

  public CategoryDataset geraDatasetDias (List<Dia> dias) {

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    int i = 1;
    for (Dia dia : dias) {
      String temp = "Dia" + i;
      dataset.addValue(dia.getPositivos(), "Positivos", temp);
      dataset.addValue(dia.getNegativos(), "Negativos", temp);
      dataset.addValue(dia.getNeutros(), "Neutros", temp);
      i++;
    }

    return dataset;
  }

  public XYDataset geraDatasetEvolucao (List<Dia> dias) {

    XYSeriesCollection dataset = new XYSeriesCollection();

    XYSeries seriePositiva = new XYSeries("Positivos");
    XYSeries serieNegativa = new XYSeries("Negativos");

    int i = 1;
    for (Dia dia : dias) {
      double x = dia.getPositivos();
      double y = dia.getNegativos();
      double p = dia.getTotal();
      double tempP = (x / p) * 100;
      double tempN = (y / p) * 100;
      serieNegativa.add(i, tempN);
      seriePositiva.add(i, tempP);
      i++;
    }

    dataset.addSeries(serieNegativa);
    dataset.addSeries(seriePositiva);

    return dataset;
  }

  public XYDataset geraDatasetMMPositivo (double[] dias) {

    XYSeriesCollection dataset = new XYSeriesCollection();

    XYSeries seriePositiva = new XYSeries("Positivos");

    for (int i = 0; i < dias.length; i++) {
      seriePositiva.add(i + 1, dias[i]);
      i++;
    }

    dataset.addSeries(seriePositiva);

    return dataset;
  }

  public XYDataset geraDatasetMMNegativo (double[] dias) {

    XYSeriesCollection dataset = new XYSeriesCollection();

    XYSeries serieNegativa = new XYSeries("Negativos");

    for (int i = 0; i < dias.length; i++) {
      serieNegativa.add(i + 1, dias[i]);
      i++;
    }

    dataset.addSeries(serieNegativa);

    return dataset;
  }

  public XYDataset geraDatasetMM (double[] positivos, double[] negativos) {

    XYSeriesCollection dataset = new XYSeriesCollection();

    XYSeries serieNegativa = new XYSeries("Negativos");
    XYSeries seriePositiva = new XYSeries("Positivos");

    for (int i = 0; i < positivos.length; i++) {
      serieNegativa.add(i + 1, negativos[i]);
      seriePositiva.add(i + 1, positivos[i]);
      i++;
    }

    dataset.addSeries(serieNegativa);
    dataset.addSeries(seriePositiva);

    return dataset;
  }

  public JFreeChart geraBarChart (CategoryDataset dataset) {
    JFreeChart chart = ChartFactory.createBarChart("KRI - Dia", "", "", dataset);

    return chart;
  }

  public JFreeChart geraSplineChart (XYDataset dataset) {
    JFreeChart chart = ChartFactory.createXYLineChart("KRI - Dia", "", "", dataset);

    return chart;
  }

}
