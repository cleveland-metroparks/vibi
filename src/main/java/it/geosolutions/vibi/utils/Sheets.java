package it.geosolutions.vibi.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Sheets {

    private final static Map<Character, Integer> charsIndex = new HashMap<>();

    static {
        charsIndex.put('a', 0);
        charsIndex.put('b', 1);
        charsIndex.put('c', 2);
        charsIndex.put('d', 3);
        charsIndex.put('e', 4);
        charsIndex.put('f', 5);
        charsIndex.put('g', 6);
        charsIndex.put('h', 7);
        charsIndex.put('i', 8);
        charsIndex.put('j', 9);
        charsIndex.put('k', 10);
        charsIndex.put('l', 11);
        charsIndex.put('m', 12);
        charsIndex.put('n', 13);
        charsIndex.put('o', 14);
        charsIndex.put('p', 15);
        charsIndex.put('q', 16);
        charsIndex.put('r', 17);
        charsIndex.put('s', 18);
        charsIndex.put('t', 19);
        charsIndex.put('u', 20);
        charsIndex.put('v', 21);
        charsIndex.put('w', 22);
        charsIndex.put('x', 23);
        charsIndex.put('y', 24);
        charsIndex.put('z', 25);
    }

    private Sheets() {
    }

    public static int getIndex(String columnIndex) {
        char[] indexParts = columnIndex.toLowerCase().toCharArray();
        int index = charsIndex.get(indexParts[indexParts.length - 1]);
        for (int i = indexParts.length - 2; i >= 0; i--) {
            index += (charsIndex.get(indexParts[i]) + 1) * 26;
        }
        return index;
    }

    public static String cellToString(Cell cell) {
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
    }

    public static List<Cell> getCells(Sheet sheet, int rowIndex, String startColumn, String endColumn) {
        int startColumnIndex = getIndex(startColumn);
        int endColumnIndex = getIndex(endColumn);
        List<Cell> cells = new ArrayList<>(endColumnIndex - startColumnIndex + 1);
        Row row = sheet.getRow(rowIndex - 1);
        for (int i = startColumnIndex; i <= endColumnIndex; i++) {
            cells.add(row.getCell(i, Row.RETURN_BLANK_AS_NULL));
        }
        return cells;
    }

    public static abstract class WorkOnSheet {

        public WorkOnSheet(String workBookPath, String sheetName) {
            try (InputStream input = new FileInputStream(workBookPath);
                 HSSFWorkbook workBook = new HSSFWorkbook(new POIFSFileSystem(input))) {
                Sheet sheet = workBook.getSheet(sheetName);
                Validations.checkNotNull(sheet, "Sheet '%d' not found.", sheetName);
                doWork(sheet);
            } catch (Exception exception) {
                throw new RuntimeException(String.format("Error processing sheet '%s' of work book '%s'.",
                        sheetName, workBookPath), exception);
            }
        }

        public abstract void doWork(Sheet sheet);
    }
}