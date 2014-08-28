package business;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class template {
	private String templateFolder = "U:\\ICE\\Workspace\\Docu Generator\\templates\\";
	private String sourcePath = "C:\\test\\";
	private String destinationPath = "C:\\test\\";
	private Pattern pattern = Pattern.compile("\\{\\$(\\w*)\\}");
	private String sourceName = "";
	private List<String> sourceNames;
	private String sourceObject = "";
	private List<String> sourceObjects;
	private List<History> history;
	private AvaloqFile file;
	private History historyRecord = new History();
	private Header header;

	/**
	 * 
	 * @param source_in
	 * @param source_out
	 */
	public void setConfig(String source_in, String source_out) {
		sourcePath = source_in;
		destinationPath = source_out;
		
		sourceNames = new ArrayList<String>();
		sourceObjects = new ArrayList<String>();
	}

	public void writeBasics() {
		writeCSS();
		writeIndex();
	}

	public void writeIndex() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(templateFolder + "index.html"));
			BufferedWriter out = new BufferedWriter(new FileWriter(destinationPath + "index.html"));

			while (in.ready()) {
				out.write(fileLine(in.readLine()) + "\n");
			}

			out.close();
			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 */
	public void writeCSS() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(templateFolder + "basic.css"));
			BufferedWriter out = new BufferedWriter(new FileWriter(destinationPath + "basic.css"));

			while (in.ready()) {
				out.write(in.readLine() + "\n");
			}

			out.close();
			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param header
	 * @param history
	 */
	public void writeFile(AvaloqFile file) {
		this.file = file;
		this.header = file.getHeader();
		this.history = file.getHeader().getHistory();
		System.out.println(history);

		try {

			BufferedReader in = new BufferedReader(new FileReader(templateFolder + "template.html"));
			BufferedWriter out = new BufferedWriter(new FileWriter(destinationPath + header.getName() + ".html"));

			while (in.ready()) {
				out.write(fileLine(in.readLine()) + "\n");
			}

			out.close();
			in.close();

			this.sourceObjects.add(this.header.getObject());
			this.sourceNames.add(this.header.getName());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param line
	 * @return
	 */
	public String fileLine(String line) {
		Matcher matcher = pattern.matcher(line);
		String replaceString = "";
		StringBuffer stringBuffer = new StringBuffer();

		while (matcher.find()) {
			if (matcher.group(1).equals("title")) {
				replaceString = "Avaloq Doku";

			} else if (matcher.group(1).equals("css_link")) {
				replaceString = "basic.css";

			} else if (matcher.group(1).equals("header")) {
				replaceString = parsFile(matcher.group(1));

			} else if (matcher.group(1).equals("body")) {
				replaceString = parsFile(matcher.group(1));

			} else if (matcher.group(1).equals("footer")) {
				replaceString = parsFile(matcher.group(1));

			} else if (matcher.group(1).equals("filename")) {
				replaceString = this.header.getName() + " (" + this.header.getSource_type() + ")";

			} else if (matcher.group(1).equals("history_table")) {
				replaceString = "";
				System.out.println("lol");
				for (int i = 0; i < this.history.size(); i++) {
						System.out.println(this.history.get(i).getDate());
						this.historyRecord = this.history.get(i);
						replaceString += parsFile(matcher.group(1));
				}

			} else if (matcher.group(1).equals("history_date")) {
				 replaceString = this.historyRecord.getDate();

			} else if (matcher.group(1).equals("history_name")) {
				 replaceString = this.historyRecord.getResp();

			} else if (matcher.group(1).equals("history_ref")) {
				 replaceString = this.historyRecord.getRef();

			} else if (matcher.group(1).equals("history_desc")) {
				 replaceString = this.historyRecord.getDesc();

			} else if (matcher.group(1).equals("mail_list")) {
				replaceString = "thierry.thomann@cic.ch;phillip.bartschinski@cic.ch";

			} else if (matcher.group(1).equals("copyright")) {
				replaceString = "CIC";

			} else if (matcher.group(1).equals("file_list")) {
				replaceString = "";
				for (int i = 0; i < this.sourceNames.size(); i++) {
						this.sourceObject = this.sourceObjects.get(i);
						this.sourceName = this.sourceNames.get(i);
						replaceString += parsFile(matcher.group(1));
				}

			} else if (matcher.group(1).equals("file_list_name")) {
				replaceString = this.sourceName;

			} else if (matcher.group(1).equals("file_list_desc")) {
				replaceString = this.sourceObject;

			} else if (matcher.group(1).equals("file_list_link")) {
				replaceString = this.sourceName + ".html";

			} else {
				replaceString = "nix";
			}

			replaceString = replaceString.replaceAll("\\$", "&#36;");
//			 System.out.println("#" + replaceString + "\n");
//			 System.out.println(stringBuffer + "\n");
			matcher.appendReplacement(stringBuffer, replaceString);
		}

		matcher.appendTail(stringBuffer);

		return stringBuffer.toString();
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public String parsFile(String type) {
		String fileContent = "";

		try {
			BufferedReader in = new BufferedReader(new FileReader(templateFolder + type + ".html"));

			while (in.ready()) {
				fileContent += fileLine(in.readLine()) + "\n";
			}

			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileContent;
	}

}
