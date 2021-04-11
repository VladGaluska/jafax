package jafax.repository;

import org.vladg.ast.repository.NonPersistentRepository;
import org.vladg.ast.repository.model.Class;
import org.vladg.ast.repository.model.File;
import org.vladg.ast.repository.model.Method;
import com.google.inject.Inject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NonPersistentRepositoryTest {

    @Inject
    private NonPersistentRepository<File> fileRepository;

    @Inject
    private NonPersistentRepository<Class> classRepository;

    @Inject
    private NonPersistentRepository<Method> methodRepository;

    @Test
    public void savingObjectsIncreasesIndex() {
        this.populateRepositories();
        assertEquals(3, ASTObject);
    }

    @Test
    public void containerStackIsProperlyPopulated() {
        this.populateRepositories();
    }

    @Test
    public void containerPopUntilDoesNotBreakWhenEmpty() {
        NonPersistentRepository.popUntilInstance(Method.class);
    }

    @Test
    public void containerPopUntilReturnsProperContainer() {
        this.populateRepositories();
    }

    @Test
    public void containerPopUntilReturnsNullWhenNoneFound() {
        this.populateRepositories();
    }

    private void populateRepositories() {
        File file1 = File.builder().name("File1").build();
        this.fileRepository.addObject(file1);
        Class clazz = Class.builder().name("Class").build();
        this.classRepository.addObject(clazz);
        Method method = Method.builder().name("Method").build();
        this.methodRepository.addObject(method);
        File file2 = File.builder().name("File2").build();
        this.fileRepository.addObject(file2);
    }

}
