package output;

import static input.builder.LineItemsBuilder.FIRST_NAME;
import static input.builder.LineItemsBuilder.LAST_NAME;
import static java.text.MessageFormat.format;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;
import input.InputFile;
import input.InputLines;
import input.LineItems;
import input.builder.LineItemsBuilder;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import domain.Resource;

public class PayrollFileTest {

    private static final LineItems LINE_ITEMS_WITH_VALUES = new LineItemsBuilder()
	    .create();
    private static final String EXPECTED_FILENAME = format("{0}-{1}.txt",
	    LAST_NAME, FIRST_NAME);
    private static final LineItems EXPECTED_FILE_CONTENT = new LineItemsBuilder(
	    " | ").create();
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void givenAResourceContentsAreWrittenToFile() throws IOException {
	File destinationFolder = folder.newFolder();
	Resource resource = new Resource(LINE_ITEMS_WITH_VALUES);

	PayrollFile payrollFile = new PayrollFile(resource);
	payrollFile.writeToFolder(destinationFolder);

	String desiredFilepath = destinationFolder.getPath()
		+ EXPECTED_FILENAME;
	InputFile file = new InputFile(desiredFilepath);
	InputLines lines = new InputLines(file);

	assertThat(lines.getLines(), hasItem(EXPECTED_FILE_CONTENT));

    }
}