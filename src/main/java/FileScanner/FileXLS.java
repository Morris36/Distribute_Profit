package FileScanner;

import App.Application;
import Backpack.Agent;
import Backpack.Project;
import DataBank.Bank;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class FileXLS {
    public ArrayList<Agent> readAgent(String path) throws FileNotFoundException, IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);
        if (sheet.getLastRowNum() == 0 && sheet.getRow(0) == null) {
            workbook.close();
            return null;
        } else {
            ArrayList<Agent> agentLinkedList = new ArrayList<>();
            int count = 0;
            while (sheet.getRow(4).getCell(count) != null) {
                agentLinkedList.add(new Agent(sheet.getRow(4).getCell(count).getStringCellValue(), sheet.getRow(5).getCell(count).getNumericCellValue()));
                count++;
            }
            count = 0;
            while (sheet.getRow(0).getCell(count) != null) {
                agentLinkedList.get(checkAgent(sheet.getRow(1).getCell(count).
                        getStringCellValue(), agentLinkedList)).addProject(new Project(sheet.getRow(0).getCell(count).getStringCellValue(),
                        sheet.getRow(2).getCell(count).getNumericCellValue(),
                        sheet.getRow(3).getCell(count).getNumericCellValue(), sheet.getRow(1).getCell(count).
                        getStringCellValue()));
                count++;
            }

            workbook.close();
            fileInputStream.close();
            return agentLinkedList;
        }
    }
    public void save(String patch) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(patch));
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);
        int numberOfRows = sheet.getPhysicalNumberOfRows();
        if(numberOfRows > 0) {
            for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                if(sheet.getRow(i) != null) {
                    sheet.removeRow( sheet.getRow(i));
                } else {
                    System.out.println("Info: clean sheet='" + sheet.getSheetName() + "' ... skip line: " + i);
                }
            }
        } else {
            System.out.println("Info: clean sheet='" + sheet.getSheetName() + "' ... is empty");
        }
        int i = 0, n = 0;
        Row row = sheet.createRow(0);
        Row row1 = sheet.createRow(1);
        Row row2 = sheet.createRow(2);
        Row row3 = sheet.createRow(3);
        Row row4 = sheet.createRow(4);
        Row row5 = sheet.createRow(5);
        for (Agent agent:
                Bank.getAgents()
             ) {
            Cell cell4 = row4.createCell(n, CellType.STRING);
            cell4.setCellValue(agent.getName());
            Cell cell5 = row5.createCell(n, CellType.NUMERIC);
            cell5.setCellValue(agent.getBudget());
            n++;
            for (Project project : agent.getProjects()){
                Cell cell = row.createCell(i, CellType.STRING);
                cell.setCellValue(project.getName());
                Cell cell1 = row1.createCell(i, CellType.STRING);
                cell1.setCellValue(project.getAgent());
                Cell cell2 = row2.createCell(i, CellType.NUMERIC);
                cell2.setCellValue(project.getExpenses());
                Cell cell3 = row3.createCell(i, CellType.NUMERIC);
                cell3.setCellValue(project.getProfit());
                i++;
            }

        }
        FileOutputStream outFile = new FileOutputStream(new File(patch));
        workbook.write(outFile);
        fileInputStream.close();
        outFile.close();
        workbook.close();
    }
    private static int checkAgent(String name, ArrayList<Agent> agents) {
        for (int i = 0; i < agents.size(); i++) {
            if (Objects.equals(name, agents.get(i).getName()))
                return i;
        }
        return -1;
    }
    public void saveNullFile(String patch) throws IOException {
        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet("Data");
        try (FileOutputStream out = new FileOutputStream(new File(patch))) {
            book.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        book.close();
    }


}
