package org.apache.felix.dm.itest;
import org.apache.felix.dm.Component;
import org.apache.felix.dm.DependencyManager;

public class AspectChainTest extends TestBase {

	public void testBuildAspectChain() {
	    DependencyManager m = getDM();
        // helper class that ensures certain steps get executed in sequence
        Ensure e = new Ensure();
        // create a service provider and consumer
        Component sp = m.createComponent().setImplementation(new ServiceProvider(e)).setInterface(ServiceInterface.class.getName(), null);
        Component sc = m.createComponent().setImplementation(new ServiceConsumer(e)).add(m.createServiceDependency().setService(ServiceInterface.class).setRequired(true));
        Component sa2 = m.createAspectService(ServiceInterface.class, null, 20, null).setImplementation(new ServiceAspect(e, 3));
        Component sa3 = m.createAspectService(ServiceInterface.class, null, 30, null).setImplementation(new ServiceAspect(e, 2));
        Component sa1 = m.createAspectService(ServiceInterface.class, null, 10, null).setImplementation(new ServiceAspect(e, 4));
        m.add(sc);

        m.add(sp);
        m.add(sa2);
        m.add(sa3);
        m.add(sa1);
        e.step();
        e.waitForStep(5,  5000);
        
        m.remove(sa3);
        m.remove(sa2);
        m.remove(sa1);
        m.remove(sp);
        
        m.remove(sc);
    }
    
    static interface ServiceInterface {
        public void invoke(Runnable run);
    }
    
    static class ServiceProvider implements ServiceInterface {
        private final Ensure m_ensure;
        public ServiceProvider(Ensure e) {
            m_ensure = e;
        }
        public void invoke(Runnable run) {
            run.run();
        }
    }
    
    static class ServiceAspect implements ServiceInterface {
        private final Ensure m_ensure;
        private volatile ServiceInterface m_parentService;
        private final int m_step;
        
        public ServiceAspect(Ensure e, int step) {
            m_ensure = e;
            m_step = step;
        }
        public void start() {
        }
        
        public void invoke(Runnable run) {
            m_ensure.step(m_step);
            m_parentService.invoke(run);
        }
        
        public void stop() {
        }
    }

    static class ServiceConsumer implements Runnable {
        private volatile ServiceInterface m_service;
        private final Ensure m_ensure;

        public ServiceConsumer(Ensure e) {
            m_ensure = e;
        }
        
        public void init() {
            Thread t = new Thread(this);
            t.start();
        }
        
        public void run() {
            m_ensure.waitForStep(1, 2000);
            m_service.invoke(Ensure.createRunnableStep(m_ensure, 5));
        }
    }
}
