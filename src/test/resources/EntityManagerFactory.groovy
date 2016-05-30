import javax.persistence.EntityManager;
import javax.persistence.Persistence;

beans {
    xmlns([aop:'http://www.springframework.org/schema/aop']);
    
    /**
     * Creates an EntityManagerFactory
     */
    applicationEntityManagerFactory(Persistence, 'myDatabaseTest') { bean ->
            bean.factoryMethod = 'createEntityManagerFactory';
            bean.destroyMethod = 'close';
    };

    /**
     * A request scoped EntityManager proxy
     */
    appEntityManager(EntityManager) { bean ->
        bean.factoryBean = 'applicationEntityManagerFactory';
        bean.factoryMethod = 'createEntityManager';
        bean.scope = 'request';
        bean.destroyMethod = 'close';
        aop.'scoped-proxy'();
    };
}
