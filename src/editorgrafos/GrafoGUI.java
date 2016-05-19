/* FACULDADE COTEMIG * TRABALHO PRATICO - ALGORITMOS E ESTRUTURAS DE DADOS II * EDITOR DE GRAFOS * REVISÃO: 2016.1 * AUTOR:  *      PROF. VIRGILIO BORGES DE OLIVEIRA *      ALUNO: LEANDRO HENRIQUE DALDEGAM FONTES **/package editorgrafos;import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.awt.event.KeyEvent;import javax.swing.JCheckBoxMenuItem;import javax.swing.JFileChooser;import javax.swing.JFrame;import javax.swing.JMenu;import javax.swing.JMenuBar;import javax.swing.JMenuItem;import javax.swing.JOptionPane;import javax.swing.KeyStroke;import javax.swing.filechooser.FileNameExtensionFilter;public class GrafoGUI extends JFrame implements ActionListener {    private JMenuBar menuBar;    private JMenu menu;    private JCheckBoxMenuItem mExibPesos,             mPesosAleatorios,             mInformarNomes,             mInformarPesos;    /* Complete a declaração abaixo com as demais opções do menu: */    private JMenuItem mNovo,            mAbrir,            mSalvar,            mSair,            mSobre,            mPares,            mEuleriano,            mUnicursal,            mCompletarGrafo,            mProfundidade,            mLargura,            mAGM,            mCM,            mNumeroCromaticoVertices,            mNumeroCromaticoArestas,            mRecriarPesos;    Grafo g = new Grafo();    public GrafoGUI(String titulo) {        super(titulo); // define o título da janela        getContentPane().add(g); //  adiciona o grafo na janela        /* MENUS */        menuBar = new JMenuBar(); // cria a barra de menus        menu = new JMenu("Arquivo"); // primeiro menu        menu.setMnemonic(KeyEvent.VK_A);        menuBar.add(menu);        mNovo = new JMenuItem("Novo", KeyEvent.VK_N);        mNovo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,                ActionEvent.CTRL_MASK));        mNovo.addActionListener(this);        menu.add(mNovo);        mAbrir = new JMenuItem("Abrir", KeyEvent.VK_A);        mAbrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,                ActionEvent.CTRL_MASK));        mAbrir.addActionListener(this);        menu.add(mAbrir);        mSalvar = new JMenuItem("Salvar", KeyEvent.VK_S);        mSalvar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,                ActionEvent.CTRL_MASK));        mSalvar.addActionListener(this);        menu.add(mSalvar);        menu.addSeparator(); // separador        mSair = new JMenuItem("Sair", KeyEvent.VK_R);        mSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,                ActionEvent.ALT_MASK));        mSair.addActionListener(this);        menu.add(mSair);        menu = new JMenu("Algoritmos"); // segundo menu        menu.setMnemonic(KeyEvent.VK_G);        menuBar.add(menu);        mPares = new JMenuItem("Pares Ordenados", KeyEvent.VK_O);        mPares.addActionListener(this);        menu.add(mPares);        mEuleriano = new JMenuItem("Grafo Euleriano", KeyEvent.VK_E);        mEuleriano.addActionListener(this);        menu.add(mEuleriano);        mUnicursal = new JMenuItem("Grafo Unicursal", KeyEvent.VK_U);        mUnicursal.addActionListener(this);        menu.add(mUnicursal);        mCompletarGrafo = new JMenuItem("Completar Grafo", KeyEvent.VK_C);        mCompletarGrafo.addActionListener(this);        menu.add(mCompletarGrafo);        mProfundidade = new JMenuItem("Busca em Profundidade", KeyEvent.VK_P);        mProfundidade.addActionListener(this);        menu.add(mProfundidade);        mLargura = new JMenuItem("Busca em Largura", KeyEvent.VK_L);        mLargura.addActionListener(this);        menu.add(mLargura);        mAGM = new JMenuItem("Arvore geradora mínima", KeyEvent.VK_A);        mAGM.addActionListener(this);        menu.add(mAGM);        mCM = new JMenuItem("Caminho mínimo");        mCM.addActionListener(this);        menu.add(mCM);        mNumeroCromaticoVertices = new JMenuItem("Número cromático (Vertices)");        mNumeroCromaticoVertices.addActionListener(this);        menu.add(mNumeroCromaticoVertices);        mNumeroCromaticoArestas = new JMenuItem("Número cromático (Arestas)");        mNumeroCromaticoArestas.addActionListener(this);        menu.add(mNumeroCromaticoArestas);        /*		          * Complete com as demais opções do menu          */        menu = new JMenu("Ferramentas"); // terceiro menu        menu.setMnemonic(KeyEvent.VK_F);        menuBar.add(menu);        mExibPesos = new JCheckBoxMenuItem("Exibir pesos");        mExibPesos.addActionListener(this);        menu.add(mExibPesos);        mPesosAleatorios = new JCheckBoxMenuItem("Gerar pesos aleatórios");        mPesosAleatorios.addActionListener(this);        menu.add(mPesosAleatorios);                mInformarNomes = new JCheckBoxMenuItem("Informar nomes dos vertices");        mInformarNomes.addActionListener(this);        menu.add(mInformarNomes);                mInformarPesos = new JCheckBoxMenuItem("Informar pesos das arestas");        mInformarPesos.addActionListener(this);        menu.add(mInformarPesos);        mRecriarPesos = new JMenuItem("Recriar pesos", KeyEvent.VK_A);        mRecriarPesos.addActionListener(this);        menu.add(mRecriarPesos);        menu = new JMenu("Ajuda"); // quarto menu        menu.setMnemonic(KeyEvent.VK_J);        menuBar.add(menu);        mSobre = new JMenuItem("Sobre...", KeyEvent.VK_S);        mSobre.addActionListener(this);        menu.add(mSobre);        setJMenuBar(menuBar); // adiciona a barra de menus ao frame        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // terminar o programa        // ao fechar a janela        setResizable(true); // permite modificar o tamanho da janela        setSize(610, 500); // define o tamanho da janela (colunas, linhas)        setVisible(true); // torna o frame visível    }    public void actionPerformed(ActionEvent e) {        if (e.getSource() == mNovo) {            g.limpar();        }        if (e.getSource() == mAbrir) {            JFileChooser chooser = new JFileChooser();            FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos de Grafo", "grf");            chooser.setFileFilter(filter);            int returnVal = chooser.showOpenDialog(this);            if (returnVal == JFileChooser.APPROVE_OPTION) {                g.abrirArquivo(chooser.getSelectedFile().getAbsoluteFile());            }        }        if (e.getSource() == mSalvar) {            JFileChooser chooser = new JFileChooser();            FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos de Grafo", "grf");            chooser.setFileFilter(filter);            int returnVal = chooser.showSaveDialog(this);            if (returnVal == JFileChooser.APPROVE_OPTION) {                g.salvarArquivo(chooser.getSelectedFile().getAbsoluteFile());            }        }        if (e.getSource() == mSair) {            System.exit(0);        }        if (e.getSource() == mExibPesos) {            g.setExibirPesos(mExibPesos.getState());        }        if (e.getSource() == mPesosAleatorios) {            g.setPesosAleatorios(mPesosAleatorios.getState());        }        if (e.getSource() == mInformarNomes) {            g.setInformarNomes(mInformarNomes.getState());        }        if (e.getSource() == mInformarPesos) {            g.setInformarPesos(mInformarPesos.getState());        }        if (e.getSource() == mSobre) {            JOptionPane.showMessageDialog(this,                    "EDITOR DE GRAFOS\nversão 2016.1\n"                    + "Interface desenvolvida por: Virgílio Borges de Oliveira.\n"                    + "Implementações complementares desenvolvida por: Leandro Daldegam.\n"                    + "FACULDADE COTEMIG (somente para fins didáticos)");        }        if (e.getSource() == mCompletarGrafo) {            g.completarGrafo();        }        if (e.getSource() == mPares) {            JOptionPane.showMessageDialog(this, g.paresOrdenados());        }        if (e.getSource() == mEuleriano) {            if (g.isEuleriano()) {                JOptionPane.showMessageDialog(this, "É Euleriano");            } else {                JOptionPane.showMessageDialog(this, "Não é Euleriano");            }        }        if (e.getSource() == mUnicursal) {            if (g.isUnicursal()) {                JOptionPane.showMessageDialog(this, "É Unicursal");            } else {                JOptionPane.showMessageDialog(this, "Não é Unicursal");            }        }        if (e.getSource() == mProfundidade) {            g.profundidade(0);        }        if (e.getSource() == mLargura) {            g.largura(0);        }        if (e.getSource() == mAGM) {            g.AGM();        }        if (e.getSource() == mCM) {            String vertice1 = JOptionPane.showInputDialog(rootPane, "Digite o nome do vertice 1:");            String vertice2 = JOptionPane.showInputDialog(rootPane, "Digite o nome do vertice 2:");            int v1 = -1;            int v2 = -1;            for (int i = 0; i < g.getN(); i++) {                if (g.getVertice(i).getRotulo().toLowerCase().equals(vertice1)) {                    v1 = i;                }                if (g.getVertice(i).getRotulo().toLowerCase().equals(vertice2)) {                    v2 = i;                }            }            if (v1 == -1 || v2 == -1) {                JOptionPane.showMessageDialog(this, "Os vertices informados não foram encontrados!");            } else {                g.caminhoMinimo(v1, v2);            }        }        if (e.getSource() == mRecriarPesos) {            g.recriarPesos();        }        if (e.getSource() == mNumeroCromaticoVertices) {            g.numeroCromaticoVertices();        }        if (e.getSource() == mNumeroCromaticoArestas) {            g.numeroCromaticoArestas();        }        /* Complete com as demais opções do menu */    }    public static void main(String args[]) {        new GrafoGUI("Editor de Grafos - 2016.1");    }}