package org.khan.android.library.db;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KHAN on 2015-07-08.
 */
public interface GenericDao<T extends BaseModel, ID extends Serializable> {

    <S extends T> S save(S entity);

    <S extends T> List<S> saveAll(List<S> entities);

    boolean exists(ID id);

    T findOne(ID id);

    List<T> findAll();

    List<T> findAll(List<ID> ids);

    List<T> findAll(String selection, String[] selectionArgs);

    long count();

    long count(String selection, String[] selectionArgs);

    boolean delete(ID id);

    boolean delete(T entity);

    boolean delete(List<? extends T> entities);

    boolean deleteAll();
}
