package com.vicentesiis.inventarioaj

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import com.itextpdf.text.Document
import com.itextpdf.text.Element.ALIGN_CENTER
import com.itextpdf.text.List
import com.itextpdf.text.ListItem
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.vicentesiis.inventarioaj.data.Category
import com.vicentesiis.inventarioaj.data.Item
import com.vicentesiis.inventarioaj.data.Sale
import com.vicentesiis.inventarioaj.data.UserLog
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class ReportsActivity : AppCompatActivity() {

    private val STORAGE_CODE: Int = 100;

    private val realm = Realm.getDefaultInstance()

    private fun getReports(): MutableList<String> {

        val reports = ArrayList<String>()

        reports.add("Disponibilidad por producto")
        reports.add("Venta por producto")
        reports.add("Disponibilidad por categoria")
        reports.add("Venta por categoria")
        reports.add("Venta por dia")
        reports.add("Venta por mes")
        reports.add("Venta por año")
        reports.add("Sesiones")

        return  reports

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            //system OS >= Marshmallow(6.0), check permission is enabled or not
            if (checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED){
                //permission was not granted, request it
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permissions, STORAGE_CODE)
            }
        }


        val listView = findViewById<ListView>(R.id.reports_list)
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, getReports())
        listView.adapter = arrayAdapter
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->

                when(position) {
                    0 -> savePDFFormatProduct() // Reporte: por producto
                    1 -> savePDFFormatSalesProduct() // Reporte: venta por producto
                    2 -> savePDFFormatCategory() // Reporte: por categoria
                    3 -> savePDFFormatSalesCategory() // Reporte: venta por categoria
                    4 -> savePDFFormatDateDAY() // Reporte: venta por dia
                    5 -> savePDFFormatDateMONTH() // Reporte: venta por mes
                    6 -> savePDFFormatDateYEAR() // Reporte: venta por año
                    7 -> savePDFFormatSession() // Reporte: sesiones
                }

            }

    }

    private fun savePDFFormatSession() {
        val mDoc = Document()
        val mFileName = "Reporte_de_Sesiones_" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFileName +".pdf"

        try {
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()
            mDoc.addAuthor("Vicente Cantú")

            val paragraph = Paragraph("Reporte de Sesiones")
            paragraph.font.size = 30f

            mDoc.add(paragraph)
            val table = PdfPTable(3)
            table.spacingBefore = 30f

            arrayOf("Nombre", "Correo", "Fecha").map {
                table.addCell(it)
            }

            val userLogs = realm.where<UserLog>().findAll()

            for (userLog in userLogs) {
                if (userLog != null) {
                    table.addCell(PdfPCell(Paragraph(userLog.user?.name)))
                    table.addCell(PdfPCell(Paragraph(userLog.user?.email)))
                    table.addCell(PdfPCell(Paragraph(dateToString(null, userLog.createdAt))))
                }
            }

            mDoc.add(table)

            mDoc.close()

            Toast.makeText(this, "$mFileName.pdf\nis saved to\n$mFilePath", Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePDFFormatDateYEAR() {

        val mDoc = Document()
        val mFileName = "Reporte_de_venta_por_año" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFileName +".pdf"

        try {
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()
            mDoc.addAuthor("Vicente Cantú")

            val paragraph = Paragraph("Reporte de venta por Año")
            paragraph.font.size = 30f
            paragraph.spacingAfter = 30f
            mDoc.add(paragraph)

            val tableHeader = PdfPTable(3)
            arrayOf("Producto", "Fecha", "Precio").map { tableHeader.addCell(it) }
            mDoc.add(tableHeader)

            val cal1 = Calendar.getInstance()
            val cal2 = Calendar.getInstance()

            var table = PdfPTable(3)

            val sales = realm.where<Sale>()
                .findAll()
                .sort("createdAt", Sort.ASCENDING)

            var insertTable = false

            for (i in sales.indices) {

                if (i == 0) {

                    val paragraphDate = Paragraph(dateToString("YYYY", sales[i]?.createdAt ?: Date()))
                    paragraphDate.alignment = ALIGN_CENTER
                    paragraphDate.spacingAfter = 10f
                    mDoc.add(paragraphDate)

                    table.addCell(sales[i]?.item?.name)
                    table.addCell(PdfPCell(Paragraph(dateToString("dd-MM | HH:mm", sales[i]?.createdAt ?: Date()))))
                    table.addCell("$${sales[i]?.item?.price}")

                } else {
                    cal1.time = sales[i]?.createdAt ?: Date()
                    cal2.time = sales[i-1]?.createdAt ?: Date()
                    val sameDay = cal1[Calendar.YEAR] === cal2[Calendar.YEAR]

                    if (sameDay) {

                        table.addCell(sales[i]?.item?.name)
                        table.addCell(PdfPCell(Paragraph(dateToString("dd-MM | HH:mm", sales[i]?.createdAt ?: Date()))))
                        table.addCell("$${sales[i]?.item?.price}")

                        insertTable = false

                    } else {

                        /*val count = realm.where<Sale>()
                            .equalTo("item.id", sales[i]?.item?.id)
                            .and()
                            .between("createdAt", atStartOfDay(sales[i]?.createdAt ?: Date()), atEndOfDay(sales[i]?.createdAt ?: Date())).count()
                        */
                        mDoc.add(table)
                        insertTable = true

                        val paragraphDate = Paragraph(dateToString("YYYY", sales[i]?.createdAt ?: Date()))
                        paragraphDate.alignment = ALIGN_CENTER
                        paragraphDate.spacingAfter = 20f
                        mDoc.add(paragraphDate)

                        table = PdfPTable(3)
                        table.addCell(sales[i]?.item?.name)
                        table.addCell(PdfPCell(Paragraph(dateToString("dd-MM | HH:mm", sales[i]?.createdAt ?: Date()))))
                        table.addCell("$${sales[i]?.item?.price}")

                    }

                    if (i+1 == sales.count() && !insertTable || !sameDay) {
                        mDoc.add(table)
                    }

                }

            }

            val sum = sales.sumBy { it.item?.price ?: 0 }

            var total = Paragraph("Total: $$sum")
            total.font.size = 15f

            mDoc.add(total)

            mDoc.close()

            Toast.makeText(this, "$mFileName.pdf\nis saved to\n$mFilePath", Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

    }

    private fun savePDFFormatDateMONTH() {

        val mDoc = Document()
        val mFileName = "Reporte_de_venta_por_mes" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFileName +".pdf"

        try {
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()
            mDoc.addAuthor("Vicente Cantú")

            val paragraph = Paragraph("Reporte de venta por Mes")
            paragraph.font.size = 30f
            paragraph.spacingAfter = 30f
            mDoc.add(paragraph)

            val tableHeader = PdfPTable(3)
            arrayOf("Producto", "Fecha", "Precio").map { tableHeader.addCell(it) }
            mDoc.add(tableHeader)

            val cal1 = Calendar.getInstance()
            val cal2 = Calendar.getInstance()

            var table = PdfPTable(3)

            val sales = realm.where<Sale>()
                .findAll()
                .sort("createdAt", Sort.ASCENDING)

            var insertTable = false

            for (i in sales.indices) {

                if (i == 0) {

                    val paragraphDate = Paragraph(dateToString("MM/yyyy", sales[i]?.createdAt ?: Date()))
                    paragraphDate.alignment = ALIGN_CENTER
                    paragraphDate.spacingAfter = 10f
                    mDoc.add(paragraphDate)

                    table.addCell(sales[i]?.item?.name)
                    table.addCell(PdfPCell(Paragraph(dateToString("dd | HH:mm", sales[i]?.createdAt ?: Date()))))
                    table.addCell("$${sales[i]?.item?.price}")

                } else {
                    cal1.time = sales[i]?.createdAt ?: Date()
                    cal2.time = sales[i-1]?.createdAt ?: Date()
                    val sameDay = cal1[Calendar.MONTH] === cal2[Calendar.MONTH] &&
                            cal1[Calendar.YEAR] === cal2[Calendar.YEAR]

                    if (sameDay) {

                        table.addCell(sales[i]?.item?.name)
                        table.addCell(PdfPCell(Paragraph(dateToString("dd | HH:mm", sales[i]?.createdAt ?: Date()))))
                        table.addCell("$${sales[i]?.item?.price}")

                        insertTable = false

                    } else {

                        /*val count = realm.where<Sale>()
                            .equalTo("item.id", sales[i]?.item?.id)
                            .and()
                            .between("createdAt", atStartOfDay(sales[i]?.createdAt ?: Date()), atEndOfDay(sales[i]?.createdAt ?: Date())).count()
                        */
                        mDoc.add(table)
                        insertTable = true

                        val paragraphDate = Paragraph(dateToString("MM/yyyy", sales[i]?.createdAt ?: Date()))
                        paragraphDate.alignment = ALIGN_CENTER
                        paragraphDate.spacingAfter = 20f
                        mDoc.add(paragraphDate)

                        table = PdfPTable(3)
                        table.addCell(sales[i]?.item?.name)
                        table.addCell(PdfPCell(Paragraph(dateToString("dd | HH:mm", sales[i]?.createdAt ?: Date()))))
                        table.addCell("$${sales[i]?.item?.price}")

                    }

                    if (i+1 == sales.count() && !insertTable || !sameDay) {
                        mDoc.add(table)
                    }

                }

            }

            val sum = sales.sumBy { it.item?.price ?: 0 }

            var total = Paragraph("Total: $$sum")
            total.font.size = 15f

            mDoc.add(total)

            mDoc.close()

            Toast.makeText(this, "$mFileName.pdf\nis saved to\n$mFilePath", Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

    }

    private fun savePDFFormatDateDAY() {

        val mDoc = Document()
        val mFileName = "Reporte_de_venta_por_dia" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFileName +".pdf"

        try {
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()
            mDoc.addAuthor("Vicente Cantú")

            val paragraph = Paragraph("Reporte de venta por Día")
            paragraph.font.size = 30f
            paragraph.spacingAfter = 30f
            mDoc.add(paragraph)

            val tableHeader = PdfPTable(3)
            arrayOf("Producto", "Hora", "Precio").map { tableHeader.addCell(it) }
            mDoc.add(tableHeader)

            val cal1 = Calendar.getInstance()
            val cal2 = Calendar.getInstance()

            var table = PdfPTable(3)

            val sales = realm.where<Sale>()
                .findAll()
                .sort("createdAt", Sort.ASCENDING)

            var insertTable = false

            for (i in sales.indices) {

                if (i == 0) {

                    val paragraphDate = Paragraph(dateToString("dd-MM-yyyy", sales[i]?.createdAt ?: Date()))
                    paragraphDate.alignment = ALIGN_CENTER
                    paragraphDate.spacingAfter = 10f
                    mDoc.add(paragraphDate)

                    table.addCell(sales[i]?.item?.name)
                    table.addCell(PdfPCell(Paragraph(dateToString("HH:mm", sales[i]?.createdAt ?: Date()))))
                    table.addCell("$${sales[i]?.item?.price}")

                } else {
                    cal1.time = sales[i]?.createdAt ?: Date()
                    cal2.time = sales[i-1]?.createdAt ?: Date()
                    val sameDay = cal1[Calendar.DAY_OF_YEAR] === cal2[Calendar.DAY_OF_YEAR] &&
                            cal1[Calendar.YEAR] === cal2[Calendar.YEAR]

                    if (sameDay) {

                        table.addCell(sales[i]?.item?.name)
                        table.addCell(PdfPCell(Paragraph(dateToString("HH:mm", sales[i]?.createdAt ?: Date()))))
                        table.addCell("$${sales[i]?.item?.price}")

                        insertTable = false

                    } else {

                        /*val count = realm.where<Sale>()
                            .equalTo("item.id", sales[i]?.item?.id)
                            .and()
                            .between("createdAt", atStartOfDay(sales[i]?.createdAt ?: Date()), atEndOfDay(sales[i]?.createdAt ?: Date())).count()
                        */
                        mDoc.add(table)
                        insertTable = true

                        val paragraphDate = Paragraph(dateToString("dd-MM-yyyy", sales[i]?.createdAt ?: Date()))
                        paragraphDate.alignment = ALIGN_CENTER
                        paragraphDate.spacingAfter = 20f
                        mDoc.add(paragraphDate)

                        table = PdfPTable(3)
                        table.addCell(sales[i]?.item?.name)
                        table.addCell(PdfPCell(Paragraph(dateToString("HH:mm", sales[i]?.createdAt ?: Date()))))
                        table.addCell("$${sales[i]?.item?.price}")

                    }

                    if (i+1 == sales.count() && !insertTable || !sameDay) {
                        mDoc.add(table)
                    }

                }

            }

            val sum = sales.sumBy { it.item?.price ?: 0 }

            var total = Paragraph("Total: $$sum")
            total.font.size = 15f

            mDoc.add(total)

            mDoc.close()

            Toast.makeText(this, "$mFileName.pdf\nis saved to\n$mFilePath", Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

    }

    private fun savePDFFormatSalesProduct() {
        val mDoc = Document()
        val mFileName = "Reporte_por_Producto_" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFileName +".pdf"

        try {
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()
            mDoc.addAuthor("Vicente Cantú")

            val paragraph = Paragraph("Reporte por Producto")
            paragraph.font.size = 30f

            mDoc.add(paragraph)
            val table = PdfPTable(3)
            table.spacingBefore = 30f

            arrayOf("Producto", "Vendidos", "Precio").map {
                table.addCell(it)
            }

            val items = realm.where<Item>().findAll()

            for (item in items) {
                if (item != null) {
                    table.addCell(PdfPCell(Paragraph(item.name)))
                    table.addCell(PdfPCell(Paragraph(realm.where<Sale>().equalTo("item.id", item.id).count().toString())))
                    table.addCell(PdfPCell(Paragraph("$${item.price}")))
                }
            }

            mDoc.add(table)

            mDoc.close()

            Toast.makeText(this, "$mFileName.pdf\nis saved to\n$mFilePath", Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePDFFormatProduct() {
        val mDoc = Document()
        val mFileName = "Reporte_por_Producto_" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFileName +".pdf"

        try {
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()
            mDoc.addAuthor("Vicente Cantú")

            val paragraph = Paragraph("Reporte por Producto")
            paragraph.font.size = 30f

            mDoc.add(paragraph)
            val table = PdfPTable(3)
            table.spacingBefore = 30f

            arrayOf("Producto", "Vendidos", "Disponibles").map {
                table.addCell(it)
            }

            val items = realm.where<Item>().findAll()

            for (item in items) {
                if (item != null) {
                    table.addCell(PdfPCell(Paragraph(item.name)))
                    table.addCell(PdfPCell(Paragraph(realm.where<Sale>().equalTo("item.id", item.id).count().toString())))
                    table.addCell(PdfPCell(Paragraph(item.quantity.toString())))
                }
            }

            mDoc.add(table)

            mDoc.close()

            Toast.makeText(this, "$mFileName.pdf\nis saved to\n$mFilePath", Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }


    private fun savePDFFormatCategory() {

        val mDoc = Document()
        val mFileName = "Reporte_por_Categoría_" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFileName +".pdf"

        try {
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()
            mDoc.addAuthor("Vicente Cantú")

            val paragraph = Paragraph("Reporte por Categoría")
            paragraph.font.size = 30f
            paragraph.spacingAfter = 30f
            mDoc.add(paragraph)

            val categories = realm.where<Category>().findAll()

            for (i in categories.indices) {
                if (categories[i]?.items != null) {
                    var list = List()
                    if (i+1 != categories.count()) {
                        if (categories[i]?.id != categories[i+1]?.id) {

                            list.add(categories[i]?.name)
                            mDoc.add(list)

                            val items = categories[i]?.items

                            val table = PdfPTable(3)

                            items?.map {
                                table.addCell(PdfPCell(Paragraph(it.name)))
                                table.addCell(PdfPCell(Paragraph("Vendidos: " +
                                        realm.where<Sale>().equalTo("item.id", it.id).count().toString())))
                                table.addCell(PdfPCell(Paragraph("Disponibles: " + it.quantity.toString())))
                            }

                            mDoc.add(table)

                        }
                    } else {
                        if (categories[i-1]?.id != categories[i]?.id) {

                            list.add(categories[i]?.name)
                            mDoc.add(list)

                            val items = categories[i]?.items

                            val table = PdfPTable(3)

                            items?.map {
                                table.addCell(PdfPCell(Paragraph(it.name)))
                                table.addCell(PdfPCell(Paragraph("Vendidos: " +
                                        realm.where<Sale>().equalTo("item.id", it.id).count().toString())))
                                table.addCell(PdfPCell(Paragraph("Disponibles: " + it.quantity.toString())))
                            }

                            mDoc.add(table)
                        }
                    }
                }
            }

            mDoc.close()

            Toast.makeText(this, "$mFileName.pdf\nis saved to\n$mFilePath", Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

    }

    private fun savePDFFormatSalesCategory() {
        val mDoc = Document()
        val mFileName = "Reporte_de_venta_por_Categoría_" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFileName +".pdf"

        try {
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()
            mDoc.addAuthor("Vicente Cantú")

            val paragraph = Paragraph("Reporte de venta por Categoría")
            paragraph.font.size = 30f
            paragraph.spacingAfter = 30f
            mDoc.add(paragraph)
            mDoc.addTitle("Reporte de venta por Categoría")

            val sales = realm.where<Sale>().findAll()
            for (i in sales.indices) {
                if (sales[i]?.item != null) {
                    var list = List()
                    if (i+1 != sales.count()) {
                        if (sales[i]?.item?.category?.id != sales[i+1]?.item?.category?.id) {

                            val category = realm.where<Category>().equalTo("id", sales[i]?.item?.category?.id).findFirst()
                            list.add(category?.name)
                            mDoc.add(list)

                            val items = category?.items

                            val table = PdfPTable(3)

                            items?.map {
                                table.addCell(PdfPCell(Paragraph(it.name)))
                                table.addCell(PdfPCell(Paragraph("Vendidos: " +
                                        realm.where<Sale>().equalTo("item.id", it.id).count().toString())))
                                table.addCell(PdfPCell(Paragraph("Precio: $" + it.price.toString())))
                            }

                            mDoc.add(table)

                        }
                    } else {
                        if (sales[i-1]?.item?.category?.id != sales[i]?.item?.category?.id) {

                            val category = realm.where<Category>().equalTo("id", sales[i]?.item?.category?.id).findFirst()
                            list.add(category?.name)
                            mDoc.add(list)

                            val items = category?.items

                            val table = PdfPTable(3)

                            items?.map {
                                table.addCell(PdfPCell(Paragraph(it.name)))
                                table.addCell(PdfPCell(Paragraph("Vendidos: " +
                                        realm.where<Sale>().equalTo("item.id", it.id).count().toString())))
                                table.addCell(PdfPCell(Paragraph("Precio: $" + it.price.toString())))
                            }

                            mDoc.add(table)
                        }
                    }
                }
            }

            val sum = sales.sumBy { it.item?.price ?: 0 }

            mDoc.add(Paragraph("Total: $$sum"))

            mDoc.close()

            Toast.makeText(this, "$mFileName.pdf\nis saved to\n$mFilePath", Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePdf() {
        val mDoc = Document()
        val mFileName = "Reporte Pro" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFileName +".pdf"

        try {
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()

            mDoc.addAuthor("Vicente Cantú")

            val sales = realm.where<Sale>().findAll()
            var listItem = ListItem()
            for (sale in sales) {
                listItem.add(sale.item?.name)
            }
            mDoc.add(listItem)

            mDoc.close()

            Toast.makeText(this, "$mFileName.pdf\nis saved to\n$mFilePath", Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            STORAGE_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted, call savePdf() method
                    savePdf()
                }
                else{
                    //permission from popup was denied, show error message
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateToString(format: String?, date: Date): String {
        val pattern = format ?: "dd-MM-yyyy HH:mm:ss"
        val simpleDateFormat = SimpleDateFormat(pattern)
        return simpleDateFormat.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NewApi")
    fun atStartOfDay(date: Date): Date {
        val localDateTime: LocalDateTime = dateToLocalDateTime(date)
        val startOfDay: LocalDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localDateTime.with(LocalTime.MIN)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        return localDateTimeToDate(startOfDay)
    }

    @SuppressLint("NewApi")
    fun atEndOfDay(date: Date): Date {
        val localDateTime: LocalDateTime? = dateToLocalDateTime(date)
        val endOfDay: LocalDateTime? = localDateTime?.with(LocalTime.MAX)
        return localDateTimeToDate(endOfDay ?: LocalDateTime.now())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateToLocalDateTime(date: Date): LocalDateTime {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun localDateTimeToDate(localDateTime: LocalDateTime): Date {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }


}

