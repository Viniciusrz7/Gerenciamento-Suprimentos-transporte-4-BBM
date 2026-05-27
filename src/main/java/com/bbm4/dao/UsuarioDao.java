package com.bbm4.dao;

import com.bbm4.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDao extends GenericDao<Usuario> {

    public UsuarioDao() {
        super(Usuario.class);
    }

    public Usuario buscarPorLogin(String login) {
        String sql = "SELECT * FROM usuario WHERE login = ?";
        try (Connection c = getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, login);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    Usuario u = new Usuario();
                    u.setId(r.getLong("id"));
                    u.setNome(r.getString("nome"));
                    u.setLogin(r.getString("login"));
                    u.setSenha(r.getString("senha"));
                    return u;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}