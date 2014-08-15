package domain.resources;

import input.InputFile;
import input.InputLines;
import input.LineItems;

import java.util.ArrayList;
import java.util.List;

public class ResourcesFile {

    private static final String RESOURCE_FILENAME = "resources.txt";
    private String directory;

    public ResourcesFile(String directory) {
	this.directory = directory;
    }

    public List<PayrollResource> createResources() {
	InputLines inputLines = new InputLines(new InputFile(directory
		+ RESOURCE_FILENAME));

	return readResources(inputLines);
    }

    private List<PayrollResource> readResources(InputLines inputLines) {
	List<PayrollResource> resources = new ArrayList<>();
	for (LineItems items : inputLines.getLines()) {
	    resources.add(new PayrollResource(items));
	}
	return resources;
    }

}