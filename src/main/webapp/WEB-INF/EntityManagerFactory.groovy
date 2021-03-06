import org.amc.dao.EntityManagerCacheCleaner;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

beans {
    xmlns([aop:'http://www.springframework.org/schema/aop']);
    
    /**
     * Creates an EntityManagerFactory
     */
    applicationEntityManagerFactory(Persistence, 'myDatabase') { bean ->
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

    /**
     * A websocket scoped EntityManager proxy
     */
    webSocketEntityManager(EntityManager) { bean ->
        bean.factoryBean = 'applicationEntityManagerFactory';
        bean.factoryMethod = 'createEntityManager';
        bean.scope = 'websocket';
        bean.destroyMethod = 'close';
        aop.'scoped-proxy'();
    };

    entityManagerCacheCleaner(EntityManagerCacheCleaner, ref('myEntityManagerCache'), 10 ,30) { bean ->
        bean.initMethod = 'init';
        scheduler = ref('myScheduler');
    }
}
