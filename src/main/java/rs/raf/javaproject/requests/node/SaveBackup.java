package rs.raf.javaproject.requests.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import rs.raf.javaproject.model.BackupInfo;
import rs.raf.javaproject.requests.ARequest;

public class SaveBackup extends ARequest<Boolean> {

    public SaveBackup(String url, BackupInfo backupInfo) {
        super(url);

        try {
            var body = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(backupInfo);

            RequestBody requestBody = RequestBody.create(JSON, body);

            request = new Request.Builder()
                    .url(url)
                    .put(requestBody)
                    .build();

            returnClass =  new TypeReference<Boolean>() {};
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
