package application;

import static input.LineItems.OUTPUT_SEPERATOR;
import static input.builder.LineItemsBuilder.FIRST_NAME;
import static input.builder.LineItemsBuilder.FIRST_NAME2;
import static input.builder.LineItemsBuilder.LAST_NAME;
import static input.builder.LineItemsBuilder.LAST_NAME2;
import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static org.hamcrest.collection.IsArrayWithSize.arrayWithSize;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static output.PayrollFile.FILENAME_TEMPLATE;
import input.InputFile;
import input.InputLines;
import input.LineItems;
import input.builder.LineItemsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import domain.Resource;
import domain.ResourcesFile;

@RunWith(MockitoJUnitRunner.class)
public class PayrollFilesWorkerIntegrationTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private static final String EXPECTED_FILENAME = format(FILENAME_TEMPLATE,
	    LAST_NAME, FIRST_NAME);
    private static final String EXPECTED_FILENAME2 = format(FILENAME_TEMPLATE,
	    LAST_NAME2, FIRST_NAME2);
    private static final LineItems EXPECTED_FILE_CONTENT = new LineItemsBuilder()
	    .withSeperator(OUTPUT_SEPERATOR).create();
    private static final LineItems EXPECTED_FILE_CONTENT2 = new LineItemsBuilder()
	    .withSeperator(OUTPUT_SEPERATOR).withOtherValues().create();

    private static final Resource FIRST_RESOURCE = new Resource(
	    new LineItemsBuilder().create());
    private static final Resource SECOND_RESOURCE = new Resource(
	    new LineItemsBuilder().withOtherValues().create());

    @Mock
    private InputFile workerMock;
    @Mock
    private ResourcesFile resourcesMock;

    @Test
    public void givenUserInputItReadsAResourceFileAndGeneratesPayrollFiles()
	    throws IOException {
	File tempFolder = folder.newFolder();
	PayrollFilesWorker worker = setUpWorker(tempFolder);

	worker.createPayroll();

	File[] files = tempFolder.listFiles();
	assertThat(files, arrayWithSize(2));

	List<String> fileNames = asList(files[0].getName(),
		files[1].getName());
	assertThat(fileNames,
		hasItems(EXPECTED_FILENAME, EXPECTED_FILENAME2));

	List<LineItems> fileContents = asList(getFirstLineOf(files[0]),
		getFirstLineOf(files[1]));
	assertThat(fileContents,
		hasItems(EXPECTED_FILE_CONTENT, EXPECTED_FILE_CONTENT2));
    }

    private LineItems getFirstLineOf(File file) {
	return new InputLines(new InputFile(file.getAbsolutePath())).getLines()
		.get(0);
    }

    private PayrollFilesWorker setUpWorker(File tempFolder) {
	PayrollFilesWorker worker = new PayrollFilesWorker();
	worker.setDirectory(tempFolder.getAbsolutePath() + "\\");
	worker.setResourceFile(resourcesMock);
	when(resourcesMock.createResources()).thenReturn(
		Arrays.asList(FIRST_RESOURCE, SECOND_RESOURCE));
	return worker;
    }
}
