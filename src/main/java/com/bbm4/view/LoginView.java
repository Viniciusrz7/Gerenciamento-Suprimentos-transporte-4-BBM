package com.bbm4.view;

import com.bbm4.dao.UsuarioDao;
import com.bbm4.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDateTime;
import java.util.function.Consumer;

/**
 * Tela de login modal com tema escuro Corpo de Bombeiros.
 */
public class LoginView extends JDialog {

    private final UsuarioDao usuarioDao;
    private final Consumer<Usuario> onLoginSucesso;

    public LoginView(UsuarioDao usuarioDao, Consumer<Usuario> onLoginSucesso) {
        super((Frame) null, "Login - 4º BBM", true);
        this.usuarioDao = usuarioDao;
        this.onLoginSucesso = onLoginSucesso;
        initUI();
    }

    private void initUI() {
        setSize(420, 530);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppTheme.COR_FUNDO);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
            }
        };
        root.setOpaque(false);
        root.setBorder(BorderFactory.createEmptyBorder(36, 40, 36, 40));
        setContentPane(root);
        setBackground(new Color(0, 0, 0, 0));

        // ── Cabeçalho ────────────────────────────────────────────────────────
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        ImageIcon brasao = AppTheme.getBrasao(72, 72);
        if (brasao != null) {
            JLabel imgLabel = new JLabel(brasao);
            imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            header.add(imgLabel);
            header.add(Box.createVerticalStrut(12));
        }

        JLabel titulo = new JLabel("4º BBM - Juiz de Fora", SwingConstants.CENTER);
        titulo.setFont(AppTheme.FONTE_TITULO);
        titulo.setForeground(AppTheme.COR_TEXTO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(titulo);

        JLabel subtit = new JLabel("Gerenciamento de Suprimentos e Frota", SwingConstants.CENTER);
        subtit.setFont(AppTheme.FONTE_LABEL);
        subtit.setForeground(AppTheme.COR_TEXTO_SEC);
        subtit.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(Box.createVerticalStrut(4));
        header.add(subtit);

        root.add(header, BorderLayout.NORTH);

        // ── Formulário ───────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(28, 0, 0, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.weightx = 1.0;

        JLabel lblLogin = new JLabel("Login");
        lblLogin.setFont(AppTheme.FONTE_SUBTIT);
        lblLogin.setForeground(AppTheme.COR_TEXTO_SEC);
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(lblLogin, gbc);

        JTextField txtLogin = new JTextField();
        txtLogin.setFont(AppTheme.FONTE_CAMPO);
        gbc.gridy = 1;
        form.add(txtLogin, gbc);

        JLabel lblSenha = new JLabel("Senha");
        lblSenha.setFont(AppTheme.FONTE_SUBTIT);
        lblSenha.setForeground(AppTheme.COR_TEXTO_SEC);
        gbc.gridy = 2; gbc.insets = new Insets(14, 0, 6, 0);
        form.add(lblSenha, gbc);

        JPasswordField txtSenha = new JPasswordField();
        txtSenha.setFont(AppTheme.FONTE_CAMPO);
        gbc.gridy = 3; gbc.insets = new Insets(6, 0, 6, 0);
        form.add(txtSenha, gbc);

        JLabel lblErro = new JLabel(" ", SwingConstants.CENTER);
        lblErro.setFont(AppTheme.FONTE_LABEL);
        lblErro.setForeground(AppTheme.COR_ERRO);
        gbc.gridy = 4; gbc.insets = new Insets(4, 0, 4, 0);
        form.add(lblErro, gbc);

        JButton btnEntrar = AppTheme.botaoPrimario("Entrar");
        btnEntrar.setPreferredSize(new Dimension(0, 42));
        gbc.gridy = 5; gbc.insets = new Insets(8, 0, 0, 0);
        form.add(btnEntrar, gbc);

        JButton btnSair = AppTheme.botaoSecundario("Sair");
        btnSair.setPreferredSize(new Dimension(0, 38));
        gbc.gridy = 6; gbc.insets = new Insets(8, 0, 0, 0);
        form.add(btnSair, gbc);

        root.add(form, BorderLayout.CENTER);

        // ── Ações ────────────────────────────────────────────────────────────
        Runnable tentarLogin = () -> {
            String login = txtLogin.getText().trim();
            String senha = new String(txtSenha.getPassword());
            if (login.isEmpty() || senha.isEmpty()) {
                lblErro.setText("Preencha login e senha.");
                return;
            }
            Usuario u = usuarioDao.buscarPorLogin(login);
            if (u != null && com.bbm4.Main.hash(senha).equals(u.getSenha())) {
                u.setUltimoAcesso(LocalDateTime.now());
                usuarioDao.atualizar(u);
                dispose();
                onLoginSucesso.accept(u);
            } else {
                lblErro.setText("Login ou senha inválidos.");
                txtSenha.setText("");
            }
        };

        btnEntrar.addActionListener(e -> tentarLogin.run());
        btnSair.addActionListener(e -> System.exit(0));
        txtSenha.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) tentarLogin.run();
            }
        });

        // Arrastar janela sem decoração
        Point[] dragOrigin = {null};
        root.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mousePressed(java.awt.event.MouseEvent e) { dragOrigin[0] = e.getPoint(); }
        });
        root.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override public void mouseDragged(java.awt.event.MouseEvent e) {
                if (dragOrigin[0] == null) return;
                Point loc = getLocation();
                setLocation(loc.x + e.getX() - dragOrigin[0].x, loc.y + e.getY() - dragOrigin[0].y);
            }
        });
    }
}
