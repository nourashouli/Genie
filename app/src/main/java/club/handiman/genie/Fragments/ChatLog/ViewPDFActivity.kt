package club.handiman.genie.Fragments.ChatLog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.genie_cl.R
import kotlinx.android.synthetic.main.activity_view_pdf.*
import org.json.JSONObject

class ViewPDFActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pdf)


        webView.webViewClient = WebViewClient()
        webView.settings.setSupportZoom(true)
        webView.settings.javaScriptEnabled = true
        val url = getPdfUrl()
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
    }
    fun getPdfUrl(): String? {
        return   (club.handiman.genie.Utils.Utils.BASE_IMAGE_URL.plus(intent!!.extras!!.getString("url")))    }
}
