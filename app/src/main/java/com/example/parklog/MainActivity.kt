package com.example.parklog

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    private lateinit var deviceContainer: LinearLayout
    private val viewModel: DeviceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addDeviceButton: Button = findViewById(R.id.addDeviceButton)
        deviceContainer = findViewById(R.id.deviceContainer)

        // Add New Device 버튼 클릭 시 입력 창을 표시
        addDeviceButton.setOnClickListener {
            showAddDeviceDialog()
        }

        // ViewModel의 devices 리스트를 관찰하여 UI 갱신
        viewModel.devices.observe(this, Observer { devices ->
            updateDeviceList(devices)
        })
    }

    private fun showAddDeviceDialog() {
        // 입력 창을 위한 EditText 생성
        val editText = EditText(this).apply {
            hint = "장치 이름을 입력하세요"
        }

        // AlertDialog를 통해 입력 창 표시
        AlertDialog.Builder(this)
            .setTitle("새 장치 등록")
            .setView(editText)
            .setPositiveButton("등록") { _, _ ->
                val deviceName = editText.text.toString()
                if (deviceName.isNotBlank()) {
                    viewModel.addDevice(deviceName)
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun updateDeviceList(devices: List<String>) {
        deviceContainer.removeAllViews()
        devices.forEachIndexed { index, deviceName ->
            val newDeviceLayout = createDeviceLayout(deviceName, index)
            deviceContainer.addView(newDeviceLayout)
        }
    }

    private fun createDeviceLayout(deviceName: String, index: Int): LinearLayout {
        val newDeviceLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(16, 16, 16, 16)
        }

        val deviceTextView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
            text = deviceName
            textSize = 18f
        }

        val editButton = Button(this).apply {
            text = "수정"
            setOnClickListener {
                showEditDialog(deviceName, index)
            }
        }

        val deleteButton = Button(this).apply {
            text = "삭제"
            setOnClickListener {
                viewModel.removeDevice(index)
            }
        }

        newDeviceLayout.addView(deviceTextView)
        newDeviceLayout.addView(editButton)
        newDeviceLayout.addView(deleteButton)

        return newDeviceLayout
    }

    private fun showEditDialog(currentName: String, index: Int) {
        val editText = EditText(this).apply {
            setText(currentName.replace(" 🚗", ""))
        }

        AlertDialog.Builder(this)
            .setTitle("장치 이름 수정")
            .setView(editText)
            .setPositiveButton("확인") { _, _ ->
                val newName = "${editText.text} 🚗"  // 수정된 이름에 이모지 추가
                viewModel.updateDeviceName(index, newName)
            }
            .setNegativeButton("취소", null)
            .show()
    }
}
