
/*

	+ =====================================================================
	|
	|	== SMARTTRAP @ 2016
	|   == HOW TO BE A LORD - APP
	|	====================================
	|   ==    FILE: ViewAPP.java
	|	==    DATE (YYYY-MM-DD): 2016-12-20 | TIME (HH:MM): 13:55
	|   ==   SINCE: 0.0.1a
	|	==  AUTHOR: Henrique Fantini
	|   == CONTACT: smart.trap@outlook.com
	|   ====================================
	|   ==


*/

package smarttrap.android.app.lordfree.view;

// == IMPORT LIBS
// ==========================================================================================

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdSize;
import smarttrap.android.app.lordfree.R;
import smarttrap.android.app.lordfree.info.ADSKey;
import smarttrap.android.app.lordfree.info.GlobalAttr;
import smarttrap.android.app.lordfree.info.GlobalOptions;
import smarttrap.android.lib.core.ads.ADSContainer;
import smarttrap.android.lib.core.ads.ADSContainerGoogleBanner;
import smarttrap.android.lib.core.hardware.HardwareUtil;
import smarttrap.android.lib.core.interfac.ILoadableContent;

// == CLASS
// ==========================================================================================

public class ViewMenu extends AppCompatActivity implements ILoadableContent
{

    // == DECLARATION
    // ======================================================================================

    // == CONST =============================================================================

    // == VAR ===============================================================================

    private Bitmap bgImage;

    // == CLASS CONSTRUCTOR(S)
    // ======================================================================================

    public ViewMenu()
    {
        super();

        loadContent(GlobalAttr.defaultAppContext);
    }

    // == CLASS METHODS
    // ======================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // REMOVE TITLE BAR
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        // SET DEFAULT THEME

        // DEFINE CONTENT VIEW
        setContentView(R.layout.activity_view_menu);

        //CONFIGURE GENERAL COMPONENTS

        // SETTING BG IMAGE

        ImageView bgContainer = (ImageView) findViewById(R.id.imgMnuBackground);
        bgContainer.setImageBitmap(bgImage);

        // CREATING BUTTON EVENTS

        ImageButton btnStart = (ImageButton) findViewById(R.id.btnMnuStart);
        btnStart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity( ActivityInfo.viewApp );
            }

        });

        ImageButton btnMnuCredits = (ImageButton) findViewById(R.id.btnMnuCredits);
        btnMnuCredits.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity( ActivityInfo.viewCredits );
            }

        });

        //CONFIGURE PREMIUM/FREE VERSION

        ImageButton btnRemoveADS = (ImageButton) findViewById(R.id.btnMnuRemoveADS);

        if(GlobalOptions.PREMIUM_VERSION == true)
        {
            //CHANGE LOGO
            ImageView imgMnuLogo = (ImageView) findViewById(R.id.imgMnuLogo);
            imgMnuLogo.setImageResource(R.drawable.img_mnu_logo_premium);

            //REMOVE ADS BUTTON

            LinearLayout view = (LinearLayout) findViewById(R.id.layMnuSecondRow);
            view.removeView(btnRemoveADS);

            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) view.getLayoutParams();
            param.topMargin = 5;

            view.setLayoutParams(param);

        }
        else
        {
            //REMOVE ADS BUTTON

            btnRemoveADS.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    startActivity( ActivityInfo.viewRemoveADS );
                }

            });

            //ADS
            LinearLayout layAds = (LinearLayout) findViewById(R.id.layMnuADSBarContainer);
            ADSContainer container = new ADSContainerGoogleBanner(getApplicationContext(), layAds, AdSize.SMART_BANNER, ADSKey.ADS_MENU_KEY, GlobalOptions.ADS_TESTMODE);

            try
            {
                container.loadContent( getApplicationContext() );
                container.loadADS();
            }
            catch(Exception e)
            {
                //THROW EXCEPTION
                e.printStackTrace();
            }
        }
    }

    @Override
    public void loadContent(Context context)
    {
        Bitmap bgImageLarger = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_mnu_bg);
        bgImage = Bitmap.createScaledBitmap(bgImageLarger, HardwareUtil.getScreenSize().getXValue(), HardwareUtil.getScreenSize().getYValue(), false);
    }

    // == CLASS EVENTS
    // ======================================================================================

    // == GETTERS AND SETTERS
    // ======================================================================================

}
