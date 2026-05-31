package com.bbm4.view;

import com.bbm4.dao.*;
import com.bbm4.model.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MainView extends JFrame {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ViaturaDao             viaturaDao   = new ViaturaDao();
    private final ConvenioDao            convenioDao  = new ConvenioDao();
    private final RevisaoDao             revisaoDao   = new RevisaoDao();
    private final TrocaOleoDao           trocaOleoDao = new TrocaOleoDao();
    private final AcidenteDao            acidenteDao  = new AcidenteDao();
    private final DoacaoDao              doacaoDao    = new DoacaoDao();
    private final PneuEstoqueDao         pneuDao      = new PneuEstoqueDao();
    private final BateriaDao             bateriaDao   = new BateriaDao();
    private final CartaoAbastecimentoDao cartaoDao      = new CartaoAbastecimentoDao();
    private final ModeloSeiDao           modeloSeiDao   = new ModeloSeiDao();
    private final com.bbm4.dao.ServicoFeitoDao servicoDao = new com.bbm4.dao.ServicoFeitoDao();
    private final com.bbm4.dao.SituacaoProcessoSeiDao situacaoProcessoSeiDao = new com.bbm4.dao.SituacaoProcessoSeiDao();

    private JTable tViaturas, tRevisoes, tOleo, tAcidentes;
    private JTable tConvenios, tDoacoes, tPneus, tBaterias, tCartoes, tModelosSei, tServicos, tSituacaoProcessoSei;

    public MainView(String nomeUsuario) {
        setTitle("4\u00ba BBM \u2014 Gerenciamento de Suprimentos e Frota  |  " + nomeUsuario);
        setSize(1366, 860);
        setMinimumSize(new Dimension(1100, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppTheme.COR_FUNDO);
        initUI();
        carregarTodos();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        add(criarHeader(), BorderLayout.NORTH);
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setBackground(AppTheme.COR_FUNDO);
        tabs.setForeground(AppTheme.COR_TEXTO_SEC);
        tabs.setFont(AppTheme.FONTE_ABA);
        tabs.addTab("Viaturas",           painelViaturas());
        tabs.addTab("Revis\u00f5es",      painelRevisoes());
        tabs.addTab("Troca de \u00d3leo", painelOleo());
        tabs.addTab("Acidentes",          painelAcidentes());
        tabs.addTab("Conv\u00eanios",     painelConvenios());
        tabs.addTab("Doa\u00e7\u00f5es", painelDoacoes());
        tabs.addTab("Pneus Estoque",      painelPneus());
        tabs.addTab("Baterias",           painelBaterias());
        tabs.addTab("Cart\u00f5es",       painelCartoes());
        tabs.addTab("Modelos SEI",        painelModelosSei());
        tabs.addTab("Situa\u00e7\u00f5es SEI",  painelSituacaoProcessoSei());
        tabs.addTab("Servi\u00e7os",       painelServicos());
        tabs.addTab("Dashboard",         painelDashboard());
        for (int ti = 0; ti < tabs.getTabCount(); ti++) {
            final int idx = ti;
            final String titulo = tabs.getTitleAt(ti);
            JPanel tp = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            tp.setOpaque(false);
            final int tipo = ti;
            JComponent dot = new JComponent() {
                { setPreferredSize(new Dimension(18, 16)); }
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    boolean sel = tabs.getSelectedIndex() == idx;
                    Color c = sel ? Color.WHITE : AppTheme.COR_PRIMARIA;
                    g2.setColor(c);
                    g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    switch (tipo) {
                        case 0: g2.fillRoundRect(1,5,12,7,2,2); g2.fillOval(2,10,4,4); g2.fillOval(10,10,4,4); g2.fillRect(3,3,6,4); break;
                        case 1: g2.setStroke(new BasicStroke(2f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND)); g2.drawLine(3,13,11,5); g2.fillOval(1,10,5,5); g2.fillOval(10,1,5,5); break;
                        case 2: g2.fillPolygon(new int[]{9,15,9,3},new int[]{1,9,14,9},4); break;
                        case 3: g2.setStroke(new BasicStroke(2f)); g2.drawPolygon(new int[]{9,17,1},new int[]{1,15,15},3); g2.fillRect(8,6,2,5); g2.fillRect(8,12,2,2); break;
                        case 4: g2.setStroke(new BasicStroke(1.8f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND)); g2.drawLine(1,8,7,4); g2.drawLine(7,4,11,8); g2.drawLine(11,8,17,4); break;
                        case 5: g2.setStroke(new BasicStroke(1.8f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND)); g2.drawRect(2,7,14,8); g2.drawLine(9,1,9,9); g2.drawLine(5,5,9,1); g2.drawLine(13,5,9,1); break;
                        case 6: g2.setStroke(new BasicStroke(3f)); g2.drawOval(3,2,12,12); g2.setStroke(new BasicStroke(1.5f)); g2.drawOval(7,6,4,4); break;
                        case 12: g2.setStroke(new BasicStroke(1.8f)); g2.drawRect(2,3,13,11); g2.drawLine(2,6,15,6); g2.fillRect(5,9,7,2); break;
                        case 7: g2.setStroke(new BasicStroke(1.8f)); g2.drawRect(2,5,14,9); g2.fillRect(5,3,3,3); g2.fillRect(10,3,3,3); g2.setColor(new Color(48,209,88)); g2.drawLine(5,8,5,12); g2.drawLine(3,10,7,10); g2.setColor(c); g2.drawLine(11,10,15,10); break;
                        case 8: g2.setStroke(new BasicStroke(1.5f)); g2.drawRoundRect(1,4,16,10,3,3); g2.fillRect(1,7,16,3); g2.fillRoundRect(3,10,5,2,1,1); break;
                        default: g2.setStroke(new BasicStroke(1.5f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND)); g2.drawRoundRect(2,1,12,15,2,2); g2.drawLine(5,5,11,5); g2.drawLine(5,8,11,8); g2.drawLine(5,11,9,11); break;
                    }
                    g2.dispose();
                }
            };
            JLabel lbl = new JLabel(titulo);
            lbl.setFont(AppTheme.FONTE_ABA);
            lbl.setForeground(AppTheme.COR_TEXTO_SEC);
            tp.add(dot); tp.add(lbl);
            tabs.setTabComponentAt(ti, tp);
            tabs.addChangeListener(e -> {
                boolean sel = tabs.getSelectedIndex() == idx;
                lbl.setForeground(sel ? Color.WHITE : AppTheme.COR_TEXTO_SEC);
                dot.repaint();
            });
        }
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(AppTheme.COR_FUNDO);
        centro.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));
        centro.add(tabs, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
    }

    private JPanel criarHeader() {
        JPanel h = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0,0,AppTheme.COR_PRIMARIA,getWidth(),0,AppTheme.COR_PRIMARIA_DARK));
                g2.fillRect(0,0,getWidth(),getHeight());
                g2.dispose();
            }
        };
        h.setOpaque(false);
        h.setBorder(BorderFactory.createEmptyBorder(12,20,12,20));
        JPanel esq = new JPanel(new FlowLayout(FlowLayout.LEFT,14,0));
        esq.setOpaque(false);
        ImageIcon brasao = AppTheme.getBrasao(60,60);
        if (brasao != null) esq.add(new JLabel(brasao));
        JPanel txt = new JPanel(); txt.setLayout(new BoxLayout(txt,BoxLayout.Y_AXIS)); txt.setOpaque(false);
        JLabel t1 = new JLabel("4\u00ba BBM \u2014 Gerenciamento de Suprimentos e Frota");
        t1.setFont(new Font("Segoe UI",Font.BOLD,16)); t1.setForeground(Color.WHITE);
        JLabel t2 = new JLabel("Corpo de Bombeiros Militar de Minas Gerais");
        t2.setFont(new Font("Segoe UI",Font.PLAIN,11)); t2.setForeground(new Color(255,255,255,180));
        txt.add(t1); txt.add(Box.createVerticalStrut(3)); txt.add(t2);
        esq.add(txt); h.add(esq,BorderLayout.WEST);
        return h;
    }

    // =========================================================================
    // HELPERS DE TABELA
    // =========================================================================

    private JTable novaTabela(String[] cols, int[] widths) {
        DefaultTableModel m = new DefaultTableModel(cols,0) {
            @Override public boolean isCellEditable(int r,int c) { return false; }
        };
        JTable t = new JTable(m);
        t.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setBackground(AppTheme.COR_SUPERFICIE); t.setForeground(AppTheme.COR_TEXTO);
        t.setGridColor(new Color(60,60,65)); t.setFont(new Font("Segoe UI",Font.PLAIN,13));
        t.setRowHeight(36); t.setSelectionBackground(AppTheme.COR_PRIMARIA);
        t.setSelectionForeground(Color.WHITE); t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false); t.setIntercellSpacing(new Dimension(0,1));
        t.setFillsViewportHeight(true);
        JTableHeader th = t.getTableHeader();
        th.setBackground(new Color(30,30,32)); th.setForeground(AppTheme.COR_TEXTO_SEC);
        th.setFont(new Font("Segoe UI",Font.BOLD,12)); th.setReorderingAllowed(false);
        th.setPreferredSize(new Dimension(0,38));
        th.setBorder(BorderFactory.createMatteBorder(0,0,2,0,AppTheme.COR_PRIMARIA));
        t.getColumnModel().getColumn(0).setMinWidth(0); t.getColumnModel().getColumn(0).setMaxWidth(0); t.getColumnModel().getColumn(0).setWidth(0);
        if (widths != null) {
            for (int i = 0; i < widths.length && (i+1) < cols.length; i++)
                t.getColumnModel().getColumn(i+1).setPreferredWidth(widths[i]);
        }
        // Renderer com wrap de texto - altura ajustada em ajustarAlturas(), NAO aqui
        TableCellRenderer rend = new TableCellRenderer() {
            private final JTextArea area = new JTextArea();
            { area.setLineWrap(true); area.setWrapStyleWord(true); area.setOpaque(true); area.setBorder(BorderFactory.createEmptyBorder(4,10,4,10)); }
            @Override public Component getTableCellRendererComponent(JTable tbl,Object val,boolean sel,boolean foc,int row,int col) {
                String txt = val != null ? val.toString() : "";
                area.setText(txt);
                area.setToolTipText(txt.length() > 60 ? "<html><body style='width:400px'>"+txt.replace("\n","<br>")+"</body></html>" : null);
                area.setFont(tbl.getFont());
                if (sel) { area.setBackground(tbl.getSelectionBackground()); area.setForeground(tbl.getSelectionForeground()); }
                else { area.setBackground(row%2==0 ? AppTheme.COR_SUPERFICIE : new Color(38,38,42)); area.setForeground(AppTheme.COR_TEXTO); }
                return area;
            }
        };
        for (int i = 0; i < cols.length; i++) t.getColumnModel().getColumn(i).setCellRenderer(rend);
        return t;
    }

    private void ajustarAlturas(JTable t) {
        if (t.getWidth() <= 0) return;
        JTextArea calc = new JTextArea(); calc.setLineWrap(true); calc.setWrapStyleWord(true); calc.setFont(t.getFont());
        calc.setBorder(BorderFactory.createEmptyBorder(4,10,4,10));
        for (int row = 0; row < t.getRowCount(); row++) {
            int maxH = 36;
            for (int col = 1; col < t.getColumnCount(); col++) {
                Object v = t.getValueAt(row,col); if (v == null) continue;
                int cw = t.getColumnModel().getColumn(col).getWidth(); if (cw <= 0) continue;
                calc.setText(v.toString()); calc.setSize(new Dimension(cw,Short.MAX_VALUE));
                int h = calc.getPreferredSize().height + 8;
                if (h > maxH) maxH = h;
            }
            if (t.getRowHeight(row) != maxH) t.setRowHeight(row,maxH);
        }
    }

    private JPanel aba(JTable tabela, Runnable onRefresh, JButton... botoes) {
        JPanel p = new JPanel(new BorderLayout(0,0));
        p.setBackground(AppTheme.COR_FUNDO); p.setBorder(BorderFactory.createEmptyBorder(8,0,0,0));
        // Barra de busca
        JPanel barraBusca = new JPanel(new BorderLayout(8,0));
        barraBusca.setBackground(new Color(28,28,30));
        barraBusca.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(55,55,60)),BorderFactory.createEmptyBorder(7,10,7,10)));
        JLabel lblB = new JLabel("Buscar:"); lblB.setForeground(AppTheme.COR_TEXTO_SEC); lblB.setFont(new Font("Segoe UI",Font.PLAIN,12));
        JTextField txtB = new JTextField();
        txtB.setFont(new Font("Segoe UI",Font.PLAIN,13)); txtB.setForeground(AppTheme.COR_TEXTO);
        txtB.setBackground(new Color(44,44,48)); txtB.setCaretColor(AppTheme.COR_TEXTO);
        txtB.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(70,70,75),1),BorderFactory.createEmptyBorder(5,10,5,10)));
        JButton btnX = new JButton("X"); btnX.setFont(new Font("Segoe UI",Font.BOLD,11));
        btnX.setForeground(AppTheme.COR_TEXTO_SEC); btnX.setBackground(new Color(60,60,65));
        btnX.setBorder(BorderFactory.createEmptyBorder(5,10,5,10)); btnX.setFocusPainted(false);
        btnX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        barraBusca.add(lblB,BorderLayout.WEST); barraBusca.add(txtB,BorderLayout.CENTER); barraBusca.add(btnX,BorderLayout.EAST);
        p.add(barraBusca,BorderLayout.NORTH);
        // Filtro em tempo real
        txtB.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            void filtrar() {
                String termo = txtB.getText().trim();
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel)tabela.getModel());
                tabela.setRowSorter(sorter);
                sorter.setRowFilter(termo.isEmpty() ? null : RowFilter.regexFilter("(?i)"+java.util.regex.Pattern.quote(termo)));
            }
        });
        btnX.addActionListener(e -> { txtB.setText(""); tabela.setRowSorter(null); });
        // Scroll
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.getViewport().setBackground(AppTheme.COR_SUPERFICIE);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(55,55,60),1));
        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() { thumbColor=AppTheme.COR_PRIMARIA; trackColor=new Color(30,30,32); }
            @Override protected JButton createDecreaseButton(int o) { JButton b=new JButton(); b.setPreferredSize(new Dimension(0,0)); return b; }
            @Override protected JButton createIncreaseButton(int o) { JButton b=new JButton(); b.setPreferredSize(new Dimension(0,0)); return b; }
        });
        p.add(scroll,BorderLayout.CENTER);
        // Barra de botoes
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT,8,8));
        bar.setBackground(new Color(28,28,30)); bar.setBorder(BorderFactory.createMatteBorder(1,0,0,0,new Color(55,55,60)));
        for (JButton b : botoes) bar.add(b);
        if (onRefresh != null) {
            JButton btnAt = AppTheme.botaoSecundario("Atualizar");
            btnAt.setForeground(new Color(48,209,88));
            btnAt.addActionListener(e -> { btnAt.setEnabled(false); btnAt.setText("..."); new SwingWorker<Void,Void>() { @Override protected Void doInBackground() { onRefresh.run(); return null; } @Override protected void done() { btnAt.setEnabled(true); btnAt.setText("Atualizar"); } }.execute(); });
            bar.add(Box.createHorizontalStrut(8)); bar.add(btnAt);
            bar.add(Box.createHorizontalStrut(16));
            JButton btnSairAba = AppTheme.botaoDanger("Sair do Sistema");
            btnSairAba.addActionListener(ev -> System.exit(0));
            bar.add(btnSairAba);
            JButton btnCsv=AppTheme.botaoSecundario("Exportar CSV");
            btnCsv.setForeground(new Color(100,220,180));
            btnCsv.addActionListener(ev->exportarCSV(tabela,"exportacao"));
            bar.add(btnCsv);
        }
        p.add(bar,BorderLayout.SOUTH);
        return p;
    }

    // =========================================================================
    // HELPERS DE FORMULARIO
    // =========================================================================

    private JPanel formPanel() { JPanel p=new JPanel(new GridBagLayout()); p.setBackground(AppTheme.COR_SUPERFICIE); p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12)); return p; }

    private void addRow(JPanel p,String label,JComponent field) {
        int row=p.getComponentCount()/2;
        GridBagConstraints lc=new GridBagConstraints(); lc.gridx=0; lc.gridy=row; lc.anchor=GridBagConstraints.WEST; lc.insets=new Insets(5,4,5,10);
        JLabel lbl=new JLabel(label); lbl.setForeground(AppTheme.COR_TEXTO_SEC); lbl.setFont(new Font("Segoe UI",Font.PLAIN,13)); p.add(lbl,lc);
        GridBagConstraints fc=new GridBagConstraints(); fc.gridx=1; fc.gridy=row; fc.fill=GridBagConstraints.HORIZONTAL; fc.weightx=1.0; fc.insets=new Insets(5,0,5,4); fc.ipadx=200; p.add(field,fc);
    }

    private int showForm(JPanel p,String titulo) {
        JPanel w=new JPanel(new BorderLayout()); w.setBackground(AppTheme.COR_SUPERFICIE);
        JLabel lT=new JLabel("   "+titulo); lT.setFont(new Font("Segoe UI",Font.BOLD,14)); lT.setForeground(Color.WHITE); lT.setOpaque(true); lT.setBackground(AppTheme.COR_PRIMARIA); lT.setBorder(BorderFactory.createEmptyBorder(12,14,12,14));
        JScrollPane sc=new JScrollPane(p); sc.setBorder(BorderFactory.createEmptyBorder()); sc.getViewport().setBackground(AppTheme.COR_SUPERFICIE); sc.setPreferredSize(new Dimension(480,Math.min(p.getPreferredSize().height+50,520)));
        w.add(lT,BorderLayout.NORTH); w.add(sc,BorderLayout.CENTER);
        UIManager.put("OptionPane.background",AppTheme.COR_SUPERFICIE); UIManager.put("Panel.background",AppTheme.COR_SUPERFICIE);
        return JOptionPane.showConfirmDialog(this,w,titulo,JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
    }

    private JTextField campo(String val) {
        JTextField f=new JTextField(val!=null?val:""); f.setFont(new Font("Segoe UI",Font.PLAIN,13)); f.setForeground(AppTheme.COR_TEXTO); f.setBackground(new Color(44,44,48)); f.setCaretColor(AppTheme.COR_TEXTO);
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(70,70,75),1),BorderFactory.createEmptyBorder(7,10,7,10))); return f;
    }

    private JTextArea area(String val) {
        JTextArea a=new JTextArea(val!=null?val:"",3,20); a.setFont(new Font("Segoe UI",Font.PLAIN,13)); a.setForeground(AppTheme.COR_TEXTO); a.setBackground(new Color(44,44,48)); a.setCaretColor(AppTheme.COR_TEXTO);
        a.setBorder(BorderFactory.createEmptyBorder(7,10,7,10)); a.setLineWrap(true); a.setWrapStyleWord(true); return a;
    }

    private JComboBox<String> combo(String[] ops,String sel) {
        JComboBox<String> cb=new JComboBox<>(ops); cb.setBackground(new Color(44,44,48)); cb.setForeground(AppTheme.COR_TEXTO); cb.setFont(new Font("Segoe UI",Font.PLAIN,13)); if(sel!=null) cb.setSelectedItem(sel); return cb;
    }

    private JComboBox<Viatura> comboViatura(List<Viatura> lista, Viatura sel) {
        // Ordena por prefixo
        lista.sort((a,b) -> { if(a.getPrefixo()==null) return -1; if(b.getPrefixo()==null) return 1; return a.getPrefixo().compareToIgnoreCase(b.getPrefixo()); });
        JComboBox<Viatura> cb = new JComboBox<>(lista.toArray(new Viatura[0]));
        cb.setRenderer((l,v,i,s,f) -> new JLabel(v==null?"":v.getPrefixo()+"  \u2014  "+v.getPlaca()));
        cb.setBackground(new Color(44,44,48)); cb.setForeground(AppTheme.COR_TEXTO); cb.setFont(new Font("Segoe UI",Font.PLAIN,13));
        // Seleciona a viatura correta pelo ID
        if(sel!=null && sel.getId()!=null){
            for(int i=0;i<cb.getItemCount();i++){
                Viatura v=cb.getItemAt(i);
                if(v!=null && sel.getId().equals(v.getId())){ cb.setSelectedIndex(i); break; }
            }
        }
        // Busca por letra: pressionar mesma letra avanca para proxima com aquela letra
        final int[] lastIdx = {-1};
        final char[] lastChar = {0};
        cb.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyTyped(java.awt.event.KeyEvent e) {
                char c = Character.toUpperCase(e.getKeyChar());
                if(!Character.isLetterOrDigit(c)) return;
                int start = (c == lastChar[0]) ? lastIdx[0]+1 : 0;
                lastChar[0] = c;
                for(int i=start;i<cb.getItemCount();i++){
                    Viatura v=cb.getItemAt(i);
                    if(v!=null && v.getPrefixo()!=null && Character.toUpperCase(v.getPrefixo().charAt(0))==c){
                        cb.setSelectedIndex(i); lastIdx[0]=i; return;
                    }
                }
                // Volta ao inicio se nao achou apos o start
                for(int i=0;i<start;i++){
                    Viatura v=cb.getItemAt(i);
                    if(v!=null && v.getPrefixo()!=null && Character.toUpperCase(v.getPrefixo().charAt(0))==c){
                        cb.setSelectedIndex(i); lastIdx[0]=i; return;
                    }
                }
            }
        });
        return cb;
    }

    private Long idSelecionado(JTable t) { int r=t.getSelectedRow(); if(r<0) return null; int mr=t.convertRowIndexToModel(r); return (Long)t.getModel().getValueAt(mr,0); }
    private boolean confirmar(String msg) { return JOptionPane.showConfirmDialog(this,msg,"Confirmar",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION; }
    private void info(String msg) { JOptionPane.showMessageDialog(this,msg,"Aten\u00e7\u00e3o",JOptionPane.INFORMATION_MESSAGE); }
    private void erro(String msg) { JOptionPane.showMessageDialog(this,msg,"Erro",JOptionPane.ERROR_MESSAGE); }

    /** Imprime a tabela na impressora selecionada pelo usuario. */
    private void imprimirTabela(JTable tabela) {
        try {
            boolean printed = tabela.print(JTable.PrintMode.FIT_WIDTH,
                new java.text.MessageFormat("Relat\u00f3rio - 4\u00ba BBM"),
                new java.text.MessageFormat("P\u00e1gina {0}"),
                true, null, true);
            if (!printed) return; // usuario cancelou
        } catch (java.awt.print.PrinterAbortException ex) {
            // cancelado silenciosamente
        } catch (java.awt.print.PrinterException ex) {
            String msg = ex.getMessage();
            if (msg != null && !msg.trim().isEmpty()) erro("Erro ao imprimir: " + msg);
        } catch (Exception ex) {
            String msg = ex.getMessage();
            if (msg != null && !msg.trim().isEmpty()) erro("Erro: " + msg);
        }
    }

    /** Exporta a tabela para PDF via impressora PDF virtual do Windows. */
    private void gerarPDF(JTable tabela) {
        // Escolher onde salvar o PDF
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Salvar PDF");
        fc.setSelectedFile(new java.io.File("relatorio.pdf"));
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF (*.pdf)", "pdf"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        java.io.File arquivo = fc.getSelectedFile();
        if (!arquivo.getName().toLowerCase().endsWith(".pdf"))
            arquivo = new java.io.File(arquivo.getAbsolutePath() + ".pdf");
        // Gerar PDF via impressora PDF virtual do Windows (Microsoft Print to PDF)
        javax.print.PrintService pdfService = null;
        for (javax.print.PrintService ps : java.awt.print.PrinterJob.lookupPrintServices()) {
            if (ps.getName().toLowerCase().contains("pdf")) { pdfService = ps; break; }
        }
        if (pdfService == null) {
            // Fallback: usar impressora padrao com dialogo
            try {
                tabela.print(JTable.PrintMode.FIT_WIDTH,
                    new java.text.MessageFormat("Relat\u00f3rio - 4\u00ba BBM"),
                    new java.text.MessageFormat("P\u00e1gina {0}"), true, null, false);
            } catch (Exception ex) { if (ex.getMessage() != null) erro(ex.getMessage()); }
            return;
        }
        try {
            java.awt.print.PrinterJob job = java.awt.print.PrinterJob.getPrinterJob();
            job.setPrintService(pdfService);
            // Definir destino do arquivo
            javax.print.attribute.PrintRequestAttributeSet attrs = new javax.print.attribute.HashPrintRequestAttributeSet();
            attrs.add(new javax.print.attribute.standard.Destination(arquivo.toURI()));
            attrs.add(javax.print.attribute.standard.OrientationRequested.LANDSCAPE);
            job.setPrintable(tabela.getPrintable(JTable.PrintMode.FIT_WIDTH,
                new java.text.MessageFormat("Relat\u00f3rio - 4\u00ba BBM"),
                new java.text.MessageFormat("P\u00e1gina {0}")));
            job.print(attrs);
            JOptionPane.showMessageDialog(this, "PDF salvo em:\n" + arquivo.getAbsolutePath(), "PDF Gerado", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            if (ex.getMessage() != null && !ex.getMessage().trim().isEmpty())
                erro("Erro ao gerar PDF: " + ex.getMessage());
        }
    }

    // =========================================================================
    // BADGE RENDERERS
    // =========================================================================

    private DefaultTableCellRenderer badgeSituacao() {
        return new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tbl,Object val,boolean sel,boolean foc,int row,int col) {
                super.getTableCellRendererComponent(tbl,val,sel,foc,row,col);
                String v=val!=null?val.toString():"";
                if(!sel) { setBackground(row%2==0?AppTheme.COR_SUPERFICIE:new Color(38,38,42));
                    if("Dispon\u00edvel".equals(v)) setForeground(new Color(48,209,88));
                    else if("Baixada".equals(v)) setForeground(new Color(255,69,58));
                    else if("Descarga".equals(v)) setForeground(new Color(255,159,10));
                    else if("Em manuten\u00e7\u00e3o".equals(v)) setForeground(new Color(100,180,255));
                    else setForeground(AppTheme.COR_TEXTO); }
                setFont(new Font("Segoe UI",Font.BOLD,12)); setBorder(BorderFactory.createEmptyBorder(0,10,0,10)); return this;
            }
        };
    }

    private DefaultTableCellRenderer badgeStatus() {
        return new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tbl,Object val,boolean sel,boolean foc,int row,int col) {
                super.getTableCellRendererComponent(tbl,val,sel,foc,row,col);
                String v=val!=null?val.toString():"";
                if(!sel) { setBackground(row%2==0?AppTheme.COR_SUPERFICIE:new Color(38,38,42));
                    if("Conclu\u00eddo".equals(v)) setForeground(new Color(48,209,88));
                    else if("Em andamento".equals(v)) setForeground(new Color(255,159,10));
                    else if("Aguardando pe\u00e7as".equals(v)) setForeground(new Color(255,69,58));
                    else setForeground(new Color(100,180,255)); }
                setFont(new Font("Segoe UI",Font.BOLD,12)); setBorder(BorderFactory.createEmptyBorder(0,10,0,10)); return this;
            }
        };
    }

    private DefaultTableCellRenderer badgeVinculo() {
        return new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tbl,Object val,boolean sel,boolean foc,int row,int col) {
                super.getTableCellRendererComponent(tbl,val,sel,foc,row,col);
                String v=val!=null?val.toString():"";
                if(!sel) { setBackground(row%2==0?AppTheme.COR_SUPERFICIE:new Color(38,38,42)); setForeground("Vinculado".equals(v)?new Color(48,209,88):new Color(255,159,10)); }
                setFont(new Font("Segoe UI",Font.BOLD,12)); setBorder(BorderFactory.createEmptyBorder(0,10,0,10)); return this;
            }
        };
    }

    // =========================================================================
    // ABA VIATURAS
    // =========================================================================

    private JPanel painelViaturas() {
        tViaturas = novaTabela(new String[]{"ID","Prefixo","Placa","Modelo","Sede / Fra\u00e7\u00e3o","KM","Situa\u00e7\u00e3o","Motivo Baixa/Descarga"},new int[]{110,110,160,200,80,110,180});
        tViaturas.getColumnModel().getColumn(6).setCellRenderer(badgeSituacao());
        JButton btnAdd=AppTheme.botaoPrimario("+ Adicionar"); JButton btnEdit=AppTheme.botaoSecundario("Editar"); JButton btnDel=AppTheme.botaoDanger("Excluir");
        btnAdd.addActionListener(e->formViatura(null));
        btnEdit.addActionListener(e->{ Long id=idSelecionado(tViaturas); if(id==null){info("Selecione uma viatura.");return;} try{formViatura(viaturaDao.buscarPorId(id));}catch(Exception ex){erro(ex.getMessage());} });
        btnDel.addActionListener(e->{ Long id=idSelecionado(tViaturas); if(id==null){info("Selecione uma viatura.");return;} if(confirmar("Excluir viatura?")) try{viaturaDao.deletar(id);carregarViaturas();}catch(Exception ex){erro(ex.getMessage());} });
        JButton btnPdf=AppTheme.botaoSecundario("Gerar PDF"); btnPdf.setForeground(new Color(255,120,120)); btnPdf.addActionListener(e->gerarPDF(tViaturas));
        JButton btnImprtViaturas=AppTheme.botaoSecundario("Imprimir"); btnImprtViaturas.setForeground(new Color(100,180,255)); btnImprtViaturas.addActionListener(e->imprimirTabela(tViaturas));
        return aba(tViaturas,this::carregarViaturas,btnAdd,btnEdit,btnDel,btnPdf,btnImprtViaturas);
    }

    private void formViatura(Viatura v) {
        boolean novo=(v==null); if(novo) v=new Viatura();
        JPanel p=formPanel();
        JTextField fPref=campo(v.getPrefixo()); JTextField fPlaca=campo(v.getPlaca()); JTextField fMod=campo(v.getModelo());
        JComboBox<String> cbSede=combo(new String[]{"4\u00ba BBM - Juiz de Fora","PEMAD","3\u00aa CIA PV","6\u00ba PEL SUL","Ub\u00e1","Muriae","Al\u00e9m Para\u00edba","Cataguases","COB","Vi\u00e7osa","Leopoldina"},v.getSedeFracao());
        JTextField fKm=campo(v.getKm()!=null?String.valueOf(v.getKm()):"");
        JComboBox<String> cbSit=combo(new String[]{"Dispon\u00edvel","Baixada","Descarga","Em manuten\u00e7\u00e3o"},v.getSituacao());
        JTextField fMotivo=campo(v.getMotivoBaixaDescarga());
        addRow(p,"Prefixo *",fPref); addRow(p,"Placa",fPlaca); addRow(p,"Modelo",fMod); addRow(p,"Sede/Fra\u00e7\u00e3o",cbSede); addRow(p,"KM",fKm); addRow(p,"Situa\u00e7\u00e3o",cbSit); addRow(p,"Motivo Baixa/Descarga",fMotivo);
        if(showForm(p,novo?"Nova Viatura":"Editar Viatura")!=JOptionPane.OK_OPTION) return;
        if(fPref.getText().trim().isEmpty()){erro("Prefixo \u00e9 obrigat\u00f3rio.");return;}
        v.setPrefixo(fPref.getText().trim()); v.setPlaca(fPlaca.getText().trim()); v.setModelo(fMod.getText().trim()); v.setSedeFracao((String)cbSede.getSelectedItem());
        try{v.setKm(fKm.getText().trim().isEmpty()?null:Double.parseDouble(fKm.getText().trim()));}catch(NumberFormatException ex){erro("KM inv\u00e1lido.");return;}
        v.setSituacao((String)cbSit.getSelectedItem());
        v.setMotivoBaixaDescarga(fMotivo.getText().trim().isEmpty()?null:fMotivo.getText().trim());
        try{if(novo)viaturaDao.salvar(v);else viaturaDao.atualizar(v);carregarViaturas();}catch(Exception ex){erro(ex.getMessage());}
    }

    // =========================================================================
    // ABA REVISOES
    // =========================================================================

    private JPanel painelRevisoes() {
        tRevisoes=novaTabela(new String[]{"ID","Viatura","Placa","Modelo","Sede/Fra\u00e7\u00e3o","Pr\u00f3xima Revis\u00e3o","KM","Situa\u00e7\u00e3o","Onde foi feito","Hist\u00f3rico","Garantia"},new int[]{130,90,100,120,130,60,80,120,300,120});
        tRevisoes.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(JTable t,Object val,boolean sel,boolean foc,int row,int col){
                super.getTableCellRendererComponent(t,val,sel,foc,row,col);
                setText(val!=null?val.toString():"");
                setFont(new Font("Segoe UI",Font.BOLD,12));
                setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
                if(!sel && val!=null){
                    String txt=val.toString();
                    java.time.LocalDate data=null;
                    java.util.regex.Matcher m=java.util.regex.Pattern.compile("(\\d{2}/\\d{2}/\\d{4})").matcher(txt);
                    if(m.find()){try{data=java.time.LocalDate.parse(m.group(1),java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));}catch(Exception e){}}
                    if(data!=null){
                        java.time.LocalDate hoje=java.time.LocalDate.now();
                        long dias=java.time.temporal.ChronoUnit.DAYS.between(hoje,data);
                        if(dias<0){setBackground(new Color(180,30,30));setForeground(Color.WHITE);}
                        else if(dias<=60){setBackground(new Color(200,160,0));setForeground(Color.BLACK);}
                        else{setBackground(row%2==0?AppTheme.COR_SUPERFICIE:new Color(38,38,42));setForeground(AppTheme.COR_TEXTO);}
                    } else {setBackground(row%2==0?AppTheme.COR_SUPERFICIE:new Color(38,38,42));setForeground(AppTheme.COR_TEXTO);}
                }
                return this;
            }
        });
        JButton btnAdd=AppTheme.botaoPrimario("+ Adicionar"); JButton btnEdit=AppTheme.botaoSecundario("Editar"); JButton btnDel=AppTheme.botaoDanger("Excluir");
        btnAdd.addActionListener(e->formRevisao(null));
        btnEdit.addActionListener(e->{ Long id=idSelecionado(tRevisoes); if(id==null){info("Selecione uma revis\u00e3o.");return;} try{formRevisao(revisaoDao.buscarPorId(id));}catch(Exception ex){erro(ex.getMessage());} });
        btnDel.addActionListener(e->{ Long id=idSelecionado(tRevisoes); if(id==null){info("Selecione uma revis\u00e3o.");return;} if(confirmar("Excluir revis\u00e3o?")) try{revisaoDao.deletar(id);carregarRevisoes();}catch(Exception ex){erro(ex.getMessage());} });
        JButton btnPdftRevisoes=AppTheme.botaoSecundario("Gerar PDF"); btnPdftRevisoes.setForeground(new Color(255,120,120)); btnPdftRevisoes.addActionListener(e->gerarPDF(tRevisoes));
        JButton btnImprtRevisoes=AppTheme.botaoSecundario("Imprimir"); btnImprtRevisoes.setForeground(new Color(100,180,255)); btnImprtRevisoes.addActionListener(e->imprimirTabela(tRevisoes));
        return aba(tRevisoes,this::carregarRevisoes,btnAdd,btnEdit,btnDel,btnPdftRevisoes,btnImprtRevisoes);
    }

    private void formRevisao(Revisao r) {
        boolean novo=(r==null); if(novo) r=new Revisao();
        JPanel p=formPanel(); List<Viatura> vl; try{vl=viaturaDao.buscarTodos();}catch(Exception ex){erro(ex.getMessage());return;}
        JComboBox<Viatura> cbV=comboViatura(vl,r.getViatura());
        JTextField fProxima=campo(r.getProximaRevisao());
        JTextField fKm=campo(r.getKmRevisao()!=null?String.valueOf(r.getKmRevisao()):"");
        JTextField fSit=campo(r.getSituacaoRevisao());
        JTextField fLocal=campo(r.getLocalUltimaRevisao());
        JTextArea aFeito=area(r.getOQueFoiFeito());
        JTextField fGar=campo(r.getTempoGarantia()!=null?r.getTempoGarantia():"");
        addRow(p,"Viatura *",cbV); addRow(p,"Pr\u00f3xima Revis\u00e3o",fProxima); addRow(p,"KM",fKm); addRow(p,"Situa\u00e7\u00e3o",fSit); addRow(p,"Onde foi feito",fLocal); addRow(p,"Hist\u00f3rico",new JScrollPane(aFeito)); addRow(p,"Garantia",fGar);
        if(showForm(p,novo?"Nova Revis\u00e3o":"Editar Revis\u00e3o")!=JOptionPane.OK_OPTION) return;
        if(cbV.getSelectedItem()==null){erro("Selecione a viatura.");return;}
        r.setViatura((Viatura)cbV.getSelectedItem());
        r.setProximaRevisao(fProxima.getText().trim());
        try{r.setKmRevisao(fKm.getText().trim().isEmpty()?null:Double.parseDouble(fKm.getText().trim()));}catch(NumberFormatException ex){erro("KM inv\u00e1lido.");return;}
        r.setSituacaoRevisao(fSit.getText().trim());
        r.setLocalUltimaRevisao(fLocal.getText().trim());
        r.setOQueFoiFeito(aFeito.getText().trim());
        r.setTempoGarantia(fGar.getText().trim());
        try{if(novo)revisaoDao.salvar(r);else revisaoDao.atualizar(r);carregarRevisoes();}catch(Exception ex){erro(ex.getMessage());}
    }

    // =========================================================================
    // ABA TROCA DE OLEO
    // =========================================================================

    private JPanel painelOleo() {
        tOleo=novaTabela(new String[]{"ID","Viatura","Data","KM","Filtros","Observa\u00e7\u00e3o"},new int[]{200,100,90,180,330});
        tOleo.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(JTable t,Object val,boolean sel,boolean foc,int row,int col){
                super.getTableCellRendererComponent(t,val,sel,foc,row,col);
                setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
                if(!sel && val!=null && !val.toString().isEmpty()){
                    try{
                        java.time.LocalDate troca=java.time.LocalDate.parse(val.toString(),java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        java.time.LocalDate proxima=troca.plusYears(1);
                        long diasRestantes=java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(),proxima);
                        if(diasRestantes<=30 && diasRestantes>=0){setBackground(new Color(200,160,0));setForeground(Color.BLACK);}
                        else if(diasRestantes<0){setBackground(new Color(180,30,30));setForeground(Color.WHITE);}
                        else{setBackground(row%2==0?AppTheme.COR_SUPERFICIE:new Color(38,38,42));setForeground(AppTheme.COR_TEXTO);}
                    }catch(Exception e){setBackground(row%2==0?AppTheme.COR_SUPERFICIE:new Color(38,38,42));setForeground(AppTheme.COR_TEXTO);}
                }
                return this;
            }
        });
        JButton btnAdd=AppTheme.botaoPrimario("+ Adicionar"); JButton btnEdit=AppTheme.botaoSecundario("Editar"); JButton btnDel=AppTheme.botaoDanger("Excluir");
        btnAdd.addActionListener(e->formOleo(null));
        btnEdit.addActionListener(e->{ Long id=idSelecionado(tOleo); if(id==null){info("Selecione um registro.");return;} try{formOleo(trocaOleoDao.buscarPorId(id));}catch(Exception ex){erro(ex.getMessage());} });
        btnDel.addActionListener(e->{ Long id=idSelecionado(tOleo); if(id==null){info("Selecione um registro.");return;} if(confirmar("Excluir registro?")) try{trocaOleoDao.deletar(id);carregarOleo();}catch(Exception ex){erro(ex.getMessage());} });
        JButton btnPdftOleo=AppTheme.botaoSecundario("Gerar PDF"); btnPdftOleo.setForeground(new Color(255,120,120)); btnPdftOleo.addActionListener(e->gerarPDF(tOleo));
        JButton btnImprtOleo=AppTheme.botaoSecundario("Imprimir"); btnImprtOleo.setForeground(new Color(100,180,255)); btnImprtOleo.addActionListener(e->imprimirTabela(tOleo));
        return aba(tOleo,this::carregarOleo,btnAdd,btnEdit,btnDel,btnPdftOleo,btnImprtOleo);
    }

    private void formOleo(TrocaOleo t) {
        boolean novo=(t==null); if(novo) t=new TrocaOleo();
        JPanel p=formPanel(); List<Viatura> vl; try{vl=viaturaDao.buscarTodos();}catch(Exception ex){erro(ex.getMessage());return;}
        JComboBox<Viatura> cbV=comboViatura(vl,t.getViatura());
        JTextField fData=campo(t.getDataTroca()!=null?t.getDataTroca().format(FMT):""); JTextField fKm=campo(t.getKm()!=null?String.valueOf(t.getKm()):"");
        JTextField fFilt=campo(t.getFiltros()); JTextArea aObs=area(t.getObservacao());
        addRow(p,"Viatura *",cbV); addRow(p,"Data (dd/MM/yyyy)",fData); addRow(p,"KM",fKm); addRow(p,"Filtros",fFilt); addRow(p,"Observa\u00e7\u00e3o",new JScrollPane(aObs));
        if(showForm(p,novo?"Nova Troca de \u00d3leo":"Editar Troca de \u00d3leo")!=JOptionPane.OK_OPTION) return;
        if(cbV.getSelectedItem()==null){erro("Selecione a viatura.");return;}
        t.setViatura((Viatura)cbV.getSelectedItem());
        try{t.setDataTroca(fData.getText().trim().isEmpty()?null:LocalDate.parse(fData.getText().trim(),FMT));}catch(DateTimeParseException ex){erro("Data inv\u00e1lida.");return;}
        try{t.setKm(fKm.getText().trim().isEmpty()?null:Double.parseDouble(fKm.getText().trim()));}catch(NumberFormatException ex){erro("KM inv\u00e1lido.");return;}
        t.setFiltros(fFilt.getText().trim()); t.setObservacao(aObs.getText().trim());
        try{if(novo)trocaOleoDao.salvar(t);else trocaOleoDao.atualizar(t);carregarOleo();}catch(Exception ex){erro(ex.getMessage());}
    }

    // =========================================================================
    // ABA ACIDENTES
    // =========================================================================

    private JPanel painelAcidentes() {
        tAcidentes=novaTabela(new String[]{"ID","Data","Viatura","Motorista","Parte Prejudicada","Local","REDS","SEI","Status"},new int[]{90,160,160,140,140,80,80,130});
        tAcidentes.getColumnModel().getColumn(8).setCellRenderer(badgeStatus());
        JButton btnAdd=AppTheme.botaoPrimario("+ Adicionar"); JButton btnEdit=AppTheme.botaoSecundario("Editar"); JButton btnDel=AppTheme.botaoDanger("Excluir");
        btnAdd.addActionListener(e->formAcidente(null));
        btnEdit.addActionListener(e->{ Long id=idSelecionado(tAcidentes); if(id==null){info("Selecione um acidente.");return;} try{formAcidente(acidenteDao.buscarPorId(id));}catch(Exception ex){erro(ex.getMessage());} });
        btnDel.addActionListener(e->{ Long id=idSelecionado(tAcidentes); if(id==null){info("Selecione um acidente.");return;} if(confirmar("Excluir acidente?")) try{acidenteDao.deletar(id);carregarAcidentes();}catch(Exception ex){erro(ex.getMessage());} });
        JButton btnPdftAcidentes=AppTheme.botaoSecundario("Gerar PDF"); btnPdftAcidentes.setForeground(new Color(255,120,120)); btnPdftAcidentes.addActionListener(e->gerarPDF(tAcidentes));
        JButton btnImprtAcidentes=AppTheme.botaoSecundario("Imprimir"); btnImprtAcidentes.setForeground(new Color(100,180,255)); btnImprtAcidentes.addActionListener(e->imprimirTabela(tAcidentes));
        return aba(tAcidentes,this::carregarAcidentes,btnAdd,btnEdit,btnDel,btnPdftAcidentes,btnImprtAcidentes);
    }

    private void formAcidente(Acidente a) {
        boolean novo=(a==null); if(novo) a=new Acidente();
        JPanel p=formPanel(); List<Viatura> vl; try{vl=viaturaDao.buscarTodos();}catch(Exception ex){erro(ex.getMessage());return;}
        JTextField fData=campo(a.getDataAcidente()!=null?a.getDataAcidente().format(FMT):""); JComboBox<Viatura> cbV=comboViatura(vl,a.getViatura());
        JTextField fMotorista=campo(a.getMotorista());
        JTextField fParte=campo(a.getPartePrejudicada()); JTextField fLocal=campo(a.getLocal()); JTextField fReds=campo(a.getReds()); JTextField fSei=campo(a.getSei());
        JComboBox<String> cbSt=combo(new String[]{"Em andamento","Conclu\u00eddo","Aguardando pe\u00e7as","Aguardando laudo"},a.getStatusAndamento());
        addRow(p,"Data (dd/MM/yyyy)",fData); addRow(p,"Viatura *",cbV); addRow(p,"Motorista",fMotorista); addRow(p,"Parte Prejudicada",fParte); addRow(p,"Local",fLocal); addRow(p,"REDS",fReds); addRow(p,"SEI",fSei); addRow(p,"Status",cbSt);
        if(showForm(p,novo?"Novo Acidente":"Editar Acidente")!=JOptionPane.OK_OPTION) return;
        if(cbV.getSelectedItem()==null){erro("Selecione a viatura.");return;}
        try{a.setDataAcidente(fData.getText().trim().isEmpty()?null:LocalDate.parse(fData.getText().trim(),FMT));}catch(DateTimeParseException ex){erro("Data inv\u00e1lida.");return;}
        a.setViatura((Viatura)cbV.getSelectedItem()); a.setMotorista(fMotorista.getText().trim()); a.setPartePrejudicada(fParte.getText().trim()); a.setLocal(fLocal.getText().trim()); a.setReds(fReds.getText().trim()); a.setSei(fSei.getText().trim()); a.setStatusAndamento((String)cbSt.getSelectedItem());
        try{if(novo)acidenteDao.salvar(a);else acidenteDao.atualizar(a);carregarAcidentes();}catch(Exception ex){erro(ex.getMessage());}
    }

    // =========================================================================
    // ABA CONVENIOS
    // =========================================================================

    private JPanel painelConvenios() {
        tConvenios = new JTable(new DefaultTableModel(new String[]{"ID","N\u00ba Conv\u00eanio","Part\u00edcipe","Tipo","Objeto","Recurso","Benef\u00edcios","Valor","In\u00edcio","T\u00e9rmino","Situa\u00e7\u00e3o","Termos Aditivos","N\u00ba Processo SEI","Informa\u00e7\u00f5es Adicionais","_flag"},0){@Override public boolean isCellEditable(int r,int c){return false;}});
        tConvenios.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); tConvenios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tConvenios.setBackground(AppTheme.COR_SUPERFICIE); tConvenios.setForeground(AppTheme.COR_TEXTO); tConvenios.setGridColor(new Color(60,60,65));
        tConvenios.setFont(new Font("Segoe UI",Font.PLAIN,13)); tConvenios.setRowHeight(36); tConvenios.setSelectionBackground(AppTheme.COR_PRIMARIA); tConvenios.setSelectionForeground(Color.WHITE);
        tConvenios.setShowHorizontalLines(true); tConvenios.setShowVerticalLines(false); tConvenios.setIntercellSpacing(new Dimension(0,1)); tConvenios.setFillsViewportHeight(true);
        JTableHeader th=tConvenios.getTableHeader(); th.setBackground(new Color(30,30,32)); th.setForeground(AppTheme.COR_TEXTO_SEC); th.setFont(new Font("Segoe UI",Font.BOLD,12)); th.setReorderingAllowed(false); th.setPreferredSize(new Dimension(0,38)); th.setBorder(BorderFactory.createMatteBorder(0,0,2,0,AppTheme.COR_PRIMARIA));
        tConvenios.getColumnModel().getColumn(0).setMinWidth(0); tConvenios.getColumnModel().getColumn(0).setMaxWidth(0); tConvenios.getColumnModel().getColumn(0).setWidth(0);
        tConvenios.getColumnModel().getColumn(14).setMinWidth(0); tConvenios.getColumnModel().getColumn(14).setMaxWidth(0); tConvenios.getColumnModel().getColumn(14).setWidth(0);
        int[] wC={130,160,90,250,120,150,100,100,100,110,200,130,300}; for(int i=0;i<wC.length;i++) tConvenios.getColumnModel().getColumn(i+1).setPreferredWidth(wC[i]);
        DefaultTableCellRenderer rendConv=new DefaultTableCellRenderer(){
            private final JTextArea area=new JTextArea();
            {area.setLineWrap(true);area.setWrapStyleWord(true);area.setOpaque(true);area.setBorder(BorderFactory.createEmptyBorder(4,10,4,10));}
            @Override public Component getTableCellRendererComponent(JTable tbl,Object val,boolean sel,boolean foc,int row,int col){
                String txt=val!=null?val.toString():"";
                area.setText(txt);
                area.setFont(tbl.getFont());
                area.setToolTipText(txt.length()>60?"<html><body style='width:400px'>"+txt.replace("\n","<br>")+"</body></html>":null);
                if(!sel){
                    String flag=String.valueOf(tbl.getModel().getValueAt(row,14));
                    if("VENCIDO".equals(flag)){area.setBackground(new Color(80,20,20));area.setForeground(new Color(255,100,100));}
                    else if("ALERTA".equals(flag)){area.setBackground(new Color(90,70,0));area.setForeground(new Color(255,220,50));}
                    else{area.setBackground(row%2==0?new Color(18,45,18):new Color(22,50,22));area.setForeground(new Color(48,209,88));}
                } else {area.setBackground(tbl.getSelectionBackground());area.setForeground(tbl.getSelectionForeground());}
                return area;
            }
        };
        for(int i=1;i<=13;i++) tConvenios.getColumnModel().getColumn(i).setCellRenderer(rendConv);
        JButton btnAdd=AppTheme.botaoPrimario("+ Adicionar"); JButton btnEdit=AppTheme.botaoSecundario("Editar"); JButton btnDel=AppTheme.botaoDanger("Excluir");
        btnAdd.addActionListener(e->formConvenio(null));
        btnEdit.addActionListener(e->{ Long id=idSelecionado(tConvenios); if(id==null){info("Selecione um conv\u00eanio.");return;} try{formConvenio(convenioDao.buscarPorId(id));}catch(Exception ex){erro(ex.getMessage());} });
        btnDel.addActionListener(e->{ Long id=idSelecionado(tConvenios); if(id==null){info("Selecione um conv\u00eanio.");return;} if(confirmar("Excluir conv\u00eanio?")) try{convenioDao.deletar(id);carregarConvenios();}catch(Exception ex){erro(ex.getMessage());} });
        JPanel legenda=new JPanel(new FlowLayout(FlowLayout.LEFT,20,7)); legenda.setBackground(new Color(28,28,30)); legenda.setBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(55,55,60)));
        JLabel lV=new JLabel("  Vigente"); lV.setForeground(new Color(48,209,88)); lV.setFont(new Font("Segoe UI",Font.BOLD,12));
        JLabel lA=new JLabel("  Vence em ate 4 meses"); lA.setForeground(new Color(255,220,50)); lA.setFont(new Font("Segoe UI",Font.BOLD,12));
        JLabel lR=new JLabel("  Vencido"); lR.setForeground(new Color(255,100,100)); lR.setFont(new Font("Segoe UI",Font.BOLD,12));
        legenda.add(lV); legenda.add(lA); legenda.add(lR);
        JPanel painel=aba(tConvenios,this::carregarConvenios,btnAdd,btnEdit,btnDel);
        painel.add(legenda,BorderLayout.NORTH);
        return painel;
    }

    private void formConvenio(Convenio c) {
        boolean novo=(c==null); if(novo) c=new Convenio();
        JPanel p=formPanel();
        JTextField fNum=campo(c.getNumeroConvenio()); JTextField fPart=campo(c.getParticipe()); JTextField fTipo=campo(c.getTipo()); JTextField fObj=campo(c.getObjeto());
        JTextField fRec=campo(c.getRecurso()); JTextField fBen=campo(c.getBeneficios()); JTextField fVal=campo(c.getValor()!=null?String.valueOf(c.getValor()):"");
        JTextField fIni=campo(c.getInicio()!=null?c.getInicio().format(FMT):""); JTextField fTer=campo(c.getTermino()!=null?c.getTermino().format(FMT):"");
        JTextField fSit=campo(c.getSituacao()); JTextField fTermos=campo(c.getTermosAditivos()); JTextField fSei=campo(c.getNumeroProcessoSei()); JTextArea aInfo=area(c.getInformacoesAdicionais());
        addRow(p,"N\u00ba Conv\u00eanio *",fNum); addRow(p,"Part\u00edcipe *",fPart); addRow(p,"Tipo",fTipo); addRow(p,"Objeto",fObj); addRow(p,"Recurso",fRec); addRow(p,"Benef\u00edcios",fBen);
        addRow(p,"Valor (R$)",fVal); addRow(p,"In\u00edcio (dd/MM/yyyy)",fIni); addRow(p,"T\u00e9rmino (dd/MM/yyyy)",fTer); addRow(p,"Situa\u00e7\u00e3o",fSit); addRow(p,"Termos Aditivos",fTermos); addRow(p,"N\u00ba Processo SEI",fSei); addRow(p,"Informa\u00e7\u00f5es Adicionais",new JScrollPane(aInfo));
        if(showForm(p,novo?"Novo Conv\u00eanio":"Editar Conv\u00eanio")!=JOptionPane.OK_OPTION) return;
        if(fNum.getText().trim().isEmpty()){erro("N\u00famero do conv\u00eanio \u00e9 obrigat\u00f3rio.");return;}
        if(fPart.getText().trim().isEmpty()){erro("Part\u00edcipe \u00e9 obrigat\u00f3rio.");return;}
        c.setNumeroConvenio(fNum.getText().trim()); c.setParticipe(fPart.getText().trim()); c.setTipo(fTipo.getText().trim()); c.setObjeto(fObj.getText().trim()); c.setRecurso(fRec.getText().trim()); c.setBeneficios(fBen.getText().trim());
        try{c.setValor(fVal.getText().trim().isEmpty()?null:Double.parseDouble(fVal.getText().trim()));}catch(NumberFormatException ex){erro("Valor inv\u00e1lido.");return;}
        try{c.setInicio(fIni.getText().trim().isEmpty()?null:LocalDate.parse(fIni.getText().trim(),FMT));}catch(DateTimeParseException ex){erro("Data de in\u00edcio inv\u00e1lida.");return;}
        try{c.setTermino(fTer.getText().trim().isEmpty()?null:LocalDate.parse(fTer.getText().trim(),FMT));}catch(DateTimeParseException ex){erro("Data de t\u00e9rmino inv\u00e1lida.");return;}
        c.setSituacao(fSit.getText().trim()); c.setTermosAditivos(fTermos.getText().trim()); c.setNumeroProcessoSei(fSei.getText().trim()); c.setInformacoesAdicionais(aInfo.getText().trim());
        try{if(novo)convenioDao.salvar(c);else convenioDao.atualizar(c);carregarConvenios();}catch(Exception ex){erro(ex.getMessage());}
    }

    // =========================================================================
    // ABA DOACOES
    // =========================================================================

    private JPanel painelDoacoes() {
        tDoacoes=novaTabela(new String[]{"ID","Item Recebido","Qtd","De Quem","N\u00ba SEI","Situa\u00e7\u00e3o"},new int[]{280,60,220,160,200});
        JButton btnAdd=AppTheme.botaoPrimario("+ Adicionar"); JButton btnEdit=AppTheme.botaoSecundario("Editar"); JButton btnDel=AppTheme.botaoDanger("Excluir");
        btnAdd.addActionListener(e->formDoacao(null));
        btnEdit.addActionListener(e->{ Long id=idSelecionado(tDoacoes); if(id==null){info("Selecione uma doa\u00e7\u00e3o.");return;} try{formDoacao(doacaoDao.buscarPorId(id));}catch(Exception ex){erro(ex.getMessage());} });
        btnDel.addActionListener(e->{ Long id=idSelecionado(tDoacoes); if(id==null){info("Selecione uma doa\u00e7\u00e3o.");return;} if(confirmar("Excluir doa\u00e7\u00e3o?")) try{doacaoDao.deletar(id);carregarDoacoes();}catch(Exception ex){erro(ex.getMessage());} });
        JButton btnPdftDoacoes=AppTheme.botaoSecundario("Gerar PDF"); btnPdftDoacoes.setForeground(new Color(255,120,120)); btnPdftDoacoes.addActionListener(e->gerarPDF(tDoacoes));
        JButton btnImprtDoacoes=AppTheme.botaoSecundario("Imprimir"); btnImprtDoacoes.setForeground(new Color(100,180,255)); btnImprtDoacoes.addActionListener(e->imprimirTabela(tDoacoes));
        return aba(tDoacoes,this::carregarDoacoes,btnAdd,btnEdit,btnDel,btnPdftDoacoes,btnImprtDoacoes);
    }

    private void formDoacao(Doacao d) {
        boolean novo=(d==null); if(novo) d=new Doacao();
        JPanel p=formPanel();
        JTextField fItem=campo(d.getItemRecebido()); JTextField fQtd=campo(d.getQuantidade()!=null?String.valueOf(d.getQuantidade()):"");
        JTextField fQuem=campo(d.getDeQuemRecebeu()); JTextField fSei=campo(d.getNumeroSei()); JTextField fSit=campo(d.getSituacaoProcesso());
        addRow(p,"Item Recebido *",fItem); addRow(p,"Quantidade",fQtd); addRow(p,"De Quem",fQuem); addRow(p,"N\u00ba SEI",fSei); addRow(p,"Situa\u00e7\u00e3o",fSit);
        if(showForm(p,novo?"Nova Doa\u00e7\u00e3o":"Editar Doa\u00e7\u00e3o")!=JOptionPane.OK_OPTION) return;
        if(fItem.getText().trim().isEmpty()){erro("Item recebido \u00e9 obrigat\u00f3rio.");return;}
        d.setItemRecebido(fItem.getText().trim());
        try{d.setQuantidade(fQtd.getText().trim().isEmpty()?null:Integer.parseInt(fQtd.getText().trim()));}catch(NumberFormatException ex){erro("Quantidade inv\u00e1lida.");return;}
        d.setDeQuemRecebeu(fQuem.getText().trim()); d.setNumeroSei(fSei.getText().trim()); d.setSituacaoProcesso(fSit.getText().trim());
        try{if(novo)doacaoDao.salvar(d);else doacaoDao.atualizar(d);carregarDoacoes();}catch(Exception ex){erro(ex.getMessage());}
    }

    // =========================================================================
    // ABA PNEUS
    // =========================================================================

    private JPanel painelPneus() {
        tPneus=novaTabela(new String[]{"ID","Medida","Quantidade","DOT / Vencimento"},new int[]{350,150,400});
        JButton btnAdd=AppTheme.botaoPrimario("+ Adicionar"); JButton btnEdit=AppTheme.botaoSecundario("Editar"); JButton btnDel=AppTheme.botaoDanger("Excluir");
        btnAdd.addActionListener(e->formPneu(null));
        btnEdit.addActionListener(e->{ Long id=idSelecionado(tPneus); if(id==null){info("Selecione um pneu.");return;} try{formPneu(pneuDao.buscarPorId(id));}catch(Exception ex){erro(ex.getMessage());} });
        btnDel.addActionListener(e->{ Long id=idSelecionado(tPneus); if(id==null){info("Selecione um pneu.");return;} if(confirmar("Excluir pneu?")) try{pneuDao.deletar(id);carregarPneus();}catch(Exception ex){erro(ex.getMessage());} });
        JButton btnPdftPneus=AppTheme.botaoSecundario("Gerar PDF"); btnPdftPneus.setForeground(new Color(255,120,120)); btnPdftPneus.addActionListener(e->gerarPDF(tPneus));
        JButton btnImprtPneus=AppTheme.botaoSecundario("Imprimir"); btnImprtPneus.setForeground(new Color(100,180,255)); btnImprtPneus.addActionListener(e->imprimirTabela(tPneus));
        return aba(tPneus,this::carregarPneus,btnAdd,btnEdit,btnDel,btnPdftPneus,btnImprtPneus);
    }

    private void formPneu(PneuEstoque pe) {
        boolean novo=(pe==null); if(novo) pe=new PneuEstoque();
        JPanel p=formPanel();
        JTextField fMed=campo(pe.getMedida()); JTextField fQtd=campo(pe.getQuantidade()!=null?String.valueOf(pe.getQuantidade()):""); JTextField fDot=campo(pe.getDotVencimento());
        addRow(p,"Medida *",fMed); addRow(p,"Quantidade",fQtd); addRow(p,"DOT/Vencimento",fDot);
        if(showForm(p,novo?"Novo Pneu":"Editar Pneu")!=JOptionPane.OK_OPTION) return;
        if(fMed.getText().trim().isEmpty()){erro("Medida \u00e9 obrigat\u00f3ria.");return;}
        pe.setMedida(fMed.getText().trim());
        try{pe.setQuantidade(fQtd.getText().trim().isEmpty()?null:Integer.parseInt(fQtd.getText().trim()));}catch(NumberFormatException ex){erro("Quantidade inv\u00e1lida.");return;}
        pe.setDotVencimento(fDot.getText().trim());
        try{if(novo)pneuDao.salvar(pe);else pneuDao.atualizar(pe);carregarPneus();}catch(Exception ex){erro(ex.getMessage());}
    }

    // =========================================================================
    // ABA BATERIAS
    // =========================================================================

    private JPanel painelBaterias() {
        tBaterias=novaTabela(new String[]{"ID","Viatura","Bateria","Garantia (meses)","Observa\u00e7\u00e3o"},new int[]{220,220,110,350});
        JButton btnAdd=AppTheme.botaoPrimario("+ Adicionar"); JButton btnEdit=AppTheme.botaoSecundario("Editar"); JButton btnDel=AppTheme.botaoDanger("Excluir");
        btnAdd.addActionListener(e->formBateria(null));
        btnEdit.addActionListener(e->{ Long id=idSelecionado(tBaterias); if(id==null){info("Selecione uma bateria.");return;} try{formBateria(bateriaDao.buscarPorId(id));}catch(Exception ex){erro(ex.getMessage());} });
        btnDel.addActionListener(e->{ Long id=idSelecionado(tBaterias); if(id==null){info("Selecione uma bateria.");return;} if(confirmar("Excluir bateria?")) try{bateriaDao.deletar(id);carregarBaterias();}catch(Exception ex){erro(ex.getMessage());} });
        JButton btnPdftBaterias=AppTheme.botaoSecundario("Gerar PDF"); btnPdftBaterias.setForeground(new Color(255,120,120)); btnPdftBaterias.addActionListener(e->gerarPDF(tBaterias));
        JButton btnImprtBaterias=AppTheme.botaoSecundario("Imprimir"); btnImprtBaterias.setForeground(new Color(100,180,255)); btnImprtBaterias.addActionListener(e->imprimirTabela(tBaterias));
        return aba(tBaterias,this::carregarBaterias,btnAdd,btnEdit,btnDel,btnPdftBaterias,btnImprtBaterias);
    }

    private void formBateria(Bateria b) {
        boolean novo=(b==null); if(novo) b=new Bateria();
        JPanel p=formPanel(); List<Viatura> vl; try{vl=viaturaDao.buscarTodos();}catch(Exception ex){erro(ex.getMessage());return;}
        JComboBox<Viatura> cbV=comboViatura(vl,b.getViatura());
        JTextField fEsp=campo(b.getEspecificacaoBateria()); JTextField fGar=campo(b.getTempoGarantiaMeses()!=null?String.valueOf(b.getTempoGarantiaMeses()):""); JTextArea aObs=area(b.getObservacao());
        addRow(p,"Viatura *",cbV); addRow(p,"Especifica\u00e7\u00e3o",fEsp); addRow(p,"Garantia (meses)",fGar); addRow(p,"Observa\u00e7\u00e3o",new JScrollPane(aObs));
        if(showForm(p,novo?"Nova Bateria":"Editar Bateria")!=JOptionPane.OK_OPTION) return;
        if(cbV.getSelectedItem()==null){erro("Selecione a viatura.");return;}
        b.setViatura((Viatura)cbV.getSelectedItem()); b.setEspecificacaoBateria(fEsp.getText().trim());
        try{b.setTempoGarantiaMeses(fGar.getText().trim().isEmpty()?null:Integer.parseInt(fGar.getText().trim()));}catch(NumberFormatException ex){erro("Garantia inv\u00e1lida.");return;}
        b.setObservacao(aObs.getText().trim());
        try{if(novo)bateriaDao.salvar(b);else bateriaDao.atualizar(b);carregarBaterias();}catch(Exception ex){erro(ex.getMessage());}
    }

    // =========================================================================
    // ABA CARTOES
    // =========================================================================

    private JPanel painelCartoes() {
        tCartoes=novaTabela(new String[]{"ID","Cart\u00e3o / Coringa","Viatura Vinculada","Situa\u00e7\u00e3o"},new int[]{300,400,200});
        tCartoes.getColumnModel().getColumn(3).setCellRenderer(badgeVinculo());
        JButton btnAdd=AppTheme.botaoPrimario("+ Adicionar"); JButton btnEdit=AppTheme.botaoSecundario("Editar"); JButton btnDel=AppTheme.botaoDanger("Excluir");
        btnAdd.addActionListener(e->formCartao(null));
        btnEdit.addActionListener(e->{ Long id=idSelecionado(tCartoes); if(id==null){info("Selecione um cart\u00e3o.");return;} try{formCartao(cartaoDao.buscarPorId(id));}catch(Exception ex){erro(ex.getMessage());} });
        btnDel.addActionListener(e->{ Long id=idSelecionado(tCartoes); if(id==null){info("Selecione um cart\u00e3o.");return;} if(confirmar("Excluir cart\u00e3o?")) try{cartaoDao.deletar(id);carregarCartoes();}catch(Exception ex){erro(ex.getMessage());} });
        JButton btnPdftCartoes=AppTheme.botaoSecundario("Gerar PDF"); btnPdftCartoes.setForeground(new Color(255,120,120)); btnPdftCartoes.addActionListener(e->gerarPDF(tCartoes));
        JButton btnImprtCartoes=AppTheme.botaoSecundario("Imprimir"); btnImprtCartoes.setForeground(new Color(100,180,255)); btnImprtCartoes.addActionListener(e->imprimirTabela(tCartoes));
        return aba(tCartoes,this::carregarCartoes,btnAdd,btnEdit,btnDel,btnPdftCartoes,btnImprtCartoes);
    }

    private void formCartao(CartaoAbastecimento ca) {
        boolean novo=(ca==null); if(ca==null) ca=new CartaoAbastecimento();
        JPanel p=formPanel(); List<Viatura> vl; try{vl=viaturaDao.buscarTodos();}catch(Exception ex){erro(ex.getMessage());return;}
        vl.add(0,null);
        JComboBox<Viatura> cbV=new JComboBox<>(vl.toArray(new Viatura[0]));
        cbV.setRenderer((l,v,i,s,f)->new JLabel(v==null?"\u2014 N\u00e3o vinculado \u2014":v.getPrefixo()+"  \u2014  "+v.getPlaca()));
        cbV.setBackground(new Color(44,44,48)); cbV.setForeground(AppTheme.COR_TEXTO); cbV.setFont(new Font("Segoe UI",Font.PLAIN,13)); cbV.setSelectedItem(ca.getViaturaVinculada());
        JTextField fNome=campo(ca.getNomeCartaoCoringa()); JComboBox<String> cbSit=combo(new String[]{"Vinculado","N\u00e3o Vinculado"},ca.getSituacao());
        addRow(p,"Nome / Coringa *",fNome); addRow(p,"Viatura Vinculada",cbV); addRow(p,"Situa\u00e7\u00e3o",cbSit);
        if(showForm(p,novo?"Novo Cart\u00e3o":"Editar Cart\u00e3o")!=JOptionPane.OK_OPTION) return;
        if(fNome.getText().trim().isEmpty()){erro("Nome do cart\u00e3o \u00e9 obrigat\u00f3rio.");return;}
        ca.setNomeCartaoCoringa(fNome.getText().trim()); ca.setViaturaVinculada((Viatura)cbV.getSelectedItem()); ca.setSituacao((String)cbSit.getSelectedItem());
        try{if(novo)cartaoDao.salvar(ca);else cartaoDao.atualizar(ca);carregarCartoes();}catch(Exception ex){erro(ex.getMessage());}
    }

    // =========================================================================
    // ABA MODELOS SEI
    // =========================================================================

    private JPanel painelModelosSei() {
        tModelosSei=novaTabela(new String[]{"ID","N\u00ba SEI","Modelo"},new int[]{250,650});
        JButton btnAdd=AppTheme.botaoPrimario("+ Adicionar"); JButton btnEdit=AppTheme.botaoSecundario("Editar"); JButton btnDel=AppTheme.botaoDanger("Excluir");
        btnAdd.addActionListener(e->formModeloSei(null));
        btnEdit.addActionListener(e->{ Long id=idSelecionado(tModelosSei); if(id==null){info("Selecione um modelo.");return;} try{formModeloSei(modeloSeiDao.buscarPorId(id));}catch(Exception ex){erro(ex.getMessage());} });
        btnDel.addActionListener(e->{ Long id=idSelecionado(tModelosSei); if(id==null){info("Selecione um modelo.");return;} if(confirmar("Excluir modelo SEI?")) try{modeloSeiDao.deletar(id);carregarModelosSei();}catch(Exception ex){erro(ex.getMessage());} });
        JButton btnPdftModelosSei=AppTheme.botaoSecundario("Gerar PDF"); btnPdftModelosSei.setForeground(new Color(255,120,120)); btnPdftModelosSei.addActionListener(e->gerarPDF(tModelosSei));
        JButton btnImprtModelosSei=AppTheme.botaoSecundario("Imprimir"); btnImprtModelosSei.setForeground(new Color(100,180,255)); btnImprtModelosSei.addActionListener(e->imprimirTabela(tModelosSei));
        return aba(tModelosSei,this::carregarModelosSei,btnAdd,btnEdit,btnDel,btnPdftModelosSei,btnImprtModelosSei);
    }

    private void formModeloSei(ModeloSei ms) {
        boolean novo=(ms==null); if(novo) ms=new ModeloSei();
        JPanel p=formPanel();
        JTextField fSei=campo(ms.getNumeroSei()); JTextField fMod=campo(ms.getModelo());
        addRow(p,"N\u00ba SEI *",fSei); addRow(p,"Modelo *",fMod);
        if(showForm(p,novo?"Novo Modelo SEI":"Editar Modelo SEI")!=JOptionPane.OK_OPTION) return;
        if(fSei.getText().trim().isEmpty()){erro("N\u00famero SEI \u00e9 obrigat\u00f3rio.");return;}
        if(fMod.getText().trim().isEmpty()){erro("Modelo \u00e9 obrigat\u00f3rio.");return;}
        ms.setNumeroSei(fSei.getText().trim()); ms.setModelo(fMod.getText().trim());
        try{if(novo)modeloSeiDao.salvar(ms);else modeloSeiDao.atualizar(ms);carregarModelosSei();}catch(Exception ex){erro(ex.getMessage());}
    }

    // =========================================================================
    // CARREGAMENTO DE DADOS
    // =========================================================================

    // =========================================================================
    // ABA SERVICOS FEITOS
    // =========================================================================

        // =========================================================================
    // ABA SITUACAO PROCESSOS SEI
    // =========================================================================

    private JPanel painelSituacaoProcessoSei() {
        tSituacaoProcessoSei=novaTabela(new String[]{"ID","Assunto","N\u00ba SEI","Situa\u00e7\u00e3o do Processo"},new int[]{60,300,200,300});
        JButton btnAdd=AppTheme.botaoPrimario("+ Adicionar"); JButton btnEdit=AppTheme.botaoSecundario("Editar"); JButton btnDel=AppTheme.botaoDanger("Excluir");
        btnAdd.addActionListener(e->formSituacaoProcessoSei(null));
        btnEdit.addActionListener(e->{ Long id=idSelecionado(tSituacaoProcessoSei); if(id==null){info("Selecione um processo.");return;} try{formSituacaoProcessoSei(situacaoProcessoSeiDao.buscarPorId(id));}catch(Exception ex){erro(ex.getMessage());} });
        btnDel.addActionListener(e->{ Long id=idSelecionado(tSituacaoProcessoSei); if(id==null){info("Selecione um processo.");return;} if(confirmar("Excluir processo SEI?")) try{situacaoProcessoSeiDao.deletar(id);carregarSituacaoProcessoSei();}catch(Exception ex){erro(ex.getMessage());} });
        JButton btnPdf=AppTheme.botaoSecundario("Gerar PDF"); btnPdf.setForeground(new Color(255,120,120)); btnPdf.addActionListener(e->gerarPDF(tSituacaoProcessoSei));
        JButton btnImpr=AppTheme.botaoSecundario("Imprimir"); btnImpr.setForeground(new Color(100,180,255)); btnImpr.addActionListener(e->imprimirTabela(tSituacaoProcessoSei));
        return aba(tSituacaoProcessoSei,this::carregarSituacaoProcessoSei,btnAdd,btnEdit,btnDel,btnPdf,btnImpr);
    }

    private void formSituacaoProcessoSei(SituacaoProcessoSei sp) {
        boolean novo=(sp==null); if(novo) sp=new SituacaoProcessoSei();
        JPanel p=formPanel();
        JTextField fAssunto=campo(sp.getAssunto()); JTextField fSei=campo(sp.getNumeroSei()); JTextField fSit=campo(sp.getSituacaoProcesso());
        addRow(p,"Assunto *",fAssunto); addRow(p,"N\u00ba SEI",fSei); addRow(p,"Situa\u00e7\u00e3o do Processo *",fSit);
        if(showForm(p,novo?"Nova Situa\u00e7\u00e3o SEI":"Editar Situa\u00e7\u00e3o SEI")!=JOptionPane.OK_OPTION) return;
        if(fAssunto.getText().trim().isEmpty()){erro("Assunto \u00e9 obrigat\u00f3rio.");return;}
        if(fSit.getText().trim().isEmpty()){erro("Situa\u00e7\u00e3o \u00e9 obrigat\u00f3rio.");return;}
        sp.setAssunto(fAssunto.getText().trim()); sp.setNumeroSei(fSei.getText().trim()); sp.setSituacaoProcesso(fSit.getText().trim());
        try{if(novo)situacaoProcessoSeiDao.salvar(sp);else situacaoProcessoSeiDao.atualizar(sp);carregarSituacaoProcessoSei();}catch(Exception ex){erro(ex.getMessage());}
    }

    private void carregarSituacaoProcessoSei() {
        DefaultTableModel m=(DefaultTableModel)tSituacaoProcessoSei.getModel(); m.setRowCount(0);
        try{ for(SituacaoProcessoSei sp:situacaoProcessoSeiDao.buscarTodos()) m.addRow(new Object[]{sp.getId(),sp.getAssunto(),sp.getNumeroSei(),sp.getSituacaoProcesso()}); }
        catch(Exception ex){erro("Erro ao carregar situa\u00e7\u00f5es SEI: "+ex.getMessage());}
        ajustarAlturas(tSituacaoProcessoSei);
    }


    private JPanel painelServicos() {
        tServicos = novaTabela(new String[]{"ID","Viatura","Problema Apresentado","Data","KM","Local de Conserto","Status"},new int[]{140,260,90,90,200,130});
        tServicos.getColumnModel().getColumn(6).setCellRenderer(badgeStatus());
        JButton btnAdd=AppTheme.botaoPrimario("+ Adicionar"); JButton btnEdit=AppTheme.botaoSecundario("Editar"); JButton btnDel=AppTheme.botaoDanger("Excluir");
        btnAdd.addActionListener(e->formServico(null));
        btnEdit.addActionListener(e->{ Long id=idSelecionado(tServicos); if(id==null){info("Selecione um servi\u00e7o.");return;} try{formServico(servicoDao.buscarPorId(id));}catch(Exception ex){erro(ex.getMessage());} });
        btnDel.addActionListener(e->{ Long id=idSelecionado(tServicos); if(id==null){info("Selecione um servi\u00e7o.");return;} if(confirmar("Excluir servi\u00e7o?")) try{servicoDao.deletar(id);carregarServicos();}catch(Exception ex){erro(ex.getMessage());} });
        JButton btnPdf=AppTheme.botaoSecundario("Gerar PDF"); btnPdf.setForeground(new Color(255,120,120)); btnPdf.addActionListener(e->gerarPDF(tServicos));
        JButton btnImpr=AppTheme.botaoSecundario("Imprimir"); btnImpr.setForeground(new Color(100,180,255)); btnImpr.addActionListener(e->imprimirTabela(tServicos));
        return aba(tServicos,this::carregarServicos,btnAdd,btnEdit,btnDel,btnPdf,btnImpr);
    }

    private void formServico(com.bbm4.model.ServicoFeito s) {
        boolean novo=(s==null); if(novo) s=new com.bbm4.model.ServicoFeito();
        JPanel p=formPanel();
        List<Viatura> viaturas; try{ viaturas=viaturaDao.buscarTodos(); }catch(Exception ex){ erro(ex.getMessage()); return; }
        JComboBox<Viatura> cbViat=comboViatura(viaturas, s.getViatura());
        JTextArea fProb=area(s.getProblemaApresentado());
        JTextField fData=campo(s.getData()!=null?s.getData().format(FMT):"");
        JTextField fKm=campo(s.getKm()!=null?String.valueOf(s.getKm()):"");
        JTextField fLocal=campo(s.getLocalConserto());
        JComboBox<String> cbSt=combo(new String[]{"Em andamento","Conclu\u00eddo","Aguardando pe\u00e7as","Aguardando or\u00e7amento"},s.getStatus());
        addRow(p,"Viatura *",cbViat); addRow(p,"Problema Apresentado",new JScrollPane(fProb)); addRow(p,"Data (dd/MM/yyyy)",fData); addRow(p,"KM",fKm); addRow(p,"Local de Conserto",fLocal); addRow(p,"Status",cbSt);
        if(showForm(p,novo?"Novo Servi\u00e7o":"Editar Servi\u00e7o")!=JOptionPane.OK_OPTION) return;
        if(cbViat.getSelectedItem()==null){erro("Selecione a viatura.");return;}
        s.setViatura((Viatura)cbViat.getSelectedItem());
        s.setProblemaApresentado(fProb.getText().trim());
        try{ s.setData(fData.getText().trim().isEmpty()?null:LocalDate.parse(fData.getText().trim(),FMT)); }catch(Exception ex){erro("Data inv\u00e1lida.");return;}
        try{ s.setKm(fKm.getText().trim().isEmpty()?null:Double.parseDouble(fKm.getText().trim())); }catch(NumberFormatException ex){erro("KM inv\u00e1lido.");return;}
        s.setLocalConserto(fLocal.getText().trim());
        s.setStatus((String)cbSt.getSelectedItem());
        try{if(novo)servicoDao.salvar(s);else servicoDao.atualizar(s);carregarServicos();}catch(Exception ex){erro(ex.getMessage());}
    }

    private void carregarServicos() {
        DefaultTableModel m=(DefaultTableModel)tServicos.getModel(); m.setRowCount(0);
        try{ for(com.bbm4.model.ServicoFeito s:servicoDao.buscarTodos()) m.addRow(new Object[]{s.getId(), s.getViatura()!=null?s.getViatura().getPrefixo()+" \u2014 "+s.getViatura().getPlaca():"", s.getProblemaApresentado(), s.getData()!=null?s.getData().format(FMT):"", s.getKm(), s.getLocalConserto(), s.getStatus()}); }
        catch(Exception ex){erro("Erro ao carregar servi\u00e7os: "+ex.getMessage());}
        ajustarAlturas(tServicos);
    }

    private void carregarTodos() {
        carregarViaturas(); carregarRevisoes(); carregarOleo(); carregarAcidentes();
        carregarConvenios(); carregarDoacoes(); carregarPneus(); carregarBaterias();
        carregarCartoes(); carregarModelosSei(); carregarSituacaoProcessoSei(); carregarServicos();
    }

    private void carregarViaturas() {
        DefaultTableModel m=(DefaultTableModel)tViaturas.getModel(); m.setRowCount(0);
        try{ for(Viatura v:viaturaDao.buscarTodos()) m.addRow(new Object[]{v.getId(),v.getPrefixo(),v.getPlaca(),v.getModelo(),v.getSedeFracao(),v.getKm(),v.getSituacao(),v.getMotivoBaixaDescarga()}); }
        catch(Exception ex){erro("Erro ao carregar viaturas: "+ex.getMessage());}
        ajustarAlturas(tViaturas);
    }

    private void carregarRevisoes() {
        DefaultTableModel m=(DefaultTableModel)tRevisoes.getModel(); m.setRowCount(0);
        try{ for(Revisao r:revisaoDao.buscarTodos()){ String vt=r.getViatura()!=null?r.getViatura().getPrefixo():""; String pl=r.getViatura()!=null?r.getViatura().getPlaca():""; String mo=r.getViatura()!=null?r.getViatura().getModelo():""; String sf=r.getViatura()!=null?r.getViatura().getSedeFracao():""; m.addRow(new Object[]{r.getId(),vt,pl,mo,sf,r.getProximaRevisao(),r.getKmRevisao(),r.getSituacaoRevisao(),r.getLocalUltimaRevisao(),r.getOQueFoiFeito(),r.getTempoGarantia()}); } }
        catch(Exception ex){erro("Erro ao carregar revis\u00f5es: "+ex.getMessage());}
        ajustarAlturas(tRevisoes);
    }

    private void carregarOleo() {
        DefaultTableModel m=(DefaultTableModel)tOleo.getModel(); m.setRowCount(0);
        try{ for(TrocaOleo t:trocaOleoDao.buscarTodos()){ String vt=t.getViatura()!=null?t.getViatura().getPrefixo()+"  "+t.getViatura().getPlaca():""; m.addRow(new Object[]{t.getId(),vt,t.getDataTroca()!=null?t.getDataTroca().format(FMT):"",t.getKm(),t.getFiltros(),t.getObservacao()}); } }
        catch(Exception ex){erro("Erro ao carregar trocas de \u00f3leo: "+ex.getMessage());}
        ajustarAlturas(tOleo);
    }

    private void carregarAcidentes() {
        DefaultTableModel m=(DefaultTableModel)tAcidentes.getModel(); m.setRowCount(0);
        try{ for(Acidente a:acidenteDao.buscarTodos()){ String vt=a.getViatura()!=null?a.getViatura().getPrefixo()+"  "+a.getViatura().getPlaca():""; m.addRow(new Object[]{a.getId(),a.getDataAcidente()!=null?a.getDataAcidente().format(FMT):"",vt,a.getMotorista(),a.getPartePrejudicada(),a.getLocal(),a.getReds(),a.getSei(),a.getStatusAndamento()}); } }
        catch(Exception ex){erro("Erro ao carregar acidentes: "+ex.getMessage());}
        ajustarAlturas(tAcidentes);
    }

    private void carregarConvenios() {
        DefaultTableModel m=(DefaultTableModel)tConvenios.getModel(); m.setRowCount(0);
        try{ for(Convenio c:convenioDao.buscarTodos()){
            String flag=c.isVencido()?"VENCIDO":c.isVencendoEmBreve()?"ALERTA":"";
            m.addRow(new Object[]{c.getId(),c.getNumeroConvenio(),c.getParticipe(),c.getTipo(),c.getObjeto(),c.getRecurso(),c.getBeneficios(),c.getValor()!=null?String.format("R$ %.2f",c.getValor()):"",c.getInicio()!=null?c.getInicio().format(FMT):"",c.getTermino()!=null?c.getTermino().format(FMT):"",c.getSituacao(),c.getTermosAditivos(),c.getNumeroProcessoSei(),c.getInformacoesAdicionais(),flag});
        } }
        catch(Exception ex){erro("Erro ao carregar conv\u00eanios: "+ex.getMessage());}
        ajustarAlturas(tConvenios);
    }

    private void carregarDoacoes() {
        DefaultTableModel m=(DefaultTableModel)tDoacoes.getModel(); m.setRowCount(0);
        try{ for(Doacao d:doacaoDao.buscarTodos()) m.addRow(new Object[]{d.getId(),d.getItemRecebido(),d.getQuantidade(),d.getDeQuemRecebeu(),d.getNumeroSei(),d.getSituacaoProcesso()}); }
        catch(Exception ex){erro("Erro ao carregar doa\u00e7\u00f5es: "+ex.getMessage());}
        ajustarAlturas(tDoacoes);
    }

    private void carregarPneus() {
        DefaultTableModel m=(DefaultTableModel)tPneus.getModel(); m.setRowCount(0);
        try{ for(PneuEstoque pe:pneuDao.buscarTodos()) m.addRow(new Object[]{pe.getId(),pe.getMedida(),pe.getQuantidade(),pe.getDotVencimento()}); }
        catch(Exception ex){erro("Erro ao carregar pneus: "+ex.getMessage());}
        ajustarAlturas(tPneus);
    }

    private void carregarBaterias() {
        DefaultTableModel m=(DefaultTableModel)tBaterias.getModel(); m.setRowCount(0);
        try{ for(Bateria b:bateriaDao.buscarTodos()){ String vt=b.getViatura()!=null?b.getViatura().getPrefixo()+"  "+b.getViatura().getPlaca():""; m.addRow(new Object[]{b.getId(),vt,b.getEspecificacaoBateria(),b.getTempoGarantiaMeses(),b.getObservacao()}); } }
        catch(Exception ex){erro("Erro ao carregar baterias: "+ex.getMessage());}
        ajustarAlturas(tBaterias);
    }

    private void carregarCartoes() {
        DefaultTableModel m=(DefaultTableModel)tCartoes.getModel(); m.setRowCount(0);
        try{ for(CartaoAbastecimento ca:cartaoDao.buscarTodos()){ String vt=ca.getViaturaVinculada()!=null?ca.getViaturaVinculada().getPrefixo()+"  \u2014  "+ca.getViaturaVinculada().getPlaca():"\u2014 N\u00e3o vinculado \u2014"; m.addRow(new Object[]{ca.getId(),ca.getNomeCartaoCoringa(),vt,ca.getSituacao()}); } }
        catch(Exception ex){erro("Erro ao carregar cart\u00f5es: "+ex.getMessage());}
        ajustarAlturas(tCartoes);
    }

    private void carregarModelosSei() {
        DefaultTableModel m=(DefaultTableModel)tModelosSei.getModel(); m.setRowCount(0);
        try{ for(ModeloSei ms:modeloSeiDao.buscarTodos()) m.addRow(new Object[]{ms.getId(),ms.getNumeroSei(),ms.getModelo()}); }
        catch(Exception ex){erro("Erro ao carregar modelos SEI: "+ex.getMessage());}
        ajustarAlturas(tModelosSei);
    }

    // =========================================================================
    // ABA DASHBOARD
    // =========================================================================

    private JPanel painelDashboard() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(AppTheme.COR_FUNDO);
        p.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // ---- Coleta de dados ----
        java.util.List<Viatura> viaturas = new java.util.ArrayList<>();
        java.util.List<com.bbm4.model.Revisao> revisoes = new java.util.ArrayList<>();
        java.util.List<Convenio> convenios = new java.util.ArrayList<>();
        java.util.List<Acidente> acidentes = new java.util.ArrayList<>();
        java.util.List<com.bbm4.model.ServicoFeito> servicos = new java.util.ArrayList<>();
        try { viaturas = viaturaDao.buscarTodos(); } catch (Exception e) {}
        try { revisoes = revisaoDao.buscarTodos(); } catch (Exception e) {}
        try { convenios = convenioDao.buscarTodos(); } catch (Exception e) {}
        try { acidentes = acidenteDao.buscarTodos(); } catch (Exception e) {}
        try { servicos = servicoDao.buscarTodos(); } catch (Exception e) {}

        // Situacao viaturas
        int vDisp=0,vBaixa=0,vDesc=0,vManut=0;
        java.util.Map<String,Integer> porSede = new java.util.LinkedHashMap<>();
        for(Viatura v:viaturas){
            String s=v.getSituacao()!=null?v.getSituacao():"";
            if(s.contains("Dispon")) vDisp++; else if(s.contains("Baixada")) vBaixa++; else if(s.contains("Descarga")) vDesc++; else vManut++;
            String sede=v.getSedeFracao()!=null?v.getSedeFracao():"Outros";
            porSede.merge(sede,1,Integer::sum);
        }
        // Revisoes: vencidas, proximas (<=60 dias), ok
        int rVenc=0,rProx=0,rOk=0;
        java.time.LocalDate hoje=java.time.LocalDate.now();
        java.time.format.DateTimeFormatter fmtD=java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for(com.bbm4.model.Revisao r:revisoes){
            String txt=r.getProximaRevisao()!=null?r.getProximaRevisao():"";
            java.util.regex.Matcher m=java.util.regex.Pattern.compile("(\\d{2}/\\d{2}/\\d{4})").matcher(txt);
            if(m.find()){try{java.time.LocalDate d=java.time.LocalDate.parse(m.group(1),fmtD);long dias=java.time.temporal.ChronoUnit.DAYS.between(hoje,d);if(dias<0)rVenc++;else if(dias<=60)rProx++;else rOk++;}catch(Exception e){rOk++;}}else rOk++;
        }
        // Convenios
        int cVig=0,cVenc=0,cAtenc=0;
        for(Convenio c:convenios){String s=c.getSituacao()!=null?c.getSituacao():"";if(s.contains("VIGENTE"))cVig++;else if(s.contains("ENCERRADO"))cVenc++;else cAtenc++;}
        // Acidentes por ano
        java.util.Map<String,Integer> acidPorAno=new java.util.LinkedHashMap<>();
        for(Acidente a:acidentes){String ano=a.getDataAcidente()!=null?String.valueOf(a.getDataAcidente().getYear()):"?";acidPorAno.merge(ano,1,Integer::sum);}
        // Servicos por status
        int sConcl=0,sAnd=0,sAg=0;
        for(com.bbm4.model.ServicoFeito s:servicos){String st=s.getStatus()!=null?s.getStatus():"";if(st.contains("onclu"))sConcl++;else if(st.contains("ndamento"))sAnd++;else sAg++;}

        final int[] dViaturas={vDisp,vBaixa,vDesc,vManut};
        final int[] dRevisoes={rOk,rProx,rVenc};
        final int[] dConvenios={cVig,cVenc,cAtenc};
        final java.util.Map<String,Integer> dAcid=acidPorAno;
        // Convenios ativos por participe (valor)
        java.util.Map<String,Double> convValores=new java.util.LinkedHashMap<>();
        for(Convenio cv:convenios){
            if(cv.isVencido()) continue;
            if(cv.getValor()==null||cv.getValor()<=0) continue;
            String part=cv.getParticipe()!=null?cv.getParticipe():"Outros";
            // Extrai cidade: remove "Prefeitura de ", "Prefeitura ", etc.
            part=part.replaceAll("(?i)prefeitura\\s+(de\\s+|do\\s+|da\\s+)?","").trim();
            if(part.length()>12) part=part.substring(0,11)+"..";
            convValores.merge(part,cv.getValor(),Double::sum);
        }
        final java.util.Map<String,Double> dConvVal=convValores;
        final java.util.Map<String,Integer> dSede=porSede;

        final int[] dServicos={sConcl,sAnd,sAg};
        JPanel cards = new JPanel(new GridLayout(1,7,10,0)); cards.setOpaque(false);
        Object[][] cardData = {
            {"Viaturas",viaturas.size(),new Color(100,180,255)},
            {"Dispon\u00edveis",vDisp,new Color(48,209,88)},
            {"Revis\u00f5es Vencidas",rVenc,new Color(255,69,58)},
            {"Conv\u00eanios Vigentes",cVig,new Color(255,220,50)},
            {"Acidentes",acidentes.size(),new Color(255,120,80)},
            {"Doa\u00e7\u00f5es",doacaoDao.buscarTodos().size(),new Color(180,100,255)},
            {"Pneus Estoque",pneuDao.buscarTodos().size(),new Color(100,220,180)}
        };
        for(Object[] cd:cardData){
            Color ci=(Color)cd[2]; String ni=(String)cd[0]; int vi=(Integer)cd[1];
            JPanel card=new JPanel(new BorderLayout()){@Override protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(AppTheme.COR_SUPERFICIE);g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14);g2.setColor(ci);g2.setStroke(new BasicStroke(2f));g2.drawRoundRect(1,1,getWidth()-2,getHeight()-2,14,14);g2.dispose();super.paintComponent(g);}};
            card.setOpaque(false);card.setBorder(BorderFactory.createEmptyBorder(14,14,14,14));
            JLabel ln=new JLabel(ni,SwingConstants.CENTER);ln.setFont(new Font("Segoe UI",Font.BOLD,12));ln.setForeground(AppTheme.COR_TEXTO_SEC);
            JLabel lv=new JLabel(String.valueOf(vi),SwingConstants.CENTER);lv.setFont(new Font("Segoe UI",Font.BOLD,40));lv.setForeground(ci);
            card.add(ln,BorderLayout.NORTH);card.add(lv,BorderLayout.CENTER);cards.add(card);
        }
        p.add(cards,BorderLayout.NORTH);

        // ---- Graficos ----
        JPanel graficos=new JPanel(new GridLayout(2,4,10,10));graficos.setOpaque(false);graficos.setPreferredSize(new Dimension(0,700));

        // 1. Pizza: Situacao Viaturas
        graficos.add(graficoPizza("Situa\u00e7\u00e3o das Viaturas",
            new String[]{"Dispon\u00edvel","Baixada","Descarga","Em Manut."},
            dViaturas,
            new Color[]{new Color(48,209,88),new Color(255,69,58),new Color(255,159,10),new Color(100,180,255)}));

        // 2. Pizza: Status Revisoes
        graficos.add(graficoPizza("Status das Revis\u00f5es",
            new String[]{"Em dia","Vence em 60d","Vencida"},
            dRevisoes,
            new Color[]{new Color(48,209,88),new Color(255,220,50),new Color(255,69,58)}));

        // 3. Pizza: Convenios
        graficos.add(graficoPizza("Conv\u00eanios",
            new String[]{"Vigente","Encerrado","Aten\u00e7\u00e3o"},
            dConvenios,
            new Color[]{new Color(48,209,88),new Color(120,120,130),new Color(255,159,10)}));

        // 4. Barras: Viaturas por Sede
        graficos.add(graficoBarras("Viaturas por Sede/Fra\u00e7\u00e3o", dSede, new Color(100,180,255)));

        // 5. Barras: Acidentes por Ano
        graficos.add(graficoBarras("Acidentes por Ano", dAcid, new Color(255,100,80)));

        // 7. Barras: Valor Convenios Ativos por Participe
        graficos.add(graficoBarrasDouble("Conv\u00eanios Ativos - Valor (R$)", dConvVal, new Color(255,220,50)));

        // 6. Pizza: Servicos por Status
        graficos.add(graficoPizza("Servi\u00e7os Realizados",new String[]{"Conclu\u00eddo","Em andamento","Aguardando"},dServicos,new Color[]{new Color(48,209,88),new Color(255,159,10),new Color(255,69,58)}));

        p.add(graficos,BorderLayout.CENTER);

        JPanel sul=new JPanel(new FlowLayout(FlowLayout.LEFT));sul.setOpaque(false);
        JButton btnAt=AppTheme.botaoPrimario("Atualizar Dashboard");
        btnAt.addActionListener(e->{
            Container parent=p.getParent();
            if(parent!=null){int idx=-1;if(parent instanceof JTabbedPane){JTabbedPane tp=(JTabbedPane)parent;for(int i=0;i<tp.getTabCount();i++){if(tp.getComponentAt(i)==p){idx=i;break;}}if(idx>=0){tp.setComponentAt(idx,painelDashboard());tp.setSelectedIndex(idx);}}}
        });
        sul.add(btnAt);p.add(sul,BorderLayout.SOUTH);
        return p;
    }

    private JPanel graficoPizza(String titulo, String[] labels, int[] vals, Color[] cores) {
        return new JPanel(){
            {setOpaque(false);setPreferredSize(new Dimension(340,280));}
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppTheme.COR_SUPERFICIE);g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14);
                g2.setColor(new Color(60,60,65));g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,14,14);
                g2.setColor(Color.WHITE);g2.setFont(new Font("Segoe UI",Font.BOLD,14));
                FontMetrics fm=g2.getFontMetrics();
                g2.drawString(titulo,(getWidth()-fm.stringWidth(titulo))/2,22);
                int total=0;for(int v:vals)total+=v;
                if(total==0){g2.setColor(AppTheme.COR_TEXTO_SEC);g2.setFont(new Font("Segoe UI",Font.PLAIN,12));g2.drawString("Sem dados",(getWidth()-60)/2,getHeight()/2);g2.dispose();return;}
                int margin=18;
                int titleHeight=32;
                int availableWidth=getWidth()-margin*2;
                int availableHeight=getHeight()-titleHeight-margin-10;
                // Calcula tamanho da pizza considerando espaço para legenda
                int sz=Math.min(availableWidth-150, availableHeight-20);
                sz=Math.max(sz,80);
                int px=margin+5, py=titleHeight+5;
                // Legenda sempre à direita
                int lx=px+sz+18;
                int ly=py+15;
                double ang=0;
                for(int i=0;i<vals.length;i++){
                    if(vals[i]==0)continue;
                    double sweep=360.0*vals[i]/total;
                    g2.setColor(cores[i%cores.length]);
                    g2.fillArc(px,py,sz,sz,(int)ang,(int)sweep);
                    g2.setColor(AppTheme.COR_SUPERFICIE);g2.setStroke(new BasicStroke(1.5f));
                    g2.drawArc(px,py,sz,sz,(int)ang,(int)sweep);
                    ang+=sweep;
                }
                // Legenda com fonte menor e espaçamento ajustado
                g2.setFont(new Font("Segoe UI",Font.BOLD,11));
                FontMetrics fmLeg=g2.getFontMetrics();
                for(int i=0;i<labels.length;i++){
                    if(i>=vals.length)break;
                    int yPos=ly+i*20;
                    // Verifica se a legenda cabe dentro do painel
                    if(yPos+12>getHeight()-margin) break;
                    g2.setColor(cores[i%cores.length]);g2.fillRoundRect(lx,yPos,10,10,3,3);
                    g2.setColor(AppTheme.COR_TEXTO);
                    String text=labels[i]+" ("+vals[i]+")";
                    // Trunca texto se muito longo
                    int maxWidth=getWidth()-lx-margin-14;
                    while(fmLeg.stringWidth(text)>maxWidth && text.length()>8){
                        text=text.substring(0,text.lastIndexOf(' ')>0?text.lastIndexOf(' '):text.length()-4)+"..";
                    }
                    g2.drawString(text,lx+14,yPos+9);
                }
                g2.dispose();
            }
        };
    }

    private JPanel graficoBarrasDouble(String titulo, java.util.Map<String,Double> dados, Color cor) {
        java.util.Map<String,Integer> intMap=new java.util.LinkedHashMap<>();
        for(java.util.Map.Entry<String,Double> e:dados.entrySet()) intMap.put(e.getKey(),(int)(e.getValue()/1000));
        return new JPanel(){
            {setOpaque(false);setPreferredSize(new Dimension(340,280));}
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppTheme.COR_SUPERFICIE);g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14);
                g2.setColor(new Color(60,60,65));g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,14,14);
                g2.setColor(Color.WHITE);g2.setFont(new Font("Segoe UI",Font.BOLD,14));
                FontMetrics fm=g2.getFontMetrics();
                g2.drawString(titulo,(getWidth()-fm.stringWidth(titulo))/2,22);
                if(intMap.isEmpty()){g2.setColor(AppTheme.COR_TEXTO_SEC);g2.setFont(new Font("Segoe UI",Font.PLAIN,12));g2.drawString("Sem dados",(getWidth()-60)/2,getHeight()/2);g2.dispose();return;}
                int mE=18,mB=65,mT=38,mD=18;
                int wP=getWidth()-mE-mD,hP=getHeight()-mT-mB;
                int max=intMap.values().stream().mapToInt(Integer::intValue).max().orElse(1);
                String[] keys=intMap.keySet().toArray(new String[0]);
                int n=keys.length;
                int bW=Math.max(18,(wP/n)-10);
                g2.setFont(new Font("Segoe UI",Font.BOLD,10));
                FontMetrics fm2=g2.getFontMetrics();
                for(int i=0;i<n;i++){
                    int v=intMap.get(keys[i]);
                    int bH=Math.max(4,(int)((v/(double)max)*(hP-25)));
                    int x=mE+i*(wP/n)+(wP/n-bW)/2;
                    int y=mT+hP-bH;
                    g2.setPaint(new GradientPaint(x,y,cor.brighter(),x,y+bH,cor.darker()));
                    g2.fillRoundRect(x,y,bW,bH,4,4);
                    g2.setColor(Color.WHITE);
                    String sv=v+"k";
                    int svW=fm2.stringWidth(sv);
                    // Posiciona valor acima da barra, dentro da área do gráfico
                    int labelY=Math.max(y-4,mT+12);
                    g2.drawString(sv,x+(bW-svW)/2,labelY);
                    g2.setColor(AppTheme.COR_TEXTO_SEC);
                    g2.setFont(new Font("Segoe UI",Font.PLAIN,9));
                    FontMetrics fm3=g2.getFontMetrics();
                    String lbl=keys[i];
                    int maxLblW=wP/n-4;
                    while(fm3.stringWidth(lbl)>maxLblW && lbl.length()>3) lbl=lbl.substring(0,lbl.length()-1);
                    if(!lbl.equals(keys[i]) && lbl.length()>2) lbl=lbl.substring(0,lbl.length()-1)+".";
                    java.awt.geom.AffineTransform orig=g2.getTransform();
                    int cx=x+bW/2; int cy=mT+hP+12;
                    g2.rotate(-Math.PI/5.5,cx,cy);
                    g2.drawString(lbl,cx-fm3.stringWidth(lbl)/2,cy+fm3.getAscent()/2);
                    g2.setTransform(orig);
                    g2.setFont(new Font("Segoe UI",Font.BOLD,10));
                }
                g2.dispose();
            }
        };
    }

    private void exportarCSV(JTable tabela, String nomeArquivo) {
        JFileChooser fc=new JFileChooser();
        fc.setDialogTitle("Salvar CSV");
        fc.setSelectedFile(new java.io.File(nomeArquivo+".csv"));
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV (*.csv)","csv"));
        if(fc.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION) return;
        java.io.File f=fc.getSelectedFile();
        if(!f.getName().toLowerCase().endsWith(".csv")) f=new java.io.File(f.getAbsolutePath()+".csv");
        try(java.io.PrintWriter pw=new java.io.PrintWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(f),"UTF-8"))){
            pw.write('\uFEFF'); // BOM para Excel reconhecer UTF-8
            javax.swing.table.TableModel m=tabela.getModel();
            StringBuilder sb=new StringBuilder();
            for(int c=0;c<m.getColumnCount();c++){if(c>0)sb.append(";");sb.append('"').append(m.getColumnName(c).replace("\"","\"\"")).append('"');}
            pw.println(sb);
            for(int r=0;r<m.getRowCount();r++){
                sb=new StringBuilder();
                for(int c=0;c<m.getColumnCount();c++){
                    if(c>0)sb.append(";");
                    Object v=m.getValueAt(r,c);
                    String s=v!=null?v.toString():"";
                    sb.append('"').append(s.replace("\"","\"\"")).append('"');
                }
                pw.println(sb);
            }
            JOptionPane.showMessageDialog(this,"CSV salvo em:\n"+f.getAbsolutePath()+"\n\nVoce pode enviar este arquivo pelo WhatsApp.","Exportado",JOptionPane.INFORMATION_MESSAGE);
        }catch(Exception ex){erro("Erro ao exportar: "+ex.getMessage());}
    }

    private JPanel graficoBarras(String titulo, java.util.Map<String,Integer> dados, Color cor) {
        return new JPanel(){
            {setOpaque(false);setPreferredSize(new Dimension(340,280));}
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppTheme.COR_SUPERFICIE);g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14);
                g2.setColor(new Color(60,60,65));g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,14,14);
                g2.setColor(Color.WHITE);g2.setFont(new Font("Segoe UI",Font.BOLD,14));
                FontMetrics fm=g2.getFontMetrics();
                g2.drawString(titulo,(getWidth()-fm.stringWidth(titulo))/2,22);
                if(dados.isEmpty()){g2.setColor(AppTheme.COR_TEXTO_SEC);g2.setFont(new Font("Segoe UI",Font.PLAIN,12));g2.drawString("Sem dados",(getWidth()-60)/2,getHeight()/2);g2.dispose();return;}
                int mE=18,mB=65,mT=38,mD=18;
                int wP=getWidth()-mE-mD,hP=getHeight()-mT-mB;
                int max=dados.values().stream().mapToInt(Integer::intValue).max().orElse(1);
                String[] keys=dados.keySet().toArray(new String[0]);
                int n=keys.length;
                int bW=Math.max(18,(wP/n)-10);
                g2.setFont(new Font("Segoe UI",Font.BOLD,10));
                FontMetrics fm2=g2.getFontMetrics();
                for(int i=0;i<n;i++){
                    int v=dados.get(keys[i]);
                    int bH=Math.max(4,(int)((v/(double)max)*(hP-25)));
                    int x=mE+i*(wP/n)+(wP/n-bW)/2;
                    int y=mT+hP-bH;
                    g2.setPaint(new GradientPaint(x,y,cor.brighter(),x,y+bH,cor.darker()));
                    g2.fillRoundRect(x,y,bW,bH,4,4);
                    g2.setColor(Color.WHITE);
                    String sv=String.valueOf(v);
                    int svW=fm2.stringWidth(sv);
                    // Posiciona valor acima da barra, dentro da área do gráfico
                    int labelY=Math.max(y-4,mT+12);
                    g2.drawString(sv,x+(bW-svW)/2,labelY);
                    g2.setColor(AppTheme.COR_TEXTO_SEC);
                    g2.setFont(new Font("Segoe UI",Font.PLAIN,9));
                    FontMetrics fm3=g2.getFontMetrics();
                    String lbl=keys[i];
                    int maxLblW=wP/n-4;
                    while(fm3.stringWidth(lbl)>maxLblW && lbl.length()>3) lbl=lbl.substring(0,lbl.length()-1);
                    if(!lbl.equals(keys[i]) && lbl.length()>2) lbl=lbl.substring(0,lbl.length()-1)+".";
                    java.awt.geom.AffineTransform orig=g2.getTransform();
                    int cx=x+bW/2; int cy=mT+hP+12;
                    g2.rotate(-Math.PI/5.5,cx,cy);
                    g2.drawString(lbl,cx-fm3.stringWidth(lbl)/2,cy+fm3.getAscent()/2);
                    g2.setTransform(orig);
                    g2.setFont(new Font("Segoe UI",Font.BOLD,10));
                }
                g2.dispose();
            }
        };
    }



}