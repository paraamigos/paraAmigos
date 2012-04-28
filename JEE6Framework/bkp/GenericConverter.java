package framework.presentation.faces;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.Component;

import br.gov.ancine.framework.model.service.BaseCRUDService;
import br.gov.ancine.framework.util.BeansUtil;
import br.gov.ancine.framework.util.FacesUtil;

/**
 * Conversor gen�rico para qualquer EntityBean
 * 
 * @author Luiz Alberto Sodr&eacute;
 */
@SuppressWarnings("rawtypes")
public class GenericConverter extends BaseEntityConverter {

	private static String ENTITY_CLASS_KEY = "ENTITY_CLASS_KEY";
		
	
	@Override
	public String getAsString(FacesContext facesCtx, UIComponent component, Object entity) {
		Class cacheClass  = (Class)FacesUtil.getSessionAttribute(ENTITY_CLASS_KEY);
		Class entityClass = entity.getClass();
		
		if (cacheClass == null || !entityClass.equals(cacheClass)) {
			FacesUtil.setSessionAttribute(ENTITY_CLASS_KEY, entityClass);
		}
		
		return super.getAsString(facesCtx, component, entity);
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public Object getAsObject(FacesContext facesCtx, UIComponent component, String value) {
		BaseApplicationService service = (BaseApplicationService)Component.getInstance("crudService");
		
		Class entityClass = (Class)FacesUtil.getSessionAttribute(ENTITY_CLASS_KEY);
		Class returnType  = BeansUtil.getIdMethodReturnType(entityClass);
		Object entityId   = null;
	
		if (returnType.equals(Integer.class)) {
			entityId = Integer.valueOf(value);
		} else if (returnType.equals(Long.class)) {
			entityId = Long.valueOf(value);
		} else {
			entityId = value;
		}
		return service.findObject(entityClass, entityId);
	}

}
