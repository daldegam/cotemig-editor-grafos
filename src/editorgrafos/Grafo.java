package editorgrafos;

import java.awt.Color;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private boolean visitado[];

    public void AGM(int v) {

    }

    public void caminhoMinimo(int i, int j) {

    }

    public void completarGrafo() {

        for (int i = 0; i < this.getN(); i++) {
            for (int j = 0; j < this.getN(); j++) {
                Aresta aresta = getAresta(i, j);
                if (aresta == null && i != j) {
                    setAresta(i, j, 1);
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
                if (getAresta(i, j) != null) {
                    getAresta(i, j).setCor(Color.BLACK);
                }
            }
        }
    }

    public void largura(int v) {
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

    public void numeroCromatico() {
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

    public void profundidade(int v) {
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
