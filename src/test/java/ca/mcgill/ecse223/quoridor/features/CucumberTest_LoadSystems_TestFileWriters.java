package ca.mcgill.ecse223.quoridor.features;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import ca.mcgill.ecse223.quoridor.configuration.SaveConfig;

/**
 * The LoadPosition feature requires that pre-existing test files produced certain behaviours. 
 * The contents of these test files is not defined in the tests, and therefore must be defined here.
 * This class fulfills a single purpose: to create the .dat test save files needed for the feature tests.
 * It is not meant to be used by a class on startup, but rather by the step definitions as given
 * or when statements are provided which describe what test files are being accessed.
 * A single public static method will provide the interface through which a String argument must be passed
 * in the form of the name of the desired text file.
 * @author Edwin Pan
 *
 */
public class CucumberTest_LoadSystems_TestFileWriters {	
	
    /**
     * Takes in a filename. If the filename is recognized, then the related File will be written into the File System,
     * thereby generating its related test file.
     * This entire class is a developer tool specific for dealing with testing. If you want to know what suite of test files it provides,
     * read the hardcoded implementation of filename keys to filecontent values in the testfiletable provider private method, getTestFileTable, of this class.
     * This is important because this function will throw you an IllegalArgumentException if you provide a filename which it does not recognize.
     * @param filename
     */
	public static void createGameSaveTestFile(String filename) {
		//Make sure there is the actual saves folder to work with.
		SaveConfig.createGameSavesFolder();
		
		//Get the hashtable of all supported test files.
		Hashtable<String,String> testFileTable = getTestFileTable();
		if(!testFileTable.containsKey(filename)) {
			throw new IllegalArgumentException("Provided test filename " + filename + " is not supported by this test environment provider.");
		}
		
		//Create the test file.
		File file = new File( SaveConfig.getGameSaveFilePath(filename) );
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter(file) );
			writer.write( testFileTable.get(filename) );
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes the test files this class provides from directory.
	 */
	public static void clearGameSaveLoadingTestFiles() {
		for( String testfilename : getTestFileTable().keySet() ) {
			File file = new File ( SaveConfig.getGameSaveFilePath(testfilename) );
			file.delete();
		}
	}
	
	
	
	
	
	
	/*
	 * Creates the hashtable of test files to their testing contents
	 */
	private static Hashtable<String,String> getTestFileTable() {
		Hashtable<String,String> testFileTable = new Hashtable<String,String>();
		
		
		testFileTable.put("quoridor_test_game_1.dat",							"B: c3, e5v\n"	+	
																				"W: g7, a1h");
		
		testFileTable.put("quoridor_test_game_invalid_pawn.dat",				"W: e3, c3v\n"	+ 	
																				"B: e3, f5h");
		
		testFileTable.put("quoridor_test_game_invalid_wall_overlap_.dat",		"B: e3, b6v\n"	+	
																				"W: e7, b7v");
		testFileTable.put("quoridor_test_game_invalid_wall_out-of-track.dat",	"W: c3, i1v\n"	+	
																				"B: e7, e9v");
		
		testFileTable.put("quoridor_test_game_1.mov",							"1. e8 e2\n"	+
																				"2. e7 e3\n"	+
																				"3. e3v e6h\n");
		
		testFileTable.put("quoridor_test_game_2.mov",							"1. e8 e2\n"	+
																				"2. b6h a1h\n"	+
																				"3. e3v e6h\n");
		
		testFileTable.put("quoridor_test_game_3.mov",							"1. e8 e2\n"	+
																				"2. e7 e3\n"	+
																				"3. f7 e4\n"	+
																				"4. f6 e5\n"	+
																				"5. f5 e6\n"	+
																				"6. f4 e7\n"	+
																				"7. f3 e8\n"	+
																				"8. f2 e9\n");
		
		testFileTable.put("quoridor_test_game_invalid_pawn_move.mov",			"1. e8 e2\n"	+
																				"2. e7 e3\n"	+
																				"3. e6 e4\n"	+
																				"4. e5 e5\n");
		
		testFileTable.put("quoridor_test_game_invalid_wall_move.mov",			"1. e3h e6h\n"	+
																				"2. f5h f5v\n");
		
		testFileTable.put("quoridor_test_game_invalid_jump_move.mov",			"1. e8 e2\n"	+
																				"2. e7 e3\n"	+
																				"3. e6 e4\n"	+
																				"4. e4h e5\n"	+
																				"5. e4 a1v\n");
		
		
		return testFileTable;
	}
	
	
}
