/* FACULDADE COTEMIG
 * TRABALHO PRATICO - ALGORITMOS E ESTRUTURAS DE DADOS II
 * EDITOR DE GRAFOS
 * REVISÃO: 2016.1
 * AUTOR: 
 *      PROF. VIRGILIO BORGES DE OLIVEIRA
 *      ALUNO: LEANDRO HENRIQUE DALDEGAM FONTES
 **/
package editorgrafos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public abstract class GrafoBase extends JPanel implements MouseListener {

    private int n; // n�mero de v�rtices
    private LinkedList<Vertice> vertices; // lista de v�rtices
    private Aresta matAdj[][]; // matriz de adjac�ncias
    private Vertice vMarcado;
    private boolean exibirPesos, pesosAleatorios, informarNomes, informarPesos;

    public GrafoBase() {
        setLayout(null);
        n = 0;
        vertices = new LinkedList<Vertice>(); // inicializa a lista de v�rtices		
        vMarcado = null;
        matAdj = new Aresta[500][500];
        exibirPesos = false;
        informarNomes = false;
        informarPesos = false;
        addMouseListener(this);
        setBackground(Color.WHITE); // cor de fundo
    }

    public void setInformarNomes(boolean informarNomes) {
        this.informarNomes = informarNomes;
    }

    public void setInformarPesos(boolean informarPesos) {
        this.informarPesos = informarPesos;
    }

    public void setExibirPesos(boolean e) {
        exibirPesos = e;
        repaint();
    }

    public boolean getExibirPesos() {
        return exibirPesos;
    }

    public void setPesosAleatorios(boolean e) {
        pesosAleatorios = e;
    }

    public boolean getPesosAleatorios() {
        return pesosAleatorios;
    }

    /* Retorna o objeto Vertice referente ao n� passado */
    public Vertice getVertice(int v) {
        int i;
        Vertice aux;
        for (i = 0; i < vertices.size(); i++) {
            aux = vertices.get(i);
            if (aux.getNum() == v) {
                return aux;
            }
        }
        return null;
    }

    public Vertice getVerticeMarcado() {
        return vMarcado;
    }

    public Aresta getAresta(int i, int j) {
        return matAdj[i][j];
    }

    public void setAresta(int i, int j, int peso) {
        matAdj[i][j] = matAdj[j][i] = new Aresta(peso, Color.BLACK, this);
    }
    
    public void removeAresta(int i, int j) {
        matAdj[i][j] = matAdj[j][i] = null;
        repaint();
    }

    public int getN() {
        return n;
    }

    public ArrayList<Vertice> getAdjacentes(int v) {
        ArrayList<Vertice> lista = new ArrayList<Vertice>();
        for (int i = 0; i < n; i++) {
            if (matAdj[v][i] != null) {
                lista.add(getVertice(i));
            }
        }
        return lista;
    }

    public final void paint(Graphics g) {
        int i, j, tam;
        Vertice vi, vj;
        Aresta a;
        super.paint(g);
        for (i = 0; i < n; i++) {
            for (j = 0; j < i; j++) {
                if (matAdj[i][j] != null) {
                    a = matAdj[i][j];
                    vi = getVertice(i);
                    vj = getVertice(j);
                    g.setColor(a.getCor());
                    g.drawLine(vi.getX() + 5, vi.getY() + 5, vj.getX() + 5, vj.getY() + 5);
                    if (exibirPesos) {
                        g.setColor(Color.lightGray);
                        tam = g.getFontMetrics().stringWidth(a.getPeso() + "");
                        g.fillRect((vi.getX() + vj.getX() + tam + 4) / 2, (vi.getY() + vj.getY()) / 2, tam + 4, 15);
                        g.setColor(a.getCor());
                        g.drawString(a.getPeso() + "", (vi.getX() + vj.getX() + tam + 7) / 2, (vi.getY() + vj.getY() + 23) / 2);
                    }
                }
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX(), // coordenada X do clique do mouse
                y = e.getY(); // coordenada Y do clique do mouse

        /* ADICIONA O V�RTICE v NA TELA, NAS COORDENADAS x,y DO CLICK MOUSE */
        addVertice(n, "V" + (n + 1), x, y);
        n++;
    }

    /* DEMAIS EVENTOS DO MOUSE (n�o mexer) */
    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    /* EVENTOS DO GRAFO */
    public void addVertice(int n, String r, int x, int y) {
        Vertice v = new Vertice(n, r, x, y); // cria novo v�rtice 
        v.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Vertice v = (Vertice) e.getSource();
                v.setLocation(e.getX() + v.getX(), e.getY() + v.getY());
                repaint();
            }
        });
        v.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                clicouVertice((Vertice) e.getComponent());
            }
        });
        v.setToolTipText(v.getRotulo());
        if (adicionouVertice(v)) {
            vertices.add(v); // adiciona a lista de v�rtices
            this.add(v);
        }
    }
    
    public void removeVertice(Vertice v)
    {
        n--; // vai bugar quando remover um vertice que não for o ultimo, pois é necessário recalcular todos os N e posicionar eles novamente
        int vNum = v.getNum();
        for (int i = 0; i < n; i++) {
            if (matAdj[vNum][i] != null) {
                this.removeAresta(vNum, i);
            }
        }
        this.remove(v);
        vertices.remove(v);
        repaint();
    }

    public boolean adicionouVertice(Vertice v) {
        System.out.println("[DEBUG] => Novo vertice: " + v);
        if (informarNomes) {
            v.setRotulo(JOptionPane.showInputDialog("Digite o rótulo do vértice:"));
        }
        return true;
    }

    public boolean adicionouAresta(Aresta a) {
        System.out.println("[DEBUG] => Nova aresta");
        if (informarPesos) {
            a.setPeso(Integer.parseInt(JOptionPane.showInputDialog("Digite o peso:")));
        }
        return true;
    }

    public void clicouVertice(Vertice v) {
        if (v.getMarcado()) {
            v.desmarcar();
            vMarcado = null;
        } else {
            v.marcar();
            if (vMarcado != null) {
                int peso;

                if (getPesosAleatorios()) {
                    peso = (int) (Math.random() * 100) + 1;
                } else {
                    peso = 1;
                }

                Aresta a = new Aresta(peso, Color.BLACK, this); // cria a nova aresta
                if (matAdj[vMarcado.getNum()][v.getNum()] == null && adicionouAresta(a)) {
                    matAdj[vMarcado.getNum()][v.getNum()] = matAdj[v.getNum()][vMarcado.getNum()] = a;
                    vMarcado.desmarcar();
                    vMarcado = v;
                    repaint();
                } else {
                    a = null;
                    v.desmarcar();
                }
            } else {
                vMarcado = v;
            }
        }
    }

    public void limpar() {
        n = 0;
        vertices.clear(); // limpa a lista de v�rtices
        vMarcado = null; // limpa a referencia a qualquer vertice marcado
        matAdj = new Aresta[500][500];
        this.removeAll();
        this.repaint(); // redesenha a tela
    }

    public void abrirArquivo(File f) {
        try {
            int i, j, p;
            int ultimo, vx, vy;
            String nome;
            BufferedReader in;
            in = new BufferedReader(new FileReader(f));
            Scanner s = new Scanner(in);

            this.limpar();

            this.n = s.nextInt();
            ultimo = s.nextInt();

            for (i = 0; i < n; i++) {
                vx = s.nextInt(); // coordenada x
                vy = s.nextInt(); // coordenada y
                nome = s.next();
                addVertice(i, nome, vx, vy);
            }

            for (i = 0; i < n; i++) {
                for (j = 0; j < i; j++) {
                    s.nextInt();
                }
            }

            for (i = 0; i < n; i++) {
                for (j = 0; j < i; j++) {
                    p = s.nextInt();
                    if (p > 0) {
                        matAdj[i][j] = matAdj[j][i] = new Aresta(p, Color.BLACK, this);
                    }
                }
            }
            in.close();
            s.close();
        } catch (Exception eX) {
            System.out.println("Erro ao abrir arquivo. " + eX.getMessage());
        }
    }

    public void salvarArquivo(File f) {
        try {
            int i, j;
            BufferedWriter out;
            String arq = f.toString();
            if (!arq.toLowerCase().substring(arq.length() - 4).equals(".grf")) {
                arq = arq + ".grf";
            }
            out = new BufferedWriter(new FileWriter(arq));

            out.write(this.n + "");
            out.newLine();
            out.write(this.n + "");
            out.newLine();
            out.newLine();

            for (i = 0; i < n; i++) {
                out.write(getVertice(i).getX() + " "
                        + getVertice(i).getY() + " "
                        + getVertice(i).getRotulo());
                out.newLine();
            }
            out.newLine();

            for (i = 0; i < n; i++) {
                for (j = 0; j < i; j++) {
                    if (matAdj[i][j] != null) {
                        out.write("1 ");
                    } else {
                        out.write("0 ");
                    }
                }
                out.newLine();
            }

            for (i = 0; i < n; i++) {
                for (j = 0; j < i; j++) {
                    if (matAdj[i][j] != null) {
                        out.write(matAdj[i][j].getPeso() + " ");
                    } else {
                        out.write("0 ");
                    }
                }
                out.newLine();
            }
            out.close();
        } catch (IOException eX) {
            System.out.println("Erro ao gravar arquivo. " + eX.getMessage());
        }
    }

    public int grau(int v) {
        int cont = 0;
        for (int i = 0; i < n; i++) {
            if (matAdj[v][i] != null) {
                cont++;
            }
        }
        return cont;
    }

    public abstract boolean isEuleriano();

    public abstract boolean isUnicursal();

    public abstract String paresOrdenados();

    public abstract void completarGrafo();

    public abstract void profundidade(int v);

    public abstract void largura(int v);

    public abstract void AGM();

    public abstract void caminhoMinimo(int i, int j);

    public abstract void numeroCromaticoVertices();

    public abstract void numeroCromaticoArestas();
}
