package com.hotel.dao;

import java.util.List;
import java.util.Optional;

/**
 * Interface générique pour la couche d'accès aux données (DAO).
 * @param <T> Le type d'entité géré par le DAO.
 */
public interface IDao<T> {
    void sauvegarder(T entity);
    Optional<T> trouverParId(String id);
    List<T> trouverTous();
    void modifier(T entity);
    void supprimer(String id);
    String genererProchainId();
}
