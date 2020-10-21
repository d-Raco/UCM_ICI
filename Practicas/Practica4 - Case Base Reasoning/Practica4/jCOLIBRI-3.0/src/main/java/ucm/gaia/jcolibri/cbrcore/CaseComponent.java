package ucm.gaia.jcolibri.cbrcore;

/**
 * Interface that defines a component of a case. Cases are composed by instances of this interface. 
 * These components are normal Java Beans with set/get() methods for each field.
 * 
 * @author Juan A. Recio-García
 */
public interface CaseComponent {
	
	/**
	 * Returns the attribute that identifies the component. 
	 * An id-attribute must be unique for each component.
	 */
	Attribute getIdAttribute();
}
