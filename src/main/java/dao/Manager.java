package dao;

import annotations.*;
import connection.ConnectionFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.stream.Stream;

public class Manager implements ORManager {
    ConnectionFactory connectionFactory = null;
    Connection connection;

    @Override
    public ORManager withPropertiesFrom(String filename) { // initialize connection factory for the DB
        connectionFactory = new ConnectionFactory();
        try {
            connection = connectionFactory.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public void register(Class... entityClasses) { // generate the schema in the DB

        for (Class entityClass : entityClasses) {
            if (Arrays.stream(entityClass.getDeclaredAnnotations()).filter(el -> el.annotationType().equals(Entity.class)).findFirst().isEmpty()) {
                break;
            }

            var meta = MetaInfo.of(entityClass);
            String Sql = meta.createTableSql();
            try {
                Statement statement = connection.createStatement();
                statement.execute(Sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public Object save(Object o) {  // insert() or save()

        if (o.getClass().isAnnotationPresent(Entity.class)) {

            var meta = MetaInfo.of(o.getClass());
            String tableName = meta.tableName;
            List<MetaInfo.ColumnInfo> fields = meta.columns;

            //To SQL


            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO ").append(tableName).append(" (");
            fields.forEach((field) ->
                    sb.append(field.columnName).append(", "));
            sb.setLength(sb.length() - 2);
            sb.append(" )");

            sb.append(" VALUES ( ");
            fields.forEach((field) -> {
                Object value = "String".equals(field.getType()) ? "'" + field.getValue(o) + "'" : field.getValue(o);
                sb.append(value);
                sb.append(", ");
            });
            sb.setLength(sb.length() - 2);
            sb.append(" )");

            try {
                PreparedStatement statement = connection.prepareStatement(sb.toString());
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return o;
    }

    @Override
    public <T> Optional<T> findByID(Serializable id, Class<T> cls) throws Exception {    // READ
        Optional<T> optional = Optional.empty();

        if (cls.isAnnotationPresent(Entity.class)) {

            var meta = MetaInfo.of(cls);
            String tableName = meta.tableName;
            Field primaryKey = meta.primaryKey.field;
            List<MetaInfo.ColumnInfo> fields = meta.columns;

            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM ").append(tableName);
            sb.append(" WHERE id = ").append(id).append(";");

            T res = null;
            try {
                res = cls.getConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sb.toString());
                ResultSet resultSet = preparedStatement.executeQuery();
//                resultSet.next();

                if (!resultSet.next()) return optional;

                primaryKey.setAccessible(true);
                primaryKey.set(res, resultSet.getInt(primaryKey.getName()));

                for (Field field : cls.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Column.class)) {
                        field.setAccessible(true);
                        if (field.getType() == int.class) {
                            field.set(res, resultSet.getInt(field.getName()));
                        } else if (field.getType() == String.class) {
                            field.set(res, resultSet.getString(field.getName()));
                        }
                    }
                }
                primaryKey.setAccessible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (res != null) optional = Optional.of(res);
        }
        return optional;
    }

    @Override
    public <T> List<T> findAll(Class<T> cls) throws SQLException {
        List<T> result = new ArrayList<>();
        if (cls.isAnnotationPresent(Entity.class)) {

            var meta = MetaInfo.of(cls);
            String tableName = meta.tableName;
            Field primaryKey = meta.primaryKey.field;
            List<MetaInfo.ColumnInfo> fields = meta.columns;

            StringBuilder selectQuery = new StringBuilder("SELECT ");
            selectQuery.append(primaryKey.getName());
            fields.forEach((field) -> selectQuery.append(", ").append(field.columnName));
            selectQuery.append(" FROM ").append(tableName);

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(selectQuery.toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    T obj = cls.getConstructor().newInstance();
                    fields.forEach((field) -> {
                        field.field.setAccessible(true);
                        try {
                            field.field.set(obj, resultSet.getObject(field.columnName));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        field.field.setAccessible(false);
                    });
                    result.add(obj);
                }
            } catch (Exception e) {
//                e.printStackTrace();
                System.out.println("This table do not exist in the database");
            }
        }
        return result;
    }

    @Override
    public <T> Stream<T> findAllAsStream(Class<T> cls) throws SQLException { // the lazy one
        return findAll(cls).stream();
    }

    @Override
    public <T> T update(T o) {   // send DB row -> obj     UPDATE
        T res = null;

        if (o.getClass().isAnnotationPresent(Entity.class)) {   // check if object has @Entity

            var meta = MetaInfo.of(o.getClass());
            String tableName = meta.tableName;
            String id = meta.primaryKey.getValue(o).toString();
            Field primaryKey = meta.primaryKey.field;
            List<MetaInfo.ColumnInfo> fields = meta.columns;

            StringBuilder selectQuery = new StringBuilder("SELECT ");
            selectQuery.append(primaryKey.getName());
            fields.forEach((field) -> selectQuery.append(", ").append(field.columnName));
            selectQuery.append(" FROM ").append(tableName);
            selectQuery.append(" WHERE id = ").append(id).append(";");

            try {
                res = (T) o.getClass().getConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(selectQuery.toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();

                primaryKey.setAccessible(true);
                primaryKey.set(res, resultSet.getInt(primaryKey.getName()));

                for (Field field : o.getClass().getDeclaredFields()) {
                    if (field.isAnnotationPresent(Column.class)) {
                        field.setAccessible(true);

                        if (field.getType() == int.class) {
                            field.set(res, resultSet.getInt(field.getName()));
                        } else if (field.getType() == String.class) {
                            field.set(res, resultSet.getString(field.getName()));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    @Override
    public <T> T merge(T obj) {

        if (obj.getClass().isAnnotationPresent(Entity.class)) {
            var meta = MetaInfo.of(obj.getClass());
            String tableName = meta.tableName;
            String idFieldName = meta.primaryKey.columnName;
            String idFieldValue = meta.primaryKey.getValue(obj).toString();

            List<MetaInfo.ColumnInfo> fields = meta.columns;

            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM ").append(tableName);
            sb.append(" WHERE id = ").append(idFieldValue).append(";");

            try (Connection connection = ConnectionFactory.getInstance().connect();
                 PreparedStatement ps = connection.prepareStatement(sb.toString(),
                         ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    fields.forEach((field) -> {
                        if (!"id".equals(field.columnName)) {
                            try {
                                field.field.setAccessible(true);
                                Object value = field.getValue(obj);
                                field.field.setAccessible(false);
                                rs.updateObject(field.columnName, value);
                            } catch (RuntimeException | SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    rs.updateRow();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    @Override
    public boolean delete(Object o) {
        var meta = MetaInfo.of(o.getClass());
        String tableName = meta.tableName;
        String idFieldName = meta.primaryKey.columnName;
        String idFieldValue = meta.primaryKey.getValue(o).toString();

        String Sql = String.format("DELETE FROM %s WHERE %s = %s;",
                tableName, idFieldName, idFieldValue);

        try {
            connection.prepareStatement(Sql).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public <T> void dropTable(Class<T> cls) throws SQLException {
        var meta = MetaInfo.of(cls);
        String tableName = meta.tableName;

        String Sql = "DROP TABLE " + tableName + ";";
        PreparedStatement preparedStatement = connection.prepareStatement(Sql);
        preparedStatement.execute();
    }
}

