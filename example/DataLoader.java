import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class DataLoader {
    protected ArrayList<String> getFileValues(final String path) throws IOException {
        File folder = new File(path);
        ArrayList<String> arrayList = new ArrayList<>();
        for (String file : Objects.requireNonNull(folder.list())) {
            arrayList.add(new String(Files.readAllBytes(Paths.get(path + "/" + file))));
        }
        return arrayList;
    }

    protected List<String> loadData(File file, String suffix) throws IOException {
        List<String> array = new ArrayList<>();
        for (int i = 0; i < Objects.requireNonNull(file.list()).length; ++i) {
            array.addAll(getFileValues(file.getPath() + "/" + Objects.requireNonNull(file.list())[i] + suffix));
        }
        return array;
    }
}
