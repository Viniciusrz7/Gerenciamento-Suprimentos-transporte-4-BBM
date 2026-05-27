package com.bbm4.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

/**
 * Tema visual centralizado — paleta Corpo de Bombeiros.
 * Vermelho institucional + cinza escuro + branco.
 */
public class AppTheme {

    // ── Paleta ──────────────────────────────────────────────────────────────
    public static final Color COR_PRIMARIA      = new Color(0xC0392B); // vermelho CBM
    public static final Color COR_PRIMARIA_DARK = new Color(0x922B21); // hover
    public static final Color COR_FUNDO         = new Color(0x1C1C1E); // fundo escuro
    public static final Color COR_SUPERFICIE    = new Color(0x2C2C2E); // cards
    public static final Color COR_SUPERFICIE2   = new Color(0x3A3A3C); // inputs / linhas
    public static final Color COR_TEXTO         = new Color(0xF5F5F5); // texto principal
    public static final Color COR_TEXTO_SEC     = new Color(0xAEAEB2); // texto secundário
    public static final Color COR_BORDA         = new Color(0x48484A); // bordas
    public static final Color COR_SUCESSO       = new Color(0x30D158); // verde
    public static final Color COR_ALERTA        = new Color(0xFFD60A); // amarelo
    public static final Color COR_ERRO          = new Color(0xFF453A); // vermelho erro
    public static final Color COR_HEADER_TAB    = new Color(0x252527); // header abas

    // ── Fontes ───────────────────────────────────────────────────────────────
    public static final Font FONTE_TITULO   = new Font("Segoe UI", Font.BOLD,   22);
    public static final Font FONTE_SUBTIT   = new Font("Segoe UI", Font.BOLD,   14);
    public static final Font FONTE_LABEL    = new Font("Segoe UI", Font.PLAIN,  13);
    public static final Font FONTE_CAMPO    = new Font("Segoe UI", Font.PLAIN,  13);
    public static final Font FONTE_BOTAO    = new Font("Segoe UI", Font.BOLD,   13);
    public static final Font FONTE_TABELA   = new Font("Segoe UI", Font.PLAIN,  12);
    public static final Font FONTE_HEADER   = new Font("Segoe UI", Font.BOLD,   12);
    public static final Font FONTE_ABA      = new Font("Segoe UI", Font.BOLD,   12);

    // ── Brasão ───────────────────────────────────────────────────────────────
    private static ImageIcon brasaoOriginal;

    public static ImageIcon getBrasao(int largura, int altura) {
        if (brasaoOriginal == null) {
            URL url = AppTheme.class.getClassLoader().getResource("brasao_cbmmg.png");
            if (url != null) {
                brasaoOriginal = new ImageIcon(url);
            } else {
                return null;
            }
        }
        Image img = brasaoOriginal.getImage()
                .getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    // ── Aplicar UIManager global ─────────────────────────────────────────────
    public static void aplicar() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Painel / janela
        UIManager.put("Panel.background",          COR_FUNDO);
        UIManager.put("OptionPane.background",      COR_SUPERFICIE);
        UIManager.put("OptionPane.messageForeground", COR_TEXTO);

        // Botões
        UIManager.put("Button.background",          COR_PRIMARIA);
        UIManager.put("Button.foreground",          Color.WHITE);
        UIManager.put("Button.font",                FONTE_BOTAO);
        UIManager.put("Button.border",              BorderFactory.createEmptyBorder(8, 18, 8, 18));
        UIManager.put("Button.focus",               new Color(0, 0, 0, 0));

        // Campos de texto
        UIManager.put("TextField.background",       COR_SUPERFICIE2);
        UIManager.put("TextField.foreground",       COR_TEXTO);
        UIManager.put("TextField.caretForeground",  COR_TEXTO);
        UIManager.put("TextField.border",           campoInputBorder());
        UIManager.put("TextField.font",             FONTE_CAMPO);
        UIManager.put("PasswordField.background",   COR_SUPERFICIE2);
        UIManager.put("PasswordField.foreground",   COR_TEXTO);
        UIManager.put("PasswordField.caretForeground", COR_TEXTO);
        UIManager.put("PasswordField.border",       campoInputBorder());
        UIManager.put("PasswordField.font",         FONTE_CAMPO);
        UIManager.put("TextArea.background",        COR_SUPERFICIE2);
        UIManager.put("TextArea.foreground",        COR_TEXTO);
        UIManager.put("TextArea.caretForeground",   COR_TEXTO);
        UIManager.put("TextArea.font",              FONTE_CAMPO);

        // Labels
        UIManager.put("Label.foreground",           COR_TEXTO);
        UIManager.put("Label.font",                 FONTE_LABEL);

        // ComboBox
        UIManager.put("ComboBox.background",        COR_SUPERFICIE2);
        UIManager.put("ComboBox.foreground",        COR_TEXTO);
        UIManager.put("ComboBox.font",              FONTE_CAMPO);
        UIManager.put("ComboBox.selectionBackground", COR_PRIMARIA);
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);

        // Tabela
        UIManager.put("Table.background",           COR_SUPERFICIE);
        UIManager.put("Table.foreground",           COR_TEXTO);
        UIManager.put("Table.font",                 FONTE_TABELA);
        UIManager.put("Table.gridColor",            COR_BORDA);
        UIManager.put("Table.selectionBackground",  COR_PRIMARIA);
        UIManager.put("Table.selectionForeground",  Color.WHITE);
        UIManager.put("Table.rowHeight",            28);
        UIManager.put("TableHeader.background",     COR_HEADER_TAB);
        UIManager.put("TableHeader.foreground",     COR_TEXTO);
        UIManager.put("TableHeader.font",           FONTE_HEADER);

        // ScrollPane
        UIManager.put("ScrollPane.background",      COR_SUPERFICIE);
        UIManager.put("ScrollPane.border",          BorderFactory.createLineBorder(COR_BORDA, 1));
        UIManager.put("Viewport.background",        COR_SUPERFICIE);

        // TabbedPane
        UIManager.put("TabbedPane.background",      COR_FUNDO);
        UIManager.put("TabbedPane.foreground",      COR_TEXTO_SEC);
        UIManager.put("TabbedPane.selected",        COR_SUPERFICIE);
        UIManager.put("TabbedPane.selectedForeground", COR_TEXTO);
        UIManager.put("TabbedPane.font",            FONTE_ABA);
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.tabInsets",       new Insets(8, 16, 8, 16));

        // Diálogos
        UIManager.put("Dialog.background",          COR_SUPERFICIE);
        UIManager.put("OptionPane.buttonFont",      FONTE_BOTAO);
    }

    // ── Helpers de estilo ────────────────────────────────────────────────────

    public static Border campoInputBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        );
    }

    /** Botão primário vermelho com cantos arredondados. */
    public static JButton botaoPrimario(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed()   ? COR_PRIMARIA_DARK :
                           getModel().isRollover()  ? COR_PRIMARIA_DARK.brighter() :
                                                      COR_PRIMARIA;
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btn.setFont(FONTE_BOTAO);
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        return btn;
    }

    /** Botão secundário (outline) para ações destrutivas ou neutras. */
    public static JButton botaoSecundario(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed()  ? COR_SUPERFICIE2 :
                           getModel().isRollover() ? COR_BORDA :
                                                     COR_SUPERFICIE;
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(COR_BORDA);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btn.setFont(FONTE_BOTAO);
        btn.setForeground(COR_TEXTO);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        return btn;
    }

    /** Botão vermelho de perigo (excluir). */
    public static JButton botaoDanger(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed()  ? new Color(0x7B241C) :
                           getModel().isRollover() ? new Color(0xA93226) :
                                                     new Color(0x922B21);
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        btn.setFont(FONTE_BOTAO);
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        return btn;
    }

    /** Painel com fundo de superfície e borda arredondada. */
    public static JPanel painelCard() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COR_SUPERFICIE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
            }
        };
        p.setOpaque(false);
        return p;
    }

    /** Separador horizontal estilizado. */
    public static JSeparator separador() {
        JSeparator sep = new JSeparator();
        sep.setForeground(COR_BORDA);
        sep.setBackground(COR_FUNDO);
        return sep;
    }
}
