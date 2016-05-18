package editorgrafos;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/* FACULDADE COTEMIG
 * TRABALHO PRATICO - ALGORITMOS E ESTRUTURAS DE DADOS II
 * EDITOR DE GRAFOS
 * REVISÃO: 2016.1
 * AUTOR: 
 *      PROF. VIRGILIO BORGES DE OLIVEIRA
 *      ALUNO: LEANDRO HENRIQUE DALDEGAM FONTES
 **/
public class Grafo extends GrafoBase {

    private Random random = new Random(System.currentTimeMillis());

    public void doAGM() {
        int custoTotal = 0;
        Aresta menorArestaAtual = null;
        Vertice verticeDeDestinoDaMenorAresta = null;
        for (int r = 0; r < this.getN(); r++) {
            for (int i = 0; i < this.getN(); i++) {
                Vertice vertice = getVertice(i);
                if (vertice.isChecked() == true) {

                    for (int j = 0; j < this.getN(); j++) {
                        Aresta aresta = getAresta(i, j);
                        if (aresta != null && i != j) {
                            System.out.format("Comparando %s com %s\n", vertice.getRotulo(), getVertice(j).getRotulo());
                            if (getVertice(j).isChecked() == false
                                    && (menorArestaAtual == null
                                    || aresta.getPeso() < menorArestaAtual.getPeso())) {
                                menorArestaAtual = aresta;
                                verticeDeDestinoDaMenorAresta = getVertice(j);

                                System.out.format("\tLigou o %s com %s\n", vertice.getRotulo(), getVertice(j).getRotulo());
                            }
                        }
                    }

                    vertice.setChecked(true);
                    vertice.setCor(Color.red);

                }
            }
            if (menorArestaAtual != null) {
                verticeDeDestinoDaMenorAresta.setChecked(true);
                verticeDeDestinoDaMenorAresta.setCor(Color.red);
                custoTotal += menorArestaAtual.getPeso();
                menorArestaAtual.setCor(Color.RED);
                menorArestaAtual = null;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                }
            }

        }
        JOptionPane.showMessageDialog(this, "Menor custo total da AGM: " + custoTotal);
    }

    public void AGM() {
        this.resetChecked();
        getVertice(0).setChecked(true);
        new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground() {
                doAGM();
                return null;
            }

        }.execute();
    }

    // Variáveis para auxiliar o caminhoMinimo()
    private int ANTECESSOR = 0;
    private int ESTIMATIVA = 1;
    private int FECHADO = 2;

    public void caminhoMinimo(int begin, int end) {
        this.resetChecked();
        int controle[][] = new int[this.getN()][3];
        for (int i = 0; i < this.getN(); i++) {
            controle[i][ANTECESSOR] = 0; //I do antecessor
            controle[i][ESTIMATIVA] = Integer.MAX_VALUE; //Estimativa minima
            controle[i][FECHADO] = 0; // 0 fechado, 1 aberto
        }

        controle[begin][ESTIMATIVA] = 0; // Seta a estimativa do primeiro para 0
        controle[begin][FECHADO] = 1; // Seta o primeiro como fechado

        int i = begin;
        boolean finish = false;
        while (finish == false) {

            for (int j = 0; j < this.getN(); j++) {
                Aresta tmpAresta = this.getAresta(i, j);

                if (tmpAresta != null) {

                    if (controle[i][ESTIMATIVA] + tmpAresta.getPeso() < controle[j][ESTIMATIVA] && controle[j][FECHADO] == 0) {
                        if (controle[j][ESTIMATIVA] == Integer.MAX_VALUE) {
                            controle[j][ESTIMATIVA] = 0;
                        }

                        controle[j][ANTECESSOR] = i;
                        controle[j][ESTIMATIVA] = controle[i][ESTIMATIVA] + tmpAresta.getPeso();
                    }
                }
            }

            // Informação de debug
            for (int y = 0; y < this.getN(); y++) {
                System.out.print(this.getVertice(y).getRotulo() + " "
                        + (controle[y][ESTIMATIVA] == Integer.MAX_VALUE ? "∞" : controle[y][ESTIMATIVA])
                        + (controle[y][FECHADO] == 1 ? " T" : " F") + ", ");
            }
            System.out.println("");

            // Testa se ainda existem abertos
            boolean naoTemMaisAbertos = true;
            for (int k = 0; k < this.getN(); k++) {
                if (controle[k][FECHADO] == 0) {
                    naoTemMaisAbertos = false;
                }
            }
            finish = naoTemMaisAbertos;

            // Encontra o proximo que vamos testar (Menor valor de estimativa aberto)
            int menorEstimativaTmp = Integer.MAX_VALUE;
            for (int p = 0; p < this.getN(); p++) {
                if (controle[p][ESTIMATIVA] < menorEstimativaTmp && controle[p][FECHADO] == 0) {
                    menorEstimativaTmp = controle[p][ESTIMATIVA];
                    i = p;
                }
            }
            controle[i][FECHADO] = 1;
        }

        // Monta o caminho mínimo do begin até o end
        int caminhoAtual = end;
        while (caminhoAtual != begin) {
            getAresta(caminhoAtual, controle[caminhoAtual][ANTECESSOR]).setCor(Color.red);
            caminhoAtual = controle[caminhoAtual][ANTECESSOR];
        }
        JOptionPane.showMessageDialog(this, "Caminho mínimo do "
                + this.getVertice(begin).getRotulo()
                + " até " + this.getVertice(end).getRotulo() + " é: " + controle[end][ESTIMATIVA]);
    }

    public void recriarPesos() {
        for (int i = 0; i < this.getN(); i++) {
            for (int j = 0; j < this.getN(); j++) {
                Aresta aresta = getAresta(i, j);
                if (aresta != null && i != j) {
                    aresta.setPeso(random.nextInt(10));
                }
            }
        }
    }

    public void completarGrafo() {
        for (int i = 0; i < this.getN(); i++) {
            for (int j = 0; j < this.getN(); j++) {
                Aresta aresta = getAresta(i, j);
                if (aresta == null && i != j) {
                    setAresta(i, j, this.getPesosAleatorios() ? random.nextInt(10) : 1);
                }
            }
        }
    }

    public boolean isEuleriano() {
        int i;
        //getVertice(1).setCor(Color.BLUE);
        //getVertice(0).setRotulo("SP");
        //getAresta(0, 1).setCor(Color.YELLOW);
        //getAresta(0, 1).setPeso(Integer.parseInt(JOptionPane.showInputDialog(null, "Digite o peso:")));
        for (i = 0; i < this.getN(); i++) {
            if (this.grau(i) % 2 != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isUnicursal() {
        int p = 0;
        for (int i = 0; i < this.getN(); i++) {
            if (this.grau(i) % 2 != 0) {
                p++;
            }
        }
        return p == 2;
    }

    private void resetChecked() {
        for (int i = 0; i < getN(); i++) {
            for (int j = 0; j < getN(); j++) {
                getVertice(i).setChecked(false);
                getVertice(i).setCor(Color.BLACK);
                getVertice(i).setCorMarcado(Color.BLACK);
                getVertice(i).desmarcar();
                if (getAresta(i, j) != null) {
                    getAresta(i, j).setCor(Color.BLACK);
                }
            }
        }
    }

    public void largura(final int v) {
        this.resetChecked();
        new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground() {
                doLargura(v);
                return null;
            }
        }.execute();
    }

    public void doLargura(int v) {
        Fila f = new Fila(getN());
        f.enfileirar(v);
        getVertice(v).setChecked(true);
        getVertice(v).setCor(Color.GREEN);
        while (!f.vazia()) {
            v = f.desenfileirar();
            for (int i = 0; i < getN(); i++) {
                if (getAresta(v, i) != null && getVertice(i).isChecked() == false) {
                    f.enfileirar(i);
                    getVertice(i).setChecked(true);
                    getVertice(i).setCor(Color.GREEN);
                    getAresta(v, i).setCor(Color.GREEN);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }
    }

    public void doNumeroCromatico() {
        int maximoDeCores = 1;
        Color coresList[] = new Color[8];
        coresList[0] = Color.PINK;
        coresList[1] = Color.BLUE;
        coresList[2] = Color.RED;
        coresList[3] = Color.YELLOW;
        coresList[4] = Color.GREEN;
        coresList[5] = Color.MAGENTA;
        coresList[6] = Color.CYAN;
        coresList[7] = Color.WHITE;

        int v = 0; // Vertice de maior grau
        
        int maiorGrau = 0;
        for(int i = 0; i < this.getN(); i++)
        {
            int tempGrau = this.grau(i);
            if(tempGrau > maiorGrau)
            {
                maiorGrau = tempGrau;
                v = i; // Vertice de maior grau
            }
        }
        
        
        Fila f = new Fila(getN());
        f.enfileirar(v);
        getVertice(v).setChecked(true);
        getVertice(v).setCor(coresList[0]);
        int indexTempCor = 1;
        maximoDeCores++;
        while (!f.vazia()) {
            v = f.desenfileirar();
            for (int i = 0; i < getN(); i++) {
                if (getAresta(v, i) != null && getVertice(i).isChecked() == false) {
                    f.enfileirar(i);
                    getVertice(i).setChecked(true);
                    
                    boolean testaCorNovamente = false;
                    do
                    {
                        testaCorNovamente = false;
                        for(int c = 0; c < this.getN(); c++)
                        {
                            if(getAresta(i, c) != null) {
                                if(getVertice(c).getCor() == coresList[indexTempCor])
                                {
                                    testaCorNovamente = true;
                                    indexTempCor++;
                                    if(indexTempCor > maximoDeCores)
                                    {
                                        maximoDeCores = indexTempCor;
                                    }
                                }
                            }
                        }
                    } while(testaCorNovamente);
                    
                    getVertice(i).setCor(coresList[indexTempCor]);
                    
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                    }
                }
            }
            indexTempCor = 0;
        }
        JOptionPane.showMessageDialog(this, "Número cromático: " + maximoDeCores);
    }
    
    public void numeroCromatico()
    {
        this.resetChecked();
        new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground() {
                doNumeroCromatico();
                return null;
            }
        }.execute();
    }

    public String paresOrdenados() {
        String pares = "E = { \n";
        for (int i = 0; i < this.getN(); i++) {
            Vertice vi = getVertice(i);
            for (int j = 0; j < this.getN(); j++) {
                Aresta aresta = getAresta(i, j);
                if (aresta != null) {
                    pares += "(" + vi.getRotulo() + ", " + getVertice(j).getRotulo() + "),";
                }
            }
            pares += "\n";
        }
        return pares + " }";
    }

    public void profundidade(final int v) {
        this.resetChecked();
        new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground() {
                doProfundidade(v);
                return null;
            }
        }.execute();
    }

    public void doProfundidade(int v) {
        getVertice(v).setChecked(true);
        getVertice(v).setCor(Color.ORANGE);

        for (int i = 0; i < getN(); i++) {
            if (getAresta(v, i) != null && getVertice(i).isChecked() == false) {
                getAresta(v, i).setCor(Color.ORANGE);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                }
                this.doProfundidade(i);
            }
        }
    }
}
