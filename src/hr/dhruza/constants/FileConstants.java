package hr.dhruza.constants;

public class FileConstants {

    public static final String SERIALIZED_STATE_FILE = "context.ser";
    public static final String DOCUMENTATION_FILENAME = "documentation.html";
    public static final String CLASSES_PATH = "src/hr/dhruza/model";
    public static final String CLASSES_PACKAGE
            = CLASSES_PATH.substring(CLASSES_PATH.indexOf("/") + 1).replace("/", ".").concat(".");

    private FileConstants(){}
}
