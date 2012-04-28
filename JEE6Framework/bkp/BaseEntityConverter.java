package framework.presentation.faces;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.jboss.seam.Component;

import br.gov.ancine.framework.model.service.BaseCRUDService;
import br.gov.ancine.framework.util.BeansUtil;

/**
 * Conversor para EntityBeans
 * 
 * @author Luiz Alberto Sodr&eacute;
 */
public class BaseEntityConverter<E> implements Converter {

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object getAsObject(FacesContext facesCtx, UIComponent component, String value) {
		BaseApplicationService service = (BaseApplicationService)Component.getInstance("crudService");
		
		Class<E> entityClass = BeansUtil.getGenericSuperclass(getClass());
		Class returnType 	 = BeansUtil.getIdMethodReturnType(entityClass);
		Object entityId 	 = null;
	
		if (returnType.equals(Integer.class)) {
			entityId = Integer.valueOf(value);
		} else if (returnType.equals(Long.class)) {
			entityId = Long.valueOf(value);
		} else {
			entityId = value;
		}
		return service.findObject(entityClass, entityId);
	}

	@Override
	public String getAsString(FacesContext facesCtx, UIComponent component, Object entity) {
		Object value = BeansUtil.getEntityId(entity);
		
		return value != null ? value.toString() : null;
	}

}













