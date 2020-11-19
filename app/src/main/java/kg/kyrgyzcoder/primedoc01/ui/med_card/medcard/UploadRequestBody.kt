package kg.kyrgyzcoder.primedoc01.ui.med_card.medcard

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream


class UploadRequestBody(
    private val file: File,
    private val contentType: String,
    private val callback: UploadCallback
) : RequestBody() {


    override fun contentType() = "$contentType/*".toMediaTypeOrNull()

    override fun contentLength() = file.length()

    override fun writeTo(sink: BufferedSink) {
        val length = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)

        val fileInputStream = FileInputStream(file)
        var uploaded = 0L

        fileInputStream.use {
            var read: Int

            val handler = Handler(Looper.getMainLooper())

            while (fileInputStream.read(buffer).also { read = it } != -1) {
                handler.post(ProgressUpdate(uploaded, length))

                uploaded += read
                sink.write(buffer, 0, read)
            }
        }
    }

    inner class ProgressUpdate(
        private val uploaded: Long,
        private val total: Long
    ) : Runnable {
        override fun run() {
            callback.onProgressUpdate((100 * uploaded / total).toInt())
        }
    }

    interface UploadCallback {
        fun onProgressUpdate(percentage: Int)
    }
}