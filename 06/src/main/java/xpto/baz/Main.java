package xpto.baz;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.internal.jpa.deployment.SEPersistenceUnitInfo;
import org.eclipse.persistence.jpa.PersistenceProvider;

import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String... args) throws Exception {
        SEPersistenceUnitInfo puInfo = new SEPersistenceUnitInfo();
        puInfo.setPersistenceUnitName("default");
        puInfo.setPersistenceUnitRootUrl(Main.class.getProtectionDomain().getCodeSource().getLocation());
        puInfo.setClassLoader(Main.class.getClassLoader());
        puInfo.setManagedClassNames(List
                .of("xpto.baz.TodoList", "xpto.baz.TodoItem"));

        Properties props = new Properties();
        props.put(PersistenceUnitProperties.ECLIPSELINK_SE_PUINFO, puInfo);
        props.load(Main.class.getClassLoader()
                .getResourceAsStream("jpa.properties"));
        EntityManagerFactory emf = new PersistenceProvider()
                .createEntityManagerFactory("default", props);
        EntityManager em = emf.createEntityManager();

        TodoList todoList = new TodoList("today");
        em.getTransaction().begin();
        em.persist(todoList);
        em.getTransaction().commit();


    }
}
