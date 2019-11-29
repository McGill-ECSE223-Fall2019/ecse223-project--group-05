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
		
		testFileTable.put("quoridor_test_game_1.mov",							"1. e9 f9\n"	+
																				"2. e1 d1\n"	+
																				"3. f9 f8\n"	+
																				"4. d1 d2\n"	+
																				"5. f8 f7\n"	+
																				"6. d2 d3\n"	+
																				"7. f7 f6\n"	+
																				"8. d3 d4\n"	+
																				"9. f6 f5\n"	+
																				"10. d4 d5\n"	+
																				"11. f5 f4\n"	+
																				"12. d5 d6\n"	+
																				"13. f4 f3\n"	+
																				"14. d6 d7\n"	+
																				"15. f3 e3\n"	+
																				"16. d7 e7\n"	+
																				"17. e6h\n"	+
																				"18. e3v\n");
		
		testFileTable.put("quoridor_test_game_2.mov",							"1. e9 f9\n"	+
																				"2. e1 d1\n"	+
																				"3. f9 f8\n"	+
																				"4. d1 d2\n"	+
																				"5. f8 f7\n"	+
																				"6. d2 d3\n"	+
																				"7. f7 f6\n"	+
																				"8. d3 d4\n"	+
																				"9. f6 f5\n"	+
																				"10. d4 d5\n"	+
																				"11. f5 f4\n"	+
																				"12. d5 d6\n"	+
																				"13. f4 f3\n"	+
																				"14. d6 d7\n"	+
																				"15. f3 e3\n"	+
																				"16. d7 e7\n"	+
																				"17. e3 e2\n"	+
																				"18. e7 e8\n"	+
																				"19. e6h\n"	+
																				"20. e3v\n"	+
																				"21. e5h\n"	+
																				"22. e4h\n");
		
		testFileTable.put("quoridor_test_game_3.mov",							"1. e9 f9\n"	+
																				"2. e1 d1\n"	+
																				"3. f9 f8\n"	+
																				"4. d1 d2\n"	+
																				"5. f8 f7\n"	+
																				"6. d2 d3\n"	+
																				"7. f7 f6\n"	+
																				"8. d3 d4\n"	+
																				"9. f6 f5\n"	+
																				"10. d4 d5\n"	+
																				"11. f5 f4\n"	+
																				"12. d5 d6\n"	+
																				"13. f4 f3\n"	+
																				"14. d6 d7\n"	+
																				"15. f3 e3\n"	+
																				"16. d7 e7\n"	+
																				"17. e6h\n"	+
																				"18. e3h\n"	+
																				"19. e5h\n"	+
																				"20. e4h\n"	+
																				"21. e3 e2\n"	+
																				"22. e7 e8\n"	+
																				"23. e2 e1\n");
		
		testFileTable.put("quoridor_test_game_invalid_pawn_move.mov",			"1. e9 f1\n");
		
		testFileTable.put("quoridor_test_game_invalid_wall_move.mov",			"1. e9 e8\n"	+
																				"2. e7h\n"	+
																				"3. e8 e7\n");
		
		testFileTable.put("quoridor_test_game_invalid_jump_move.mov",			"1. e9 e8\n"	+
																				"2. e1 e2\n"	+
																				"3. e8 e7\n"	+
																				"4. e2 e3\n"	+
																				"5. e7 e6\n"	+
																				"6. e4 e5\n"	+
																				"7. e6 f4\n");
		
		
		return testFileTable;
	}
	
	
}
