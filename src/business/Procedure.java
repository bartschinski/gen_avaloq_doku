package business;

import java.util.ArrayList;
import java.util.List;

/**
 * @author titom
 *
 */
public class Procedure {
	
	String 		name;
	boolean 	isprivate;
	String[]	content;
	String[]	exception;
	private List<Parameter> parameters = new ArrayList<Parameter>();
	String[]	returnvalue;
	String		description;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isIsprivate() {
		return isprivate;
	}
	public void setIsprivate(boolean isprivate) {
		this.isprivate = isprivate;
	}
	public String[] getContent() {
		return content;
	}
	public void setContent(String[] content) {
		this.content = content;
	}
	public String[] getException() {
		return exception;
	}
	public void setException(String[] exception) {
		this.exception = exception;
	}
	public String[] getReturnvalue() {
		return returnvalue;
	}
	public void setReturnvalue(String[] returnvalue) {
		this.returnvalue = returnvalue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the parameters
	 */
	public List<Parameter> getParameters() {
		return parameters;
	}
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	
	public void addParameter (Parameter p){
		parameters.add(p);
	}
	
	public Parameter getParameter(int index){
		return parameters.get(index);
	}
}
