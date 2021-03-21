package ast.repository;

public interface NonPersistentRepository<T> {

    T findById(Long id);

}
