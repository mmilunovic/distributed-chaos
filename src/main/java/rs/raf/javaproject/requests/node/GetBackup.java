package rs.raf.javaproject.requests.node;

import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import rs.raf.javaproject.model.BackupInfo;
import rs.raf.javaproject.model.Point;
import rs.raf.javaproject.requests.ARequest;

import java.util.Collection;

public class GetBackup extends ARequest<BackupInfo> {

    public GetBackup(String url) {
        super(url);
        System.out.println(url);
        request = new Request.Builder()
                .url(url)
                .get()
                .build();

        returnClass = new TypeReference<BackupInfo>() {};
    }
}

