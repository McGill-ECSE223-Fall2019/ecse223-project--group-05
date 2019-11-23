package ca.mcgill.ecse223.quoridor.features;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
public class CucumberTest_LoadPosition_TestFileWriters {

	public static final String	goodtestfile						=	"quoridor_test_game_1.dat";
	private static final String	goodtestfile_content				=	"B: c3, e5v\n"	+	"W: g7, a1h";
	public static final String	badpawntestfile						=	"quoridor_test_game_invalid_pawn.dat";
	private static final String	badpawntestfile_content				=	"W: e3, c3v\n"	+ 	"B: e3, f5h";
	public static final String	overlappingwalltestfile				=	"quoridor_test_game_invalid_wall_overlap_.dat";
	private static final String overlappingwalltestfile_content		=	"B: e3, b6v\n"	+	"W: e7, b7v";
    public static final String	outoftrackwalltestfile				=	"quoridor_test_game_invalid_wall_out-of-track.dat";
    private static final String	outoftrackwalltestfile_content		=	"W: c3, i1v\n"	+	"B: e7, e9v";
	
    /**
     * Takes in a filename. If the filename is recognized, then the related File will be written into the File System,
     * thereby generating its related test file.
     * @param filename
     */
	public static void createGameSaveTestFile(String filename) {
		SaveConfig.createGameSavesFolder();
		File file = new File( SaveConfig.getGameSaveFilePath(filename) );
		try {
			if(filename.equals(goodtestfile)) {
				BufferedWriter bufferedWriter = new BufferedWriter( new FileWriter(file) );
				bufferedWriter.write(goodtestfile_content);
				bufferedWriter.close();
			} else if(filename.equals(badpawntestfile)) {
				BufferedWriter bufferedWriter = new BufferedWriter( new FileWriter(file) );
				bufferedWriter.write(badpawntestfile_content);
				bufferedWriter.close();
			} else if(filename.equals(overlappingwalltestfile)) {
				BufferedWriter bufferedWriter = new BufferedWriter( new FileWriter(file) );
				bufferedWriter.write(overlappingwalltestfile_content);
				bufferedWriter.close();
			} else if(filename.equals(outoftrackwalltestfile)) {
				BufferedWriter bufferedWriter = new BufferedWriter( new FileWriter(file) );
				bufferedWriter.write(outoftrackwalltestfile_content);
				bufferedWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes the test files this class provides from directory.
	 */
	public static void clearGameSaveLoadingTestFiles() {
		File file1 = new File( SaveConfig.getGameSaveFilePath(goodtestfile) );
		File file2 = new File( SaveConfig.getGameSaveFilePath(badpawntestfile) );
		File file3 = new File( SaveConfig.getGameSaveFilePath(overlappingwalltestfile) );
		File file4 = new File( SaveConfig.getGameSaveFilePath(outoftrackwalltestfile) );
		file1.delete();
		file2.delete();
		file3.delete();
		file4.delete();
	}
	
}
