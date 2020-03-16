
/*

	+ =====================================================================
	|
	|	== SMARTTRAP @ 2016
	|   == HOW TO BE A LORD - APP
	|	====================================
	|   ==    FILE: MusicSelector.java
	|	==    DATE (YYYY-MM-DD): 2017-01-03 | TIME (HH:MM): 11:00
	|   ==   SINCE: 0.0.1a
	|	==  AUTHOR: Henrique Fantini
	|   == CONTACT: smart.trap@outlook.com
	|   ====================================
	|   ==


*/

package smarttrap.android.app.lordfree.component;

// == IMPORT LIBS
// ==========================================================================================

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import smarttrap.android.app.lordfree.R;
import smarttrap.android.app.lordfree.model.PlayableMusic;
import smarttrap.android.app.lordfree.view.ViewApp;
import smarttrap.android.lib.core.hardware.HardwareUtil;
import smarttrap.android.lib.core.math.DoubleVector;

// == CLASS
// ==========================================================================================

public class MusicSelector extends RelativeLayout
{
    // == DECLARATION
    // ======================================================================================

    // == CONST =============================================================================

    // == VAR ===============================================================================

    private View rootView;
    private TextView txtMusic;
    private ImageView imgSeparator;
    private PlayableMusic music;
    private Boolean isPlaying;
    private ViewApp app;
    private Typeface type;

    // == CLASS CONSTRUCTOR(S)
    // ======================================================================================

    public MusicSelector(Context context)
    {
        super(context);
        initComponent(context);
    }

    public MusicSelector(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initComponent(context);
    }

    public MusicSelector(ViewApp app, Context context, PlayableMusic music)
    {
        super(context);

        this.app = app;
        this.music = music;

        initComponent(context);
    }

    // == CLASS METHODS
    // ======================================================================================

    private void initComponent(Context context)
    {
        inflate(context, R.layout.comp_music_selector, this);

        isPlaying = false;

        txtMusic = (TextView) findViewById(R.id.txtMusicName);
        imgSeparator = (ImageView) findViewById(R.id.imgSeparator);

        DoubleVector<Integer> screenSize = HardwareUtil.getScreenSize();
        txtMusic.getLayoutParams().height = screenSize.getYValue() / 10;
        txtMusic.setTextSize(TypedValue.COMPLEX_UNIT_PX, txtMusic.getLayoutParams().height / 1.3F);

        this.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                app.changeMusic( getMusic() );
            }
        });

        type = Typeface.createFromAsset(context.getAssets(), "fonts/music.ttf");

        updateComponent();
    }

    private void updateComponent()
    {
        if(music != null)
        {
            txtMusic.setText(music.getMusicName());

            if(isPlaying)
            {
                txtMusic.setTypeface(type, Typeface.BOLD_ITALIC);
                txtMusic.setSelected(true);
            }
            else
            {
                txtMusic.setTypeface(type, Typeface.NORMAL);
                txtMusic.setSelected(false);
            }
        }
    }

    // == CLASS EVENTS
    // ======================================================================================

    // == GETTERS AND SETTERS
    // ======================================================================================


    public PlayableMusic getMusic()
    {
        return music;
    }

    public void setMusic(PlayableMusic music)
    {
        this.music = music;
        updateComponent();
    }

    public Boolean getPlaying()
    {
        return isPlaying;
    }

    public void setPlaying(Boolean playing)
    {
        isPlaying = playing;
        updateComponent();
    }
}
