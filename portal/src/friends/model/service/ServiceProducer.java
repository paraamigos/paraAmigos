package friends.model.service;

import javax.ejb.Stateful;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import framework.model.service.DefaultApplicationService;


@Named
@Stateful
public class ServiceProducer {

	@PersistenceContext
	protected EntityManager entityManager;

	
	@Produces
	public DefaultApplicationService getDefaultService() {
		DefaultApplicationService service = new DefaultApplicationService();
		
		service.setEntityManager(entityManager);
		
		return service;
	}
	
}

