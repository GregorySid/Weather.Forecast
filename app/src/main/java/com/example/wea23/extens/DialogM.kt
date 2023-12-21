package com.example.wea23.extens

import android.app.AlertDialog
import android.content.Context

object DialogM {
    fun locDialog(context: Context, listner: Listn) {
        val bilder = AlertDialog.Builder(context)
        val dialog = bilder.create()
        dialog.setTitle(
            "Bключите службу " +
                    "определения местоположения"
        )
        dialog.setMessage(
            "Сервису 'Погода' требуется служба определения " +
                    "местоположения для получения метеорологических " +
                    "данных для текущего местоположения"
        )
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Настр.") { _, _ ->
            listner.onClick()
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Отмена") { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    interface Listn {
        fun onClick()
    }
}