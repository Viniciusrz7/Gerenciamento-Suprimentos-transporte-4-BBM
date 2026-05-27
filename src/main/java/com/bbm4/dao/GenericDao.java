package com.bbm4.dao;

import com.bbm4.orm.*;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO genérico com suporte a:
 * - @Entity(tableName) para nome da tabela
 * - @ManyToOne: salva/lê como coluna <campo>_id (Long FK)
 * - @Id + @GeneratedValue: chave primária autoincrement
 * - LocalDate / LocalDateTime via TEXT ISO
 */
public class GenericDao<T> {

    private final Class<T> entityClass;
    private final String tableName;

    public GenericDao(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.tableName = resolveTableName(entityClass);
        createTableIfNotExists();
    }

    // -------------------------------------------------------------------------
    // Conexão
    // -------------------------------------------------------------------------

    protected Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException(e);
        }
        return DriverManager.getConnection("jdbc:sqlite:bbm4.db");
    }

    // -------------------------------------------------------------------------
    // DDL
    // -------------------------------------------------------------------------

    private void createTableIfNotExists() {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(tableName).append(" (");

        Field[] fields = entityClass.getDeclaredFields();
        List<String> cols = new ArrayList<>();

        for (Field f : fields) {
            f.setAccessible(true);

            if (f.isAnnotationPresent(ManyToOne.class)) {
                // FK: <campo>_id INTEGER
                cols.add(f.getName() + "_id INTEGER");
                continue;
            }

            StringBuilder col = new StringBuilder(f.getName()).append(" ");

            Class<?> type = f.getType();
            if (type == Long.class || type == long.class
                    || type == Integer.class || type == int.class) {
                col.append("INTEGER");
            } else if (type == Double.class || type == double.class
                    || type == Float.class || type == float.class) {
                col.append("REAL");
            } else {
                col.append("TEXT");
            }

            if (f.isAnnotationPresent(Id.class)) {
                col.append(" PRIMARY KEY AUTOINCREMENT");
            }

            cols.add(col.toString());
        }

        sql.append(String.join(", ", cols)).append(")");

        try (Connection c = getConnection(); Statement s = c.createStatement()) {
            s.execute(sql.toString());
            addMissingColumns(c, fields);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMissingColumns(Connection c, Field[] fields) {
        // Descobre colunas existentes
        java.util.Set<String> existing = new java.util.HashSet<>();
        try (ResultSet r = c.getMetaData().getColumns(null, null, tableName, null)) {
            while (r.next()) existing.add(r.getString("COLUMN_NAME").toLowerCase());
        } catch (Exception e) { return; }

        for (Field f : fields) {
            f.setAccessible(true);
            String colName = f.isAnnotationPresent(ManyToOne.class) ? f.getName() + "_id" : f.getName();
            if (f.isAnnotationPresent(Id.class)) continue;
            if (existing.contains(colName.toLowerCase())) continue;
            String colType;
            if (f.isAnnotationPresent(ManyToOne.class)) {
                colType = "INTEGER";
            } else {
                Class<?> t = f.getType();
                if (t == Long.class || t == long.class || t == Integer.class || t == int.class) colType = "INTEGER";
                else if (t == Double.class || t == double.class || t == Float.class || t == float.class) colType = "REAL";
                else colType = "TEXT";
            }
            try (Statement s = c.createStatement()) {
                s.execute("ALTER TABLE " + tableName + " ADD COLUMN " + colName + " " + colType);
            } catch (Exception ignored) {}
        }
    }

    // -------------------------------------------------------------------------
    // CRUD
    // -------------------------------------------------------------------------

    public void salvar(T entity) {
        Field[] fields = entityClass.getDeclaredFields();
        List<String> colNames = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for (Field f : fields) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(Id.class)) continue;

            try {
                Object val = f.get(entity);
                if (f.isAnnotationPresent(ManyToOne.class)) {
                    // salva o id do objeto relacionado
                    colNames.add(f.getName() + "_id");
                    values.add(val != null ? extractId(val) : null);
                } else {
                    colNames.add(f.getName());
                    values.add(val != null ? val.toString() : null);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (colNames.isEmpty()) return;

        String placeholders = colNames.stream().map(c -> "?")
                .collect(java.util.stream.Collectors.joining(", "));
        String sql = "INSERT INTO " + tableName
                + " (" + String.join(", ", colNames) + ")"
                + " VALUES (" + placeholders + ")";

        try (Connection c = getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                p.setObject(i + 1, values.get(i));
            }
            p.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void atualizar(T entity) {
        Field[] fields = entityClass.getDeclaredFields();
        List<String> setClauses = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        Object idValue = null;

        for (Field f : fields) {
            f.setAccessible(true);
            try {
                if (f.isAnnotationPresent(Id.class)) {
                    idValue = f.get(entity);
                    continue;
                }
                Object val = f.get(entity);
                if (f.isAnnotationPresent(ManyToOne.class)) {
                    setClauses.add(f.getName() + "_id = ?");
                    values.add(val != null ? extractId(val) : null);
                } else {
                    setClauses.add(f.getName() + " = ?");
                    values.add(val != null ? val.toString() : null);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (setClauses.isEmpty() || idValue == null) return;

        String sql = "UPDATE " + tableName
                + " SET " + String.join(", ", setClauses)
                + " WHERE id = ?";

        try (Connection c = getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                p.setObject(i + 1, values.get(i));
            }
            p.setObject(values.size() + 1, idValue);
            p.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        try (Connection c = getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            p.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<T> buscarTodos() {
        List<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;

        try (Connection c = getConnection();
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery(sql)) {

            while (r.next()) {
                T obj = entityClass.getDeclaredConstructor().newInstance();
                populateFromResultSet(obj, r, c);
                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public T buscarPorId(Long id) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        try (Connection c = getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    T obj = entityClass.getDeclaredConstructor().newInstance();
                    populateFromResultSet(obj, r, c);
                    return obj;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // -------------------------------------------------------------------------
    // Helpers internos
    // -------------------------------------------------------------------------

    private void populateFromResultSet(T obj, ResultSet r, Connection c) throws Exception {
        for (Field f : entityClass.getDeclaredFields()) {
            f.setAccessible(true);

            if (f.isAnnotationPresent(ManyToOne.class)) {
                // lê a FK e carrega o objeto relacionado
                String fkCol = f.getName() + "_id";
                try {
                    long fkId = r.getLong(fkCol);
                    if (!r.wasNull()) {
                        Object related = loadRelated(f.getType(), fkId, c);
                        f.set(obj, related);
                    }
                } catch (SQLException ignored) {
                    // coluna pode não existir em queries customizadas
                }
                continue;
            }

            try {
                String val = r.getString(f.getName());
                if (val == null) continue;
                setFieldValue(obj, f, val);
            } catch (SQLException ignored) {
                // coluna não existe no ResultSet — ignora
            }
        }
    }

    private void setFieldValue(T obj, Field f, String val) throws Exception {
        Class<?> type = f.getType();
        if (type == Long.class || type == long.class) {
            f.set(obj, Long.parseLong(val));
        } else if (type == Integer.class || type == int.class) {
            f.set(obj, Integer.parseInt(val));
        } else if (type == Double.class || type == double.class) {
            f.set(obj, Double.parseDouble(val));
        } else if (type == Float.class || type == float.class) {
            f.set(obj, Float.parseFloat(val));
        } else if (type == Boolean.class || type == boolean.class) {
            f.set(obj, Boolean.parseBoolean(val));
        } else if (type == java.time.LocalDate.class) {
            f.set(obj, java.time.LocalDate.parse(val));
        } else if (type == java.time.LocalDateTime.class) {
            f.set(obj, java.time.LocalDateTime.parse(val));
        } else {
            f.set(obj, val);
        }
    }

    /**
     * Carrega um objeto relacionado (@ManyToOne) pelo id usando uma query direta,
     * sem instanciar outro GenericDao (evita recursão infinita).
     */
    @SuppressWarnings("unchecked")
    private <R> R loadRelated(Class<R> relatedClass, long id, Connection c) {
        String relatedTable = resolveTableName(relatedClass);
        String sql = "SELECT * FROM " + relatedTable + " WHERE id = ?";
        try (PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    R obj = relatedClass.getDeclaredConstructor().newInstance();
                    for (Field f : relatedClass.getDeclaredFields()) {
                        f.setAccessible(true);
                        if (f.isAnnotationPresent(ManyToOne.class)) continue; // não carrega nested
                        try {
                            String val = r.getString(f.getName());
                            if (val != null) setFieldValueGeneric(obj, f, val);
                        } catch (SQLException ignored) {}
                    }
                    return obj;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <R> void setFieldValueGeneric(R obj, Field f, String val) throws Exception {
        Class<?> type = f.getType();
        if (type == Long.class || type == long.class) f.set(obj, Long.parseLong(val));
        else if (type == Integer.class || type == int.class) f.set(obj, Integer.parseInt(val));
        else if (type == Double.class || type == double.class) f.set(obj, Double.parseDouble(val));
        else if (type == Boolean.class || type == boolean.class) f.set(obj, Boolean.parseBoolean(val));
        else if (type == java.time.LocalDate.class) f.set(obj, java.time.LocalDate.parse(val));
        else if (type == java.time.LocalDateTime.class) f.set(obj, java.time.LocalDateTime.parse(val));
        else f.set(obj, val);
    }

    private Long extractId(Object obj) {
        try {
            for (Field f : obj.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.isAnnotationPresent(Id.class)) {
                    Object val = f.get(obj);
                    if (val == null) return null;
                    return Long.parseLong(val.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String resolveTableName(Class<?> clazz) {
        Entity annotation = clazz.getAnnotation(Entity.class);
        if (annotation != null && !annotation.tableName().isEmpty()) {
            return annotation.tableName();
        }
        return clazz.getSimpleName().toLowerCase();
    }
}
