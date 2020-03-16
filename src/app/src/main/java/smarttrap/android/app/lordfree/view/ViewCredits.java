
/*

	+ =====================================================================
	|
	|	== SMARTTRAP @ 2016
	|   == HOW TO BE A LORD - APP
	|	====================================
	|   ==    FILE: ViewCredits.java
	|	==    DATE (YYYY-MM-DD): 2016-12-28 | TIME (HH:MM): 15:06
	|   ==   SINCE: 0.0.4a
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

public class ViewCredits extends AppCompatActivity implements ILoadableContent
{

    // == DECLARATION
    // ======================================================================================

    // == CONST =============================================================================

    // == VAR ===============================================================================

    private Bitmap bgImage;

    // == CLASS CONSTRUCTOR(S)
    // ======================================================================================

    public ViewCredits()
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

        //REMOVE TITLE BAR
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        //SET DEFAULT THEME

        //DEFINE CONTENT VIEW
        setContentView(R.layout.activity_view_credits);

        // == CONFIGURE COMPONENTS

        ImageView bgContainer = (ImageView) findViewById(R.id.imgCreBackground);
        bgContainer.setImageBitmap(bgImage);

        ImageButton btnBack = (ImageButton) findViewById( R.id.btnCreBack );
        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        //CONFIGURE PREMIUM/FREE VERSION

        if(GlobalOptions.PREMIUM_VERSION == false)
        {

            //ADS
            LinearLayout layAds = (LinearLayout) findViewById(R.id.layCreADSBar);
            ADSContainer container = new ADSContainerGoogleBanner(getApplicationContext(), layAds, AdSize.SMART_BANNER, ADSKey.ADS_CRE_KEY, GlobalOptions.ADS_TESTMODE);

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
        Bitmap bgImageLarger = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_ads_bg);
        bgImage = Bitmap.createScaledBitmap(bgImageLarger, HardwareUtil.getScreenSize().getXValue(), HardwareUtil.getScreenSize().getYValue(), false);
    }

    // == CLASS EVENTS
    // ======================================================================================

    // == GETTERS AND SETTERS
    // ======================================================================================
}
