package juniojsv.minimum

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.preference.PreferenceManager
import com.robotemi.sdk.Robot
import com.robotemi.sdk.listeners.OnRobotReadyListener
import juniojsv.minimum.PreferencesActivity.Companion.registerActivity

class MinimumActivity : AppCompatActivity() , OnRobotReadyListener {
    private lateinit var preferences: SharedPreferences

    private lateinit var robot : Robot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        appearanceHandler(preferences)
        setContentView(R.layout.minimum_activity)

        this.robot = Robot.getInstance(); // get an instance of the robot in order to begin using its features.

        registerActivity(this)
        supportFragmentManager.commit {
            replace(R.id.mApplicationsFragment, ApplicationsFragment())
        }
    }

    override fun onStart() {
        super.onStart()
        robot.addOnRobotReadyListener(this)
    }

    override fun onRobotReady(isReady: Boolean) {
        if (isReady) {
            try {
                val activityInfo = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
                robot.onStart(activityInfo)
            } catch (e: PackageManager.NameNotFoundException) {
                throw RuntimeException(e)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.minimum_shortcuts, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mDial ->
                startActivity(Intent(Intent.ACTION_DIAL))
            R.id.mCamera ->
                startActivity(
                        Intent.createChooser(
                                Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA),
                                getString(R.string.take_pictures_with)
                        )
                )
            R.id.mPreferences ->
                startActivity(Intent(this, PreferencesActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        // Nothings
    }

}
