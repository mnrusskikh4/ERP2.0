package base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class CsvDataProviders {

    @DataProvider(name = "csvReader")
    public static Iterator<Object[]> csvReader(Method method) {
        List<Object[]> list = new ArrayList<Object[]>();
        String pathname = "src" + File.separator + "test" + File.separator + "resources" + File.separator
                + "dataproviders" + File.separator + method.getDeclaringClass().getSimpleName() + File.separator
                + method.getName() + ".csv";
        File file = new File(pathname);
        try {
            CSVReader reader = new CSVReader(new FileReader(file));
            String[] keys = reader.readNext();
            if (keys != null) {
                String[] dataParts;
                while ((dataParts = reader.readNext()) != null) {
                    if (dataParts.length != keys.length) {
                        // Обработка случая, когда количество столбцов не соответствует количеству ключей
                        throw new CsvValidationException("Number of columns doesn't match number of keys.");
                    }
                    Map<String, String> testData = new HashMap<String, String>();
                    for (int i = 0; i < keys.length; i++) {
                        testData.put(keys[i], dataParts[i]);
                    }
                    list.add(new Object[] { testData });
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File " + pathname + " was not found.\n" + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Could not read " + pathname + " file.\n" + e.getMessage());
        } catch (CsvValidationException e) {
            throw new RuntimeException("CSV validation error: " + e.getMessage());
        }

        return list.iterator();
    }
}
