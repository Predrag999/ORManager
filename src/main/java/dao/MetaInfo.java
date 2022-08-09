package dao;

import annotations.*;
import connection.ConnectionFactory;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;

public class MetaInfo<T> {
    private static final Map<Class<?>, String> JavaTypeToSqlType = Map.of(
            int.class, "INTEGER",
            Integer.class, "INTEGER",
            double.class, "FLOAT",
            Double.class, "FLOAT",
            long.class, "BIGINTEGER",
            Long.class, "BIGINTEGER",
            String.class, "VARCHAR(250)"
    );

    private static final Map<Class<?>, MetaInfo> cache = new HashMap<>();

    Class<T> cls;
    String tableName;
    PrimaryKey primaryKey;
    List<ColumnInfo> columns = new ArrayList<>();
    Connection connection;
    ConnectionFactory connectionFactory;

    static class PrimaryKey extends ColumnInfo {

    }

    static class ColumnInfo {
        Field field;
        String columnName;

        public ColumnInfo() {

        }

        public ColumnInfo(Field field, String columnName) {
            this.field = field;
            this.columnName = columnName;
        }

        Object getValue(Object o) {
            field.setAccessible(true);
            try {
                return field.get(o);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        String getType() {
            field.setAccessible(true);
            return field.getType().getSimpleName();
        }

    }

    public MetaInfo(Class<T> cls) {
        this.cls = cls;
        init();
//        init1();
    }

    public MetaInfo() {
    }

    public static <T1> MetaInfo<T1> of(Class<T1> cls) {
        return cache.computeIfAbsent(cls, c -> new MetaInfo<>(cls));
    }

    private void init() {
        setTableName();
        setPrimaryKey();
        setColumns();
//        setColumnsForNewTable();
    }
//    private void init1(){
//        setColumnsForNewTable();
//    }


    public void setColumns() {
        for (Field field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(Id.class)) {
                columns.add(new ColumnInfo(field, field.getName()));
            }
        }
    }
    public void setColumnsForNewTable(){
        setForeignKeyRelation(cls);
        var foreignKey = addForeignKey(cls);
        try {
            connectionFactory = new ConnectionFactory();
            try {
                connection = connectionFactory.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            PreparedStatement preparedStatement = connection.prepareStatement(foreignKey);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setForeignKeyRelation(Class cls) {
        for (Field field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(ManyToOne.class)) {

                Class<T> name = (Class<T>) field.getType();
                System.out.println(name);

                for (Field field1 : name.getDeclaredFields()) {
                    if (field1.isAnnotationPresent(OneToMany.class)) {
                        System.out.println("In this for circle");
                        if (field.isAnnotationPresent(Id.class)) {
                            var res = new PrimaryKey();
                                res.field = field;
                                res.columnName = field.getName();
                                this.primaryKey = res;
                                columns.add(new ColumnInfo(field,field.getName()));
                                break;
                        }
                    }
                }
            }
        }
    }

    private void setPrimaryKey() {
        for (var field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                var res = new PrimaryKey();
                res.field = field;
                res.columnName = field.getName();
                this.primaryKey = res;
                break;
            }
        }
    }

    private void setTableName() {
        var ann = cls.getDeclaredAnnotation(Table.class);
        if (ann != null && !ann.value().isEmpty()) {
            tableName = ann.value();
        } else {
            tableName = cls.getSimpleName();
        }
    }

    public String createTableSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ").append(tableName).append(" (");
        sb.append(columnDefinition(primaryKey)).append(" NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY ");
        for (var column : columns) {
            sb.append(", \n");
            sb.append(columnDefinition(column));
        }
        sb.append(");");
        return sb.toString();
    }

    String constraint = "FK_StudentsLaptops";

    public String addForeignKey(Class<T> clz) {
        String laptops = "laptops";
        String fk = "FK_StudentLaptops";
        String pk = "studentId";
        StringBuilder sb = new StringBuilder();
        for(Field field : clz.getDeclaredFields()) {
            if(field.isAnnotationPresent(JoinColumn.class)) {
                sb.append("ALTER TABLE " + laptops);
                sb.append("\n");
                sb.append("ADD CONSTRAINT " + fk);
                sb.append("\n");
                sb.append("FOREIGN KEY " + "student_Id" + " REFERENCES " + "students" + "(" + "student_Id" + ");");
                System.out.println(primaryKey);
                System.out.println("Success");
                columns.add(new ColumnInfo(field,field.getName()));
            }
        }
        return sb.toString();
    }

    private String columnDefinition(ColumnInfo column) {
        // -> name VARCHAR(250)
        String sqlType = JavaTypeToSqlType.get(column.field.getType());
        return column.columnName + " " + sqlType;
    }
}
