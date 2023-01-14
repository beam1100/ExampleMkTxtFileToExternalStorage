package com.jym.examplemktxt

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.jym.examplemktxt.databinding.ActivityMainBinding
import java.io.FileOutputStream
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {

	private val vBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

		// 메인엑티비티로 돌아올 때 실행
		private val resultLauncher = registerForActivityResult( ActivityResultContracts.StartActivityForResult() ){ result ->
		if (result.resultCode == RESULT_OK){
			val uri = result.data?.data
			val fileData:ByteArray = vBinding.filecontent.text.toString().toByteArray(Charset.forName("utf-8"))
			writeData(uri, fileData)
			Toast.makeText(this, "저장되었습니다!", Toast.LENGTH_SHORT).show()
		}
	} // 'onCreate' 또는 'onStart' 에서 호출하지 않으면 에러 발생

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(vBinding.root)

		vBinding.button.setOnClickListener {
			val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
			intent.addCategory(Intent.CATEGORY_OPENABLE)
			intent.type="text/plain"
			intent.putExtra(Intent.EXTRA_TITLE, "텍스트 파일")
			resultLauncher.launch(intent)

			/*// 또는
			val _intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
				addCategory(Intent.CATEGORY_OPENABLE)
				type="text/plain"
				putExtra(Intent.EXTRA_TITLE, "텍스트 파일")
			}
			resultLauncher.launch(_intent)*/
		}
	}

	private fun writeData(uri: Uri?, fileData:ByteArray) {
		try {
			contentResolver.openFileDescriptor(uri!!, "w")?.use { txt ->
				FileOutputStream(txt.fileDescriptor).use { fos ->
					fos.write(fileData)
					fos.close()
				}
				txt.close()
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
}