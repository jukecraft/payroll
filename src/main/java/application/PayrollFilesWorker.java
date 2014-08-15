package application;

import java.util.List;

import output.PayrollFile;
import domain.Resource;
import domain.ResourcesFile;

public class PayrollFilesWorker implements PayrollWorker {

    private String directory = "src\\main\\resources\\";
    private ResourcesFile resourcesFile = new ResourcesFile(directory);

    @Override
    public void createPayroll() {
	List<Resource> resources = resourcesFile.createResources();
	for (Resource resource : resources) {
	    new PayrollFile(resource).writeToFolder(directory);
	}
    }

    public void setDirectory(String directory) {
	this.directory = directory;
    }

    public void setResourceFile(ResourcesFile resourcesFile) {
	this.resourcesFile = resourcesFile;
    }

}