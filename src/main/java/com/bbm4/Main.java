package com.bbm4;

import com.bbm4.dao.UsuarioDao;
import com.bbm4.model.Usuario;
import com.bbm4.view.AppTheme;
import com.bbm4.view.LoginView;
import com.bbm4.view.MainView;

import javax.swing.*;
import java.security.MessageDigest;

public class Main {

    /** SHA-256 usado para hash de senha. */
    public static String hash(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(base.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder();
            for (byte b : bytes) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) hex.append('0');
                hex.append(h);
            }
            return hex.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) {
        // Aplica o tema visual antes de qualquer componente ser criado
        AppTheme.aplicar();

        SwingUtilities.invokeLater(() -> {
            UsuarioDao usuarioDao = new UsuarioDao();
            if (usuarioDao.buscarTodos().isEmpty()) {
                usuarioDao.salvar(new Usuario("Administrador", "admin", hash("admin")));
            }
            new LoginView(usuarioDao, (Usuario u) ->
                    new MainView(u.getNome()).setVisible(true)
            ).setVisible(true);
        });
    }
}
