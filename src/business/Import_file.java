package business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Import_file {

	private String output_html = "";
	private Header header;
	private int modifications_count = -1;
	private String modifications = "";
	private boolean header_done = false;
	private boolean body_done = false;
	private List<History> history;
	private List<Procedure> procedure;
	private List<String> lines;
	private String source_out;
	private String source_in;
	private AvaloqFile file;
	private Parameter parameter;

	public Import_file(String source_in, String source_out) {

		this.source_out = source_out;
		this.source_in = source_in;
		history = new ArrayList<History>();
		header = new Header();

		read(source_in + "nc$ekip_script.txt");

		file = new AvaloqFile();
		header.setHistory(history);
		file.setHeader(header);

		template templ = new template();
		templ.setConfig(source_in, source_out);
		templ.writeFile(file);
		templ.writeBasics();
	}

	public void read(String source_in) {
		File file = new File(source_in);
		try {

			FileReader in = new FileReader(file);
			BufferedReader br = new BufferedReader(in);
			lines = new ArrayList<String>();

			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}

			for (int i = 0; i < lines.size(); i++) {
				handle_line(lines.get(i), i);
			}

			br.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void handle_line(String line, int line_count) {

		String line_clean = line.toLowerCase();
		line_clean = line_clean.replaceAll(" ", "");
		line_clean = line_clean.replaceAll("-", "");
		
		// Process Header
		if (!header_done) {
			if (line_clean.startsWith("[")) {
				line = line.replaceAll("\\[", "");
				line = line.replaceAll("]", "");
				header.setSource_type(line.trim());
				System.out.println("source type: " + header.getSource_type());
			} else if (line_clean.startsWith("name")) {
				line.replaceAll(" ", "");
				header.setName(line.substring(line.indexOf(":") + 1,
						line.length()).trim());
				System.out.println("source name: " + header.getName());
			} else if (line_clean.startsWith("object")) {
				line.replaceAll(" ", "");
				header.setObject(line.substring(line.indexOf(":") + 1,
						line.length()).trim());
				System.out.println("source object: " + header.getObject());
			} else if (line_clean.startsWith("date")) {
				modifications_count = 0;
			} else if (line_clean.startsWith("20") && modifications_count >= 0) {
				modifications += line + "<br>";
				addHistoryEntry(line, modifications_count);
				modifications_count++;
			} else if (line.startsWith("-------") && modifications_count > 0) {
				header_done = true;
			}
		}

		// Process Body
		// "\\w*\\s+procedure\\s+([a-zA-Z_0-9#]+)\\s*"
		if (!body_done && header_done) {
			if (line.contains("procedure")) {
				System.out.println("Procedure: " + line);
				Procedure p = new Procedure();
				addProcedureComment(p, line_count);
			}
		}

	}

	private void addProcedureComment(Procedure p, int line_count) {

		int comment_start = 0;
		int comment_end = 0;
		String line = "";

		for (int i = line_count; line_count > comment_start; i--) {
			line = lines.get(i).trim();
			if (Pattern.matches("s*[-]{4,}", line) && comment_end == 0) {
				comment_end = i;
			} else if (Pattern.matches("s*[-]{4,}", line) && comment_end != 0) {
				comment_start = i;
				break;
			}
		}

		for (int i = comment_start; i <= comment_end; i++) {
			line = lines.get(i).trim();

			// Parameters
			if (line.contains("@param")) {
				parameter = new Parameter();
				String entry[] = line.split(" ");
				int indexParam = -1;
				String desc = "";

				for (int ii = 0; ii < entry.length; ii++) {
					entry[ii] = entry[ii].trim();
					//System.out.println("entry: ]" + entry[ii] + "[");
					if (entry[ii].length() > 0) {
						if (entry[ii].equals("@param")) {
							indexParam = ii;
						} else if (entry[ii] != "" && indexParam >= 0
								&& parameter.getDatatype() == null) {
							parameter.setDatatype(entry[ii]);
						} else if (entry[ii] != "" && indexParam >= 0
								&& parameter.getName() == null) {
							parameter.setName(entry[ii]);
						} else if (entry[ii] != "" && indexParam >= 0
								&& parameter.getDatatype() != null
								&& parameter.getName() != null) {
							desc += entry[ii] + " ";
						}
					}
				}
				parameter.setDesc(desc.trim());
				p.addParameter(parameter);
			}
		}
	}

	private void addHistoryEntry(String line, int hist_nr) {

		String desc = "";
		String entry[] = line.split(" ");
		History history_entry = new History();

		for (int i = 0; i < entry.length; i++) {
			if (Pattern.matches("[0-9]{4}.[0-1][0-9].[0-3][0-9]", entry[i])) {
				history_entry.setDate(entry[i]);
			} else if (Pattern.matches("[A-Z]{4,6}[0-9]*", entry[i])) {
				history_entry.setResp(entry[i]);
			} else if (Pattern.matches("SEBP-[0-9]{1,6}", entry[i])) {
				history_entry.setRef(entry[i]);
			} else if (Pattern.matches("Q[0-9]{1,6}", entry[i])) {
				history_entry.setRef(entry[i]);
			} else if (!entry[i].startsWith("--")) {
				desc += entry[i] + " ";
			}
		}
		history_entry.setDesc(desc.trim());

		System.out.println("History [" + hist_nr + "] Date: "
				+ history_entry.getDate());
		System.out.println("History [" + hist_nr + "] Resp: "
				+ history_entry.getResp());
		System.out.println("History [" + hist_nr + "] Ref: "
				+ history_entry.getRef());
		System.out.println("History [" + hist_nr + "] Desc: "
				+ history_entry.getDesc());

		history.add(history_entry);
	}

}
