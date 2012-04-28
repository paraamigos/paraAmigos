package framework.model.persistence.hibernate;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;

/**
 * Permite ordenar por uma SQL formula passada como parametro
 * 
 * @author DBA Eng. de Sistemas
 */
public class OrderBySqlFormula extends Order {

	private static final long serialVersionUID = 1208455822142821651L;
	
	private String sqlFormula;
    
    /**
     * Construtor para Order.
     * 
     * @param sqlFormula SQL formula
     */
    protected OrderBySqlFormula(String sqlFormula) {
        super(sqlFormula, true);
        this.sqlFormula = sqlFormula;
    }
 
    public String toString() {
        return sqlFormula;
    }
 
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return sqlFormula;
    }
 
    /**
     * Custom order
     *
     * @param sqlFormula an SQL formula
     * @return Order
     */
    public static Order sqlFormula(String sqlFormula) {
        return new OrderBySqlFormula(sqlFormula);
    }	
}
