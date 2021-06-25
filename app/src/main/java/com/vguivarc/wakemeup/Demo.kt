package com.vguivarc.wakemeup

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType

class Demo : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!

        setImmersiveMode()
        setTransformer(AppIntroPageTransformerType.Zoom)
        isColorTransitionsEnabled = true
        isWizardMode = true

        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlide(
            AppIntroFragment.newInstance(
                title = "Etape 1",
                description = "Connectez-vous pour accéder à toutes les fonctionnalités de l'application",
                imageDrawable = R.drawable.demo1,
                backgroundColor = resources.getColor(R.color.colorPrimary, theme)
                // titleTypefaceFontRes = R.font.opensans_regular,
                // descriptionTypefaceFontRes = R.font.opensans_regular,
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = "Etape 2",
                description = "Ajoutez un ou plusieurs réveils selon vos besoins",
                imageDrawable = R.drawable.demo2,
                backgroundColor = resources.getColor(R.color.colorPrimary, theme)
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = "Etape 3",
                description = "Partagez un lien permettant à vos amis de vous rajouter dans leurs contacts",
                imageDrawable = R.drawable.demo3,
                backgroundColor = resources.getColor(R.color.colorPrimary, theme)
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = "Etape 4",
                description = "Lorsque vous recevez un lien de contact, ajouter le ou envoyez leur des musiques",
                imageDrawable = R.drawable.demo4,
                backgroundColor = resources.getColor(R.color.colorPrimary, theme)
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = "Etape 5",
                description = "Directement dans l'applicaton YouTube, cliquez sur Partager pour ajouter une vidéo dans vos favoris",
                imageDrawable = R.drawable.demo5,
                backgroundColor = resources.getColor(R.color.colorPrimary, theme)
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = "Etape 6",
                description = "Retrouvez vos favoris, et envoyez une vidéo à vos contacts !",
                imageDrawable = R.drawable.demo6,
                backgroundColor = resources.getColor(R.color.colorPrimary, theme)
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = "Etape 7",
                description = "Lorsque votre réveil sonne, vous êtes réveillez par une musique choisie par un de vos contacts !",
                imageDrawable = R.drawable.demo7,
                backgroundColor = resources.getColor(R.color.colorPrimary, theme)
            )
        )
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        finish()
    }
}
