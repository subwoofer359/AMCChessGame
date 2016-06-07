package org.amc.test

import org.amc.dao.EntityManagerCache;
import org.amc.dao.EntityManagerCacheCleaner;


beans {
    xmlns([task: 'http://www.springframework.org/schema/task']);
    
    entityManagerCacheCleaner(EntityManagerCacheCleaner, ref('myEntityManagerCache'), 1) { bean ->
        bean.initMethod = 'init';
        scheduler = ref('myScheduler');
    }
    
    myEntityManagerCache(EntityManagerCache) { bean ->
        bean.scope = 'singleton';
        entityManagerFactory = ref('applicationEntityManagerFactory');
    }
    
    
    task.'annotation-driven'(scheduler: 'myScheduler');
    task.scheduler(id: 'myScheduler', poolSize: 10);
    
}
