package com.orbits.ticketmodule.helper

import android.os.Environment
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object FileConfig {

    var image_FileNames: Array<String?>? = null
    var image_FilePaths: Array<String?>? = null

    fun createExcelFile(filePath: String) {
        val workbook: Workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("Sheet 1")

        // Example of setting data
        val rowOne = sheet.createRow(0)
        rowOne.createCell(0).setCellValue(Constants.TICKET_TILES_COLOR)
        rowOne.createCell(1).setCellValue("#E8F0FE")

        // Example of setting data
        val rowTwo = sheet.createRow(2)
        rowTwo.createCell(0).setCellValue(Constants.TICKET_TILES_TEXT_COLOR)
        rowTwo.createCell(1).setCellValue("#295F98")

        val rowThree = sheet.createRow(4)
        rowThree.createCell(0).setCellValue(Constants.TICKET_CONFIRM_COLOR)
        rowThree.createCell(1).setCellValue("#F7B5CA")

        val rowFour = sheet.createRow(6)
        rowFour.createCell(0).setCellValue(Constants.TICKET_TILES_CURVE)
        rowFour.createCell(1).setCellValue("20")

        // Write the output to the file
        FileOutputStream(filePath).use { outputStream ->
            workbook.write(outputStream)
        }

        workbook.close()
    }

    fun readExcelFile(filePath: String): Map<String, String> {
        val colors = mutableMapOf<String, String>()

        FileInputStream(filePath).use { inputStream ->
            val workbook: Workbook = WorkbookFactory.create(inputStream)
            val sheet: Sheet = workbook.getSheetAt(0)

            // Use iterator to ensure all rows are handled, even if they have no physical data
            for (row in sheet) {
                // Ensure the row has at least two cells
                if (row.lastCellNum >= 2) {
                    val keyCell = row.getCell(0)
                    val valueCell = row.getCell(1)

                    if (keyCell != null && valueCell != null) {
                        // Get the key as a string
                        val key = when (keyCell.cellType) {
                            CellType.STRING -> keyCell.stringCellValue
                            CellType.NUMERIC -> keyCell.numericCellValue.toString() // Convert numeric to string
                            else -> "" // Handle other cell types if necessary
                        }

                        // Get the value as a string
                        val value = when (valueCell.cellType) {
                            CellType.STRING -> valueCell.stringCellValue
                            CellType.NUMERIC -> valueCell.numericCellValue.toString() // Convert numeric to string
                            else -> "" // Handle other cell types if necessary
                        }

                        // Ensure key and value are not empty before adding them to the map
                        if (key.isNotEmpty() && value.isNotEmpty()) {
                            colors[key] = value
                        }
                    }
                }
            }

            workbook.close()
        }

        return colors
    }

    fun readImageFile() {
        try {
            val listFile: Array<File>
            image_FileNames = null
            image_FilePaths = null

            val file = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Ticket_Config/Company_Images"
            )
            if (file.isDirectory) {
                listFile = file.listFiles()
                // List file path for Images
                image_FilePaths = arrayOfNulls(listFile.size)
                // List file path for Image file names
                image_FileNames = arrayOfNulls(listFile.size)

                for (i in listFile.indices) {
                    // Get the path
                    image_FilePaths!![i] = listFile[i].absolutePath
                    // Get the name
                    image_FileNames!![i] = listFile[i].name
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}