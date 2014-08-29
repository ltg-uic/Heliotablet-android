package ltg.heliotablet_android.data;

import java.sql.Blob;
import java.sql.Timestamp;

public class ReasonImage {

    private Blob imageRaw;
    private String url;
    private Long id;
    private Long reasonId;
    private Timestamp creationTime;

    public Blob getImageRaw() {
        return imageRaw;
    }

    public void setImageRaw(Blob imageRaw) {
        this.imageRaw = imageRaw;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }
}
