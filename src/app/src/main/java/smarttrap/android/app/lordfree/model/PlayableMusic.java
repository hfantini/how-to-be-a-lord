
/*

	+ =====================================================================
	|
	|	== SMARTTRAP @ 2016
	|   == HOW TO BE A LORD - APP
	|	====================================
	|   ==    FILE: PlayableModel.java
	|	==    DATE (YYYY-MM-DD): 2017-01-04 | TIME (HH:MM): 07:26
	|   ==   SINCE: 0.0.5a
	|	==  AUTHOR: Henrique Fantini
	|   == CONTACT: smart.trap@outlook.com
	|   ====================================
	|   ==


*/

package smarttrap.android.app.lordfree.model;

// == IMPORT LIBS
// ==========================================================================================

// == CLASS
// ==========================================================================================

public class PlayableMusic
{
    // == DECLARATION
    // ======================================================================================

    // == CONST =============================================================================

    // == VAR ===============================================================================

    private int musicID;
    private String musicName;
    private int musicFileID;

    // == CLASS CONSTRUCTOR(S)
    // ======================================================================================

    public PlayableMusic(int musicID)
    {
        this.musicID = musicID;
    }

    public PlayableMusic(int musicID, String musicName, int musicFileID)
    {
        this.musicID = musicID;
        this.musicName = musicName;
        this.musicFileID = musicFileID;
    }

    // == CLASS METHODS
    // ======================================================================================

    // == CLASS EVENTS
    // ======================================================================================

    // == GETTERS AND SETTERS
    // ======================================================================================

    public String getMusicName()
    {
        return musicName;
    }

    public void setMusicName(String musicName)
    {
        this.musicName = musicName;
    }

    public int getMusicFileID()
    {
        return musicFileID;
    }

    public void setMusicFileID(int musicFileID)
    {
        this.musicFileID = musicFileID;
    }

    public int getMusicID()
    {
        return musicID;
    }
}
