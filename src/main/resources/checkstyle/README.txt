To use checkstyle you must use Commandline
Move both checkstyle files to the /src/ folder (or the folder you are working in if you want less typing)
Command: java -jar checkstyle.jar -c CS1332checkstyle.xml /path/to/file.java

For example(from src folder):
java -jar checkstyle.jar -c CS1332checkstyle.xml controller\LoginScreenController.java