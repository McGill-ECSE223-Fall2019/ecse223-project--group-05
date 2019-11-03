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
	private static final String	goodtestfile_content				=	"B: c3, e5v\n" + "W: g7, a1h";
	public static final String	badpawntestfile						=	"quoridor_test_game_invalid_pawn.dat";
	private static final String	badpawntestfile_content				=	"W: j0, e5v\n" + "B: 01, a1h";
	public static final String	overlappingwalltestfile				=	"quoridor_test_game_invalid_wall_overlap_.dat";
	private static final String overlappingwalltestfile_content		=	"W: c3, e5v\n" + "B: e6v, e6h";
    public static final String	outoftrackwalltestfile				=	"quoridor_test_game_invalid_wall_out-of-track.dat";
    private static final String	outoftrackwalltestfile_content		=	"W: c3, i1v\n" + "B: e9v, i3h, a9h";
	
    /**
     * Takes in a filename. If the filename is recognized, then the related File will be written into the File System,
     * thereby generating its related test file.
     * @param filename
     */
	public static void createGameSaveTestFile(String filename) {
		SaveConfig.createGameSavesFolder();
		File file = new File( SaveConfig.getGameSaveFilePath(filename) );
		try {
			BufferedWriter bufferedWriter = new BufferedWriter( new FileWriter(file) );
			if(filename.equals(goodtestfile)) {
				bufferedWriter.write(goodtestfile_content);
			} else if(filename.equals(badpawntestfile)) {
				bufferedWriter.write(badpawntestfile_content);
			} else if(filename.equals(overlappingwalltestfile)) {
				bufferedWriter.write(overlappingwalltestfile_content);
			} else if(filename.equals(outoftrackwalltestfile)) {
				bufferedWriter.write(outoftrackwalltestfile_content);
			}
			bufferedWriter.close();
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
