/**
 * 
 */
package business;

import java.util.List;

/**
 * @author titom
 *
 */
public class Header {

	private String Source_type;
	private String name;
	private String object;
	private List<History> history;
	/**
	 * @return the history
	 */
	public List<History> getHistory() {
		return history;
	}
	/**
	 * @param history the history to set
	 */
	public void setHistory(List<History> history) {
		this.history = history;
	}
	/**
	 * @return the source_type
	 */
	public String getSource_type() {
		return Source_type;
	}
	/**
	 * @param source_type the source_type to set
	 */
	public void setSource_type(String source_type) {
		Source_type = source_type;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the object
	 */
	public String getObject() {
		return object;
	}
	/**
	 * @param object the object to set
	 */
	public void setObject(String object) {
		this.object = object;
	}
	
}
