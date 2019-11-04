package ca.mcgill.ecse223.quoridor.enumerations;

/**
 * enum whose sole purpose is to pass as an argument to game save controllers.
 * DEFAULT is has the same meaning as WEAK_SAVE - that is, they are not FORCE_OVERWRITE.
 * The passing of FORCE_OVERWRITE as an argument is solely for explicitly writing that the call is meant to overwrite an already-existing save file.
 * Note that FORCE_OVERWRITE should only be used when we know there already exists a file to be overwritten. Liberal use of FORCE_OVERWRITE is not acceptable.
 * @author ALPHA Agent_BENNETTE
 *
 */
public enum SavePriority {
	DEFAULT,WEAK_SAVE,FORCE_OVERWRITE,DO_NOT_SAVE
}
