package dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ORManager {
    // let it work with ids:
    // - Long (autogenerated at DB side)
    // - UUID (autogenerated at ORM side)
    // - String
    // The fields may be of types:
    // - int/Integer
    // - long/Long
    // - double/Double
    // - boolean/Boolean
    // - String
    // - LocalDate
    // - LocalTime
    // - LocalDateTime/Instant
    // - BigDecimal
    // - Enum + @Enumerated(EnumType.ORDINAL/EnumType.STRING)

    // initialize connection factory for the DB
    ORManager withPropertiesFrom(String filename);

    // generate the schema in the DB
    void register(Class... entityClasses);

//    Object save(Object o); // CREATE

    Object save(Object o);

    <T> Optional<T> findByID(Serializable id, Class<T> cls) throws Exception; // READ

    <T> List<T> findAll(Class<T> cls) throws SQLException; // READ ALL

    <T> Stream<T> findAllAsStream(Class<T> cls) throws SQLException; // READ ALL LAZY

    <T> T merge(T o) throws SQLException; // send o -> DB row      UPDATE

    <T> T update(T obj) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException; // send o <- DB row  UPDATE

    // return true if successfully deleted
    boolean delete(Object o) throws SQLException; // set autogenerated id to null DELETE

    <T> void dropTable(Class<T> cls) throws SQLException;
}
