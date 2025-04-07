package geoanalytique.model.geoobject.operation;

import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;
import geoanalytique.model.GeoObject;
import geoanalytique.util.Operation;
 
/**
 * Classe concrete d'une operation permettant de changer le nom
 * d'un objet geometrique
 * 
 */
public class ChangeNomOperation implements Operation {

	private GeoObject object;
	private transient String tmp;
	
	/**
	 * @param object 
	 * 
	 */
	public ChangeNomOperation(GeoObject object) {
		this.object = object;
	}

	/**
	 * @see geoanalytique.util.Operation#calculer()
	 */
	public Object calculer() {
		object.setName(tmp);
		return null; // rien a renvoyer
	}

	/**
	 * @see geoanalytique.util.Operation#getArite()
	 */
	public int getArite() {
		return 1;
	}

	/**
	 * @see geoanalytique.util.Operation#getClassArgument(int)
	 */
	public Class getClassArgument(int num) {
		return String.class;
	}

	/**
	 * @see geoanalytique.util.Operation#getDescriptionArgument(int)
	 */
	public String getDescriptionArgument(int num)
			throws ArgumentOperationException {
		return "Nouveau nom de l'object";
	}

	/**
	 * @see geoanalytique.util.Operation#getTitle()
	 */
	public String getTitle() {
		return "Changer nom";
	}

	/**
	 * @see geoanalytique.util.Operation#setArgument(int, java.lang.Object)
	 */
	public void setArgument(int num, Object o)
			throws ArgumentOperationException, IncorrectTypeOperationException {
		tmp = (String)o;
	}

}
